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

import ai.toloka.client.v1.metadata.UserMetadata
import ai.toloka.client.v1.metadata.UserMetadataAttributes;
import groovy.json.JsonBuilder;
import org.mockserver.client.server.MockServerClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class UserMetadataClientImplSpec extends AbstractClientSpec {
    def "getUserMetadata"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-metadata/123abc'))
                .respond(response(new JsonBuilder(user_metadata_map_with_readonly()) as String))

        when:
        def result = factory.userMetadataClient.getUserMetadata('123abc')

        then:
        matches result, user_metadata_with_readonly()
    }

    def user_metadata_map_with_readonly() {
        [
                id     : '123abc',
                country: 'RU',
                languages  : ['EN', 'RU'],
                attributes : [
                        country_by_phone : 'RU',
                        country_by_ip : 'RU',
                        client_type : 'BROWSE',
                        user_agent_type : 'BROWSE',
                        device_category : 'PERSONAL_COMPUTER',
                        os_family : 'WINDOWS',
                        os_version : 6.1,
                        os_version_major : 6,
                        os_version_minor : 1,
                        os_version_bugfix: 0
                ],
                adult_allowed: 'true',
        ]
    }

    def user_metadata_with_readonly() {
        new UserMetadata('RU', ['EN', 'RU'],
                new UserMetadataAttributes('RU', 'RU',
                        'BROWSE', 'BROWSE',
                        'PERSONAL_COMPUTER', 'WINDOWS',
                        6.1, 6, 1, 0),
                true).with {
            id = '123abc'
            it
        }
    }
}
