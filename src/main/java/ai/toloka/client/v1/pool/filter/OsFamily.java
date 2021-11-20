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

public class OsFamily extends FlexibleEnum<OsFamily> {

    public static final OsFamily WINDOWS = new OsFamily("WINDOWS");
    public static final OsFamily OS_X = new OsFamily("OS_X");
    public static final OsFamily MAC_OS = new OsFamily("MAC_OS");
    public static final OsFamily LINUX = new OsFamily("LINUX");
    public static final OsFamily BSD = new OsFamily("BSD");
    public static final OsFamily ANDROID = new OsFamily("ANDROID");
    public static final OsFamily IOS = new OsFamily("IOS");
    public static final OsFamily BLACKBERRY = new OsFamily("BLACKBERRY");

    private static final OsFamily[] VALUES = {WINDOWS, OS_X, MAC_OS, LINUX, BSD, ANDROID, IOS, BLACKBERRY};
    private static final ConcurrentMap<String, OsFamily> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private OsFamily(String name) {
        super(name);
    }

    public static OsFamily[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), OsFamily.class);
    }

    public static OsFamily valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<OsFamily>() {
            @Override
            public OsFamily create(String name) {
                return new OsFamily(name);
            }
        });
    }
}
