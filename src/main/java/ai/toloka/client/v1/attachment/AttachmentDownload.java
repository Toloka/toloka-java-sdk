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

package ai.toloka.client.v1.attachment;

import java.io.InputStream;

public class AttachmentDownload {

    private final InputStream entity;

    private final String contentType;
    private final String contentLength;
    private final String transferEncoding;
    private final String contentDisposition;

    public AttachmentDownload(final InputStream entity,
                              final String contentType,
                              final String contentLength,
                              final String transferEncoding,
                              final String contentDisposition) {
        this.entity = entity;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.transferEncoding = transferEncoding;
        this.contentDisposition = contentDisposition;
    }

    public InputStream getEntity() {
        return entity;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentLength() {
        return contentLength;
    }

    public String getTransferEncoding() {
        return transferEncoding;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }
}
