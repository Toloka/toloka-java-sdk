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

public class UserAgentType extends FlexibleEnum<UserAgentType> {

    public static final UserAgentType BROWSER = new UserAgentType("BROWSER");
    public static final UserAgentType MOBILE_BROWSER = new UserAgentType("MOBIL_BROWSER");
    public static final UserAgentType OTHER = new UserAgentType("OTHER");

    private static final UserAgentType[] VALUES = {BROWSER, MOBILE_BROWSER, OTHER};
    private static final ConcurrentMap<String, UserAgentType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private UserAgentType(String name) {
        super(name);
    }

    public static UserAgentType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), UserAgentType.class);
    }

    public static UserAgentType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<UserAgentType>() {
            @Override
            public UserAgentType create(String name) {
                return new UserAgentType(name);
            }
        });
    }
}
