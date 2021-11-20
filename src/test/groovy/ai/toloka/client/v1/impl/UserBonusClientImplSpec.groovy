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

import ai.toloka.client.v1.userbonus.UserBonus
import ai.toloka.client.v1.userbonus.UserBonusCreateBatchOperation
import ai.toloka.client.v1.userbonus.UserBonusCreateRequestParameters
import ai.toloka.client.v1.userbonus.UserBonusSearchRequest
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static AbstractClientSpec.parseDate
import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class UserBonusClientImplSpec extends AbstractClientSpec {

    def "createUserBonus"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-bonuses').withMethod('POST')
                        .withBody(json(new JsonBuilder(user_bonus_map()) as String)), once())
                .respond(response(new JsonBuilder(user_bonus_map_with_readonly()) as String).withStatusCode(201))

        when:
        def result = factory.userBonusClient.createUserBonus(user_bonus())

        then:
        matches result, user_bonus_with_readonly()
    }

    def "createUserBonuses"() {
        setup:
        def user_bonuses_map = [[user_id: 'user-2', amount: -5], user_bonus_map()]
        def result_map = [
                items            : ['1': user_bonus_map_with_readonly()],
                validation_errors: [
                        '0': [
                                'amount': [
                                        code   : 'VALUE_LESS_THAN_MIN',
                                        message: 'Value must be greater or equal to 0.01',
                                        params : [0.01]
                                ]
                        ]
                ]
        ]

        def user_bonuses = [new UserBonus('user-2', -5), user_bonus()]
        def expected_result = new ai.toloka.client.v1.BatchCreateResult(
                items: [1: user_bonus_with_readonly()],
                validationsErrors: [
                        0: ['amount': new ai.toloka.client.v1.FieldValidationError(
                                'VALUE_LESS_THAN_MIN', 'Value must be greater or equal to 0.01', [0.01])]
                ]
        )

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-bonuses').withMethod('POST')
                        .withQueryStringParameters(async_mode: ['false'], skip_invalid_items: ['true'])
                        .withBody(json(new JsonBuilder(user_bonuses_map) as String)), once()
                )
                .respond(response(new JsonBuilder(result_map) as String).withStatusCode(201))

        when:
        def result = factory.userBonusClient.createUserBonuses(
                user_bonuses, new UserBonusCreateRequestParameters(skipInvalidItems: true))

        then:
        matches result, expected_result
    }

    def "createUserBonuses; without message"() {
        setup:
        def bonus_map = user_bonus_map()
        bonus_map.remove('public_title')
        bonus_map.remove('public_message')
        bonus_map['without_message'] = true
        def result_map = [
                items: ['0': user_bonus_map_with_readonly().with {
                    it['public_title'] = null
                    it['public_message'] = null
                    it['without_message'] = true
                    it
                }],
        ]

        def bonus = new UserBonus(bonus_map['user_id'], bonus_map['amount'], null, null)
        bonus.setPrivateComment(bonus_map['private_comment'])
        bonus.setWithoutMessage(true)
        bonus.setAssignmentId('assignment-1')
        def user_bonuses = [bonus]
        def expected_result = new ai.toloka.client.v1.BatchCreateResult(items: [0: user_bonus_with_readonly()])

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-bonuses').withMethod('POST')
                        .withQueryStringParameters(async_mode: ['false'], skip_invalid_items: ['true'])
                        .withBody(json(new JsonBuilder([bonus_map]) as String)), once()
                )
                .respond(response(new JsonBuilder(result_map) as String).withStatusCode(201))

        when:
        def result = factory.userBonusClient.createUserBonuses(
                user_bonuses, new UserBonusCreateRequestParameters(skipInvalidItems: true))

        then:
        matches result, expected_result
    }

    def "createUserBonusesAsync"() {
        setup:
        def operationUUID = UUID.randomUUID()

        def user_bonuses_map = [[user_id: 'user-1', amount: 10], [user_id: 'user-2', amount: 12]]
        def operation_map = [
                id       : operationUUID as String,
                type     : 'USER_BONUS.BATCH_CREATE',
                status   : 'SUCCESS',
                submitted: '2016-10-23T14:02:01',
                started  : '2016-10-23T14:02:02',
                finished : '2016-10-23T14:02:03'
        ]

        def user_bonuses = [new UserBonus('user-1', 10), new UserBonus('user-2', 12)]
        def operation = new UserBonusCreateBatchOperation(
                id: operationUUID as String,
                type: ai.toloka.client.v1.operation.OperationType.USER_BONUS_BATCH_CREATE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-10-23 14:02:01'),
                started: parseDate('2016-10-23 14:02:02'),
                finished: parseDate('2016-10-23 14:02:03'),
                operationClient: factory.operationClient
        )

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-bonuses').withMethod('POST')
                        .withQueryStringParameters(operation_id: [operationUUID as String], async_mode: ['true'])
                        .withBody(new JsonBuilder(user_bonuses_map) as String), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.userBonusClient.createUserBonusesAsync(
                user_bonuses.iterator(), new UserBonusCreateRequestParameters(operationId: operationUUID)
        ).waitAndGetSuccessful()

        then:
        matches result, operation
    }

    def "findUserBonuses"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-bonuses')
                        .withQueryStringParameters(
                                user_id: ['user-1'],
                                created_gte: ['2012-01-01T12:00:00'],
                                sort: ['created,-id'],
                                limit: ['20']), once()
                )
                .respond(response(
                        new JsonBuilder([items: [user_bonus_map_with_readonly()], has_more: false]) as String)
                )

        when:
        def request = UserBonusSearchRequest.make()
                .filter().byUserId('user-1').and()
                .range().byCreated(parseDate('2012-01-01 12:00:00')).gte().and()
                .sort().byCreated().asc().byId().desc().and()
                .limit(20)
                .done()
        def result = factory.userBonusClient.findUserBonuses(request)

        then:
        matches result.items.first(), user_bonus_with_readonly()
    }

    def "getUserBonus"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-bonuses/user-bonus-1'))
                .respond(response(new JsonBuilder(user_bonus_map_with_readonly()) as String))

        when:
        def result = factory.userBonusClient.getUserBonus('user-bonus-1')

        then:
        matches result, user_bonus_with_readonly()
    }

    def user_bonus_map() {
        [
                user_id        : 'user-1',
                amount         : 1.5,
                private_comment: 'pool_23214',
                assignment_id  : 'assignment-1',
                public_title   : [
                        EN     : 'Good Job!',
                        RU     : 'Молодец!',
                        'PT-BR': 'Bem feito!'
                ],
                public_message : [
                        EN     : 'Ten tasks completed',
                        RU     : 'Выполнено 10 заданий',
                        'PT-BR': 'Concluiu 10 tarefas'
                ]
        ]
    }

    def user_bonus_map_with_readonly() {
        [
                *      : user_bonus_map(),
                id     : 'user-bonus-1',
                created: '2016-10-23T13:27:00'
        ]
    }

    def user_bonus() {
        new UserBonus('user-1', 1.5,
                [(ai.toloka.client.v1.LangIso639.EN): 'Good Job!', (ai.toloka.client.v1.LangIso639.RU): 'Молодец!', (ai.toloka.client.v1.LangIso639.PT_BR): 'Bem feito!'],
                [(ai.toloka.client.v1.LangIso639.EN): 'Ten tasks completed', (ai.toloka.client.v1.LangIso639.RU): 'Выполнено 10 заданий', (ai.toloka.client.v1.LangIso639.PT_BR): 'Concluiu 10 tarefas']).with {
            privateComment = 'pool_23214'
            assignmentId = 'assignment-1'
            it
        }
    }

    def user_bonus_with_readonly() {
        user_bonus().with {
            id = 'user-bonus-1'
            created = parseDate('2016-10-23 13:27:00')
        }
    }
}
