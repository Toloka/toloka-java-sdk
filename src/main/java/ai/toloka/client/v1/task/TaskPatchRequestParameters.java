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

package ai.toloka.client.v1.task;

import java.util.HashMap;
import java.util.Map;

import ai.toloka.client.v1.AbstractRequestParameters;
import ai.toloka.client.v1.RequestParameters;

public class TaskPatchRequestParameters extends AbstractRequestParameters implements RequestParameters {

    private static final String OPEN_POOL_PARAMETER = "open_pool";

    private Boolean openPool;

    public Boolean getOpenPool() {
        return openPool;
    }

    public void setOpenPool(Boolean openPool) {
        this.openPool = openPool;
    }

    @Override public Map<String, Object> getQueryParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put(OPEN_POOL_PARAMETER, openPool);
        return filterNulls(params);
    }
}
