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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import ai.toloka.client.v1.LangIso639;
import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.TolokaClientFactory;
import ai.toloka.client.v1.assignment.AssignmentSearchRequest;
import ai.toloka.client.v1.assignment.AssignmentStatus;
import ai.toloka.client.v1.impl.TolokaClientFactoryImpl;
import ai.toloka.client.v1.impl.transport.DefaultHttpClientConfiguration;
import ai.toloka.client.v1.pool.Pool;
import ai.toloka.client.v1.pool.PoolDefaults;
import ai.toloka.client.v1.pool.PoolStatus;
import ai.toloka.client.v1.pool.filter.ArrayInclusionOperator;
import ai.toloka.client.v1.pool.filter.CompareOperator;
import ai.toloka.client.v1.pool.filter.Connective;
import ai.toloka.client.v1.pool.filter.DeviceCategory;
import ai.toloka.client.v1.pool.filter.Expression;
import ai.toloka.client.v1.pool.filter.IdentityOperator;
import ai.toloka.client.v1.pool.qualitycontrol.CollectorConfig;
import ai.toloka.client.v1.pool.qualitycontrol.QualityControl;
import ai.toloka.client.v1.pool.qualitycontrol.QualityControlConfig;
import ai.toloka.client.v1.pool.qualitycontrol.RuleAction;
import ai.toloka.client.v1.pool.qualitycontrol.RuleCondition;
import ai.toloka.client.v1.pool.qualitycontrol.RuleConfig;
import ai.toloka.client.v1.project.AssignmentsIssuingType;
import ai.toloka.client.v1.project.Project;
import ai.toloka.client.v1.project.spec.FieldSpec;
import ai.toloka.client.v1.project.spec.TaskSpec;
import ai.toloka.client.v1.project.spec.TbTaskViewSpec;
import ai.toloka.client.v1.skill.Skill;
import ai.toloka.client.v1.skill.SkillSearchRequest;
import ai.toloka.client.v1.task.BaseTask;
import ai.toloka.client.v1.tasksuite.TaskSuite;

/**
 * Example of using toloka-java-sdk for image collection.
 *
 * @see <a href="https://github.com/Toloka/toloka-java-sdk/tree/master/examples/image_collection">
 *          README.md</a>
 */

public class ImageCollection {

    private static final Logger LOGGER = Logger.getLogger(ImageClassification.class.getName());

    private final TolokaClientFactoryImpl clientFactory;

    public ImageCollection(String token) {
        clientFactory = new TolokaClientFactoryImpl(
                DefaultHttpClientConfiguration.DEFAULT_TOLOKA_PROD_URI,
                token);
    }

    public String createNewProject() {
        final String name = "Take a photo of your pet(test test)";
        final String description = "If you have a cat or a dog, take a picture of it. "
                + "If you don't have any such animals, take a random photo.";
        String config = "{\n"
                + "    \"view\": {\n"
                + "        \"items\": [\n"
                + "            {\n"
                + "                \"data\": {\n"
                + "                    \"path\": \"image\",\n"
                + "                    \"type\": \"data.output\"\n"
                + "                },\n"
                + "                \"accept\": {\n"
                + "                    \"gallery\": true,\n"
                + "                    \"photo\": true\n"
                + "                },\n"
                + "                \"multiple\": false,\n"
                + "                \"label\": \"Upload a photo of your cat or dog. "
                + "Read the instructions carefully.\",\n"
                + "                \"validation\": {\n"
                + "                    \"type\": \"condition.required\"\n"
                + "                },\n"
                + "                \"type\": \"field.media-file\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"data\": {\n"
                + "                    \"path\": \"label\",\n"
                + "                    \"type\": \"data.output\"\n"
                + "                },\n"
                + "                \"options\": [\n"
                + "                    {\n"
                + "                        \"value\": \"cat\",\n"
                + "                        \"label\": \"Cat\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"value\": \"dog\",\n"
                + "                        \"label\": \"Dog\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"value\": \"none\",\n"
                + "                        \"label\": \"Not a cat nor a dog\"\n"
                + "                    }\n"
                + "                ],\n"
                + "                \"label\": \"What is in your photograph?\",\n"
                + "                \"validation\": {\n"
                + "                    \"type\": \"condition.required\"\n"
                + "                },\n"
                + "                \"type\": \"field.radio-group\"\n"
                + "            }\n"
                + "        ],\n"
                + "        \"type\": \"view.list\"\n"
                + "    }\n"
                + "}";
        final Map<String, FieldSpec> inputSpec = Map.of("label", new FieldSpec.StringSpec(true));
        var labelSpec = new FieldSpec.StringSpec(true);
        labelSpec.setAllowedValues(Set.of("cat", "dog", "none"));
        Map<String, FieldSpec> outputSpec = Map.of("image", new FieldSpec.FileSpec(true),
                "label", labelSpec);
        var viewSpec = new TbTaskViewSpec(Map.ofEntries(Map.entry("showSubmit", true),
                Map.entry("showFinish", true),
                Map.entry("showTimer", true),
                Map.entry("showReward", true),
                Map.entry("showTitle", true),
                Map.entry("showRoute", true),
                Map.entry("showComplain", true),
                Map.entry("showMessage", true),
                Map.entry("showSubmitExit", true),
                Map.entry("showFullscreen", true),
                Map.entry("showInstructions", true)));
        viewSpec.setConfig(config);
        var lock = Map.of("core", "1.0.0",
                "condition.required", "1.0.0",
                "field.media-file", "1.0.0",
                "field.radio-group", "1.0.0",
                "view.list", "1.0.0");
        viewSpec.setLock(lock);
        TaskSpec taskSpecification = new TaskSpec(inputSpec, outputSpec, viewSpec);
        String instructions = "Take a picture of your pet if it is a cat or a dog "
                + "and select the appropriate label type.<br><br>\n"
                + "If you do not have a cat or a dog, take a photo of anything "
                + "and select a \"Not a cat nor a dog\" label. "
                + "There should be exactly one animal in the photo, clearly visible, not cropped. "
                + "The animal can be photographed from any side and in any position. "
                + "You can take a picture of a pet in your arms.<br><br>\n"
                + "It should be clearly visible what animal is depicted "
                + "(e.g. do not photograph your pet's back in the dark).";

        Project project = new Project(
                name,
                description,
                instructions,
                taskSpecification,
                AssignmentsIssuingType.AUTOMATED
        );
        ModificationResult<Project> projectCreationResult = clientFactory.getProjectClient().createProject(project);
        String projectId = projectCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new project with ID {0} created", projectId);
        return projectId;
    }

    public String createPool(String projectId, String poolName, String skillName) {
        var skill = getSkill(clientFactory, skillName);
        Pool pool = new Pool(projectId,
                poolName,
                false,
                Date.from(Instant.now().plus(365, ChronoUnit.DAYS)),
                BigDecimal.valueOf(0.01),
                600,
                true,
                new PoolDefaults(1)
        );

        pool.setFilter(new Connective.And(List.of(new Expression.Languages(ArrayInclusionOperator.IN, LangIso639.EN),
                new Expression.Skill(skill.getId(), CompareOperator.EQ, null),
                new Expression.DeviceCategory(IdentityOperator.EQ, DeviceCategory.SMARTPHONE))));
        QualityControl qualityControl = new QualityControl(List.of(createSetSkillControlConfig(skill)));
        pool.setQualityControl(qualityControl);
        var poolCreationResult = clientFactory.getPoolClient().createPool(pool);
        String poolId = poolCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new pool with ID {0} created", poolId);
        return poolId;
    }

    public Skill getSkill(TolokaClientFactory clientFactory, String skillName) {
        var skillClient = clientFactory.getSkillClient();
        var skillSearchRequest = SkillSearchRequest.make().done();
        var findSkillsResult = skillClient.findSkills(skillSearchRequest);
        for (var skill : findSkillsResult.getItems()) {
            if (skill.getName().equals(skillName)) {
                LOGGER.log(Level.INFO, "Skill with name {0} already exists", skillName);
                return skill;
            }
        }
        var skill = new Skill(skillName);
        skill.setHidden(true);
        skill.setPublicRequesterDescription(Map.of(LangIso639.EN, "The performer took a photo of their pet."));
        var createSkillResult = skillClient.createSkill(skill);
        LOGGER.log(Level.INFO, "Create skill with name {0}", skillName);
        return createSkillResult.getResult();
    }

    private QualityControlConfig createSetSkillControlConfig(Skill skill) {
        RuleConfig setSkillRuleConfig = new RuleConfig(
                List.of(new RuleCondition.AssignmentsAcceptedCount(CompareOperator.GTE, 0)),
                new RuleAction.SetSkill(
                        new RuleAction.SetSkill.Parameters(
                                skill.getId(),
                                1)
                ));
        return new QualityControlConfig(new CollectorConfig.AnswerCount(), List.of(setSkillRuleConfig));
    }

    public void createTaskSuit(int imageCount, String poolId) {
        var taskSuit = new TaskSuite(poolId, List.of(new BaseTask(Map.of("label", "Cats vs Dogs"))));
        taskSuit.setOverlap(imageCount);
        var taskSuitClient = clientFactory.getTaskSuiteClient();
        taskSuitClient.createTaskSuite(taskSuit);
        LOGGER.log(Level.INFO, "TaskSuit created");
    }

    public void startPool(String poolId) {
        clientFactory.getPoolClient().openPool(poolId);
        LOGGER.log(Level.INFO, "Pool with ID {0} is started", poolId);
    }

    public void waitToComplete(String poolId) {
        Pool pool = clientFactory.getPoolClient().getPool(poolId);
        while (pool.getStatus() != PoolStatus.CLOSED) {
            LOGGER.log(Level.INFO, "Waiting for responses on pool {0}", poolId);
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException ignored) {
                // ignore
            }
            pool = clientFactory.getPoolClient().getPool(poolId);
        }
    }

    public void downloadAttachments(String poolId, String filesDirectory) {
        var solutions = getAttachmentsSolutions(poolId);
        LOGGER.log(Level.INFO, "Received responses amount: {0}", solutions.size());
        int solutionNumber = 1;
        for (var solution : solutions.entrySet()) {
            String fileName = "output" + solutionNumber + "_" + solution.getValue();
            if (filesDirectory != null) {
                fileName = filesDirectory + "/" + fileName;
            }
            downloadOneAttachment(solution.getKey(), fileName);
            solutionNumber++;
        }
    }

    public Map<String, String> getAttachmentsSolutions(String poolId) {
        var assigmentClient = clientFactory.getAssignmentClient();
        var assigmentSearchRequest = AssignmentSearchRequest.make()
                .filter().byStatus(AssignmentStatus.ACCEPTED).byPoolId(poolId)
                .and()
                .done();
        var searchAssigmentResult = assigmentClient.findAssignments(assigmentSearchRequest);
        Map<String, String> solutions = new HashMap<>();
        for (var item : searchAssigmentResult.getItems()) {
            var outputValues = item.getSolutions().get(0).getOutputValues();
            solutions.put((String) outputValues.get("image"), (String) outputValues.get("label"));
        }
        return solutions;
    }

    private void downloadOneAttachment(String attachmentId, String outputFile) {
        var attachmentClient = clientFactory.getAttachmentClient();
        var attachmentDownload = attachmentClient.downloadAttachment(attachmentId);
        try {
            byte [] data = attachmentDownload.getEntity().readAllBytes();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            BufferedImage bufferedImage = ImageIO.read(bis);
            ImageIO.write(bufferedImage, "jpg", new File(outputFile + ".jpg"));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Can not download attachment {0}", attachmentId);
        }
    }

    public static void main(String[] args) {
        var imageCollector = new ImageCollection("Your token here");
        String projectId = imageCollector.createNewProject();
        String poolId = imageCollector.createPool(projectId, "Pool 1", "Pet photo");
        imageCollector.createTaskSuit(1, poolId);
        imageCollector.startPool(poolId);
        imageCollector.waitToComplete(poolId);
        imageCollector.downloadAttachments(poolId, "examples/src/main/resources");
    }
}
