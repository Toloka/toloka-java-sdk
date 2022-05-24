# Text recognition

To run code below use [class TextRecognition](../src/main/java/TextRecognition.java).

We have a set of water meter images. We need to get each water meter’s readings. We ask performers to look at the 
images and write down the digits on each water meter.

To get acquainted with Toloka tools for free, you can use the promo code **TOLOKAKIT1** on $20 on 
your [profile page](https://toloka.yandex.com/requester/profile?utm_source=github&utm_medium=site&utm_campaign=tolokakit) after registration.

Сreate toloka-client factory instance. All api calls will go through it.

```java
var clientFactory = new TolokaClientFactoryImpl(DefaultHttpClientConfiguration.DEFAULT_TOLOKA_PROD_URI,
                                                "Your token here");
```

Switch `DefaultHttpClientConfiguration.DEFAULT_TOLOKA_PROD_URI` to `DefaultHttpClientConfiguration.DEFAULT_TOLOKA_SANDBOX_URI`
to use [Sandbox](https://toloka.ai/docs/guide/concepts/sandbox.html?lang=en).

## Creating new project
Enter a clear project name and description.
> The project name and description will be visible to the performers.

````java
String name = "Write down the digits in an image";
String description = "Look at the image and write down the digits shown on the water meter.";
````
Create task interface.

- Read about configuring the [task interface](https://toloka.ai/docs/guide/reference/interface-spec.html?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in the Requester’s Guide.
- Check the [Interfaces section](https://toloka.ai/knowledgebase/interface?utm_source=github&utm_medium=site&utm_campaign=tolokakit) of our Knowledge Base for more tips on interface design.
- Read more about the [Template builder](https://toloka.ai/docs/template-builder/index.html?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in the Requester’s Guide.

````java
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
````

Set data specification. And set task interface to project.
> Specifications are a description of input data that will be used in a project and the output data that will be collected from the performers.

Read more about [input and output data specifications](https://yandex.ru/support/toloka-tb/operations/create-specs.html?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in the Requester’s Guide.

````java
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
````

Write short and clear instructions.

> Though the task itself is simple, be sure to add examples for non-obvious cases (like when there are no red digits on an image). This helps to eliminate noise in the labels.

Get more tips on designing [instructions](https://toloka.ai/knowledgebase/instruction?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in our Knowledge Base.

````java
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
````

Create a project.

````java
Project project = new Project(
        name,
        description,
        instructions,
        taskSpecification,
        AssignmentsIssuingType.AUTOMATED);
ModificationResult<Project> projectCreationResult = clientFactory.getProjectClient().createProject(project);
String projectId = projectCreationResult.getResult().getId();
````
Link to open in web interface: `https://toloka.yandex.com/requester/project/<project_id>`

## Preparing data

This example uses [Toloka WaterMeters](https://toloka.ai/datasets?utm_source=github&utm_medium=site&utm_campaign=tolokakit) dataset collected by Roman Kucev.

Upload dataset into file `dataset_text_recognition.tsv`.
```bash
!curl https://s3.mds.yandex.net/tlk/dataset/TlkWaterMeters/data.tsv --output dataset_text_recognition.tsv
```

In code below function `readTsv` is used for getting data from `dataset_text_recognition.tsv`. 
It takes file name and returns `List<Map<String, String>>`, where each map keys are column names in file
and values are values in corresponding row.

A variant of implementation `readTsv` can be found in [class TextRecognition](../src/main/java/TextRecognition.java)

## Create a training pool
> Training is an essential part of almost every crowdsourcing project. It allows you to select performers who have really mastered the task, and thus improve quality. Training is also a great tool for scaling your task because you can run it any time you need new performers.

Read more about [selecting performers](https://toloka.ai/knowledgebase/quality-control?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in our Knowledge Base.

````java
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
````

Link to open in web interface `https://toloka.yandex.com/requester/project/projectId/training/trainingId`

## Upload training tasks to the pool.
Training task must contain ground truth and hint about how to perform it.

> It’s important to include examples for all сases in the training. Make sure the training set is balanced and 
> the comments explain why an answer is correct. Don’t just name the correct answers.

````java
List<Map<String, String>> dataset = TextRecognition.readTsv("dataset_text_recognition.tsv");

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
````

## Create the main pool
A pool is a set of paid tasks grouped into task pages. These tasks are sent out for completion at the same time.

> All tasks within a pool have the same settings (price, quality control, etc.)


```java
Pool pool = new Pool(projectId, 
        "private_name",                                      // Give the pool any convenient name. You are the only one who will see it.
        false,                                               // Specify, if pool may contain adult content
        Date.from(Instant.now().plus(365, ChronoUnit.DAYS)), // Set expiration date
        BigDecimal.valueOf(0.01),                            // Set the price per task page.
        600,                                                 // Time allowed for completing a task page
        true,                                                // Allow auto-acceptence for solutions or not
        new PoolDefaults(3)                                  // Overlap. This is the number of users who will complete the same task.
);
```

- Read more about [pricing principles](https://toloka.ai/knowledgebase/pricing?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in our Knowledge Base.
- To understand [how overlap works](https://toloka.ai/docs/guide/concepts/mvote.html?utm_source=github&utm_medium=site&utm_campaign=tolokakit), go to the Requester’s Guide.
- To understand how much time it should take to complete a task suite, try doing it yourself.

Attach the training you created earlier and select the accuracy level that is required to reach the main pool.

````java
var trainingRequirement = new TrainingRequirement(trainingPoolId, 75);
````

Select English-speaking performers

````java
pool.setFilter(new Connective.And(List.of(new Expression.Languages(ArrayInclusionOperator.IN, LangIso639.EN))));
````

Set up [Quality control](https://toloka.ai/docs/guide/concepts/control.html?utm_source=github&utm_medium=site&utm_campaign=tolokakit). Ban performers who give incorrect responses to control tasks.

> Since tasks such as these have an answer that can be used as ground truth, we can use standard quality control rules like golden sets.

Read more about [quality control principles](https://toloka.ai/knowledgebase/quality-control?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in our Knowledge Base or check out [control tasks settings](https://toloka.ai/docs/guide/concepts/goldenset.html?utm_source=github&utm_medium=site&utm_campaign=tolokakit) in the Requester’s Guide.answer

Evaluate performers based on their responses to control tasks. 
You can assign a skill value or ban performers who make too many mistakes.

If the percentage of correct control responses to control tasks is < 80 and the number of control responses >= 3, 
restrict access to the project for 2 days.

````java
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
QualityControlConfig goldenSetControlConfig = new QualityControlConfig(
        new CollectorConfig.GoldenSet(),
        List.of(goldenSetRuleConfig));
````


Restrict access for performers who submit assignments too fast. 
Set the minimum number of seconds per task suite and choose the type of restriction.


If the number of fast responses is >= 1, restrict access to the project for 2 days.

````java
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

QualityControlConfig assignmentSubmitTimeControlConfig = new QualityControlConfig(
        new CollectorConfig.AssignmentSubmitTime(
                new CollectorConfig.AssignmentSubmitTime.Parameters(5, 7)
        ),
        List.of(assignmentSubmitTimeRuleConfig));
````

```java
QualityControl qualityControl = new QualityControl(List.of(
        goldenSetControlConfig,
        assignmentSubmitTimeControlConfig
));
pool.setQualityControl(qualityControl);
```

Specify	the number of tasks per page. For example: 3 main tasks and 1 control task.

> We recommend putting as many tasks on one page as a performer can complete in 1 to 5 minutes. That way, performers are less likely to get tired, and they won’t lose a significant amount of data if a technical issue occurs.

To learn more about [grouping tasks](https://toloka.ai/docs/search/?utm_source=github&utm_medium=site&utm_campaign=tolokakit&query=smart+mixing) into suites, read the Requester’s Guide.

````java
pool.setMixerConfig(new MixerConfig(
        3,  // real tasks count
        1,  // golden tasks count
        0   // training tasks count
 ));
````

Create pool.

````java
ModificationResult<Pool> poolCreationResult = clientFactory.getPoolClient().createPool(pool); 
String poolId = poolCreationResult.getResult().getId();
````

Link to open in web interface `https://toloka.yandex.com/requester/project/projectId/pool/poolId`

## Uploading tasks

Create control tasks. In small pools, control tasks should account for 10–20% of all tasks.

> Control tasks are tasks that already contain the correct response. They are used for checking the quality of responses from performers. The performer's response is compared to the response you provided. If they match, it means the performer answered correctly.
> Make sure to include different variations of correct responses in equal amounts.

To learn more about [creating control tasks](https://toloka.ai/docs/guide/concepts/task_markup.html?utm_source=github&utm_medium=site&utm_campaign=tolokakit), go to the Requester’s Guide.tasks

````java
List<Map<String, String>> dataset = TextRecognition.readTsv("dataset_text_recognition.tsv");

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
````

## Start the pool.

**Important.** Remember that real Toloka performers will complete the tasks.
Double check that everything is correct
with your project configuration before you start the pool

````java
clientFactory.getPoolClient().openPool(poolId);
clientFactory.getTrainingClient().openTraining(trainingPoolId);
````

## Receiving responses

Wait until the pool is completed.

```java
Pool pool = clientFactory.getPoolClient().getPool(poolId);
while (pool.getStatus() != PoolStatus.CLOSED) {
    try {
        Thread.sleep(1000 * 60);
    } catch (InterruptedException ignored) {
    }
    pool = clientFactory.getPoolClient().getPool(poolId);
}
```

In this example we can not use Dawid-Skene model or aggregation by skill, because responses are not categorical.

You can use [Crowd-Kit](https://toloka.ai/en/docs/crowd-kit/) to see more aggregation algorithms. For textual responses it is
recommended to use [ROVER](https://toloka.ai/en/docs/crowd-kit/reference/crowdkit.aggregation.texts.rover.ROVER)

By using `toloka-java-sdk` you can get all responses.

````java
var assigmentClient = clientFactory.getAssignmentClient();
var assigmentSearchRequest = AssignmentSearchRequest.make()
        .filter().byStatus(AssignmentStatus.ACCEPTED).byPoolId(poolId)
        .and()
        .done();
var searchAssigmentResult = assigmentClient.findAssignments(assigmentSearchRequest);
Map<String, List<String>> responses = new HashMap<>();
for (var item: searchAssigmentResult.getItems()) {
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
````

`responses` is a `Map<String, List<String>>` where key is image url and value is a list of solutions for this task. 