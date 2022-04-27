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

package ai.toloka.client.v1.metadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserMetadataAttributes {

    @JsonProperty("country_by_phone")
    private String countryByPhone;

    @JsonProperty("country_by_ip")
    private String countryByIp;

    @JsonProperty("client_type")
    private String clientType;

    @JsonProperty("user_agent_type")
    private String userAgentType;

    @JsonProperty("device_category")
    private String deviceCategory;

    @JsonProperty("os_family")
    private String osFamily;

    @JsonProperty("os_version")
    private Double osVersion;

    @JsonProperty("os_version_major")
    private Long osVersionMajor;

    @JsonProperty("os_version_minor")
    private Long osVersionMinor;

    @JsonProperty("os_version_bugfix")
    private Long osVersionBugfix;

    @JsonCreator
    public UserMetadataAttributes(@JsonProperty("country_by_phone") String countryByPhone,
                                  @JsonProperty("country_by_ip") String countryByIp,
                                  @JsonProperty("client_type") String clientType,
                                  @JsonProperty("user_agent_type") String userAgentType,
                                  @JsonProperty("device_category") String deviceCategory,
                                  @JsonProperty("os_family") String osFamily,
                                  @JsonProperty("os_version") Double osVersion,
                                  @JsonProperty("os_version_major") Long osVersionMajor,
                                  @JsonProperty("os_version_minor") Long osVersionMinor,
                                  @JsonProperty("os_version_bugfix") Long osVersionBugfix) {
        this.countryByPhone = countryByPhone;
        this.countryByIp = countryByIp;
        this.clientType = clientType;
        this.userAgentType = userAgentType;
        this.deviceCategory = deviceCategory;
        this.osFamily = osFamily;
        this.osVersion = osVersion;
        this.osVersionMajor = osVersionMajor;
        this.osVersionMinor = osVersionMinor;
        this.osVersionBugfix = osVersionBugfix;
    }

    public String getCountryByPhone() {
        return countryByPhone;
    }

    public void setCountryByPhone(String countryByPhone) {
        this.countryByPhone = countryByPhone;
    }

    public String getUserAgentType() {
        return userAgentType;
    }

    public void setUserAgentType(String userAgentType) {
        this.userAgentType = userAgentType;
    }

    public Long getOsVersionMinor() {
        return osVersionMinor;
    }

    public void setOsVersionMinor(Long osVersionMinor) {
        this.osVersionMinor = osVersionMinor;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Double getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(Double osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public Long getOsVersionMajor() {
        return osVersionMajor;
    }

    public void setOsVersionMajor(Long osVersionMajor) {
        this.osVersionMajor = osVersionMajor;
    }

    public Long getOsVersionBugfix() {
        return osVersionBugfix;
    }

    public void setOsVersionBugfix(Long osVersionBugfix) {
        this.osVersionBugfix = osVersionBugfix;
    }

    public String getCountryByIp() {
        return countryByIp;
    }

    public void setCountryByIp(String countryByIp) {
        this.countryByIp = countryByIp;
    }
}
