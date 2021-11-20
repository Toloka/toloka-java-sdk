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

package ai.toloka.client.v1.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class PoolStatus extends FlexibleEnum<PoolStatus> {
    public static PoolStatus OPEN = new PoolStatus("OPEN");
    public static PoolStatus CLOSED = new PoolStatus("CLOSED");
    public static PoolStatus ARCHIVED = new PoolStatus("ARCHIVED");
    public static PoolStatus LOCKED = new PoolStatus("LOCKED");

    private static final PoolStatus[] VALUES = {OPEN, CLOSED, ARCHIVED, LOCKED};
    private static final ConcurrentMap<String, PoolStatus> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static PoolStatus[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), PoolStatus.class);
    }

    @JsonCreator
    public static PoolStatus valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<PoolStatus>() {
            @Override public PoolStatus create(String name) {
                return new PoolStatus(name);
            }
        });
    }

    private PoolStatus(String name) {
        super(name);
    }
}
