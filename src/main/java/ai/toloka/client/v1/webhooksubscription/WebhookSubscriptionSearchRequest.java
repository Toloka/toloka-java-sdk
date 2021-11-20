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
import java.util.Map;

import ai.toloka.client.v1.SearchRequest;

public class WebhookSubscriptionSearchRequest extends SearchRequest {

    public static final String EVENT_TYPE_PARAMETER = "event_type";
    public static final String POOL_ID_PARAMETER = "pool_id";

    public static final String ID_PARAMETER = "id";
    public static final String CREATED_PARAMETER = "created";

    private WebhookSubscriptionSearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                                             String sortParameter, Integer limit) {

        super(filterParameters, rangeParameters, sortParameter, limit);
    }

    public static WebhookSubscriptionBuilder make() {
        return new WebhookSubscriptionBuilder(
                new WebhookSubscriptionFilterBuilder(), new WebhookSubscriptionRangeBuilder(),
                new WebhookSubscriptionSortBuilder());
    }

    public static class WebhookSubscriptionBuilder extends Builder<
            WebhookSubscriptionSearchRequest,
            WebhookSubscriptionBuilder,
            WebhookSubscriptionFilterBuilder,
            WebhookSubscriptionRangeBuilder,
            WebhookSubscriptionSortBuilder> {

        private WebhookSubscriptionBuilder(
                WebhookSubscriptionFilterBuilder filterBuilder,
                WebhookSubscriptionRangeBuilder rangeBuilder,
                WebhookSubscriptionSortBuilder sortBuilder
        ) {

            super(filterBuilder, rangeBuilder, sortBuilder);
        }

        @Override public WebhookSubscriptionSearchRequest done() {
            return new WebhookSubscriptionSearchRequest(filterBuilder.getFilterParameters(),
                    rangeBuilder.getRangeParameters(), sortBuilder.getSortParameter(), getLimit());
        }
    }

    public static class WebhookSubscriptionFilterBuilder
            extends FilterBuilder<WebhookSubscriptionFilterBuilder, WebhookSubscriptionBuilder,
            WebhookSubscriptionFilterParam> {

        public WebhookSubscriptionFilterBuilder byEventType(WebhookEventType eventType) {
            return by(WebhookSubscriptionFilterParam.eventType, eventType);
        }

        public WebhookSubscriptionFilterBuilder byPoolId(String poolId) {
            return by(WebhookSubscriptionFilterParam.poolId, poolId);
        }
    }

    public static class WebhookSubscriptionRangeBuilder
            extends RangeBuilder<WebhookSubscriptionRangeBuilder, WebhookSubscriptionBuilder,
            WebhookSubscriptionRangeParam> {

        public RangeItemBuilder<WebhookSubscriptionRangeBuilder> byId(String id) {
            return by(WebhookSubscriptionRangeParam.id, id);
        }

        public RangeItemBuilder<WebhookSubscriptionRangeBuilder> byCreated(Date created) {
            return by(WebhookSubscriptionRangeParam.created, created);
        }
    }

    public static class WebhookSubscriptionSortBuilder
            extends SortBuilder<WebhookSubscriptionSortBuilder, WebhookSubscriptionBuilder,
            WebhookSubscriptionSortParam> {

        public SortItem<WebhookSubscriptionSortBuilder> byId() {
            return by(WebhookSubscriptionSortParam.id);
        }

        public SortItem<WebhookSubscriptionSortBuilder> byCreated() {
            return by(WebhookSubscriptionSortParam.created);
        }
    }
}
