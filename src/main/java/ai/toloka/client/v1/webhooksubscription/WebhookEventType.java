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

package ai.toloka.client.v1.webhooksubscription;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public class WebhookEventType extends FlexibleEnum<WebhookEventType> {

    public static WebhookEventType POOL_CLOSED = new WebhookEventType("POOL_CLOSED");
    public static WebhookEventType DYNAMIC_OVERLAP_COMPLETED = new WebhookEventType("DYNAMIC_OVERLAP_COMPLETED");
    public static WebhookEventType ASSIGNMENT_CREATED = new WebhookEventType("ASSIGNMENT_CREATED");
    public static WebhookEventType ASSIGNMENT_SUBMITTED = new WebhookEventType("ASSIGNMENT_SUBMITTED");
    public static WebhookEventType ASSIGNMENT_SKIPPED = new WebhookEventType("ASSIGNMENT_SKIPPED");
    public static WebhookEventType ASSIGNMENT_EXPIRED = new WebhookEventType("ASSIGNMENT_EXPIRED");
    public static WebhookEventType ASSIGNMENT_APPROVED = new WebhookEventType("ASSIGNMENT_APPROVED");
    public static WebhookEventType ASSIGNMENT_REJECTED = new WebhookEventType("ASSIGNMENT_REJECTED");

    private static final WebhookEventType[] VALUES = {POOL_CLOSED, DYNAMIC_OVERLAP_COMPLETED, ASSIGNMENT_CREATED,
            ASSIGNMENT_SUBMITTED, ASSIGNMENT_SKIPPED, ASSIGNMENT_EXPIRED, ASSIGNMENT_APPROVED, ASSIGNMENT_REJECTED};
    private static final ConcurrentMap<String, WebhookEventType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static WebhookEventType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), WebhookEventType.class);
    }

    @JsonCreator
    public static WebhookEventType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<WebhookEventType>() {
            @Override public WebhookEventType create(String name) {
                return new WebhookEventType(name);
            }
        });
    }

    private WebhookEventType(String name) {
        super(name);
    }
}
