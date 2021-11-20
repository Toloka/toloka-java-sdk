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

import ai.toloka.client.v1.training.*
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class TrainingClientImplSpec extends AbstractClientSpec {

    def "findTrainings"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings')
                        .withQueryStringParameters([
                                project_id     : ['10'],
                                id_gt          : ['20'],
                                last_started_lt: ['2016-03-23T12:59:00'],
                                sort           : ['-created,id']
                        ]), once())
                .respond(response(new JsonBuilder([items: [training_map_with_readonly()], has_more: true]).toString()))

        and:
        def request = TrainingSearchRequest.make()
                .filter().by(TrainingFilterParam.projectId, '10')
                .and()
                .range()
                .by(TrainingRangeParam.id, '20', ai.toloka.client.v1.RangeOperator.gt)
                .by(TrainingRangeParam.lastStarted, parseDate('2016-03-23 12:59:00'), ai.toloka.client.v1.RangeOperator.lt)
                .and()
                .sort().by(TrainingSortParam.created, ai.toloka.client.v1.SortDirection.desc).by(TrainingSortParam.id, ai.toloka.client.v1.SortDirection.asc)
                .and()
                .done()

        when:
        def result = factory.trainingClient.findTrainings(request)

        then:
        result.hasMore
        matches result.items.first(), training_with_readonly()
    }

    def "getTraining"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings/22'), once())
                .respond(response(new JsonBuilder(training_map_with_readonly()).toString()))

        when:
        def result = factory.trainingClient.getTraining('22')

        then:
        matches result, training_with_readonly()
    }

    def "createTraining"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings').withBody(json(new JsonBuilder(training_map()).toString())), once())
                .respond(response(new JsonBuilder(training_map_with_readonly()).toString()).withStatusCode(201))

        when:
        def result = factory.trainingClient.createTraining(training())

        then:
        matches result.result, training_with_readonly()

    }

    def "updateTraining"() {
        setup:
        def updated_form = training_with_readonly().with {
            privateName = 'updated name'
            publicInstructions = 'updated instructions'
            it
        }

        def updated_form_map = training_map_with_readonly() + [
                private_name   : 'updated name',
                public_instructions: 'updated instructions'
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings/22').withBody(json(new JsonBuilder(updated_form_map) as String)), once())
                .respond(response(new JsonBuilder(updated_form_map) as String))

        when:
        def result = factory.trainingClient.updateTraining('22', updated_form)

        then:
        !result.newCreated
        matches result.result, updated_form
    }

    def "openTraining"() {
        setup:
        def operation_map = [
                id        : 'open-training-op1id',
                type      : 'TRAINING.OPEN',
                status    : 'RUNNING',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                parameters: [
                        training_id: "22"
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings/22/open').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(202))

        def completeResponseBody =
                new JsonBuilder(operation_map + [status: 'SUCCESS', finished: '2016-03-07T15:48:03']).toString()
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/open-training-op1id'), once())
                .respond(response(completeResponseBody).withStatusCode(200))

        when:
        def result = factory.trainingClient.openTraining('22').waitToComplete()

        then:
        matches result, new TrainingOpenOperation(
                id: 'open-training-op1id',
                type: ai.toloka.client.v1.operation.OperationType.TRAINING_OPEN,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-03-07 15:47:00'),
                started: parseDate('2016-03-07 15:47:21'),
                finished: parseDate('2016-03-07 15:48:03'),
                parameters: new TrainingOpenOperation.Parameters(trainingId: '22'),
                operationClient: factory.operationClient,
        )
    }

    def "openAlreadyOpened"() {
        setup:

        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings/22/open').withMethod('POST'), once())
                .respond(response().withStatusCode(204))

        when:
        def result = factory.trainingClient.openTraining('22').waitToComplete()

        then:
        result.isCompleted()
        result.isPseudo()
    }

    def "closeTraining"() {
        setup:
        def operation_map = [
                id        : 'close-training-op1id',
                type      : 'TRAINING.CLOSE',
                status    : 'SUCCESS',
                submitted : '2016-07-22T13:04:00',
                started   : '2016-07-22T13:04:01',
                finished  : '2016-07-22T13:04:02',
                parameters: [
                        training_id: '22'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings/22/close').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.trainingClient.closeTraining('22').waitToComplete()

        then:
        matches result, new TrainingCloseOperation(
                id: 'close-training-op1id',
                type: ai.toloka.client.v1.operation.OperationType.TRAINING_CLOSE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-07-22 13:04:00'),
                started: parseDate('2016-07-22 13:04:01'),
                finished: parseDate('2016-07-22 13:04:02'),
                parameters: new TrainingCloseOperation.Parameters(trainingId: '22'),
                operationClient: factory.operationClient
        )
    }

    def "archiveTraining"() {
        setup:
        def operation_map = [
                id        : 'archive-training-op1id',
                type      : 'TRAINING.ARCHIVE',
                status    : 'SUCCESS',
                submitted : '2016-07-22T13:04:00',
                started   : '2016-07-22T13:04:01',
                finished  : '2016-07-22T13:04:02',
                parameters: [
                        training_id: '22'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings/22/archive').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.trainingClient.archiveTraining('22').waitToComplete()

        then:
        matches result, new TrainingArchiveOperation(
                id: 'archive-training-op1id',
                type: ai.toloka.client.v1.operation.OperationType.TRAINING_ARCHIVE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-07-22 13:04:00'),
                started: parseDate('2016-07-22 13:04:01'),
                finished: parseDate('2016-07-22 13:04:02'),
                parameters: new TrainingArchiveOperation.Parameters(trainingId: '22'),
                operationClient: factory.operationClient
        )
    }

    def "cloneTraining"() {
        setup:
        def operation_map = [
                id        : 'clone-training-op1id',
                type      : 'TRAINING.CLONE',
                status    : 'SUCCESS',
                submitted : '2016-07-22T13:04:00',
                started   : '2016-07-22T13:04:01',
                finished  : '2016-07-22T13:04:02',
                parameters: [
                        training_id: '22'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/trainings/22/clone').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.trainingClient.cloneTraining('22').waitToComplete()

        then:
        matches result, new TrainingCloneOperation(
                id: 'clone-training-op1id',
                type: ai.toloka.client.v1.operation.OperationType.TRAINING_CLONE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-07-22 13:04:00'),
                started: parseDate('2016-07-22 13:04:01'),
                finished: parseDate('2016-07-22 13:04:02'),
                parameters: new TrainingCloneOperation.Parameters(trainingId: '22'),
                operationClient: factory.operationClient
        )
    }

    def training_map() {
        [
            project_id : '10',
            private_name : 'training_v12_231',
            inherited_instructions : false,
            public_instructions : 'training instructions',
            may_contain_adult_content : true,
            assignment_max_duration_seconds : 600,
            mix_tasks_in_creation_order : false,
            shuffle_tasks_in_task_suite : true,
            training_tasks_in_task_suite_count : 7,
            task_suites_required_to_pass : 5,
            retry_training_after_days : 5
        ]
    }

    def training_map_with_readonly() {
        training_map() + [
                id               : '22',
                owner            : [id: 'requester-1', myself: true, company_id: '1'],
                created          : '2017-12-03T12:03:00',
                last_started     : '2017-12-04T12:12:03',
                status           : 'OPEN',
        ]
    }

    def training() {
        new Training().with {
            projectId = '10'
            privateName = 'training_v12_231'
            inheritedInstructions = false
            publicInstructions = 'training instructions'
            mayContainAdultContent = true
            assignmentMaxDurationSeconds = 600
            mixTasksInCreationOrder = false
            shuffleTasksInTaskSuite = true
            trainingTasksInTaskSuiteCount = 7
            taskSuitesRequiredToPass = 5
            retryTrainingAfterDays = 5
            it
        }
    }

    def training_with_readonly() {
        training().with {
            id = '22'
            it.owner = new ai.toloka.client.v1.Owner(id: 'requester-1', myself: true, companyId: '1')
            status = TrainingStatus.OPEN
            created = parseDate('2017-12-03 12:03:00')
            lastStarted = parseDate('2017-12-04 12:12:03')
            it
        }
    }
}
