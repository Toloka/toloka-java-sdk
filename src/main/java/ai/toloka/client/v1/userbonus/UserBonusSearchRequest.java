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

package ai.toloka.client.v1.userbonus;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class UserBonusSearchRequest extends SearchRequest {

    public static final String USER_ID_PARAMETER = "user_id";
    public static final String PRIVATE_COMMENT_PARAMETER = "private_comment";

    public static final String ID_PARAMETER = "id";
    public static final String CREATED_PARAMETER = "created";

    private UserBonusSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                   String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static UserBonusBuilder make() {
        return new UserBonusBuilder(
                new UserBonusFilterBuilder(), new UserBonusRangeBuilder(), new UserBonusSortBuilder());
    }

    public static class UserBonusBuilder extends Builder<
            UserBonusSearchRequest,
            UserBonusBuilder,
            UserBonusFilterBuilder,
            UserBonusRangeBuilder,
            UserBonusSortBuilder> {

        private UserBonusBuilder(UserBonusFilterBuilder filterBuilder, UserBonusRangeBuilder rangeBuilder,
                                 UserBonusSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public UserBonusSearchRequest done() {
            return new UserBonusSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class UserBonusFilterBuilder
            extends FilterBuilder<UserBonusFilterBuilder, UserBonusBuilder, UserBonusFilterParam> {

        public UserBonusFilterBuilder byUserId(String userId) {
            return by(UserBonusFilterParam.userId, userId);
        }

        public UserBonusFilterBuilder byPrivateComment(String privateComment) {
            return by(UserBonusFilterParam.privateComment, privateComment);
        }
    }

    public static class UserBonusRangeBuilder
            extends RangeBuilder<UserBonusRangeBuilder, UserBonusBuilder, UserBonusRangeParam> {

        public RangeItemBuilder<UserBonusRangeBuilder> byId(String id) {
            return by(UserBonusRangeParam.id, id);
        }

        public RangeItemBuilder<UserBonusRangeBuilder> byCreated(Date created) {
            return by(UserBonusRangeParam.created, created);
        }
    }

    public static class UserBonusSortBuilder
            extends SortBuilder<UserBonusSortBuilder, UserBonusBuilder, UserBonusSortParam> {

        public SortItem<UserBonusSortBuilder> byId() {
            return by(UserBonusSortParam.id);
        }

        public SortItem<UserBonusSortBuilder> byCreated() {
            return by(UserBonusSortParam.created);
        }
    }
}
