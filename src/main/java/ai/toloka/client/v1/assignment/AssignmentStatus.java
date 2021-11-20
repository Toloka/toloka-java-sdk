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

package ai.toloka.client.v1.assignment;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class AssignmentStatus extends FlexibleEnum<AssignmentStatus> {

    private AssignmentStatus(String name) {
        super(name);
    }

    public static final AssignmentStatus ACTIVE = new AssignmentStatus("ACTIVE");
    public static final AssignmentStatus SUBMITTED = new AssignmentStatus("SUBMITTED");
    public static final AssignmentStatus ACCEPTED = new AssignmentStatus("ACCEPTED");
    public static final AssignmentStatus REJECTED = new AssignmentStatus("REJECTED");
    public static final AssignmentStatus SKIPPED = new AssignmentStatus("SKIPPED");
    public static final AssignmentStatus EXPIRED = new AssignmentStatus("EXPIRED");

    private static final AssignmentStatus[] VALUES = {ACTIVE, SUBMITTED, ACCEPTED, REJECTED, SKIPPED, EXPIRED};
    private static final ConcurrentMap<String, AssignmentStatus> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static AssignmentStatus[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), AssignmentStatus.class);
    }

    @JsonCreator
    public static AssignmentStatus valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<AssignmentStatus>() {
            @Override public AssignmentStatus create(String name) {
                return new AssignmentStatus(name);
            }
        });
    }
}
