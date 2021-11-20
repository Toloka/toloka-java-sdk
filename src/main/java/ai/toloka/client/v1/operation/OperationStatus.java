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

public final class OperationStatus extends FlexibleEnum<OperationStatus> {

    private final boolean terminal;

    private OperationStatus(String name, boolean terminal) {
        super(name);
        this.terminal = terminal;
    }

    public static final OperationStatus PENDING = new OperationStatus("PENDING", false);
    public static final OperationStatus RUNNING = new OperationStatus("RUNNING", false);
    public static final OperationStatus SUCCESS = new OperationStatus("SUCCESS", true);
    public static final OperationStatus FAIL = new OperationStatus("FAIL", true);

    public boolean isTerminal() {
        return terminal;
    }

    private static final OperationStatus[] VALUES = {PENDING, RUNNING, SUCCESS, FAIL};
    private static final ConcurrentMap<String, OperationStatus> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static OperationStatus[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), OperationStatus.class);
    }

    @JsonCreator
    public static OperationStatus valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<OperationStatus>() {
            @Override public OperationStatus create(String name) {
                return new OperationStatus(name, false);
            }
        });
    }

}
