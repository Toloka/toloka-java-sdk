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

package ai.toloka.client.v1.pool;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MixerConfig {

    @JsonProperty("real_tasks_count")
    private Integer realTasksCount;

    @JsonProperty("golden_tasks_count")
    private Integer goldenTasksCount;

    @JsonProperty("training_tasks_count")
    private Integer trainingTasksCount;

    @JsonProperty("min_real_tasks_count")
    private Integer minRealTasksCount;

    @JsonProperty("min_golden_tasks_count")
    private Integer minGoldenTasksCount;

    @JsonProperty("min_training_tasks_count")
    private Integer minTrainingTasksCount;

    @JsonProperty("force_last_assignment")
    private Boolean forceLastAssignment;

    @JsonProperty("force_last_assignment_delay_seconds")
    private Integer forceLastAssignmentDelaySeconds;

    @JsonProperty("mix_tasks_in_creation_order")
    private Boolean mixTasksInCreationOrder;

    @JsonProperty("shuffle_tasks_in_task_suite")
    private Boolean shuffleTasksInTaskSuite;

    @JsonProperty("golden_task_distribution_function")
    private TaskDistributionFunction goldenTaskDistributionFunction;

    @JsonProperty("training_task_distribution_function")
    private TaskDistributionFunction trainingTaskDistributionFunction;

    @JsonCreator
    public MixerConfig(@JsonProperty("real_tasks_count") Integer realTasksCount,
                       @JsonProperty("golden_tasks_count") Integer goldenTasksCount,
                       @JsonProperty("training_tasks_count") Integer trainingTasksCount) {

        this.realTasksCount = realTasksCount;
        this.goldenTasksCount = goldenTasksCount;
        this.trainingTasksCount = trainingTasksCount;
    }

    public Integer getRealTasksCount() {
        return realTasksCount;
    }

    public void setRealTasksCount(Integer realTasksCount) {
        this.realTasksCount = realTasksCount;
    }

    public Integer getGoldenTasksCount() {
        return goldenTasksCount;
    }

    public void setGoldenTasksCount(Integer goldenTasksCount) {
        this.goldenTasksCount = goldenTasksCount;
    }

    public Integer getTrainingTasksCount() {
        return trainingTasksCount;
    }

    public void setTrainingTasksCount(Integer trainingTasksCount) {
        this.trainingTasksCount = trainingTasksCount;
    }

    public Integer getMinRealTasksCount() {
        return minRealTasksCount;
    }

    public void setMinRealTasksCount(Integer minRealTasksCount) {
        this.minRealTasksCount = minRealTasksCount;
    }

    public Integer getMinGoldenTasksCount() {
        return minGoldenTasksCount;
    }

    public void setMinGoldenTasksCount(Integer minGoldenTasksCount) {
        this.minGoldenTasksCount = minGoldenTasksCount;
    }

    public Integer getMinTrainingTasksCount() {
        return minTrainingTasksCount;
    }

    public void setMinTrainingTasksCount(Integer minTrainingTasksCount) {
        this.minTrainingTasksCount = minTrainingTasksCount;
    }

    public Boolean getForceLastAssignment() {
        return forceLastAssignment;
    }

    public void setForceLastAssignment(Boolean forceLastAssignment) {
        this.forceLastAssignment = forceLastAssignment;
    }

    public Boolean getMixTasksInCreationOrder() {
        return mixTasksInCreationOrder;
    }

    public void setMixTasksInCreationOrder(Boolean mixTasksInCreationOrder) {
        this.mixTasksInCreationOrder = mixTasksInCreationOrder;
    }

    public Boolean getShuffleTasksInTaskSuite() {
        return shuffleTasksInTaskSuite;
    }

    public void setShuffleTasksInTaskSuite(Boolean shuffleTasksInTaskSuite) {
        this.shuffleTasksInTaskSuite = shuffleTasksInTaskSuite;
    }

    public TaskDistributionFunction getGoldenTaskDistributionFunction() {
        return goldenTaskDistributionFunction;
    }

    public void setGoldenTaskDistributionFunction(TaskDistributionFunction goldenTaskDistributionFunction) {
        this.goldenTaskDistributionFunction = goldenTaskDistributionFunction;
    }

    public TaskDistributionFunction getTrainingTaskDistributionFunction() {
        return trainingTaskDistributionFunction;
    }

    public void setTrainingTaskDistributionFunction(TaskDistributionFunction trainingTaskDistributionFunction) {
        this.trainingTaskDistributionFunction = trainingTaskDistributionFunction;
    }

    public Integer getForceLastAssignmentDelaySeconds() {
        return forceLastAssignmentDelaySeconds;
    }

    public void setForceLastAssignmentDelaySeconds(Integer forceLastAssignmentDelaySeconds) {
        this.forceLastAssignmentDelaySeconds = forceLastAssignmentDelaySeconds;
    }
}
