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

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskOverlapPatch {
    
    private Integer overlap;
    @JsonProperty("infinite_overlap")
    private Boolean infiniteOverlap;

    public TaskOverlapPatch() {
    }

    public TaskOverlapPatch(final int overlap) {
        this.overlap = overlap;
        this.infiniteOverlap = null;
    }

    public TaskOverlapPatch(final boolean infiniteOverlap) {
        this.overlap = null;
        this.infiniteOverlap = infiniteOverlap;
    }

    public Integer getOverlap() {
        return overlap;
    }

    public void setOverlap(final Integer overlap) {
        this.overlap = overlap;
    }

    public Boolean getInfiniteOverlap() {
        return infiniteOverlap;
    }

    public void setInfiniteOverlap(final Boolean infiniteOverlap) {
        this.infiniteOverlap = infiniteOverlap;
    }
}
