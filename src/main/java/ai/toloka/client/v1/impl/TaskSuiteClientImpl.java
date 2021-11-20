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
import ai.toloka.client.v1.tasksuite.TaskSuite;
import ai.toloka.client.v1.tasksuite.TaskSuiteClient;
import ai.toloka.client.v1.tasksuite.TaskSuiteCreateBatchOperation;
import ai.toloka.client.v1.tasksuite.TaskSuiteCreateRequestParameters;
import ai.toloka.client.v1.tasksuite.TaskSuiteOverlapPatch;
import ai.toloka.client.v1.tasksuite.TaskSuitePatch;
import ai.toloka.client.v1.tasksuite.TaskSuitePatchRequestParameters;
import ai.toloka.client.v1.tasksuite.TaskSuiteSearchRequest;

import static ai.toloka.client.v1.impl.transport.MapperUtil.getObjectReader;

public class TaskSuiteClientImpl extends AbstractClientImpl implements TaskSuiteClient {

    private static final String TASK_SUITES_PATH = "task-suites";
    private static final String SET_OVERLAP_OR_MIN_PATH = "set-overlap-or-min";

    TaskSuiteClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public ModificationResult<TaskSuite> createTaskSuite(TaskSuite taskSuite) {
        return createTaskSuite(taskSuite, null);
    }

    @Override
    public ModificationResult<TaskSuite> createTaskSuite(final TaskSuite taskSuite,
                                                         final TaskSuiteCreateRequestParameters parameters) {

        Assertions.checkArgNotNull(taskSuite, "Task suite may not be null");

        return create(taskSuite, TASK_SUITES_PATH, TaskSuite.class,
                parameters != null ? parameters.getQueryParameters() : null);
    }

    @Override
    public BatchCreateResult<TaskSuite> createTaskSuites(List<TaskSuite> taskSuites) {
        return createTaskSuites(taskSuites, null);
    }

    @Override
    public BatchCreateResult<TaskSuite> createTaskSuites(final List<TaskSuite> taskSuites,
                                                         final TaskSuiteCreateRequestParameters parameters) {

        Assertions.checkArgNotNull(taskSuites, "Task suites may not be null");

        return createMultiple(
                taskSuites, TASK_SUITES_PATH, new TypeReference<BatchCreateResult<TaskSuite>>() {}, parameters);
    }

    @Override
    public TaskSuiteCreateBatchOperation createTaskSuitesAsync(Iterator<TaskSuite> taskSuites) {
        return createTaskSuitesAsync(taskSuites, null);
    }

    @Override
    public TaskSuiteCreateBatchOperation createTaskSuitesAsync(final Iterator<TaskSuite> taskSuites,
                                                               final TaskSuiteCreateRequestParameters parameters) {

        Assertions.checkArgNotNull(taskSuites, "Task suites may not be null");

        return (TaskSuiteCreateBatchOperation) createMultipleAsync(
                taskSuites, TASK_SUITES_PATH, Operation.class, parameters);
    }

    @Override
    public SearchResult<TaskSuite> findTaskSuites(TaskSuiteSearchRequest request) {
        return find(request, TASK_SUITES_PATH, new TypeReference<SearchResult<TaskSuite>>() {});
    }

    @Override
    public TaskSuite getTaskSuite(String taskSuiteId) {
        Assertions.checkArgNotNull(taskSuiteId, "Id may not be null");

        return get(taskSuiteId, TASK_SUITES_PATH, TaskSuite.class);
    }

    @Override
    public ModificationResult<TaskSuite> patchTaskSuite(String taskSuiteId, TaskSuitePatch patch) {
        return patchTaskSuite(taskSuiteId, patch, null);
    }

    @Override
    public ModificationResult<TaskSuite> patchTaskSuite(String taskSuiteId,
                                                        TaskSuitePatch patch,
                                                        TaskSuitePatchRequestParameters parameters) {
        Assertions.checkArgNotNull(taskSuiteId, "Id may not be null");
        Assertions.checkArgNotNull(patch, "Patch form may not be null");

        return patch(taskSuiteId, patch, TASK_SUITES_PATH, TaskSuite.class,
                parameters != null ? parameters.getQueryParameters() : null);
    }

    @Override
    public ModificationResult<TaskSuite> setOverlapOrMin(
            final String taskSuiteId,
            final TaskSuiteOverlapPatch overlapPatch
    ) {
        Assertions.checkArgNotNull(taskSuiteId, "Id may not be null");
        Assertions.checkArgNotNull(overlapPatch, "Overlap patch form may not be null");

        return new RequestExecutorWrapper<ModificationResult<TaskSuite>>() {
            @Override
            ModificationResult<TaskSuite> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), TASK_SUITES_PATH,
                        taskSuiteId, SET_OVERLAP_OR_MIN_PATH);

                HttpResponse response = TransportUtil.executePatch(getHttpClient(), uriBuilder.build(),
                        getHttpConsumer(), overlapPatch);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    @SuppressWarnings("unchecked")
                    ModificationResult<TaskSuite> result = new ModificationResult<>(
                            getObjectReader(TaskSuite.class).readValue(response.getEntity().getContent()), false);
                    return result;
                }

                throw parseException(response);
            }
        }.wrap();
    }
}
