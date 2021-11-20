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

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public class CompareOperator extends FlexibleEnum<CompareOperator> {

    public static final CompareOperator EQ = new CompareOperator("EQ");
    public static final CompareOperator NE = new CompareOperator("NE");
    public static final CompareOperator GT = new CompareOperator("GT");
    public static final CompareOperator GTE = new CompareOperator("GTE");
    public static final CompareOperator LT = new CompareOperator("LT");
    public static final CompareOperator LTE = new CompareOperator("LTE");

    private static final CompareOperator[] VALUES = {EQ, NE, GT, GTE, LT, LTE};
    private static final ConcurrentMap<String, CompareOperator> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private CompareOperator(String name) {
        super(name);
    }

    public static CompareOperator[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), CompareOperator.class);
    }

    @JsonCreator
    public static CompareOperator valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<CompareOperator>() {
            @Override
            public CompareOperator create(String name) {
                return new CompareOperator(name);
            }
        });
    }
}
