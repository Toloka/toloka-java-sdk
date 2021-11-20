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
import ai.toloka.client.v1.operation.OperationStatus
import ai.toloka.client.v1.operation.OperationType
import ai.toloka.client.v1.pool.*
import ai.toloka.client.v1.pool.dynamicoverlap.BasicDynamicOverlapConfig
import ai.toloka.client.v1.pool.filter.*
import ai.toloka.client.v1.pool.qualitycontrol.*
import ai.toloka.client.v1.userrestriction.UserRestrictionScope
import com.google.common.collect.ImmutableList
import groovy.json.JsonBuilder
import org.mockserver.client.server.MockServerClient
import spock.lang.Unroll

import static org.mockserver.matchers.Times.once
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.JsonBody.json

class PoolClientImplSpec extends AbstractClientSpec {

    def "findPools"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools')
                        .withQueryStringParameters([
                                project_id     : ['10'],
                                id_gt          : ['20'],
                                last_started_lt: ['2016-03-23T12:59:00'],
                                sort           : ['-created,id']
                        ]), once())
                .respond(response(new JsonBuilder([items: [pool_map_with_readonly()], has_more: true]).toString()))

        and:
        def request = PoolSearchRequest.make()
                .filter().by(PoolFilterParam.projectId, '10')
                .and()
                .range()
                .by(PoolRangeParam.id, '20', RangeOperator.gt)
                .by(PoolRangeParam.lastStarted, parseDate('2016-03-23 12:59:00'), RangeOperator.lt)
                .and()
                .sort().by(PoolSortParam.created, SortDirection.desc).by(PoolSortParam.id, SortDirection.asc)
                .and()
                .done()

        when:
        def result = factory.poolClient.findPools(request)

        then:
        result.hasMore
        matches result.items.first(), pool_with_readonly()
    }

    def "getPool"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21'), once())
                .respond(response(new JsonBuilder(pool_map_with_readonly()).toString()))

        when:
        def result = factory.poolClient.getPool('21')

        then:
        matches result, pool_with_readonly()
    }

    def "getTraining"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/22'), once())
                .respond(response(new JsonBuilder(training_map()).toString()))

        when:
        def result = factory.poolClient.getPool('22')

        then:
        matches result, training()
    }

    def "createPool"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools').withBody(json(new JsonBuilder(pool_map()).toString())), once())
                .respond(response(new JsonBuilder(pool_map_with_readonly()).toString()).withStatusCode(201))

        when:
        def result = factory.poolClient.createPool(pool())

        then:
        matches result.result, pool_with_readonly()

    }

    def "createPool; check all filters"() {
        setup:
        def pool_map = pool_map_with_readonly().with {
            it.filter = [
                    and: [
                            [
                                    or: [
                                            [category: 'profile', key: 'gender', operator: 'EQ', value: 'FEMALE'],
                                            [category: 'profile', key: 'country', operator: 'NE', value: 'BE']
                                    ]
                            ],
                            [category: 'profile', key: 'citizenship', operator: 'EQ', value: 'BY'],
                            [category: 'profile', key: 'education', operator: 'EQ', value: 'MIDDLE'],
                            [category: 'profile', key: 'adult_allowed', operator: 'EQ', value: true],
                            [category: 'profile', key: 'date_of_birth', operator: 'GT', value: 604972800],
                            [category: 'profile', key: 'city', operator: 'NOT_IN', value: 225],
                            [category: 'profile', key: 'languages', operator: 'IN', value: 'RU'],
                            [category: 'profile', key: 'verified', operator: 'EQ', value: true],
                            [
                                    and: [
                                            [category: 'computed', key: 'region_by_phone', operator: 'IN', value: 213],
                                            [category: 'computed', key: 'region_by_ip', operator: 'NOT_IN', value: 1]
                                    ]
                            ],
                            [category: 'computed', key: 'device_category', operator: 'EQ', value: 'PERSONAL_COMPUTER'],
                            [category: 'computed', key: 'os_family', operator: 'EQ', value: 'WINDOWS'],
                            [category: 'computed', key: 'os_version', operator: 'GTE', value: 8.1],
                            [category: 'computed', key: 'os_version_major', operator: 'GT', value: 8],
                            [category: 'computed', key: 'os_version_minor', operator: 'GTE', value: 1],
                            [category: 'computed', key: 'os_version_bugfix', operator: 'LTE', value: 225],
                            [category: 'computed', key: 'user_agent_type', operator: 'EQ', value: 'BROWSER'],
                            [category: 'computed', key: 'user_agent_family', operator: 'NE', value: 'OPERA'],
                            [category: 'computed', key: 'user_agent_version', operator: 'LT', value: 11.12],
                            [category: 'computed', key: 'user_agent_version_major', operator: 'LT', value: 11],
                            [category: 'computed', key: 'user_agent_version_minor', operator: 'LT', value: 12],
                            [category: 'computed', key: 'user_agent_version_bugfix', operator: 'GT', value: 2026],
                            [category: 'computed', key: 'rating', operator: 'GTE', value: 885.15],
                            [
                                    or: [
                                            [category: 'skill', key: '224', operator: 'GTE', value: 85],
                                            [category: 'skill', key: '300', operator: 'NE', value: null],
                                            [category: 'skill', key: '350', operator: 'EQ', value: 75.512]
                                    ]
                            ]
                    ]
            ]
            it
        }

        def pool = pool_with_readonly().with {
            it.filter = new Connective.And([
                    new Connective.Or([
                            new Expression.Gender(IdentityOperator.EQ, Gender.FEMALE),
                            new Expression.Country(IdentityOperator.NE, CountryIso3166.BE)
                    ]),
                    new Expression.Citizenship(IdentityOperator.EQ, CountryIso3166.BY),
                    new Expression.Education(IdentityOperator.EQ, Education.MIDDLE),
                    new Expression.AdultAllowed(IdentityOperator.EQ, true),
                    new Expression.DateOfBirth(CompareOperator.GT, 604972800),
                    new Expression.City(RegionCompareOperator.NOT_IN, new Region(225)),
                    new Expression.Languages(ArrayInclusionOperator.IN, LangIso639.RU),
                    new Expression.Verified(IdentityOperator.EQ, true),
                    new Connective.And([
                            new Expression.RegionByPhone(RegionCompareOperator.IN, new Region(213)),
                            new Expression.RegionByIp(RegionCompareOperator.NOT_IN, new Region(1))
                    ]),
                    new Expression.DeviceCategory(IdentityOperator.EQ, DeviceCategory.PERSONAL_COMPUTER),
                    new Expression.OsFamily(IdentityOperator.EQ, OsFamily.WINDOWS),
                    new Expression.OsVersion(CompareOperator.GTE, 8.1),
                    new Expression.OsVersionMajor(CompareOperator.GT, 8),
                    new Expression.OsVersionMinor(CompareOperator.GTE, 1),
                    new Expression.OsVersionBugfix(CompareOperator.LTE, 225),
                    new Expression.UserAgentType(IdentityOperator.EQ, UserAgentType.BROWSER),
                    new Expression.UserAgentFamily(IdentityOperator.NE, UserAgentFamily.valueOf('OPERA')),
                    new Expression.UserAgentVersion(CompareOperator.LT, 11.12),
                    new Expression.UserAgentVersionMajor(CompareOperator.LT, 11),
                    new Expression.UserAgentVersionMinor(CompareOperator.LT, 12),
                    new Expression.UserAgentVersionBugfix(CompareOperator.GT, 2026),
                    new Expression.Rating(CompareOperator.GTE, 885.15),
                    new Connective.Or([
                            new Expression.Skill('224', CompareOperator.GTE, 85),
                            new Expression.Skill('300', CompareOperator.NE, null),
                            Expression.Skill.exactValueOf('350', CompareOperator.EQ, 75.512)
                    ])
            ])
            it
        }

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools').withBody(json(new JsonBuilder(pool_map).toString())), once())
                .respond(response(new JsonBuilder(pool_map).toString()).withStatusCode(201))

        when:
        def result = factory.poolClient.createPool(pool)

        then:
        matches result.result, pool
    }

    @Unroll
    def "createPool; check quality configs; #_collectorType"() {
        setup:
        def pool_map = pool_map_with_readonly().with {
            it.quality_control = [
                    captcha_frequency   : 'MEDIUM',
                    training_requirement: [
                            training_pool_id            : '21',
                            training_passing_skill_value: 85
                    ],
                    configs             : [_configMap]
            ]
            it
        }

        def pool = pool_with_readonly().with {
            it.qualityControl = new QualityControl([_configObject]).with {
                it.captchaFrequency = CaptchaFrequency.MEDIUM
                it.trainingRequirement = new TrainingRequirement('21', 85)
                it
            }
            it
        }

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools').withBody(json(new JsonBuilder(pool_map).toString())), once())
                .respond(response(new JsonBuilder(pool_map).toString()).withStatusCode(201))

        when:
        def result = factory.poolClient.createPool(pool)

        then:
        matches result.result, pool

        where:
        [_collectorType, _configMap, _configObject] << [
                [
                        'GOLDEN SET',
                        [
                                collector_config: [type: 'GOLDEN_SET', parameters: [history_size: 10]],
                                rules           : [
                                        [
                                                conditions: [[key: 'total_answers_count', operator: 'GT', value: 7]],
                                                action    : [
                                                        type      : 'SET_SKILL_FROM_OUTPUT_FIELD',
                                                        parameters: [skill_id: '42', from_field: 'correct_answers_rate']
                                                ]
                                        ],
                                        [
                                                conditions: [
                                                        [key: 'total_answers_count', operator: 'GT', value: 7],
                                                        [key: 'correct_answers_rate', operator: 'LT', value: 75.5]
                                                ],
                                                action    : [
                                                        type      : 'RESTRICTION',
                                                        parameters: [
                                                                scope          : 'PROJECT',
                                                                duration_days  : 1,
                                                                private_comment: 'Failed to pass golden set'
                                                        ]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.GoldenSet(
                                        new CollectorConfig.GoldenSet.Parameters(historySize: 10)),
                                [
                                        new RuleConfig(
                                                [new RuleCondition.TotalAnswersCount(CompareOperator.GT, 7)],
                                                new RuleAction.SetSkillFromOutputField(
                                                        new RuleAction.SetSkillFromOutputField.Parameters(
                                                                '42', RuleConditionKey.CORRECT_ANSWERS_RATE))
                                        ),
                                        new RuleConfig(
                                                [
                                                        new RuleCondition.TotalAnswersCount(CompareOperator.GT, 7),
                                                        new RuleCondition.CorrectAnswersRate(CompareOperator.LT, 75.5)
                                                ],
                                                new RuleAction.Restriction(new RuleAction.Restriction.Parameters(
                                                        UserRestrictionScope.PROJECT, 1, 'Failed to pass golden set'))
                                        )
                                ]
                        )
                ],
                [
                        'MAJORITY VOTE',
                        [
                                collector_config: [
                                        type      : 'MAJORITY_VOTE',
                                        parameters: [answer_threshold: 3, history_size: 10]
                                ],
                                rules           : [
                                        [
                                                conditions: [[key: 'total_answers_count', operator: 'GT', value: 2]],
                                                action    : [
                                                        type      : 'SET_SKILL_FROM_OUTPUT_FIELD',
                                                        parameters: [
                                                                skill_id  : '43',
                                                                from_field: 'incorrect_answers_rate'
                                                        ]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.MajorityVote(
                                        new CollectorConfig.MajorityVote.Parameters(3).with { historySize = 10; it }
                                ),
                                [
                                        new RuleConfig(
                                                [new RuleCondition.TotalAnswersCount(CompareOperator.GT, 2)],
                                                new RuleAction.SetSkillFromOutputField(
                                                        new RuleAction.SetSkillFromOutputField.Parameters(
                                                                '43', RuleConditionKey.INCORRECT_ANSWERS_RATE)
                                                )
                                        )
                                ]
                        )
                ],
                [
                        'CAPTCHA',
                        [
                                collector_config: [type: 'CAPTCHA', parameters: [history_size: 10]],
                                rules           : [
                                        [
                                                conditions: [
                                                        [key: 'stored_results_count', operator: 'EQ', value: 10],
                                                        [key: 'success_rate', operator: 'LTE', value: 70.1]
                                                ],
                                                action    : [
                                                        type      : 'RESTRICTION',
                                                        parameters: [
                                                                scope          : 'PROJECT',
                                                                duration_days  : 2,
                                                                private_comment: 'Incorrect captcha'
                                                        ]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.Captcha(new CollectorConfig.Captcha.Parameters(10)),
                                [
                                        new RuleConfig(
                                                [
                                                        new RuleCondition.StoredResultsCount(CompareOperator.EQ, 10),
                                                        new RuleCondition.SuccessRate(CompareOperator.LTE, 70.1)
                                                ],
                                                new RuleAction.Restriction(new RuleAction.Restriction.Parameters(
                                                        UserRestrictionScope.PROJECT, 2, 'Incorrect captcha'))
                                        )
                                ]
                        )
                ],
                [
                        'INCOME',
                        [
                                collector_config: [type: 'INCOME'],
                                rules           : [
                                        [
                                                conditions: [
                                                        [
                                                                key     : 'income_sum_for_last_24_hours',
                                                                operator: 'GTE',
                                                                value   : 20.0
                                                        ]
                                                ],
                                                action    : [
                                                        type      : 'RESTRICTION',
                                                        parameters: [scope: 'ALL_PROJECTS', duration_days: 1]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.Income(),
                                [
                                        new RuleConfig(
                                                [new RuleCondition.IncomeSumForLast24Hours(CompareOperator.GTE, 20.0)],
                                                new RuleAction.Restriction(new RuleAction.Restriction.Parameters(
                                                        UserRestrictionScope.ALL_PROJECTS, 1, null))
                                        )
                                ]
                        )
                ],
                [
                        'SKIPPED IN ROW ASSIGNMENTS',
                        [
                                collector_config: [type: 'SKIPPED_IN_ROW_ASSIGNMENTS', parameters: [:]],
                                rules           : [
                                        [
                                                conditions: [[key: 'skipped_in_row_count', operator: 'GTE', value: 10]],
                                                action    : [
                                                        type      : 'RESTRICTION',
                                                        parameters: [
                                                                scope          : 'POOL',
                                                                duration_days  : 1,
                                                                private_comment: 'Skipped more than 10 assignments'
                                                        ]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.SkippedInRowAssignments(
                                        new CollectorConfig.SkippedInRowAssignments.Parameters()),
                                [
                                        new RuleConfig(
                                                [new RuleCondition.SkippedInRowCount(CompareOperator.GTE, 10)],
                                                new RuleAction.Restriction(new RuleAction.Restriction.Parameters(
                                                        UserRestrictionScope.POOL,
                                                        1,
                                                        'Skipped more than 10 assignments'
                                                ))

                                        )
                                ]
                        )
                ],
                [
                        'ANSWER COUNT',
                        [
                                collector_config: [type: 'ANSWER_COUNT'],
                                rules           : [
                                        [
                                                conditions: [
                                                        [key: 'assignments_accepted_count', operator: 'GTE', value: 12]
                                                ],
                                                action    : [
                                                        type      : 'RESTRICTION',
                                                        parameters: [
                                                                scope          : 'POOL',
                                                                private_comment: 'Completed 12 task suites in pool',
                                                                duration_days  : 2,
                                                        ]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.AnswerCount(),
                                [
                                        new RuleConfig(
                                                [new RuleCondition.AssignmentsAcceptedCount(CompareOperator.GTE, 12)],
                                                new RuleAction.Restriction(new RuleAction.Restriction.Parameters(
                                                        UserRestrictionScope.POOL,
                                                        2,
                                                        'Completed 12 task suites in pool')
                                                )
                                        )
                                ]
                        )
                ],
                [
                        'ASSIGNMENT SUBMIT TIME',
                        [
                                collector_config: [
                                        type      : 'ASSIGNMENT_SUBMIT_TIME',
                                        parameters: [history_size: 10, fast_submit_threshold_seconds: 3]
                                ],
                                rules           : [
                                        [
                                                conditions: [
                                                        [key: 'total_submitted_count', operator: 'EQ', value: 10],
                                                        [key: 'fast_submitted_count', operator: 'GTE', value: 4]
                                                ],
                                                action    : [
                                                        type      : 'RESTRICTION',
                                                        parameters: [
                                                                scope          : 'PROJECT',
                                                                duration_days  : 1,
                                                                private_comment: 'More than 4 fast answers'
                                                        ]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.AssignmentSubmitTime(
                                        new CollectorConfig.AssignmentSubmitTime.Parameters(10, 3)),
                                [
                                        new RuleConfig(
                                                [
                                                        new RuleCondition.TotalSubmittedCount(CompareOperator.EQ, 10),
                                                        new RuleCondition.FastSubmittedCount(CompareOperator.GTE, 4)
                                                ],
                                                new RuleAction.Restriction(new RuleAction.Restriction.Parameters(
                                                        UserRestrictionScope.PROJECT, 1, 'More than 4 fast answers'))
                                        )
                                ]
                        )
                ],
                [
                        'ACCEPTANCE_RATE',
                        [
                                collector_config: [type: 'ACCEPTANCE_RATE'],
                                rules           : [
                                        [
                                                conditions: [
                                                        [key: 'total_assignments_count', operator: 'GTE', value: 10],
                                                        [key: 'rejected_assignments_rate', operator: 'GT', value: 0.12]
                                                ],
                                                action    : [
                                                        type      : 'RESTRICTION',
                                                        parameters: [duration_days: 1, scope: 'PROJECT']
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.AcceptanceRate(),
                                [
                                        new RuleConfig(
                                                [
                                                        new RuleCondition.TotalAssignmentsCount(
                                                                CompareOperator.GTE, 10),
                                                        new RuleCondition.RejectedAssignmentsRate(
                                                                CompareOperator.GT, 0.12)
                                                ],
                                                new RuleAction.Restriction(new RuleAction.Restriction.Parameters(
                                                        UserRestrictionScope.PROJECT, 1, null))
                                        )
                                ]
                        )
                ],
                [
                        'ASSIGNMENTS_ASSESSMENT',
                        [
                                collector_config: [type: 'ASSIGNMENTS_ASSESSMENT'],
                                rules           : [
                                        [
                                                conditions: [
                                                        [key: 'rejected_assignments_count', operator: 'GTE', value: 2],
                                                        [key: 'assessment_event', operator: 'EQ', value: 'REJECT']
                                                ],
                                                action    : [
                                                        type      : 'CHANGE_OVERLAP',
                                                        parameters: [delta: 1, open_pool: true]
                                                ]
                                        ],
                                        [
                                                conditions: [
                                                        [key: 'pending_assignments_count', operator: 'GT', value: 3]
                                                ],
                                                action    : [
                                                        type      : 'SET_SKILL',
                                                        parameters: [skill_id: '2117', skill_value: 1]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.AssignmentsAssessment(),
                                [
                                        new RuleConfig(
                                                [
                                                        new RuleCondition.RejectedAssignmentsCount(
                                                                CompareOperator.GTE, 2),
                                                        new RuleCondition.AssessmentEvent(
                                                                IdentityOperator.EQ,
                                                                RuleCondition.AssessmentEvent.Type.REJECT
                                                        )
                                                ],
                                                new RuleAction.ChangeOverlap(
                                                        new RuleAction.ChangeOverlap.Parameters(1, true))
                                        ),
                                        new RuleConfig(
                                                [new RuleCondition.PendingAssignmentsCount(CompareOperator.GT, 3)],
                                                new RuleAction.SetSkill(new RuleAction.SetSkill.Parameters('2117', 1))
                                        )
                                ]
                        )
                ],
                [
                        'USERS_ASSESSMENT',
                        [
                                collector_config: [type: 'USERS_ASSESSMENT'],
                                rules           : [
                                        [
                                                conditions: [
                                                        [
                                                                key     : 'pool_access_revoked_reason',
                                                                operator: 'EQ',
                                                                value   : 'SKILL_CHANGE'
                                                        ],
                                                        [key: 'skill_id', operator: 'EQ', value: '2626']
                                                ],
                                                action    : [
                                                        type      : 'CHANGE_OVERLAP',
                                                        parameters: [delta: 1, open_pool: true]
                                                ]
                                        ]
                                ]
                        ],
                        new QualityControlConfig(
                                new CollectorConfig.UsersAssessment(),
                                [
                                        new RuleConfig(
                                                [
                                                        new RuleCondition.PoolAccessRevokedReason(
                                                                IdentityOperator.EQ,
                                                                RuleCondition.PoolAccessRevokedReason.Type.SKILL_CHANGE
                                                        ),
                                                        new RuleCondition.SkillId(IdentityOperator.EQ, '2626')
                                                ],
                                                new RuleAction.ChangeOverlap(
                                                        new RuleAction.ChangeOverlap.Parameters(1, true))
                                        )
                                ]
                        )
                ]
        ]
    }

    def "updatePool"() {
        setup:
        def updated_form = pool_with_readonly().with {
            privateName = 'updated name'
            privateComment = 'updated comment'
            it
        }

        def updated_form_map = pool_map_with_readonly() + [
                private_name   : 'updated name',
                private_comment: 'updated comment'
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21').withBody(json(new JsonBuilder(updated_form_map) as String)), once())
                .respond(response(new JsonBuilder(updated_form_map) as String))

        when:
        def result = factory.poolClient.updatePool('21', updated_form)

        then:
        !result.newCreated
        matches result.result, updated_form
    }

    def "Patch pool"() {
        given:
        def updated_form = pool_with_readonly().with {
            priority = 42
            it
        }

        def updated_form_map = pool_map_with_readonly() + [priority: 42]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21')
                        .withMethod('PATCH')
                        .withBody(json(new JsonBuilder([priority: 42L]) as String)), once())
                .respond(response(new JsonBuilder(updated_form_map) as String))

        when:
        def result = factory.poolClient.patchPool('21', PoolPatchRequest.make().priority(42L).done())

        then:
        !result.newCreated
        matches result.result, updated_form
    }

    def "openPool"() {
        setup:
        def operation_map = [
                id        : 'open-pool-op1id',
                type      : 'POOL.OPEN',
                status    : 'RUNNING',
                submitted : '2016-03-07T15:47:00',
                started   : '2016-03-07T15:47:21',
                parameters: [
                        pool_id: "21"
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21/open').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map).toString()).withStatusCode(202))

        def completeResponseBody =
                new JsonBuilder(operation_map + [status: 'SUCCESS', finished: '2016-03-07T15:48:03']).toString()
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/operations/open-pool-op1id'), once())
                .respond(response(completeResponseBody).withStatusCode(200))

        when:
        def result = factory.poolClient.openPool('21').waitToComplete()

        then:
        matches result, new PoolOpenOperation(
                id: 'open-pool-op1id',
                type: OperationType.POOL_OPEN,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-03-07 15:47:00'),
                started: parseDate('2016-03-07 15:47:21'),
                finished: parseDate('2016-03-07 15:48:03'),
                parameters: new PoolOpenOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient,
        )
    }

    def "openAlreadyOpened"() {
        setup:

        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21/open').withMethod('POST'), once())
                .respond(response().withStatusCode(204))

        when:
        def result = factory.poolClient.openPool('21').waitToComplete()

        then:
        result.isCompleted()
        result.isPseudo()
    }

    def "closePool"() {
        setup:
        def operation_map = [
                id        : 'close-pool-op1id',
                type      : 'POOL.CLOSE',
                status    : 'SUCCESS',
                submitted : '2016-07-22T13:04:00',
                started   : '2016-07-22T13:04:01',
                finished  : '2016-07-22T13:04:02',
                parameters: [
                        pool_id: '21'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21/close').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.poolClient.closePool('21').waitToComplete()

        then:
        matches result, new PoolCloseOperation(
                id: 'close-pool-op1id',
                type: OperationType.POOL_CLOSE,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-07-22 13:04:00'),
                started: parseDate('2016-07-22 13:04:01'),
                finished: parseDate('2016-07-22 13:04:02'),
                parameters: new PoolCloseOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    def "closePoolForUpdate"() {
        setup:
        def operation_map = [
                id        : 'close-pool-for-update-op1id',
                type      : 'POOL.CLOSE',
                status    : 'SUCCESS',
                submitted : '2016-07-22T13:04:00',
                started   : '2016-07-22T13:04:01',
                finished  : '2016-07-22T13:04:02',
                parameters: [
                        pool_id: '21'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21/close-for-update').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.poolClient.closePoolForUpdate('21').waitToComplete()

        then:
        matches result, new PoolCloseOperation(
                id: 'close-pool-for-update-op1id',
                type: OperationType.POOL_CLOSE,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-07-22 13:04:00'),
                started: parseDate('2016-07-22 13:04:01'),
                finished: parseDate('2016-07-22 13:04:02'),
                parameters: new PoolCloseOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    def "archivePool"() {
        setup:
        def operation_map = [
                id        : 'archive-pool-op1id',
                type      : 'POOL.ARCHIVE',
                status    : 'SUCCESS',
                submitted : '2016-07-22T13:04:00',
                started   : '2016-07-22T13:04:01',
                finished  : '2016-07-22T13:04:02',
                parameters: [
                        pool_id: '21'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21/archive').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.poolClient.archivePool('21').waitToComplete()

        then:
        matches result, new PoolArchiveOperation(
                id: 'archive-pool-op1id',
                type: OperationType.POOL_ARCHIVE,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-07-22 13:04:00'),
                started: parseDate('2016-07-22 13:04:01'),
                finished: parseDate('2016-07-22 13:04:02'),
                parameters: new PoolArchiveOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    def "clonePool"() {
        setup:
        def operation_map = [
                id        : 'clone-pool-op1id',
                type      : 'POOL.CLONE',
                status    : 'SUCCESS',
                submitted : '2016-07-22T13:04:00',
                started   : '2016-07-22T13:04:01',
                finished  : '2016-07-22T13:04:02',
                parameters: [
                        pool_id: '21'
                ]
        ]

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/pools/21/clone').withMethod('POST'), once())
                .respond(response(new JsonBuilder(operation_map) as String).withStatusCode(202))

        when:
        def result = factory.poolClient.clonePool('21').waitToComplete()

        then:
        matches result, new PoolCloneOperation(
                id: 'clone-pool-op1id',
                type: OperationType.POOL_CLONE,
                status: OperationStatus.SUCCESS,
                submitted: parseDate('2016-07-22 13:04:00'),
                started: parseDate('2016-07-22 13:04:01'),
                finished: parseDate('2016-07-22 13:04:02'),
                parameters: new PoolCloneOperation.Parameters(poolId: '21'),
                operationClient: factory.operationClient
        )
    }

    def pool_map() {
        [
                project_id                             : '10',
                private_name                           : 'pool_v12_231',
                public_description                     : '42',
                may_contain_adult_content              : true,
                will_expire                            : '2016-03-23T12:59:00',
                auto_close_after_complete_delay_seconds: 600,
                reward_per_assignment                  : 0.03,
                dynamic_pricing_config                 : [
                        type     : "SKILL",
                        skill_id : "123123",
                        intervals: [
                                [
                                        from                 : 50,
                                        to                   : 79,
                                        reward_per_assignment: 0.05
                                ],
                                [
                                        from                 : 80,
                                        reward_per_assignment: 0.1
                                ]
                        ]
                ],
                dynamic_overlap_config                 : [
                        type                  : "BASIC",
                        max_overlap           : 5,
                        min_confidence        : 0.95,
                        answer_weight_skill_id: '42',
                        fields                : [
                                [
                                        name: 'out1'
                                ]
                        ]
                ],
                metadata:  ['testKey': ['testValue']],
                assignment_max_duration_seconds        : 600,
                auto_accept_solutions                  : true,
                priority                               : 10L,
                defaults                               : [
                        default_overlap_for_new_task_suites: 3,
                        default_overlap_for_new_tasks      : 2
                ],
                mixer_config                           : [
                        real_tasks_count                   : 10,
                        golden_tasks_count                 : 2,
                        training_tasks_count               : 1,
                        min_training_tasks_count           : 0,
                        min_golden_tasks_count             : 1,
                        force_last_assignment              : false,
                        force_last_assignment_delay_seconds: 10,
                        mix_tasks_in_creation_order        : false,
                        shuffle_tasks_in_task_suite        : true,
                        golden_task_distribution_function  : [
                                scope       : 'POOL',
                                distribution: 'UNIFORM',
                                window_days : 5,
                                intervals   : [
                                        [
                                                to       : 50,
                                                frequency: 5
                                        ],
                                        [
                                                from     : 100,
                                                frequency: 50
                                        ]
                                ]
                        ]
                ],
                assignments_issuing_config             : [
                        issue_task_suites_in_creation_order: true
                ],
                filter                                 : [
                        and: [
                                [
                                        category: 'profile',
                                        key     : 'adult_allowed',
                                        operator: 'EQ',
                                        value   : true
                                ],
                                [
                                        or: [
                                                [
                                                        category: 'skill',
                                                        key     : '20',
                                                        operator: 'GTE',
                                                        value   : 60
                                                ],
                                                [
                                                        category: 'skill',
                                                        key     : '22',
                                                        operator: 'GT',
                                                        value   : 95
                                                ]
                                        ]
                                ]
                        ]
                ],
                quality_control                        : [
                        captcha_frequency : 'LOW',
                        checkpoints_config: [
                                real_settings: [
                                        target_overlap            : 5,
                                        task_distribution_function: [
                                                scope       : 'PROJECT',
                                                distribution: 'UNIFORM',
                                                window_days : 7,
                                                intervals   : [
                                                        [
                                                                to       : 100,
                                                                frequency: 5
                                                        ],
                                                        [
                                                                from     : 101,
                                                                frequency: 50
                                                        ]
                                                ]
                                        ]
                                ]
                        ],
                        configs           : [
                                [
                                        collector_config: [
                                                type      : 'CAPTCHA',
                                                parameters: [
                                                        history_size: 5
                                                ]
                                        ],
                                        rules           : [
                                                [
                                                        conditions: [
                                                                [
                                                                        key     : 'stored_results_count',
                                                                        operator: 'EQ',
                                                                        value   : 5
                                                                ],
                                                                [
                                                                        key     : 'success_rate',
                                                                        operator: 'LTE',
                                                                        value   : 60.0
                                                                ]
                                                        ],
                                                        action    : [
                                                                type      : 'RESTRICTION',
                                                                parameters: [
                                                                        scope          : 'POOL',
                                                                        duration_days  : 10,
                                                                        private_comment: 'ban in pool'
                                                                ]
                                                        ]
                                                ]
                                        ]
                                ]
                        ]
                ]
        ]
    }

    def training_map() {
        [
                id                             : '22',
                owner                          : [id        : 'requester-1',
                                                  myself    : true,
                                                  company_id: '1'],
                type                           : 'TRAINING',
                project_id                     : '10',
                private_name                   : 'training_v12_231',
                public_description             : '42',
                public_instructions            : 'training instructions',
                may_contain_adult_content      : true,
                reward_per_assignment          : 0.00,
                assignment_max_duration_seconds: 600,
                auto_accept_solutions          : true,
                priority                       : 0,
                defaults                       : [
                        default_overlap_for_new_task_suites: 30_000
                ],
                mixer_config                   : [
                        real_tasks_count                   : 0,
                        golden_tasks_count                 : 0,
                        training_tasks_count               : 7,
                        min_training_tasks_count           : 1,
                        force_last_assignment              : false,
                        force_last_assignment_delay_seconds: 10,
                        mix_tasks_in_creation_order        : false,
                        shuffle_tasks_in_task_suite        : true,
                ],
                assignments_issuing_config     : [
                        issue_task_suites_in_creation_order: true
                ],
                quality_control                : [
                        configs: [
                                [
                                        collector_config: [
                                                type: 'TRAINING',
                                                uuid: 'cdf0f2ee-04e4-11e8-8a8d-6c96cfdb64bb'
                                        ],
                                        rules           : [
                                                [
                                                        conditions: [
                                                                [
                                                                        key     : 'submitted_assignments_count',
                                                                        operator: 'EQ',
                                                                        value   : 5
                                                                ]
                                                        ],
                                                        action    : [
                                                                type      : 'SET_SKILL_FROM_OUTPUT_VALUE',
                                                                parameters: [
                                                                        skill_id  : '676',
                                                                        from_field: 'correct_answers_rate'
                                                                ]
                                                        ]
                                                ],
                                                [
                                                        conditions: [
                                                                [
                                                                        key     : 'next_assignment_available',
                                                                        operator: 'EQ',
                                                                        value   : false
                                                                ],
                                                                [
                                                                        key     : 'total_answers_count',
                                                                        operator: 'GT',
                                                                        value   : 0
                                                                ]
                                                        ],
                                                        action    : [
                                                                type      : 'SET_SKILL_FROM_OUTPUT_VALUE',
                                                                parameters: [
                                                                        skill_id  : '676',
                                                                        from_field: 'correct_answers_rate'
                                                                ]
                                                        ]
                                                ]
                                        ]
                                ]
                        ]
                ],
                training_config                : [
                        training_skill_ttl_days: 5
                ],
                status                         : 'OPEN',
                created                        : '2017-12-03T12:03:00',
                last_started                   : '2017-12-04T12:12:03'
        ]
    }

    def pool_map_with_readonly() {
        pool_map() + [
                id               : '21',
                owner            : [id: 'requester-1', myself: true, company_id: '1'],
                type             : 'REGULAR',
                created          : '2015-12-16T12:55:01',
                last_started     : '2015-12-17T08:00:01',
                last_stopped     : '2015-12-18T08:00:01',
                last_close_reason: 'MANUAL',
                status           : 'CLOSED'
        ]
    }

    def pool() {
        new Pool('10', 'pool_v12_231', true, parseDate('2016-03-23 12:59:00'), 0.03, 600, true, new PoolDefaults(3))
                .with {
                    publicDescription = '42'
                    autoCloseAfterCompleteDelaySeconds = 600
                    priority = 10L
                    defaults.defaultOverlapForNewTasks = 2

                    dynamicPricingConfig = new DynamicPricingConfig()
                    dynamicPricingConfig.type = DynamicPricingConfig.Type.SKILL
                    dynamicPricingConfig.skillId = "123123"
                    dynamicPricingConfig.intervals = ImmutableList.of(
                            new DynamicPricingConfig.Interval(50, 79, 0.05),
                            new DynamicPricingConfig.Interval(80, null, 0.1)
                    )

                    metadata = ['testKey': ['testValue']]

                    dynamicOverlapConfig = new BasicDynamicOverlapConfig()
                    dynamicOverlapConfig.maxOverlap = 5
                    dynamicOverlapConfig.minConfidence = 0.95
                    dynamicOverlapConfig.answerWeightSkillId = '42'
                    dynamicOverlapConfig.fields = [new BasicDynamicOverlapConfig.Field("out1")]

                    mixerConfig = new MixerConfig(10, 2, 1)
                    mixerConfig.forceLastAssignment = false
                    mixerConfig.forceLastAssignmentDelaySeconds = 10
                    mixerConfig.minGoldenTasksCount = 1
                    mixerConfig.minTrainingTasksCount = 0
                    mixerConfig.mixTasksInCreationOrder = false
                    mixerConfig.shuffleTasksInTaskSuite = true
                    mixerConfig.goldenTaskDistributionFunction = new TaskDistributionFunction(
                            TaskDistributionFunction.Scope.POOL,
                            TaskDistributionFunction.Distribution.UNIFORM,
                            5,
                            [
                                    new TaskDistributionFunction.Interval(null, 50, 5),
                                    new TaskDistributionFunction.Interval(100, null, 50)
                            ]
                    )

                    assignmentsIssuingConfig = new AssignmentsIssuingConfig(issueTaskSuitesInCreationOrder: true)
                    filter = new Connective.And([
                            new Expression.AdultAllowed(IdentityOperator.EQ, true),
                            new Connective.Or([
                                    new Expression.Skill('20', CompareOperator.GTE, 60),
                                    new Expression.Skill('22', CompareOperator.GT, 95)
                            ])
                    ])
                    qualityControl = new QualityControl([
                            new QualityControlConfig(
                                    new CollectorConfig.Captcha(new CollectorConfig.Captcha.Parameters(5)),
                                    [new RuleConfig(
                                            [
                                                    new RuleCondition.StoredResultsCount(CompareOperator.EQ, 5),
                                                    new RuleCondition.SuccessRate(CompareOperator.LTE, 60.0)
                                            ],
                                            new RuleAction.Restriction(
                                                    new RuleAction.Restriction.Parameters(
                                                            UserRestrictionScope.POOL, 10, 'ban in pool')
                                            )
                                    )]
                            )
                    ]).with {
                        captchaFrequency = CaptchaFrequency.LOW
                        checkpointsConfig = new CheckpointsConfig(
                                realSettings: new CheckpointsConfig.Settings(
                                        5,
                                        new TaskDistributionFunction(
                                                TaskDistributionFunction.Scope.PROJECT,
                                                TaskDistributionFunction.Distribution.UNIFORM,
                                                7,
                                                [
                                                        new TaskDistributionFunction.Interval(null, 100, 5),
                                                        new TaskDistributionFunction.Interval(101, null, 50)
                                                ]
                                        )
                                )
                        )
                        it
                    }
                    it
                }
    }

    def training() {
        new Pool('22', 'training_v12_231', true, null, 0.00, 600, true, new PoolDefaults(30_000)).with {
            type = PoolType.TRAINING
            publicDescription = '42'
            publicInstructions = 'training instructions'
            priority = 0
            mixerConfig = new MixerConfig(0, 0, 7).with {
                minTrainingTasksCount = 1
                forceLastAssignment = false
                forceLastAssignmentDelaySeconds = 10
                mixTasksInCreationOrder = false
                shuffleTasksInTaskSuite = true
                it
            }
            assignmentsIssuingConfig = new AssignmentsIssuingConfig(true)
            qualityControl = new QualityControl([
                    new QualityControlConfig(
                            new CollectorConfig.Training([:]).with {
                                uuid = UUID.fromString('cdf0f2ee-04e4-11e8-8a8d-6c96cfdb64bb')
                                it
                            },
                            [
                                    new RuleConfig(
                                            [new RuleCondition.SubmittedAssignmentsCount(CompareOperator.EQ, 5)],
                                            new RuleAction.SetSkillFromOutputField(
                                                    new RuleAction.SetSkillFromOutputField.Parameters(
                                                            '676', RuleConditionKey.CORRECT_ANSWERS_RATE)
                                            )
                                    ),
                                    new RuleConfig(
                                            [
                                                    new RuleCondition.NextAssignmentAvailable(
                                                            IdentityOperator.EQ, false),
                                                    new RuleCondition.TotalAnswersCount(CompareOperator.GT, 0)
                                            ],
                                            new RuleAction.SetSkillFromOutputField(
                                                    new RuleAction.SetSkillFromOutputField.Parameters(
                                                            '676', RuleConditionKey.CORRECT_ANSWERS_RATE)
                                            )
                                    )
                            ]
                    )
            ])
            trainingConfig = new PoolTrainingConfig(trainingSkillTtlDays: 5)
            status = PoolStatus.OPEN
            created = parseDate('2017-12-03 12:03:00')
            lastStarted = parseDate('2017-12-04 12:12:03')
        }
    }

    def pool_with_readonly() {
        pool().with {
            id = '21'
            it.owner = new Owner(id: 'requester-1', myself: true, companyId: '1')
            type = PoolType.REGULAR
            status = PoolStatus.CLOSED
            created = parseDate('2015-12-16 12:55:01')
            lastStarted = parseDate('2015-12-17 08:00:01')
            lastStopped = parseDate('2015-12-18 08:00:01')
            lastCloseReason = PoolCloseReason.MANUAL
            it
        }
    }
}
