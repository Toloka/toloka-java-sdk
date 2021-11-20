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

package ai.toloka.client.v1.pool.qualitycontrol;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ai.toloka.client.v1.FlexibleEnum;

public class CaptchaFrequency extends FlexibleEnum<CaptchaFrequency> {

    public static final CaptchaFrequency LOW = new CaptchaFrequency("LOW");
    public static final CaptchaFrequency MEDIUM = new CaptchaFrequency("MEDIUM");
    public static final CaptchaFrequency HIGH = new CaptchaFrequency("HIGH");

    private static final CaptchaFrequency[] VALUES = {LOW, MEDIUM, HIGH};
    private static final ConcurrentMap<String, CaptchaFrequency> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static CaptchaFrequency[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), CaptchaFrequency.class);
    }

    public static CaptchaFrequency valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<CaptchaFrequency>() {
            @Override
            public CaptchaFrequency create(String name) {
                return new CaptchaFrequency(name);
            }
        });
    }

    private CaptchaFrequency(String name) {
        super(name);
    }
}
