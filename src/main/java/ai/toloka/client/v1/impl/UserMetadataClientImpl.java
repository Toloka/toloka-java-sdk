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

import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.metadata.UserMetadata;
import ai.toloka.client.v1.metadata.UserMetadataClient;

public class UserMetadataClientImpl extends AbstractClientImpl implements UserMetadataClient {

    private static final String USER_METADATA_PATH = "user-metadata";

    UserMetadataClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public UserMetadata getUserMetadata(String userId) {
        Assertions.checkArgNotNull(userId, "Id may not be null");

        return get(userId, USER_METADATA_PATH, UserMetadata.class);
    }
}
