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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "code",
        defaultImpl = TlkError.class, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValidationError.class, name = ValidationError.VALIDATION_ERROR_CODE)
})
public class TlkError<P> {

    public static final String NOT_FOUND_CODE = "NOT_FOUND";
    public static final String METHOD_NOT_ALLOWED_CODE = "METHOD_NOT_ALLOWED";
    public static final String NOT_ACCEPTABLE_CODE = "NOT_ACCEPTABLE";
    public static final String UNSUPPORTED_MEDIA_TYPE_CODE = "UNSUPPORTED_MEDIA_TYPE";
    public static final String AUTHENTICATION_ERROR_CODE = "AUTHENTICATION_ERROR";
    public static final String ACCESS_DENIED_CODE = "ACCESS_DENIED";
    public static final String DOES_NOT_EXIST_CODE = "DOES_NOT_EXIST";
    public static final String CONFLICT_STATE_CODE = "CONFLICT_STATE";
    public static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";
    public static final String TOO_MANY_REQUESTS_CODE = "TOO_MANY_REQUESTS";
    public static final String ENTITY_TOO_LARGE_CODE = "ENTITY_TOO_LARGE";
    public static final String REMOTE_SERVICE_UNAVAILABLE_CODE = "REMOTE_SERVICE_UNAVAILABLE";
    public static final String INTERNAL_ERROR_CODE = "INTERNAL_ERROR";
    public static final String NGINX_ERROR_CODE = "NGINX_ERROR";

    /**
     * Operation is not allowed because of unarchived pools. Please, archive all active pools before you proceed.
     */
    public static final String UNARCHIVED_POOLS_CONFLICT_CODE = "UNARCHIVED_POOLS_CONFLICT";

    /**
     * Project is in an inappropriate status. Operation is not allowed.
     */
    public static final String PROJECT_INAPPROPRIATE_STATUS_CODE = "PROJECT_INAPPROPRIATE_STATUS";

    /**
     * Pool contains no tasks. Operation is not allowed.
     */
    public static final String EMPTY_POOL_CODE = "EMPTY_POOL";

    /**
     * There are submitted assignments which are not accepted.
     */
    public static final String SUBMITTED_ASSIGNMENTS_CONFLICT_CODE = "SUBMITTED_ASSIGNMENTS_CONFLICT";

    /**
     * Pool is in an inappropriate status. Operation is not allowed.
     */
    public static final String POOL_INAPPROPRIATE_STATUS_CODE = "POOL_INAPPROPRIATE_STATUS";

    /**
     * Pool must contain mixer config to perform request.
     */
    public static final String MIXER_CONFIG_REQUIRED_CODE = "MIXER_CONFIG_REQUIRED";

    /**
     * Limit of task suites in pool have been exceeded.
     */
    public static final String POOL_TASK_SUITES_COUNT_EXCEEDED_CODE = "POOL_TASK_SUITES_COUNT_EXCEEDED";

    /**
     * Limit of tasks in pool have been exceeded.
     */
    public static final String POOL_TASKS_COUNT_EXCEEDED_CODE = "POOL_TASKS_COUNT_EXCEEDED";

    /**
     * Assignments count is already greater than requested value.
     */
    public static final String ASSIGNMENTS_COUNT_CONFLICT_CODE = "ASSIGNMENTS_COUNT_CONFLICT";

    /**
     * Operation forbidden while pool locked by another operation.
     */
    public static final String POOL_LOCKED_BY_ANOTHER_OPERATION_CODE = "POOL_LOCKED_BY_ANOTHER_OPERATION";

    /**
     * Restrictions in 'SYSTEM' scope are forbidden to modify.
     */
    public static final String SYSTEM_SCOPE_MODIFICATION_CODE = "SYSTEM_SCOPE_MODIFICATION";

    /**
     * Batch processing may not be continued.
     */
    public static final String BATCH_INITIALIZATION_ERROR_CODE = "BATCH_INITIALIZATION_ERROR";

    /**
     * Operation execution failed because of internal error. Operation will be terminated. Please contact support.
     */
    public static final String OPERATION_EXECUTION_ERROR_CODE = "OPERATION_EXECUTION_ERROR";

    /**
     * Operation with provided id already exists.
     */
    public static final String OPERATION_ALREADY_EXISTS_CODE = "OPERATION_ALREADY_EXISTS";

    private final String code;
    private final String requestId;
    private final String message;
    private final Map<String, P> payload;

    @JsonCreator
    public TlkError(@JsonProperty("code") String code, @JsonProperty("request_id") String requestId,
                    @JsonProperty("message") String message, @JsonProperty("property") Map<String, P> payload) {

        this.code = code;
        this.requestId = requestId;
        this.message = message;
        this.payload = payload;
    }

    public TlkError(String code, String message, Map<String, P> payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;

        this.requestId = null;
    }

    public TlkError(String code, String message) {
        this.code = code;
        this.message = message;

        this.requestId = null;
        this.payload = null;
    }

    public String getCode() {
        return code;
    }

    /**
     * @return id of failed associated request, or null if no id was provided
     */
    public String getRequestId() {
        return requestId;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @return payload of an error, or null if no payload provided
     */
    public Map<String, P> getPayload() {
        return payload;
    }

    @Override public String toString() {
        return "TlkError{"
                + "code='" + code + '\''
                + ", requestId='" + requestId + '\''
                + ", message='" + message + '\''
                +  ", payload=" + payload
                + '}';
    }
}
