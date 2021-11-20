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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.FlexibleEnum;

public class DynamicPricingConfig {

    private Type type;

    @JsonProperty("skill_id")
    private String skillId;

    private List<Interval> intervals;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public static class Type extends FlexibleEnum<Type> {

        public static final Type SKILL = new Type("SKILL");

        private static final Type[] VALUES = {SKILL};
        private static final ConcurrentMap<String, Type> DISCOVERED_VALUES = new ConcurrentHashMap<>();

        private Type(String name) {
            super(name);
        }

        public static Type[] values() {
            return values(VALUES, DISCOVERED_VALUES.values(), Type.class);
        }

        public static Type valueOf(String name) {
            return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Type>() {
                @Override public Type create(String name) {
                    return new Type(name);
                }
            });
        }
    }

    public static class Interval {

        private Long from;

        private Long to;

        @JsonProperty("reward_per_assignment")
        private BigDecimal rewardPerAssignment;

        @JsonCreator
        public Interval(@JsonProperty("from") Long from,
                        @JsonProperty("to") Long to,
                        @JsonProperty("reward_per_assignment") BigDecimal rewardPerAssignment) {
            this.from = from;
            this.to = to;
            this.rewardPerAssignment = rewardPerAssignment;
        }

        public Long getFrom() {
            return from;
        }

        public void setFrom(Long from) {
            this.from = from;
        }

        public Long getTo() {
            return to;
        }

        public void setTo(Long to) {
            this.to = to;
        }

        public BigDecimal getRewardPerAssignment() {
            return rewardPerAssignment;
        }

        public void setRewardPerAssignment(BigDecimal rewardPerAssignment) {
            this.rewardPerAssignment = rewardPerAssignment;
        }
    }
}
