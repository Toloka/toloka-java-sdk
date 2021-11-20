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

import ai.toloka.client.v1.aggregatedsolutions.*
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static ai.toloka.client.v1.aggregatedsolutions.WeightedDynamicOverlapPoolAggregatedSolutionRequest.Field
import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class AggregatedSolutionClientImplSpec extends AbstractClientSpec {

    // See https://github.com/jamesdbloom/mockserver/issues/339 about
    // WARN  o.m.c.s.ObjectMapperFactory - Ignoring invalid value for "type" field of "WEIGHTED_DYNAMIC_OVERLAP"
    def "aggregateSolutionsByPool"() {
        setup:
        def operation_map = [
                id        : 'aggregated-solution-op1id',
                type      : 'SOLUTION.AGGREGATE',
                status    : 'RUNNING',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                parameters: [
                        pool_id: "21"
                ]
        ]

        def pool_aggregated_solution_request_map = [
                type                  : 'WEIGHTED_DYNAMIC_OVERLAP',
                pool_id               : '21',
                answer_weight_skill_id: '42',
                fields                : [[name: 'out1']]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/aggregated-solutions/aggregate-by-pool')
                .withBody(json(new JsonBuilder(pool_aggregated_solution_request_map) as String))
                .withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(202))

        def completeResponseBody =
                new JsonBuilder(operation_map + [status: 'SUCCESS', finished: '2016-03-07T15:48:03']).toString()
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/aggregated-solution-op1id'), once())
                .respond(response(completeResponseBody).withStatusCode(200))

        when:
        def request = new WeightedDynamicOverlapPoolAggregatedSolutionRequest('21', '42',
                [new Field('out1')])
        def result = factory.aggregatedSolutionClient.aggregateSolutionsByPool(request).waitToComplete()

        then:
        matches result, new AggregatedSolutionOperation(
                id: 'aggregated-solution-op1id',
                type: ai.toloka.client.v1.operation.OperationType.SOLUTION_AGGREGATE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-03-07 15:47:00'),
                started: parseDate('2016-03-07 15:47:21'),
                finished: parseDate('2016-03-07 15:48:03'),
                parameters: new AggregatedSolutionOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    def "aggregateSolutionsByPool; DAWID_SKENE"() {
        setup:
        def operation_map = [
                id        : 'aggregated-solution-op1id',
                type      : 'SOLUTION.AGGREGATE',
                status    : 'RUNNING',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                parameters: [
                        pool_id: "21"
                ]
        ]

        def pool_aggregated_solution_request_map = [
                type   : 'DAWID_SKENE',
                pool_id: '21',
                fields : [[name: 'out1']]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/aggregated-solutions/aggregate-by-pool')
                        .withBody(json(new JsonBuilder(pool_aggregated_solution_request_map) as String))
                        .withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(202))

        def completeResponseBody =
                new JsonBuilder(operation_map + [status: 'SUCCESS', finished: '2016-03-07T15:48:03']).toString()
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/aggregated-solution-op1id'), once())
                .respond(response(completeResponseBody).withStatusCode(200))

        when:
        def request = new DawidSkenePoolAggregatedSolutionRequest('21',
                [new DawidSkenePoolAggregatedSolutionRequest.Field('out1')])
        def result = factory.aggregatedSolutionClient.aggregateSolutionsByPool(request).waitToComplete()

        then:
        matches result, new AggregatedSolutionOperation(
                id: 'aggregated-solution-op1id',
                type: ai.toloka.client.v1.operation.OperationType.SOLUTION_AGGREGATE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-03-07 15:47:00'),
                started: parseDate('2016-03-07 15:47:21'),
                finished: parseDate('2016-03-07 15:48:03'),
                parameters: new AggregatedSolutionOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    // See https://github.com/jamesdbloom/mockserver/issues/339 about
    // WARN  o.m.c.s.ObjectMapperFactory - Ignoring invalid value for "type" field of "WEIGHTED_DYNAMIC_OVERLAP"
    def "aggregateSolutionsByTask"() {
        setup:
        def aggregated_solution_map = [
                pool_id      : '21',
                task_id      : 'qwerty-123',
                confidence   : 0.42,
                output_values: [out1: true]
        ]

        def task_aggregated_solution_request_map = [
                type                  : 'WEIGHTED_DYNAMIC_OVERLAP',
                pool_id               : '21',
                task_id               : 'qwerty-123',
                answer_weight_skill_id: '42',
                fields                : [[name: 'out1']]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/aggregated-solutions/aggregate-by-task')
                .withBody(json(new JsonBuilder(task_aggregated_solution_request_map) as String))
                .withMethod('POST'), once())
                .respond(response(new JsonBuilder(aggregated_solution_map).toString()).withStatusCode(200))

        when:
        def request = new WeightedDynamicOverlapTaskAggregatedSolutionRequest('qwerty-123', '21',
                '42', [new WeightedDynamicOverlapTaskAggregatedSolutionRequest.Field('out1')])
        def result = factory.aggregatedSolutionClient.aggregateSolutionsByTask(request)

        then:
        !result.isNewCreated()
        matches result.result, new AggregatedSolution(
                poolId: '21',
                taskId: 'qwerty-123',
                confidence: 0.42,
                outputValues: ['out1': true]
        )
    }

    def "aggregateSolutionsByTask; DAWID_SKENE"() {
        setup:
        def aggregated_solution_map = [
                pool_id      : '21',
                task_id      : 'qwerty-123',
                confidence   : 0.42,
                output_values: [out1: true]
        ]

        def task_aggregated_solution_request_map = [
                type   : 'DAWID_SKENE',
                pool_id: '21',
                task_id: 'qwerty-123',
                fields : [[name: 'out1']]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/aggregated-solutions/aggregate-by-task')
                        .withBody(json(new JsonBuilder(task_aggregated_solution_request_map) as String))
                        .withMethod('POST'), once())
                .respond(response(new JsonBuilder(aggregated_solution_map).toString()).withStatusCode(200))
    }

    def "findAggregatedSolutions"() {
        setup:
        def aggregated_solutions_1_map = [
                pool_id      : '21',
                task_id      : 'qwerty-234',
                confidence   : 0.41,
                output_values: [out1: true]
        ]
        def aggregated_solutions_2_map = [
                pool_id      : '21',
                task_id      : 'qwerty-876',
                confidence   : 0.42,
                output_values: [out1: false]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/aggregated-solutions/op_id').withQueryStringParameters(
                task_id_gte: ['qwerty_123'],
                task_id_lte: ['qwerty_987'],
                sort: ['-task_id'],
                limit: ['42']), once()
        )
                .respond(response(
                new JsonBuilder([
                        items   : [aggregated_solutions_1_map, aggregated_solutions_2_map],
                        has_more: false]
                ) as String)
        )

        when:
        def request = AggregatedSolutionSearchRequest.make()
                .filter().and()
                .range().byTaskId('qwerty_123').gte().and()
                .range().byTaskId('qwerty_987').lte().and()
                .sort().byTaskId().desc().and()
                .limit(42)
                .done()
        def result = factory.aggregatedSolutionClient.findAggregatedSolutions('op_id', request)

        then:
        !result.hasMore

        matches result.items[0], new AggregatedSolution(
                poolId: '21',
                taskId: 'qwerty-234',
                confidence: 0.41,
                outputValues: ['out1': true]
        )

        matches result.items[1], new AggregatedSolution(
                poolId: '21',
                taskId: 'qwerty-876',
                confidence: 0.42,
                outputValues: ['out1': false]
        )
    }
}
