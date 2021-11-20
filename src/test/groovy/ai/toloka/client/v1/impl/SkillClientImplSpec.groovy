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

import static ai.toloka.client.v1.LangIso639.EN
import static ai.toloka.client.v1.LangIso639.RU
import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class SkillClientImplSpec extends AbstractClientSpec {

    def "findSkills"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/skills')
                .withQueryStringParameters([
                id_gt: ['20'],
                sort : ['-created,id']
        ]), once())
                .respond(response(new JsonBuilder([items: [skill_map_with_readonly()], has_more: true]).toString()))

        and:
        def request = ai.toloka.client.v1.skill.SkillSearchRequest.make()
                .range()
                .by(ai.toloka.client.v1.skill.SkillRangeParam.id, '20', ai.toloka.client.v1.RangeOperator.gt)
                .and()
                .sort().by(ai.toloka.client.v1.skill.SkillSortParam.created, ai.toloka.client.v1.SortDirection.desc).by(ai.toloka.client.v1.skill.SkillSortParam.id, ai.toloka.client.v1.SortDirection.asc)
                .and()
                .done()

        when:
        def result = factory.skillClient.findSkills(request)

        then:
        result.hasMore
        matches result.items.first(), skill_with_readonly()
    }

    def "getSkill"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/skills/21'), once())
                .respond(response(new JsonBuilder(skill_map_with_readonly()).toString()))

        when:
        def result = factory.skillClient.getSkill('21')

        then:
        matches result, skill_with_readonly()
    }

    def "createSkill"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/skills').withBody(json(new JsonBuilder(skill_map()).toString())), once())
                .respond(response(new JsonBuilder(skill_map_with_readonly()).toString()).withStatusCode(201))

        when:
        def result = factory.skillClient.createSkill(skill())

        then:
        matches result.result, skill_with_readonly()

    }

    def "updateSkill"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/skills/21').withBody(json(new JsonBuilder(skill_map()).toString())), once())
                .respond(response(new JsonBuilder(skill_map_with_readonly()).toString()).withStatusCode(200))

        when:
        def result = factory.skillClient.updateSkill('21', skill())

        then:
        matches result.result, skill_with_readonly()

    }

    def skill_map() {
        [
                name                        : 'Skill name',
                private_comment             : 'Private comment',
                hidden                      : false,
                public_requester_description: [EN:'Skill description', RU:'Описание навыка'],
                deprecated                  : false
        ]
    }

    def skill_map_with_readonly() {
        skill_map() + [
                id     : '21',
                created: '2015-12-16T12:55:01'
        ]
    }

    def skill() {
        new ai.toloka.client.v1.skill.Skill('Skill name').with {
            privateComment = 'Private comment'
            hidden = false
            publicRequesterDescription = [(EN):'Skill description', (RU):'Описание навыка']
            deprecated = false
            it
        }
    }

    def skill_with_readonly() {
        skill().with {
            id = '21'
            created = parseDate('2015-12-16 12:55:01')
            it
        }
    }
}
