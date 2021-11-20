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

package ai.toloka.client.v1.tasksuite;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class TaskSuiteSearchRequest extends SearchRequest {

    static final String OWNER_ID_PARAMETER = "owner_id";
    static final String OWNER_COMPANY_ID_PARAMETER = "owner_company_id";
    static final String TASK_ID_PARAMETER = "task_id";
    static final String POOL_ID_PARAMETER = "pool_id";
    static final String OVERLAP = "overlap";

    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";

    private TaskSuiteSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                   String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static TaskSuiteBuilder make() {
        return new TaskSuiteBuilder(
                new TaskSuiteFilterBuilder(), new TaskSuiteRangeBuilder(), new TaskSuiteSortBuilder());
    }

    public static class TaskSuiteBuilder extends Builder<TaskSuiteSearchRequest, TaskSuiteBuilder,
            TaskSuiteFilterBuilder, TaskSuiteRangeBuilder, TaskSuiteSortBuilder> {

        public TaskSuiteBuilder(TaskSuiteFilterBuilder filterBuilder, TaskSuiteRangeBuilder rangeBuilder,
                                TaskSuiteSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public TaskSuiteSearchRequest done() {
            return new TaskSuiteSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class TaskSuiteFilterBuilder
            extends FilterBuilder<TaskSuiteFilterBuilder, TaskSuiteBuilder, TaskSuiteFilterParam> {

        public TaskSuiteFilterBuilder byOwnerId(String ownerId) {
            return by(TaskSuiteFilterParam.owner_id, ownerId);
        }

        public TaskSuiteFilterBuilder byOwnerCompanyId(String companyId) {
            return by(TaskSuiteFilterParam.owner_company_id, companyId);
        }

        public TaskSuiteFilterBuilder byTaskId(String taskId) {
            return by(TaskSuiteFilterParam.task_id, taskId);
        }

        public TaskSuiteFilterBuilder byPoolId(String poolId) {
            return by(TaskSuiteFilterParam.pool_id, poolId);
        }

        /**
         * @since 65
         */
        public TaskSuiteFilterBuilder byOverlap(Integer overlap) {
            return by(TaskSuiteFilterParam.overlap, overlap);
        }
    }

    public static class TaskSuiteRangeBuilder
            extends RangeBuilder<TaskSuiteRangeBuilder, TaskSuiteBuilder, TaskSuiteRangeParam> {

        public RangeItemBuilder<TaskSuiteRangeBuilder> byId(String id) {
            return by(TaskSuiteRangeParam.id, id);
        }

        public RangeItemBuilder<TaskSuiteRangeBuilder> byCreated(Date created) {
            return by(TaskSuiteRangeParam.created, created);
        }

        /**
         * @since 65
         */
        public RangeItemBuilder<TaskSuiteRangeBuilder> byOverlap(Integer overlap) {
            return by(TaskSuiteRangeParam.overlap, overlap);
        }
    }

    public static class TaskSuiteSortBuilder
            extends SortBuilder<TaskSuiteSortBuilder, TaskSuiteBuilder, TaskSuiteSortParam> {

        public SortItem<TaskSuiteSortBuilder> byId() {
            return by(TaskSuiteSortParam.id);
        }

        public SortItem<TaskSuiteSortBuilder> byCreated() {
            return by(TaskSuiteSortParam.created);
        }
    }
}
