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

import ai.toloka.client.v1.assignment.Assignment
import ai.toloka.client.v1.assignment.AssignmentPatch
import ai.toloka.client.v1.assignment.AssignmentSearchRequest
import ai.toloka.client.v1.assignment.AssignmentStatus
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class AssignmentClientImplSpec extends AbstractClientSpec {

    def "getAssignment"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/assignments/assignment-i1d'), once())
                .respond(response(new JsonBuilder(assignment_map()) as String))

        when:
        def result = factory.assignmentClient.getAssignment('assignment-i1d')

        then:
        matches result, assignment()
    }

    def "findAssignments"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/assignments').withQueryStringParameters(
                [
                        status     : ['ACCEPTED'],
                        pool_id    : ['21'],
                        user_id    : ['user-i1d'],
                        created_gte: ['2015-12-01T00:00:00'],
                        created_lt : ['2016-06-01T00:00:00'],
                        sort       : ['-submitted']
                ]
        ), once())
                .respond(response(new JsonBuilder([items: [assignment_map()], has_more: false]) as String))

        when:
        def request = AssignmentSearchRequest.make()
                .filter().byStatus(AssignmentStatus.ACCEPTED).byPoolId('21').byUserId('user-i1d')
                .and()
                .range()
                .byCreated(parseDate('2015-12-01 00:00:00')).gte()
                .byCreated(parseDate('2016-06-01 00:00:00')).lt()
                .and()
                .sort().bySubmitted().desc()
                .and()
                .done()

        def result = factory.assignmentClient.findAssignments(request)

        then:
        matches result.items[0], assignment()
    }

    def 'patchAssignment'() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/assignments/assignment-i1d').withMethod('PATCH')
                .withBody(new JsonBuilder([status: 'ACCEPTED', public_comment: 'Well done']) as String), once())
                .respond(response(new JsonBuilder(assignment_map() + [public_comment: 'Well done']) as String))

        when:
        def result = factory.assignmentClient.patchAssignment(
                'assignment-i1d', new AssignmentPatch(AssignmentStatus.ACCEPTED, 'Well done'))

        then:
        matches result.result, assignment().with { publicComment = 'Well done'; it }
    }

    def assignment_map() {
        [
                id                             : 'assignment-i1d',
                task_suite_id                  : 'task-suite-i1d',
                pool_id                        : '21',
                user_id                        : 'user-i1d',
                status                         : 'ACCEPTED',
                reward                         : 0.05,
                mixed                          : true,
                automerged                     : true,
                created                        : '2015-12-15T14:52:00',
                submitted                      : '2015-12-15T15:10:00',
                accepted                       : '2015-12-15T20:00:00',
                tasks                          : [
                        [
                                pool_id       : '21',
                                input_values  : [
                                        image: 'http://images.com/1.png'
                                ],
                                origin_task_id: '42'
                        ]
                ],
                first_declined_solution_attempt: [
                        [
                                output_values: [
                                        color  : 'black',
                                        comment: 'So white'
                                ]
                        ]
                ],
                solutions                      : [
                        [
                                output_values: [
                                        color  : 'white',
                                        comment: 'So white'
                                ]
                        ]
                ]
        ]
    }

    def assignment() {
        new Assignment(
                id: 'assignment-i1d',
                taskSuiteId: 'task-suite-i1d',
                poolId: '21',
                userId: 'user-i1d',
                status: AssignmentStatus.ACCEPTED,
                reward: 0.05,
                mixed: true,
                automerged: true,
                created: parseDate('2015-12-15 14:52:00'),
                submitted: parseDate('2015-12-15 15:10:00'),
                accepted: parseDate('2015-12-15 20:00:00'),
                tasks: [
                        new ai.toloka.client.v1.task.Task('21', [image: 'http://images.com/1.png']).with { originTaskId = '42'; it }
                ],
                firstDeclinedSolutionAttempt: [
                        new ai.toloka.client.v1.solution.Solution(outputValues: [color: 'black', comment: 'So white'])
                ],
                solutions: [
                        new ai.toloka.client.v1.solution.Solution(outputValues: [color: 'white', comment: 'So white'])
                ]
        )
    }
}
