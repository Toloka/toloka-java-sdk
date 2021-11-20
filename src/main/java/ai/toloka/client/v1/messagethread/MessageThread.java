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

package ai.toloka.client.v1.messagethread;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.LangIso639;

public class MessageThread {

    private String id;

    private Map<LangIso639, String> topic;

    @JsonProperty("interlocutors_inlined")
    private boolean interlocutorsInlined;

    private List<Interlocutor> interlocutors;

    @JsonProperty("messages_inlined")
    private boolean messagesInlined;

    private List<Message> messages;

    @JsonProperty("compose_details")
    private ComposeDetails composeDetails;

    private Meta meta;

    private Boolean answerable;

    private Date created;

    private Set<Folder> folders;

    public String getId() {
        return id;
    }

    public Map<LangIso639, String> getTopic() {
        return topic;
    }

    public boolean isInterlocutorsInlined() {
        return interlocutorsInlined;
    }

    public List<Interlocutor> getInterlocutors() {
        return interlocutors;
    }

    public boolean isMessagesInlined() {
        return messagesInlined;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ComposeDetails getComposeDetails() {
        return composeDetails;
    }

    public Boolean getAnswerable() {
        return answerable;
    }

    public Date getCreated() {
        return created;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public Meta getMeta() {
        return meta;
    }
}
