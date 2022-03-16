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

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseTask {

    private String id;

    @JsonProperty("input_values")
    private final Map<String, Object> inputValues;

    @JsonProperty("known_solutions")
    private List<KnownSolution> knownSolutions;

    @JsonProperty("message_on_unknown_solution")
    private String messageOnUnknownSolution;

    @JsonProperty("localization_config")
    private TaskLocalizationConfig localizationConfig;

    @JsonCreator
    public BaseTask(@JsonProperty("input_values") Map<String, Object> inputValues) {
        this.inputValues = inputValues;
    }

    public BaseTask(Map<String, Object> inputValues, List<KnownSolution> knownSolutions) {
        this(inputValues, knownSolutions, null);
    }

    public BaseTask(Map<String, Object> inputValues, List<KnownSolution> knownSolutions,
                    String messageOnUnknownSolution) {
        this(inputValues, knownSolutions, messageOnUnknownSolution, null);
    }

    public BaseTask(Map<String, Object> inputValues, List<KnownSolution> knownSolutions,
                    String messageOnUnknownSolution, TaskLocalizationConfig localizationConfig) {
        this.inputValues = inputValues;
        this.knownSolutions = knownSolutions;
        this.messageOnUnknownSolution = messageOnUnknownSolution;
        this.localizationConfig = localizationConfig;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getInputValues() {
        return inputValues;
    }

    public List<KnownSolution> getKnownSolutions() {
        return knownSolutions;
    }

    public void setKnownSolutions(List<KnownSolution> knownSolutions) {
        this.knownSolutions = knownSolutions;
    }

    public String getMessageOnUnknownSolution() {
        return messageOnUnknownSolution;
    }

    public void setMessageOnUnknownSolution(String messageOnUnknownSolution) {
        this.messageOnUnknownSolution = messageOnUnknownSolution;
    }

    public TaskLocalizationConfig getLocalizationConfig() {
        return localizationConfig;
    }
}
