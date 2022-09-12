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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

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
    public static final String AUTHORIZATION_API_KEY_HEADER_FORMAT = "ApiKey %s";
    public static final List<String> AUTHORIZATION_SUPPORTED_TYPES = List.of("ApiKey", "OAuth");

    public static final String USER_AGENT_FORMAT = "toloka-java-sdk/%s";

    public static HttpClient buildDefaultClient(String tokenOrKey) {
        return HttpClientBuilder.create()
                .setMaxConnTotal(DEFAULT_MAX_CONNECTIONS)
                .setMaxConnPerRoute(DEFAULT_MAX_CONNECTIONS)
                .setDefaultRequestConfig(getDefaultRequestConfig())
                .setUserAgent(getUserAgent())
                .setDefaultHeaders(singletonList(getDefaultAuthorizationHeader(tokenOrKey)))
                .build();
    }

    public static RequestConfig getDefaultRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
    }

    public static Header getDefaultAuthorizationHeader(String tokenOrKey) {
        return new BasicHeader(AUTHORIZATION_HEADER_NAME, recognizeAuthFormat(tokenOrKey));
    }

    public static String getUserAgent() {
        return String.format(USER_AGENT_FORMAT, EncodeUtil.encodeNonAscii(getVersion()));
    }

    /**
     * @param tokenOrKey different Authorization format. Check test for details.
     * @return Authorization header value
     */
    protected static String recognizeAuthFormat(String tokenOrKey) {
        String cred = tokenOrKey.strip();
        if (cred.contains(" ")) {
            List<String> parsedCred = Arrays.stream(tokenOrKey.split(" "))
                    .map(String::strip)
                    .filter(part -> part.length() > 0)
                    .collect(Collectors.toList());

            if (2 != parsedCred.size()) {
                throw new IllegalArgumentException(
                        "Token or Api Key must contain only itself or be prefixed with its type. "
                                + "Given value has next format: " + tokenOrKey.replaceAll("\\w", "X"));
            }

            if (!AUTHORIZATION_SUPPORTED_TYPES.contains(parsedCred.get(0))) {
                throw new IllegalArgumentException("Unsupported Authorization type");
            }

            return String.format("%s %s", parsedCred.get(0), EncodeUtil.encodeNonAscii(parsedCred.get(1)));
        }

        if (isApiKey(cred)) {
            return String.format(AUTHORIZATION_API_KEY_HEADER_FORMAT, EncodeUtil.encodeNonAscii(cred));
        }

        // fallback
        return String.format(AUTHORIZATION_OAUTH_HEADER_FORMAT, EncodeUtil.encodeNonAscii(cred));
    }

    private static boolean isApiKey(String cred) {
        long dotCount = cred.chars()
                .filter(c -> '.' == c)
                .count();

        if (1 == dotCount) {
            int dotIdx = cred.indexOf('.');

            return dotIdx > 21 && dotIdx < 25;
        }

        return false;
    }

    private static String getVersion() {
        String version = DefaultHttpClientConfiguration.class.getPackage().getImplementationVersion();
        return null == version ? "undefined" : version;
    }
}
