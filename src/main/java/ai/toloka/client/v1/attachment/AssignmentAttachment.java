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

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignmentAttachment extends Attachment<AssignmentAttachment.Details> {

    public static class Details {

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("assignment_id")
        private String assignmentId;

        @JsonProperty("pool_id")
        private String poolId;

        public String getUserId() {
            return userId;
        }

        public String getAssignmentId() {
            return assignmentId;
        }

        public String getPoolId() {
            return poolId;
        }
    }
}
