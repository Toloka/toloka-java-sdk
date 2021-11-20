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

package ai.toloka.client.v1.project;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class AssignmentsIssuingType extends FlexibleEnum<AssignmentsIssuingType> {

    private AssignmentsIssuingType(String name) {
        super(name);
    }

    public static final AssignmentsIssuingType AUTOMATED = new AssignmentsIssuingType("AUTOMATED");
    public static final AssignmentsIssuingType MAP_SELECTOR = new AssignmentsIssuingType("MAP_SELECTOR");

    private static final AssignmentsIssuingType[] VALUES = {AUTOMATED, MAP_SELECTOR};
    private static final ConcurrentMap<String, AssignmentsIssuingType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static AssignmentsIssuingType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), AssignmentsIssuingType.class);
    }

    @JsonCreator
    public static AssignmentsIssuingType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<AssignmentsIssuingType>() {
            @Override public AssignmentsIssuingType create(String name) {
                return new AssignmentsIssuingType(name);
            }
        });
    }

}
