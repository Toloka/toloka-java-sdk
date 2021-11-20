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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ai.toloka.client.v1.userrestriction.DurationUnit;
import ai.toloka.client.v1.userrestriction.UserRestrictionScope;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type",
        visible = true, defaultImpl = RuleAction.Unknown.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RuleAction.Restriction.class, name = "RESTRICTION"),
        @JsonSubTypes.Type(value = RuleAction.RestrictionV2.class, name = "RESTRICTION_V2"),
        @JsonSubTypes.Type(value = RuleAction.SetSkillFromOutputField.class, name = "SET_SKILL_FROM_OUTPUT_FIELD"),
        @JsonSubTypes.Type(value = RuleAction.ChangeOverlap.class, name = "CHANGE_OVERLAP"),
        @JsonSubTypes.Type(value = RuleAction.SetSkill.class, name = "SET_SKILL"),
        @JsonSubTypes.Type(value = RuleAction.RejectAllAssignments.class, name = "REJECT_ALL_ASSIGNMENTS"),
        @JsonSubTypes.Type(value = RuleAction.ApproveAllAssignments.class, name = "APPROVE_ALL_ASSIGNMENTS")
})
public abstract class RuleAction<P> {

    private final RuleActionType type;

    private P parameters;

    private RuleAction(RuleActionType type) {
        this.type = type;
    }

    private RuleAction(RuleActionType type, P parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public RuleActionType getType() {
        return type;
    }

    public P getParameters() {
        return parameters;
    }

    public void setParameters(P parameters) {
        this.parameters = parameters;
    }

    /**
     * See {@link RuleAction.RestrictionV2}
     */
    @Deprecated
    public static class Restriction extends RuleAction<Restriction.Parameters> {

        @JsonCreator
        public Restriction(@JsonProperty("parameters") Restriction.Parameters parameters) {
            super(RuleActionType.RESTRICTION, parameters);
        }

        public static class Parameters {

            private UserRestrictionScope scope;

            @JsonProperty("duration_days")
            private Integer durationDays;

            @JsonProperty("private_comment")
            private String privateComment;

            @JsonCreator
            public Parameters(@JsonProperty("scope") UserRestrictionScope scope) {
                this.scope = scope;
            }

            public Parameters(UserRestrictionScope scope, Integer durationDays, String privateComment) {
                this.scope = scope;
                this.durationDays = durationDays;
                this.privateComment = privateComment;
            }

            public UserRestrictionScope getScope() {
                return scope;
            }

            public void setScope(UserRestrictionScope scope) {
                this.scope = scope;
            }

            public Integer getDurationDays() {
                return durationDays;
            }

            public void setDurationDays(Integer durationDays) {
                this.durationDays = durationDays;
            }

            public String getPrivateComment() {
                return privateComment;
            }

            public void setPrivateComment(String privateComment) {
                this.privateComment = privateComment;
            }
        }
    }

    public static class RestrictionV2 extends RuleAction<RestrictionV2.Parameters> {

        @JsonCreator
        public RestrictionV2(@JsonProperty("parameters") RestrictionV2.Parameters parameters) {
            super(RuleActionType.RESTRICTION_V2, parameters);
        }

        public static class Parameters {

            private UserRestrictionScope scope;

            @JsonProperty("duration")
            private Integer duration;

            @JsonProperty("duration_unit")
            private DurationUnit durationUnit;

            @JsonProperty("private_comment")
            private String privateComment;

            @JsonCreator
            public Parameters(@JsonProperty("scope") UserRestrictionScope scope) {
                this.scope = scope;
            }

            public Parameters(
                    UserRestrictionScope scope,
                    Integer duration,
                    DurationUnit durationUnit,
                    String privateComment
            ) {
                this.scope = scope;
                this.duration = duration;
                this.durationUnit = durationUnit;
                this.privateComment = privateComment;
            }

            public UserRestrictionScope getScope() {
                return scope;
            }

            public void setScope(UserRestrictionScope scope) {
                this.scope = scope;
            }

            public Integer getDuration() {
                return duration;
            }

            public void setDuration(Integer duration) {
                this.duration = duration;
            }

            public DurationUnit getDurationUnit() {
                return durationUnit;
            }

            public void setDurationUnit(DurationUnit durationUnit) {
                this.durationUnit = durationUnit;
            }

            public String getPrivateComment() {
                return privateComment;
            }

            public void setPrivateComment(String privateComment) {
                this.privateComment = privateComment;
            }
        }
    }

    public static class SetSkillFromOutputField extends RuleAction<SetSkillFromOutputField.Parameters> {

        @JsonCreator
        public SetSkillFromOutputField(@JsonProperty("parameters") SetSkillFromOutputField.Parameters parameters) {
            super(RuleActionType.SET_SKILL_FROM_OUTPUT_FIELD, parameters);
        }

        public static class Parameters {

            @JsonProperty("skill_id")
            private String skillId;

            @JsonProperty("from_field")
            private RuleConditionKey fromField;

            @JsonCreator
            public Parameters(@JsonProperty("skill_id") String skillId,
                              @JsonProperty("from_field") RuleConditionKey fromField) {

                this.skillId = skillId;
                this.fromField = fromField;
            }

            public String getSkillId() {
                return skillId;
            }

            public void setSkillId(String skillId) {
                this.skillId = skillId;
            }

            public RuleConditionKey getFromField() {
                return fromField;
            }

            public void setFromField(RuleConditionKey fromField) {
                this.fromField = fromField;
            }
        }
    }

    public static class ChangeOverlap extends RuleAction<ChangeOverlap.Parameters> {

        @JsonCreator
        public ChangeOverlap(@JsonProperty("parameters") Parameters parameters) {
            super(RuleActionType.CHANGE_OVERLAP, parameters);
        }

        public static class Parameters {

            private Integer delta;

            @JsonProperty("open_pool")
            private Boolean openPool;

            @JsonCreator
            public Parameters(@JsonProperty("delta") Integer delta) {
                this.delta = delta;
            }

            public Parameters(Integer delta, Boolean openPool) {
                this.delta = delta;
                this.openPool = openPool;
            }

            public Integer getDelta() {
                return delta;
            }

            public void setDelta(Integer delta) {
                this.delta = delta;
            }

            public Boolean getOpenPool() {
                return openPool;
            }

            public void setOpenPool(Boolean openPool) {
                this.openPool = openPool;
            }
        }
    }

    public static class SetSkill extends RuleAction<SetSkill.Parameters> {

        @JsonCreator
        public SetSkill(@JsonProperty("parameters") Parameters parameters) {
            super(RuleActionType.SET_SKILL, parameters);
        }

        public static class Parameters {

            @JsonProperty("skill_id")
            private String skillId;

            @JsonProperty("skill_value")
            private Integer skillValue;

            @JsonCreator
            public Parameters(@JsonProperty("skill_id") String skillId,
                              @JsonProperty("skill_value") Integer skillValue) {

                this.skillId = skillId;
                this.skillValue = skillValue;
            }

            public String getSkillId() {
                return skillId;
            }

            public void setSkillId(String skillId) {
                this.skillId = skillId;
            }

            public Integer getSkillValue() {
                return skillValue;
            }

            public void setSkillValue(Integer skillValue) {
                this.skillValue = skillValue;
            }
        }
    }

    @Deprecated
    public static class RejectAllAssignments extends RuleAction<RejectAllAssignments.Parameters> {

        @JsonCreator
        public RejectAllAssignments(@JsonProperty("parameters") RejectAllAssignments.Parameters parameters) {
            super(RuleActionType.REJECT_ALL_ASSIGNMENTS, parameters);
        }

        public static class Parameters {

            @JsonProperty("public_comment")
            private String publicComment;

            @JsonCreator
            public Parameters(@JsonProperty("public_comment") String publicComment) {
                this.publicComment = publicComment;
            }

            public String getPublicComment() {
                return publicComment;
            }

            public void setPublicComment(String publicComment) {
                this.publicComment = publicComment;
            }
        }
    }

    public static class ApproveAllAssignments extends RuleAction<ApproveAllAssignments.Parameters> {

        @JsonCreator
        public ApproveAllAssignments(@JsonProperty("parameters") ApproveAllAssignments.Parameters parameters) {
            super(RuleActionType.APPROVE_ALL_ASSIGNMENTS, parameters);
        }

        public static class Parameters {
        }
    }

    public static class Unknown extends RuleAction<Map<String, Object>> {

        @JsonCreator
        public Unknown(@JsonProperty("type") RuleActionType type) {
            super(type);
        }
    }
}
