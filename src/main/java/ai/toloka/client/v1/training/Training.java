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
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.Owner;

public class Training {

    private String id;

    private Owner owner;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("private_name")
    private String privateName;

    @JsonProperty("private_comment")
    private String privateComment;

    @JsonProperty("public_description")
    private String publicDescription;

    @JsonProperty("inherited_instructions")
    private Boolean inheritedInstructions;

    @JsonProperty("public_instructions")
    private String publicInstructions;

    @JsonProperty("may_contain_adult_content")
    private Boolean mayContainAdultContent;

    @JsonProperty("assignment_max_duration_seconds")
    private Integer assignmentMaxDurationSeconds;

    @JsonProperty("mix_tasks_in_creation_order")
    private Boolean mixTasksInCreationOrder;

    @JsonProperty("shuffle_tasks_in_task_suite")
    private Boolean shuffleTasksInTaskSuite;

    @JsonProperty("training_tasks_in_task_suite_count")
    private Integer trainingTasksInTaskSuiteCount;

    @JsonProperty("task_suites_required_to_pass")
    private Integer taskSuitesRequiredToPass;

    @JsonProperty("retry_training_after_days")
    private Long retryTrainingAfterDays;

    private Map<String, List<String>> metadata;

    private TrainingStatus status;

    private Date created;

    @JsonProperty("last_started")
    private Date lastStarted;

    @JsonProperty("last_stopped")
    private Date lastStopped;

    @JsonProperty("last_close_reason")
    private TrainingCloseReason lastCloseReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrivateName() {
        return privateName;
    }

    public void setPrivateName(String privateName) {
        this.privateName = privateName;
    }

    public String getPrivateComment() {
        return privateComment;
    }

    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    public String getPublicDescription() {
        return publicDescription;
    }

    public void setPublicDescription(String publicDescription) {
        this.publicDescription = publicDescription;
    }

    public Boolean getInheritedInstructions() {
        return inheritedInstructions;
    }

    public void setInheritedInstructions(Boolean inheritedInstructions) {
        this.inheritedInstructions = inheritedInstructions;
    }

    public String getPublicInstructions() {
        return publicInstructions;
    }

    public void setPublicInstructions(String publicInstructions) {
        this.publicInstructions = publicInstructions;
    }

    public Boolean getMayContainAdultContent() {
        return mayContainAdultContent;
    }

    public void setMayContainAdultContent(Boolean mayContainAdultContent) {
        this.mayContainAdultContent = mayContainAdultContent;
    }

    public Map<String, List<String>> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, List<String>> metadata) {
        this.metadata = metadata;
    }

    public Integer getAssignmentMaxDurationSeconds() {
        return assignmentMaxDurationSeconds;
    }

    public void setAssignmentMaxDurationSeconds(Integer assignmentMaxDurationSeconds) {
        this.assignmentMaxDurationSeconds = assignmentMaxDurationSeconds;
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

    public Integer getTrainingTasksInTaskSuiteCount() {
        return trainingTasksInTaskSuiteCount;
    }

    public void setTrainingTasksInTaskSuiteCount(Integer trainingTasksInTaskSuiteCount) {
        this.trainingTasksInTaskSuiteCount = trainingTasksInTaskSuiteCount;
    }

    public Integer getTaskSuitesRequiredToPass() {
        return taskSuitesRequiredToPass;
    }

    public void setTaskSuitesRequiredToPass(Integer taskSuitesRequiredToPass) {
        this.taskSuitesRequiredToPass = taskSuitesRequiredToPass;
    }

    public Long getRetryTrainingAfterDays() {
        return retryTrainingAfterDays;
    }

    public void setRetryTrainingAfterDays(Long retryTrainingAfterDays) {
        this.retryTrainingAfterDays = retryTrainingAfterDays;
    }

    public TrainingStatus getStatus() {
        return status;
    }

    public void setStatus(TrainingStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastStarted() {
        return lastStarted;
    }

    public void setLastStarted(Date lastStarted) {
        this.lastStarted = lastStarted;
    }

    public Date getLastStopped() {
        return lastStopped;
    }

    public void setLastStopped(Date lastStopped) {
        this.lastStopped = lastStopped;
    }

    public TrainingCloseReason getLastCloseReason() {
        return lastCloseReason;
    }

    public void setLastCloseReason(TrainingCloseReason lastCloseReason) {
        this.lastCloseReason = lastCloseReason;
    }
}
