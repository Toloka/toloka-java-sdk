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

public class InterlocutorRole extends FlexibleEnum<InterlocutorRole> {

    public static InterlocutorRole USER = new InterlocutorRole("USER");
    public static InterlocutorRole REQUESTER = new InterlocutorRole("REQUESTER");
    public static InterlocutorRole ADMINISTRATOR = new InterlocutorRole("ADMINISTRATOR");
    public static InterlocutorRole SYSTEM = new InterlocutorRole("SYSTEM");

    private static final InterlocutorRole[] VALUES = {USER, REQUESTER, ADMINISTRATOR, SYSTEM};
    private static final ConcurrentHashMap<String, InterlocutorRole> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static InterlocutorRole[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), InterlocutorRole.class);
    }

    @JsonCreator
    public static InterlocutorRole valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<InterlocutorRole>() {
            @Override public InterlocutorRole create(String name) {
                return new InterlocutorRole(name);
            }
        });
    }

    private InterlocutorRole(String name) {
        super(name);
    }
}
