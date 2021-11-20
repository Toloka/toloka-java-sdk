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

package ai.toloka.client.v1.skill;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.LangIso639;

public class Skill {

    private String id;

    private String name;

    @JsonProperty("public_name")
    private Map<LangIso639, String> publicName;

    @JsonProperty("private_comment")
    private String privateComment;

    @JsonProperty("public_requester_description")
    private Map<LangIso639, String> publicRequesterDescription;

    private boolean hidden;

    @JsonProperty("skill_ttl_hours")
    private Long skillTtlHours;

    private Boolean training;

    private Date created;

    private Boolean global;

    private Boolean deprecated;

    @JsonCreator
    public Skill(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<LangIso639, String> getPublicName() {
        return publicName;
    }

    public String getPrivateComment() {
        return privateComment;
    }

    public Map<LangIso639, String> getPublicRequesterDescription() {
        return publicRequesterDescription;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Long getSkillTtlHours() {
        return skillTtlHours;
    }

    public Boolean getTraining() {
        return training;
    }

    public Date getCreated() {
        return created;
    }

    public Boolean getGlobal() {
        return global;
    }

    public Boolean isDeprecated() {
        return deprecated;
    }

    public void setPublicName(Map<LangIso639, String> publicName) {
        this.publicName = publicName;
    }

    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    public void setPublicRequesterDescription(Map<LangIso639, String> publicRequesterDescription) {
        this.publicRequesterDescription = publicRequesterDescription;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setSkillTtlHours(Long skillTtlHours) {
        this.skillTtlHours = skillTtlHours;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }
}
