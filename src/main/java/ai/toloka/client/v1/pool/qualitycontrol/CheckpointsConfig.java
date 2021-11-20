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

import ai.toloka.client.v1.pool.TaskDistributionFunction;

public class CheckpointsConfig {

    @JsonProperty("real_settings")
    private Settings realSettings;

    @JsonProperty("golden_settings")
    private Settings goldenSettings;

    @JsonProperty("training_settings")
    private Settings trainingSettings;

    public Settings getRealSettings() {
        return realSettings;
    }

    public void setRealSettings(Settings realSettings) {
        this.realSettings = realSettings;
    }

    public Settings getGoldenSettings() {
        return goldenSettings;
    }

    public void setGoldenSettings(Settings goldenSettings) {
        this.goldenSettings = goldenSettings;
    }

    public Settings getTrainingSettings() {
        return trainingSettings;
    }

    public void setTrainingSettings(Settings trainingSettings) {
        this.trainingSettings = trainingSettings;
    }

    public static class Settings {

        @JsonProperty("target_overlap")
        private Integer targetOverlap;

        @JsonProperty("task_distribution_function")
        private TaskDistributionFunction taskDistributionFunction;

        @JsonCreator
        public Settings(@JsonProperty("target_overlap") Integer targetOverlap,
                        @JsonProperty("task_distribution_function") TaskDistributionFunction taskDistributionFunction) {
            this.targetOverlap = targetOverlap;
            this.taskDistributionFunction = taskDistributionFunction;
        }

        public Integer getTargetOverlap() {
            return targetOverlap;
        }

        public void setTargetOverlap(Integer targetOverlap) {
            this.targetOverlap = targetOverlap;
        }

        public TaskDistributionFunction getTaskDistributionFunction() {
            return taskDistributionFunction;
        }

        public void setTaskDistributionFunction(TaskDistributionFunction taskDistributionFunction) {
            this.taskDistributionFunction = taskDistributionFunction;
        }
    }
}
