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

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.operation.OperationType;

public class TrainingCloneOperation extends Operation<TrainingCloneOperation.Parameters, TrainingCloneOperation> {

    public static final OperationType TYPE = OperationType.TRAINING_CLONE;

    public TrainingCloneOperation() {
    }

    private TrainingCloneOperation(Date currentDateTime) {
        super(currentDateTime);
    }

    public static class Parameters {

        @JsonProperty("training_id")
        private String trainingId;

        public String getTrainingId() {
            return trainingId;
        }
    }

    public static TrainingCloneOperation createPseudo(Date currentDateTime) {
        return new TrainingCloneOperation(currentDateTime);
    }
}
