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

package ai.toloka.client.v1.pool;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class PoolSearchRequest extends SearchRequest {

    static final String OWNER_ID_PARAMETER = "owner_id";
    static final String OWNER_COMPANY_ID_PARAMETER = "owner_company_id";
    static final String STATUS_PARAMETER = "status";
    static final String PROJECT_ID_PARAMETER = "project_id";
    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";
    static final String LAST_STARTED_PARAMETER = "last_started";


    public PoolSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                             String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static PoolBuilder make() {
        return new PoolBuilder(new PoolFilterBuilder(), new PoolRangeBuilder(), new PoolSortBuilder());
    }

    public static class PoolBuilder extends Builder<
            PoolSearchRequest, PoolBuilder, PoolFilterBuilder, PoolRangeBuilder, PoolSortBuilder> {

        public PoolBuilder(PoolFilterBuilder filterBuilder, PoolRangeBuilder rangeBuilder,
                           PoolSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public PoolSearchRequest done() {
            return new PoolSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class PoolFilterBuilder extends FilterBuilder<PoolFilterBuilder, PoolBuilder, PoolFilterParam> {

        public PoolFilterBuilder byOwnerId(String ownerId) {
            return by(PoolFilterParam.ownerId, ownerId);
        }

        public PoolFilterBuilder byOwnerCompanyId(String companyId) {
            return by(PoolFilterParam.ownerCompanyId, companyId);
        }

        public PoolFilterBuilder byStatus(PoolStatus status) {
            return by(PoolFilterParam.status, status);
        }

        public PoolFilterBuilder byProjectId(String projectId) {
            return by(PoolFilterParam.projectId, projectId);
        }
    }

    public static class PoolRangeBuilder extends RangeBuilder<PoolRangeBuilder, PoolBuilder, PoolRangeParam> {

        public RangeItemBuilder<PoolRangeBuilder> byId(String id) {
            return by(PoolRangeParam.id, id);
        }

        public RangeItemBuilder<PoolRangeBuilder> byCreated(Date created) {
            return by(PoolRangeParam.created, created);
        }

        public RangeItemBuilder<PoolRangeBuilder> byLastStarted(Date lastStarted) {
            return by(PoolRangeParam.lastStarted, lastStarted);
        }
    }

    public static class PoolSortBuilder extends SortBuilder<PoolSortBuilder, PoolBuilder, PoolSortParam> {

        public SortItem<PoolSortBuilder> byId() {
            return by(PoolSortParam.id);
        }

        public SortItem<PoolSortBuilder> byCreated() {
            return by(PoolSortParam.created);
        }

        public SortItem<PoolSortBuilder> byLastStarted() {
            return by(PoolSortParam.lastStarted);
        }
    }
}
