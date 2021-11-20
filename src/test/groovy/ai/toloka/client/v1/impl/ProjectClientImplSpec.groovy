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

package ai.toloka.client.v1.impl

import ai.toloka.client.v1.*
import ai.toloka.client.v1.pool.filter.CompareOperator
import ai.toloka.client.v1.pool.qualitycontrol.*
import ai.toloka.client.v1.project.*
import ai.toloka.client.v1.project.spec.ClassicTaskViewSpec
import ai.toloka.client.v1.project.spec.FieldSpec
import ai.toloka.client.v1.project.spec.TaskSpec
import ai.toloka.client.v1.project.spec.TbTaskViewSpec
import ai.toloka.client.v1.userrestriction.UserRestrictionScope
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient
import org.unitils.reflectionassert.ReflectionComparatorMode

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class ProjectClientImplSpec extends AbstractClientSpec {

    public static final String TEST_UUID = 'daab2575-374a-4006-b285-9760db09795c'

    def "findProjects"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects')
                        .withQueryStringParameters(
                                [
                                        status     : ['ACTIVE'],
                                        id_gt      : ['123'],
                                        created_lte: ['2015-12-09T12:10:00'],
                                        sort       : ['-public_name,id'],
                                        limit      : ['50']
                                ])
                        .withHeader('Authorization', "OAuth abc"), once())
                .respond(response()
                        .withStatusCode(200)
                        .withBody(new JsonBuilder([items: [project_map_with_readonly()], has_more: false]).toString())
                )

        and:
        def taskSpec = new TaskSpec(
                [image: new FieldSpec.UrlSpec(true)],
                [
                        color  : new FieldSpec.ArrayStringSpec(true).with {
                            minSize = 1
                            maxSize = 3
                            allowedValues = ['orange', 'red', 'blue', 'green'] as Set
                            it
                        },
                        comment: new FieldSpec.StringSpec(false).with { maxLength = 2048; it }
                ],
                new ClassicTaskViewSpec([resolution: 1024]).with {
                    markup = """<div class="grid">
	{{#each image}}
		{{#task class="grid_item"}}
			{{img src=../this width=300 height=300}}
			{{field type="checkbox" name="red" label="Red" hotkey="1"}}
		{{/task}}
	{{/each}}
</div>"""; it
                })

        def project = new Project(
                'Choose image color',
                'Look at the picture and choose it\'s dominant color',
                """<p>
Some complex instructions
</p>""",
                taskSpec,
                AssignmentsIssuingType.AUTOMATED).with {
            id = 10
            privateComment = 'Submitted by Joe'
            status = ProjectStatus.ACTIVE
            created = parseDate('2015-12-09 12:10:00')
            it
        }
        def ruLanguage = new ProjectLocalizationConfig.AdditionalLanguage(LangIso639.RU)
        ruLanguage.publicName = new ProjectLocalizationConfig.AdditionalLanguage.LocalizedValue('Выбери цвет картинки', ProjectLocalizationConfig.AdditionalLanguage.Source.REQUESTER)
        ruLanguage.publicDescription = new ProjectLocalizationConfig.AdditionalLanguage.LocalizedValue('Посмотрите на картинку и выберете преобладающий на ней цвет', ProjectLocalizationConfig.AdditionalLanguage.Source.REQUESTER)
        ruLanguage.publicInstructions = new ProjectLocalizationConfig.AdditionalLanguage.LocalizedValue('<p>\nКакая-то сложная инструкция\n</p>', ProjectLocalizationConfig.AdditionalLanguage.Source.REQUESTER)
        project.localizationConfig = new ProjectLocalizationConfig(
                defaultLanguage: LangIso639.EN,
                additionalLanguages: [ruLanguage]
        )

        and:
        def projectRequest = ProjectSearchRequest.make()
                .filter().byStatus(ProjectStatus.ACTIVE)
                .and()
                .range().byId('123').gt().byCreated(parseDate('2015-12-09 12:10:00')).lte()
                .and()
                .sort().byPublicName().desc().byId().asc()
                .and()
                .limit(50)
                .done()

        when:
        def result = factory.projectClient.findProjects(projectRequest)

        then:
        !result.hasMore
        matches result.items.first(), project, ReflectionComparatorMode.LENIENT_ORDER
    }

    def "getProject"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects/10').withHeader('Authorization', "OAuth abc"), once())
                .respond(response()
                        .withStatusCode(200)
                        .withBody(json(new JsonBuilder([
                                id                      : '10',
                                owner                   : [
                                        id        : 'requester-1',
                                        myself    : true,
                                        company_id: '1'
                                ],
                                public_name             : 'Choose image color',
                                public_description      : 'Look at the picture and choose it\'s dominant color',
                                private_comment         : 'Submitted by Joe',
                                public_instructions     : '<p>\nSome complex instructions\n</p>',
                                metadata                : ['projectMetadataKey': ['projectMetadataValue']],
                                task_spec               : [
                                        input_spec : [image: [type: 'url', required: true]],
                                        output_spec: [
                                                color  : [
                                                        type          : 'array_string',
                                                        required      : true,
                                                        min_size      : 1,
                                                        max_size      : 3,
                                                        allowed_values: ['orange', 'red', 'blue', 'green']
                                                ],
                                                comment: [type: 'string', required: false, max_length: 2048]
                                        ],
                                        view_spec  : [
                                                settings: [
                                                        resolution: 1024
                                                ],
                                                markup  : """<div class="grid">
\t{{#each image}}
\t\t{{#task class="grid_item"}}
\t\t\t{{img src=../this width=300 height=300}}
\t\t\t{{field type="checkbox" name="red" label="Red" hotkey="1"}}
\t\t{{/task}}
\t{{/each}}
</div>"""
                                        ]
                                ],
                                assignments_issuing_type: 'AUTOMATED',
                                status                  : 'ACTIVE',
                                created                 : '2015-12-09T12:10:00'
                        ]) as String))
                )

        when:
        def result = factory.projectClient.getProject('10')

        then:
        matches result, new Project(
                'Choose image color',
                'Look at the picture and choose it\'s dominant color',
                """<p>
Some complex instructions
</p>""",
                new TaskSpec(
                        [image: new FieldSpec.UrlSpec(true)],
                        [
                                color  : new FieldSpec.ArrayStringSpec(true).with {
                                    minSize = 1
                                    maxSize = 3
                                    allowedValues = ['orange', 'red', 'blue', 'green'] as Set
                                    it
                                },
                                comment: new FieldSpec.StringSpec(false).with { maxLength = 2048; it }
                        ],
                        new ClassicTaskViewSpec([resolution: 1024])
                                .with {
                                    markup = """<div class="grid">
\t{{#each image}}
\t\t{{#task class="grid_item"}}
\t\t\t{{img src=../this width=300 height=300}}
\t\t\t{{field type="checkbox" name="red" label="Red" hotkey="1"}}
\t\t{{/task}}
\t{{/each}}
</div>"""; it
                                }),
                AssignmentsIssuingType.AUTOMATED).with {
            id = '10'
            it.owner = new Owner(id: 'requester-1', myself: true, companyId: '1')
            privateComment = 'Submitted by Joe'
            metadata = ['projectMetadataKey': ['projectMetadataValue']]
            status = ProjectStatus.ACTIVE
            created = parseDate('2015-12-09 12:10:00')
            it
        }, ReflectionComparatorMode.LENIENT_ORDER
    }

    def "getProject; returns internal server error"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects/10'), once())
                .respond(response()
                        .withStatusCode(500)
                        .withBody("""
                    {
                        "code": "INTERNAL_ERROR",
                        "request_id": "abc-123",
                        "message": "Internal Error",
                        "payload": {
                          "additional_message": "Error details"
                        }
                    }
""")
                )

        when:
        factory.projectClient.getProject('10');

        then:
        def ex = thrown(TlkException)

        matches ex.error, new TlkError(
                'INTERNAL_ERROR', 'abc-123', 'Internal Error', [additional_message: 'Error details'])
    }

    def "getProject; tb_view_spec"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects/10').withHeader('Authorization', "OAuth abc"), once())
                .respond(response()
                        .withStatusCode(200)
                        .withBody(json(new JsonBuilder([
                                id                      : '10',
                                owner                   : [
                                        id        : 'requester-1',
                                        myself    : true,
                                        company_id: '1'
                                ],
                                public_name             : 'Choose image color',
                                public_description      : 'Look at the picture and choose it\'s dominant color',
                                private_comment         : 'Submitted by Joe',
                                public_instructions     : '<p>\nSome complex instructions\n</p>',
                                metadata                : ['projectMetadataKey': ['projectMetadataValue']],
                                task_spec               : [
                                        input_spec : [image: [type: 'url', required: true]],
                                        output_spec: [
                                                color  : [
                                                        type          : 'array_string',
                                                        required      : true,
                                                        min_size      : 1,
                                                        max_size      : 3,
                                                        allowed_values: ['orange', 'red', 'blue', 'green']
                                                ],
                                                comment: [type: 'string', required: false, max_length: 2048]
                                        ],
                                        view_spec  : [
                                                settings: [
                                                        resolution: 1024
                                                ],
                                                type    : "tb",
                                                config  : "my_configuration",
                                                lock    : [
                                                        lock_key : "lock_value"
                                                ],
                                                localizationConfig: [
                                                        keys : [
                                                                [
                                                                        key: 'buttonOne',
                                                                        defaultValue: 'Press me'
                                                                ],
                                                                [
                                                                        key: 'buttonTwo',
                                                                        defaultValue: 'Do not press me'
                                                                ]
                                                        ]
                                                ]
                                        ]
                                ],
                                assignments_issuing_type: 'AUTOMATED',
                                status                  : 'ACTIVE',
                                created                 : '2015-12-09T12:10:00'
                        ]) as String))
                )

        when:
        def result = factory.projectClient.getProject('10')

        then:
        matches result, new Project(
                'Choose image color',
                'Look at the picture and choose it\'s dominant color',
                """<p>
Some complex instructions
</p>""",
                new TaskSpec(
                        [image: new FieldSpec.UrlSpec(true)],
                        [
                                color  : new FieldSpec.ArrayStringSpec(true).with {
                                    minSize = 1
                                    maxSize = 3
                                    allowedValues = ['orange', 'red', 'blue', 'green'] as Set
                                    it
                                },
                                comment: new FieldSpec.StringSpec(false).with { maxLength = 2048; it }
                        ],
                        new TbTaskViewSpec([resolution: 1024]).with {
                            it.config = "my_configuration"
                            it.lock = [
                                    lock_key : "lock_value"
                            ]
                            it.localizationConfig = new TbTaskViewSpec.LocalizationConfig()
                            it.localizationConfig.keys = [new TbTaskViewSpec.LocalizationConfig.Key("buttonOne", "Press me"),
                                                          new TbTaskViewSpec.LocalizationConfig.Key("buttonTwo", "Do not press me")]
                            it
                        }),
                AssignmentsIssuingType.AUTOMATED).with {
            id = '10'
            it.owner = new Owner(id: 'requester-1', myself: true, companyId: '1')
            privateComment = 'Submitted by Joe'
            metadata = ['projectMetadataKey': ['projectMetadataValue']]
            status = ProjectStatus.ACTIVE
            created = parseDate('2015-12-09 12:10:00')
            it
        }, ReflectionComparatorMode.LENIENT_ORDER
    }

    def "createProject"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects')
                        .withMethod('POST')
                        .withHeader('Content-Type', 'application/json; charset=UTF-8')
                        .withBody(json(
                                new JsonBuilder({
                                    public_name 'Map Task'
                                    public_description 'Simple map task'
                                    public_instructions 'Check if company exists'
                                    task_spec {
                                        input_spec {
                                            point {
                                                type 'coordinates'
                                                required true
                                            }
                                            company {
                                                type 'string'
                                                required true
                                            }
                                        }
                                        output_spec {
                                            exists {
                                                type 'boolean'
                                                required true
                                            }
                                        }
                                        view_spec {
                                            markup '<dummy/>'
                                            type 'classic'
                                        }
                                    }
                                    assignments_issuing_type 'MAP_SELECTOR'
                                    assignments_issuing_view_config {
                                        title_template 'Company: {{inputParams[\'company\']}}'
                                        description_template 'Check if company {{inputParams[\'company\']}} exists'
                                    }
                                    metadata(['projectMetadataKey': ['projectMetadataValue']])
                                    localization_config {
                                        default_language 'EN'
                                        additional_languages([
                                                {
                                                    language 'RU'
                                                    public_name {
                                                        value 'Карточное Задание'
                                                        source 'REQUESTER'
                                                    }
                                                }
                                        ])
                                    }
                                    quality_control {
                                        configs([
                                                {
                                                    collector_config {
                                                        type 'ANSWER_COUNT'
                                                    }
                                                    rules([
                                                            {
                                                                action {
                                                                    type 'RESTRICTION'
                                                                    parameters {
                                                                        scope 'POOL'
                                                                    }
                                                                }
                                                                conditions([
                                                                        {
                                                                            key 'assignments_accepted_count'
                                                                            value 42000
                                                                            operator 'GTE'
                                                                        }
                                                                ])
                                                            }
                                                    ])
                                                }
                                        ])
                                    }
                                    assignments_automerge_enabled true
                                    max_active_assignments_count 10
                                }).toString())), once())
                .respond(response()
                        .withStatusCode(201)
                        .withBody(
                                new JsonBuilder({
                                    id '10'
                                    public_name 'Map Task'
                                    public_description 'Simple map task'
                                    public_instructions 'Check if company exists'
                                    task_spec {
                                        input_spec {
                                            point {
                                                type 'coordinates'
                                                required true
                                            }
                                            company {
                                                type 'string'
                                                required true
                                            }
                                        }
                                        output_spec {
                                            exists {
                                                type 'boolean'
                                                required true
                                            }
                                        }
                                        view_spec {
                                            markup '<dummy/>'
                                            type 'classic'
                                        }
                                    }
                                    assignments_issuing_type 'MAP_SELECTOR'
                                    assignments_issuing_view_config {
                                        title_template 'Company: {{inputParams[\'company\']}}'
                                        description_template 'Check if company {{inputParams[\'company\']}} exists'
                                    }
                                    metadata(['projectMetadataKey': ['projectMetadataValue']])
                                    localization_config {
                                        default_language 'EN'
                                        additional_languages([
                                                {
                                                    language 'RU'
                                                    public_name {
                                                        value 'Карточное Задание'
                                                        source 'REQUESTER'
                                                    }
                                                }
                                        ])
                                    }
                                    quality_control {
                                        configs([
                                                {
                                                    collector_config {
                                                        type 'ANSWER_COUNT'
                                                        uuid TEST_UUID
                                                    }
                                                    rules([
                                                            {
                                                                action {
                                                                    type 'RESTRICTION'
                                                                    parameters {
                                                                        scope 'POOL'
                                                                    }
                                                                }
                                                                conditions([
                                                                        {
                                                                            key 'assignments_accepted_count'
                                                                            value 42000
                                                                            operator 'GTE'
                                                                        }
                                                                ])
                                                            }
                                                    ])
                                                }
                                        ])
                                    }
                                    assignments_automerge_enabled true
                                    status 'ACTIVE'
                                    created '2015-12-09T12:10:00'
                                }).toString()))

        when:
        def result = factory.projectClient.createProject(new Project(
                'Map Task',
                'Simple map task',
                'Check if company exists',
                new TaskSpec(
                        [point: new FieldSpec.CoordinatesSpec(true), company: new FieldSpec.StringSpec(true)],
                        [exists: new FieldSpec.BooleanSpec(true)],
                        new ClassicTaskViewSpec(null as Map).with { markup = '<dummy/>'; it }
                ),
                AssignmentsIssuingType.MAP_SELECTOR).with {
            assignmentsIssuingViewConfig = new AssignmentsIssuingViewConfig(
                    titleTemplate: 'Company: {{inputParams[\'company\']}}',
                    descriptionTemplate: 'Check if company {{inputParams[\'company\']}} exists'
            )
            qualityControl = new ProjectQualityControl([
                    new QualityControlConfig(
                            new CollectorConfig.AnswerCount(),
                            [new RuleConfig(
                                    [new RuleCondition.AssignmentsAcceptedCount(CompareOperator.GTE, 42000)],
                                    new RuleAction.Restriction(new RuleAction.Restriction.Parameters(UserRestrictionScope.POOL))
                            )]
                    )
            ])
            assignmentsAutomergeEnabled = true
            maxActiveAssignmentsCount = 10
            metadata = ['projectMetadataKey': ['projectMetadataValue']]
            localizationConfig = new ProjectLocalizationConfig(LangIso639.EN, [
                    new ProjectLocalizationConfig.AdditionalLanguage(
                            language: LangIso639.RU,
                            publicName: new ProjectLocalizationConfig.AdditionalLanguage.LocalizedValue(
                                    'Карточное Задание',
                                    ProjectLocalizationConfig.AdditionalLanguage.Source.REQUESTER
                            )
                    )
            ])
            it
        })

        then:
        result.isNewCreated()
        matches result.result, new Project(
                'Map Task',
                'Simple map task',
                'Check if company exists',
                new TaskSpec(
                        [point: new FieldSpec.CoordinatesSpec(true), company: new FieldSpec.StringSpec(true)],
                        [exists: new FieldSpec.BooleanSpec(true)],
                        new ClassicTaskViewSpec(null as Map).with { markup = '<dummy/>'; it }
                ),
                AssignmentsIssuingType.MAP_SELECTOR).with {
            assignmentsIssuingViewConfig = new AssignmentsIssuingViewConfig(
                    titleTemplate: 'Company: {{inputParams[\'company\']}}',
                    descriptionTemplate: 'Check if company {{inputParams[\'company\']}} exists'
            )
            qualityControl = new ProjectQualityControl([
                    new QualityControlConfig(
                            new CollectorConfig.AnswerCount().with {
                                uuid = UUID.fromString(TEST_UUID)
                                it
                            },
                            [new RuleConfig(
                                    [new RuleCondition.AssignmentsAcceptedCount(CompareOperator.GTE, 42000)],
                                    new RuleAction.Restriction(new RuleAction.Restriction.Parameters(UserRestrictionScope.POOL))
                            )]
                    )
            ])
            assignmentsAutomergeEnabled = true
            metadata = ['projectMetadataKey': ['projectMetadataValue']]
            localizationConfig = new ProjectLocalizationConfig(LangIso639.EN, [
                    new ProjectLocalizationConfig.AdditionalLanguage(
                            language: LangIso639.RU,
                            publicName: new ProjectLocalizationConfig.AdditionalLanguage.LocalizedValue(
                                    'Карточное Задание',
                                    ProjectLocalizationConfig.AdditionalLanguage.Source.REQUESTER
                            )
                    )
            ])
            id = '10'
            status = ProjectStatus.ACTIVE
            created = parseDate('2015-12-09 12:10:00')
            it
        }, ReflectionComparatorMode.LENIENT_ORDER
    }

    def "createProject; check validation errors"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects')
                        .withMethod('POST')
                        .withHeader('Content-Type', 'application/json; charset=UTF-8')
                        .withBody(json(
                                new JsonBuilder(
                                        [
                                                public_name             : 'Map Task',
                                                task_spec               : [
                                                        input_spec : [input: [type: 'boolean', required: true]],
                                                        output_spec: [output: [type: 'boolean', required: true]],
                                                        view_spec  : [markup: '<dummy/>']
                                                ],
                                                assignments_issuing_type: 'AUTOMATED'
                                        ]
                                ) as String)), once())
                .respond(response()
                        .withStatusCode(400)
                        .withBody(
                                new JsonBuilder({
                                    code 'VALIDATION_ERROR'
                                    message 'Validation error'
                                    request_id 'abc-123'
                                    payload {
                                        public_description {
                                            code 'VALUE_REQUIRED'
                                            message 'Value must be present'
                                        }
                                        task_spec {
                                            code 'OBJECT_EXPECTED'
                                            message 'Value must be an object'
                                        }
                                    }
                                }).toString()))

        when:
        factory.projectClient.createProject(
                new Project(
                        'Map Task',
                        null,
                        null,
                        new TaskSpec(
                                [input: new FieldSpec.BooleanSpec(true)],
                                [output: new FieldSpec.BooleanSpec(true)],
                                new ClassicTaskViewSpec(null as Map).with { markup = '<dummy/>'; it }),
                        AssignmentsIssuingType.AUTOMATED))

        then:
        def ex = thrown ValidationException

        matches ex.validationPayload, [
                public_description: new FieldValidationError('VALUE_REQUIRED', 'Value must be present', []),
                task_spec         : new FieldValidationError('OBJECT_EXPECTED', 'Value must be an object', [])
        ]
    }

    def "updateProject"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects/10')
                        .withMethod('PUT')
                        .withHeader('Content-Type', 'application/json; charset=UTF-8')
                        .withBody(json(
                                new JsonBuilder({
                                    public_name 'Map Task'
                                    public_description 'Simple map task'
                                    public_instructions 'Check if company exists'
                                    task_spec {
                                        input_spec {
                                            point {
                                                type 'coordinates'
                                                required true
                                            }
                                            company {
                                                type 'string'
                                                required true
                                            }
                                            hidden {
                                                type 'string'
                                                hidden true
                                            }
                                        }
                                        output_spec {
                                            exists {
                                                type 'boolean'
                                                required true
                                            }
                                        }
                                        view_spec {
                                            markup '<dummy/>'
                                            type 'classic'
                                        }
                                    }
                                    metadata(['projectMetadataKey': ['projectMetadataValue']])
                                    assignments_issuing_type 'MAP_SELECTOR'
                                    assignments_issuing_view_config {
                                        title_template 'Company: {{inputParams[\'company\']}}'
                                        description_template 'Check if company {{inputParams[\'company\']}} exists'
                                    }
                                }).toString())), once())
                .respond(response()
                        .withStatusCode(200)
                        .withBody(
                                new JsonBuilder({
                                    id '10'
                                    public_name 'Map Task'
                                    public_description 'Simple map task'
                                    public_instructions 'Check if company exists'
                                    task_spec {
                                        input_spec {
                                            point {
                                                type 'coordinates'
                                                required true
                                            }
                                            company {
                                                type 'string'
                                                required true
                                            }
                                            hidden {
                                                type 'string'
                                                hidden true
                                            }
                                        }
                                        output_spec {
                                            exists {
                                                type 'boolean'
                                                required true
                                            }
                                        }
                                        view_spec {
                                            markup '<dummy/>'
                                            type 'classic'
                                        }
                                    }
                                    metadata(['projectMetadataKey': ['projectMetadataValue']])
                                    assignments_issuing_type 'MAP_SELECTOR'
                                    assignments_issuing_view_config {
                                        title_template 'Company: {{inputParams[\'company\']}}'
                                        description_template 'Check if company {{inputParams[\'company\']}} exists'
                                    }
                                    status 'ACTIVE'
                                    created '2015-12-09T12:10:00'
                                    max_active_assignments_count 5
                                }).toString()))

        when:
        def result = factory.projectClient.updateProject('10', new Project(
                'Map Task',
                'Simple map task',
                'Check if company exists',
                new TaskSpec(
                        [point  : new FieldSpec.CoordinatesSpec(true),
                         company: new FieldSpec.StringSpec(true),
                         hidden : new FieldSpec.StringSpec(false, true)],
                        [exists: new FieldSpec.BooleanSpec(true)],
                        new ClassicTaskViewSpec(null as Map).with { markup = '<dummy/>'; it }
                ),
                AssignmentsIssuingType.MAP_SELECTOR).with {
            assignmentsIssuingViewConfig = new AssignmentsIssuingViewConfig(
                    titleTemplate: 'Company: {{inputParams[\'company\']}}',
                    descriptionTemplate: 'Check if company {{inputParams[\'company\']}} exists'
            )
            maxActiveAssignmentsCount = 5
            metadata = ['projectMetadataKey': ['projectMetadataValue']]
            it
        })

        then:
        !result.isNewCreated()
        matches result.result, new Project(
                'Map Task',
                'Simple map task',
                'Check if company exists',
                new TaskSpec(
                        [point  : new FieldSpec.CoordinatesSpec(true),
                         company: new FieldSpec.StringSpec(true),
                         hidden : new FieldSpec.StringSpec(false, true)],
                        [exists: new FieldSpec.BooleanSpec(true)],
                        new ClassicTaskViewSpec(null as Map).with { markup = '<dummy/>'; it }
                ),
                AssignmentsIssuingType.MAP_SELECTOR).with {
            assignmentsIssuingViewConfig = new AssignmentsIssuingViewConfig(
                    titleTemplate: 'Company: {{inputParams[\'company\']}}',
                    descriptionTemplate: 'Check if company {{inputParams[\'company\']}} exists'
            )
            id = '10'
            status = ProjectStatus.ACTIVE
            created = parseDate('2015-12-09 12:10:00')
            maxActiveAssignmentsCount = 5
            metadata = ['projectMetadataKey': ['projectMetadataValue']]
            it
        }, ReflectionComparatorMode.LENIENT_ORDER
    }

    def "archiveProject"() {
        setup:
        def operation_map = [
                id        : 'archive-project-op-1',
                type      : 'PROJECT.ARCHIVE',
                status    : 'SUCCESS',
                submitted : '2016-10-21T15:37:00',
                started   : '2016-10-21T15:37:01',
                finished  : '2016-10-21T15:37:02',
                parameters: [
                        project_id: '10'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/projects/10/archive').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.projectClient.archiveProject('10')

        then:
        matches result, new ProjectArchiveOperation(
                id: 'archive-project-op-1',
                type: ai.toloka.client.v1.operation.OperationType.PROJECT_ARCHIVE,
                status: ai.toloka.client.v1.operation.OperationStatus.SUCCESS,
                submitted: parseDate('2016-10-21 15:37:00'),
                started: parseDate('2016-10-21 15:37:01'),
                finished: parseDate('2016-10-21 15:37:02'),
                parameters: new ProjectArchiveOperation.Parameters(projectId: '10'),
                operationClient: factory.operationClient
        )
    }

    def project_map_with_readonly() {
        return [
                id                      : 10,
                public_name             : 'Choose image color',
                public_description      : 'Look at the picture and choose it\'s dominant color',
                private_comment         : 'Submitted by Joe',
                public_instructions     : '<p>\nSome complex instructions\n</p>',
                task_spec               : [
                        input_spec : [
                                image: [
                                        type    : 'url',
                                        required: true
                                ]
                        ],
                        output_spec: [
                                color  : [
                                        type          : 'array_string',
                                        required      : true,
                                        min_size      : 1,
                                        max_size      : 3,
                                        allowed_values: ['orange', 'red', 'blue', 'green']
                                ],
                                comment: [
                                        type      : 'string',
                                        required  : false,
                                        max_length: 2048
                                ]
                        ],
                        view_spec  : [
                                settings: [
                                        resolution: 1024
                                ],
                                type    : "classic",
                                markup  : """<div class="grid">
\t{{#each image}}
\t\t{{#task class="grid_item"}}
\t\t\t{{img src=../this width=300 height=300}}
\t\t\t{{field type="checkbox" name="red" label="Red" hotkey="1"}}
\t\t{{/task}}
\t{{/each}}
</div>"""
                        ]
                ],
                assignments_issuing_type: 'AUTOMATED',
                status                  : 'ACTIVE',
                created                 : '2015-12-09T12:10:00',
                localization_config     : [
                        default_language    : 'EN',
                        additional_languages: [
                                [
                                        language           : 'RU',
                                        public_name        : [
                                                value : 'Выбери цвет картинки',
                                                source: 'REQUESTER'
                                        ],
                                        public_description : [
                                                value : 'Посмотрите на картинку и выберете преобладающий на ней цвет',
                                                source: 'REQUESTER'
                                        ],
                                        public_instructions: [
                                                value : '<p>\nКакая-то сложная инструкция\n</p>',
                                                source: 'REQUESTER'
                                        ]
                                ]
                        ]
                ]
        ]
    }
}
