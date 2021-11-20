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

package ai.toloka.client.v1.aggregatedsolutions;

import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class AggregatedSolutionSearchRequest extends SearchRequest {

    static final String TASK_ID_PARAMETER = "task_id";

    public AggregatedSolutionSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                           String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static AggregatedAssignmentsBuilder make() {
        return new AggregatedAssignmentsBuilder(
                new AssignmentFilterBuilder(), new AggregatedAssignmentsRangeBuilder(),
                new AggregatedAssignmentsSortBuilder());
    }

    public static class AggregatedAssignmentsBuilder extends Builder
            <AggregatedSolutionSearchRequest, AggregatedAssignmentsBuilder, AssignmentFilterBuilder,
                    AggregatedAssignmentsRangeBuilder, AggregatedAssignmentsSortBuilder> {

        public AggregatedAssignmentsBuilder(
                AssignmentFilterBuilder filterBuilder,
                AggregatedAssignmentsRangeBuilder rangeBuilder,
                AggregatedAssignmentsSortBuilder sortBuilder
        ) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public AggregatedSolutionSearchRequest done() {
            return new AggregatedSolutionSearchRequest(filterBuilder.getFilterParameters(),
                    rangeBuilder.getRangeParameters(), sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class AssignmentFilterBuilder
            extends FilterBuilder<AssignmentFilterBuilder, AggregatedAssignmentsBuilder,
            AggregatedSolutionFilterParam> {
    }

    public static class AggregatedAssignmentsRangeBuilder
            extends RangeBuilder<AggregatedAssignmentsRangeBuilder, AggregatedAssignmentsBuilder,
            AggregatedSolutionRangeParam> {

        public RangeItemBuilder<AggregatedAssignmentsRangeBuilder> byTaskId(String taskId) {
            return by(AggregatedSolutionRangeParam.taskId, taskId);
        }
    }

    public static class AggregatedAssignmentsSortBuilder
            extends SortBuilder<AggregatedAssignmentsSortBuilder, AggregatedAssignmentsBuilder,
            AggregatedSolutionSortParam> {

        public SortItem<AggregatedAssignmentsSortBuilder> byTaskId() {
            return by(AggregatedSolutionSortParam.taskId);
        }
    }
}
