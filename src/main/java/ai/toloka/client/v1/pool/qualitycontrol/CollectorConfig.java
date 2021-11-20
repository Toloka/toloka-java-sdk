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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type",
        visible = true, defaultImpl = CollectorConfig.Unknown.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CollectorConfig.GoldenSet.class, name = "GOLDEN_SET"),
        @JsonSubTypes.Type(value = CollectorConfig.MajorityVote.class, name = "MAJORITY_VOTE"),
        @JsonSubTypes.Type(value = CollectorConfig.Captcha.class, name = "CAPTCHA"),
        @JsonSubTypes.Type(value = CollectorConfig.Income.class, name = "INCOME"),
        @JsonSubTypes.Type(value = CollectorConfig.SkippedInRowAssignments.class, name = "SKIPPED_IN_ROW_ASSIGNMENTS"),
        @JsonSubTypes.Type(value = CollectorConfig.AnswerCount.class, name = "ANSWER_COUNT"),
        @JsonSubTypes.Type(value = CollectorConfig.AssignmentSubmitTime.class, name = "ASSIGNMENT_SUBMIT_TIME"),
        @JsonSubTypes.Type(value = CollectorConfig.AcceptanceRate.class, name = "ACCEPTANCE_RATE"),
        @JsonSubTypes.Type(value = CollectorConfig.AssignmentsAssessment.class, name = "ASSIGNMENTS_ASSESSMENT"),
        @JsonSubTypes.Type(value = CollectorConfig.UsersAssessment.class, name = "USERS_ASSESSMENT"),
        @JsonSubTypes.Type(value = CollectorConfig.Training.class, name = "TRAINING")
})
public abstract class CollectorConfig<P> {

    protected UUID uuid;
    private final CollectorConfigType type;
    private P parameters;

    private CollectorConfig(CollectorConfigType type) {
        this.type = type;
    }

    private CollectorConfig(CollectorConfigType type, P parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public CollectorConfigType getType() {
        return type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public P getParameters() {
        return parameters;
    }

    public void setParameters(P parameters) {
        this.parameters = parameters;
    }

    public static class GoldenSet extends CollectorConfig<GoldenSet.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.TOTAL_ANSWERS_COUNT);
                    add(RuleConditionKey.CORRECT_ANSWERS_RATE);
                    add(RuleConditionKey.INCORRECT_ANSWERS_RATE);
                    add(RuleConditionKey.GOLDEN_SET_ANSWERS_COUNT);
                    add(RuleConditionKey.GOLDEN_SET_CORRECT_ANSWERS_RATE);
                    add(RuleConditionKey.GOLDEN_SET_INCORRECT_ANSWERS_RATE);
                }}
                );

        public GoldenSet() {
            super(CollectorConfigType.GOLDEN_SET);
        }

        @JsonCreator
        public GoldenSet(@JsonProperty("parameters") GoldenSet.Parameters parameters) {
            super(CollectorConfigType.GOLDEN_SET, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {

            @JsonProperty("history_size")
            private Integer historySize;

            public Integer getHistorySize() {
                return historySize;
            }

            public void setHistorySize(Integer historySize) {
                this.historySize = historySize;
            }
        }
    }

    public static class MajorityVote extends CollectorConfig<MajorityVote.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.TOTAL_ANSWERS_COUNT);
                    add(RuleConditionKey.CORRECT_ANSWERS_RATE);
                    add(RuleConditionKey.INCORRECT_ANSWERS_RATE);
                }}
                );

        @JsonCreator
        public MajorityVote(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.MAJORITY_VOTE, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {

            @JsonProperty("answer_threshold")
            private Integer answerThreshold;

            @JsonProperty("history_size")
            private Integer historySize;

            @JsonCreator
            public Parameters(@JsonProperty("answer_threshold") Integer answerThreshold) {
                this.answerThreshold = answerThreshold;
            }

            public Integer getAnswerThreshold() {
                return answerThreshold;
            }

            public void setAnswerThreshold(Integer answerThreshold) {
                this.answerThreshold = answerThreshold;
            }

            public Integer getHistorySize() {
                return historySize;
            }

            public void setHistorySize(Integer historySize) {
                this.historySize = historySize;
            }
        }
    }

    public static class Captcha extends CollectorConfig<Captcha.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.STORED_RESULTS_COUNT);
                    add(RuleConditionKey.SUCCESS_RATE);
                    add(RuleConditionKey.FAIL_RATE);
                }}
                );

        @JsonCreator
        public Captcha(@JsonProperty("parameters") Captcha.Parameters parameters) {
            super(CollectorConfigType.CAPTCHA, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {

            @JsonProperty("history_size")
            private Integer historySize;

            @JsonCreator
            public Parameters(@JsonProperty("history_size") Integer historySize) {
                this.historySize = historySize;
            }

            public Integer getHistorySize() {
                return historySize;
            }

            public void setHistorySize(Integer historySize) {
                this.historySize = historySize;
            }
        }
    }

    public static class Income extends CollectorConfig<Income.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.INCOME_SUM_FOR_LAST_24_HOURS);
                }}
                );

        public Income() {
            super(CollectorConfigType.INCOME);
        }

        @JsonCreator
        public Income(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.INCOME, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {}
    }

    public static class SkippedInRowAssignments extends CollectorConfig<SkippedInRowAssignments.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.SKIPPED_IN_ROW_COUNT);
                }}
                );

        public SkippedInRowAssignments() {
            super(CollectorConfigType.SKIPPED_IN_ROW_ASSIGNMENTS);
        }

        @JsonCreator
        public SkippedInRowAssignments(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.SKIPPED_IN_ROW_ASSIGNMENTS, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {}
    }

    public static class AnswerCount extends CollectorConfig<AnswerCount.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.ASSIGNMENTS_ACCEPTED_COUNT);
                }}
                );

        public AnswerCount() {
            super(CollectorConfigType.ANSWER_COUNT);
        }

        @JsonCreator
        public AnswerCount(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.ANSWER_COUNT, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {}
    }

    public static class AssignmentSubmitTime extends CollectorConfig<AssignmentSubmitTime.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.TOTAL_SUBMITTED_COUNT);
                    add(RuleConditionKey.FAST_SUBMITTED_COUNT);
                }}
                );

        @JsonCreator
        public AssignmentSubmitTime(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.ASSIGNMENT_SUBMIT_TIME, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {

            @JsonProperty("history_size")
            private Integer historySize;

            @JsonProperty("fast_submit_threshold_seconds")
            private Integer fastSubmitThresholdSeconds;

            @JsonCreator
            public Parameters(@JsonProperty("history_size") Integer historySize,
                              @JsonProperty("fast_submit_threshold_seconds") Integer fastSubmitThresholdSeconds) {

                this.historySize = historySize;
                this.fastSubmitThresholdSeconds = fastSubmitThresholdSeconds;
            }

            public Integer getHistorySize() {
                return historySize;
            }

            public void setHistorySize(Integer historySize) {
                this.historySize = historySize;
            }

            public Integer getFastSubmitThresholdSeconds() {
                return fastSubmitThresholdSeconds;
            }

            public void setFastSubmitThresholdSeconds(Integer fastSubmitThresholdSeconds) {
                this.fastSubmitThresholdSeconds = fastSubmitThresholdSeconds;
            }
        }
    }

    public static class AcceptanceRate extends CollectorConfig<AcceptanceRate.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.TOTAL_ASSIGNMENTS_COUNT);
                    add(RuleConditionKey.ACCEPTED_ASSIGNMENTS_RATE);
                    add(RuleConditionKey.REJECTED_ASSIGNMENTS_RATE);
                }}
                );

        public AcceptanceRate() {
            super(CollectorConfigType.ACCEPTANCE_RATE);
        }

        @JsonCreator
        public AcceptanceRate(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.ACCEPTANCE_RATE, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {}
    }

    public static class AssignmentsAssessment extends CollectorConfig<AssignmentsAssessment.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.PENDING_ASSIGNMENTS_COUNT);
                    add(RuleConditionKey.ACCEPTED_ASSIGNMENTS_COUNT);
                    add(RuleConditionKey.REJECTED_ASSIGNMENTS_COUNT);
                    add(RuleConditionKey.ASSESSMENT_EVENT);
                }}
                );

        public AssignmentsAssessment() {
            super(CollectorConfigType.ASSIGNMENTS_ASSESSMENT);
        }

        @JsonCreator
        public AssignmentsAssessment(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.ASSIGNMENTS_ASSESSMENT, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {}
    }

    public static class UsersAssessment extends CollectorConfig<UsersAssessment.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.POOL_ACCESS_REVOKED_REASON);
                    add(RuleConditionKey.SKILL_ID);
                }}
                );

        public UsersAssessment() {
            super(CollectorConfigType.USERS_ASSESSMENT);
        }

        @JsonCreator
        public UsersAssessment(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.USERS_ASSESSMENT, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {}
    }

    public static class Training extends CollectorConfig<Training.Parameters> {

        private static final Set<RuleConditionKey> OUTPUT_FIELDS =
                Collections.unmodifiableSet(new HashSet<RuleConditionKey>() {{
                    add(RuleConditionKey.SUBMITTED_ASSIGNMENTS_COUNT);
                    add(RuleConditionKey.TOTAL_ANSWERS_COUNT);
                    add(RuleConditionKey.CORRECT_ANSWERS_RATE);
                    add(RuleConditionKey.INCORRECT_ANSWERS_RATE);
                    add(RuleConditionKey.NEXT_ASSIGNMENT_AVAILABLE);
                }}
                );

        public Training() {
            super(CollectorConfigType.TRAINING);
        }

        @JsonCreator
        public Training(@JsonProperty("parameters") Parameters parameters) {
            super(CollectorConfigType.TRAINING, parameters);
        }

        public static Set<RuleConditionKey> outputFields() {
            return OUTPUT_FIELDS;
        }

        public static class Parameters {}
    }

    public static class Unknown extends CollectorConfig<Map<String, Object>> {

        @JsonCreator
        public Unknown(@JsonProperty("type") CollectorConfigType type) {
            super(type);
        }
    }
}
