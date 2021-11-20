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

public class UserAgentFamily extends FlexibleEnum<UserAgentFamily> {

    public static final UserAgentFamily IE = new UserAgentFamily("IE");
    public static final UserAgentFamily CHROMIUM = new UserAgentFamily("CHROMIUM");
    public static final UserAgentFamily CHROME = new UserAgentFamily("CHROME");
    public static final UserAgentFamily FIREFOX = new UserAgentFamily("FIREFOX");
    public static final UserAgentFamily SAFARI = new UserAgentFamily("SAFARI");
    public static final UserAgentFamily YANDEX_BROWSER = new UserAgentFamily("YANDEX_BROWSER");

    public static final UserAgentFamily IE_MOBILE = new UserAgentFamily("IE_MOBILE");
    public static final UserAgentFamily CHROME_MOBILE = new UserAgentFamily("CHROME_MOBILE");
    public static final UserAgentFamily MOBILE_FIREFOX = new UserAgentFamily("MOBILE_FIREFOX");
    public static final UserAgentFamily MOBILE_SAFARI = new UserAgentFamily("MOBILE_SAFARI");

    private static final UserAgentFamily[] VALUES = {
            IE, CHROMIUM, CHROME, FIREFOX, SAFARI, YANDEX_BROWSER,
            IE_MOBILE, CHROME_MOBILE, MOBILE_FIREFOX, MOBILE_SAFARI
    };
    private static final ConcurrentMap<String, UserAgentFamily> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private UserAgentFamily(String name) {
        super(name);
    }

    public static UserAgentFamily[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), UserAgentFamily.class);
    }

    public static UserAgentFamily valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<UserAgentFamily>() {
            @Override
            public UserAgentFamily create(String name) {
                return new UserAgentFamily(name);
            }
        });
    }
}
