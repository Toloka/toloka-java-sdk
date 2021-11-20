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

package ai.toloka.client.v1.userrestriction;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "scope", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SystemUserRestriction.class, name = "SYSTEM"),
        @JsonSubTypes.Type(value = AllProjectsUserRestriction.class, name = "ALL_PROJECTS"),
        @JsonSubTypes.Type(value = ProjectUserRestriction.class, name = "PROJECT"),
        @JsonSubTypes.Type(value = PoolUserRestriction.class, name = "POOL")
})
public abstract class UserRestriction {

    protected String id;

    protected UserRestrictionScope scope;

    @JsonProperty("user_id")
    protected String userId;

    @JsonProperty("private_comment")
    protected String privateComment;

    @JsonProperty("will_expire")
    protected Date willExpire;

    protected Date created;

    UserRestriction() {}

    UserRestriction(UserRestrictionScope scope, String userId, String privateComment, Date willExpire) {
        this.scope = scope;
        this.userId = userId;
        this.privateComment = privateComment;
        this.willExpire = willExpire;
    }

    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    public void setWillExpire(Date willExpire) {
        this.willExpire = willExpire;
    }

    public String getId() {
        return id;
    }

    public UserRestrictionScope getScope() {
        return scope;
    }

    public String getUserId() {
        return userId;
    }

    public String getPrivateComment() {
        return privateComment;
    }

    public Date getWillExpire() {
        return willExpire;
    }

    public Date getCreated() {
        return created;
    }
}
