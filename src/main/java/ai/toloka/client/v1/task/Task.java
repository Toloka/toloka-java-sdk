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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task extends BaseTask {

    @JsonProperty("pool_id")
    private final String poolId;

    private Integer overlap;

    @JsonProperty("infinite_overlap")
    private Boolean infiniteOverlap;

    @JsonProperty("remaining_overlap")
    private Integer remainingOverlap;

    @JsonProperty("reserved_for")
    private Set<String> reservedFor;

    @JsonProperty("unavailable_for")
    private Set<String> unavailableFor;

    @JsonProperty("traits_all_of")
    private Set<String> traitsAllOf;

    @JsonProperty("traits_any_of")
    private Set<String> traitsAnyOf;

    @JsonProperty("traits_none_of_any")
    private Set<String> traitsNoneOfAny;

    private Date created;

    @JsonProperty("origin_task_id")
    private String originTaskId;

    @JsonProperty("baseline_solutions")
    private List<BaselineSolution> baselineSolutions;

    @JsonCreator
    public Task(@JsonProperty("pool_id") String poolId, @JsonProperty("input_values") Map<String, Object> inputValues) {
        super(inputValues);
        this.poolId = poolId;
    }

    public Task(String poolId, Map<String, Object> inputValues, List<KnownSolution> knownSolutions) {
        super(inputValues, knownSolutions);
        this.poolId = poolId;
    }

    public Task(String poolId, Map<String, Object> inputValues, List<KnownSolution> knownSolutions,
                String messageOnUnknownSolution) {
        super(inputValues, knownSolutions, messageOnUnknownSolution);
        this.poolId = poolId;
    }

    public Task(String poolId, Map<String, Object> inputValues, List<KnownSolution> knownSolutions,
                String messageOnUnknownSolution, TaskLocalizationConfig localizationConfig) {
        super(inputValues, knownSolutions, messageOnUnknownSolution, localizationConfig);
        this.poolId = poolId;
    }

    public String getPoolId() {
        return poolId;
    }

    public Integer getOverlap() {
        return overlap;
    }

    public void setOverlap(Integer overlap) {
        this.overlap = overlap;
    }

    public Boolean getInfiniteOverlap() {
        return infiniteOverlap;
    }

    public void setInfiniteOverlap(final Boolean infiniteOverlap) {
        this.infiniteOverlap = infiniteOverlap;
    }

    public Set<String> getReservedFor() {
        return reservedFor;
    }

    public void setReservedFor(Set<String> reservedFor) {
        this.reservedFor = reservedFor;
    }

    public Set<String> getUnavailableFor() {
        return unavailableFor;
    }

    public void setUnavailableFor(Set<String> unavailableFor) {
        this.unavailableFor = unavailableFor;
    }

    public Set<String> getTraitsAllOf() {
        return traitsAllOf;
    }

    public void setTraitsAllOf(Set<String> traitsAllOf) {
        this.traitsAllOf = traitsAllOf;
    }

    public Set<String> getTraitsAnyOf() {
        return traitsAnyOf;
    }

    public void setTraitsAnyOf(Set<String> traitsAnyOf) {
        this.traitsAnyOf = traitsAnyOf;
    }

    public Set<String> getTraitsNoneOfAny() {
        return traitsNoneOfAny;
    }

    public void setTraitsNoneOfAny(Set<String> traitsNoneOfAny) {
        this.traitsNoneOfAny = traitsNoneOfAny;
    }

    public Date getCreated() {
        return created;
    }

    public String getOriginTaskId() {
        return originTaskId;
    }

    public Integer getRemainingOverlap() {
        return remainingOverlap;
    }

    public List<BaselineSolution> getBaselineSolutions() {
        return baselineSolutions;
    }

    public void setBaselineSolutions(List<BaselineSolution> baselineSolutions) {
        this.baselineSolutions = baselineSolutions;
    }
}
