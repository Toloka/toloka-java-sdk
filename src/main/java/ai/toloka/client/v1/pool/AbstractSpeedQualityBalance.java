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

package ai.toloka.client.v1.pool;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TopPercentageSpeedQualityBalance.class, name =
                "TOP_PERCENTAGE_BY_QUALITY"),
        @JsonSubTypes.Type(value = BestConcurrentSpeedQualityBalance.class, name =
                "BEST_CONCURRENT_USERS_BY_QUALITY")
})
public abstract class AbstractSpeedQualityBalance {

    protected Type type;

    public AbstractSpeedQualityBalance(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        TOP_PERCENTAGE_BY_QUALITY,
        BEST_CONCURRENT_USERS_BY_QUALITY
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractSpeedQualityBalance that = (AbstractSpeedQualityBalance) o;

        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
