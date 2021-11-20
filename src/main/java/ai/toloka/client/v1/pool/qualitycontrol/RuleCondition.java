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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ai.toloka.client.v1.FlexibleEnum;
import ai.toloka.client.v1.pool.filter.CompareOperator;
import ai.toloka.client.v1.pool.filter.IdentityOperator;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "key",
        visible = true,
        defaultImpl = RuleCondition.Unknown.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RuleCondition.TotalAnswersCount.class, name = "total_answers_count"),
        @JsonSubTypes.Type(value = RuleCondition.CorrectAnswersRate.class, name = "correct_answers_rate"),
        @JsonSubTypes.Type(value = RuleCondition.IncorrectAnswersRate.class, name = "incorrect_answers_rate"),
        @JsonSubTypes.Type(value = RuleCondition.GoldenSetAnswersCount.class, name = "golden_set_answers_count"),
        @JsonSubTypes.Type(value = RuleCondition.GoldenSetCorrectAnswersRate.class,
                name = "golden_set_correct_answers_rate"),
        @JsonSubTypes.Type(value = RuleCondition.GoldenSetIncorrectAnswersRate.class,
                name = "golden_set_incorrect_answers_rate"),
        @JsonSubTypes.Type(value = RuleCondition.StoredResultsCount.class, name = "stored_results_count"),
        @JsonSubTypes.Type(value = RuleCondition.SuccessRate.class, name = "success_rate"),
        @JsonSubTypes.Type(value = RuleCondition.FailRate.class, name = "fail_rate"),
        @JsonSubTypes.Type(value = RuleCondition.IncomeSumForLast24Hours.class, name = "income_sum_for_last_24_hours"),
        @JsonSubTypes.Type(value = RuleCondition.SkippedInRowCount.class, name = "skipped_in_row_count"),
        @JsonSubTypes.Type(value = RuleCondition.AssignmentsAcceptedCount.class, name = "assignments_accepted_count"),
        @JsonSubTypes.Type(value = RuleCondition.TotalSubmittedCount.class, name = "total_submitted_count"),
        @JsonSubTypes.Type(value = RuleCondition.FastSubmittedCount.class, name = "fast_submitted_count"),
        @JsonSubTypes.Type(value = RuleCondition.TotalAssignmentsCount.class, name = "total_assignments_count"),
        @JsonSubTypes.Type(value = RuleCondition.AcceptedAssignmentsRate.class, name = "accepted_assignments_rate"),
        @JsonSubTypes.Type(value = RuleCondition.RejectedAssignmentsRate.class, name = "rejected_assignments_rate"),
        @JsonSubTypes.Type(value = RuleCondition.PendingAssignmentsCount.class, name = "pending_assignments_count"),
        @JsonSubTypes.Type(value = RuleCondition.AcceptedAssignmentsCount.class, name = "accepted_assignments_count"),
        @JsonSubTypes.Type(value = RuleCondition.RejectedAssignmentsCount.class, name = "rejected_assignments_count"),
        @JsonSubTypes.Type(value = RuleCondition.AssessmentEvent.class, name = "assessment_event"),
        @JsonSubTypes.Type(value = RuleCondition.PoolAccessRevokedReason.class, name = "pool_access_revoked_reason"),
        @JsonSubTypes.Type(value = RuleCondition.SkillId.class, name = "skill_id"),
        @JsonSubTypes.Type(value = RuleCondition.SubmittedAssignmentsCount.class, name = "submitted_assignments_count"),
        @JsonSubTypes.Type(value = RuleCondition.NextAssignmentAvailable.class, name = "next_assignment_available")
})
public abstract class RuleCondition<O, V> {

    private final RuleConditionKey key;

    private O operator;

    private V value;

    private RuleCondition(RuleConditionKey key) {
        this.key = key;
    }

    private RuleCondition(RuleConditionKey key, O operator, V value) {
        this.key = key;
        this.operator = operator;
        this.value = value;
    }

    public RuleConditionKey getKey() {
        return key;
    }

    public O getOperator() {
        return operator;
    }

    public void setOperator(O operator) {
        this.operator = operator;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public static class TotalAnswersCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public TotalAnswersCount(@JsonProperty("operator") CompareOperator operator,
                                 @JsonProperty("value") Integer value) {

            super(RuleConditionKey.TOTAL_ANSWERS_COUNT, operator, value);
        }
    }

    public static class CorrectAnswersRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public CorrectAnswersRate(@JsonProperty("operator") CompareOperator operator,
                                  @JsonProperty("value") Double value) {

            super(RuleConditionKey.CORRECT_ANSWERS_RATE, operator, value);
        }
    }

    public static class IncorrectAnswersRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public IncorrectAnswersRate(@JsonProperty("operator") CompareOperator operator,
                                    @JsonProperty("value") Double value) {

            super(RuleConditionKey.INCORRECT_ANSWERS_RATE, operator, value);
        }
    }

    public static class GoldenSetAnswersCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public GoldenSetAnswersCount(@JsonProperty("operator") CompareOperator operator,
                                     @JsonProperty("value") Integer value) {

            super(RuleConditionKey.GOLDEN_SET_ANSWERS_COUNT, operator, value);
        }
    }

    public static class GoldenSetCorrectAnswersRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public GoldenSetCorrectAnswersRate(@JsonProperty("operator") CompareOperator operator,
                                           @JsonProperty("value") Double value) {

            super(RuleConditionKey.GOLDEN_SET_CORRECT_ANSWERS_RATE, operator, value);
        }
    }

    public static class GoldenSetIncorrectAnswersRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public GoldenSetIncorrectAnswersRate(@JsonProperty("operator") CompareOperator operator,
                                             @JsonProperty("value") Double value) {

            super(RuleConditionKey.GOLDEN_SET_INCORRECT_ANSWERS_RATE, operator, value);
        }
    }

    public static class StoredResultsCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public StoredResultsCount(@JsonProperty("operator") CompareOperator operator,
                                  @JsonProperty("value") Integer value) {

            super(RuleConditionKey.STORED_RESULTS_COUNT, operator, value);
        }
    }

    public static class SuccessRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public SuccessRate(@JsonProperty("operator") CompareOperator operator, @JsonProperty("value") Double value) {
            super(RuleConditionKey.SUCCESS_RATE, operator, value);
        }
    }

    public static class FailRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public FailRate(@JsonProperty("operator") CompareOperator operator, @JsonProperty("value") Double value) {
            super(RuleConditionKey.FAIL_RATE, operator, value);
        }
    }

    public static class IncomeSumForLast24Hours extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public IncomeSumForLast24Hours(@JsonProperty("operator") CompareOperator operator,
                                       @JsonProperty("value") Double value) {

            super(RuleConditionKey.INCOME_SUM_FOR_LAST_24_HOURS, operator, value);
        }
    }

    public static class SkippedInRowCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public SkippedInRowCount(@JsonProperty("operator") CompareOperator operator,
                                 @JsonProperty("value") Integer value) {

            super(RuleConditionKey.SKIPPED_IN_ROW_COUNT, operator, value);
        }
    }

    public static class AssignmentsAcceptedCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public AssignmentsAcceptedCount(@JsonProperty("operator") CompareOperator operator,
                                        @JsonProperty("value") Integer value) {

            super(RuleConditionKey.ASSIGNMENTS_ACCEPTED_COUNT, operator, value);
        }
    }

    public static class TotalSubmittedCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public TotalSubmittedCount(@JsonProperty("operator") CompareOperator operator,
                                   @JsonProperty("value") Integer value) {

            super(RuleConditionKey.TOTAL_SUBMITTED_COUNT, operator, value);
        }
    }

    public static class FastSubmittedCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public FastSubmittedCount(@JsonProperty("operator") CompareOperator operator,
                                  @JsonProperty("value") Integer value) {

            super(RuleConditionKey.FAST_SUBMITTED_COUNT, operator, value);
        }
    }

    public static class TotalAssignmentsCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public TotalAssignmentsCount(@JsonProperty("operator") CompareOperator operator,
                                     @JsonProperty("value") Integer value) {

            super(RuleConditionKey.TOTAL_ASSIGNMENTS_COUNT, operator, value);
        }
    }

    public static class AcceptedAssignmentsRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public AcceptedAssignmentsRate(@JsonProperty("operator") CompareOperator operator,
                                       @JsonProperty("value") Double value) {

            super(RuleConditionKey.ACCEPTED_ASSIGNMENTS_RATE, operator, value);
        }
    }

    public static class RejectedAssignmentsRate extends RuleCondition<CompareOperator, Double> {

        @JsonCreator
        public RejectedAssignmentsRate(@JsonProperty("operator") CompareOperator operator,
                                       @JsonProperty("value") Double value) {

            super(RuleConditionKey.REJECTED_ASSIGNMENTS_RATE, operator, value);
        }
    }

    public static class PendingAssignmentsCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public PendingAssignmentsCount(@JsonProperty("operator") CompareOperator operator,
                                       @JsonProperty("value") Integer value) {

            super(RuleConditionKey.PENDING_ASSIGNMENTS_COUNT, operator, value);
        }
    }

    public static class AcceptedAssignmentsCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public AcceptedAssignmentsCount(@JsonProperty("operator") CompareOperator operator,
                                        @JsonProperty("value") Integer value) {

            super(RuleConditionKey.ACCEPTED_ASSIGNMENTS_COUNT, operator, value);
        }
    }

    public static class RejectedAssignmentsCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public RejectedAssignmentsCount(@JsonProperty("operator") CompareOperator operator,
                                        @JsonProperty("value") Integer value) {

            super(RuleConditionKey.REJECTED_ASSIGNMENTS_COUNT, operator, value);
        }
    }

    public static class AssessmentEvent extends RuleCondition<IdentityOperator, AssessmentEvent.Type> {

        @JsonCreator
        public AssessmentEvent(@JsonProperty("operator") IdentityOperator operator, @JsonProperty("value") Type value) {
            super(RuleConditionKey.ASSESSMENT_EVENT, operator, value);
        }

        public static class Type extends FlexibleEnum<Type> {

            public static final Type ACCEPT = new Type("ACCEPT");
            public static final Type REJECT = new Type("REJECT");
            public static final Type ACCEPT_AFTER_REJECT = new Type("ACCEPT_AFTER_REJECT");

            private static final Type[] VALUES = {ACCEPT, REJECT, ACCEPT_AFTER_REJECT};
            private static final ConcurrentMap<String, Type> DISCOVERED_VALUES = new ConcurrentHashMap<>();

            private Type(String name) {
                super(name);
            }

            public static Type[] values() {
                return values(VALUES, DISCOVERED_VALUES.values(), Type.class);
            }

            public static Type valueOf(String name) {
                return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Type>() {
                    @Override
                    public Type create(String name) {
                        return new Type(name);
                    }
                });
            }
        }
    }

    public static class PoolAccessRevokedReason extends RuleCondition<IdentityOperator, PoolAccessRevokedReason.Type> {

        @JsonCreator
        public PoolAccessRevokedReason(@JsonProperty("operator") IdentityOperator operator,
                                       @JsonProperty("value") Type value) {

            super(RuleConditionKey.POOL_ACCESS_REVOKED_REASON, operator, value);
        }

        public static class Type extends FlexibleEnum<Type> {

            public static final Type SKILL_CHANGE = new Type("SKILL_CHANGE");
            public static final Type RESTRICTION = new Type("RESTRICTION");

            private static final Type[] VALUES = {SKILL_CHANGE, RESTRICTION};
            private static final ConcurrentMap<String, Type> DISCOVERED_VALUES = new ConcurrentHashMap<>();

            public Type(String name) {
                super(name);
            }

            public static Type[] values() {
                return values(VALUES, DISCOVERED_VALUES.values(), Type.class);
            }

            public static Type valueOf(String name) {
                return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Type>() {
                    @Override
                    public Type create(String name) {
                        return new Type(name);
                    }
                });
            }
        }
    }

    public static class SkillId extends RuleCondition<IdentityOperator, String> {

        @JsonCreator
        public SkillId(@JsonProperty("operator") IdentityOperator operator, @JsonProperty("value") String value) {
            super(RuleConditionKey.SKILL_ID, operator, value);
        }
    }

    public static class SubmittedAssignmentsCount extends RuleCondition<CompareOperator, Integer> {

        @JsonCreator
        public SubmittedAssignmentsCount(@JsonProperty("operator") CompareOperator operator,
                                         @JsonProperty("value") Integer value) {

            super(RuleConditionKey.SUBMITTED_ASSIGNMENTS_COUNT, operator, value);
        }
    }

    public static class NextAssignmentAvailable extends RuleCondition<IdentityOperator, Boolean> {

        @JsonCreator
        public NextAssignmentAvailable(@JsonProperty("operator") IdentityOperator operator,
                                       @JsonProperty("value") Boolean value) {

            super(RuleConditionKey.NEXT_ASSIGNMENT_AVAILABLE, operator, value);
        }
    }

    public static class Unknown extends RuleCondition<CompareOperator, Object> {

        @JsonCreator
        public Unknown(@JsonProperty("key") RuleConditionKey key) {
            super(key);
        }
    }
}
