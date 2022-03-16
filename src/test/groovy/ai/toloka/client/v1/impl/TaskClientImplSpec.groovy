/*
 * Copyright 2021 YANDEX LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.toloka.client.v1.impl

import ai.toloka.client.v1.BatchCreateResult
import ai.toloka.client.v1.FieldValidationError
import ai.toloka.client.v1.task.*
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient
import org.unitils.reflectionassert.ReflectionComparatorMode

import static AbstractClientSpec.parseDate
import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class TaskClientImplSpec extends AbstractClientSpec {

    def "createTask"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks').withMethod('POST')
                        .withBody(json(new JsonBuilder(task_map()).toString())), once())
                .respond(response(new JsonBuilder(task_map_with_readonly()).toString()).withStatusCode(201))

        when:
        def result = factory.taskClient.createTask(task())

        then:
        matches result.result, task_with_readonly(), ReflectionComparatorMode.LENIENT_ORDER
    }

    def "createTaskSuites"() {
        setup:
        def tasks_map = [task_map(), [pool_id: '21', input_values: {}]]

        def result_map = [
                items            : ['0': task_map_with_readonly()],
                validation_errors: [
                        '1': ['input_values.image': [code: 'VALUE_REQUIRED', message: 'May not be null']]
                ]
        ]

        def tasks = [task(), new Task('21', [image: null])]

        def expected_result = new BatchCreateResult<>(
                items: [0: task_with_readonly()],
                validationsErrors: [
                        1: ['input_values.image': new FieldValidationError('VALUE_REQUIRED', 'May not be null', [])]
                ]
        )

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks').withMethod('POST')
                        .withQueryStringParameters(async_mode: ['false'], allow_defaults: ['true'])
                        .withBody(json(new JsonBuilder(tasks_map) as String)), once())
                .respond(response(new JsonBuilder(result_map) as String).withStatusCode(201))

        when:
        def result = factory.taskClient.createTasks(
                tasks, new TaskCreateRequestParameters().with { allowDefaults = true; it })

        then:
        matches result, expected_result
    }

    def "createTaskSuitesAsync"() {
        setup:
        def tasks_map = [
                [pool_id: '21', input_values: [image: 'http://images.com/1.png']],
                [pool_id: '21', input_values: [image: 'http://images.com/2.png']]
        ]

        def operationUUID = UUID.randomUUID()

        def operation_map = [
                id        : operationUUID as String,
                type      : 'TASK.BATCH_CREATE',
                status    : 'SUCCESS',
                submitted : '2016-10-10T20:33:01',
                started   : '2016-10-10T23:33:00',
                parameters: [skip_invalid_items: true, allow_defaults: true, open_pool: true],
                details   : [items_count: 2]
        ]


        def tasks = [
                new Task('21', [image: 'http://images.com/1.png']), new Task('21', [image: 'http://images.com/2.png'])
        ]

        def operation = new TaskCreateBatchOperation(
                id: operationUUID as String,
                type: ai.toloka.client.v1.operation.OperationType.TASK_BATCH_CREATE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-10-10 20:33:01'),
                started: parseDate('2016-10-10 23:33:00'),
                parameters: new TaskCreateBatchOperation.Parameters(
                        skipInvalidItems: true, allowDefaults: true, openPool: true),
                details: m.createObjectNode().put('items_count', 2),
                operationClient: factory.operationClient
        )


        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks').withMethod('POST')
                        .withQueryStringParameters(
                                operation_id: [operationUUID as String],
                                skip_invalid_items: ['true'],
                                allow_defaults: ['true'],
                                open_pool: ['true'],
                                async_mode: ['true']
                        )
                        .withBody(json(new JsonBuilder(tasks_map) as String)), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.taskClient.createTasksAsync(
                tasks.iterator(),
                new TaskCreateRequestParameters(
                        operationId: operationUUID, skipInvalidItems: true, allowDefaults: true, openPool: true)
        ).waitToComplete()

        then:
        matches result, operation
    }

    def "findTasks"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks').withQueryStringParameters(
                        pool_id: ['21'],
                        created_gt: ['2001-01-01T12:00:00'],
                        overlap_gte: ['42'],
                        sort: ['id,-created'], limit: ['20']), once()
                )
                .respond(response(new JsonBuilder([items: [task_map_with_readonly()], has_more: false]) as String))

        and:
        def request = TaskSearchRequest.make()
                .filter().byPoolId('21')
                .and()
                .range().byCreated(parseDate('2001-01-01 12:00:00')).gt()
                .and()
                .range().byOverlap(42).gte().and()
                .sort().byId().asc().byCreated().desc()
                .and()
                .limit(20)
                .done()

        when:
        def result = factory.taskClient.findTasks(request)

        then:
        !result.hasMore
        matches result.items.first(), task_with_readonly()
    }

    def "getTask"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1'), once())
                .respond(response(new JsonBuilder(task_map_with_readonly()) as String))

        when:
        def result = factory.taskClient.getTask('task-1')

        then:
        matches result, task_with_readonly()
    }

    def "patchTask"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1').withMethod('PATCH')
                        .withQueryStringParameters(open_pool: ['true'])
                        .withBody(new JsonBuilder([overlap: 10]) as String), once())
                .respond(response(new JsonBuilder(task_map_with_readonly().with { it.overlap = 10; it }) as String))

        when:
        def result = factory.taskClient.patchTask(
                'task-1', new TaskPatch(overlap: 10), new TaskPatchRequestParameters(openPool: true))

        then:
        matches result.result, task_with_readonly().with { overlap = 10; it }
    }

    def "patchTask; update baseline solutions"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1').withMethod('PATCH')
                        .withBody(new JsonBuilder([baseline_solutions: [[output_values: [color: 'green'], confidence_weight: 4.2]]]) as String), once())
                .respond(response(new JsonBuilder(task_map_with_readonly().with {
                    it.baseline_solutions = [
                        [
                                output_values     : [
                                        color: 'green'
                                ],
                                confidence_weight: 4.20
                        ]
                    ]
                    it
                }) as String))

        when:
        def result = factory.taskClient.patchTask(
                'task-1',
                new TaskPatch(baselineSolutions: [new BaselineSolution([color: 'green'], 4.2)]))

        then:
        matches result.result, task_with_readonly().with {
            baselineSolutions = [new BaselineSolution([color: 'green'], 4.2)]
            it
        }
    }

    def "patchTask; update known solutions"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1').withMethod('PATCH')
                        .withBody(new JsonBuilder([known_solutions: [[output_values: [color: 'green'], correctness_weight: 0.0]]]) as String), once())
                .respond(response(new JsonBuilder(task_map_with_readonly().with {
                    it.known_solutions = [
                            [
                                    output_values     : [
                                            color: 'green'
                                    ],
                                    correctness_weight: 0.0
                            ]
                    ]
                    it
                }) as String))

        when:
        def result = factory.taskClient.patchTask(
                'task-1',
                new TaskPatch(knownSolutions: [new KnownSolution([color: 'green'], 0.0)]))

        then:
        matches result.result, task_with_readonly().with {
            knownSolutions = [new KnownSolution([color: 'green'], 0.0)]
            it
        }
    }

    def "patchTask; update message on unknown solution"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1').withMethod('PATCH')
                        .withBody(new JsonBuilder([message_on_unknown_solution: 'useful message']) as String), once())
                .respond(response(new JsonBuilder(task_map_with_readonly().with {
                    it.message_on_unknown_solution = 'useful message'
                    it
                }) as String))

        when:
        def result = factory.taskClient.patchTask(
                'task-1',
                new TaskPatch(messageOnUnknownSolution: 'useful message')
        )

        then:
        matches result.result, task_with_readonly().with {
            messageOnUnknownSolution = 'useful message'
            it
        }
    }

    def "patchTask; set overlap and update known solutions and update message on unknown solution"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1').withMethod('PATCH')
                        .withBody(new JsonBuilder([
                                overlap: 10,
                                known_solutions: [[output_values: [color: 'green'], correctness_weight: 0.0]],
                                message_on_unknown_solution: 'useful message'
                        ]) as String), once())
                .respond(response(new JsonBuilder(task_map_with_readonly().with {
                    it.overlap = 10
                    it.known_solutions = [
                            [
                                    output_values     : [
                                            color: 'green'
                                    ],
                                    correctness_weight: 0.0
                            ]
                    ]
                    it.message_on_unknown_solution = 'useful message'
                    it
                }) as String))

        when:
        def result = factory.taskClient.patchTask(
                'task-1',
                new TaskPatch(
                        overlap: 10,
                        knownSolutions: [new KnownSolution([color: 'green'], 0.0)],
                        messageOnUnknownSolution: 'useful message'
                )
        )

        then:
        matches result.result, task_with_readonly().with {
            overlap = 10
            knownSolutions = [new KnownSolution([color: 'green'], 0.0)]
            messageOnUnknownSolution = 'useful message'
            it
        }
    }

    def 'setTaskOverlapOrMin'() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1/set-overlap-or-min').withMethod('PATCH')
                        .withBody(new JsonBuilder([overlap: 10]) as String), once())
                .respond(response(new JsonBuilder(task_map_with_readonly().with { it.overlap = 12; it }) as String))

        when:
        def result = factory.taskClient.setOverlapOrMin('task-1', new TaskOverlapPatch(overlap: 10))

        then:
        matches result.result, task_with_readonly().with { overlap = 12; it }
    }

    def 'setTaskOverlapOrMinInfiniteOverlap'() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/tasks/task-1/set-overlap-or-min').withMethod('PATCH')
                        .withBody(new JsonBuilder([infinite_overlap: true]) as String), once())
                .respond(response(new JsonBuilder(task_map_with_readonly().with { it.overlap = null; it }) as String))

        when:
        def result = factory.taskClient.setOverlapOrMin('task-1', new TaskOverlapPatch(infiniteOverlap: true))

        then:
        matches result.result, task_with_readonly().with { overlap = null; it }
    }

    static def task_map() {
        [
                pool_id                    : '21',
                input_values               : [
                        image: 'http://images.com/1.png'
                ],
                known_solutions            : [
                        [
                                output_values     : [
                                        color: 'white'
                                ],
                                correctness_weight: 1.0
                        ],
                        [
                                output_values     : [
                                        color: 'gray'
                                ],
                                correctness_weight: 0.71
                        ]
                ],
                message_on_unknown_solution: 'Main color is white',
                baseline_solutions         : [
                        [
                                output_values     : [
                                        color: 'white'
                                ],
                                confidence_weight: 1.0
                        ],
                        [
                                output_values     : [
                                        color: 'gray'
                                ],
                                confidence_weight: 0.71
                        ]
                ],
                overlap                    : 3,
                remaining_overlap          : 3,
                unavailable_for            : ['user-1id'],
                reserved_for               : ['user-2id'],
                traits_all_of              : ['trait-1'],
                traits_any_of              : ['trait-2'],
                traits_none_of_any         : ['trait-3']
        ]
    }

    static def task_map_with_readonly() {
        [
                *      : task_map(),
                created: '2016-10-09T11:42:01'
        ]
    }

    static Task task() {
        new Task(
                '21',
                [image: 'http://images.com/1.png'],
                [new KnownSolution([color: 'white'], 1.0), new KnownSolution([color: 'gray'], 0.71)],
                'Main color is white'
        ).with {
            baselineSolutions = [
                    new BaselineSolution([color: 'white'], 1.0),
                    new BaselineSolution([color: 'gray'], 0.71),
            ]
            overlap = 3
            remainingOverlap = 3
            unavailableFor = ['user-1id'] as Set
            reservedFor = ['user-2id'] as Set
            traitsAllOf = ['trait-1'] as Set
            traitsAnyOf = ['trait-2'] as Set
            traitsNoneOfAny = ['trait-3'] as Set
            it
        }
    }

    static Task task_with_readonly() {
        task().with {
            created = parseDate('2016-10-09 11:42:01')
            it
        }
    }
}
