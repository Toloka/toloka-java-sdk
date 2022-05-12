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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;


import ai.toloka.client.v1.LangIso639;
import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionSearchRequest;
import ai.toloka.client.v1.aggregatedsolutions.WeightedDynamicOverlapPoolAggregatedSolutionRequest;
import ai.toloka.client.v1.assignment.AssignmentSearchRequest;
import ai.toloka.client.v1.assignment.AssignmentStatus;
import ai.toloka.client.v1.impl.TolokaClientFactoryImpl;
import ai.toloka.client.v1.impl.transport.DefaultHttpClientConfiguration;
import ai.toloka.client.v1.pool.MixerConfig;
import ai.toloka.client.v1.pool.Pool;
import ai.toloka.client.v1.pool.PoolDefaults;
import ai.toloka.client.v1.pool.PoolStatus;
import ai.toloka.client.v1.pool.filter.ArrayInclusionOperator;
import ai.toloka.client.v1.pool.filter.CompareOperator;
import ai.toloka.client.v1.pool.filter.Connective;
import ai.toloka.client.v1.pool.filter.Expression;
import ai.toloka.client.v1.pool.filter.IdentityOperator;
import ai.toloka.client.v1.pool.qualitycontrol.CollectorConfig;
import ai.toloka.client.v1.pool.qualitycontrol.QualityControl;
import ai.toloka.client.v1.pool.qualitycontrol.QualityControlConfig;
import ai.toloka.client.v1.pool.qualitycontrol.RuleAction;
import ai.toloka.client.v1.pool.qualitycontrol.RuleCondition;
import ai.toloka.client.v1.pool.qualitycontrol.RuleConditionKey;
import ai.toloka.client.v1.pool.qualitycontrol.RuleConfig;
import ai.toloka.client.v1.project.AssignmentsIssuingType;
import ai.toloka.client.v1.project.Project;
import ai.toloka.client.v1.project.spec.FieldSpec;
import ai.toloka.client.v1.project.spec.TaskSpec;
import ai.toloka.client.v1.project.spec.TbTaskViewSpec;
import ai.toloka.client.v1.skill.Skill;
import ai.toloka.client.v1.skill.SkillSearchRequest;
import ai.toloka.client.v1.task.Task;
import ai.toloka.client.v1.task.TaskCreateRequestParameters;
import ai.toloka.client.v1.userrestriction.DurationUnit;
import ai.toloka.client.v1.userrestriction.UserRestrictionScope;

/**
 * Example of using toloka-java-sdk for image detection.
 *
 * @see <a href="https://github.com/Toloka/toloka-java-sdk/tree/master/examples/image_detection">
 *          README.md</a>
 */
public class ImageDetection {
    private static final Logger LOGGER = Logger.getLogger(ImageClassification.class.getName());
    private final TolokaClientFactoryImpl clientFactory;

    public ImageDetection(String token) {
        clientFactory = new TolokaClientFactoryImpl(
                DefaultHttpClientConfiguration.DEFAULT_TOLOKA_PROD_URI,
                token);
    }

    public String createNewDetectionProject() {
        String name = "Outline all traffic signs with bounding boxes";
        String description = "Find and outline all traffic signs with bounding boxes.";
        String config = "{\n"
                + "    \"view\": {\n"
                + "        \"data\": {\n"
                + "            \"path\": \"result\",\n"
                + "            \"type\": \"data.output\"\n"
                + "        },\n"
                + "        \"image\": {\n"
                + "            \"path\": \"image\",\n"
                + "            \"type\": \"data.input\"\n"
                + "        },\n"
                + "        \"shapes\": {\n"
                + "            \"rectangle\": true\n"
                + "        },\n"
                + "        \"validation\": {\n"
                + "            \"hint\": \"Please select an area\",\n"
                + "            \"type\": \"condition.required\"\n"
                + "        },\n"
                + "        \"type\": \"field.image-annotation\"\n"
                + "    }\n"
                + "}";

        Map<String, FieldSpec> inputSpec = Map.of("image", new FieldSpec.UrlSpec(true));
        Map<String, FieldSpec> outputSpec = Map.of("result", new FieldSpec.JsonSpec(true));
        var viewSpec = new TbTaskViewSpec(Map.of());
        viewSpec.setConfig(config);
        var lock = Map.of("core", "1.0.0",
                "condition.required", "1.0.0",
                "field.image-annotation", "1.0.0");
        viewSpec.setLock(lock);
        TaskSpec taskSpecification = new TaskSpec(inputSpec, outputSpec, viewSpec);
        String instructions = "<b>Outline each traffic sign with a separate bounding box(rectangle).</b>";

        Project project = new Project(
                name,
                description,
                instructions,
                taskSpecification,
                AssignmentsIssuingType.AUTOMATED
        );
        ModificationResult<Project> projectCreationResult = clientFactory.getProjectClient().createProject(project);
        String projectId = projectCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new detection project with ID {0} created", projectId);
        return projectId;
    }

    public String createDetectionPool(String projectId, String poolName) {
        final var detectionSkill = getSkill("Area selection of road signs",
                "Performer is annotating road signs");
        var poolDefaults = new PoolDefaults(1);
        poolDefaults.setDefaultOverlapForNewTasks(1);
        Pool pool = new Pool(projectId,
                poolName,
                false,
                Date.from(Instant.now().plus(365, ChronoUnit.DAYS)),
                BigDecimal.valueOf(0.01),
                600,
                false,
                poolDefaults
        );
        pool.setAutoAcceptPeriodDay(7);
        pool.setMixerConfig(new MixerConfig(
                1,  // real tasks count
                0,  // golden tasks count
                0   // training tasks count
        ));
        pool.setFilter(new Connective.And(List.of(new Expression.Languages(ArrayInclusionOperator.IN, LangIso639.EN))));
        QualityControl qualityControl = new QualityControl(List.of(
                createAcceptanceRateControlConfig(),
                createAssignmentSubmitTimeControlConfig(),
                createAnswerCountControlConfig(detectionSkill),
                createAssignmentsAssessmentControlConfig()));
        pool.setQualityControl(qualityControl);
        var poolCreationResult = clientFactory.getPoolClient().createPool(pool);
        String poolId = poolCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new detection pool with ID {0} created", poolId);
        return poolId;
    }

    private Skill getSkill(String skillName, String description) {
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
        skill.setPublicRequesterDescription(Map.of(LangIso639.EN, description));
        var createSkillResult = skillClient.createSkill(skill);
        LOGGER.log(Level.INFO, "Create skill with name {0}", skillName);
        return createSkillResult.getResult();
    }

    private QualityControlConfig createAcceptanceRateControlConfig() {
        RuleConfig acceptanceRateRuleConfig = new RuleConfig(
                List.of(new RuleCondition.TotalAssignmentsCount(CompareOperator.GT, 2),
                        new RuleCondition.RejectedAssignmentsRate(CompareOperator.GT, 35.0)),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.ALL_PROJECTS,
                                15, DurationUnit.DAY,
                                "Performer often make mistakes")
                ));

        return new QualityControlConfig(
                new CollectorConfig.AcceptanceRate(),
                List.of(acceptanceRateRuleConfig)
        );
    }

    private QualityControlConfig createAssignmentSubmitTimeControlConfig() {
        RuleConfig assignmentSubmitTimeRuleConfig = new RuleConfig(
                List.of(new RuleCondition.FastSubmittedCount(CompareOperator.GT, 1)),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.ALL_PROJECTS,
                                10, DurationUnit.DAY,
                                "Fast responses")
                ));

        return new QualityControlConfig(
                new CollectorConfig.AssignmentSubmitTime(
                        new CollectorConfig.AssignmentSubmitTime.Parameters(5, 20)),
                List.of(assignmentSubmitTimeRuleConfig)
        );
    }

    private QualityControlConfig createAnswerCountControlConfig(Skill skill) {
        RuleConfig answerCountRuleConfig = new RuleConfig(
                List.of(new RuleCondition.AssignmentsAcceptedCount(CompareOperator.GT, 0)),
                new RuleAction.SetSkill(
                        new RuleAction.SetSkill.Parameters(
                                skill.getId(),
                                1)
                ));
        return new QualityControlConfig(new CollectorConfig.AnswerCount(), List.of(answerCountRuleConfig));
    }

    private QualityControlConfig createAssignmentsAssessmentControlConfig() {
        RuleConfig assignmentsAssessmentRuleConfig = new RuleConfig(
                List.of(new RuleCondition.AssessmentEvent(IdentityOperator.EQ,
                        RuleCondition.AssessmentEvent.Type.REJECT)),
                new RuleAction.ChangeOverlap(
                        new RuleAction.ChangeOverlap.Parameters(
                                1,
                                true
                        )
                ));
        return new QualityControlConfig(new CollectorConfig.AssignmentsAssessment(),
                List.of(assignmentsAssessmentRuleConfig));
    }

    public void prepareAndUploadDetectionTasks(String poolId) {
        List<String> dataset;
        try {
            dataset = ImageDetection.readTsv("examples/src/main/resources/dataset_image_detection.tsv");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem while reading dataset from file: {0}", e.getMessage());
            return;
        }
        var taskDataset = dataset.subList(0, 20);

        var tasks = taskDataset.stream().map(url -> {
            var task = new Task(poolId, Map.of("image", url));
            task.setOverlap(1);
            return task;
        }).collect(Collectors.toUnmodifiableList());

        var taskCreateRequestParameters = new TaskCreateRequestParameters();
        clientFactory.getTaskClient().createTasks(tasks, taskCreateRequestParameters);
        LOGGER.log(Level.INFO, "{0} tasks are uploaded", taskDataset.size());
    }

    private static List<String> readTsv(String file) throws IOException {
        String content = Files.readString(Path.of(file), StandardCharsets.US_ASCII);
        List<String> rows = new ArrayList<>(List.of(content.split("\n")));
        rows.remove(0);
        return rows;
    }

    public void startPool(String poolId) {
        clientFactory.getPoolClient().openPool(poolId);
        LOGGER.log(Level.INFO, "Pool with ID {0} is started", poolId);
    }

    public void waitForAllResponses(String poolId) {
        String verificationProjectId = createNewVerificationProject();
        String verificationPoolId = createVerificationPool(verificationProjectId,
                "Pool 1. Road sign verification");
        while (true) {
            waitToComplete(poolId);
            if (validate(poolId, verificationPoolId)) {
                break;
            }
        }
    }

    private String createNewVerificationProject() {
        final String name = "Are the traffic signs outlined correctly?";
        final String description = "Look at the image "
                + "and decide whether or not the traffic signs are outlined correctly";
        final String config = "{\n"
                + "    \"view\": {\n"
                + "        \"items\": [\n"
                + "            {\n"
                + "                \"data\": {\n"
                + "                    \"path\": \"selection\",\n"
                + "                    \"default\": {\n"
                + "                        \"path\": \"selection\",\n"
                + "                        \"type\": \"data.input\"\n"
                + "                    },\n"
                + "                    \"type\": \"data.internal\"\n"
                + "                },\n"
                + "                \"image\": {\n"
                + "                    \"path\": \"image\",\n"
                + "                    \"type\": \"data.input\"\n"
                + "                },\n"
                + "                \"disabled\": true,\n"
                + "                \"type\": \"field.image-annotation\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"data\": {\n"
                + "                    \"path\": \"result\",\n"
                + "                    \"type\": \"data.output\"\n"
                + "                },\n"
                + "                \"options\": [\n"
                + "                    {\n"
                + "                        \"value\": \"OK\",\n"
                + "                        \"label\": \"Yes\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"value\": \"BAD\",\n"
                + "                        \"label\": \"No\"\n"
                + "                    }\n"
                + "                ],\n"
                + "                \"label\": \"Are all traffic signs outlined correctly?\",\n"
                + "                \"validation\": {\n"
                + "                    \"type\": \"condition.required\"\n"
                + "                },\n"
                + "                \"type\": \"field.radio-group\"\n"
                + "            }\n"
                + "        ],\n"
                + "        \"type\": \"view.list\"\n"
                + "    },\n"
                + "    \"plugins\": [\n"
                + "        {\n"
                + "            \"1\": {\n"
                + "                \"data\": {\n"
                + "                    \"path\": \"result\",\n"
                + "                    \"type\": \"data.output\"\n"
                + "                },\n"
                + "                \"payload\": \"OK\",\n"
                + "                \"type\": \"action.set\"\n"
                + "            },\n"
                + "            \"2\": {\n"
                + "                \"data\": {\n"
                + "                    \"path\": \"result\",\n"
                + "                    \"type\": \"data.output\"\n"
                + "                },\n"
                + "                \"payload\": \"BAD\",\n"
                + "                \"type\": \"action.set\"\n"
                + "            },\n"
                + "            \"type\": \"plugin.hotkeys\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        final Map<String, FieldSpec> inputSpec = Map.of("image", new FieldSpec.UrlSpec(true),
                                                  "selection", new FieldSpec.JsonSpec(true),
                                                  "assignment_id", new FieldSpec.StringSpec(true));
        var stringSpec = new FieldSpec.StringSpec(true);
        stringSpec.setAllowedValues(Set.of("OK", "BAD"));
        Map<String, FieldSpec> outputSpec = Map.of("result", stringSpec);
        var viewSpec = new TbTaskViewSpec(Map.of());
        viewSpec.setConfig(config);
        var lock = Map.of("core", "1.0.0",
                "field.image-annotation", "1.0.0",
                "condition.required", "1.0.0",
                "field.radio-group", "1.0.0",
                "view.list", "1.0.0",
                "action.set", "1.0.0",
                "plugin.hotkeys", "1.0.0");
        viewSpec.setLock(lock);
        TaskSpec taskSpecification = new TaskSpec(inputSpec, outputSpec, viewSpec);
        String instructions = "'<b>Look at the image and answer the question:</b><br/>\n"
                + "Are all traffic signs outlined correctly?<br/>\n"
                + "If they are, click Yes.<br/>\n"
                + "If they aren't, click No.<br/>\n"
                + "For example, the road signs here are outlined correctly, so the correct answer is Yes.";

        Project project = new Project(
                name,
                description,
                instructions,
                taskSpecification,
                AssignmentsIssuingType.AUTOMATED
        );
        ModificationResult<Project> projectCreationResult = clientFactory.getProjectClient().createProject(project);
        String projectId = projectCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new verification project with ID {0} created", projectId);
        return projectId;
    }

    private String createVerificationPool(String projectId, String poolName) {
        final var detectionSkill = getSkill("Area selection of road signs",
                "Performer is annotating road signs");
        final var verificationSkill = getSkill("Detection verification",
                "How good a performer is at verifying detection tasks");
        Pool pool = new Pool(projectId,
                poolName,
                false,
                Date.from(Instant.now().plus(365, ChronoUnit.DAYS)),
                BigDecimal.valueOf(0.01),
                600,
                true,
                new PoolDefaults(5)
        );
        var mixerConfig = new MixerConfig(
                10,  // real tasks count
                0,  // golden tasks count
                0   // training tasks count
        );
        mixerConfig.setForceLastAssignment(true);
        pool.setMixerConfig(mixerConfig);
        pool.setFilter(new Connective.And(List.of(new Expression.Languages(ArrayInclusionOperator.IN, LangIso639.EN),
                new Expression.Skill(detectionSkill.getId(), CompareOperator.EQ, null))));


        QualityControl qualityControl = new QualityControl(List.of(
                createMajorityVoteControlConfig(),
                createSetSkillControlConfig(verificationSkill)));
        pool.setQualityControl(qualityControl);
        var poolCreationResult = clientFactory.getPoolClient().createPool(pool);
        String poolId = poolCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new verification pool with ID {0} created", poolId);
        return poolId;
    }

    private QualityControlConfig createMajorityVoteControlConfig() {
        RuleConfig majorityVoteRuleConfig = new RuleConfig(
                List.of(
                        new RuleCondition.TotalAnswersCount(CompareOperator.GT, 9),
                        new RuleCondition.CorrectAnswersRate(CompareOperator.LT, 50.0)
                ),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.ALL_PROJECTS,
                                10, DurationUnit.DAY,
                                "Doesn't match the majority")
                ));

        var majorityVoteParameters = new CollectorConfig.MajorityVote.Parameters(2);
        return new QualityControlConfig(
                new CollectorConfig.MajorityVote(majorityVoteParameters),
                List.of(majorityVoteRuleConfig)
        );
    }

    private QualityControlConfig createSetSkillControlConfig(Skill verificationSkill) {
        RuleConfig setSkillRuleConfig = new RuleConfig(
                List.of(new RuleCondition.TotalAnswersCount(CompareOperator.GT, 2)),
                new RuleAction.SetSkillFromOutputField(
                        new RuleAction.SetSkillFromOutputField.Parameters(
                                verificationSkill.getId(),
                                RuleConditionKey.CORRECT_ANSWERS_RATE
                        )
                ));

        var majorityVoteParameters = new CollectorConfig.MajorityVote.Parameters(2);
        majorityVoteParameters.setHistorySize(10);
        return new QualityControlConfig(
                new CollectorConfig.MajorityVote(majorityVoteParameters),
                List.of(setSkillRuleConfig)
        );
    }

    private void waitToComplete(String poolId) {
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

    private boolean validate(String poolId, String verificationPoolId) {
        uploadTasksForVerification(poolId, verificationPoolId);
        startPool(verificationPoolId);
        waitToComplete(verificationPoolId);
        String operationId = aggregateSolutions(verificationPoolId);
        return getVerificationResults(operationId);
    }


    private void uploadTasksForVerification(String poolId, String verificationPoolId) {
        var assignmentSearchRequest = AssignmentSearchRequest.make()
                .filter().byStatus(AssignmentStatus.SUBMITTED).byPoolId(poolId)
                .and()
                .done();

        var assignmentClient = clientFactory.getAssignmentClient();
        var assignmentSearchResult = assignmentClient.findAssignments(assignmentSearchRequest);

        var tasks = assignmentSearchResult.getItems().stream().map(assignment -> {
            var task = new Task(
                    verificationPoolId,
                    Map.of("image", assignment.getTasks().get(0).getInputValues().get("image"),
                            "selection", assignment.getSolutions().get(0).getOutputValues().get("result"),
                            "assignment_id", assignment.getId())
            );
            task.setOverlap(5);
            return task;
        }).collect(Collectors.toUnmodifiableList());

        var taskCreateRequestParameters = new TaskCreateRequestParameters();
        clientFactory.getTaskClient().createTasks(tasks, taskCreateRequestParameters);
        LOGGER.log(Level.INFO, "{0} tasks for verification are uploaded", tasks.size());
    }

    private String aggregateSolutions(String poolId) {
        var verificationSkill = getSkill("Detection verification",
                "How good a performer is at verifying detection tasks");
        var aggregateSolutionsRequest = new WeightedDynamicOverlapPoolAggregatedSolutionRequest(poolId,
                verificationSkill.getId(),
                List.of(new WeightedDynamicOverlapPoolAggregatedSolutionRequest.Field("result")));

        var aggregatedSolutionOperation = clientFactory.getAggregatedSolutionClient()
                .aggregateSolutionsByPool(aggregateSolutionsRequest);

        while (!aggregatedSolutionOperation.isCompleted()) {
            try {
                LOGGER.log(Level.INFO, "Waiting for aggregating solutions");
                aggregatedSolutionOperation = aggregatedSolutionOperation.waitToComplete();
            } catch (InterruptedException ignored) {
                // ignore
            }
        }
        return aggregatedSolutionOperation.getId();
    }


    private boolean getVerificationResults(String operationId) {
        var solutionSearchRequest = AggregatedSolutionSearchRequest.make().limit(250).done();
        var aggregatedSolutionSearchResult = clientFactory.getAggregatedSolutionClient()
                .findAggregatedSolutions(operationId, solutionSearchRequest);

        var taskClient = clientFactory.getTaskClient();
        var assignmentClient = clientFactory.getAssignmentClient();
        boolean wasReject = false;
        for (var solution : aggregatedSolutionSearchResult.getItems()) {
            String assignmentId = (String) taskClient.getTask(solution.getTaskId())
                                                     .getInputValues().get("assignment_id");
            var assignment = assignmentClient.getAssignment(assignmentId);
            if (assignment.getStatus() != AssignmentStatus.SUBMITTED) {
                continue;
            }
            if (solution.getOutputValues().get("result").equals("OK")) {
                assignmentClient.acceptAssignment(assignmentId, "Well done!");
            } else {
                wasReject = true;
                assignmentClient.rejectAssignment(assignmentId,
                        "The object wasn't selected or was selected incorrectly.");
            }
        }
        LOGGER.log(Level.INFO, "Assignments statuses are updated");
        return !wasReject;
    }

    public void getResults(String poolId) {
        var assignmentSearchRequest = AssignmentSearchRequest.make()
                .filter().byStatus(AssignmentStatus.ACCEPTED).byPoolId(poolId)
                .and()
                .done();

        var assignmentClient = clientFactory.getAssignmentClient();
        var assignmentSearchResult = assignmentClient.findAssignments(assignmentSearchRequest);
        int resultNumber = 0;
        for (var item : assignmentSearchResult.getItems()) {
            saveResult((String) item.getTasks().get(0).getInputValues().get("image"),
                    item.getSolutions().get(0).getOutputValues(),
                    "examples/src/main/resources/result" + resultNumber);
            resultNumber++;
        }
        LOGGER.log(Level.INFO, "{0} results are received", resultNumber);
    }


    private void saveResult(String imgUrl, Map<String, Object> outputValues, String fileToSave) {
        BufferedImage img;
        try {
            URL url = new URL(imgUrl);
            img = ImageIO.read(url);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem while loading picture from url: {0}", e.getMessage());
            return;
        }

        int imgHeight = img.getHeight();
        int imgWidth = img.getWidth();
        Graphics2D graph = img.createGraphics();
        graph.setColor(Color.RED);

        List<Map<String, Object>> selections = (List<Map<String, Object>>) outputValues.get("result");
        for (var selection : selections) {
            if (selection.get("shape") == null || !selection.get("shape").equals("rectangle")) {
                continue;
            }
            double x = (Double) selection.get("left") * imgWidth;
            double y = (Double) selection.get("top") * imgHeight;
            double selectionWidth = (Double) selection.get("width") * imgWidth;
            double selectionHeight = (Double) selection.get("height") * imgHeight;
            graph.draw(new Rectangle((int) x, (int) y, (int) selectionWidth, (int) selectionHeight));
        }

        try {
            ImageIO.write(img, "jpg",
                    new File(fileToSave + ".jpg"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem while loading result to file: {0}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        var imageDetector = new ImageDetection("Your token here");
        String detectionProjectId = imageDetector.createNewDetectionProject();
        String detectionPoolId = imageDetector.createDetectionPool(detectionProjectId, "Pool 1");
        imageDetector.prepareAndUploadDetectionTasks(detectionPoolId);
        imageDetector.startPool(detectionPoolId);
        imageDetector.waitForAllResponses(detectionPoolId);
        imageDetector.getResults(detectionPoolId);
    }

}
