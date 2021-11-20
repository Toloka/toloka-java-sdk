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

import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionClient;
import ai.toloka.client.v1.assignment.AssignmentClient;
import ai.toloka.client.v1.attachment.AttachmentClient;
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

public interface TolokaClientFactory {

    RequesterClient getRequesterClient();

    ProjectClient getProjectClient();

    PoolClient getPoolClient();

    TrainingClient getTrainingClient();

    TaskClient getTaskClient();

    TaskSuiteClient getTaskSuiteClient();

    AssignmentClient getAssignmentClient();

    AggregatedSolutionClient getAggregatedSolutionClient();

    UserSkillClient getUserSkillClient();

    UserRestrictionClient getUserRestrictionClient();

    AttachmentClient getAttachmentClient();

    OperationClient getOperationClient();

    SkillClient getSkillClient();

    UserBonusClient getUserBonusClient();

    MessageThreadClient getMessageThreadClient();

    WebhookSubscriptionClient getWebhookSubscriptionClient();
}
