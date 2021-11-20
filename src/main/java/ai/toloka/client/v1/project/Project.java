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

package ai.toloka.client.v1.project;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.Owner;
import ai.toloka.client.v1.project.spec.TaskSpec;

public class Project {

    private String id;

    private Owner owner;

    @JsonProperty("public_name")
    private String publicName;

    @JsonProperty("public_description")
    private String publicDescription;

    @JsonProperty("public_instructions")
    private String publicInstructions;

    @JsonProperty("private_comment")
    private String privateComment;

    @JsonProperty("task_spec")
    private TaskSpec taskSpec;

    @JsonProperty("assignments_issuing_type")
    private AssignmentsIssuingType assignmentsIssuingType;

    @JsonProperty("assignments_issuing_view_config")
    private AssignmentsIssuingViewConfig assignmentsIssuingViewConfig;

    @JsonProperty("quality_control")
    private ProjectQualityControl qualityControl;

    @JsonProperty("assignments_automerge_enabled")
    private Boolean assignmentsAutomergeEnabled;

    @JsonProperty("max_active_assignments_count")
    private Integer maxActiveAssignmentsCount;

    private Map<String, List<String>> metadata;

    @JsonProperty("localization_config")
    private ProjectLocalizationConfig localizationConfig;

    private ProjectStatus status;

    private Date created;

    @JsonCreator
    public Project(@JsonProperty("public_name") String publicName,
                   @JsonProperty("public_description") String publicDescription,
                   @JsonProperty("public_instructions") String publicInstructions,
                   @JsonProperty("task_spec") TaskSpec taskSpec,
                   @JsonProperty("assignments_issuing_type") AssignmentsIssuingType assignmentsIssuingType) {

        this.publicName = publicName;
        this.publicDescription = publicDescription;
        this.publicInstructions = publicInstructions;
        this.taskSpec = taskSpec;
        this.assignmentsIssuingType = assignmentsIssuingType;
    }

    public String getId() {
        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getPublicName() {
        return publicName;
    }

    public String getPublicDescription() {
        return publicDescription;
    }

    public String getPublicInstructions() {
        return publicInstructions;
    }

    public String getPrivateComment() {
        return privateComment;
    }

    public TaskSpec getTaskSpec() {
        return taskSpec;
    }

    public AssignmentsIssuingType getAssignmentsIssuingType() {
        return assignmentsIssuingType;
    }

    public AssignmentsIssuingViewConfig getAssignmentsIssuingViewConfig() {
        return assignmentsIssuingViewConfig;
    }

    public ProjectQualityControl getQualityControl() {
        return qualityControl;
    }

    public Boolean getAssignmentsAutomergeEnabled() {
        return assignmentsAutomergeEnabled;
    }

    public Integer getMaxActiveAssignmentsCount() {
        return maxActiveAssignmentsCount;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public Date getCreated() {
        return created;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public void setPublicDescription(String publicDescription) {
        this.publicDescription = publicDescription;
    }

    public void setPublicInstructions(String publicInstructions) {
        this.publicInstructions = publicInstructions;
    }

    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    public void setTaskSpec(TaskSpec taskSpec) {
        this.taskSpec = taskSpec;
    }

    public void setAssignmentsIssuingType(AssignmentsIssuingType assignmentsIssuingType) {
        this.assignmentsIssuingType = assignmentsIssuingType;
    }

    public void setAssignmentsIssuingViewConfig(AssignmentsIssuingViewConfig assignmentsIssuingViewConfig) {
        this.assignmentsIssuingViewConfig = assignmentsIssuingViewConfig;
    }

    public void setQualityControl(ProjectQualityControl qualityControl) {
        this.qualityControl = qualityControl;
    }

    public void setAssignmentsAutomergeEnabled(Boolean assignmentsAutomergeEnabled) {
        this.assignmentsAutomergeEnabled = assignmentsAutomergeEnabled;
    }

    public void setMaxActiveAssignmentsCount(Integer maxActiveAssignmentsCount) {
        this.maxActiveAssignmentsCount = maxActiveAssignmentsCount;
    }

    public Map<String, List<String>> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, List<String>> metadata) {
        this.metadata = metadata;
    }

    public ProjectLocalizationConfig getLocalizationConfig() {
        return localizationConfig;
    }

    public void setLocalizationConfig(ProjectLocalizationConfig localizationConfig) {
        this.localizationConfig = localizationConfig;
    }
}
