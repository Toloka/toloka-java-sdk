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

package ai.toloka.client.v1.operation;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ai.toloka.client.v1.impl.transport.MapperUtil;

public class OperationLogItem {

    private String type;

    private Boolean success;

    @JsonProperty
    private ObjectNode input;

    @JsonIgnore
    private Map<String, Object> inputMap;

    @JsonProperty
    private ObjectNode output;

    @JsonIgnore
    private Map<String, Object> outputMap;

    public String getType() {
        return type;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Map<String, Object> getInputAsMap() {
        if (inputMap == null) {
            try {
                inputMap = MapperUtil.getObjectReader(Map.class).readValue(this.input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return inputMap;
    }

    public Map<String, Object> getOutputAsMap() {
        if (outputMap == null) {
            try {
                outputMap = MapperUtil.getObjectReader(Map.class).readValue(this.output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return outputMap;
    }

    @Override public String toString() {
        return "OperationLogItem{"
                + "type='" + type + '\''
                + ", success=" + success
                + ", input=" + getInputAsMap()
                + ", output=" + getOutputAsMap()
                + '}';
    }
}
