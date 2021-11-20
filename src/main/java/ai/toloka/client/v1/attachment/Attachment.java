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

package ai.toloka.client.v1.attachment;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ai.toloka.client.v1.Owner;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "attachment_type",
        visible = true, defaultImpl = Attachment.UnknownAttachment.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssignmentAttachment.class, name = "ASSIGNMENT_ATTACHMENT")
})
public abstract class Attachment<D> {

    protected String id;

    protected Owner owner;

    @JsonProperty("attachment_type")
    protected AttachmentType attachmentType;

    protected String name;

    @JsonProperty("media_type")
    protected String mediaType;

    protected D details;

    protected Date created;

    public String getId() {
        return id;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public String getName() {
        return name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public D getDetails() {
        return details;
    }

    public Date getCreated() {
        return created;
    }

    public Owner getOwner() {
        return owner;
    }

    public static class UnknownAttachment extends Attachment {}
}
