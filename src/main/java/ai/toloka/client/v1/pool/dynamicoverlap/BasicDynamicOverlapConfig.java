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

package ai.toloka.client.v1.pool.dynamicoverlap;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasicDynamicOverlapConfig extends AbstractDynamicOverlapConfig {

    @JsonProperty("max_overlap")
    private Long maxOverlap;

    @JsonProperty("min_confidence")
    private BigDecimal minConfidence;

    @JsonProperty("answer_weight_skill_id")
    private String answerWeightSkillId;

    private List<Field> fields;

    public BasicDynamicOverlapConfig() {
        super(DynamicOverlapType.BASIC);
    }

    public Long getMaxOverlap() {
        return maxOverlap;
    }

    public void setMaxOverlap(Long maxOverlap) {
        this.maxOverlap = maxOverlap;
    }

    public BigDecimal getMinConfidence() {
        return minConfidence;
    }

    public void setMinConfidence(BigDecimal minConfidence) {
        this.minConfidence = minConfidence;
    }

    public String getAnswerWeightSkillId() {
        return answerWeightSkillId;
    }

    public void setAnswerWeightSkillId(String answerWeightSkillId) {
        this.answerWeightSkillId = answerWeightSkillId;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public static class Field {

        private String name;

        public Field() {
        }

        public Field(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
