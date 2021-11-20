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

package ai.toloka.client.v1.pool;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;

public interface PoolClient {

    SearchResult<Pool> findPools(PoolSearchRequest request);

    Pool getPool(String id);

    ModificationResult<Pool> createPool(Pool pool);

    ModificationResult<Pool> updatePool(String poolId, Pool pool);

    ModificationResult<Pool> patchPool(String poolId, PoolPatchRequest request);

    PoolOpenOperation openPool(String poolId);

    PoolCloseOperation closePool(String poolId);

    PoolCloseOperation closePoolForUpdate(String poolId);

    PoolArchiveOperation archivePool(String poolId);

    PoolCloneOperation clonePool(String poolId);
}
