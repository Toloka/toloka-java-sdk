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

import ai.toloka.client.v1.tasksuite.TaskSuite
import ai.toloka.client.v1.tasksuite.TaskSuiteCreateBatchOperation
import ai.toloka.client.v1.tasksuite.TaskSuiteCreateRequestParameters
import ai.toloka.client.v1.tasksuite.TaskSuiteOverlapPatch
import ai.toloka.client.v1.tasksuite.TaskSuitePatch
import ai.toloka.client.v1.tasksuite.TaskSuitePatchRequestParameters
import ai.toloka.client.v1.tasksuite.TaskSuiteSearchRequest
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER

class TaskSuiteClientImplSpec extends AbstractClientSpec {

    def "createTaskSuite"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites').withMethod('POST')
                        .withBody(json(new JsonBuilder(task_suite_map()).toString())), once())
                .respond(response(new JsonBuilder(task_suite_map_with_readonly()).toString()).withStatusCode(201))

        when:
        def result = factory.taskSuiteClient.createTaskSuite(task_suite())

        then:
        matches result.result, task_suite_with_readonly(), LENIENT_ORDER
    }

    def "createTaskSuites"() {
        setup:
        def task_suites_map = [
                [
                        pool_id: '21',
                        tasks  : [
                                [
                                        input_values: [
                                                image: 'http://images.com/1.png'
                                        ]
                                ]
                        ]
                ],
                [
                        pool_id: '21',
                        tasks  : [
                                [
                                        input_values: {}
                                ]
                        ]
                ]
        ]

        def result_map = [
                items            : [
                        '0': [
                                id     : 'task-suite-i1d',
                                pool_id: '21',
                                tasks  : [
                                        [
                                                input_values: [
                                                        image: 'http://images.com/1.png'
                                                ]
                                        ]
                                ],
                                mixed  : false,
                                created: '2016-07-09T14:39:00'
                        ]
                ],
                validation_errors: [
                        '1': [
                                'tasks.0.input_values.image': [
                                        code   : 'VALUE_REQUIRED',
                                        message: 'May not be null'
                                ]
                        ]
                ]
        ]

        def task_suites = [
                new TaskSuite('21', [new ai.toloka.client.v1.task.BaseTask([image: 'http://images.com/1.png'])]),
                new TaskSuite('21', [new ai.toloka.client.v1.task.BaseTask([image: null])])
        ]

        def expected_result = new ai.toloka.client.v1.BatchCreateResult(
                items: [
                        0: new TaskSuite('21', [new ai.toloka.client.v1.task.BaseTask([image: 'http://images.com/1.png'])])
                                .with {
                                    id = 'task-suite-i1d'
                                    mixed = false
                                    created = parseDate('2016-07-09 14:39:00')
                                    it
                                }
                ],
                validationsErrors: [
                        1: ['tasks.0.input_values.image':
                                    new ai.toloka.client.v1.FieldValidationError('VALUE_REQUIRED', 'May not be null', [])]
                ]
        )

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites').withMethod('POST')
                        .withQueryStringParameters([async_mode: ['false']])
                        .withBody(json(new JsonBuilder(task_suites_map) as String)), once())
                .respond(response(new JsonBuilder(result_map) as String).withStatusCode(201))

        when:
        def result = factory.taskSuiteClient.createTaskSuites(task_suites)

        then:
        matches result, expected_result
    }

    def "createTaskSuitesAsync"() {
        setup:
        def task_suites_map = [
                [
                        pool_id: '21',
                        tasks  : [
                                [
                                        input_values: [
                                                image: 'http://images.com/1.png'
                                        ]
                                ]
                        ]
                ],
                [
                        pool_id: '21',
                        tasks  : [
                                [
                                        input_values: [
                                                image: 'http://images.com/2.png'
                                        ]
                                ]
                        ]
                ]
        ]

        def operation_map = [
                id        : 'operation-i1d',
                type      : 'TASK_SUITE.BATCH_CREATE',
                status    : 'SUCCESS',
                submitted : '2015-12-13T23:32:01',
                started   : '2015-12-13T23:33:00',
                parameters: [
                        skip_invalid_items: true,
                        allow_defaults    : true,
                        open_pool         : true
                ],
                details   : [
                        items_count: 2
                ]
        ]


        def task_suites = [
                new TaskSuite('21', [new ai.toloka.client.v1.task.BaseTask([image: 'http://images.com/1.png'])]),
                new TaskSuite('21', [new ai.toloka.client.v1.task.BaseTask([image: 'http://images.com/2.png'])])
        ]

        def operation = new TaskSuiteCreateBatchOperation(
                id: 'operation-i1d',
                type: ai.toloka.client.v1.operation.OperationType.TASK_SUITE_BATCH_CREATE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2015-12-13 23:32:01'),
                started: parseDate('2015-12-13 23:33:00'),
                parameters: new TaskSuiteCreateBatchOperation.Parameters(
                        skipInvalidItems: true,
                        allowDefaults: true,
                        openPool: true
                ),
                details: m.createObjectNode().put('items_count', 2),
                operationClient: factory.operationClient
        )


        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites').withMethod('POST')
                        .withQueryStringParameters([
                                skip_invalid_items: ['true'],
                                allow_defaults    : ['true'],
                                open_pool         : ['true'],
                                async_mode        : ['true']
                        ])
                        .withBody(json(new JsonBuilder(task_suites_map).toString())), once())
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(202))

        when:
        def result = factory.taskSuiteClient.createTaskSuitesAsync(
                task_suites.iterator(),
                new TaskSuiteCreateRequestParameters(skipInvalidItems: true, allowDefaults: true, openPool: true)
        ).waitToComplete()

        then:
        matches result, operation
    }

    def "findTaskSuites"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites').withQueryStringParameters(
                        pool_id: ['21'],
                        created_lte: ['2020-02-01T00:00:00'],
                        overlap_gte: ['42'],
                        sort: ['-created'], limit: ['100']), once()
                )
                .respond(
                        response(new JsonBuilder([items: [task_suite_map_with_readonly()], has_more: false]) as String)
                )

        and:
        def request = TaskSuiteSearchRequest.make()
                .filter().byPoolId('21').and()
                .range().byCreated(parseDate('2020-02-01 00:00:00')).lte().and()
                .range().byOverlap(42).gte().and()
                .sort().byCreated().desc().and()
                .limit(100)
                .done()

        when:
        def result = factory.taskSuiteClient.findTaskSuites(request)

        then:
        matches result.items.first(), task_suite_with_readonly(), LENIENT_ORDER
    }

    def "getTaskSuite"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites/task-suite-i1d'), once())
                .respond(response(new JsonBuilder(task_suite_map_with_readonly()) as String))

        when:
        def result = factory.taskSuiteClient.getTaskSuite('task-suite-i1d')

        then:
        matches result, task_suite_with_readonly(), LENIENT_ORDER
    }

    def "patchTaskSuite"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites/task-suite-i1d').withMethod('PATCH')
                        .withBody(new JsonBuilder([overlap: 12, infinite_overlap: false, issuing_order_override: 10.4]) as String)
                )
                .respond(response(new JsonBuilder(task_suite_map_with_readonly() + [overlap: 12, issuing_order_override: 10.4]) as String))

        when:
        def result = factory.taskSuiteClient.patchTaskSuite('task-suite-i1d', new TaskSuitePatch(overlap: 12, issuingOrderOverride: 10.4))

        then:
        matches result.result, task_suite_with_readonly().with {
            overlap = 12; issuingOrderOverride = 10.4
            it
        }, LENIENT_ORDER
    }

    def "patchTaskSuite; with parameters"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites/task-suite-i1d').withMethod('PATCH')
                        .withQueryStringParameters(open_pool: ['true'])
                        .withBody(new JsonBuilder([overlap: 12, infinite_overlap: false]) as String)
                )
                .respond(response(new JsonBuilder(task_suite_map_with_readonly() + [overlap: 12]) as String))

        def parameters = new TaskSuitePatchRequestParameters()
        parameters.setOpenPool(true)
        when:
        def result = factory.taskSuiteClient.patchTaskSuite('task-suite-i1d',
                new TaskSuitePatch(overlap: 12),
                parameters
        )

        then:
        matches result.result, task_suite_with_readonly().with { overlap = 12; it }, LENIENT_ORDER
    }

    def 'setTaskSuiteOverlapOrMin'() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/task-suites/task-suite-i1d/set-overlap-or-min').withMethod('PATCH')
                        .withBody(new JsonBuilder([overlap: 10]) as String), once())
                .respond(response(new JsonBuilder(task_suite_map_with_readonly() + [overlap: 12]) as String))

        when:
        def result = factory.taskSuiteClient.setOverlapOrMin('task-suite-i1d', new TaskSuiteOverlapPatch(overlap: 10))

        then:
        matches result.result, task_suite_with_readonly().with { overlap = 12; it }, LENIENT_ORDER
    }

    def task_suite_map() {
        [
                pool_id               : '21',
                tasks                 : [
                        [
                                input_values: [
                                        image: 'http://images.com/1.png'
                                ]
                        ],
                        [
                                input_values               : [
                                        image: 'http://images.com/2.png'
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
                                message_on_unknown_solution: 'Main color is white'
                        ]
                ],
                overlap               : 5,
                remaining_overlap     : 5,
                issuing_order_override: 10.3,
                unavailable_for       : ['tlk-user-i1d', 'tlk-user-i2d'],
                reserved_for          : ['tlk-user-i3d', 'tlk-user-i4d'],
                traits_all_of         : ['trait-1'],
                traits_any_of         : ['trait-2'],
                traits_none_of_any    : ['trait-3'],
                longitude             : 136.22,
                latitude              : 58.588
        ]
    }

    def task_suite_map_with_readonly() {
        task_suite_map() + [
                id        : 'task-suite-i1d',
                mixed     : false,
                automerged: true,
                created   : '2015-12-13T23:57:12'
        ]
    }

    def task_suite() {
        new TaskSuite(
                '21',
                [
                        new ai.toloka.client.v1.task.BaseTask([image: 'http://images.com/1.png']),
                        new ai.toloka.client.v1.task.BaseTask(
                                [image: 'http://images.com/2.png'],
                                [new ai.toloka.client.v1.task.KnownSolution([color: 'white'], 1), new ai.toloka.client.v1.task.KnownSolution([color: 'gray'], 0.71)],
                                'Main color is white'
                        )
                ]).with {
            overlap = 5
            remainingOverlap = 5
            issuingOrderOverride = 10.3
            unavailableFor = ['tlk-user-i1d', 'tlk-user-i2d']
            reservedFor = ['tlk-user-i3d', 'tlk-user-i4d']
            traitsAllOf = ['trait-1'] as Set
            traitsAnyOf = ['trait-2'] as Set
            traitsNoneOfAny = ['trait-3'] as Set
            longitude = 136.22
            latitude = 58.588
            it
        }
    }

    def task_suite_with_readonly() {
        task_suite().with {
            automerged = true
            id = 'task-suite-i1d'
            mixed = false
            created = parseDate('2015-12-13 23:57:12')
            it
        }
    }
}
