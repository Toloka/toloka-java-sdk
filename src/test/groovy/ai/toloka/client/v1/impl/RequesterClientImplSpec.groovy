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


import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class RequesterClientImplSpec extends AbstractClientSpec {

    def "getRequester"() {
        setup:
        def requester_resp_body = [
                id         : '566ec2b0ff0deeaae5f9d500',
                balance    : 120.3,
                public_name: [
                        EN: 'John Smith',
                        RU: 'Джон Смит'
                ],
                company    : [
                        id               : '1',
                        superintendent_id: 'superintendent-1id'
                ]
        ]

        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/requester'), once())
                .respond(response().withBody(new JsonBuilder(requester_resp_body) as String))

        when:
        def result = factory.requesterClient.getRequester()

        then:
        matches result, new ai.toloka.client.v1.requester.Requester(
                id: '566ec2b0ff0deeaae5f9d500',
                balance: 120.3,
                publicName: [(ai.toloka.client.v1.LangIso639.EN): 'John Smith', (ai.toloka.client.v1.LangIso639.RU): 'Джон Смит'],
                company: new ai.toloka.client.v1.requester.Company(id: '1', superintendentId: 'superintendent-1id')
        )
    }
}
