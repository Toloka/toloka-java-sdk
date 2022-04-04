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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ai.toloka.client.v1.Owner;
import ai.toloka.client.v1.pool.dynamicoverlap.AbstractDynamicOverlapConfig;
import ai.toloka.client.v1.pool.filter.Condition;
import ai.toloka.client.v1.pool.filter.Connective;
import ai.toloka.client.v1.pool.qualitycontrol.QualityControl;

public class Pool {

    private String id;

    private Owner owner;

    private PoolType type;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("private_name")
    private String privateName;

    @JsonProperty("private_comment")
    private String privateComment;

    @JsonProperty("public_description")
    private String publicDescription;

    @JsonProperty("public_instructions")
    private String publicInstructions;

    @JsonProperty("may_contain_adult_content")
    private Boolean mayContainAdultContent;

    @JsonProperty("will_expire")
    private Date willExpire;

    @JsonProperty("auto_close_after_complete_delay_seconds")
    private Long autoCloseAfterCompleteDelaySeconds;

    @JsonProperty("reward_per_assignment")
    private BigDecimal rewardPerAssignment;

    @JsonProperty("dynamic_pricing_config")
    private DynamicPricingConfig dynamicPricingConfig;

    private Map<String, List<String>> metadata;

    @JsonProperty("dynamic_overlap_config")
    private AbstractDynamicOverlapConfig dynamicOverlapConfig;

    @JsonProperty("assignment_max_duration_seconds")
    private Integer assignmentMaxDurationSeconds;

    @JsonProperty("auto_accept_solutions")
    private Boolean autoAcceptSolutions;

    @JsonProperty("auto_accept_period_day")
    private Integer autoAcceptPeriodDay;

    private Long priority;

    @JsonProperty("assignments_issuing_config")
    private AssignmentsIssuingConfig assignmentsIssuingConfig;

    @JsonDeserialize(using = Condition.ConditionDeserializer.class)
    private Connective filter;

    @JsonProperty("quality_control")
    private QualityControl qualityControl;

    private PoolDefaults defaults;

    @JsonProperty("mixer_config")
    private MixerConfig mixerConfig;

    @JsonProperty("training_config")
    private PoolTrainingConfig trainingConfig;

    private PoolStatus status;

    private Date created;

    @JsonProperty("last_started")
    private Date lastStarted;

    @JsonProperty("last_stopped")
    private Date lastStopped;

    @JsonProperty("last_close_reason")
    private PoolCloseReason lastCloseReason;

    @JsonProperty("speed_quality_balance")
    private AbstractSpeedQualityBalance speedQualityBalance;

    @JsonCreator
    public Pool(@JsonProperty("project_id") String projectId,
                @JsonProperty("private_name") String privateName,
                @JsonProperty("may_contain_adult_content") Boolean mayContainAdultContent,
                @JsonProperty("will_expire") Date willExpire,
                @JsonProperty("reward_per_assignment") BigDecimal rewardPerAssignment,
                @JsonProperty("assignment_max_duration_seconds") Integer assignmentMaxDurationSeconds,
                @JsonProperty("auto_accept_solutions") Boolean autoAcceptSolutions,
                @JsonProperty("defaults") PoolDefaults defaults) {

        this.projectId = projectId;
        this.privateName = privateName;
        this.mayContainAdultContent = mayContainAdultContent;
        this.willExpire = willExpire;
        this.rewardPerAssignment = rewardPerAssignment;
        this.assignmentMaxDurationSeconds = assignmentMaxDurationSeconds;
        this.autoAcceptSolutions = autoAcceptSolutions;
        this.defaults = defaults;
    }

    public void setPrivateName(String privateName) {
        this.privateName = privateName;
    }

    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    public void setPublicDescription(String publicDescription) {
        this.publicDescription = publicDescription;
    }

    public void setMayContainAdultContent(Boolean mayContainAdultContent) {
        this.mayContainAdultContent = mayContainAdultContent;
    }

    public void setWillExpire(Date willExpire) {
        this.willExpire = willExpire;
    }

    public void setRewardPerAssignment(BigDecimal rewardPerAssignment) {
        this.rewardPerAssignment = rewardPerAssignment;
    }

    public void setDynamicPricingConfig(DynamicPricingConfig dynamicPricingConfig) {
        this.dynamicPricingConfig = dynamicPricingConfig;
    }

    public void setDynamicOverlapConfig(AbstractDynamicOverlapConfig dynamicOverlapConfig) {
        this.dynamicOverlapConfig = dynamicOverlapConfig;
    }

    public void setAssignmentMaxDurationSeconds(Integer assignmentMaxDurationSeconds) {
        this.assignmentMaxDurationSeconds = assignmentMaxDurationSeconds;
    }

    public void setAutoAcceptSolutions(Boolean autoAcceptSolutions) {
        this.autoAcceptSolutions = autoAcceptSolutions;
    }

    public Integer getAutoAcceptPeriodDay() {
        return autoAcceptPeriodDay;
    }

    public void setAutoAcceptPeriodDay(Integer autoAcceptPeriodDay) {
        this.autoAcceptPeriodDay = autoAcceptPeriodDay;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public void setAssignmentsIssuingConfig(AssignmentsIssuingConfig assignmentsIssuingConfig) {
        this.assignmentsIssuingConfig = assignmentsIssuingConfig;
    }

    public void setFilter(Connective filter) {
        this.filter = filter;
    }

    public void setQualityControl(QualityControl qualityControl) {
        this.qualityControl = qualityControl;
    }

    public void setDefaults(PoolDefaults defaults) {
        this.defaults = defaults;
    }

    public String getId() {
        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getPrivateName() {
        return privateName;
    }

    public String getPrivateComment() {
        return privateComment;
    }

    public String getPublicDescription() {
        return publicDescription;
    }

    public Boolean getMayContainAdultContent() {
        return mayContainAdultContent;
    }

    public Date getWillExpire() {
        return willExpire;
    }

    public BigDecimal getRewardPerAssignment() {
        return rewardPerAssignment;
    }

    public DynamicPricingConfig getDynamicPricingConfig() {
        return dynamicPricingConfig;
    }

    public AbstractDynamicOverlapConfig getDynamicOverlapConfig() {
        return dynamicOverlapConfig;
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

    public Boolean getAutoAcceptSolutions() {
        return autoAcceptSolutions;
    }

    public Long getPriority() {
        return priority;
    }

    public AssignmentsIssuingConfig getAssignmentsIssuingConfig() {
        return assignmentsIssuingConfig;
    }

    public Connective getFilter() {
        return filter;
    }

    public QualityControl getQualityControl() {
        return qualityControl;
    }

    public PoolDefaults getDefaults() {
        return defaults;
    }

    public MixerConfig getMixerConfig() {
        return mixerConfig;
    }

    public void setMixerConfig(MixerConfig mixerConfig) {
        this.mixerConfig = mixerConfig;
    }

    public PoolStatus getStatus() {
        return status;
    }

    public Date getCreated() {
        return created;
    }

    public Date getLastStarted() {
        return lastStarted;
    }

    public Date getLastStopped() {
        return lastStopped;
    }

    public PoolCloseReason getLastCloseReason() {
        return lastCloseReason;
    }

    public Long getAutoCloseAfterCompleteDelaySeconds() {
        return autoCloseAfterCompleteDelaySeconds;
    }

    public void setAutoCloseAfterCompleteDelaySeconds(Long autoCloseAfterCompleteDelaySeconds) {
        this.autoCloseAfterCompleteDelaySeconds = autoCloseAfterCompleteDelaySeconds;
    }

    public PoolType getType() {
        return type;
    }

    public String getPublicInstructions() {
        return publicInstructions;
    }

    public PoolTrainingConfig getTrainingConfig() {
        return trainingConfig;
    }

    public AbstractSpeedQualityBalance getSpeedQualityBalance() {
        return speedQualityBalance;
    }

    public void setSpeedQualityBalance(AbstractSpeedQualityBalance speedQualityBalance) {
        this.speedQualityBalance = speedQualityBalance;
    }
}
