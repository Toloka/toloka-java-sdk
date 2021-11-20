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

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.BatchCreateResult;
import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.userbonus.UserBonus;
import ai.toloka.client.v1.userbonus.UserBonusClient;
import ai.toloka.client.v1.userbonus.UserBonusCreateBatchOperation;
import ai.toloka.client.v1.userbonus.UserBonusCreateRequestParameters;
import ai.toloka.client.v1.userbonus.UserBonusSearchRequest;

public class UserBonusClientImpl extends AbstractClientImpl implements UserBonusClient {

    private static final String USER_BONUSES_PATH = "user-bonuses";

    UserBonusClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public ModificationResult<UserBonus> createUserBonus(UserBonus userBonus) {
        return createUserBonus(userBonus, null);
    }

    @Override
    public ModificationResult<UserBonus> createUserBonus(UserBonus userBonus,
                                                         UserBonusCreateRequestParameters parameters) {

        Assertions.checkArgNotNull(userBonus, "Form may not be null");

        return create(userBonus, USER_BONUSES_PATH, UserBonus.class,
                parameters != null ? parameters.getQueryParameters() : null);
    }

    @Override
    public BatchCreateResult<UserBonus> createUserBonuses(List<UserBonus> userBonuses) {
        return createUserBonuses(userBonuses, null);
    }

    @Override
    public BatchCreateResult<UserBonus> createUserBonuses(List<UserBonus> userBonuses,
                                                          UserBonusCreateRequestParameters parameters) {

        Assertions.checkArgNotNull(userBonuses, "Forms may not be null");

        return createMultiple(
                userBonuses, USER_BONUSES_PATH, new TypeReference<BatchCreateResult<UserBonus>>() {}, parameters);
    }

    @Override
    public UserBonusCreateBatchOperation createUserBonusesAsync(Iterator<UserBonus> userBonuses) {
        return createUserBonusesAsync(userBonuses, null);
    }

    @Override
    public UserBonusCreateBatchOperation createUserBonusesAsync(Iterator<UserBonus> userBonuses,
                                                                UserBonusCreateRequestParameters parameters) {

        Assertions.checkArgNotNull(userBonuses, "Forms may not be null");

        return (UserBonusCreateBatchOperation) createMultipleAsync(
                userBonuses, USER_BONUSES_PATH, Operation.class, parameters);
    }

    @Override
    public SearchResult<UserBonus> findUserBonuses(UserBonusSearchRequest request) {
        return find(request, USER_BONUSES_PATH, new TypeReference<SearchResult<UserBonus>>() {});
    }

    @Override
    public UserBonus getUserBonus(String userBonusId) {
        Assertions.checkArgNotNull(userBonusId, "Id may not be null");

        return get(userBonusId, USER_BONUSES_PATH, UserBonus.class);
    }
}
