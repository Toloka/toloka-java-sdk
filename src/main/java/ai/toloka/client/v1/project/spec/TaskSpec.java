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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskSpec {

    @JsonProperty("input_spec")
    private Map<String, FieldSpec> inputSpec;

    @JsonProperty("output_spec")
    private Map<String, FieldSpec> outputSpec;

    @JsonProperty("view_spec")
    private BaseTaskViewSpec viewSpec;

    @JsonCreator
    public TaskSpec(@JsonProperty("input_spec") Map<String, FieldSpec> inputSpec,
                    @JsonProperty("output_spec") Map<String, FieldSpec> outputSpec,
                    @JsonProperty("view_spec") BaseTaskViewSpec viewSpec) {

        this.inputSpec = inputSpec;
        this.outputSpec = outputSpec;
        this.viewSpec = viewSpec;
    }

    public Map<String, FieldSpec> getInputSpec() {
        return inputSpec;
    }

    public void setInputSpec(Map<String, FieldSpec> inputSpec) {
        this.inputSpec = inputSpec;
    }

    public Map<String, FieldSpec> getOutputSpec() {
        return outputSpec;
    }

    public void setOutputSpec(Map<String, FieldSpec> outputSpec) {
        this.outputSpec = outputSpec;
    }

    public BaseTaskViewSpec getViewSpec() {
        return viewSpec;
    }

    public void setViewSpec(BaseTaskViewSpec viewSpec) {
        this.viewSpec = viewSpec;
    }
}
