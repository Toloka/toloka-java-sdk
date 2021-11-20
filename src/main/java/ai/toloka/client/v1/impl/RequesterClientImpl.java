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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;

import ai.toloka.client.v1.impl.transport.MapperUtil;
import ai.toloka.client.v1.impl.transport.TransportUtil;
import ai.toloka.client.v1.requester.Requester;
import ai.toloka.client.v1.requester.RequesterClient;

public class RequesterClientImpl extends AbstractClientImpl implements RequesterClient {

    private static final String REQUESTER_PATH = "requester";

    RequesterClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public Requester getRequester() {
        return new RequestExecutorWrapper<Requester>() {

            @Override
            Requester execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), REQUESTER_PATH).build();

                HttpResponse response = TransportUtil.executeGet(getHttpClient(), uri, getHttpConsumer());

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw parseException(response);
                }

                return MapperUtil.getObjectReader(Requester.class).readValue(response.getEntity().getContent());
            }
        }.wrap();
    }
}
