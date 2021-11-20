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

import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.attachment.Attachment;
import ai.toloka.client.v1.attachment.AttachmentClient;
import ai.toloka.client.v1.attachment.AttachmentDownload;
import ai.toloka.client.v1.attachment.AttachmentSearchRequest;
import ai.toloka.client.v1.impl.transport.TransportUtil;
import ai.toloka.client.v1.impl.validation.Assertions;

public class AttachmentClientImpl extends AbstractClientImpl implements AttachmentClient {

    private static final String ATTACHMENTS_PATH = "attachments";
    private static final String DOWNLOAD_PATH = "download";

    AttachmentClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<Attachment<?>> findAttachments(AttachmentSearchRequest request) {
        return find(request, ATTACHMENTS_PATH, new TypeReference<SearchResult<Attachment>>() {});
    }

    @Override
    public Attachment<?> getAttachment(String attachmentId) {
        Assertions.checkArgNotNull(attachmentId, "Id may not be null");

        return get(attachmentId, ATTACHMENTS_PATH, Attachment.class);
    }

    @Override
    public AttachmentDownload downloadAttachment(final String attachmentId) {
        Assertions.checkArgNotNull(attachmentId, "Id may not be null");

        return new RequestExecutorWrapper<AttachmentDownload>() {

            @Override
            AttachmentDownload execute() throws URISyntaxException, IOException {
                URI uri = addVersionPrefix(
                        new URIBuilder(getTolokaApiUrl()), ATTACHMENTS_PATH, attachmentId, DOWNLOAD_PATH).build();

                HttpResponse response = TransportUtil.executeGet(getHttpClient(), uri, getHttpConsumer());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    final String contentType = response.getFirstHeader("Content-Type").getValue();
                    final String contentLength = response.getFirstHeader("Content-Length") != null
                            ? response.getFirstHeader("Content-Length").getValue() : null;
                    final String transferEncoding = response.getFirstHeader("Transfer-Encoding") != null
                            ? response.getFirstHeader("Transfer-Encoding").getValue() : null;
                    final String contentDisposition = response.getFirstHeader("Content-Disposition") != null
                            ? response.getFirstHeader("Content-Disposition").getValue() : null;

                    return new AttachmentDownload(response.getEntity().getContent(), contentType, contentLength,
                            transferEncoding, contentDisposition);
                }

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    return null;
                }

                throw parseException(response);
            }
        }.wrap();
    }
}
