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

package ai.toloka.client.v1.impl;

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.messagethread.MessageThread;
import ai.toloka.client.v1.messagethread.MessageThreadClient;
import ai.toloka.client.v1.messagethread.MessageThreadCompose;
import ai.toloka.client.v1.messagethread.MessageThreadFolders;
import ai.toloka.client.v1.messagethread.MessageThreadReply;
import ai.toloka.client.v1.messagethread.MessageThreadSearchRequest;

public class MessageThreadClientImpl extends AbstractClientImpl implements MessageThreadClient {

    private static final String MESSAGE_THREADS_PATH = "message-threads";

    private static final String MESSAGE_THREADS_COMPOSE_ACTION_PATH = "compose";

    private static final String MESSAGE_THREADS_REPLY_ACTION_PATH = "reply";

    private static final String MESSAGE_THREADS_ADD_TO_FOLDERS_ACTION_PATH = "add-to-folders";

    private static final String MESSAGE_THREADS_REMOVE_FROM_FOLDERS_ACTION_PATH = "remove-from-folders";

    MessageThreadClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<MessageThread> findMessageThreads(MessageThreadSearchRequest request) {
        return find(request, MESSAGE_THREADS_PATH, new TypeReference<SearchResult<MessageThread>>() {});
    }

    @Override
    public ModificationResult<MessageThread> composeMessageThread(MessageThreadCompose compose) {
        Assertions.checkArgNotNull(compose, "Compose may not be null");

        String path = MESSAGE_THREADS_PATH + "/" + MESSAGE_THREADS_COMPOSE_ACTION_PATH;
        return create(compose, path, MessageThread.class, null);
    }

    @Override
    public ModificationResult<MessageThread> replyMessageThread(String messageThreadId, MessageThreadReply reply) {
        Assertions.checkArgNotNull(messageThreadId, "Id may not be null");
        Assertions.checkArgNotNull(reply, "Reply may not be null");

        return executeSyncAction(reply, MESSAGE_THREADS_PATH, messageThreadId, MESSAGE_THREADS_REPLY_ACTION_PATH,
                MessageThread.class, null);
    }

    @Override
    public ModificationResult<MessageThread> addToFolders(String messageThreadId, MessageThreadFolders folders) {
        return changeFolders(messageThreadId, folders, MESSAGE_THREADS_ADD_TO_FOLDERS_ACTION_PATH);
    }

    private ModificationResult<MessageThread> changeFolders(String messageThreadId, MessageThreadFolders folders,
                                                            String actionPath) {
        Assertions.checkArgNotNull(messageThreadId, "Id may not be null");
        Assertions.checkArgNotNull(folders, "Folders may not be null");
        Assertions.checkArgNotNull(folders.getFolders(), "Folders set may not be null");

        return executeSyncAction(folders, MESSAGE_THREADS_PATH, messageThreadId, actionPath, MessageThread.class,
                null);
    }

    @Override
    public ModificationResult<MessageThread> removeFromFolders(String messageThreadId, MessageThreadFolders folders) {
        return changeFolders(messageThreadId, folders, MESSAGE_THREADS_REMOVE_FROM_FOLDERS_ACTION_PATH);
    }
}
