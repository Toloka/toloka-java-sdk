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

package ai.toloka.client.v1.webhooksubscription.utils

import ai.toloka.client.v1.impl.AbstractClientSpec

class SignatureValidatorSpec extends AbstractClientSpec {

    def "generateSignature"() {
        setup:
        def secretKey = "12345";
        def requestPayload = "{\"events\":[{\"event_time\":\"2000-01-01T12:00:00\",\"project_id\":\"project-1\"" +
                ",\"pool_id\":\"pool-1\",\"uuid\":\"00000000-0000-0000-0000-000000000000\",\"task_suite_id\":" +
                "\"task-suite-1\",\"assignment_id\":\"assignment-1\",\"webhook_subscription_id\":\"subscription-1\"," +
                "\"type\":\"ASSIGNMENT_APPROVED\"}]}";
        def tolokaHeaderTs = 946728000000L;
        def tolokaHeaderV = 1;

        when:
        def actual = SignatureValidator.generateSignature(secretKey, tolokaHeaderTs, tolokaHeaderV, requestPayload)

        then:
        actual == "609af3eefd4c12b6afad30ab456efcd21fe82f4247d3340151a3ca0c97a6cbcb"
    }
}
