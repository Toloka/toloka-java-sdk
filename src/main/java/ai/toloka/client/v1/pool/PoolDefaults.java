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

package ai.toloka.client.v1.pool;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PoolDefaults {

    @JsonProperty("default_overlap_for_new_task_suites")
    private Integer defaultOverlapForNewTaskSuites;

    @JsonProperty("default_overlap_for_new_tasks")
    private Integer defaultOverlapForNewTasks;

    @JsonCreator
    public PoolDefaults(@JsonProperty("default_overlap_for_new_task_suites") Integer defaultOverlapForNewTaskSuites) {
        this.defaultOverlapForNewTaskSuites = defaultOverlapForNewTaskSuites;
    }

    public void setDefaultOverlapForNewTaskSuites(Integer defaultOverlapForNewTaskSuites) {
        this.defaultOverlapForNewTaskSuites = defaultOverlapForNewTaskSuites;
    }

    public Integer getDefaultOverlapForNewTaskSuites() {
        return defaultOverlapForNewTaskSuites;
    }

    public Integer getDefaultOverlapForNewTasks() {
        return defaultOverlapForNewTasks;
    }

    public void setDefaultOverlapForNewTasks(Integer defaultOverlapForNewTasks) {
        this.defaultOverlapForNewTasks = defaultOverlapForNewTasks;
    }
}
