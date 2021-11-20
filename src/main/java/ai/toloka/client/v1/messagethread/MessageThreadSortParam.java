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

import ai.toloka.client.v1.SortParam;

public enum MessageThreadSortParam implements SortParam {

    id(MessageThreadSearchRequest.ID_PARAMETER),
    created(MessageThreadSearchRequest.CREATED_PARAMETER);

    private String parameter;

    MessageThreadSortParam(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String parameter() {
        return parameter;
    }
}
