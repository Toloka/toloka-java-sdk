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

package ai.toloka.client.v1.aggregatedsolutions;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregatedSolution {

    @JsonProperty("pool_id")
    private String poolId;

    @JsonProperty("task_id")
    private String taskId;

    private double confidence;

    @JsonProperty("output_values")
    private Map<String, Object> outputValues;

    public String getPoolId() {
        return poolId;
    }

    public String getTaskId() {
        return taskId;
    }

    public double getConfidence() {
        return confidence;
    }

    public Map<String, Object> getOutputValues() {
        return outputValues;
    }
}
