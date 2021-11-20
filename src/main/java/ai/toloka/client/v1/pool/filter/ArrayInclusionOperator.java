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

public class ArrayInclusionOperator extends FlexibleEnum<ArrayInclusionOperator> {

    public static final ArrayInclusionOperator IN = new ArrayInclusionOperator("IN");
    public static final ArrayInclusionOperator NOT_IN = new ArrayInclusionOperator("NOT_IN");

    private static final ArrayInclusionOperator[] VALUES = {IN, NOT_IN};
    private static final ConcurrentMap<String, ArrayInclusionOperator> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private ArrayInclusionOperator(String name) {
        super(name);
    }

    public static ArrayInclusionOperator[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), ArrayInclusionOperator.class);
    }

    public static ArrayInclusionOperator valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<ArrayInclusionOperator>() {
            @Override
            public ArrayInclusionOperator create(String name) {
                return new ArrayInclusionOperator(name);
            }
        });
    }
}
