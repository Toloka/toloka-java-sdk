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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldValidationError {

    private final String code;
    private final String message;
    private final List<Object> params;

    @JsonCreator
    public FieldValidationError(@JsonProperty("code") String code, @JsonProperty("message") String message,
                                @JsonProperty("params") List<Object> params) {
        this.code = code;
        this.message = message;
        this.params = params != null ? params : new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Object> getParams() {
        return params;
    }

    @Override public String toString() {
        return "FieldValidationError{"
                + "code='" + code + '\''
                + ", message='" + message + '\''
                + ", params=" + params
                + '}';
    }
}
