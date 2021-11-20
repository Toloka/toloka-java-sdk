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

package ai.toloka.client.v1.operation;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class OperationSearchRequest extends SearchRequest {

    public static final String TYPE_PARAMETER = "type";
    public static final String STATUS_PARAMETER = "status";

    public static final String ID_PARAMETER = "id";
    public static final String SUBMITTED_PARAMETER = "submitted";
    public static final String FINISHED_PARAMETER = "finished";

    private OperationSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                   String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static OperationBuilder make() {
        return new OperationBuilder(
                new OperationFilterBuilder(), new OperationRangeBuilder(), new OperationSortBuilder());
    }

    public static class OperationBuilder extends Builder<
            OperationSearchRequest,
            OperationBuilder,
            OperationFilterBuilder,
            OperationRangeBuilder,
            OperationSortBuilder> {

        private OperationBuilder(OperationFilterBuilder filterBuilder, OperationRangeBuilder rangeBuilder,
                                 OperationSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public OperationSearchRequest done() {
            return new OperationSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class OperationFilterBuilder
            extends FilterBuilder<OperationFilterBuilder, OperationBuilder, OperationFilterParam> {

        public OperationFilterBuilder byType(OperationType type) {
            return by(OperationFilterParam.type, type);
        }

        public OperationFilterBuilder byStatus(OperationStatus status) {
            return by(OperationFilterParam.status, status);
        }
    }

    public static class OperationRangeBuilder
            extends RangeBuilder<OperationRangeBuilder, OperationBuilder, OperationRangeParam> {

        public RangeItemBuilder<OperationRangeBuilder> byId(String id) {
            return by(OperationRangeParam.id, id);
        }

        public RangeItemBuilder<OperationRangeBuilder> bySubmitted(Date submitted) {
            return by(OperationRangeParam.submitted, submitted);
        }

        public RangeItemBuilder<OperationRangeBuilder> byFinished(Date finished) {
            return by(OperationRangeParam.finished, finished);
        }
    }

    public static class OperationSortBuilder
            extends SortBuilder<OperationSortBuilder, OperationBuilder, OperationSortParam> {

        public SortItem<OperationSortBuilder> byId() {
            return by(OperationSortParam.id);
        }

        public SortItem<OperationSortBuilder> bySubmitted() {
            return by(OperationSortParam.submitted);
        }

        public SortItem<OperationSortBuilder> byFinished() {
            return by(OperationSortParam.finished);
        }
    }
}
