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

package ai.toloka.client.v1.pool;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.FlexibleEnum;

public class TaskDistributionFunction {

    public Scope scope;

    public Distribution distribution;

    @JsonProperty("window_days")
    public Long windowDays;

    public List<Interval> intervals;

    @JsonCreator
    public TaskDistributionFunction(@JsonProperty("scope") Scope scope,
                                    @JsonProperty("distribution") Distribution distribution,
                                    @JsonProperty("window_days") Long windowDays,
                                    @JsonProperty("intervals") List<Interval> intervals) {
        this.scope = scope;
        this.distribution = distribution;
        this.windowDays = windowDays;
        this.intervals = intervals;
    }

    public static class Scope extends FlexibleEnum<Scope> {

        public static final Scope PROJECT = new Scope("PROJECT");
        public static final Scope POOL = new Scope("POOL");

        private static final Scope[] VALUES = {PROJECT, POOL};
        private static final ConcurrentMap<String, Scope> DISCOVERED_VALUES = new ConcurrentHashMap<>();

        private Scope(String name) {
            super(name);
        }

        public static Scope[] values() {
            return values(VALUES, DISCOVERED_VALUES.values(), Scope.class);
        }

        public static Scope valueOf(String name) {
            return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Scope>() {
                @Override public Scope create(String name) {
                    return new Scope(name);
                }
            });
        }
    }

    public static class Distribution extends FlexibleEnum<Distribution> {

        public static final Distribution UNIFORM = new Distribution("UNIFORM");

        private static final Distribution[] VALUES = {UNIFORM};
        private static final ConcurrentMap<String, Distribution> DISCOVERED_VALUES = new ConcurrentHashMap<>();

        private Distribution(String name) {
            super(name);
        }

        public static Distribution[] values() {
            return values(VALUES, DISCOVERED_VALUES.values(), Distribution.class);
        }

        public static Distribution valueOf(String name) {
            return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Distribution>() {
                @Override public Distribution create(String name) {
                    return new Distribution(name);
                }
            });
        }
    }

    public static class Interval {

        private Long from;

        private Long to;

        private Integer frequency;

        @JsonCreator
        public Interval(@JsonProperty("from") Long from,
                        @JsonProperty("to") Long to,
                        @JsonProperty("frequency") Integer frequency) {
            this.from = from;
            this.to = to;
            this.frequency = frequency;
        }

        public Long getFrom() {
            return from;
        }

        public void setFrom(Long from) {
            this.from = from;
        }

        public Long getTo() {
            return to;
        }

        public void setTo(Long to) {
            this.to = to;
        }

        public Integer getFrequency() {
            return frequency;
        }

        public void setFrequency(Integer frequency) {
            this.frequency = frequency;
        }
    }
}
