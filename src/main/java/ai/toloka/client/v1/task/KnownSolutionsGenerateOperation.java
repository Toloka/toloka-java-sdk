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

package ai.toloka.client.v1.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.operation.OperationType;

public class KnownSolutionsGenerateOperation extends
        Operation<KnownSolutionsGenerateOperation.Parameters, KnownSolutionsGenerateOperation> {

    public static final OperationType TYPE = OperationType.KNOWN_SOLUTIONS_GENERATE;

    public static class Parameters {

        @JsonProperty("pool_id")
        private String poolId;

        private String poolName;

        @JsonProperty("project_id")
        private String projectId;

        private String projectName;

        private String secret;

        private String requesterId;

        public String getPoolName() {
            return poolName;
        }

        public String getProjectId() {
            return projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public String getSecret() {
            return secret;
        }

        public String getRequesterId() {
            return requesterId;
        }

        public String getPoolId() {
            return poolId;
        }
    }
}
