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

package ai.toloka.client.v1.userskill;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class UserSkillSearchRequest extends SearchRequest {

    static final String SKILL_ID_PARAMETER = "skill_id";
    static final String USER_ID_PARAMETER = "user_id";
    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";
    static final String MODIFIED_PARAMETER = "modified";

    public UserSkillSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                  String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static UserSkillBuilder make() {
        return new UserSkillBuilder(
                new UserSkillFilterBuilder(), new UserSkillRangeBuilder(), new UserSkillSortBuilder());
    }

    public static class UserSkillBuilder extends Builder<
            UserSkillSearchRequest, UserSkillBuilder, UserSkillFilterBuilder, UserSkillRangeBuilder,
            UserSkillSortBuilder> {

        public UserSkillBuilder(UserSkillFilterBuilder filterBuilder, UserSkillRangeBuilder rangeBuilder,
                                UserSkillSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public UserSkillSearchRequest done() {
            return new UserSkillSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class UserSkillFilterBuilder
            extends FilterBuilder<UserSkillFilterBuilder, UserSkillBuilder, UserSkillFilterParam> {

        public UserSkillFilterBuilder bySkillId(String skillId) {
            return by(UserSkillFilterParam.skillId, skillId);
        }

        public UserSkillFilterBuilder byUserId(String userId) {
            return by(UserSkillFilterParam.userId, userId);
        }
    }

    public static class UserSkillRangeBuilder
            extends RangeBuilder<UserSkillRangeBuilder, UserSkillBuilder, UserSkillRangeParam> {

        public RangeItemBuilder<UserSkillRangeBuilder> byId(String id) {
            return by(UserSkillRangeParam.id, id);
        }

        public RangeItemBuilder<UserSkillRangeBuilder> byCreated(Date created) {
            return by(UserSkillRangeParam.created, created);
        }

        public RangeItemBuilder<UserSkillRangeBuilder> byModified(Date modified) {
            return by(UserSkillRangeParam.modified, modified);
        }
    }

    public static class UserSkillSortBuilder
            extends SortBuilder<UserSkillSortBuilder, UserSkillBuilder, UserSkillSortParam> {

        public SortItem<UserSkillSortBuilder> byId() {
            return by(UserSkillSortParam.id);
        }

        public SortItem<UserSkillSortBuilder> byCreated() {
            return by(UserSkillSortParam.created);
        }
    }
}
