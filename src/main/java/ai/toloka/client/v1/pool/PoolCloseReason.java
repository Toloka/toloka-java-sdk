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

public final class PoolCloseReason extends FlexibleEnum<PoolCloseReason> {
    public static final PoolCloseReason MANUAL = new PoolCloseReason("MANUAL");
    public static final PoolCloseReason EXPIRED = new PoolCloseReason("EXPIRED");
    public static final PoolCloseReason COMPLETED = new PoolCloseReason("COMPLETED");
    public static final PoolCloseReason NOT_ENOUGH_BALANCE = new PoolCloseReason("NOT_ENOUGH_BALANCE");
    public static final PoolCloseReason ASSIGNMENTS_LIMIT_EXCEEDED = new PoolCloseReason("ASSIGNMENTS_LIMIT_EXCEEDED");
    public static final PoolCloseReason BLOCKED = new PoolCloseReason("BLOCKED");
    public static final PoolCloseReason FOR_UPDATE = new PoolCloseReason("FOR_UPDATE");

    private static final PoolCloseReason[] VALUES = {MANUAL, EXPIRED, COMPLETED,
            NOT_ENOUGH_BALANCE, ASSIGNMENTS_LIMIT_EXCEEDED, BLOCKED, FOR_UPDATE};
    private static final ConcurrentMap<String, PoolCloseReason> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private PoolCloseReason(String name) {
        super(name);
    }

    public static PoolCloseReason[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), PoolCloseReason.class);
    }

    @JsonCreator
    public static PoolCloseReason valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<PoolCloseReason>() {
            @Override public PoolCloseReason create(String name) {
                return new PoolCloseReason(name);
            }
        });
    }


}
