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
import ai.toloka.client.v1.aggregatedsolutions.AggregatedSolutionSearchRequest;
import ai.toloka.client.v1.aggregatedsolutions.DawidSkenePoolAggregatedSolutionRequest;
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
import ai.toloka.client.v1.project.AssignmentsIssuingType;
import ai.toloka.client.v1.project.Project;
import ai.toloka.client.v1.project.spec.FieldSpec;
import ai.toloka.client.v1.project.spec.TaskSpec;
import ai.toloka.client.v1.project.spec.TbTaskViewSpec;
import ai.toloka.client.v1.task.KnownSolution;
import ai.toloka.client.v1.task.Task;
import ai.toloka.client.v1.task.TaskCreateRequestParameters;
import ai.toloka.client.v1.task.TaskSearchRequest;
import ai.toloka.client.v1.userrestriction.DurationUnit;
import ai.toloka.client.v1.userrestriction.UserRestrictionScope;


/**
 * Example of using toloka-java-sdk for image classifications.
 * To find details read
 *
 * @see <a href="https://github.com/JaneKirillova/toloka-java-sdk/tree/example_image_classification/examples/image_classification">
 *          README.md</a>
 */

public class ImageClassification {
    private static final Logger LOGGER = Logger.getLogger(ImageClassification.class.getName());
    private final TolokaClientFactoryImpl clientFactory;

    public ImageClassification(String token) {
        clientFactory = new TolokaClientFactoryImpl(
                                DefaultHttpClientConfiguration.DEFAULT_TOLOKA_PROD_URI,
                                token);
    }

    private static List<Map<String, String>> readTsv(String file) throws IOException {
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

    private QualityControlConfig createIncomeSumControlConfig() {
        RuleConfig incomeSumRuleConfig = new RuleConfig(
                List.of(new RuleCondition.IncomeSumForLast24Hours(CompareOperator.GTE, 20.0)),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.PROJECT,
                                1, DurationUnit.DAY,
                                "No need more answers from this performer")
                ));

        return new QualityControlConfig(
                        new CollectorConfig.Income(),
                        List.of(incomeSumRuleConfig)
        );

    }

    private QualityControlConfig createSkippedInRowControlConfig() {
        RuleConfig skippedInRowRuleConfig = new RuleConfig(
                List.of(new RuleCondition.SkippedInRowCount(CompareOperator.GTE, 10)),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.PROJECT,
                                1, DurationUnit.DAY,
                                "Lazy performer")
                ));

        return new QualityControlConfig(
                        new CollectorConfig.SkippedInRowAssignments(),
                        List.of(skippedInRowRuleConfig)
        );
    }

    private QualityControlConfig createGoldenSetControlConfig() {
        RuleConfig goldenSetRuleConfig = new RuleConfig(
                List.of(
                        new RuleCondition.GoldenSetCorrectAnswersRate(CompareOperator.LT, 60.0),
                        new RuleCondition.GoldenSetAnswersCount(CompareOperator.GTE, 3)
                ),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.PROJECT,
                                10, DurationUnit.DAY,
                                "Golden set")
                ));

        return new QualityControlConfig(
                        new CollectorConfig.GoldenSet(),
                        List.of(goldenSetRuleConfig)
        );
    }

    private QualityControlConfig createMajorityVoteControlConfig() {
        RuleConfig majorityVoteRuleConfig = new RuleConfig(
                List.of(
                        new RuleCondition.TotalAnswersCount(CompareOperator.GTE, 4),
                        new RuleCondition.CorrectAnswersRate(CompareOperator.LT, 75.0)
                ),
                new RuleAction.RestrictionV2(
                        new RuleAction.RestrictionV2.Parameters(
                                UserRestrictionScope.PROJECT,
                                10, DurationUnit.DAY,
                                "Too low quality")
                ));

        CollectorConfig.MajorityVote.Parameters majorityVoteParameters = new CollectorConfig.MajorityVote.Parameters(2);
        majorityVoteParameters.setHistorySize(10);
        return new QualityControlConfig(
                        new CollectorConfig.MajorityVote(majorityVoteParameters),
                        List.of(majorityVoteRuleConfig)
        );
    }

    public String createNewProject() {
        String name = "Is it a cat or a dog?";
        String description = "Look at the picture and decide whether there is a cat or a dog.";
        String config = "{\n"
                + "  \"view\": {\n"
                + "    \"items\": [\n"
                + "      {\n"
                + "        \"url\": {\n"
                + "          \"path\": \"image\",\n"
                + "          \"type\": \"data.input\"\n"
                + "        },\n"
                + "        \"ratio\": [\n"
                + "          1,\n"
                + "          1\n"
                + "        ],\n"
                + "        \"rotatable\": true,\n"
                + "        \"type\": \"view.image\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"data\": {\n"
                + "          \"path\": \"result\",\n"
                + "          \"type\": \"data.output\"\n"
                + "        },\n"
                + "        \"options\": [\n"
                + "          {\n"
                + "            \"value\": \"cat\",\n"
                + "            \"label\": \"Cat\"\n"
                + "          },\n"
                + "          {\n"
                + "            \"value\": \"dog\",\n"
                + "            \"label\": \"Dog\"\n"
                + "          },\n"
                + "          {\n"
                + "            \"value\": \"other\",\n"
                + "            \"label\": \"Other\"\n"
                + "          }\n"
                + "        ],\n"
                + "        \"validation\": {\n"
                + "          \"hint\": \"choose one of the options\",\n"
                + "          \"type\": \"condition.required\"\n"
                + "        },\n"
                + "        \"type\": \"field.button-radio-group\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"type\": \"view.list\"\n"
                + "  },\n"
                + "  \"plugins\": [\n"
                + "    {\n"
                + "      \"layout\": {\n"
                + "        \"kind\": \"scroll\",\n"
                + "        \"taskWidth\": 500\n"
                + "      },\n"
                + "      \"type\": \"plugin.toloka\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"1\": {\n"
                + "        \"data\": {\n"
                + "          \"path\": \"result\",\n"
                + "          \"type\": \"data.output\"\n"
                + "        },\n"
                + "        \"payload\": \"cat\",\n"
                + "        \"type\": \"action.set\"\n"
                + "      },\n"
                + "      \"2\": {\n"
                + "        \"data\": {\n"
                + "          \"path\": \"result\",\n"
                + "          \"type\": \"data.output\"\n"
                + "        },\n"
                + "        \"payload\": \"dog\",\n"
                + "        \"type\": \"action.set\"\n"
                + "      },\n"
                + "      \"3\": {\n"
                + "        \"data\": {\n"
                + "          \"path\": \"result\",\n"
                + "          \"type\": \"data.output\"\n"
                + "        },\n"
                + "        \"payload\": \"other\",\n"
                + "        \"type\": \"action.set\"\n"
                + "      },\n"
                + "      \"type\": \"plugin.hotkeys\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";

        Map<String, FieldSpec> inputSpec = Map.of("image", new FieldSpec.UrlSpec(true));
        Map<String, FieldSpec> outputSpec = Map.of("result", new FieldSpec.StringSpec(true));
        var viewSpec = new TbTaskViewSpec(Map.of());
        viewSpec.setConfig(config);
        var lock = Map.of("core", "1.0.0",
                                        "view.image", "1.0.0",
                                        "condition.required", "1.0.0",
                                        "field.button-radio-group", "1.0.0",
                                        "view.list", "1.0.0",
                                        "plugin.toloka", "1.0.0",
                                        "action.set", "1.0.0",
                                        "plugin.hotkeys", "1.0.0");
        viewSpec.setLock(lock);
        TaskSpec taskSpecification = new TaskSpec(inputSpec, outputSpec, viewSpec);
        String instructions = "<p>Decide what category the image belongs to.</p>\n"
                + "<p>Select \"<b>Cat</b>\" if the picture contains one or more cats.</p>\n"
                + "<p>Select \"<b>Dog</b>\" if the picture contains one or more dogs.</p>\n"
                + "<p>Select \"<b>Other</b>\" if:</p>\n"
                + "<ul><li>the picture contains both cats and dogs</li>\n"
                + "<li>the picture is a picture of animals other than cats and dogs</li>\n"
                + "<li>it is not clear whether the picture is of a cat or a dog</li>\n"
                + "</ul>";

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


    public String createPool(String projectId, String poolName) {
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

        QualityControl qualityControl = new QualityControl(List.of(
                createIncomeSumControlConfig(),
                createSkippedInRowControlConfig(),
                createMajorityVoteControlConfig(),
                createGoldenSetControlConfig()
        ));
        pool.setQualityControl(qualityControl);

        pool.setMixerConfig(new MixerConfig(
                9,  // real tasks count
                1,  // golden tasks count
                0   // training tasks count
        ));

        ModificationResult<Pool> poolCreationResult = clientFactory.getPoolClient().createPool(pool);
        String poolId = poolCreationResult.getResult().getId();
        LOGGER.log(Level.INFO, "A new pool with ID {0} created", poolId);
        return poolId;
    }

    public void prepareAndUploadTasks(String poolId) {
        List<Map<String, String>> dataset;
        try {
            dataset = ImageClassification.readTsv("examples/src/main/resources/dataset.tsv");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem while reading dataset from file: {0}", e.getMessage());
            return;
        }
        var goldenDataset = dataset.subList(0, 15);
        var taskDataset = dataset.subList(15, dataset.size());

        var goldenTasks = goldenDataset.stream().map(row -> {
            var task = new Task(
                    poolId,
                    Map.of("image", row.get("url")),
                    List.of(new KnownSolution(Map.of("result", row.get("label")), 0.0))
            );
            task.setInfiniteOverlap(true);
            return task;
        }).collect(Collectors.toUnmodifiableList());

        var tasks = taskDataset.stream().map(row -> {
            var task = new Task(poolId, Map.of("image", row.get("url")));
            task.setOverlap(3);
            return task;
        }).collect(Collectors.toUnmodifiableList());

        var taskCreateRequestParameters = new TaskCreateRequestParameters();
        clientFactory.getTaskClient().createTasks(tasks, taskCreateRequestParameters);
        clientFactory.getTaskClient().createTasks(goldenTasks, taskCreateRequestParameters);
        LOGGER.log(Level.INFO, "{0} golden tasks and {1} main tasks are uploaded",
                        new Object[]{goldenDataset.size(), taskDataset.size()});
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

    public Map<String, String> receiveResponses(String poolId) {
        var aggregateSolutionsRequest = new DawidSkenePoolAggregatedSolutionRequest(poolId,
                                            List.of(new DawidSkenePoolAggregatedSolutionRequest.Field("result")));
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

        var solutionSearchRequest = AggregatedSolutionSearchRequest.make().limit(250).done();
        var aggregatedSolutionSearchResult = clientFactory.getAggregatedSolutionClient()
                .findAggregatedSolutions(aggregatedSolutionOperation.getId(), solutionSearchRequest);
        var searchTasksRequest = TaskSearchRequest.make()
                .filter().byPoolId(poolId)
                .and()
                .limit(250)
                .done();
        var tasks = clientFactory.getTaskClient().findTasks(searchTasksRequest);
        Map<String, String> solutions = new HashMap<>();
        for (var task : tasks.getItems()) {
            var aggregatedSolution = aggregatedSolutionSearchResult
                                                                .getItems()
                                                                .stream()
                                                                .filter(item -> item.getTaskId().equals(task.getId()))
                                                                .findAny();
            if (aggregatedSolution.isPresent()) {
                solutions.put((String) task.getInputValues().get("image"),
                                (String) aggregatedSolution.get().getOutputValues().get("result"));
            } else {
                solutions.put((String) task.getInputValues().get("image"), "no answer");
            }
        }
        return solutions;
    }

    public static void main(String[] args) {
        var imageClassifier = new ImageClassification("Your token here");
        String projectId = imageClassifier.createNewProject();
        String poolId = imageClassifier.createPool(projectId, "Pool 1");
        imageClassifier.prepareAndUploadTasks(poolId);
        imageClassifier.startPool(poolId);
        imageClassifier.waitToComplete(poolId);
        var result = imageClassifier.receiveResponses(poolId);
        for (var resultEntry : result.entrySet()) {
            String resultString = resultEntry.getKey() + ": " + resultEntry.getValue();
            LOGGER.log(Level.INFO, resultString);
        }
    }
}
