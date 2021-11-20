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

public class CollectorConfigType extends FlexibleEnum<CollectorConfigType> {

    public static final CollectorConfigType GOLDEN_SET = new CollectorConfigType("GOLDEN_SET");
    public static final CollectorConfigType MAJORITY_VOTE = new CollectorConfigType("MAJORITY_VOTE");
    public static final CollectorConfigType CAPTCHA = new CollectorConfigType("CAPTCHA");
    public static final CollectorConfigType INCOME = new CollectorConfigType("INCOME");
    public static final CollectorConfigType SKIPPED_IN_ROW_ASSIGNMENTS =
            new CollectorConfigType("SKIPPED_IN_ROW_ASSIGNMENTS");
    public static final CollectorConfigType ANSWER_COUNT = new CollectorConfigType("ANSWER_COUNT");
    public static final CollectorConfigType ASSIGNMENT_SUBMIT_TIME = new CollectorConfigType("ASSIGNMENT_SUBMIT_TIME");
    public static final CollectorConfigType ACCEPTANCE_RATE = new CollectorConfigType("ACCEPTANCE_RATE");
    public static final CollectorConfigType ASSIGNMENTS_ASSESSMENT = new CollectorConfigType("ASSIGNMENTS_ASSESSMENT");
    public static final CollectorConfigType USERS_ASSESSMENT = new CollectorConfigType("USERS_ASSESSMENT");
    public static final CollectorConfigType TRAINING = new CollectorConfigType("TRAINING");

    private static final CollectorConfigType[] VALUES = {
            GOLDEN_SET, MAJORITY_VOTE, CAPTCHA, INCOME, SKIPPED_IN_ROW_ASSIGNMENTS, ANSWER_COUNT,
            ASSIGNMENT_SUBMIT_TIME, ACCEPTANCE_RATE, ASSIGNMENTS_ASSESSMENT, USERS_ASSESSMENT, TRAINING
    };
    private static final ConcurrentMap<String, CollectorConfigType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static CollectorConfigType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), CollectorConfigType.class);
    }

    public static CollectorConfigType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<CollectorConfigType>() {
            @Override public CollectorConfigType create(String name) {
                return new CollectorConfigType(name);
            }
        });
    }

    private CollectorConfigType(String name) {
        super(name);
    }
}
