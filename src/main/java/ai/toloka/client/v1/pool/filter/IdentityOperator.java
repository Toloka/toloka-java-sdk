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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ai.toloka.client.v1.FlexibleEnum;

public class IdentityOperator extends FlexibleEnum<IdentityOperator> {

    public static final IdentityOperator EQ = new IdentityOperator("EQ");
    public static final IdentityOperator NE = new IdentityOperator("NE");

    private static final IdentityOperator[] VALUES = {EQ, NE};
    private static final ConcurrentMap<String, IdentityOperator> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private IdentityOperator(String name) {
        super(name);
    }

    public static IdentityOperator[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), IdentityOperator.class);
    }

    public static IdentityOperator valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<IdentityOperator>() {
            @Override
            public IdentityOperator create(String name) {
                return new IdentityOperator(name);
            }
        });
    }
}
