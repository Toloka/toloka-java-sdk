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

package ai.toloka.client.v1.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.transport.MapperUtil;
import ai.toloka.client.v1.impl.transport.TransportUtil;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.userrestriction.UserRestriction;
import ai.toloka.client.v1.userrestriction.UserRestrictionClient;
import ai.toloka.client.v1.userrestriction.UserRestrictionSearchRequest;

public class UserRestrictionClientImpl extends AbstractClientImpl implements UserRestrictionClient {

    private static final String USER_RESTRICTIONS_PATH = "user-restrictions";

    UserRestrictionClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<UserRestriction> findUserRestrictions(final UserRestrictionSearchRequest request) {
        return find(request, USER_RESTRICTIONS_PATH, new TypeReference<SearchResult<UserRestriction>>() {});
    }

    @Override
    public UserRestriction getUserRestriction(final String userRestrictionId) {
        return get(userRestrictionId, USER_RESTRICTIONS_PATH, UserRestriction.class);
    }

    @Override
    public ModificationResult<UserRestriction> setUserRestriction(final UserRestriction userRestriction) {
        Assertions.checkArgNotNull(userRestriction, "User restriction may not be null");

        return new RequestExecutorWrapper<ModificationResult<UserRestriction>>() {

            @Override
            ModificationResult<UserRestriction> execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), USER_RESTRICTIONS_PATH).build();

                HttpResponse response = TransportUtil
                        .executePut(getHttpClient(), uri, getHttpConsumer(), userRestriction);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
                        || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {

                    return new ModificationResult<>(
                            MapperUtil.getObjectReader(UserRestriction.class)
                                    .readValue(response.getEntity().getContent()),
                            response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED);
                }

                throw parseException(response);
            }
        }.wrap();
    }

    @Override
    public void deleteUserRestriction(final String userRestrictionId) {
        delete(userRestrictionId, USER_RESTRICTIONS_PATH);
    }
}
