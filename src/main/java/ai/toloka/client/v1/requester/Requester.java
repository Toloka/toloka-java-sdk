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

package ai.toloka.client.v1.requester;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.toloka.client.v1.LangIso639;

public class Requester {

    private String id;

    private BigDecimal balance;

    @JsonProperty("public_name")
    private Map<LangIso639, String> publicName;

    private Company company;

    Requester() {}

    public String getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Map<LangIso639, String> getPublicName() {
        return publicName;
    }

    public Company getCompany() {
        return company;
    }
}
