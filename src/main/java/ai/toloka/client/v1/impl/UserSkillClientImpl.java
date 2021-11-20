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
import java.math.BigDecimal;
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
import ai.toloka.client.v1.userskill.UserSkill;
import ai.toloka.client.v1.userskill.UserSkillClient;
import ai.toloka.client.v1.userskill.UserSkillSearchRequest;

import static ai.toloka.client.v1.impl.transport.MapperUtil.getObjectReader;

public class UserSkillClientImpl extends AbstractClientImpl implements UserSkillClient {

    private static final String USER_SKILLS_PATH = "user-skills";

    public UserSkillClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<UserSkill> findUsersSkills(UserSkillSearchRequest request) {
        return find(request, USER_SKILLS_PATH, new TypeReference<SearchResult<UserSkill>>() {});
    }

    @Override
    public UserSkill getUserSkill(final String userSkillId) {
        return get(userSkillId, USER_SKILLS_PATH, UserSkill.class);
    }

    @Override
    public ModificationResult<UserSkill> setUserSkill(final UserSkill userSkill) {
        return setUserSkill(userSkill, null);
    }

    @Override
    public ModificationResult<UserSkill> setUserSkill(final UserSkill userSkill, final String reason) {
        Assertions.checkArgNotNull(userSkill, "User skill may not be null");

        return new RequestExecutorWrapper<ModificationResult<UserSkill>>() {

            @Override
            ModificationResult<UserSkill> execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), USER_SKILLS_PATH).build();

                HttpResponse response = TransportUtil.executePut(getHttpClient(), uri, getHttpConsumer(),
                        new UserSkillUpdate(
                                userSkill.getId(),
                                userSkill.getSkillId(),
                                userSkill.getUserId(),
                                userSkill.getExactValue(),
                                reason
                        )
                );

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
                        || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {

                    return new ModificationResult<>(
                            getObjectReader(UserSkill.class).readValue(response.getEntity().getContent()),
                            response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED);
                }

                throw parseException(response);
            }
        }.wrap();
    }

    @Override
    public void deleteUserSkill(final String userSkillId) {
        delete(userSkillId, USER_SKILLS_PATH);
    }

    private static class UserSkillUpdate {
        public final String id;
        public final String skill_id;
        public final String user_id;
        public final BigDecimal value;
        public final String reason;

        public UserSkillUpdate(String id, String skillId, String userId, BigDecimal exactValue, String reason) {
            this.id = id;
            this.skill_id = skillId;
            this.user_id = userId;
            this.value = exactValue;
            this.reason = reason;
        }
    }
}
