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

package ai.toloka.client.v1.pool.qualitycontrol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainingRequirement {

    @JsonProperty("training_pool_id")
    private String trainingPoolId;

    @JsonProperty("training_passing_skill_value")
    private Integer trainingPassingSkillValue;

    @JsonCreator
    public TrainingRequirement(@JsonProperty("training_pool_id") String trainingPoolId,
                               @JsonProperty("training_passing_skill_value") Integer trainingPassingSkillValue) {

        this.trainingPoolId = trainingPoolId;
        this.trainingPassingSkillValue = trainingPassingSkillValue;
    }

    public String getTrainingPoolId() {
        return trainingPoolId;
    }

    public Integer getTrainingPassingSkillValue() {
        return trainingPassingSkillValue;
    }

    public void setTrainingPoolId(String trainingPoolId) {
        this.trainingPoolId = trainingPoolId;
    }

    public void setTrainingPassingSkillValue(Integer trainingPassingSkillValue) {
        this.trainingPassingSkillValue = trainingPassingSkillValue;
    }
}
