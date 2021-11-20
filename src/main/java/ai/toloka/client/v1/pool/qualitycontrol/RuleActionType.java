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

package ai.toloka.client.v1.pool.qualitycontrol;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ai.toloka.client.v1.FlexibleEnum;

public class RuleActionType extends FlexibleEnum<RuleActionType> {

    public static final RuleActionType RESTRICTION = new RuleActionType("RESTRICTION");
    public static final RuleActionType RESTRICTION_V2 = new RuleActionType("RESTRICTION_V2");
    public static final RuleActionType SET_SKILL_FROM_OUTPUT_FIELD = new RuleActionType("SET_SKILL_FROM_OUTPUT_FIELD");
    public static final RuleActionType CHANGE_OVERLAP = new RuleActionType("CHANGE_OVERLAP");
    public static final RuleActionType SET_SKILL = new RuleActionType("SET_SKILL");
    public static final RuleActionType APPROVE_ALL_ASSIGNMENTS = new RuleActionType("APPROVE_ALL_ASSIGNMENTS");

    @Deprecated
    public static final RuleActionType REJECT_ALL_ASSIGNMENTS = new RuleActionType("REJECT_ALL_ASSIGNMENTS");

    private static final RuleActionType[] VALUES = {
            RESTRICTION, RESTRICTION_V2, SET_SKILL_FROM_OUTPUT_FIELD, CHANGE_OVERLAP, SET_SKILL, REJECT_ALL_ASSIGNMENTS,
            APPROVE_ALL_ASSIGNMENTS
    };
    private static final ConcurrentMap<String, RuleActionType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private RuleActionType(String name) {
        super(name);
    }

    public static RuleActionType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), RuleActionType.class);
    }

    public static RuleActionType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<RuleActionType>() {
            @Override
            public RuleActionType create(String name) {
                return new RuleActionType(name);
            }
        });
    }
}
