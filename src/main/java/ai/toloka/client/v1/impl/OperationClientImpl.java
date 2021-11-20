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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;

import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.transport.TransportUtil;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.operation.OperationClient;
import ai.toloka.client.v1.operation.OperationLogItem;
import ai.toloka.client.v1.operation.OperationSearchRequest;

import static ai.toloka.client.v1.impl.transport.MapperUtil.getObjectReader;

public class OperationClientImpl extends AbstractClientImpl implements OperationClient {

    private static final String OPERATIONS_PATH = "operations";
    private static final String LOG_PATH = "log";

    OperationClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<Operation<?, ?>> findOperations(OperationSearchRequest request) {
        SearchResult<Operation<?, ?>> result =
                find(request, OPERATIONS_PATH, new TypeReference<SearchResult<Operation>>() {});
        for (Operation operation : result.getItems()) {
            Operation.setOperationClient(operation, this);
        }
        return result;
    }

    @Override
    public <T extends Operation<?, ?>> T getOperation(final String operationId) {
        Assertions.checkArgNotNull(operationId, "Id may not be null");

        if (Operation.PSEUDO_OPERATION_ID.equals(operationId)) {
            @SuppressWarnings("unchecked")
            T pseudo = (T) Operation.createPseudo(new Date());
            return pseudo;
        }

        return new RequestExecutorWrapper<T>() {

            @SuppressWarnings("unchecked")
            @Override
            T execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), OPERATIONS_PATH, operationId).build();

                HttpResponse response = TransportUtil.executeGet(getHttpClient(), uri, getHttpConsumer());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    T result = getObjectReader(Operation.class).readValue(response.getEntity().getContent());
                    Operation.setOperationClient(result, OperationClientImpl.this);
                    return result;
                }

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    return null;
                }

                throw parseException(response);
            }
        }.wrap();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Operation<?, ?>> T getOperation(String operationId, Class<T> c) {
        Assertions.checkArgNotNull(c, "Requested class may not be null");

        Operation<?, ?> operation = getOperation(operationId);

        if (operation == null) {
            return null;
        }

        if (!operation.getClass().equals(c)) {
            throw new IllegalArgumentException("Requested class and actual operation type doesn't match");
        }

        return (T) operation;
    }

    @Override
    public Iterator<OperationLogItem> getOperationLog(final String operationId) {
        Assertions.checkArgNotNull(operationId, "Id may not be null");

        if (Operation.PSEUDO_OPERATION_ID.equals(operationId)) {
            return Collections.emptyIterator();
        }

        return new RequestExecutorWrapper<Iterator<OperationLogItem>>() {

            @Override
            Iterator<OperationLogItem> execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), OPERATIONS_PATH, operationId, LOG_PATH)
                        .build();

                HttpResponse response = TransportUtil.executeGet(getHttpClient(), uri, getHttpConsumer());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    JsonParser parser = getObjectReader().getFactory().createParser(response.getEntity().getContent());

                    parser.nextToken(); // read start of array

                    JsonToken firstArrayToken = parser.nextToken();
                    if (firstArrayToken == JsonToken.END_ARRAY) {
                        return Collections.emptyIterator();
                    }

                    return parser.readValuesAs(OperationLogItem.class);
                }

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    return null;
                }

                throw parseException(response);
            }
        }.wrap();
    }
}
