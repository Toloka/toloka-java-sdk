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

public class Gender extends FlexibleEnum<Gender> {

    public static final Gender MALE = new Gender("MALE");
    public static final Gender FEMALE = new Gender("FEMALE");
    public static final Gender OTHER = new Gender("OTHER");

    private static final Gender[] VALUES = {MALE, FEMALE, OTHER};
    private static final ConcurrentMap<String, Gender> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private Gender(String name) {
        super(name);
    }

    public static Gender[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), Gender.class);
    }

    public static Gender valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Gender>() {
            @Override
            public Gender create(String name) {
                return new Gender(name);
            }
        });
    }
}
