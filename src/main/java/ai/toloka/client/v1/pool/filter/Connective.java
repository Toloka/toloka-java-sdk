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

package ai.toloka.client.v1.pool.filter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class Connective extends Condition {

    public static class And extends Connective {

        @JsonProperty("and")
        private List<Condition> conditions;

        @JsonCreator
        public And(
                @JsonProperty("and")
                @JsonDeserialize(contentUsing = Condition.ConditionDeserializer.class) List<Condition> conditions
        ) {
            this.conditions = conditions;
        }

        public List<Condition> getConditions() {
            return conditions;
        }

        public void setConditions(List<Condition> conditions) {
            this.conditions = conditions;
        }
    }

    public static class Or extends Connective {

        @JsonProperty("or")
        private List<Condition> conditions;

        @JsonCreator
        public Or(
                @JsonProperty("or")
                @JsonDeserialize(contentUsing = Condition.ConditionDeserializer.class) List<Condition> conditions
        ) {
            this.conditions = conditions;
        }

        public List<Condition> getConditions() {
            return conditions;
        }

        public void setConditions(List<Condition> conditions) {
            this.conditions = conditions;
        }
    }
}
