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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QualityControl {

    @JsonProperty("captcha_frequency")
    private CaptchaFrequency captchaFrequency;

    @JsonProperty("checkpoints_config")
    private CheckpointsConfig checkpointsConfig;

    @JsonProperty("training_requirement")
    private TrainingRequirement trainingRequirement;

    private List<QualityControlConfig> configs;

    @JsonCreator
    public QualityControl(@JsonProperty("configs") List<QualityControlConfig> configs) {
        this.configs = configs;
    }

    public CaptchaFrequency getCaptchaFrequency() {
        return captchaFrequency;
    }

    public void setCaptchaFrequency(CaptchaFrequency captchaFrequency) {
        this.captchaFrequency = captchaFrequency;
    }

    public CheckpointsConfig getCheckpointsConfig() {
        return checkpointsConfig;
    }

    public void setCheckpointsConfig(CheckpointsConfig checkpointsConfig) {
        this.checkpointsConfig = checkpointsConfig;
    }

    public TrainingRequirement getTrainingRequirement() {
        return trainingRequirement;
    }

    public void setTrainingRequirement(TrainingRequirement trainingRequirement) {
        this.trainingRequirement = trainingRequirement;
    }

    public List<QualityControlConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<QualityControlConfig> configs) {
        this.configs = configs;
    }
}
