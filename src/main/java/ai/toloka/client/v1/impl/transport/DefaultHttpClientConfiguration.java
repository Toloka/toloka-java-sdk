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

package ai.toloka.client.v1.impl.transport;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import static java.util.Collections.singletonList;

public class DefaultHttpClientConfiguration {

    public static final URI DEFAULT_TOLOKA_PROD_URI = URI.create("https://toloka.yandex.com/api");
    public static final URI DEFAULT_TOLOKA_SANDBOX_URI = URI.create("https://sandbox.toloka.yandex.com/api");

    public static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 120 * 1000;
    public static final int DEFAULT_MAX_CONNECTIONS = 100;

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String AUTHORIZATION_OAUTH_HEADER_FORMAT = "OAuth %s";

    public static final String USER_AGENT_FORMAT = "toloka-java-sdk/%s";

    public static HttpClient buildDefaultClient(String oauthToken) {
        return HttpClientBuilder.create()
                .setMaxConnTotal(DEFAULT_MAX_CONNECTIONS)
                .setMaxConnPerRoute(DEFAULT_MAX_CONNECTIONS)
                .setDefaultRequestConfig(getDefaultRequestConfig())
                .setUserAgent(getUserAgent())
                .setDefaultHeaders(singletonList(getDefaultAuthorizationHeader(oauthToken)))
                .build();
    }

    public static RequestConfig getDefaultRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
    }

    public static Header getDefaultAuthorizationHeader(String oauthToken) {
        return new BasicHeader(AUTHORIZATION_HEADER_NAME, String.format(AUTHORIZATION_OAUTH_HEADER_FORMAT,
                EncodeUtil.encodeNonAscii(oauthToken)));
    }

    public static String getUserAgent() {
        return String.format(USER_AGENT_FORMAT, EncodeUtil.encodeNonAscii(getVersion()));
    }

    private static String getVersion() {
        String version = DefaultHttpClientConfiguration.class.getPackage().getImplementationVersion();
        return null == version ? "undefined" : version;
    }
}
