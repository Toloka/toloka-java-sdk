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

import ai.toloka.client.v1.FilterParam;

public enum AttachmentFilterParam implements FilterParam {

    ownerId(AttachmentSearchRequest.OWNER_ID_PARAMETER),
    ownerCompanyId(AttachmentSearchRequest.OWNER_COMPANY_ID_PARAMETER),
    name(AttachmentSearchRequest.NAME_PARAMETER),
    attachmentType(AttachmentSearchRequest.ATTACHMENT_TYPE_PARAMETER),
    userId(AttachmentSearchRequest.USER_ID_PARAMETER),
    assignmentId(AttachmentSearchRequest.ASSIGNMENT_ID_PARAMETER),
    poolId(AttachmentSearchRequest.POOL_ID_PARAMETER);

    private String parameter;

    AttachmentFilterParam(String parameter) {
        this.parameter = parameter;
    }

    @Override public String parameter() {
        return parameter;
    }
}
