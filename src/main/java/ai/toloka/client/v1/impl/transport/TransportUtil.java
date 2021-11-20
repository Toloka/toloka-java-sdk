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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;

public class TransportUtil {

    public static HttpResponse executeGet(
            HttpClient client,
            URI uri,
            Consumer<HttpRequestBase> consumer
    ) throws IOException {
        HttpGet get = new HttpGet(uri);
        applyConsumer(get, consumer);
        return client.execute(get);
    }

    public static HttpResponse executePost(
            HttpClient client,
            URI uri,
            Consumer<HttpRequestBase> consumer
    ) throws IOException {
        HttpPost post = new HttpPost(uri);
        post.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        applyConsumer(post, consumer);
        return client.execute(post);
    }

    public static HttpResponse executePost(
            HttpClient client,
            URI uri,
            Consumer<HttpRequestBase> consumer,
            Object body
    ) throws IOException {
        HttpPost post = new HttpPost(uri);
        applyConsumer(post, consumer);
        post.setEntity(new StringEntity(MapperUtil.getObjectWriter().writeValueAsString(body),
                ContentType.APPLICATION_JSON));
        return client.execute(post);
    }

    public static HttpResponse executePost(HttpClient client, URI uri, Consumer<HttpRequestBase> consumer,
                                           InputStream bodyStream) throws IOException {
        HttpPost post = new HttpPost(uri);
        applyConsumer(post, consumer);
        post.setEntity(new InputStreamEntity(bodyStream, ContentType.APPLICATION_JSON));
        return client.execute(post);
    }

    public static HttpResponse executePut(
            HttpClient client,
            URI uri,
            Consumer<HttpRequestBase> consumer,
            Object body
    ) throws IOException {
        HttpPut put = new HttpPut(uri);
        applyConsumer(put, consumer);
        put.setEntity(new StringEntity(MapperUtil.getObjectWriter().writeValueAsString(body),
                ContentType.APPLICATION_JSON));
        return client.execute(put);
    }

    public static HttpResponse executePatch(
            HttpClient client,
            URI uri,
            Consumer<HttpRequestBase> consumer,
            Object body
    ) throws IOException {
        HttpPatch patch = new HttpPatch(uri);
        applyConsumer(patch, consumer);
        patch.setEntity(new StringEntity(MapperUtil.getObjectWriter().writeValueAsString(body),
                ContentType.APPLICATION_JSON));
        return client.execute(patch);
    }

    public static HttpResponse executeDelete(
            HttpClient client,
            URI uri,
            Consumer<HttpRequestBase> consumer
    ) throws IOException {
        HttpDelete delete = new HttpDelete(uri);
        applyConsumer(delete, consumer);
        return client.execute(delete);
    }

    private static void applyConsumer(HttpRequestBase httpRequest, Consumer<HttpRequestBase> httpConsumer) {
        if (httpConsumer != null) {
            httpConsumer.accept(httpRequest);
        }
    }
}
