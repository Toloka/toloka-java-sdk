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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TbTaskViewSpec extends BaseTaskViewSpec {
    private String config;
    private Map<String, String> lock;
    private LocalizationConfig localizationConfig;

    @JsonCreator
    public TbTaskViewSpec(@JsonProperty("settings") Map<String, Object> settings) {
        super(settings, Type.TB);
    }

    public TbTaskViewSpec(TbTaskViewSpec original) {
        super(original, Type.TB);

        this.config = original.config;
        this.lock = new HashMap<>(original.lock);
        this.localizationConfig = new LocalizationConfig(original.localizationConfig);
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Map<String, String> getLock() {
        return lock;
    }

    public void setLock(Map<String, String> lock) {
        this.lock = lock;
    }

    public LocalizationConfig getLocalizationConfig() {
        return localizationConfig;
    }

    public void setLocalizationConfig(LocalizationConfig localizationConfig) {
        this.localizationConfig = localizationConfig;
    }

    public static class LocalizationConfig {
        public List<Key> keys;

        public LocalizationConfig() {
        }

        public LocalizationConfig(LocalizationConfig localizationConfig) {
            this.keys = localizationConfig.keys.stream().map(Key::new).collect(Collectors.toList());
        }

        public static class Key {
            public String key;
            public String defaultValue;

            public Key() {
            }

            public Key(String key, String defaultValue) {
                this.key = key;
                this.defaultValue = defaultValue;
            }

            public Key(Key viewKey) {
                this.key = viewKey.key;
                this.defaultValue = viewKey.defaultValue;
            }
        }
    }
}
