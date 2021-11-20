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

package ai.toloka.client.v1.userskill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSkill {

    private String id;

    @JsonProperty("skill_id")
    private String skillId;

    @JsonProperty("user_id")
    private String userId;

    private Integer value;

    @JsonProperty("exact_value")
    private BigDecimal exactValue;

    private Date created;

    private Date modified;

    @JsonCreator
    public UserSkill(@JsonProperty("skill_id") String skillId, @JsonProperty("user_id") String userId,
                     @JsonProperty("value") Integer value) {

        this.skillId = skillId;
        this.userId = userId;
        setValue(value);
    }

    public void setValue(Integer value) {
        this.value = value;
        this.exactValue = value == null ? null : BigDecimal.valueOf(value);
    }

    public static UserSkill valueOf(String skillId, String userId, BigDecimal exactValue) {
        return new UserSkill(skillId, userId, exactValue);
    }

    private UserSkill(String skillId, String userId, BigDecimal exactValue) {

        this.skillId = skillId;
        this.userId = userId;
        setExactValue(exactValue);
    }

    public void setExactValue(BigDecimal exactValue) {
        this.value = exactValue == null
                ? null
                : exactValue.setScale(0, RoundingMode.HALF_UP).intValueExact();
        this.exactValue = exactValue;
    }

    public String getId() {
        return id;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getValue() {
        return value;
    }

    public BigDecimal getExactValue() {
        return exactValue;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }
}
