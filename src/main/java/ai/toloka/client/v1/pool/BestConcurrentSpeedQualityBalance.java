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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BestConcurrentSpeedQualityBalance extends AbstractSpeedQualityBalance {

    @JsonProperty("count")
    private Long count;

    public BestConcurrentSpeedQualityBalance(@JsonProperty("count") Long count) {
        super(Type.BEST_CONCURRENT_USERS_BY_QUALITY);
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        BestConcurrentSpeedQualityBalance that = (BestConcurrentSpeedQualityBalance) o;

        return Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return count.hashCode();
    }

    @Override
    public String toString() {
        return "BestConcurrentUsersCherryPickingConfig{"
                + "type=" + type
                + ", count=" + count
                + '}';
    }
}
