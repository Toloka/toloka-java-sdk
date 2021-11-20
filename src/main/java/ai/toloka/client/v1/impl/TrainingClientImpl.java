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

import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.training.Training;
import ai.toloka.client.v1.training.TrainingArchiveOperation;
import ai.toloka.client.v1.training.TrainingClient;
import ai.toloka.client.v1.training.TrainingCloneOperation;
import ai.toloka.client.v1.training.TrainingCloseOperation;
import ai.toloka.client.v1.training.TrainingOpenOperation;
import ai.toloka.client.v1.training.TrainingSearchRequest;

public class TrainingClientImpl extends AbstractClientImpl implements TrainingClient {

    private static final String TRAININGS_PATH = "trainings";
    private static final String TRAININGS_OPEN_ACTION_PATH = "open";
    private static final String TRAININGS_CLOSE_ACTION_PATH = "close";
    private static final String TRAININGS_ARCHIVE_ACTION_PATH = "archive";
    private static final String TRAININGS_CLONE_ACTION_PATH = "clone";

    TrainingClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<Training> findTrainings(final TrainingSearchRequest request) {
        return find(request, TRAININGS_PATH, new TypeReference<SearchResult<Training>>() {});
    }

    @Override
    public Training getTraining(final String id) {
        return get(id, TRAININGS_PATH, Training.class);
    }

    @Override
    public ModificationResult<Training> createTraining(final Training training) {
        Assertions.checkArgNotNull(training, "Training may not be null");

        return create(training, TRAININGS_PATH, Training.class, null);
    }

    @Override
    public ModificationResult<Training> updateTraining(String trainingId, Training training) {
        Assertions.checkArgNotNull(trainingId, "Training id may not be null");
        Assertions.checkArgNotNull(training, "Training form may not be null");

        return update(trainingId, training, TRAININGS_PATH, Training.class);
    }

    @Override
    public TrainingOpenOperation openTraining(final String trainingId) {
        Assertions.checkArgNotNull(trainingId, "Id may not be null");

        TrainingOpenOperation operation = (TrainingOpenOperation) executeAction(trainingId, TRAININGS_PATH,
                TRAININGS_OPEN_ACTION_PATH, Operation.class);
        if (operation == null) {
            return TrainingOpenOperation.createPseudo(new Date());
        }

        return operation;
    }

    @Override
    public TrainingCloseOperation closeTraining(final String trainingId) {
        Assertions.checkArgNotNull(trainingId, "Id may not be null");

        TrainingCloseOperation operation = (TrainingCloseOperation) executeAction(trainingId, TRAININGS_PATH,
                TRAININGS_CLOSE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return TrainingCloseOperation.createPseudo(new Date());
        }

        return operation;
    }

    @Override
    public TrainingArchiveOperation archiveTraining(final String trainingId) {
        Assertions.checkArgNotNull(trainingId, "Id may not be null");

        TrainingArchiveOperation operation = (TrainingArchiveOperation) executeAction(trainingId, TRAININGS_PATH,
                TRAININGS_ARCHIVE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return TrainingArchiveOperation.createPseudo(new Date());
        }

        return operation;
    }

    @Override
    public TrainingCloneOperation cloneTraining(String trainingId) {
        Assertions.checkArgNotNull(trainingId, "Id may not be null");

        TrainingCloneOperation operation = (TrainingCloneOperation) executeAction(trainingId, TRAININGS_PATH,
                TRAININGS_CLONE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return TrainingCloneOperation.createPseudo(new Date());
        }

        return operation;
    }
}
