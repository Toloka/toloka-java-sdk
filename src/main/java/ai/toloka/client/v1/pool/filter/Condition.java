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

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ai.toloka.client.v1.impl.transport.MapperUtil;

public abstract class Condition {

    public static class ConditionDeserializer extends JsonDeserializer<Condition> {

        @Override
        public Condition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

            if (p.getCurrentToken() == JsonToken.START_OBJECT) {
                p.nextToken();
            }

            TreeNode node = p.readValueAsTree();

            if (node instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) node;

                if (objectNode.has("and")) {
                    return MapperUtil.getObjectReader(Connective.And.class).readValue(objectNode);
                } else if (objectNode.has("or")) {
                    return MapperUtil.getObjectReader(Connective.Or.class).readValue(objectNode);
                } else {
                    if (objectNode.has("category") && objectNode.get("category").isTextual()) {
                        String category = objectNode.get("category").asText();
                        switch (category) {
                            case "profile":
                                return MapperUtil.getObjectReader(Expression.ProfileExpression.class)
                                        .readValue(objectNode);
                            case "computed":
                                return MapperUtil.getObjectReader(Expression.ComputedExpression.class)
                                        .readValue(objectNode);
                            case "skill":
                                return MapperUtil.getObjectReader(Expression.Skill.class).readValue(objectNode);
                            default:
                        }
                    }
                    return MapperUtil.getObjectReader(Expression.RawExpression.class).readValue(objectNode);
                }
            }

            return MapperUtil.getObjectReader(RawCondition.class).readValue((JsonNode) node);
        }
    }

    public static class RawCondition {

        private final Map<String, Object> content;

        @JsonCreator
        RawCondition(Map<String, Object> content) {
            this.content = content;
        }

        @JsonValue
        public Map<String, Object> getContent() {
            return content;
        }
    }
}
