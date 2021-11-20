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

import ai.toloka.client.v1.messagethread.*
import ai.toloka.client.v1.pool.filter.CompareOperator
import ai.toloka.client.v1.pool.filter.Connective
import ai.toloka.client.v1.pool.filter.Expression
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static ai.toloka.client.v1.LangIso639.EN
import static ai.toloka.client.v1.messagethread.RecipientsSelectType.DIRECT
import static ai.toloka.client.v1.messagethread.RecipientsSelectType.FILTER
import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class MessageThreadClientImplSpec extends AbstractClientSpec {

    def "findAssignments"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/message-threads').withQueryStringParameters(
                [
                        folder     : ['OUTBOX'],
                        folder_ne  : ['IMPORTANT'],
                        created_gte: ['2015-12-01T00:00:00'],
                        created_lt : ['2016-06-01T00:00:00'],
                        sort       : ['-created']
                ]
        ), once())
                .respond(response(new JsonBuilder([items: [message_thread_base_map()], has_more: false]) as String))

        when:
        def request = MessageThreadSearchRequest.make()
                .filter().byFolder(Folder.OUTBOX).byFolderNe(Folder.IMPORTANT)
                .and()
                .range()
                .byCreated(parseDate('2015-12-01 00:00:00')).gte()
                .byCreated(parseDate('2016-06-01 00:00:00')).lt()
                .and()
                .sort().byCreated().desc()
                .and()
                .done()

        def result = factory.messageThreadClient.findMessageThreads(request)

        then:
        matches result.items[0], message_thread_base()
    }

    def "composeMessageThread; direct"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/message-threads/compose')
                .withBody(json(new JsonBuilder(message_thread_compose_direct_map()) as String)), once())
                .respond(response(
                new JsonBuilder(
                        [*: message_thread_base_map(), compose_details: compose_details_direct_map()]) as String)
                .withStatusCode(201))

        when:
        def result = factory.messageThreadClient.composeMessageThread(message_thread_compose_direct())

        then:
        matches result, new ai.toloka.client.v1.ModificationResult(
                message_thread_base().with { composeDetails = compose_details_direct(); it }, true)
    }

    def "composeMessageThread; filter"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/message-threads/compose')
                .withBody(json(new JsonBuilder(message_thread_compose_filter_map()) as String)), once())
                .respond(response(
                new JsonBuilder(
                        [*: message_thread_base_map(), compose_details: compose_details_filter_map()]) as String)
                .withStatusCode(201))

        when:
        def result = factory.messageThreadClient.composeMessageThread(message_thread_compose_filter())

        then:
        matches result, new ai.toloka.client.v1.ModificationResult(
                message_thread_base().with { composeDetails = compose_details_filter(); it }, true)
    }

    def "replyMessageThread"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/message-threads/42/reply')
                .withBody(json(new JsonBuilder(message_thread_reply_map()) as String)), once())
                .respond(response(new JsonBuilder(message_thread_base_map()) as String))

        when:
        def result = factory.messageThreadClient.replyMessageThread('42', message_thread_reply())

        then:
        matches result, new ai.toloka.client.v1.ModificationResult(message_thread_base(), false)
    }

    def "addToFolders"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/message-threads/42/add-to-folders')
                .withBody(json('{\"folders\":[\"IMPORTANT\"]}')), once())
                .respond(response(new JsonBuilder(message_thread_base_map()) as String))

        when:
        def result = factory.messageThreadClient.addToFolders('42',
                new MessageThreadFolders([Folder.IMPORTANT] as Set))

        then:
        matches result, new ai.toloka.client.v1.ModificationResult(message_thread_base(), false)
    }

    def "removeFromFolders"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/message-threads/42/remove-from-folders')
                .withBody(json('{\"folders\":[\"UNREAD\"]}')), once())
                .respond(response(new JsonBuilder(message_thread_base_map()) as String))

        when:
        def result = factory.messageThreadClient.removeFromFolders('42',
                new MessageThreadFolders([Folder.UNREAD] as Set))

        then:
        matches result, new ai.toloka.client.v1.ModificationResult(message_thread_base(), false)
    }

    def compose_details_direct_map() {
        [recipients_select_type: 'DIRECT', recipients_ids: ['user-1', 'user-2']]
    }

    def compose_details_direct() {
        new ComposeDetails(recipientsSelectType: DIRECT, recipientsIds: ['user-1', 'user-2'])
    }

    def compose_details_filter_map() {
        [
                recipients_select_type: 'FILTER',
                recipients_filter     : [and: [[category: 'skill', key: '2022', operator: 'GT', value: 90]]]
        ]
    }

    def compose_details_filter() {
        new ComposeDetails(
                recipientsSelectType: FILTER,
                recipientsFilter: new Connective.And([new Expression.Skill('2022', CompareOperator.GT, 90)])
        )
    }

    def message_thread_reply_map() {
        [text: [(EN): 'Message text']]
    }

    def message_thread_reply() {
        new MessageThreadReply([(EN): 'Message text'])
    }

    def message_thread_compose_direct_map() {
        [topic: [(EN): 'Message title'], text: [(EN): 'Message text'], *: compose_details_direct_map()]
    }

    def message_thread_compose_direct() {
        new MessageThreadCompose([(EN): 'Message title'], [(EN): 'Message text'], ['user-1', 'user-2'])
    }

    def message_thread_compose_filter_map() {
        [
                topic: [(EN): 'Message title'],
                text : [(EN): 'Message text'],
                *    : compose_details_filter_map()
        ]
    }

    def message_thread_compose_filter() {
        new MessageThreadCompose(
                [(EN): 'Message title'],
                [(EN): 'Message text'],
                new Connective.And([new Expression.Skill('2022', CompareOperator.GT, 90)])
        )
    }

    def message_thread_base_map() {
        [
                id                   : 'message-thread-1',
                topic                : [(EN): 'Message title'],
                interlocutors_inlined: true,
                meta                 : [
                        pool_id      : '1',
                        project_id   : '2',
                        assignment_id: '3'
                ],
                interlocutors        : [
                        [id: 'requester-1', role: 'REQUESTER', myself: true],
                        [id: 'user-1', role: 'USER']
                ],
                messages_inlined     : true,
                messages             : [
                        [
                                text   : [(EN): 'Message text'],
                                from   : [id: 'requester-1', role: 'REQUESTER', myself: true],
                                created: '2017-01-31T09:38:01'
                        ]
                ],
                folders              : ['OUTBOX'],
                answerable           : true,
                created              : '2017-01-31T09:38:01'
        ]
    }

    def message_thread_base() {
        new MessageThread(
                id: 'message-thread-1',
                topic: [(EN): 'Message title'],
                interlocutorsInlined: true,
                meta: [
                        poolId      : '1',
                        projectId   : '2',
                        assignmentId: '3'
                ],
                interlocutors: [
                        new Interlocutor(id: 'requester-1', role: InterlocutorRole.REQUESTER, myself: true),
                        new Interlocutor(id: 'user-1', role: InterlocutorRole.USER)
                ],
                messagesInlined: true,
                messages: [
                        new Message(
                                text: [(EN): 'Message text'],
                                from: [id: 'requester-1', role: InterlocutorRole.REQUESTER, myself: true],
                                created: parseDate('2017-01-31 09:38:01')
                        )
                ],
                folders: [Folder.OUTBOX],
                answerable: true,
                created: parseDate('2017-01-31 09:38:01')
        )
    }
}
