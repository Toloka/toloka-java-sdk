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

package ai.toloka.client.v1.pool

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import javax.annotation.Nullable

public class PoolStatusTest extends Specification {

    def ".valueOf"() {
        expect:
        PoolStatus.values().length == 4
        PoolStatus.valueOf("UNKNOWN") == PoolStatus.valueOf("UNKNOWN")
        PoolStatus.valueOf("CLOSED") == PoolStatus.CLOSED
        PoolStatus.values().length == 5
        new HashSet<>(Arrays.asList(PoolStatus.values())).contains(PoolStatus.valueOf("UNKNOWN"))
        new HashSet<>(Arrays.asList(PoolStatus.values())).contains(PoolStatus.CLOSED)
        new HashSet<>(Arrays.asList(PoolStatus.values())).contains(PoolStatus.valueOf("CLOSED"))
    }

    def ".createFromJson"() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json0 = "{}";
        String json1 = "{\"status\": null }";
        String json2 = "{\"status\": \"UNKNOWN\" }";
        String json3 = "{\"status\": \"CLOSED\" }";

        when:
        TestModel model0 = objectMapper.readValue(json0, TestModel.class);
        then:
        model0.status == null

        when:
        TestModel model1 = objectMapper.readValue(json1, TestModel.class);
        then:
        model1.status == null

        when:
        TestModel model2 = objectMapper.readValue(json2, TestModel.class);
        then:
        model2.status == PoolStatus.valueOf("UNKNOWN")

        when:
        TestModel model3 = objectMapper.readValue(json3, TestModel.class);
        then:
        model3.status == PoolStatus.CLOSED
    }

    public static final class TestModel {
        public @Nullable PoolStatus status;
    }

}
