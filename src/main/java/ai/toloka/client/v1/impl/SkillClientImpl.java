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

package ai.toloka.client.v1.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.transport.TransportUtil;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.skill.Skill;
import ai.toloka.client.v1.skill.SkillClient;
import ai.toloka.client.v1.skill.SkillSearchRequest;

import static ai.toloka.client.v1.impl.transport.MapperUtil.getObjectReader;

public class SkillClientImpl extends AbstractClientImpl implements SkillClient {

    private static final String SKILLS_PATH = "skills";

    SkillClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<Skill> findSkills(final SkillSearchRequest request) {
        return find(request, SKILLS_PATH, new TypeReference<SearchResult<Skill>>() {});
    }

    @Override
    public Skill getSkill(final String skillId) {
        return get(skillId, SKILLS_PATH, Skill.class);
    }

    @Override
    public ModificationResult<Skill> createSkill(final Skill skill) {
        Assertions.checkArgNotNull(skill, "Skill may not be null");

        return create(skill, SKILLS_PATH, Skill.class, null);
    }

    @Override
    public ModificationResult<Skill> updateSkill(final String skillId, final Skill skill) {
        Assertions.checkArgNotNull(skillId, "Skill id may not be null");
        Assertions.checkArgNotNull(skill, "Skill may not be null");

        return new RequestExecutorWrapper<ModificationResult<Skill>>() {

            @Override
            ModificationResult<Skill> execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), SKILLS_PATH, skillId).build();

                HttpResponse response = TransportUtil.executePut(getHttpClient(), uri, getHttpConsumer(), skill);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return new ModificationResult<>(
                            getObjectReader(Skill.class).readValue(response.getEntity().getContent()), false);
                }

                throw parseException(response);
            }
        }.wrap();
    }
}
