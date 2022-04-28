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

import ai.toloka.client.v1.operation.*
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static org.mockserver.matchers.Times.exactly
import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class OperationClientImplSpec extends AbstractClientSpec {

    def "findOperations"() {
        setup:
        def pool_operation_map = [
                id        : 'open-pool-op1id',
                type      : 'POOL.OPEN',
                status    : 'RUNNING',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                finished  : '2016-03-07T15:48:03',
                parameters: [pool_id: "21"]
        ]
        def project_operation_map = [
                id        : 'archive-project-op-1',
                type      : 'PROJECT.ARCHIVE',
                status    : 'SUCCESS',
                submitted : '2016-10-21T15:37:00',
                started   : '2016-10-21T15:37:01',
                finished  : '2016-10-21T15:37:02',
                parameters: [project_id: '10']
        ]

        def known_solutions_generate_operation_map = [
                id        : 'known-solutions-generate-op-1',
                type      : 'KNOWN_SOLUTIONS.GENERATE',
                status    : 'SUCCESS',
                submitted : '2022-05-11T02:32:00',
                started   : '2022-05-11T02:32:01',
                finished  : '2022-05-11T02:32:02',
                parameters: [
                        pool_id    : '10',
                        poolName   : 'Pool 1',
                        project_id : '12',
                        projectName: 'project',
                        secret     : '123abc',
                        requesterId: 'qwe456'
                ]
        ]

        def unknown_operation_map = [
                id        : 'unknown-1',
                type      : 'UNKNOWN_TYPE',
                status    : 'PENDING',
                submitted : '2016-10-23T10:45:00',
                parameters: [unknown_property: 'test']
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations').withQueryStringParameters(
                type: ['POOL.CLOSE'],
                status: ['RUNNING'],
                finished_gt: ['2005-01-01T12:00:00'],
                sort: ['-finished,id'],
                limit: ['45']), once()
        )
                .respond(response(
                new JsonBuilder([
                        items   : [pool_operation_map, project_operation_map,
                                   known_solutions_generate_operation_map, unknown_operation_map],
                        has_more: false]
                ) as String)
        )

        when:
        def request = OperationSearchRequest.make()
                .filter().byType(OperationType.POOL_CLOSE).byStatus(OperationStatus.RUNNING).and()
                .range().byFinished(parseDate('2005-01-01 12:00:00')).gt().and()
                .sort().byFinished().desc().byId().asc().and()
                .limit(45)
                .done()
        def result = factory.operationClient.findOperations(request)

        then:
        matches result.items[0], new ai.toloka.client.v1.pool.PoolOpenOperation(
                id: 'open-pool-op1id',
                type: OperationType.POOL_OPEN,
                status: OperationStatus.RUNNING,
                submitted: parseDate('2016-03-07 15:47:00'),
                started: parseDate('2016-03-07 15:47:21'),
                finished: parseDate('2016-03-07 15:48:03'),
                parameters: new ai.toloka.client.v1.pool.PoolOpenOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )

        matches result.items[1], new ai.toloka.client.v1.project.ProjectArchiveOperation(
                id: 'archive-project-op-1',
                type: OperationType.PROJECT_ARCHIVE,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-10-21 15:37:00'),
                started: parseDate('2016-10-21 15:37:01'),
                finished: parseDate('2016-10-21 15:37:02'),
                parameters: new ai.toloka.client.v1.project.ProjectArchiveOperation.Parameters(projectId: '10'),
                operationClient: factory.operationClient
        )

        matches result.items[2], new ai.toloka.client.v1.task.KnownSolutionsGenerateOperation(
                id : 'known-solutions-generate-op-1',
                type : OperationType.KNOWN_SOLUTIONS_GENERATE,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2022-05-11 02:32:00'),
                started: parseDate('2022-05-11 02:32:01'),
                finished: parseDate('2022-05-11 2:32:02'),
                parameters: new ai.toloka.client.v1.task.KnownSolutionsGenerateOperation.Parameters(
                        poolId: '10',
                        poolName: 'Pool 1',
                        projectId: '12',
                        projectName: 'project',
                        secret: '123abc',
                        requesterId: 'qwe456'
                ),
                operationClient: factory.operationClient
        )

        matches result.items[3], new Operation.UnknownOperation(
                id: 'unknown-1',
                type: new OperationType('UNKNOWN_TYPE'),
                status: OperationStatus.PENDING,
                submitted: parseDate('2016-10-23 10:45:00'),
                parameters: [unknown_property: 'test'],
                operationClient: factory.operationClient
        )
    }

    def "getOperation"() {
        setup:
        def operation_map = [
                id        : 'open-pool-op1id',
                type      : 'POOL.OPEN',
                status    : 'RUNNING',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                parameters: [
                        pool_id: "21"
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/open-pool-op1id'), exactly(2))
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(200))

        def completeResponseBody =
                new JsonBuilder(operation_map + [status: 'SUCCESS', finished: '2016-03-07T15:48:03']).toString()
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/open-pool-op1id'), once())
                .respond(response(completeResponseBody).withStatusCode(200))

        when:
        def result = factory.operationClient.getOperation('open-pool-op1id').waitToComplete()

        then:
        matches result, new ai.toloka.client.v1.pool.PoolOpenOperation(
                id: 'open-pool-op1id',
                type: OperationType.POOL_OPEN,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-03-07 15:47:00'),
                started: parseDate('2016-03-07 15:47:21'),
                finished: parseDate('2016-03-07 15:48:03'),
                parameters: new ai.toloka.client.v1.pool.PoolOpenOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    def "getOperation; KNOWN_SOLUTIONS.GENERATE"() {
        setup:
        def operation_map = [
                id        : 'known-solutions-generate-op-1',
                type      : 'KNOWN_SOLUTIONS.GENERATE',
                status    : 'RUNNING',
                submitted : '2022-05-11T02:32:00',
                started   : '2022-05-11T02:32:01',
                parameters: [
                        pool_id    : '10',
                        poolName   : 'Pool 1',
                        project_id : '12',
                        projectName: 'project',
                        secret     : '123abc',
                        requesterId: 'qwe456'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/known-solutions-generate-op-1'), exactly(2))
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(200))

        def completeResponseBody =
                new JsonBuilder(operation_map + [status: 'SUCCESS', finished: '2022-05-11T02:32:02']).toString()
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/known-solutions-generate-op-1'), once())
                .respond(response(completeResponseBody).withStatusCode(200))

        when:
        def result = factory.operationClient.getOperation('known-solutions-generate-op-1').waitToComplete()

        then:
        matches result, new ai.toloka.client.v1.task.KnownSolutionsGenerateOperation(
                id : 'known-solutions-generate-op-1',
                type : OperationType.KNOWN_SOLUTIONS_GENERATE,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2022-05-11 02:32:00'),
                started: parseDate('2022-05-11 02:32:01'),
                finished: parseDate('2022-05-11 2:32:02'),
                parameters: new ai.toloka.client.v1.task.KnownSolutionsGenerateOperation.Parameters(
                        poolId: '10',
                        poolName: 'Pool 1',
                        projectId: '12',
                        projectName: 'project',
                        secret: '123abc',
                        requesterId: 'qwe456'
                ),
                operationClient: factory.operationClient
        )
    }

    def "getOperation; get pseudo"() {
        when:
        def result = (Operation) factory.operationClient.getOperation('PSEUDO_ID').waitToComplete()

        then:
        result.id == 'PSEUDO_ID'
        result.type == OperationType.PSEUDO
        result.status == OperationStatus.SUCCESS
    }

    def "getOperation; with class"() {
        setup:
        def operation_map = [
                id        : 'open-pool-op1id',
                type      : 'POOL.OPEN',
                status    : 'RUNNING',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                parameters: [
                        pool_id: "21"
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/open-pool-op1id'), exactly(2))
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(200))

        def completeResponseBody =
                new JsonBuilder(operation_map + [status: 'SUCCESS', finished: '2016-03-07T15:48:03']).toString()
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/open-pool-op1id'), once())
                .respond(response(completeResponseBody).withStatusCode(200))

        when:
        def result = factory.operationClient.getOperation('open-pool-op1id', ai.toloka.client.v1.pool.PoolOpenOperation).waitToComplete()

        then:
        matches result, new ai.toloka.client.v1.pool.PoolOpenOperation(
                id: 'open-pool-op1id',
                type: OperationType.POOL_OPEN,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-03-07 15:47:00'),
                started: parseDate('2016-03-07 15:47:21'),
                finished: parseDate('2016-03-07 15:48:03'),
                parameters: new ai.toloka.client.v1.pool.PoolOpenOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    def "getOperation; check for success/check details"() {
        setup:
        def operation_map = [
                id        : 'open-pool-op1id',
                type      : 'POOL.OPEN',
                status    : 'FAIL',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                finished  : '2016-03-07T15:48:03',
                parameters: [
                        pool_id: "21"
                ],
                details   : [
                        simple_field : 'test',
                        complex_field: [
                                text: 'text',
                                num : 42
                        ]
                ]
        ]

        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/open-pool-op1id'), once())
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(200))

        when:
        factory.operationClient.getOperation('open-pool-op1id').waitAndGetSuccessfulUninterrupted()

        then:
        def ex = thrown OperationFailedException
        ex.details == [
                simple_field : 'test',
                complex_field: [
                        text: 'text',
                        num : 42
                ]
        ]
    }

    def "getOperationLog"() {
        setup:
        def operation_log_map = [
                [
                        type   : 'TASK_SUITE_VALIDATE',
                        success: false,
                        input  : [
                                pool_id: '21',
                                tasks  : [
                                        [
                                                input_values: [
                                                        image: 'http:/1.png'
                                                ]
                                        ]
                                ]
                        ],
                        output : [
                                code   : 'VALIDATION_ERROR',
                                message: 'Validation error',
                                payload: [
                                        'tasks.0.image': [
                                                code   : 'INVALID_URL_SYNTAX',
                                                message: 'Invalid URL'
                                        ]
                                ]
                        ]
                ],
                [
                        type   : 'TASK_SUITE_CREATE',
                        success: true,
                        input  : [
                                pool_id: '21',
                                tasks  : [
                                        [
                                                input_values: [
                                                        image: 'http://images.com/2.png'
                                                ]
                                        ]
                                ]
                        ],
                        output : [
                                task_suite_id: 'task-suite-i1d'
                        ]
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/operation-i1d/log'), once())
                .respond(response(new JsonBuilder(operation_log_map).toString()))

        when:
        def log = factory.operationClient.getOperationLog('operation-i1d')

        then:
        def item1 = log.next()

        item1.outputAsMap == [
                code   : 'VALIDATION_ERROR',
                message: 'Validation error',
                payload: [
                        'tasks.0.image': [
                                code   : 'INVALID_URL_SYNTAX',
                                message: 'Invalid URL'
                        ]
                ]
        ]

        and:
        def item2 = log.next()
        item2.inputAsMap == [
                pool_id: '21',
                tasks  : [
                        [
                                input_values: [
                                        image: 'http://images.com/2.png'
                                ]
                        ]
                ]
        ]
        println item2

        and:
        !log.hasNext()
    }

    def "getOperationLog; empty response"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/operation-i1d/log'), once())
                .respond(response("[]"))

        when:
        def log = factory.operationClient.getOperationLog('operation-i1d')

        then:
        !log.hasNext()
    }

    def "getDetailsAsMap with details==null"() {
        given:
        def operation = new ai.toloka.client.v1.pool.PoolCloneOperation()

        expect:
        operation.getDetailsAsMap().isEmpty()
    }
}
