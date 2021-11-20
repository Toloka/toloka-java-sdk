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

import ai.toloka.client.v1.userrestriction.*
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class UserRestrictionClientImplSpec extends AbstractClientSpec {

    def "getUserRestriction"() {
        setup:
        def user_restriction_map = [
                id         : '56',
                scope      : 'ALL_PROJECTS',
                user_id    : 'user-i1d',
                will_expire: '2019-01-01T00:00:00',
                created    : '2016-03-28T18:16:00'
        ]

        def user_restriction =
                new AllProjectsUserRestriction('user-i1d', null, parseDate('2019-01-01 00:00:00')).with {
                    id = '56'
                    created = parseDate('2016-03-28 18:16:00')
                    it
                }

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-restrictions/56'), once())
                .respond(response(new JsonBuilder(user_restriction_map) as String))

        when:
        def result = factory.userRestrictionClient.getUserRestriction('56')

        then:
        matches result, user_restriction
    }

    def "setUserRestriction"() {
        setup:
        def user_restriction_map = {
            [
                    scope          : 'POOL',
                    user_id        : 'user-i1d',
                    pool_id        : '21',
                    will_expire    : '2019-01-01T00:00:00',
                    private_comment: 'Too many errors'
            ]
        }

        def user_restriction_map_with_readonly = user_restriction_map() + [id: '56', created: '2016-03-28T28:16:00']

        def user_restriction = {
            new PoolUserRestriction('user-i1d', '21', 'Too many errors', parseDate('2019-01-01 00:00:00'))
        }

        def user_restriction_with_readonly = user_restriction().with {
            id = '56'
            created = parseDate('2016-03-28 28:16:00')
            it
        }

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-restrictions').withMethod('PUT')
                .withBody(json(new JsonBuilder(user_restriction_map) as String)), once())
                .respond(response(new JsonBuilder(user_restriction_map_with_readonly) as String).withStatusCode(200))

        when:
        def result = factory.userRestrictionClient.setUserRestriction(user_restriction())

        then:
        !result.isNewCreated()
        matches result.result, user_restriction_with_readonly
    }

    def "deleteUserRestriction"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-restrictions/user-restriction-i1d').withMethod('DELETE'))
                .respond(response().withStatusCode(204))

        when:
        factory.userRestrictionClient.deleteUserRestriction('user-restriction-i1d')

        then:
        noExceptionThrown()
    }


    def "findUserRestrictions"() {
        setup:
        def user_restriction_map_one = [
                id         : '256',
                scope      : 'PROJECT',
                user_id    : 'user-i1d',
                will_expire: '2019-01-01T00:00:00',
                created    : '2016-03-28T18:16:00',
                project_id : 'p128'
        ]

        def user_restriction_map_two = [
                id         : '512',
                scope      : 'PROJECT',
                user_id    : 'user-i2d',
                will_expire: '2019-01-01T00:00:00',
                created    : '2016-02-28T18:16:00',
                project_id : 'p144'
        ]

        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-restrictions')
                .withQueryStringParameters(
                [
                        scope     : ['PROJECT'],
                        id_gt      : ['123'],
                        created_lte: ['2017-12-09T12:10:00'],
                        sort       : ['-created,id'],
                        limit      : ['50']
                ])
                .withHeader('Authorization', "OAuth abc"), once())
                .respond(response()
                .withStatusCode(200)
                .withBody(new JsonBuilder({
            items([
                    user_restriction_map_one,
                    user_restriction_map_two
            ])
            has_more false
        }).toString())
        )

        and:
        def userRestrictionRequest = UserRestrictionSearchRequest.make()
                .filter().byScope(UserRestrictionScope.PROJECT)
                .and()
                .range().byId('123').gt().byCreated(parseDate('2017-12-09 12:10:00')).lte()
                .and()
                .sort().byCreated().desc().byId().asc()
                .and()
                .limit(50)
                .done()

        when:
        def result = factory.userRestrictionClient.findUserRestrictions(userRestrictionRequest)

        then:
        !result.hasMore
        matches result.items.first(), new ProjectUserRestriction(
                'user-i1d',
                'p128',
                null,
                parseDate('2019-01-01 00:00:00')
        ).with {
            id = '256'
            created = parseDate('2016-03-28 18:16:00')
            it
        }
    }
}
