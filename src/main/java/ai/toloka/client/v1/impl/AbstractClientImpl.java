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
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ai.toloka.client.v1.BatchCreateResult;
import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.NotFoundException;
import ai.toloka.client.v1.RequestParameters;
import ai.toloka.client.v1.SearchRequest;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.ServiceUnavailableException;
import ai.toloka.client.v1.TlkError;
import ai.toloka.client.v1.TlkException;
import ai.toloka.client.v1.TolokaRequestIOException;
import ai.toloka.client.v1.ValidationError;
import ai.toloka.client.v1.ValidationException;
import ai.toloka.client.v1.impl.transport.TransportUtil;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.pool.PoolOpenOperation;

import static ai.toloka.client.v1.impl.transport.MapperUtil.getObjectReader;
import static ai.toloka.client.v1.impl.transport.MapperUtil.getTolokaDateFormat;

public abstract class AbstractClientImpl {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClientImpl.class);

    private static final String ASYNC_MODE_PARAMETER = "async_mode";

    private static final int DEFAULT_BUFFER_SIZE = 16 * 1024;

    private final String prefix;

    private final TolokaClientFactoryImpl factory;

    private final ExecutorService executor;

    protected AbstractClientImpl(TolokaClientFactoryImpl factory) {
        this(factory, "v1");
    }

    protected AbstractClientImpl(TolokaClientFactoryImpl factory, String versionPrefix) {
        this.prefix = versionPrefix;
        this.factory = factory;

        this.executor = Executors.newCachedThreadPool();
    }

    public URI getTolokaApiUrl() {
        return factory.getTolokaApiUrl();
    }

    public HttpClient getHttpClient() {
        return factory.getHttpClient();
    }

    public Consumer<HttpRequestBase> getHttpConsumer() {
        return factory.getHeadersSupplier();
    }

    public TolokaClientFactoryImpl getFactory() {
        return factory;
    }

    private static URIBuilder addPaths(URIBuilder uriBuilder, String... paths) {
        for (String path : paths) {
            if (path == null || path.isEmpty()) {
                continue;
            }
            uriBuilder.setPath(
                    uriBuilder.getPath().endsWith("/")
                            ? uriBuilder.getPath() + path
                            : uriBuilder.getPath() + "/" + path);
        }
        return uriBuilder;
    }

    protected URIBuilder addVersionPrefix(URIBuilder uriBuilder, String... paths) {
        String[] v1Paths = new String[paths.length + 1];
        v1Paths[0] = prefix;
        System.arraycopy(paths, 0, v1Paths, 1, paths.length);

        return addPaths(uriBuilder, v1Paths);
    }

    protected <T> SearchResult<T> find(
            final SearchRequest request,
            final String path,
            final TypeReference typeReference
    ) {
        return new RequestExecutorWrapper<SearchResult<T>>() {

            @Override
            SearchResult<T> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path);
                if (request != null) {
                    uriBuilder.addParameters(convertParameters(request.getQueryParameters()));
                }

                HttpResponse response = TransportUtil
                        .executeGet(getHttpClient(), uriBuilder.build(), getHttpConsumer());

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw parseException(response);
                }

                return getObjectReader(typeReference).readValue(response.getEntity().getContent());
            }
        }.wrap();
    }

    protected <T> T get(final String id, final String path, final Class<T> clazz) {
        Assertions.checkArgNotNull(id, "Id may not be null");

        return new RequestExecutorWrapper<T>() {

            @Override
            T execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path, id).build();

                HttpResponse response = TransportUtil.executeGet(getHttpClient(), uri, getHttpConsumer());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return getObjectReader(clazz).readValue(response.getEntity().getContent());
                }

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    return null;
                }

                throw parseException(response);
            }
        }.wrap();
    }

    protected <T, R> ModificationResult<R> create(final T form,
                                                  final String path,
                                                  final Class<R> responseClass,
                                                  final Map<String, Object> queryParameters) {

        return new RequestExecutorWrapper<ModificationResult<R>>() {

            @Override
            ModificationResult<R> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path);
                if (queryParameters != null) {
                    uriBuilder.addParameters(convertParameters(queryParameters));
                }

                HttpResponse response = TransportUtil
                        .executePost(getHttpClient(), uriBuilder.build(), getHttpConsumer(), form);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                    @SuppressWarnings("unchecked")
                    ModificationResult<R> result = new ModificationResult<>(
                            getObjectReader(responseClass).readValue(response.getEntity().getContent()), true);
                    return result;
                }

                throw parseException(response);
            }
        }.wrap();
    }

    protected <T, R> BatchCreateResult<R> upsertMultiple(final List<T> forms,
                                                         final String path,
                                                         final TypeReference<BatchCreateResult<R>> typeReference) {

        return new RequestExecutorWrapper<BatchCreateResult<R>>() {

            @Override
            BatchCreateResult<R> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path);

                HttpResponse response = TransportUtil.executePut(getHttpClient(), uriBuilder.build(),
                        getHttpConsumer(), forms);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return getObjectReader(typeReference).readValue(response.getEntity().getContent());
                }

                throw parseException(response);
            }
        }.wrap();
    }

    <T> BatchCreateResult<T> createMultiple(final List<T> forms,
                                            final String path,
                                            final TypeReference<BatchCreateResult<T>> typeReference,
                                            final RequestParameters requestParameters) {

        return new RequestExecutorWrapper<BatchCreateResult<T>>() {

            @Override
            BatchCreateResult<T> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path);
                if (requestParameters != null) {
                    uriBuilder.addParameters(convertParameters(requestParameters.getQueryParameters()));
                }

                uriBuilder.addParameter(ASYNC_MODE_PARAMETER, Boolean.FALSE.toString());

                HttpResponse response = TransportUtil
                        .executePost(getHttpClient(), uriBuilder.build(), getHttpConsumer(), forms);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                    return getObjectReader(typeReference).readValue(response.getEntity().getContent());
                }

                throw parseException(response);
            }
        }.wrap();
    }

    <T, O extends Operation> O createMultipleAsync(final Iterator<T> forms,
                                                     final String path,
                                                     final Class<O> opClass,
                                                     final RequestParameters requestParameters) {

        return new RequestExecutorWrapper<O>() {

            @SuppressWarnings("unchecked")
            @Override
            O execute() throws URISyntaxException, IOException {

                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path);
                if (requestParameters != null) {
                    uriBuilder.addParameters(convertParameters(requestParameters.getQueryParameters()));
                }

                uriBuilder.addParameter(ASYNC_MODE_PARAMETER, Boolean.TRUE.toString());

                try (final PipedInputStream in = new PipedInputStream(DEFAULT_BUFFER_SIZE);
                     final PipedOutputStream out = new PipedOutputStream(in)) {

                    executor.execute(readJsonForms(out, forms));

                    HttpResponse response = TransportUtil.executePost(getHttpClient(), uriBuilder.build(),
                            getHttpConsumer(), in);

                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                        O operation = getObjectReader(opClass).readValue(response.getEntity().getContent());
                        Operation.setOperationClient(operation, getFactory().getOperationClient());
                        return operation;
                    }

                    throw parseException(response);
                }
            }
        }.wrap();
    }

    protected <T> ModificationResult<T> update(final String resourceId, final T form, final String path,
                                               final Class<T> formClass) {

        return new RequestExecutorWrapper<ModificationResult<T>>() {

            @Override
            ModificationResult<T> execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path, resourceId).build();

                HttpResponse response = TransportUtil.executePut(getHttpClient(), uri, getHttpConsumer(), form);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    @SuppressWarnings("unchecked")
                    T result = getObjectReader(formClass).readValue(response.getEntity().getContent());
                    return new ModificationResult<>(result, false);
                }

                throw parseException(response);
            }
        }.wrap();
    }

    protected <T, R> ModificationResult<R> upsert(final String resourceId, final T form, final String path,
                                                  final Class<R> formClass) {

        return new RequestExecutorWrapper<ModificationResult<R>>() {

            @Override
            ModificationResult<R> execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path, resourceId).build();

                HttpResponse response = TransportUtil.executePut(getHttpClient(), uri, getHttpConsumer(), form);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
                    R result = getObjectReader(formClass).readValue(response.getEntity().getContent());
                    return new ModificationResult<>(result, statusCode == HttpStatus.SC_CREATED);
                }

                throw parseException(response);
            }
        }.wrap();
    }

    protected <P, T> ModificationResult<T> patch(
            final String resourceId,
            final P patch,
            final String path,
            final Class<T> resourceClass,
            final Map<String, Object> queryParameters
    ) {

        return new RequestExecutorWrapper<ModificationResult<T>>() {

            @Override
            ModificationResult<T> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path, resourceId);
                if (queryParameters != null && !queryParameters.isEmpty()) {
                    uriBuilder.addParameters(convertParameters(queryParameters));
                }

                HttpResponse response = TransportUtil
                        .executePatch(getHttpClient(), uriBuilder.build(), getHttpConsumer(), patch);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    @SuppressWarnings("unchecked")
                    ModificationResult<T> result = new ModificationResult<>(
                            getObjectReader(resourceClass).readValue(response.getEntity().getContent()), false);
                    return result;
                }

                throw parseException(response);
            }
        }.wrap();
    }

    /**
     * @return operation instance on 202 code, {@code null} on 204 - means that external resource is already in desired
     * state, no operation required.
     */
    protected <T extends Operation<?, ?>> T executeAction(final String resourceId,
                                                          final String path,
                                                          final String actionPath,
                                                          final Class<T> resourceClass) {
        return new RequestExecutorWrapper<T>() {

            @Override
            T execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path, resourceId, actionPath).build();

                HttpResponse response = TransportUtil.executePost(getHttpClient(), uri, getHttpConsumer());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                    T operation = getObjectReader(resourceClass).readValue(response.getEntity().getContent());
                    PoolOpenOperation.setOperationClient(operation, getFactory().getOperationClient());
                    return operation;
                }

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                    return null;
                }

                throw parseException(response);
            }
        }.wrap();
    }

    protected <T, R> ModificationResult<R> executeSyncAction(final T form,
                                                             final String path,
                                                             final String resourceId,
                                                             final String actionPath,
                                                             final Class<R> responseClass,
                                                             final Map<String, Object> queryParameters) {

        return new RequestExecutorWrapper<ModificationResult<R>>() {

            @Override
            ModificationResult<R> execute() throws URISyntaxException, IOException {
                URIBuilder uriBuilder = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path,
                        resourceId, actionPath);
                if (queryParameters != null) {
                    uriBuilder.addParameters(convertParameters(queryParameters));
                }

                HttpResponse response = TransportUtil
                        .executePost(getHttpClient(), uriBuilder.build(), getHttpConsumer(), form);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    @SuppressWarnings("unchecked")
                    ModificationResult<R> result = new ModificationResult<>(
                            getObjectReader(responseClass).readValue(response.getEntity().getContent()), false);
                    return result;
                }

                throw parseException(response);
            }
        }.wrap();
    }

    /**
     * Like create but means that return object is operation
     *
     * @return operation instance on 202 code
     */
    protected <T extends Operation<?, ?>, O> T executeAsync(final O form,
                                                            final String path,
                                                            final Class<T> opClass) {
        return new RequestExecutorWrapper<T>() {

            @Override
            T execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path).build();

                HttpResponse response = TransportUtil.executePost(getHttpClient(), uri, getHttpConsumer(), form);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                    T operation = getObjectReader(opClass).readValue(response.getEntity().getContent());
                    PoolOpenOperation.setOperationClient(operation, getFactory().getOperationClient());
                    return operation;
                }

                throw parseException(response);
            }
        }.wrap();
    }

    void delete(final String id, final String path) {
        Assertions.checkArgNotNull(id, "Id may not be null");

        new RequestExecutorWrapper() {

            @Override
            Object execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(new URIBuilder(getTolokaApiUrl()), path, id).build();

                HttpResponse response = TransportUtil.executeDelete(getHttpClient(), uri, getHttpConsumer());

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT) {
                    throw parseException(response);
                }

                return null;
            }
        }.wrap();
    }

    TlkException parseException(HttpResponse response) throws IOException {
        InputStream responseContent = response.getEntity().getContent();

        TlkError<?> error;
        try {
            error = getObjectReader(TlkError.class).readValue(responseContent);
        } catch (JsonProcessingException ex) {
            Scanner s = new Scanner(responseContent).useDelimiter("\\A");
            error = new TlkError(TlkError.NGINX_ERROR_CODE, s.hasNext() ? s.next() : null);
        }

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 503 || statusCode == 502) {
            return new ServiceUnavailableException(error, statusCode);
        }

        return parseException(error, statusCode);
    }

    private TlkException parseException(TlkError<?> error, int statusCode) {
        if (error instanceof ValidationError) {
            return new ValidationException((ValidationError) error, statusCode);
        }

        switch (error.getCode()) {
            case TlkError.NOT_FOUND_CODE:
            case TlkError.DOES_NOT_EXIST_CODE:
                return new NotFoundException(error, statusCode);
            default:
                return new TlkException(error, statusCode);
        }
    }

    private List<NameValuePair> convertParameters(Map<String, Object> mapParameters) {
        List<NameValuePair> parameters = new ArrayList<>();
        for (Map.Entry<String, Object> mapParam : mapParameters.entrySet()) {
            if (mapParam.getValue() != null) {
                parameters.add(new BasicNameValuePair(mapParam.getKey(), valueToString(mapParam.getValue())));
            }
        }
        return parameters;
    }

    private String valueToString(Object value) {
        if (value instanceof Date) {
            return getTolokaDateFormat().format(value);
        }
        return value.toString();
    }

    private <T> Runnable readJsonForms(final PipedOutputStream out, final Iterator<T> forms) {
        return new Runnable() {
            @Override
            public void run() {
                try (final JsonGenerator generator =
                             getObjectReader().getFactory().createGenerator(out, JsonEncoding.UTF8)) {

                    generator.writeStartArray();

                    while (forms.hasNext()) {
                        generator.writeObject(forms.next());
                    }

                    generator.writeEndArray();
                } catch (Exception ex) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.addSuppressed(ex);
                        throw new RuntimeException(e);
                    }
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    protected abstract static class RequestExecutorWrapper<T> {

        T wrap() {
            try {
                return execute();
            } catch (URISyntaxException e) {
                logger.error("Unable to initialize valid URL", e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.error("Request error", e);
                throw new TolokaRequestIOException(e);
            }
        }

        abstract T execute() throws URISyntaxException, IOException;
    }
}
