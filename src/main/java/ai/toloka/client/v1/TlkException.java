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

package ai.toloka.client.v1;

public class TlkException extends RuntimeException {

    private final TlkError<?> error;
    private final int statusCode;

    public TlkException(TlkError<?> error, int statusCode) {
        this.error = error;
        this.statusCode = statusCode;
    }

    public TlkError<?> getError() {
        return error;
    }

    public String getCode() {
        return error.getCode();
    }

    public String getRequestId() {
        return error.getRequestId();
    }

    public String getServerMessage() {
        return error.getMessage();
    }

    public Object getPayload() {
        return error.getPayload();
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override public String getMessage() {
        return toString();
    }

    @Override public String toString() {
        return "TlkException{"
                + "error=" + error
                + ",statusCode=" + statusCode
                + '}';
    }
}
