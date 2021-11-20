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

public class RuleConditionKey extends FlexibleEnum<RuleConditionKey> {

    public static final RuleConditionKey TOTAL_ANSWERS_COUNT = new RuleConditionKey("total_answers_count");
    public static final RuleConditionKey CORRECT_ANSWERS_RATE = new RuleConditionKey("correct_answers_rate");
    public static final RuleConditionKey INCORRECT_ANSWERS_RATE = new RuleConditionKey("incorrect_answers_rate");
    public static final RuleConditionKey GOLDEN_SET_ANSWERS_COUNT = new RuleConditionKey("golden_set_answers_count");
    public static final RuleConditionKey GOLDEN_SET_CORRECT_ANSWERS_RATE =
            new RuleConditionKey("golden_set_correct_answers_rate");
    public static final RuleConditionKey GOLDEN_SET_INCORRECT_ANSWERS_RATE =
            new RuleConditionKey("golden_set_incorrect_answers_rate");
    public static final RuleConditionKey STORED_RESULTS_COUNT = new RuleConditionKey("stored_results_count");
    public static final RuleConditionKey SUCCESS_RATE = new RuleConditionKey("success_rate");
    public static final RuleConditionKey FAIL_RATE = new RuleConditionKey("fail_rate");
    public static final RuleConditionKey INCOME_SUM_FOR_LAST_24_HOURS =
            new RuleConditionKey("income_sum_for_last_24_hours");
    public static final RuleConditionKey SKIPPED_IN_ROW_COUNT = new RuleConditionKey("skipped_in_row_count");
    public static final RuleConditionKey ASSIGNMENTS_ACCEPTED_COUNT =
            new RuleConditionKey("assignments_accepted_count");
    public static final RuleConditionKey TOTAL_SUBMITTED_COUNT = new RuleConditionKey("total_submitted_count");
    public static final RuleConditionKey FAST_SUBMITTED_COUNT = new RuleConditionKey("fast_submitted_count");
    public static final RuleConditionKey TOTAL_ASSIGNMENTS_COUNT = new RuleConditionKey("total_assignments_count");
    public static final RuleConditionKey ACCEPTED_ASSIGNMENTS_RATE = new RuleConditionKey("accepted_assignments_rate");
    public static final RuleConditionKey REJECTED_ASSIGNMENTS_RATE = new RuleConditionKey("rejected_assignments_rate");
    public static final RuleConditionKey PENDING_ASSIGNMENTS_COUNT = new RuleConditionKey("pending_assignments_count");
    public static final RuleConditionKey ACCEPTED_ASSIGNMENTS_COUNT =
            new RuleConditionKey("accepted_assignments_count");
    public static final RuleConditionKey REJECTED_ASSIGNMENTS_COUNT =
            new RuleConditionKey("rejected_assignments_count");
    public static final RuleConditionKey ASSESSMENT_EVENT = new RuleConditionKey("assessment_event");
    public static final RuleConditionKey POOL_ACCESS_REVOKED_REASON =
            new RuleConditionKey("pool_access_revoked_reason");
    public static final RuleConditionKey SKILL_ID = new RuleConditionKey("skill_id");
    public static final RuleConditionKey SUBMITTED_ASSIGNMENTS_COUNT =
            new RuleConditionKey("submitted_assignments_count");
    public static final RuleConditionKey NEXT_ASSIGNMENT_AVAILABLE = new RuleConditionKey("next_assignment_available");

    private static final RuleConditionKey[] VALUES = {
            TOTAL_ANSWERS_COUNT, CORRECT_ANSWERS_RATE, INCORRECT_ANSWERS_RATE,
            GOLDEN_SET_ANSWERS_COUNT, GOLDEN_SET_CORRECT_ANSWERS_RATE, GOLDEN_SET_INCORRECT_ANSWERS_RATE,
            STORED_RESULTS_COUNT, SUCCESS_RATE, FAIL_RATE,
            INCOME_SUM_FOR_LAST_24_HOURS,
            SKIPPED_IN_ROW_COUNT,
            ASSIGNMENTS_ACCEPTED_COUNT,
            TOTAL_SUBMITTED_COUNT, FAST_SUBMITTED_COUNT,
            TOTAL_ASSIGNMENTS_COUNT, ACCEPTED_ASSIGNMENTS_RATE, REJECTED_ASSIGNMENTS_RATE,
            PENDING_ASSIGNMENTS_COUNT, ACCEPTED_ASSIGNMENTS_COUNT, REJECTED_ASSIGNMENTS_COUNT, ASSESSMENT_EVENT,
            POOL_ACCESS_REVOKED_REASON, SKILL_ID,
            SUBMITTED_ASSIGNMENTS_COUNT, NEXT_ASSIGNMENT_AVAILABLE
    };
    private static final ConcurrentMap<String, RuleConditionKey> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static RuleConditionKey[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), RuleConditionKey.class);
    }

    public static RuleConditionKey valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<RuleConditionKey>() {
            @Override public RuleConditionKey create(String name) {
                return new RuleConditionKey(name);
            }
        });
    }

    private RuleConditionKey(String name) {
        super(name);
    }
}
