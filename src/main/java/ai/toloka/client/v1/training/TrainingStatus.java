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

public final class TrainingStatus extends FlexibleEnum<TrainingStatus> {
    public static TrainingStatus OPEN = new TrainingStatus("OPEN");
    public static TrainingStatus CLOSED = new TrainingStatus("CLOSED");
    public static TrainingStatus ARCHIVED = new TrainingStatus("ARCHIVED");
    public static TrainingStatus LOCKED = new TrainingStatus("LOCKED");

    private static final TrainingStatus[] VALUES = {OPEN, CLOSED, ARCHIVED, LOCKED};
    private static final ConcurrentMap<String, TrainingStatus> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static TrainingStatus[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), TrainingStatus.class);
    }

    @JsonCreator
    public static TrainingStatus valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<TrainingStatus>() {
            @Override public TrainingStatus create(String name) {
                return new TrainingStatus(name);
            }
        });
    }

    private TrainingStatus(String name) {
        super(name);
    }
}
