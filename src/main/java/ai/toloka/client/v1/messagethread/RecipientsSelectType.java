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

import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class RecipientsSelectType extends FlexibleEnum<RecipientsSelectType> {

    public static RecipientsSelectType DIRECT = new RecipientsSelectType("DIRECT");
    public static RecipientsSelectType FILTER = new RecipientsSelectType("FILTER");
    public static RecipientsSelectType ALL = new RecipientsSelectType("ALL");

    private static final RecipientsSelectType[] VALUES = {DIRECT, FILTER, ALL};
    private static final ConcurrentHashMap<String, RecipientsSelectType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static RecipientsSelectType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), RecipientsSelectType.class);
    }

    @JsonCreator
    public static RecipientsSelectType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<RecipientsSelectType>() {
            @Override public RecipientsSelectType create(String name) {
                return new RecipientsSelectType(name);
            }
        });
    }

    private RecipientsSelectType(String name) {
        super(name);
    }
}
