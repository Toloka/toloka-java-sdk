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

package ai.toloka.client.v1.impl;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.BatchCreateResult;
import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.webhooksubscription.WebhookPushResult;
import ai.toloka.client.v1.webhooksubscription.WebhookSubscription;
import ai.toloka.client.v1.webhooksubscription.WebhookSubscriptionClient;
import ai.toloka.client.v1.webhooksubscription.WebhookSubscriptionSearchRequest;

public class WebhookSubscriptionClientImpl extends AbstractClientImpl implements WebhookSubscriptionClient {

    private static final String WEBHOOK_SUBSCRIPTIONS_PATH = "webhook-subscriptions/";
    private static final String TEST_WEBHOOK_PATH = "test";

    WebhookSubscriptionClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public ModificationResult<WebhookSubscription> upsertWebhookSubscription(WebhookSubscription webhookSubscription) {

        Assertions.checkArgNotNull(webhookSubscription, "Form may not be null");

        return upsert(null, webhookSubscription, WEBHOOK_SUBSCRIPTIONS_PATH, WebhookSubscription.class);
    }

    @Override
    public BatchCreateResult<WebhookSubscription> upsertWebhookSubscriptions(List<WebhookSubscription> subscriptions) {

        Assertions.checkArgNotNull(subscriptions, "Forms may not be null");

        return upsertMultiple(subscriptions, WEBHOOK_SUBSCRIPTIONS_PATH,
                new TypeReference<BatchCreateResult<WebhookSubscription>>() {});
    }

    @Override
    public SearchResult<WebhookSubscription> findWebhookSubscriptions(WebhookSubscriptionSearchRequest request) {
        return find(request, WEBHOOK_SUBSCRIPTIONS_PATH, new TypeReference<SearchResult<WebhookSubscription>>() {});
    }

    @Override
    public WebhookSubscription getWebhookSubscription(String webhookSubscriptionId) {

        Assertions.checkArgNotNull(webhookSubscriptionId, "Id may not be null");

        return get(webhookSubscriptionId, WEBHOOK_SUBSCRIPTIONS_PATH, WebhookSubscription.class);
    }

    @Override
    public void deleteWebhookSubscription(String webhookSubscriptionId) {
        delete(webhookSubscriptionId, WEBHOOK_SUBSCRIPTIONS_PATH);
    }

    @Override
    public ModificationResult<WebhookPushResult> sendTestWebhook(String webhookSubscriptionId) {
        Assertions.checkArgNotNull(webhookSubscriptionId, "Id may not be null");

        return executeSyncAction(null, WEBHOOK_SUBSCRIPTIONS_PATH, webhookSubscriptionId,
                TEST_WEBHOOK_PATH, WebhookPushResult.class, null);
    }
}
