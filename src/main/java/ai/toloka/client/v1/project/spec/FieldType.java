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

package ai.toloka.client.v1.project.spec;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public class FieldType extends FlexibleEnum<FieldType> {

    public static final FieldType BOOLEAN = new FieldType("boolean");
    public static final FieldType ARRAY_BOOLEAN = new FieldType("array_boolean");

    public static final FieldType STRING = new FieldType("string");
    public static final FieldType ARRAY_STRING = new FieldType("array_string");

    public static final FieldType INTEGER = new FieldType("integer");
    public static final FieldType ARRAY_INTEGER = new FieldType("array_integer");

    public static final FieldType FLOAT = new FieldType("float");
    public static final FieldType ARRAY_FLOAT = new FieldType("array_float");

    public static final FieldType URL = new FieldType("url");
    public static final FieldType ARRAY_URL = new FieldType("array_url");

    public static final FieldType FILE = new FieldType("file");
    public static final FieldType ARRAY_FILE = new FieldType("array_file");

    public static final FieldType COORDINATES = new FieldType("coordinates");
    public static final FieldType ARRAY_COORDINATES = new FieldType("array_coordinates");

    public static final FieldType JSON = new FieldType("json");
    public static final FieldType ARRAY_JSON = new FieldType("array_json");

    private static final FieldType[] VALUES = {
            BOOLEAN, ARRAY_BOOLEAN,
            STRING, ARRAY_STRING,
            INTEGER, ARRAY_INTEGER,
            FLOAT, ARRAY_FLOAT,
            URL, ARRAY_URL,
            FILE, ARRAY_FILE,
            COORDINATES, ARRAY_COORDINATES,
            JSON, ARRAY_JSON
    };
    private static final ConcurrentMap<String, FieldType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static FieldType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), FieldType.class);
    }

    @JsonCreator
    public static FieldType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<FieldType>() {
            @Override public FieldType create(String name) {
                return new FieldType(name);
            }
        });
    }

    private FieldType(String name) {
        super(name);
    }
}
