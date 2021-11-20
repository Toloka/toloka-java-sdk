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

package ai.toloka.client.v1.impl;

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.assignment.Assignment;
import ai.toloka.client.v1.assignment.AssignmentClient;
import ai.toloka.client.v1.assignment.AssignmentPatch;
import ai.toloka.client.v1.assignment.AssignmentSearchRequest;
import ai.toloka.client.v1.assignment.AssignmentStatus;
import ai.toloka.client.v1.impl.validation.Assertions;

public class AssignmentClientImpl extends AbstractClientImpl implements AssignmentClient {

    private static final String ASSIGNMENTS_PATH = "assignments";

    public AssignmentClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<Assignment> findAssignments(AssignmentSearchRequest request) {
        return find(request, ASSIGNMENTS_PATH, new TypeReference<SearchResult<Assignment>>() {});
    }

    @Override
    public Assignment getAssignment(final String assignmentId) {
        return get(assignmentId, ASSIGNMENTS_PATH, Assignment.class);
    }

    @Override
    public ModificationResult<Assignment> patchAssignment(final String assignmentId, final AssignmentPatch patch) {
        Assertions.checkArgNotNull(assignmentId, "Id may not be null");
        Assertions.checkArgNotNull(patch, "Patch may not be null");

        return patch(assignmentId, patch, ASSIGNMENTS_PATH, Assignment.class, null);
    }

    @Override
    public ModificationResult<Assignment> acceptAssignment(String assignmentId, String publicComment) {
        return patchAssignment(assignmentId, new AssignmentPatch(AssignmentStatus.ACCEPTED, publicComment));
    }

    @Override
    public ModificationResult<Assignment> rejectAssignment(String assignmentId, String publicComment) {
        return patchAssignment(assignmentId, new AssignmentPatch(AssignmentStatus.REJECTED, publicComment));
    }
}
