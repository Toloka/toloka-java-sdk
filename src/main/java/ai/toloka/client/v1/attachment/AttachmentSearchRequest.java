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

package ai.toloka.client.v1.attachment;

import java.util.Date;
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class AttachmentSearchRequest extends SearchRequest {

    static final String OWNER_ID_PARAMETER = "owner_id";
    static final String OWNER_COMPANY_ID_PARAMETER = "owner_company_id";
    public static final String NAME_PARAMETER = "name";
    public static final String ATTACHMENT_TYPE_PARAMETER = "type";
    public static final String USER_ID_PARAMETER = "user_id";
    public static final String ASSIGNMENT_ID_PARAMETER = "assignment_id";
    public static final String POOL_ID_PARAMETER = "pool_id";

    public static final String ID_PARAMETER = "id";
    public static final String CREATED_PARAMETER = "created";

    private AttachmentSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                    String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static AttachmentBuilder make() {
        return new AttachmentBuilder(
                new AttachmentFilterBuilder(), new AttachmentRangeBuilder(), new AttachmentSortBuilder());
    }

    public static class AttachmentBuilder extends Builder<
            AttachmentSearchRequest,
            AttachmentBuilder,
            AttachmentFilterBuilder,
            AttachmentRangeBuilder,
            AttachmentSortBuilder> {

        private AttachmentBuilder(AttachmentFilterBuilder filterBuilder, AttachmentRangeBuilder rangeBuilder,
                                  AttachmentSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public AttachmentSearchRequest done() {
            return new AttachmentSearchRequest(filterBuilder.getFilterParameters(), rangeBuilder.getRangeParameters(),
                    sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class AttachmentFilterBuilder
            extends FilterBuilder<AttachmentFilterBuilder, AttachmentBuilder, AttachmentFilterParam> {

        public AttachmentFilterBuilder byOwnerId(String ownerId) {
            return by(AttachmentFilterParam.ownerId, ownerId);
        }

        public AttachmentFilterBuilder byOwnerCompanyId(String ownerCompanyId) {
            return by(AttachmentFilterParam.ownerCompanyId, ownerCompanyId);
        }

        public AttachmentFilterBuilder byName(String name) {
            return by(AttachmentFilterParam.name, name);
        }

        public AttachmentFilterBuilder byAttachmentType(AttachmentType attachmentType) {
            return by(AttachmentFilterParam.attachmentType, attachmentType);
        }

        public AttachmentFilterBuilder byUserId(String userId) {
            return by(AttachmentFilterParam.userId, userId);
        }

        public AttachmentFilterBuilder byAssignmentId(String assignmentId) {
            return by(AttachmentFilterParam.assignmentId, assignmentId);
        }

        public AttachmentFilterBuilder byPoolId(String poolId) {
            return by(AttachmentFilterParam.poolId, poolId);
        }
    }

    public static class AttachmentRangeBuilder
            extends RangeBuilder<AttachmentRangeBuilder, AttachmentBuilder, AttachmentRangeParam> {

        public RangeItemBuilder<AttachmentRangeBuilder> byId(String id) {
            return by(AttachmentRangeParam.id, id);
        }

        public RangeItemBuilder<AttachmentRangeBuilder> byCreated(Date created) {
            return by(AttachmentRangeParam.created, created);
        }
    }

    public static class AttachmentSortBuilder
            extends SortBuilder<AttachmentSortBuilder, AttachmentBuilder, AttachmentSortParam> {

        public SortItem<AttachmentSortBuilder> byId() {
            return by(AttachmentSortParam.id);
        }

        public SortItem<AttachmentSortBuilder> byCreated() {
            return by(AttachmentSortParam.created);
        }
    }
}
