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

package ai.toloka.client.v1.tasksuite;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.task.BaseTask;

public class TaskSuite {

    private String id;

    @JsonProperty("pool_id")
    private String poolId;

    private List<BaseTask> tasks;

    private Integer overlap;

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

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Boolean mixed;

    private Boolean automerged;

    private Date created;

    @JsonProperty("issuing_order_override")
    private Double issuingOrderOverride;

    @JsonCreator
    public TaskSuite(@JsonProperty("pool_id") String poolId, @JsonProperty("tasks") List<BaseTask> tasks) {
        this.poolId = poolId;
        this.tasks = tasks;
    }

    public void setOverlap(Integer overlap) {
        this.overlap = overlap;
    }

    public void setReservedFor(Set<String> reservedFor) {
        this.reservedFor = reservedFor;
    }

    public void setUnavailableFor(Set<String> unavailableFor) {
        this.unavailableFor = unavailableFor;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public String getPoolId() {
        return poolId;
    }

    public List<BaseTask> getTasks() {
        return tasks;
    }

    public Integer getOverlap() {
        return overlap;
    }

    public Set<String> getReservedFor() {
        return reservedFor;
    }

    public Set<String> getUnavailableFor() {
        return unavailableFor;
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

    public BigDecimal getLongitude() {
        return longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public Boolean getMixed() {
        return mixed;
    }

    public Boolean getAutomerged() {
        return automerged;
    }

    public Date getCreated() {
        return created;
    }

    public Double getIssuingOrderOverride() {
        return issuingOrderOverride;
    }

    public void setIssuingOrderOverride(Double issuingOrderOverride) {
        this.issuingOrderOverride = issuingOrderOverride;
    }

    public Integer getRemainingOverlap() {
        return remainingOverlap;
    }
}
