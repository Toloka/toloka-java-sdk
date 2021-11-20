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

import ai.toloka.client.v1.userskill.*
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class UserSkillClientImplSpec extends AbstractClientSpec {

    def "findUserSkills"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-skills').withQueryStringParameters([
                skill_id   : ['skill-i1d'],
                created_gt : ['2016-03-25T00:00:00'],
                modified_lt: ['2017-03-25T00:00:00'],
                sort       : ['id'],
                limit      : ['100']
        ]), once())
                .respond(response(
                new JsonBuilder([items: [user_skill_map_with_readonly()], has_more: false]) as String))

        when:
        def request = UserSkillSearchRequest.make()
                .filter().by(UserSkillFilterParam.skillId, 'skill-i1d')
                .and()
                .range().by(UserSkillRangeParam.created, parseDate('2016-03-25 00:00:00'), ai.toloka.client.v1.RangeOperator.gt)
                .and()
                .range().by(UserSkillRangeParam.modified, parseDate('2017-03-25 00:00:00'), ai.toloka.client.v1.RangeOperator.lt)
                .and()
                .sort().by(UserSkillSortParam.id, ai.toloka.client.v1.SortDirection.asc)
                .and()
                .limit(100)
                .done()
        def result = factory.userSkillClient.findUsersSkills(request)

        then:
        matches result.items[0], user_skill_with_readonly()
    }

    def "getUserSkill"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-skills/user-skill-i1d'), once())
                .respond(response(new JsonBuilder(user_skill_map_with_readonly()) as String))

        when:
        def result = factory.userSkillClient.getUserSkill('user-skill-i1d')

        then:
        matches result, user_skill_with_readonly()
    }

    def "setUserSkill"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-skills').withMethod('PUT')
                .withBody(new JsonBuilder(set_user_skill_map()) as String), once())
                .respond(response(new JsonBuilder(user_skill_map_with_readonly()) as String).withStatusCode(201))

        when:
        def result = factory.userSkillClient.setUserSkill(user_skill())

        then:
        result.isNewCreated()
        matches result.result, user_skill_with_readonly()
    }

    def "deleteUserSkill"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/user-skills/user-skill-i1d').withMethod('DELETE'), once())
                .respond(response().withStatusCode(204))

        when:
        factory.userSkillClient.deleteUserSkill('user-skill-i1d')

        then:
        noExceptionThrown()
    }

    def set_user_skill_map() {
        [
                skill_id   : 'skill-i1d',
                user_id    : 'user-i1d',
                value      : 85.42
        ]
    }

    def user_skill_map() {
        [
                skill_id   : 'skill-i1d',
                user_id    : 'user-i1d',
                value      : 85,
                exact_value: 85.42
        ]
    }

    def user_skill_map_with_readonly() {
        user_skill_map() + [
                id      : 'user-skill-i1d',
                created : '2016-03-25T15:59:08',
                modified: '2017-03-24T15:59:08'
        ]
    }

    def user_skill() {
        UserSkill.valueOf('skill-i1d', 'user-i1d', 85.42)
    }

    def user_skill_with_readonly() {
        user_skill().with {
            id = 'user-skill-i1d'; created = parseDate('2016-03-25 15:59:08');
            modified = parseDate('2017-03-24 15:59:08'); it
        }
    }
}
