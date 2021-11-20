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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public class OperationType extends FlexibleEnum<OperationType> {

    public static final OperationType PSEUDO = new OperationType("PSEUDO.PSEUDO");

    public static final OperationType PROJECT_ARCHIVE = new OperationType("PROJECT.ARCHIVE");

    public static final OperationType POOL_OPEN = new OperationType("POOL.OPEN");
    public static final OperationType POOL_CLOSE = new OperationType("POOL.CLOSE");
    public static final OperationType POOL_ARCHIVE = new OperationType("POOL.ARCHIVE");
    public static final OperationType POOL_CLONE = new OperationType("POOL.CLONE");

    public static final OperationType TRAINING_OPEN = new OperationType("TRAINING.OPEN");
    public static final OperationType TRAINING_CLOSE = new OperationType("TRAINING.CLOSE");
    public static final OperationType TRAINING_ARCHIVE = new OperationType("TRAINING.ARCHIVE");
    public static final OperationType TRAINING_CLONE = new OperationType("TRAINING.CLONE");

    public static final OperationType TASK_BATCH_CREATE = new OperationType("TASK.BATCH_CREATE");

    public static final OperationType TASK_SUITE_BATCH_CREATE = new OperationType("TASK_SUITE.BATCH_CREATE");

    public static final OperationType USER_BONUS_BATCH_CREATE = new OperationType("USER_BONUS.BATCH_CREATE");

    public static final OperationType ANALYTICS = new OperationType("ANALYTICS");

    public static final OperationType SOLUTION_AGGREGATE = new OperationType("SOLUTION.AGGREGATE");

    private static final OperationType[] VALUES = {
            PROJECT_ARCHIVE,

            POOL_OPEN, POOL_CLOSE, POOL_ARCHIVE, POOL_CLONE,

            TRAINING_OPEN, TRAINING_CLOSE, TRAINING_ARCHIVE, TRAINING_CLONE,

            TASK_BATCH_CREATE,

            TASK_SUITE_BATCH_CREATE,

            USER_BONUS_BATCH_CREATE,

            ANALYTICS,

            SOLUTION_AGGREGATE,

            PSEUDO
    };
    private static final ConcurrentMap<String, OperationType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static OperationType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), OperationType.class);
    }

    @JsonCreator
    public static OperationType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<OperationType>() {
            @Override public OperationType create(String name) {
                return new OperationType(name);
            }
        });
    }

    private OperationType(String name) {
        super(name);
    }
}
