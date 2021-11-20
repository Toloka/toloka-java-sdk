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

import java.util.Iterator;
import java.util.List;

import ai.toloka.client.v1.BatchCreateResult;
import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;

public interface TaskSuiteClient {

    ModificationResult<TaskSuite> createTaskSuite(TaskSuite taskSuite);

    ModificationResult<TaskSuite> createTaskSuite(TaskSuite taskSuite, TaskSuiteCreateRequestParameters parameters);

    BatchCreateResult<TaskSuite> createTaskSuites(List<TaskSuite> taskSuites);

    BatchCreateResult<TaskSuite> createTaskSuites(List<TaskSuite> taskSuites,
                                                  TaskSuiteCreateRequestParameters parameters);

    TaskSuiteCreateBatchOperation createTaskSuitesAsync(Iterator<TaskSuite> taskSuites);

    TaskSuiteCreateBatchOperation createTaskSuitesAsync(Iterator<TaskSuite> taskSuites,
                                                        TaskSuiteCreateRequestParameters parameters);

    SearchResult<TaskSuite> findTaskSuites(TaskSuiteSearchRequest request);

    TaskSuite getTaskSuite(String taskSuiteId);

    ModificationResult<TaskSuite> patchTaskSuite(String taskSuiteId, TaskSuitePatch patch);

    ModificationResult<TaskSuite> patchTaskSuite(String taskSuiteId,
                                                 TaskSuitePatch patch,
                                                 TaskSuitePatchRequestParameters parameters);
    
    ModificationResult<TaskSuite> setOverlapOrMin(String taskSuiteId, TaskSuiteOverlapPatch overlapPatch);
    
}
