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

package ai.toloka.client.v1.project.spec;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = ClassicTaskViewSpec.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassicTaskViewSpec.class, name = "classic"),
        @JsonSubTypes.Type(value = TbTaskViewSpec.class, name = "tb")
})
public abstract class BaseTaskViewSpec {
    private Map<String, Object> settings;
    private Type type;

    public BaseTaskViewSpec(Map<String, Object> settings, Type type) {
        this.settings = settings;
        this.type = type;
    }

    public BaseTaskViewSpec(BaseTaskViewSpec original, Type type) {
        this.settings = original.settings;
        this.type = type;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        CLASSIC("classic"),
        TB("tb");

        final String value;

        Type(String type) {
            this.value = type;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
