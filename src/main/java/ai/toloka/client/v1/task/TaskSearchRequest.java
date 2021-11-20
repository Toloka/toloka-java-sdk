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

package ai.toloka.client.v1.task;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class TaskSearchRequest extends SearchRequest {

    static final String OWNER_ID_PARAMETER = "owner_id";
    static final String OWNER_COMPANY_ID_PARAMETER = "owner_company_id";
    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";

    static final String POOL_ID_PARAMETER = "pool_id";
    static final String OVERLAP = "overlap";

    private TaskSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                              String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static TaskSearchRequest.TaskBuilder make() {
        return new TaskBuilder(new TaskFilterBuilder(), new TaskRangeBuilder(), new TaskSortBuilder());
    }

    public static class TaskBuilder extends SearchRequest.Builder<
            TaskSearchRequest, TaskBuilder, TaskFilterBuilder, TaskRangeBuilder, TaskSortBuilder> {

        private TaskBuilder(TaskFilterBuilder filterBuilder, TaskRangeBuilder rangeBuilder,
                            TaskSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public TaskSearchRequest done() {
            return new TaskSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class TaskFilterBuilder
            extends SearchRequest.FilterBuilder<TaskFilterBuilder, TaskBuilder, TaskFilterParam> {

        public TaskFilterBuilder byOwnerId(String ownerId) {
            return by(TaskFilterParam.ownerId, ownerId);
        }

        public TaskFilterBuilder byOwnerCompanyId(String companyId) {
            return by(TaskFilterParam.ownerCompanyId, companyId);
        }

        public TaskFilterBuilder byPoolId(String poolId) {
            return by(TaskFilterParam.poolId, poolId);
        }

        /**
         * @since 65
         */
        public TaskFilterBuilder byOverlap(Integer overlap) {
            return by(TaskFilterParam.overlap, overlap);
        }
    }

    public static class TaskRangeBuilder
            extends SearchRequest.RangeBuilder<TaskRangeBuilder, TaskBuilder, TaskRangeParam> {

        public RangeItemBuilder<TaskRangeBuilder> byId(String id) {
            return by(TaskRangeParam.id, id);
        }

        public RangeItemBuilder<TaskRangeBuilder> byCreated(Date created) {
            return by(TaskRangeParam.created, created);
        }

        public RangeItemBuilder<TaskRangeBuilder> byOverlap(Integer overlap) {
            return by(TaskRangeParam.overlap, overlap);
        }
    }

    public static class TaskSortBuilder extends SearchRequest.SortBuilder<TaskSortBuilder, TaskBuilder, TaskSortParam> {

        public SortItem<TaskSortBuilder> byId() {
            return by(TaskSortParam.id);
        }

        public SortItem<TaskSortBuilder> byCreated() {
            return by(TaskSortParam.created);
        }
    }
}
