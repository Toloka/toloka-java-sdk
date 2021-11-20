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

import java.net.URI;
import java.util.function.Consumer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import ai.toloka.client.v1.TolokaClientFactory;
import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionClient;
import ai.toloka.client.v1.assignment.AssignmentClient;
import ai.toloka.client.v1.attachment.AttachmentClient;
import ai.toloka.client.v1.impl.transport.DefaultHttpClientConfiguration;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.messagethread.MessageThreadClient;
import ai.toloka.client.v1.operation.OperationClient;
import ai.toloka.client.v1.pool.PoolClient;
import ai.toloka.client.v1.project.ProjectClient;
import ai.toloka.client.v1.requester.RequesterClient;
import ai.toloka.client.v1.skill.SkillClient;
import ai.toloka.client.v1.task.TaskClient;
import ai.toloka.client.v1.tasksuite.TaskSuiteClient;
import ai.toloka.client.v1.training.TrainingClient;
import ai.toloka.client.v1.userbonus.UserBonusClient;
import ai.toloka.client.v1.userrestriction.UserRestrictionClient;
import ai.toloka.client.v1.userskill.UserSkillClient;
import ai.toloka.client.v1.webhooksubscription.WebhookSubscriptionClient;

public class TolokaClientFactoryImpl implements TolokaClientFactory {

    private final HttpClient httpClient;
    private final URI tolokaApiUrl;
    private Consumer<HttpRequestBase> headersSupplier;

    private RequesterClient requesterClient;
    private ProjectClient projectClient;
    private PoolClient poolClient;
    private TrainingClient trainingClient;
    private TaskClient taskClient;
    private TaskSuiteClient taskSuiteClient;
    private AssignmentClient assignmentClient;
    private AggregatedSolutionClient aggregatedSolutionClient;
    private UserSkillClient userSkillClient;
    private UserRestrictionClient userRestrictionClient;
    private AttachmentClient attachmentClient;
    private OperationClient operationClient;
    private SkillClient skillClient;
    private UserBonusClient userBonusClient;
    private MessageThreadClient messageThreadClient;
    private WebhookSubscriptionClient webhookSubscriptionClient;

    public TolokaClientFactoryImpl(String oauthToken) {
        Assertions.checkArgNotNull(oauthToken, "OAuth token may not be null");

        this.tolokaApiUrl = DefaultHttpClientConfiguration.DEFAULT_TOLOKA_SANDBOX_URI;
        this.httpClient = DefaultHttpClientConfiguration.buildDefaultClient(oauthToken);
    }

    /**
     * @param tolokaApiUrl path to toloka API
     * @param oauthToken   OAuth token, may be obtained from GUI
     */
    public TolokaClientFactoryImpl(URI tolokaApiUrl, String oauthToken) {
        Assertions.checkArgNotNull(tolokaApiUrl, "Toloka API URL may not be null");
        Assertions.checkArgNotNull(oauthToken, "OAuth token may not be null");

        this.tolokaApiUrl = tolokaApiUrl;
        this.httpClient = DefaultHttpClientConfiguration.buildDefaultClient(oauthToken);
    }

    /**
     * @param tolokaApiUrl path to toloka API
     * @param oauthToken   OAuth token, may be obtained from GUI
     */
    public TolokaClientFactoryImpl(String tolokaApiUrl, String oauthToken) {
        Assertions.checkArgNotNull(tolokaApiUrl, "Toloka API URL may not be null");
        Assertions.checkArgNotNull(oauthToken, "OAuth token may not be null");

        this.tolokaApiUrl = URI.create(tolokaApiUrl);
        this.httpClient = DefaultHttpClientConfiguration.buildDefaultClient(oauthToken);
    }

    public TolokaClientFactoryImpl(URI tolokaApiUrl, HttpClient httpClient) {
        Assertions.checkArgNotNull(tolokaApiUrl, "Toloka API URL may not be null");
        Assertions.checkArgNotNull(httpClient, "Http Client may not be null");

        this.tolokaApiUrl = tolokaApiUrl;
        this.httpClient = httpClient;
    }

    public TolokaClientFactoryImpl(String tolokaApiUrl, HttpClient httpClient) {
        Assertions.checkArgNotNull(tolokaApiUrl, "Toloka API URL may not be null");
        Assertions.checkArgNotNull(httpClient, "Http Client may not be null");

        this.tolokaApiUrl = URI.create(tolokaApiUrl);
        this.httpClient = httpClient;
    }

    @Override
    public RequesterClient getRequesterClient() {
        if (requesterClient == null) {
            this.requesterClient = new RequesterClientImpl(this);
        }
        return requesterClient;
    }

    @Override
    public ProjectClient getProjectClient() {
        if (projectClient == null) {
            this.projectClient = new ProjectClientImpl(this);
        }
        return projectClient;
    }

    @Override
    public PoolClient getPoolClient() {
        if (poolClient == null) {
            this.poolClient = new PoolClientImpl(this);
        }
        return poolClient;
    }

    @Override
    public TrainingClient getTrainingClient() {
        if (trainingClient == null) {
            this.trainingClient = new TrainingClientImpl(this);
        }
        return trainingClient;
    }


    @Override
    public TaskClient getTaskClient() {
        if (taskClient == null) {
            taskClient = new TaskClientImpl(this);
        }
        return taskClient;
    }

    @Override
    public TaskSuiteClient getTaskSuiteClient() {
        if (taskSuiteClient == null) {
            this.taskSuiteClient = new TaskSuiteClientImpl(this);
        }
        return taskSuiteClient;
    }

    @Override
    public AssignmentClient getAssignmentClient() {
        if (this.assignmentClient == null) {
            this.assignmentClient = new AssignmentClientImpl(this);
        }
        return assignmentClient;
    }

    @Override
    public AggregatedSolutionClient getAggregatedSolutionClient() {
        if (this.aggregatedSolutionClient == null) {
            this.aggregatedSolutionClient = new AggregatedSolutionClientImpl(this);
        }
        return aggregatedSolutionClient;
    }

    @Override
    public UserSkillClient getUserSkillClient() {
        if (this.userSkillClient == null) {
            this.userSkillClient = new UserSkillClientImpl(this);
        }
        return this.userSkillClient;
    }

    @Override
    public UserRestrictionClient getUserRestrictionClient() {
        if (this.userRestrictionClient == null) {
            this.userRestrictionClient = new UserRestrictionClientImpl(this);
        }
        return this.userRestrictionClient;
    }

    @Override
    public AttachmentClient getAttachmentClient() {
        if (this.attachmentClient == null) {
            this.attachmentClient = new AttachmentClientImpl(this);
        }
        return this.attachmentClient;
    }

    @Override
    public OperationClient getOperationClient() {
        if (operationClient == null) {
            this.operationClient = new OperationClientImpl(this);
        }
        return operationClient;
    }

    @Override
    public SkillClient getSkillClient() {
        if (skillClient == null) {
            this.skillClient = new SkillClientImpl(this);
        }
        return skillClient;
    }

    @Override
    public UserBonusClient getUserBonusClient() {
        if (userBonusClient == null) {
            this.userBonusClient = new UserBonusClientImpl(this);
        }
        return userBonusClient;
    }

    @Override
    public MessageThreadClient getMessageThreadClient() {
        if (messageThreadClient == null) {
            this.messageThreadClient = new MessageThreadClientImpl(this);
        }
        return messageThreadClient;
    }

    @Override
    public WebhookSubscriptionClient getWebhookSubscriptionClient() {
        if (webhookSubscriptionClient == null) {
            this.webhookSubscriptionClient = new WebhookSubscriptionClientImpl(this);
        }
        return webhookSubscriptionClient;
    }

    public URI getTolokaApiUrl() {
        return tolokaApiUrl;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public Consumer<HttpRequestBase> getHeadersSupplier() {
        return headersSupplier;
    }

    public TolokaClientFactoryImpl tune(Consumer<HttpRequestBase> httpConsumer) {
        this.headersSupplier = httpConsumer;
        return this;
    }
}
