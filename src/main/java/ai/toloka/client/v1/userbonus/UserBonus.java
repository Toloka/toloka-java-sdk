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

package ai.toloka.client.v1.userbonus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.LangIso639;

public class UserBonus {

    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("assignment_id")
    private String assignmentId;

    private BigDecimal amount;

    @JsonProperty("private_comment")
    private String privateComment;

    @JsonProperty("public_title")
    private Map<LangIso639, String> publicTitle;

    @JsonProperty("public_message")
    private Map<LangIso639, String> publicMessage;

    @JsonProperty("without_message")
    private Boolean withoutMessage;

    private Date created;

    @JsonCreator
    public UserBonus(@JsonProperty("user_id") String userId, @JsonProperty("amount") BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public UserBonus(String userId,
                     BigDecimal amount,
                     Map<LangIso639, String> publicTitle,
                     Map<LangIso639, String> publicMessage) {
        this.userId = userId;
        this.amount = amount;
        this.publicTitle = publicTitle;
        this.publicMessage = publicMessage;
    }

    public UserBonus(String userId,
                     BigDecimal amount,
                     boolean withoutMessage) {
        this.userId = userId;
        this.amount = amount;
        this.withoutMessage = withoutMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPrivateComment() {
        return privateComment;
    }

    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    public Map<LangIso639, String> getPublicTitle() {
        return publicTitle;
    }

    public void setPublicTitle(Map<LangIso639, String> publicTitle) {
        this.publicTitle = publicTitle;
    }

    public Map<LangIso639, String> getPublicMessage() {
        return publicMessage;
    }

    public void setPublicMessage(Map<LangIso639, String> publicMessage) {
        this.publicMessage = publicMessage;
    }

    public Boolean getWithoutMessage() {
        return withoutMessage;
    }

    public void setWithoutMessage(Boolean withoutMessage) {
        this.withoutMessage = withoutMessage;
    }

    public String getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }
}
