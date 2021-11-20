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

import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.pool.Pool;
import ai.toloka.client.v1.pool.PoolArchiveOperation;
import ai.toloka.client.v1.pool.PoolClient;
import ai.toloka.client.v1.pool.PoolCloneOperation;
import ai.toloka.client.v1.pool.PoolCloseOperation;
import ai.toloka.client.v1.pool.PoolOpenOperation;
import ai.toloka.client.v1.pool.PoolPatchRequest;
import ai.toloka.client.v1.pool.PoolSearchRequest;

public class PoolClientImpl extends AbstractClientImpl implements PoolClient {

    private static final String POOLS_PATH = "pools";
    private static final String POOLS_OPEN_ACTION_PATH = "open";
    private static final String POOLS_CLOSE_ACTION_PATH = "close";
    private static final String POOLS_CLOSE_FOR_UPDATE_ACTION_PATH = "close-for-update";
    private static final String POOLS_ARCHIVE_ACTION_PATH = "archive";
    private static final String POOLS_CLONE_ACTION_PATH = "clone";

    PoolClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<Pool> findPools(final PoolSearchRequest request) {
        return find(request, POOLS_PATH, new TypeReference<SearchResult<Pool>>() {});
    }

    @Override
    public Pool getPool(final String id) {
        return get(id, POOLS_PATH, Pool.class);
    }

    @Override
    public ModificationResult<Pool> createPool(final Pool pool) {
        Assertions.checkArgNotNull(pool, "Pool may not be null");

        return create(pool, POOLS_PATH, Pool.class, null);
    }

    @Override
    public ModificationResult<Pool> updatePool(String poolId, Pool pool) {
        Assertions.checkArgNotNull(poolId, "Pool id may not be null");
        Assertions.checkArgNotNull(pool, "Pool form may not be null");

        return update(poolId, pool, POOLS_PATH, Pool.class);
    }

    @Override
    public ModificationResult<Pool> patchPool(String poolId, PoolPatchRequest request) {
        Assertions.checkArgNotNull(poolId, "Id may not be null");
        Assertions.checkArgNotNull(request, "Patch request may not be null");

        return patch(poolId, request, POOLS_PATH, Pool.class, null);
    }

    @Override
    public PoolOpenOperation openPool(final String poolId) {
        Assertions.checkArgNotNull(poolId, "Id may not be null");

        PoolOpenOperation operation =
                (PoolOpenOperation) executeAction(poolId, POOLS_PATH, POOLS_OPEN_ACTION_PATH, Operation.class);
        if (operation == null) {
            return PoolOpenOperation.createPseudo(new Date());
        }

        return operation;
    }

    @Override
    public PoolCloseOperation closePool(final String poolId) {
        Assertions.checkArgNotNull(poolId, "Id may not be null");

        PoolCloseOperation operation =
                (PoolCloseOperation) executeAction(poolId, POOLS_PATH, POOLS_CLOSE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return PoolCloseOperation.createPseudo(new Date());
        }

        return operation;
    }

    @Override
    public PoolCloseOperation closePoolForUpdate(String poolId) {
        Assertions.checkArgNotNull(poolId, "Id may not be null");

        PoolCloseOperation operation = (PoolCloseOperation) executeAction(poolId, POOLS_PATH,
                POOLS_CLOSE_FOR_UPDATE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return PoolCloseOperation.createPseudo(new Date());
        }

        return operation;
    }

    @Override
    public PoolArchiveOperation archivePool(final String poolId) {
        Assertions.checkArgNotNull(poolId, "Id may not be null");

        PoolArchiveOperation operation =
                (PoolArchiveOperation) executeAction(poolId, POOLS_PATH, POOLS_ARCHIVE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return PoolArchiveOperation.createPseudo(new Date());
        }

        return operation;
    }

    @Override
    public PoolCloneOperation clonePool(String poolId) {
        Assertions.checkArgNotNull(poolId, "Id may not be null");

        PoolCloneOperation operation =
                (PoolCloneOperation) executeAction(poolId, POOLS_PATH, POOLS_CLONE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return PoolCloneOperation.createPseudo(new Date());
        }

        return operation;
    }
}
