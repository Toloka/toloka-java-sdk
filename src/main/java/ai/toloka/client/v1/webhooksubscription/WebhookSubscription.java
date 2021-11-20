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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookSubscription {

    private String id;

    private Date created;

    @JsonProperty("event_type")
    private WebhookEventType eventType;

    @JsonProperty("webhook_url")
    private String webhookUrl;

    @JsonProperty("pool_id")
    private String poolId;

    @JsonProperty("secret_key")
    private String secretKey;

    public WebhookSubscription() {}

    @JsonCreator
    public WebhookSubscription(@JsonProperty("event_type") WebhookEventType eventType,
                               @JsonProperty("webhook_url") String webhookUrl,
                               @JsonProperty("pool_id") String poolId,
                               @JsonProperty("secret_key") String secretKey) {
        this.eventType = eventType;
        this.webhookUrl = webhookUrl;
        this.poolId = poolId;
        this.secretKey = secretKey;
    }

    public WebhookSubscription(@JsonProperty("event_type") WebhookEventType eventType,
                               @JsonProperty("webhook_url") String webhookUrl,
                               @JsonProperty("pool_id") String poolId) {
        this(eventType, webhookUrl, poolId, null);
    }

    public String getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public WebhookEventType getEventType() {
        return eventType;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public String getPoolId() {
        return poolId;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
