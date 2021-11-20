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

package ai.toloka.client.v1.project;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.FlexibleEnum;
import ai.toloka.client.v1.LangIso639;

public class ProjectLocalizationConfig {

    @JsonProperty("default_language")
    private LangIso639 defaultLanguage;

    @JsonProperty("additional_languages")
    private List<AdditionalLanguage> additionalLanguages;

    public ProjectLocalizationConfig() {
    }

    public ProjectLocalizationConfig(LangIso639 defaultLanguage,
                                     List<AdditionalLanguage> additionalLanguages) {
        this.defaultLanguage = defaultLanguage;
        this.additionalLanguages = additionalLanguages;
    }

    public LangIso639 getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(LangIso639 defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<AdditionalLanguage> getAdditionalLanguages() {
        return additionalLanguages;
    }

    public void setAdditionalLanguages(List<AdditionalLanguage> additionalLanguages) {
        this.additionalLanguages = additionalLanguages;
    }

    public static class AdditionalLanguage {

        private LangIso639 language;

        @JsonProperty("public_name")
        private LocalizedValue publicName;

        @JsonProperty("public_description")
        private LocalizedValue publicDescription;

        @JsonProperty("public_instructions")
        private LocalizedValue publicInstructions;

        @JsonProperty("tb_view_spec")
        private LocalizedTbViewSpec tbViewSpec;

        public AdditionalLanguage() {
        }

        public AdditionalLanguage(LangIso639 language) {
            this.language = language;
        }

        public LangIso639 getLanguage() {
            return language;
        }

        public void setLanguage(LangIso639 language) {
            this.language = language;
        }

        public LocalizedValue getPublicName() {
            return publicName;
        }

        public void setPublicName(LocalizedValue publicName) {
            this.publicName = publicName;
        }

        public LocalizedValue getPublicDescription() {
            return publicDescription;
        }

        public LocalizedTbViewSpec getTbViewSpec() {
            return tbViewSpec;
        }

        public void setTbViewSpec(LocalizedTbViewSpec tbViewSpec) {
            this.tbViewSpec = tbViewSpec;
        }

        public void setPublicDescription(LocalizedValue publicDescription) {
            this.publicDescription = publicDescription;
        }

        public LocalizedValue getPublicInstructions() {
            return publicInstructions;
        }

        public void setPublicInstructions(LocalizedValue publicInstructions) {
            this.publicInstructions = publicInstructions;
        }

        public static class LocalizedTbViewSpec {
            private List<LocalizedKeyValue> keys;

            public LocalizedTbViewSpec() {
            }

            public LocalizedTbViewSpec(List<LocalizedKeyValue> keys) {
                this.keys = keys;
            }

            public List<LocalizedKeyValue> getKeys() {
                return keys;
            }

            public void setKeys(List<LocalizedKeyValue> keys) {
                this.keys = keys;
            }
        }

        public static class LocalizedKeyValue {
            private String key;
            private String value;
            private Source source;

            public LocalizedKeyValue() {
            }

            public LocalizedKeyValue(String key, String value, Source source) {
                this.key = key;
                this.value = value;
                this.source = source;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Source getSource() {
                return source;
            }

            public void setSource(Source source) {
                this.source = source;
            }
        }

        public static class LocalizedValue {

            private String value;
            private Source source;

            public LocalizedValue() {
            }

            public LocalizedValue(String value, Source source) {
                this.value = value;
                this.source = source;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Source getSource() {
                return source;
            }

            public void setSource(Source source) {
                this.source = source;
            }
        }

        public static class Source extends FlexibleEnum<Source> {

            public static final Source REQUESTER = new Source("REQUESTER");

            private static final Source[] VALUES = {REQUESTER};
            private static final ConcurrentMap<String, Source> DISCOVERED_VALUES = new ConcurrentHashMap<>();

            public static Source[] values() {
                return values(VALUES, DISCOVERED_VALUES.values(), Source.class);
            }

            @JsonCreator
            public static Source valueOf(String name) {
                return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Source>() {
                    @Override public Source create(String name) {
                        return new Source(name);
                    }
                });
            }

            private Source(String name) {
                super(name);
            }
        }
    }
}
