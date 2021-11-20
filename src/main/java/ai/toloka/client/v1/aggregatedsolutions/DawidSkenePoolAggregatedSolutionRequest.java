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

package ai.toloka.client.v1.aggregatedsolutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DawidSkenePoolAggregatedSolutionRequest extends PoolAggregatedSolutionRequest {

    private List<Field> fields;

    public DawidSkenePoolAggregatedSolutionRequest(String poolId, List<Field> fields) {
        super(AggregatedSolutionType.DAWID_SKENE, poolId);
        this.fields = new ArrayList<>(fields);
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public static class Field {

        private String name;

        public Field(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
