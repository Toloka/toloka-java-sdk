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

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolution;
import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionClient;
import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionOperation;
import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionSearchRequest;
import ai.toloka.client.v1.aggregatedsolutions.PoolAggregatedSolutionRequest;
import ai.toloka.client.v1.aggregatedsolutions.TaskAggregatedSolutionRequest;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;

public class AggregatedSolutionClientImpl extends AbstractClientImpl implements AggregatedSolutionClient {

    private static final String AGGREGATED_SOLUTIONS_PATH = "aggregated-solutions";

    public AggregatedSolutionClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public AggregatedSolutionOperation aggregateSolutionsByPool(PoolAggregatedSolutionRequest request) {
        Assertions.checkArgNotNull(request, "Request may not be null");

        return (AggregatedSolutionOperation) executeAsync(request,
                AGGREGATED_SOLUTIONS_PATH + "/aggregate-by-pool", Operation.class);
    }

    @Override
    public ModificationResult<AggregatedSolution> aggregateSolutionsByTask(TaskAggregatedSolutionRequest request) {
        Assertions.checkArgNotNull(request, "Request may not be null");
        Assertions.checkArgNotNull(request.getTaskId(), "Task id may not be null");

        return executeSyncAction(request, AGGREGATED_SOLUTIONS_PATH, null,
                "aggregate-by-task", AggregatedSolution.class, null);
    }

    @Override
    public SearchResult<AggregatedSolution> findAggregatedSolutions(String operationId,
                                                                    AggregatedSolutionSearchRequest request) {
        return find(request, AGGREGATED_SOLUTIONS_PATH + "/" + operationId,
                new TypeReference<SearchResult<AggregatedSolution>>() {});
    }
}
