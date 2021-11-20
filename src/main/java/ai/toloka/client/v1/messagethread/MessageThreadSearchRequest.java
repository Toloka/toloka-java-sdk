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
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class MessageThreadSearchRequest extends SearchRequest {

    static final String FOLDER_PARAMETER = "folder";
    static final String FOLDER_NE_PARAMETER = "folder_ne";
    static final String ID_PARAMETER = "id";
    static final String CREATED_PARAMETER = "created";

    public MessageThreadSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                      String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static MessageThreadBuilder make() {
        return new MessageThreadBuilder(
                new MessageThreadFilterBuilder(), new MessageThreadRangeBuilder(), new MessageThreadSortBuilder());
    }

    public static class MessageThreadBuilder extends Builder
            <MessageThreadSearchRequest, MessageThreadBuilder, MessageThreadFilterBuilder, MessageThreadRangeBuilder,
                    MessageThreadSortBuilder> {

        public MessageThreadBuilder(MessageThreadFilterBuilder filterBuilder, MessageThreadRangeBuilder rangeBuilder,
                                 MessageThreadSortBuilder sortBuilder) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public MessageThreadSearchRequest done() {
            return new MessageThreadSearchRequest(filterBuilder.getFilterParameters(),
                    rangeBuilder.getRangeParameters(), sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class MessageThreadFilterBuilder
            extends FilterBuilder<MessageThreadFilterBuilder, MessageThreadBuilder, MessageThreadFilterParam> {

        public MessageThreadFilterBuilder byFolder(Folder folder) {
            return by(MessageThreadFilterParam.folder, folder);
        }

        public MessageThreadFilterBuilder byFolderNe(Folder folder) {
            return by(MessageThreadFilterParam.folder_ne, folder);
        }
    }

    public static class MessageThreadRangeBuilder
            extends RangeBuilder<MessageThreadRangeBuilder, MessageThreadBuilder, MessageThreadRangeParam> {

        public RangeItemBuilder<MessageThreadRangeBuilder> byId(String id) {
            return by(MessageThreadRangeParam.id, id);
        }

        public RangeItemBuilder<MessageThreadRangeBuilder> byCreated(Date created) {
            return by(MessageThreadRangeParam.created, created);
        }
    }

    public static class MessageThreadSortBuilder
            extends SortBuilder<MessageThreadSortBuilder, MessageThreadBuilder, MessageThreadSortParam> {

        public SortItem<MessageThreadSortBuilder> byId() {
            return by(MessageThreadSortParam.id);
        }

        public SortItem<MessageThreadSortBuilder> byCreated() {
            return by(MessageThreadSortParam.created);
        }
    }
}
