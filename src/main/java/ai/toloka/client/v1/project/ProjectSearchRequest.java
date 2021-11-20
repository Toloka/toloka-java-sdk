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

package ai.toloka.client.v1.project;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class ProjectSearchRequest extends SearchRequest {

    static final String STATUS_PARAMETER = "status";
    static final String OWNER_ID_PARAMETER = "owner_id";
    static final String OWNER_COMPANY_ID_PARAMETER = "owner_company_id";
    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";
    static final String PUBLIC_NAME_PARAMETER = "public_name";
    static final String PRIVATE_COMMENT_PARAMETER = "private_comment";

    private ProjectSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                 String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static ProjectBuilder make() {
        return new ProjectBuilder(new ProjectFilterBuilder(), new ProjectRangeBuilder(), new ProjectSortBuilder());
    }

    public static class ProjectBuilder extends Builder<
            ProjectSearchRequest, ProjectBuilder, ProjectFilterBuilder, ProjectRangeBuilder, ProjectSortBuilder> {

        private ProjectBuilder(ProjectFilterBuilder filterBuilder, ProjectRangeBuilder rangeBuilder,
                               ProjectSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public ProjectSearchRequest done() {
            return new ProjectSearchRequest(
                    filterBuilder.getFilterParameters(),
                    rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(),
                    getLimit());
        }
    }

    public static class ProjectFilterBuilder
            extends FilterBuilder<ProjectFilterBuilder, ProjectBuilder, ProjectFilterParam> {

        public ProjectFilterBuilder byStatus(ProjectStatus status) {
            return by(ProjectFilterParam.status, status);
        }

        public ProjectFilterBuilder byOwnerId(String ownerId) {
            return by(ProjectFilterParam.ownerId, ownerId);
        }

        public ProjectFilterBuilder byOwnerCompanyId(String ownerCompanyId) {
            return by(ProjectFilterParam.ownerCompanyId, ownerCompanyId);
        }
    }

    public static class ProjectRangeBuilder
            extends RangeBuilder<ProjectRangeBuilder, ProjectBuilder, ProjectRangeParam> {

        public RangeItemBuilder<ProjectRangeBuilder> byId(String id) {
            return by(ProjectRangeParam.id, id);
        }

        public RangeItemBuilder<ProjectRangeBuilder> byCreated(Date date) {
            return by(ProjectRangeParam.created, date);
        }
    }

    public static class ProjectSortBuilder extends SortBuilder<ProjectSortBuilder, ProjectBuilder, ProjectSortParam> {

        public SortItem<ProjectSortBuilder> byId() {
            return by(ProjectSortParam.id);
        }

        public SortItem<ProjectSortBuilder> byCreated() {
            return by(ProjectSortParam.created);
        }

        public SortItem<ProjectSortBuilder> byPublicName() {
            return by(ProjectSortParam.publicName);
        }

        public SortItem<ProjectSortBuilder> byPrivateComment() {
            return by(ProjectSortParam.privateComment);
        }
    }
}
