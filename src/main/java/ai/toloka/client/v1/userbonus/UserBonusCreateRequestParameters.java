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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ai.toloka.client.v1.AbstractRequestParameters;
import ai.toloka.client.v1.RequestParameters;

public class UserBonusCreateRequestParameters extends AbstractRequestParameters implements RequestParameters {

    private static final String OPERATION_ID_PARAMETER = "operation_id";
    private static final String SKIP_INVALID_ITEMS_PARAMETER = "skip_invalid_items";

    private UUID operationId;
    private Boolean skipInvalidItems;

    public UUID getOperationId() {
        return operationId;
    }

    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
    }

    public Boolean getSkipInvalidItems() {
        return skipInvalidItems;
    }

    public void setSkipInvalidItems(Boolean skipInvalidItems) {
        this.skipInvalidItems = skipInvalidItems;
    }

    @Override public Map<String, Object> getQueryParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put(OPERATION_ID_PARAMETER, operationId);
        params.put(SKIP_INVALID_ITEMS_PARAMETER, skipInvalidItems);
        return filterNulls(params);
    }
}
