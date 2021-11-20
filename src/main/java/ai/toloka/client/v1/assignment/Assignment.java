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

package ai.toloka.client.v1.assignment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.solution.Solution;
import ai.toloka.client.v1.task.Task;

public class Assignment {

    private String id;

    @JsonProperty("task_suite_id")
    private String taskSuiteId;

    @JsonProperty("pool_id")
    private String poolId;

    @JsonProperty("user_id")
    private String userId;

    private AssignmentStatus status;

    private BigDecimal reward;

    private List<Task> tasks;

    @JsonProperty("first_declined_solution_attempt")
    public List<Solution> firstDeclinedSolutionAttempt;

    private List<Solution> solutions;

    @JsonProperty("public_comment")
    private String publicComment;

    private Boolean mixed;

    private Boolean automerged;

    private Date created;

    private Date submitted;

    private Date accepted;

    private Date rejected;

    private Date skipped;

    private Date expired;

    public String getId() {
        return id;
    }

    public String getTaskSuiteId() {
        return taskSuiteId;
    }

    public String getPoolId() {
        return poolId;
    }

    public String getUserId() {
        return userId;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Solution> getFirstDeclinedSolutionAttempt() {
        return firstDeclinedSolutionAttempt;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public String getPublicComment() {
        return publicComment;
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

    public Date getSubmitted() {
        return submitted;
    }

    public Date getAccepted() {
        return accepted;
    }

    public Date getRejected() {
        return rejected;
    }

    public Date getSkipped() {
        return skipped;
    }

    public Date getExpired() {
        return expired;
    }
}
