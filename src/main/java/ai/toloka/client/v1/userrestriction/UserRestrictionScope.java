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

package ai.toloka.client.v1.userrestriction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class UserRestrictionScope extends FlexibleEnum<UserRestrictionScope> {

    private UserRestrictionScope(String name) {
        super(name);
    }

    public static final UserRestrictionScope SYSTEM = new UserRestrictionScope("SYSTEM");
    public static final UserRestrictionScope ALL_PROJECTS = new UserRestrictionScope("ALL_PROJECTS");
    public static final UserRestrictionScope PROJECT = new UserRestrictionScope("PROJECT");
    public static final UserRestrictionScope POOL = new UserRestrictionScope("POOL");

    private static final UserRestrictionScope[] VALUES = {SYSTEM, ALL_PROJECTS, PROJECT, POOL};
    private static final ConcurrentMap<String, UserRestrictionScope> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static UserRestrictionScope[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), UserRestrictionScope.class);
    }

    @JsonCreator
    public static UserRestrictionScope valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<UserRestrictionScope>() {
            @Override public UserRestrictionScope create(String name) {
                return new UserRestrictionScope(name);
            }
        });
    }

}
