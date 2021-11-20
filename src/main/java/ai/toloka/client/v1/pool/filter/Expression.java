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

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ai.toloka.client.v1.CountryIso3166;
import ai.toloka.client.v1.LangIso639;
import ai.toloka.client.v1.Region;

public abstract class Expression<K, O, V> extends Condition {

    private FilterCategory category;

    private K key;

    private O operator;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private V value;

    private Expression(FilterCategory category, K key, O operator, V value) {
        this.category = category;
        this.key = key;
        this.operator = operator;
        this.value = value;
    }

    protected Expression() {
        this.category = category;
    }

    public FilterCategory getCategory() {
        return category;
    }

    public K getKey() {
        return key;
    }

    public O getOperator() {
        return operator;
    }

    public void setOperator(O operator) {
        this.operator = operator;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key",
            visible = true, defaultImpl = RawProfileExpression.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = AdultAllowed.class, name = "adult_allowed"),
            @JsonSubTypes.Type(value = Gender.class, name = "gender"),
            @JsonSubTypes.Type(value = Country.class, name = "country"),
            @JsonSubTypes.Type(value = Citizenship.class, name = "citizenship"),
            @JsonSubTypes.Type(value = Education.class, name = "education"),
            @JsonSubTypes.Type(value = DateOfBirth.class, name = "date_of_birth"),
            @JsonSubTypes.Type(value = City.class, name = "city"),
            @JsonSubTypes.Type(value = Languages.class, name = "languages"),
            @JsonSubTypes.Type(value = Verified.class, name = "verified")
    })
    public abstract static class ProfileExpression<O, V> extends Expression<FilterCategory.ProfileKey, O, V> {

        private ProfileExpression(FilterCategory.ProfileKey key, O operator, V value) {
            super(FilterCategory.PROFILE, key, operator, value);
        }
    }

    public static class AdultAllowed extends ProfileExpression<IdentityOperator, Boolean> {

        @JsonCreator
        public AdultAllowed(@JsonProperty("operator") IdentityOperator operator, @JsonProperty("value") Boolean value) {
            super(FilterCategory.ProfileKey.ADULT_ALLOWED, operator, value);
        }
    }

    public static class Gender
            extends ProfileExpression<IdentityOperator, ai.toloka.client.v1.pool.filter.Gender> {

        @JsonCreator
        public Gender(@JsonProperty("operator") IdentityOperator operator,
                      @JsonProperty("value") ai.toloka.client.v1.pool.filter.Gender value) {

            super(FilterCategory.ProfileKey.GENDER, operator, value);
        }
    }

    public static class Country extends ProfileExpression<IdentityOperator, CountryIso3166> {

        @JsonCreator
        public Country(@JsonProperty("operator") IdentityOperator operator,
                       @JsonProperty("value") CountryIso3166 value) {

            super(FilterCategory.ProfileKey.COUNTRY, operator, value);
        }
    }

    public static class Citizenship extends ProfileExpression<IdentityOperator, CountryIso3166> {

        @JsonCreator
        public Citizenship(@JsonProperty("operator") IdentityOperator operator,
                           @JsonProperty("value") CountryIso3166 value) {

            super(FilterCategory.ProfileKey.CITIZENSHIP, operator, value);
        }
    }

    public static class Education
            extends ProfileExpression<IdentityOperator, ai.toloka.client.v1.pool.filter.Education> {

        @JsonCreator
        public Education(@JsonProperty("operator") IdentityOperator operator,
                         @JsonProperty("value") ai.toloka.client.v1.pool.filter.Education value) {

            super(FilterCategory.ProfileKey.EDUCATION, operator, value);
        }
    }

    public static class DateOfBirth extends ProfileExpression<CompareOperator, Long> {

        @JsonCreator
        public DateOfBirth(@JsonProperty("operator") CompareOperator operator, @JsonProperty("value") Long value) {
            super(FilterCategory.ProfileKey.DATE_OF_BIRTH, operator, value);
        }
    }

    public static class City extends ProfileExpression<RegionCompareOperator, Region> {

        @JsonCreator
        public City(@JsonProperty("operator") RegionCompareOperator operator, @JsonProperty("value") Region value) {
            super(FilterCategory.ProfileKey.CITY, operator, value);
        }
    }

    public static class Languages extends ProfileExpression<ArrayInclusionOperator, LangIso639> {

        @JsonCreator
        public Languages(@JsonProperty("operator") ArrayInclusionOperator operator,
                         @JsonProperty("value") LangIso639 value) {

            super(FilterCategory.ProfileKey.LANGUAGES, operator, value);
        }
    }

    public static class Verified extends ProfileExpression<IdentityOperator, Boolean> {

        @JsonCreator
        public Verified(@JsonProperty("operator") IdentityOperator operator,
                        @JsonProperty("value") Boolean value) {

            super(FilterCategory.ProfileKey.VERIFIED, operator, value);
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key",
            visible = true, defaultImpl = RawComputedExpression.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RegionByPhone.class, name = "region_by_phone"),
            @JsonSubTypes.Type(value = RegionByIp.class, name = "region_by_ip"),
            @JsonSubTypes.Type(value = DeviceCategory.class, name = "device_category"),
            @JsonSubTypes.Type(value = OsFamily.class, name = "os_family"),
            @JsonSubTypes.Type(value = OsVersion.class, name = "os_version"),
            @JsonSubTypes.Type(value = OsVersionMajor.class, name = "os_version_major"),
            @JsonSubTypes.Type(value = OsVersionMinor.class, name = "os_version_minor"),
            @JsonSubTypes.Type(value = OsVersionBugfix.class, name = "os_version_bugfix"),
            @JsonSubTypes.Type(value = UserAgentType.class, name = "user_agent_type"),
            @JsonSubTypes.Type(value = UserAgentFamily.class, name = "user_agent_family"),
            @JsonSubTypes.Type(value = UserAgentVersion.class, name = "user_agent_version"),
            @JsonSubTypes.Type(value = UserAgentVersionMajor.class, name = "user_agent_version_major"),
            @JsonSubTypes.Type(value = UserAgentVersionMinor.class, name = "user_agent_version_minor"),
            @JsonSubTypes.Type(value = UserAgentVersionBugfix.class, name = "user_agent_version_bugfix"),
            @JsonSubTypes.Type(value = Rating.class, name = "rating")
    })
    public static class ComputedExpression<O, V> extends Expression<FilterCategory.ComputedKey, O, V> {

        private ComputedExpression(FilterCategory.ComputedKey key, O operator, V value) {
            super(FilterCategory.COMPUTED, key, operator, value);
        }
    }

    public static class RegionByPhone extends ComputedExpression<RegionCompareOperator, Region> {

        @JsonCreator
        public RegionByPhone(@JsonProperty("operator") RegionCompareOperator operator,
                             @JsonProperty("value") Region value) {

            super(FilterCategory.ComputedKey.REGION_BY_PHONE, operator, value);
        }
    }

    public static class RegionByIp extends ComputedExpression<RegionCompareOperator, Region> {

        @JsonCreator
        public RegionByIp(@JsonProperty("operator") RegionCompareOperator operator,
                          @JsonProperty("value") Region value) {

            super(FilterCategory.ComputedKey.REGION_BY_IP, operator, value);
        }
    }

    public static class DeviceCategory
            extends ComputedExpression<IdentityOperator, ai.toloka.client.v1.pool.filter.DeviceCategory> {

        @JsonCreator
        public DeviceCategory(@JsonProperty("operator") IdentityOperator operator,
                              @JsonProperty("value") ai.toloka.client.v1.pool.filter.DeviceCategory value) {

            super(FilterCategory.ComputedKey.DEVICE_CATEGORY, operator, value);
        }
    }

    public static class OsFamily
            extends ComputedExpression<IdentityOperator, ai.toloka.client.v1.pool.filter.OsFamily> {

        @JsonCreator
        public OsFamily(@JsonProperty("operator") IdentityOperator operator,
                        @JsonProperty("value") ai.toloka.client.v1.pool.filter.OsFamily value) {

            super(FilterCategory.ComputedKey.OS_FAMILY, operator, value);
        }
    }

    public static class OsVersion extends ComputedExpression<CompareOperator, Double> {

        @JsonCreator
        public OsVersion(@JsonProperty("operator") CompareOperator operator, @JsonProperty("value") Double value) {
            super(FilterCategory.ComputedKey.OS_VERSION, operator, value);
        }
    }

    public static class OsVersionMajor extends ComputedExpression<CompareOperator, Integer> {

        @JsonCreator
        public OsVersionMajor(@JsonProperty("operator") CompareOperator operator,
                              @JsonProperty("value") Integer value) {

            super(FilterCategory.ComputedKey.OS_VERSION_MAJOR, operator, value);
        }
    }

    public static class OsVersionMinor extends ComputedExpression<CompareOperator, Integer> {

        @JsonCreator
        public OsVersionMinor(
                @JsonProperty("operator") CompareOperator operator,
                @JsonProperty("value") Integer value
        ) {
            super(FilterCategory.ComputedKey.OS_VERSION_MINOR, operator, value);
        }
    }

    public static class OsVersionBugfix extends ComputedExpression<CompareOperator, Integer> {

        @JsonCreator
        public OsVersionBugfix(@JsonProperty("operator") CompareOperator operator,
                               @JsonProperty("value") Integer value) {

            super(FilterCategory.ComputedKey.OS_VERSION_BUGFIX, operator, value);
        }
    }

    public static class UserAgentType
            extends ComputedExpression<IdentityOperator, ai.toloka.client.v1.pool.filter.UserAgentType> {

        @JsonCreator
        public UserAgentType(@JsonProperty("operator") IdentityOperator operator,
                             @JsonProperty("value") ai.toloka.client.v1.pool.filter.UserAgentType value) {

            super(FilterCategory.ComputedKey.USER_AGENT_TYPE, operator, value);
        }
    }

    public static class UserAgentFamily
            extends ComputedExpression<IdentityOperator, ai.toloka.client.v1.pool.filter.UserAgentFamily> {

        @JsonCreator
        public UserAgentFamily(@JsonProperty("operator") IdentityOperator operator,
                               @JsonProperty("value") ai.toloka.client.v1.pool.filter.UserAgentFamily value) {

            super(FilterCategory.ComputedKey.USER_AGENT_FAMILY, operator, value);
        }
    }

    public static class UserAgentVersion extends ComputedExpression<CompareOperator, Double> {

        @JsonCreator
        public UserAgentVersion(@JsonProperty("operator") CompareOperator operator,
                                @JsonProperty("value") Double value) {

            super(FilterCategory.ComputedKey.USER_AGENT_VERSION, operator, value);
        }
    }

    public static class UserAgentVersionMajor extends ComputedExpression<CompareOperator, Integer> {

        @JsonCreator
        public UserAgentVersionMajor(@JsonProperty("operator") CompareOperator operator,
                                     @JsonProperty("value") Integer value) {

            super(FilterCategory.ComputedKey.USER_AGENT_VERSION_MAJOR, operator, value);
        }
    }

    public static class UserAgentVersionMinor extends ComputedExpression<CompareOperator, Integer> {

        @JsonCreator
        public UserAgentVersionMinor(@JsonProperty("operator") CompareOperator operator,
                                     @JsonProperty("value") Integer value) {

            super(FilterCategory.ComputedKey.USER_AGENT_VERSION_MINOR, operator, value);
        }
    }

    public static class UserAgentVersionBugfix extends ComputedExpression<CompareOperator, Integer> {

        @JsonCreator
        public UserAgentVersionBugfix(@JsonProperty("operator") CompareOperator operator,
                                      @JsonProperty("value") Integer value) {

            super(FilterCategory.ComputedKey.USER_AGENT_VERSION_BUGFIX, operator, value);
        }
    }

    @Deprecated
    public static class Rating extends ComputedExpression<CompareOperator, Double> {

        @JsonCreator
        public Rating(@JsonProperty("operator") CompareOperator operator, @JsonProperty("value") Double value) {
            super(FilterCategory.ComputedKey.RATING, operator, value);
        }
    }

    public static class Skill extends Expression<String, CompareOperator, BigDecimal> {

        /**
         * @param fakeValue used just to prevent compilation fails on constructor overloading
         */
        @JsonCreator
        protected Skill(@JsonProperty("key") String skillId, @JsonProperty("operator") CompareOperator operator,
                        @JsonProperty(value = "___fake_value___", defaultValue = "0") Integer fakeValue,
                        @JsonProperty("value") BigDecimal exactValue) {

            super(FilterCategory.SKILL, skillId, operator, exactValue);
        }

        /**
         * See {@link Skill#valueOf(String, CompareOperator, Integer)}
         */
        @Deprecated
        public Skill(String skillId, CompareOperator operator, Integer value) {

            super(FilterCategory.SKILL, skillId, operator, value == null ? null : BigDecimal.valueOf(value));
        }

        public static Skill valueOf(String skillId, CompareOperator operator, Integer value) {
            return new Skill(skillId, operator, value);
        }

        public static Skill exactValueOf(String skillId, CompareOperator operator, BigDecimal exactValue) {
            return new Skill(skillId, operator, null, exactValue);
        }

        @JsonIgnore
        public String getSkillId() {
            return getKey();
        }
    }

    public static class RawProfileExpression extends ProfileExpression<String, Object> {

        @JsonCreator
        public RawProfileExpression(@JsonProperty("key") FilterCategory.ProfileKey key,
                                    @JsonProperty("operator") String operator,
                                    @JsonProperty("value") Object value) {

            super(key, operator, value);
        }
    }

    public static class RawComputedExpression extends ComputedExpression<String, Object> {

        @JsonCreator
        public RawComputedExpression(@JsonProperty("key") FilterCategory.ComputedKey key,
                                     @JsonProperty("operator") String operator,
                                     @JsonProperty("value") Object value) {

            super(key, operator, value);
        }
    }

    public static class RawExpression extends Expression<String, String, Object> {

        @JsonCreator
        public RawExpression(@JsonProperty("category") FilterCategory category,
                             @JsonProperty("key") String key,
                             @JsonProperty("operator") String operator,
                             @JsonProperty("value") Object value) {

            super(category, key, operator, value);
        }
    }
}
