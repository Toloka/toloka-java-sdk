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

package ai.toloka.client.v1.skill;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class SkillSearchRequest extends SearchRequest {

    static final String ID_PARAMETER = "id";
    static final String OWNER_ID_PARAMETER = "owner_id";
    static final String OWNER_COMPANY_ID_PARAMETER = "owner_company_id";
    static final String CREATED_PARAMETER = "created";
    static final String GLOBAL_PARAMETER = "global";

    private SkillSearchRequest(Map<String, Object> filterParameters,
                               Map<String, Object> rangeParameters,
                               String sortParameter,
                               Integer limit) {
        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static SkillSearchRequest.SkillBuilder make() {
        return new SkillBuilder(new SkillFilterBuilder(), new SkillRangeBuilder(), new SkillSortBuilder());
    }

    public static class SkillBuilder extends SearchRequest.Builder<
            SkillSearchRequest, SkillBuilder, SkillFilterBuilder, SkillRangeBuilder, SkillSortBuilder> {

        private SkillBuilder(SkillFilterBuilder filterBuilder,
                             SkillRangeBuilder rangeBuilder,
                             SkillSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public SkillSearchRequest done() {
            return new SkillSearchRequest(
                    filterBuilder.getFilterParameters(),
                    rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(),
                    getLimit());
        }
    }

    public static class SkillFilterBuilder
            extends SearchRequest.FilterBuilder<SkillFilterBuilder, SkillBuilder, SkillFilterParam> {

        public SkillFilterBuilder byOwnerId(String ownerId) {
            return by(SkillFilterParam.ownerId, ownerId);
        }

        public SkillFilterBuilder byOwnerCompanyId(String ownerCompanyId) {
            return by(SkillFilterParam.ownerCompanyId, ownerCompanyId);
        }

        public SkillFilterBuilder byGlobal(Boolean global) {
            return by(SkillFilterParam.global, global);
        }
    }

    public static class SkillRangeBuilder
            extends SearchRequest.RangeBuilder<SkillRangeBuilder, SkillBuilder, SkillRangeParam> {

        public RangeItemBuilder<SkillRangeBuilder> byId(String id) {
            return by(SkillRangeParam.id, id);
        }

        public RangeItemBuilder<SkillRangeBuilder> byCreated(Date date) {
            return by(SkillRangeParam.created, date);
        }
    }

    public static class SkillSortBuilder
            extends SearchRequest.SortBuilder<SkillSortBuilder, SkillBuilder, SkillSortParam> {

        public SortItem<SkillSortBuilder> byId() {
            return by(SkillSortParam.id);
        }

        public SortItem<SkillSortBuilder> byCreated() {
            return by(SkillSortParam.created);
        }
    }
}
