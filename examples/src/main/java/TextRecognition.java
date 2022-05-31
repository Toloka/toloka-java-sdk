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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ai.toloka.client.v1.LangIso639;
import ai.toloka.client.v1.ModificationResult;
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
import ai.toloka.client.v1.pool.qualitycontrol.CollectorConfig;
import ai.toloka.client.v1.pool.qualitycontrol.QualityControl;
import ai.toloka.client.v1.pool.qualitycontrol.QualityControlConfig;
import ai.toloka.client.v1.pool.qualitycontrol.RuleAction;
import ai.toloka.client.v1.pool.qualitycontrol.RuleCondition;
import ai.toloka.client.v1.pool.qualitycontrol.RuleConfig;
import ai.toloka.client.v1.pool.qualitycontrol.TrainingRequirement;
import ai.toloka.client.v1.project.AssignmentsIssuingType;
import ai.toloka.client.v1.project.Project;
import ai.toloka.client.v1.project.spec.FieldSpec;
import ai.toloka.client.v1.project.spec.TaskSpec;
import ai.toloka.client.v1.project.spec.TbTaskViewSpec;
import ai.toloka.client.v1.task.KnownSolution;
import ai.toloka.client.v1.task.Task;
import ai.toloka.client.v1.task.TaskCreateRequestParameters;
import ai.toloka.client.v1.training.Training;
import ai.toloka.client.v1.userrestriction.DurationUnit;
import ai.toloka.client.v1.userrestriction.UserRestrictionScope;


/**
 * Example of using toloka-java-sdk for text recognition.
 * To find details read
 *
 * @see <a href="https://github.com/Toloka/toloka-java-sdk/tree/master/examples/text_recognition">
 *          README.md</a>
 */

public class TextRecognition {
    private static final Logger LOGGER = Logger.getLogger(ImageClassification.class.getName());
    private final TolokaClientFactoryImpl clientFactory;

    public TextRecognition(String token) {
        clientFactory = new TolokaClientFactoryImpl(
                DefaultHttpClientConfiguration.DEFAULT_TOLOKA_PROD_URI,
                token);
    }

    public static List<Map<String, String>> readTsv(String file) throws IOException {
        String content = Files.readString(Path.of(file), StandardCharsets.US_ASCII);
        List<String> rows = List.of(content.split("\n"));
        List<String> headers = List.of(rows.get(0).split("\t"));
        List<Map<String, String>> result = new LinkedList<>();
        for (int rowNumber = 1; rowNumber < rows.size(); rowNumber++) {
            Map<String, String> currentMap = new HashMap<>();
            List<String> values = List.of(rows.get(rowNumber).split("\t"));
            for (int columnNumber = 0; columnNumber < headers.size(); columnNumber++) {
                currentMap.put(headers.get(columnNumber), values.get(columnNumber));
            }
            result.add(currentMap);
        }
        return result;
    }

    public String createNewProject() {
        String name = "Write down the digits in an image";
        String description = "Look at the image and write down the digits shown on the water meter.";
        String config = "{\n"
                + "    \"view\": {\n"
                + "        \"items\": [\n"
                + "            {\n"
                + "                \"content\": \"1. Look at the image\\n"
                + "2. Find boxes with the numbers\\n"
                + "3. Write down the digits in black section. (Put '0' if there are no digits there)\\n"
                + "4. Put '.'\\n"
                + "5. Write down the digits in red section\",\n"
                + "                \"type\": \"view.markdown\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"url\": {\n"
                + "                    \"path\": \"image_url\",\n"
                + "                    \"type\": \"data.input\"\n"
                + "                },\n"
                + "                \"rotatable\": true,\n"
                + "                \"type\": \"view.image\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"data\": {\n"
                + "                    \"path\": \"value\",\n"
                + "                    \"type\": \"data.output\"\n"
                + "                },\n"
                + "                \"placeholder\": \"Enter value\",\n"
                + "                \"hint\": \"Make sure your format of number is '365.235' or '0.112'\",\n"
                + "                \"label\": \"Write down the digits. Format: 365.235\",\n"
                + "                \"validation\": {\n"
                + "                    \"schema\": {\n"
                + "                        \"type\": \"string\",\n"
                + "                        \"pattern\": \"^\\\\d+\\\\.?\\\\d{0,3}$\",\n"
                + "                        \"minLength\": 1,\n"
                + "                        \"maxLength\": 9\n"
                + "                    },\n"
                + "                    \"type\": \"condition.schema\"\n"
                + "                },\n"
                + "                \"type\": \"field.text\"\n"
                + "            }\n"
                + "        ],\n"
                + "        \"type\": \"view.list\"\n"
                + "    },\n"
                + "    \"plugins\": [\n"
                + "        {\n"
                + "            \"layout\": {\n"
                + "                \"kind\": \"scroll\",\n"
                + "                \"taskWidth\": 600\n"
                + "            },\n"
                + "            \"type\": \"plugin.toloka\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";

        Map<String, FieldSpec> inputSpec = Map.of("image_url", new FieldSpec.UrlSpec(true));
        Map<String, FieldSpec> outputSpec = Map.of("value", new FieldSpec.StringSpec(true));
        var viewSpec = new TbTaskViewSpec(Map.of());
        viewSpec.setConfig(config);
        var lock = Map.of("core", "1.0.0",
                "view.markdown", "1.0.0",
                "view.image", "1.0.0",
                "condition.schema", "1.0.0",
                "field.text", "1.0.0",
                "view.list", "1.0.0",
                "plugin.toloka", "1.0.0");
        viewSpec.setLock(lock);
        TaskSpec taskSpecification = new TaskSpec(inputSpec, outputSpec, viewSpec);
        String instructions = "This task is to solve machine learning problem "
                + "of digit recognition on the image.<br>\n"
                + "The more precise you read the information from the image the more precise would be algorithm<br>\n"
                + "Your contribution here is to get exact information even "
                + "if there a   re any complicated and uncertain cases.<br>\n"
                + "We hope for your skills to solve one of the important science problem.<br><br>\n"
                + "<b>Basic steps:</b><br>\n"
                + "<ul><li>Look at the image and find meter with the numbers in the boxes</li>\n"
                + "<li>Find black numbers/section and red numbers/section</li>\n"
                + "<li>Put black and red numbers separated with '.' to text field</li></ul>";

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

    public String createTrainingPool(String projectId, String trainingName) {
        var training = new Training();
        training.setProjectId(projectId);
        training.setPrivateName(trainingName);
        training.setMayContainAdultContent(false);
        training.setAssignmentMaxDurationSeconds(60 * 10);
        training.setMixTasksInCreationOrder(false);
        training.setShuffleTasksInTaskSuite(false);
        training.setTrainingTasksInTaskSuiteCount(2);
        training.setTaskSuitesRequiredToPass(5);
        training.setRetryTrainingAfterDays(5L);
        training.setInheritedInstructions(true);

        String trainingId = clientFactory.getTrainingClient().createTraining(training).getResult().getId();
        LOGGER.log(Level.INFO, "A new training with ID {0} created", trainingId);
        return trainingId;
    }

    public void prepareAndUploadTrainingTasks(String poolId) {
        List<Map<String, String>> dataset;
        try {
            dataset = TextRecognition.readTsv("examples/src/main/resources/dataset_text_recognition.tsv");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem while reading dataset from file: {0}", e.getMessage());
            return;
        }
        var trainingDataset = dataset.subList(0, 10);

        var tasks = trainingDataset.stream().map(row -> {
            var splittedValues = row.get("value").split("\\.");
            return new Task(poolId,
                    Map.of("image_url", row.get("image_url")),
                    List.of(new KnownSolution(Map.of("value", row.get("value")), 0.0)),
                    "Black section is " + splittedValues[0]
                            + ". Red section is " + splittedValues[1] + ".");
        }).collect(Collectors.toUnmodifiableList());

        var taskCreateRequestParameters = new TaskCreateRequestParameters();
        taskCreateRequestParameters.setAllowDefaults(true);
        clientFactory.getTaskClient().createTasks(tasks, taskCreateRequestParameters);
        LOGGER.log(Level.INFO, "{0} training tasks are uploaded", trainingDataset.size());
    }


    public String createMainPool(String projectId, String poolName, String trainingPoolId) {
        Pool pool = new Pool(projectId,
                poolName,
                false,
                Date.from(Instant.now().plus(365, ChronoUnit.DAYS)),
                BigDecimal.valueOf(0.01),
                600,
                true,
                new PoolDefaults(3)
        );
        pool.setFilter(new Connective.And(List.of(new Expression.Languages(ArrayInclusionOperator.IN, LangIso639.EN))));

        var trainingRequirement = new TrainingRequirement(trainingPoolId, 75);

        QualityControl qualityControl = new QualityControl(List.of(
                createAssignmentSubmitTimeControlConfig(),
                createGoldenSetControlConfig()
        ));
        qualityControl.setTrainingRequirement(trainingRequirement);
        pool.setQualityControl(qualityControl);

        pool.setMixerConfig(new MixerConfig(
                3,  // real tasks count
                1,  // golden tasks count
                0   // training tasks count
        ));

        ModificationResult<Pool> poolCreationResult = clientFactory.getPoolClient().createPool(pool);
        String poolId = poolCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new pool with ID {0} created", poolId);
        return poolId;
    }

    private QualityControlConfig createGoldenSetControlConfig() {
        RuleConfig goldenSetRuleConfig = new RuleConfig(
                List.of(
                        new RuleCondition.GoldenSetCorrectAnswersRate(CompareOperator.LT, 80.0),
                        new RuleCondition.GoldenSetAnswersCount(CompareOperator.GTE, 3)
                ),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.PROJECT,
                                2, DurationUnit.DAY,
                                "Control tasks failed")
                ));

        return new QualityControlConfig(
                new CollectorConfig.GoldenSet(),
                List.of(goldenSetRuleConfig)
        );
    }

    private QualityControlConfig createAssignmentSubmitTimeControlConfig() {
        RuleConfig assignmentSubmitTimeRuleConfig = new RuleConfig(
                List.of(
                        new RuleCondition.FastSubmittedCount(CompareOperator.GTE, 1)
                ),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.PROJECT,
                                2, DurationUnit.DAY,
                                "Fast response")
                ));

        return new QualityControlConfig(
                new CollectorConfig.AssignmentSubmitTime(
                        new CollectorConfig.AssignmentSubmitTime.Parameters(5, 7)
                ),
                List.of(assignmentSubmitTimeRuleConfig)
        );
    }

    public void prepareAndUploadTasks(String poolId) {
        List<Map<String, String>> dataset;
        try {
            dataset = TextRecognition.readTsv("examples/src/main/resources/dataset_text_recognition.tsv");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem while reading dataset from file: {0}", e.getMessage());
            return;
        }
        var goldenDataset = dataset.subList(10, 20);
        var taskDataset = dataset.subList(20, 120);

        var goldenTasks = goldenDataset.stream().map(row -> {
            var task = new Task(
                    poolId,
                    Map.of("image_url", row.get("image_url")),
                    List.of(new KnownSolution(Map.of("value", row.get("value")), 0.0))
            );
            task.setInfiniteOverlap(true);
            return task;
        }).collect(Collectors.toUnmodifiableList());

        var tasks = taskDataset.stream().map(row -> {
            var task = new Task(poolId, Map.of("image_url", row.get("image_url")));
            task.setOverlap(3);
            return task;
        }).collect(Collectors.toUnmodifiableList());

        var taskCreateRequestParameters = new TaskCreateRequestParameters();
        clientFactory.getTaskClient().createTasks(tasks, taskCreateRequestParameters);
        clientFactory.getTaskClient().createTasks(goldenTasks, taskCreateRequestParameters);
        LOGGER.log(Level.INFO, "{0} golden tasks and {1} main tasks are uploaded",
                new Object[]{goldenDataset.size(), taskDataset.size()});
    }

    public void startPools(String poolId, String trainingPoolId) {
        clientFactory.getPoolClient().openPool(poolId);
        clientFactory.getTrainingClient().openTraining(trainingPoolId);
        LOGGER.log(Level.INFO, "Training with ID {0} and pool with ID {1} are started",
                new Object[]{trainingPoolId, poolId});
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

    public Map<String, List<String>> getResponses(String poolId) {
        var assigmentClient = clientFactory.getAssignmentClient();
        var assigmentSearchRequest = AssignmentSearchRequest.make()
                .filter().byStatus(AssignmentStatus.ACCEPTED).byPoolId(poolId)
                .and()
                .done();
        var searchAssigmentResult = assigmentClient.findAssignments(assigmentSearchRequest);
        Map<String, List<String>> responses = new HashMap<>();
        for (var item : searchAssigmentResult.getItems()) {
            var tasks = item.getTasks();
            var solutions = item.getSolutions();
            for (int i = 0; i < tasks.size(); i++) {
                var entry = responses.get(tasks.get(i).getInputValues().get("image_url"));
                if (entry == null) {
                    List<String> newList = new ArrayList<>();
                    newList.add((String) solutions.get(i).getOutputValues().get("value"));
                    responses.put((String) tasks.get(i).getInputValues().get("image_url"), newList);
                    continue;
                }
                entry.add((String) solutions.get(i).getOutputValues().get("value"));
            }
        }
        LOGGER.log(Level.INFO, "Get responses for {0} tasks", responses.size());
        return responses;
    }

    public static void main(String[] args) throws IOException {
        var textRecognitor = new TextRecognition("Your token here");
        String projectId = textRecognitor.createNewProject();
        String trainingId = textRecognitor.createTrainingPool(projectId, "Text recognition training");
        textRecognitor.prepareAndUploadTrainingTasks(trainingId);

        String poolId = textRecognitor.createMainPool(projectId, "Write down the digits in an image.", trainingId);
        textRecognitor.prepareAndUploadTasks(poolId);
        textRecognitor.startPools(poolId, trainingId);
        textRecognitor.waitToComplete(poolId);
        var responses = textRecognitor.getResponses(poolId);
    }
}
