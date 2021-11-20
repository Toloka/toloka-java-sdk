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

package ai.toloka.client.v1.messagethread;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ai.toloka.client.v1.pool.filter.Condition;
import ai.toloka.client.v1.pool.filter.Connective;

public class ComposeDetails {

    @JsonProperty("recipients_select_type")
    private RecipientsSelectType recipientsSelectType;

    @JsonProperty("recipients_ids")
    private List<String> recipientsIds;

    @JsonProperty("recipients_filter")
    @JsonDeserialize(using = Condition.ConditionDeserializer.class)
    private Connective recipientsFilter;

    public RecipientsSelectType getRecipientsSelectType() {
        return recipientsSelectType;
    }

    public List<String> getRecipientsIds() {
        return recipientsIds;
    }

    public Connective getRecipientsFilter() {
        return recipientsFilter;
    }
}
