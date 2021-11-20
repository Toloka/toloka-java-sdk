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

public class RegionCompareOperator extends FlexibleEnum<RegionCompareOperator> {

    public static final RegionCompareOperator IN = new RegionCompareOperator("IN");
    public static final RegionCompareOperator NOT_IN = new RegionCompareOperator("NOT_IN");

    private static final RegionCompareOperator[] VALUES = {IN, NOT_IN};
    private static final ConcurrentMap<String, RegionCompareOperator> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private RegionCompareOperator(String name) {
        super(name);
    }

    public static RegionCompareOperator[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), RegionCompareOperator.class);
    }

    public static RegionCompareOperator valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<RegionCompareOperator>() {
            @Override
            public RegionCompareOperator create(String name) {
                return new RegionCompareOperator(name);
            }
        });
    }
}
