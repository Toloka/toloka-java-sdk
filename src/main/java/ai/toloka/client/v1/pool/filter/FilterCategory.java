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

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public class FilterCategory extends FlexibleEnum<FilterCategory> {

    public static final FilterCategory PROFILE = new FilterCategory("profile");
    public static final FilterCategory COMPUTED = new FilterCategory("computed");
    public static final FilterCategory SKILL = new FilterCategory("skill");

    private static final FilterCategory[] VALUES = {PROFILE, COMPUTED, SKILL};
    private static final ConcurrentMap<String, FilterCategory> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    private FilterCategory(String name) {
        super(name);
    }

    public static FilterCategory[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), FilterCategory.class);
    }

    @JsonCreator
    public static FilterCategory valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<FilterCategory>() {
            @Override
            public FilterCategory create(String name) {
                return new FilterCategory(name);
            }
        });
    }

    public static class ProfileKey extends FlexibleEnum<ProfileKey> {

        public static final ProfileKey GENDER = new ProfileKey("gender");
        public static final ProfileKey COUNTRY = new ProfileKey("country");
        public static final ProfileKey CITIZENSHIP = new ProfileKey("citizenship");
        public static final ProfileKey EDUCATION = new ProfileKey("education");
        public static final ProfileKey ADULT_ALLOWED = new ProfileKey("adult_allowed");
        public static final ProfileKey DATE_OF_BIRTH = new ProfileKey("date_of_birth");
        public static final ProfileKey CITY = new ProfileKey("city");
        public static final ProfileKey LANGUAGES = new ProfileKey("languages");
        public static final ProfileKey VERIFIED = new ProfileKey("verified");

        private static final ProfileKey[] VALUES =
                {GENDER, COUNTRY, CITIZENSHIP, EDUCATION, ADULT_ALLOWED, DATE_OF_BIRTH, CITY, LANGUAGES, VERIFIED};
        private static final ConcurrentMap<String, ProfileKey> DISCOVERED_VALUES = new ConcurrentHashMap<>();

        private ProfileKey(String name) {
            super(name);
        }

        public static ProfileKey[] values() {
            return values(VALUES, DISCOVERED_VALUES.values(), ProfileKey.class);
        }

        @JsonCreator
        public static ProfileKey valueOf(String name) {
            return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<ProfileKey>() {
                @Override
                public ProfileKey create(String name) {
                    return new ProfileKey(name);
                }
            });
        }
    }

    public static class ComputedKey extends FlexibleEnum<ComputedKey> {

        public static final ComputedKey REGION_BY_PHONE = new ComputedKey("region_by_phone");
        public static final ComputedKey REGION_BY_IP = new ComputedKey("region_by_ip");
        public static final ComputedKey RATING = new ComputedKey("rating");
        public static final ComputedKey DEVICE_CATEGORY = new ComputedKey("device_category");
        public static final ComputedKey OS_FAMILY = new ComputedKey("os_family");
        public static final ComputedKey OS_VERSION = new ComputedKey("os_version");
        public static final ComputedKey USER_AGENT_TYPE = new ComputedKey("user_agent_type");
        public static final ComputedKey USER_AGENT_FAMILY = new ComputedKey("user_agent_family");
        public static final ComputedKey USER_AGENT_VERSION = new ComputedKey("user_agent_version");

        public static final ComputedKey OS_VERSION_MAJOR = new ComputedKey("os_version_major");
        public static final ComputedKey OS_VERSION_MINOR = new ComputedKey("os_version_minor");
        public static final ComputedKey OS_VERSION_BUGFIX = new ComputedKey("os_version_bugfix");
        public static final ComputedKey USER_AGENT_VERSION_MAJOR = new ComputedKey("user_agent_version_major");
        public static final ComputedKey USER_AGENT_VERSION_MINOR = new ComputedKey("user_agent_version_minor");
        public static final ComputedKey USER_AGENT_VERSION_BUGFIX = new ComputedKey("user_agent_version_bugfix");

        private static final ComputedKey[] VALUES = {
                REGION_BY_PHONE, REGION_BY_IP,
                RATING,
                DEVICE_CATEGORY,
                OS_FAMILY, OS_VERSION,
                USER_AGENT_TYPE, USER_AGENT_FAMILY, USER_AGENT_VERSION,
                OS_VERSION_MAJOR, OS_VERSION_MINOR, OS_VERSION_BUGFIX,
                USER_AGENT_VERSION_MAJOR, USER_AGENT_VERSION_MINOR, USER_AGENT_VERSION_BUGFIX
        };
        private static final ConcurrentMap<String, ComputedKey> DISCOVERED_VALUES = new ConcurrentHashMap<>();

        private ComputedKey(String name) {
            super(name);
        }

        public static ComputedKey[] values() {
            return values(VALUES, DISCOVERED_VALUES.values(), ComputedKey.class);
        }

        @JsonCreator
        public static ComputedKey valueOf(String name) {
            return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<ComputedKey>() {
                @Override
                public ComputedKey create(String name) {
                    return new ComputedKey(name);
                }
            });
        }
    }
}
