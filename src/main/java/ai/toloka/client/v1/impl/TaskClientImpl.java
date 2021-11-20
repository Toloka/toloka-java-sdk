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

package ai.toloka.client.v1.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;

import ai.toloka.client.v1.BatchCreateResult;
import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.transport.TransportUtil;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.task.Task;
import ai.toloka.client.v1.task.TaskClient;
import ai.toloka.client.v1.task.TaskCreateBatchOperation;
import ai.toloka.client.v1.task.TaskCreateRequestParameters;
import ai.toloka.client.v1.task.TaskOverlapPatch;
import ai.toloka.client.v1.task.TaskPatch;
import ai.toloka.client.v1.task.TaskPatchRequestParameters;
import ai.toloka.client.v1.task.TaskSearchRequest;

import static ai.toloka.client.v1.impl.transport.MapperUtil.getObjectReader;

public class TaskClientImpl extends AbstractClientImpl implements TaskClient {

    private static final String TASKS_PATH = "tasks";
    private static final String SET_OVERLAP_OR_MIN_PATH = "set-overlap-or-min";

    TaskClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public ModificationResult<Task> createTask(Task task) {
        return createTask(task, null);
    }

    @Override
    public ModificationResult<Task> createTask(Task task, TaskCreateRequestParameters parameters) {
        Assertions.checkArgNotNull(task, "Task may not be null");

        return create(task, TASKS_PATH, Task.class, parameters != null ? parameters.getQueryParameters() : null);
    }

    @Override
    public BatchCreateResult<Task> createTasks(List<Task> tasks) {
        return createTasks(tasks, null);
    }

    @Override
    public BatchCreateResult<Task> createTasks(List<Task> tasks, TaskCreateRequestParameters parameters) {
        Assertions.checkArgNotNull(tasks, "Tasks may not be null");

        return createMultiple(tasks, TASKS_PATH, new TypeReference<BatchCreateResult<Task>>() {}, parameters);
    }

    @Override
    public TaskCreateBatchOperation createTasksAsync(Iterator<Task> tasks) {
        return createTasksAsync(tasks, null);
    }

    @Override
    public TaskCreateBatchOperation createTasksAsync(Iterator<Task> tasks, TaskCreateRequestParameters parameters) {
        return (TaskCreateBatchOperation) createMultipleAsync(tasks, TASKS_PATH, Operation.class, parameters);
    }

    @Override
    public SearchResult<Task> findTasks(TaskSearchRequest request) {
        return find(request, TASKS_PATH, new TypeReference<SearchResult<Task>>() {});
    }

    @Override
    public Task getTask(String taskId) {
        return get(taskId, TASKS_PATH, Task.class);
    }

    @Override
    public ModificationResult<Task> patchTask(String taskId, TaskPatch patch) {
        return patchTask(taskId, patch, null);
    }

    @Override
    public ModificationResult<Task> patchTask(String taskId, TaskPatch patch,
                                              TaskPatchRequestParameters parameters) {

        return patch(taskId, patch, TASKS_PATH, Task.class,
                parameters != null ? parameters.getQueryParameters() : null);
    }

    @Override
    public ModificationResult<Task> setOverlapOrMin(final String taskId, final TaskOverlapPatch overlapPatch) {
        Assertions.checkArgNotNull(taskId, "Id may not be null");
        Assertions.checkArgNotNull(overlapPatch, "Patch form may not be null");

        return new RequestExecutorWrapper<ModificationResult<Task>>() {
            @Override
            ModificationResult<Task> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), TASKS_PATH, taskId,
                        SET_OVERLAP_OR_MIN_PATH);

                HttpResponse response = TransportUtil.executePatch(getHttpClient(), uriBuilder.build(),
                        getHttpConsumer(), overlapPatch);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    @SuppressWarnings("unchecked")
                    ModificationResult<Task> result = new ModificationResult<>(
                            getObjectReader(Task.class).readValue(response.getEntity().getContent()), false);
                    return result;
                }

                throw parseException(response);
            }
        }.wrap();
    }
}
