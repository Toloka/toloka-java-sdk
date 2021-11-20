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

package ai.toloka.client.v1.impl

import ai.toloka.client.v1.webhooksubscription.WebhookEventType
import ai.toloka.client.v1.webhooksubscription.WebhookSubscription
import ai.toloka.client.v1.webhooksubscription.WebhookSubscriptionSearchRequest
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient

import static AbstractClientSpec.parseDate
import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class WebhookSubscriptionClientImplSpec extends AbstractClientSpec {

    def "upsertWebhookSubscription"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/webhook-subscriptions/').withMethod('PUT')
                .withBody(json(new JsonBuilder(webhook_subscription_map()) as String)), once())
                .respond(response(new JsonBuilder(webhook_subscription_map_with_readonly()) as String)
                .withStatusCode(201))

        when:
        def result = factory.webhookSubscriptionClient.upsertWebhookSubscription(webhook_subscription())

        then:
        matches result, webhook_subscription_with_readonly()
        result.newCreated
    }

    def "upsertWebhookSubscription; updating existing subscription"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/webhook-subscriptions/').withMethod('PUT')
                .withBody(json(new JsonBuilder(webhook_subscription_map()) as String)), once())
                .respond(response(new JsonBuilder(webhook_subscription_map_with_readonly()) as String)
                .withStatusCode(200))

        when:
        def result = factory.webhookSubscriptionClient.upsertWebhookSubscription(webhook_subscription())

        then:
        matches result, webhook_subscription_with_readonly()
        !result.newCreated
    }

    def "upsertWebhookSubscription; with secret key"() {
        setup:
        def subscription = new WebhookSubscription(WebhookEventType.POOL_CLOSED, 'https://test.com', 'pool-1', "SecretKey")

        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/webhook-subscriptions/').withMethod('PUT')
                        .withBody(json(new JsonBuilder([
                                event_type : 'POOL_CLOSED',
                                webhook_url: 'https://test.com',
                                pool_id    : 'pool-1',
                                secret_key : 'SecretKey'
                        ]) as String)), once())
                .respond(response(new JsonBuilder([
                        event_type : 'POOL_CLOSED',
                        webhook_url: 'https://test.com',
                        pool_id    : 'pool-1',
                        id     : 'webhook-subscription-1',
                        created: '2018-12-03T15:36:00'
                ]) as String)
                        .withStatusCode(201))

        when:
        def result = factory.webhookSubscriptionClient.upsertWebhookSubscription(subscription)

        then:
        matches result, webhook_subscription_with_readonly()
        result.newCreated
    }

    def "upsertWebhookSubscriptions"() {
        setup:
        def webhook_subscriptions_map = [
                [event_type: 'ASSIGNMENT_SKIPPED', webhook_url: 'https://test.com'],
                webhook_subscription_map()
        ]
        def result_map = [
                items            : ['1': webhook_subscription_map_with_readonly()],
                validation_errors: [
                        '0': [
                                'pool_id': [
                                        code   : 'VALUE_REQUIRED',
                                        message: 'Value must be present and not equal to null'
                                ]
                        ]
                ]
        ]

        def webhook_subscriptions = [
                new WebhookSubscription(eventType: WebhookEventType.ASSIGNMENT_SKIPPED,
                        webhookUrl: 'https://test.com'),
                webhook_subscription()
        ]
        def expected_result = new ai.toloka.client.v1.BatchCreateResult(
                items: [1: webhook_subscription_with_readonly()],
                validationsErrors: [
                        0: ['pool_id': new ai.toloka.client.v1.FieldValidationError(
                                'VALUE_REQUIRED', 'Value must be present and not equal to null', null)]
                ]
        )

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/webhook-subscriptions/').withMethod('PUT')
                .withBody(json(new JsonBuilder(webhook_subscriptions_map) as String)), once()
        ).respond(response(new JsonBuilder(result_map) as String).withStatusCode(200))

        when:
        def result = factory.webhookSubscriptionClient.upsertWebhookSubscriptions(webhook_subscriptions)

        then:
        matches result, expected_result
    }

    def "findWebhookSubscriptions"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/webhook-subscriptions/')
                .withQueryStringParameters(
                pool_id: ['pool-1'],
                event_type: ['POOL_CLOSED'],
                created_gte: ['2012-01-01T12:00:00'],
                sort: ['created,-id'],
                limit: ['20']), once()
        ).respond(response(
                new JsonBuilder([items: [webhook_subscription_map_with_readonly()], has_more: false]) as String)
        )

        when:
        def request = WebhookSubscriptionSearchRequest.make()
                .filter().byPoolId('pool-1').byEventType(WebhookEventType.POOL_CLOSED).and()
                .range().byCreated(parseDate('2012-01-01 12:00:00')).gte().and()
                .sort().byCreated().asc().byId().desc().and()
                .limit(20)
                .done()
        def result = factory.webhookSubscriptionClient.findWebhookSubscriptions(request)

        then:
        matches result.items.first(), webhook_subscription_with_readonly()
    }

    def "getWebhookSubscription"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/webhook-subscriptions/webhook-subscription-1'))
                .respond(response(new JsonBuilder(webhook_subscription_map_with_readonly()) as String))

        when:
        def result = factory.webhookSubscriptionClient.getWebhookSubscription('webhook-subscription-1')

        then:
        matches result, webhook_subscription_with_readonly()
    }

    def "deleteWebhookSubscription"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/webhook-subscriptions/webhook-subscription-1d').withMethod('DELETE'))
                .respond(response().withStatusCode(204))

        when:
        factory.webhookSubscriptionClient.deleteWebhookSubscription('webhook-subscription-1d')

        then:
        noExceptionThrown()
    }

    def webhook_subscription_map() {
        [
                event_type : 'POOL_CLOSED',
                webhook_url: 'https://test.com',
                pool_id    : 'pool-1'
        ]
    }

    def webhook_subscription_map_with_readonly() {
        [
                *      : webhook_subscription_map(),
                id     : 'webhook-subscription-1',
                created: '2018-12-03T15:36:00'
        ]
    }

    def webhook_subscription() {
        new WebhookSubscription(WebhookEventType.POOL_CLOSED, 'https://test.com', 'pool-1', null)
    }

    def webhook_subscription_with_readonly() {
        webhook_subscription().with {
            id = 'webhook-subscription-1'
            created = parseDate('2018-12-03 15:36:00')
        }
    }
}
