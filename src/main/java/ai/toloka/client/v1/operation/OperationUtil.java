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

package ai.toloka.client.v1.operation;

class OperationUtil {

    public static final int DEFAULT_TIME_TO_WAIT_MILLIS = 1000;
    public static final int DEFAULT_TIMEOUT_MILLIS = 10 * 60 * 1000;
    public static final int DEFAULT_INITIAL_DELAY_MILLIS = 500;

    @SuppressWarnings("unchecked")
    static <P, T extends Operation<P, T>> T waitToComplete(T operation, OperationClient client)
            throws InterruptedException {

        if (operation == null || operation.getStatus().isTerminal()) {
            return operation;
        }

        long now = System.currentTimeMillis();
        long timeoutMillis = now + DEFAULT_TIMEOUT_MILLIS;

        if (operation.getStarted() == null || now - operation.getStarted().getTime() < DEFAULT_INITIAL_DELAY_MILLIS) {
            // if this method called immediately after watcher was created then it's better to wait a little before
            // first check
            Thread.sleep(DEFAULT_INITIAL_DELAY_MILLIS);
        }

        while (true) {
            Operation<?, ?> newOperation = client.getOperation(operation.getId());

            if (newOperation.isCompleted()) {
                return (T) newOperation;
            }

            Thread.sleep(DEFAULT_TIME_TO_WAIT_MILLIS);

            if (System.currentTimeMillis() > timeoutMillis) {
                throw new OperationTimeoutException();
            }
        }
    }

    static <P, T extends Operation<P, T>> T waitToCompleteUninterrupted(T operation, OperationClient client) {
        try {
            return waitToComplete(operation, client);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    static <P, T extends Operation<P, T>> T waitAndGetSuccessful(T operation, OperationClient client)
            throws InterruptedException {

        T completed = waitToComplete(operation, client);

        if (!completed.isSuccess()) {
            throw new OperationFailedException(completed.getDetailsAsMap());
        }

        return completed;
    }

    static <P, T extends Operation<P, T>> T waitAndGetSuccessfulUninterrupted(T operation, OperationClient client) {
        try {
            return waitAndGetSuccessful(operation, client);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
