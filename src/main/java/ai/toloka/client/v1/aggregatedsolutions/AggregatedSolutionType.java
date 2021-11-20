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

package ai.toloka.client.v1.aggregatedsolutions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class AggregatedSolutionType extends FlexibleEnum<AggregatedSolutionType> {

    private AggregatedSolutionType(String name) {
        super(name);
    }

    public static final AggregatedSolutionType WEIGHTED_DYNAMIC_OVERLAP =
            new AggregatedSolutionType("WEIGHTED_DYNAMIC_OVERLAP");
    public static final AggregatedSolutionType DAWID_SKENE = new AggregatedSolutionType("DAWID_SKENE");

    private static final AggregatedSolutionType[] VALUES = {WEIGHTED_DYNAMIC_OVERLAP, DAWID_SKENE};
    private static final ConcurrentMap<String, AggregatedSolutionType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static AggregatedSolutionType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), AggregatedSolutionType.class);
    }

    @JsonCreator
    public static AggregatedSolutionType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new FlexibleEnum.NewEnumCreator<AggregatedSolutionType>() {
            @Override public AggregatedSolutionType create(String name) {
                return new AggregatedSolutionType(name);
            }
        });
    }
}

