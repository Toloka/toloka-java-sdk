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

package ai.toloka.client.v1;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BatchCreateResult<T> {

    private Map<Integer, T> items;

    @JsonProperty("validation_errors")
    private Map<Integer, Map<String, FieldValidationError>> validationsErrors;

    public Map<Integer, T> getItems() {
        return items;
    }

    public Map<Integer, Map<String, FieldValidationError>> getValidationsErrors() {
        return validationsErrors;
    }
}
