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

package ai.toloka.client.v1.training;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class TrainingSearchRequest extends SearchRequest {

    static final String OWNER_ID_PARAMETER = "owner_id";
    static final String OWNER_COMPANY_ID_PARAMETER = "owner_company_id";
    static final String STATUS_PARAMETER = "status";
    static final String PROJECT_ID_PARAMETER = "project_id";
    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";
    static final String LAST_STARTED_PARAMETER = "last_started";


    public TrainingSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                 String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static TrainingBuilder make() {
        return new TrainingBuilder(new TrainingFilterBuilder(), new TrainingRangeBuilder(), new TrainingSortBuilder());
    }

    public static class TrainingBuilder extends Builder<
            TrainingSearchRequest, TrainingBuilder, TrainingFilterBuilder, TrainingRangeBuilder, TrainingSortBuilder> {

        public TrainingBuilder(TrainingFilterBuilder filterBuilder, TrainingRangeBuilder rangeBuilder,
                           TrainingSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public TrainingSearchRequest done() {
            return new TrainingSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class TrainingFilterBuilder extends FilterBuilder<TrainingFilterBuilder, TrainingBuilder,
            TrainingFilterParam> {

        public TrainingFilterBuilder byOwnerId(String ownerId) {
            return by(TrainingFilterParam.ownerId, ownerId);
        }

        public TrainingFilterBuilder byOwnerCompanyId(String companyId) {
            return by(TrainingFilterParam.ownerCompanyId, companyId);
        }

        public TrainingFilterBuilder byStatus(TrainingStatus status) {
            return by(TrainingFilterParam.status, status);
        }

        public TrainingFilterBuilder byProjectId(String projectId) {
            return by(TrainingFilterParam.projectId, projectId);
        }
    }

    public static class TrainingRangeBuilder extends RangeBuilder<TrainingRangeBuilder, TrainingBuilder,
            TrainingRangeParam> {

        public RangeItemBuilder<TrainingRangeBuilder> byId(String id) {
            return by(TrainingRangeParam.id, id);
        }

        public RangeItemBuilder<TrainingRangeBuilder> byCreated(Date created) {
            return by(TrainingRangeParam.created, created);
        }

        public RangeItemBuilder<TrainingRangeBuilder> byLastStarted(Date lastStarted) {
            return by(TrainingRangeParam.lastStarted, lastStarted);
        }
    }

    public static class TrainingSortBuilder extends SortBuilder<TrainingSortBuilder, TrainingBuilder,
            TrainingSortParam> {

        public SortItem<TrainingSortBuilder> byId() {
            return by(TrainingSortParam.id);
        }

        public SortItem<TrainingSortBuilder> byCreated() {
            return by(TrainingSortParam.created);
        }

        public SortItem<TrainingSortBuilder> byLastStarted() {
            return by(TrainingSortParam.lastStarted);
        }
    }
}
