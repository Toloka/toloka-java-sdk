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

package ai.toloka.client.v1.training;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class TrainingCloseReason extends FlexibleEnum<TrainingCloseReason> {
    public static final TrainingCloseReason MANUAL = new TrainingCloseReason("MANUAL");
    public static final TrainingCloseReason EXPIRED = new TrainingCloseReason("EXPIRED");
    public static final TrainingCloseReason COMPLETED = new TrainingCloseReason("COMPLETED");
    public static final TrainingCloseReason NOT_ENOUGH_BALANCE = new TrainingCloseReason("NOT_ENOUGH_BALANCE");
    public static final TrainingCloseReason ASSIGNMENTS_LIMIT_EXCEEDED =
            new TrainingCloseReason("ASSIGNMENTS_LIMIT_EXCEEDED");
    public static final TrainingCloseReason BLOCKED = new TrainingCloseReason("BLOCKED");
    public static final TrainingCloseReason FOR_UPDATE = new TrainingCloseReason("FOR_UPDATE");

    private static final TrainingCloseReason[] VALUES = {MANUAL, EXPIRED, COMPLETED,
            NOT_ENOUGH_BALANCE, ASSIGNMENTS_LIMIT_EXCEEDED, BLOCKED, FOR_UPDATE};
    private static final ConcurrentMap<String, TrainingCloseReason> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private TrainingCloseReason(String name) {
        super(name);
    }

    public static TrainingCloseReason[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), TrainingCloseReason.class);
    }

    @JsonCreator
    public static TrainingCloseReason valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<TrainingCloseReason>() {
            @Override public TrainingCloseReason create(String name) {
                return new TrainingCloseReason(name);
            }
        });
    }


}
