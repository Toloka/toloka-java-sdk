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

package ai.toloka.client.v1.assignment;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class AssignmentSearchRequest extends SearchRequest {

    static final String STATUS_PARAMETER = "status";
    static final String TASK_ID_PARAMETER = "task_id";
    static final String TASK_SUITE_ID_PARAMETER = "task_suite_id";
    static final String POOL_ID_PARAMETER = "pool_id";
    static final String USER_ID_PARAMETER = "user_id";
    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";
    static final String SUBMITTED_PARAMETER = "submitted";

    public AssignmentSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                   String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static AssignmentBuilder make() {
        return new AssignmentBuilder(
                new AssignmentFilterBuilder(), new AssignmentRangeBuilder(), new AssignmentSortBuilder());
    }

    public static class AssignmentBuilder extends Builder
            <AssignmentSearchRequest, AssignmentBuilder, AssignmentFilterBuilder, AssignmentRangeBuilder,
                    AssignmentSortBuilder> {

        public AssignmentBuilder(AssignmentFilterBuilder filterBuilder, AssignmentRangeBuilder rangeBuilder,
                                 AssignmentSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public AssignmentSearchRequest done() {
            return new AssignmentSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class AssignmentFilterBuilder
            extends FilterBuilder<AssignmentFilterBuilder, AssignmentBuilder, AssignmentFilterParam> {

        public AssignmentFilterBuilder byStatus(AssignmentStatus status) {
            return by(AssignmentFilterParam.status, status);
        }

        public AssignmentFilterBuilder byTaskId(String taskId) {
            return by(AssignmentFilterParam.taskId, taskId);
        }

        public AssignmentFilterBuilder byTaskSuiteId(String taskSuiteId) {
            return by(AssignmentFilterParam.taskSuiteId, taskSuiteId);
        }

        public AssignmentFilterBuilder byPoolId(String poolId) {
            return by(AssignmentFilterParam.poolId, poolId);
        }

        public AssignmentFilterBuilder byUserId(String userId) {
            return by(AssignmentFilterParam.userId, userId);
        }
    }

    public static class AssignmentRangeBuilder
            extends RangeBuilder<AssignmentRangeBuilder, AssignmentBuilder, AssignmentRangeParam> {

        public RangeItemBuilder<AssignmentRangeBuilder> byId(String id) {
            return by(AssignmentRangeParam.id, id);
        }

        public RangeItemBuilder<AssignmentRangeBuilder> byCreated(Date created) {
            return by(AssignmentRangeParam.created, created);
        }

        public RangeItemBuilder<AssignmentRangeBuilder> bySubmitted(Date submitted) {
            return by(AssignmentRangeParam.submitted, submitted);
        }
    }

    public static class AssignmentSortBuilder
            extends SortBuilder<AssignmentSortBuilder, AssignmentBuilder, AssignmentSortParam> {

        public SortItem<AssignmentSortBuilder> byId() {
            return by(AssignmentSortParam.id);
        }

        public SortItem<AssignmentSortBuilder> byCreated() {
            return by(AssignmentSortParam.created);
        }

        public SortItem<AssignmentSortBuilder> bySubmitted() {
            return by(AssignmentSortParam.submitted);
        }
    }
}
