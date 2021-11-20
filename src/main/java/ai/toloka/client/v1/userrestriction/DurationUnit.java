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

package ai.toloka.client.v1.userrestriction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public class DurationUnit extends FlexibleEnum<DurationUnit> {

    private DurationUnit(String name) {
        super(name);
    }

    public static final DurationUnit MINUTE = new DurationUnit("MINUTES");
    public static final DurationUnit HOUR = new DurationUnit("HOURS");
    public static final DurationUnit DAY = new DurationUnit("DAYS");
    public static final DurationUnit FOREVER = new DurationUnit("PERMANENT");

    private static final DurationUnit[] VALUES = {MINUTE, HOUR, DAY, FOREVER};
    private static final ConcurrentMap<String, DurationUnit> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static DurationUnit[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), DurationUnit.class);
    }

    @JsonCreator
    public static DurationUnit valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, DurationUnit::new);
    }

}
