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

package ai.toloka.client.v1.operation;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionOperation;
import ai.toloka.client.v1.impl.transport.MapperUtil;
import ai.toloka.client.v1.pool.PoolArchiveOperation;
import ai.toloka.client.v1.pool.PoolCloneOperation;
import ai.toloka.client.v1.pool.PoolCloseOperation;
import ai.toloka.client.v1.pool.PoolOpenOperation;
import ai.toloka.client.v1.project.ProjectArchiveOperation;
import ai.toloka.client.v1.task.KnownSolutionsGenerateOperation;
import ai.toloka.client.v1.task.TaskCreateBatchOperation;
import ai.toloka.client.v1.tasksuite.TaskSuiteCreateBatchOperation;
import ai.toloka.client.v1.training.TrainingArchiveOperation;
import ai.toloka.client.v1.training.TrainingCloneOperation;
import ai.toloka.client.v1.training.TrainingCloseOperation;
import ai.toloka.client.v1.training.TrainingOpenOperation;
import ai.toloka.client.v1.userbonus.UserBonusCreateBatchOperation;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type",
        visible = true, defaultImpl = Operation.UnknownOperation.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProjectArchiveOperation.class, name = "PROJECT.ARCHIVE"),
        @JsonSubTypes.Type(value = PoolOpenOperation.class, name = "POOL.OPEN"),
        @JsonSubTypes.Type(value = PoolCloseOperation.class, name = "POOL.CLOSE"),
        @JsonSubTypes.Type(value = PoolArchiveOperation.class, name = "POOL.ARCHIVE"),
        @JsonSubTypes.Type(value = PoolCloneOperation.class, name = "POOL.CLONE"),
        @JsonSubTypes.Type(value = TrainingOpenOperation.class, name = "TRAINING.OPEN"),
        @JsonSubTypes.Type(value = TrainingCloseOperation.class, name = "TRAINING.CLOSE"),
        @JsonSubTypes.Type(value = TrainingArchiveOperation.class, name = "TRAINING.ARCHIVE"),
        @JsonSubTypes.Type(value = TrainingCloneOperation.class, name = "TRAINING.CLONE"),
        @JsonSubTypes.Type(value = TaskCreateBatchOperation.class, name = "TASK.BATCH_CREATE"),
        @JsonSubTypes.Type(value = TaskSuiteCreateBatchOperation.class, name = "TASK_SUITE.BATCH_CREATE"),
        @JsonSubTypes.Type(value = UserBonusCreateBatchOperation.class, name = "USER_BONUS.BATCH_CREATE"),
        @JsonSubTypes.Type(value = AggregatedSolutionOperation.class, name = "SOLUTION.AGGREGATE"),
        @JsonSubTypes.Type(value = KnownSolutionsGenerateOperation.class, name = "KNOWN_SOLUTIONS.GENERATE")
})
public abstract class Operation<P, T extends Operation<P, T>> {

    public static final String PSEUDO_OPERATION_ID = "PSEUDO_ID";
    public static final OperationType DEFAULT_PSEUDO_OPERATION_TYPE = OperationType.PSEUDO;

    protected String id;

    protected OperationType type;

    protected OperationStatus status;

    protected Date submitted;

    protected Date started;

    protected Date finished;

    protected Integer progress;

    protected P parameters;

    @JsonProperty
    protected JsonNode details;

    @JsonIgnore
    private Map<String, Object> detailsMap;

    @JsonIgnore
    protected OperationClient operationClient;

    public Operation() {}

    /**
     * Constructor for manual creation of pseudo operations.
     */
    protected Operation(Date currentDateTime) {
        this.id = PSEUDO_OPERATION_ID;
        this.type = OperationType.PSEUDO;
        this.status = OperationStatus.SUCCESS;
        this.submitted = currentDateTime;
        this.started = currentDateTime;
        this.finished = currentDateTime;
        this.progress = 100;
    }

    public String getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public Date getStarted() {
        return started;
    }

    public Date getFinished() {
        return finished;
    }

    public Integer getProgress() {
        return progress;
    }

    public P getParameters() {
        return parameters;
    }

    public Map<String, Object> getDetailsAsMap() {
        if (details == null) {
            return Collections.emptyMap();
        }
        if (detailsMap == null) {
            try {
                this.detailsMap = MapperUtil.getObjectReader(Map.class).readValue(details);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return detailsMap;
    }

    public boolean isCompleted() {
        return getStatus().isTerminal();
    }

    public boolean isSuccess() {
        return getStatus() == OperationStatus.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public T refresh() {
        if (isPseudo()) {
            return (T) this;
        } else {
            return operationClient.getOperation(id);
        }
    }

    @SuppressWarnings("unchecked")
    public T waitToComplete() throws InterruptedException {
        if (isPseudo()) {
            return (T) this;
        } else {
            return OperationUtil.waitToComplete((T) this, operationClient);
        }
    }

    @SuppressWarnings("unchecked")
    public T waitToCompleteUninterrupted() {
        if (isPseudo()) {
            return (T) this;
        } else {
            return OperationUtil.waitToCompleteUninterrupted((T) this, operationClient);
        }
    }

    @SuppressWarnings("unchecked")
    public T waitAndGetSuccessful() throws InterruptedException {
        if (isPseudo()) {
            return (T) this;
        } else {
            return OperationUtil.waitAndGetSuccessful((T) this, operationClient);
        }
    }

    @SuppressWarnings("unchecked")
    public T waitAndGetSuccessfulUninterrupted() {
        if (isPseudo()) {
            return (T) this;
        } else {
            return OperationUtil.waitAndGetSuccessfulUninterrupted((T) this, operationClient);
        }
    }

    public boolean isPseudo() {
        return getId().equals(PSEUDO_OPERATION_ID);
    }

    public static void setOperationClient(Operation<?, ?> operation, OperationClient operationClient) {
        operation.operationClient = operationClient;
    }

    public static Operation createPseudo(Date currentDateTime) {
        return new Operation(currentDateTime) {};
    }

    public static class UnknownOperation extends Operation {}
}
