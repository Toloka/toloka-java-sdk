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

package ai.toloka.client.v1.pool.qualitycontrol;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QualityControlConfig {

    @JsonProperty("collector_config")
    private CollectorConfig collectorConfig;

    private List<RuleConfig> rules;

    @JsonCreator
    public QualityControlConfig(@JsonProperty("collector_config") CollectorConfig<?> collectorConfig,
                                @JsonProperty("rules") List<RuleConfig> rules) {

        this.collectorConfig = collectorConfig;
        this.rules = rules;
    }

    public CollectorConfig<?> getCollectorConfig() {
        return collectorConfig;
    }

    public void setCollectorConfig(CollectorConfig<?> collectorConfig) {
        this.collectorConfig = collectorConfig;
    }

    public List<RuleConfig> getRules() {
        return rules;
    }

    public void setRules(List<RuleConfig> rules) {
        this.rules = rules;
    }
}
