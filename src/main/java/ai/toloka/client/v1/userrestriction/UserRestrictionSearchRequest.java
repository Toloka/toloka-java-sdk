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

package ai.toloka.client.v1.userrestriction;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class UserRestrictionSearchRequest extends SearchRequest {

    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";

    static final String SCOPE_PARAMETER = "scope";
    static final String USER_ID_PARAMETER = "user_id";
    static final String PROJECT_ID_PARAMETER = "project_id";
    static final String POOL_ID_PARAMETER = "pool_id";


    private UserRestrictionSearchRequest(Map<String, Object> filterParameters,
                                         Map<String, Object> rangeParameters,
                                         String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static UserRestrictionBuilder make() {
        return new UserRestrictionBuilder(new UserRestrictionFilterBuilder(), new UserRestrictionRangeBuilder(),
                new UserRestrictionSortBuilder());
    }

    public static class UserRestrictionBuilder extends Builder<
            UserRestrictionSearchRequest, UserRestrictionBuilder, UserRestrictionFilterBuilder,
            UserRestrictionRangeBuilder, UserRestrictionSortBuilder> {

        private UserRestrictionBuilder(UserRestrictionFilterBuilder filterBuilder,
                                       UserRestrictionRangeBuilder rangeBuilder,
                                       UserRestrictionSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override
        public UserRestrictionSearchRequest done() {
            return new UserRestrictionSearchRequest(
                    filterBuilder.getFilterParameters(),
                    rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(),
                    getLimit());
        }
    }

    public static class UserRestrictionFilterBuilder
            extends FilterBuilder<UserRestrictionFilterBuilder, UserRestrictionBuilder, UserRestrictionFilterParam> {

        public UserRestrictionFilterBuilder byScope(UserRestrictionScope scope) {
            return by(UserRestrictionFilterParam.scope, scope);
        }

        public UserRestrictionFilterBuilder byUser(String userId) {
            return by(UserRestrictionFilterParam.userId, userId);
        }

        public UserRestrictionFilterBuilder byProject(String projectId) {
            return by(UserRestrictionFilterParam.projectId, projectId);
        }

        public UserRestrictionFilterBuilder byPool(String poolId) {
            return by(UserRestrictionFilterParam.poolId, poolId);
        }
    }

    public static class UserRestrictionRangeBuilder
            extends RangeBuilder<UserRestrictionRangeBuilder, UserRestrictionBuilder, UserRestrictionRangeParam> {

        public RangeItemBuilder<UserRestrictionRangeBuilder> byId(String id) {
            return by(UserRestrictionRangeParam.id, id);
        }

        public RangeItemBuilder<UserRestrictionRangeBuilder> byCreated(Date date) {
            return by(UserRestrictionRangeParam.created, date);
        }
    }

    public static class UserRestrictionSortBuilder
            extends SortBuilder<UserRestrictionSortBuilder, UserRestrictionBuilder, UserRestrictionSortParam> {

        public SortItem<UserRestrictionSortBuilder> byId() {
            return by(UserRestrictionSortParam.id);
        }

        public SortItem<UserRestrictionSortBuilder> byCreated() {
            return by(UserRestrictionSortParam.created);
        }
    }
}
