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

package ai.toloka.client.v1.pool.filter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ai.toloka.client.v1.FlexibleEnum;

public class Education extends FlexibleEnum<Education> {

    public static final Education BASIC = new Education("BASIC");
    public static final Education MIDDLE = new Education("MIDDLE");
    public static final Education HIGH = new Education("HIGH");

    private static final Education[] VALUES = {BASIC, MIDDLE, HIGH};
    private static final ConcurrentMap<String, Education> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private Education(String name) {
        super(name);
    }

    public static Education[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), Education.class);
    }

    public static Education valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Education>() {
            @Override
            public Education create(String name) {
                return new Education(name);
            }
        });
    }
}
