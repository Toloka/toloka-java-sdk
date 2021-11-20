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

import ai.toloka.client.v1.attachment.AssignmentAttachment
import ai.toloka.client.v1.attachment.AttachmentSearchRequest
import ai.toloka.client.v1.attachment.AttachmentType
import groovy.json.JsonBuilder
import org.apache.commons.io.IOUtils
import org.mockserver.client.server.MockServerClient
import org.mockserver.matchers.Times

import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class AttachmentClientImplSpec extends AbstractClientSpec {

    def "findAttachments"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/attachments').withQueryStringParameters(
                        type: ['ASSIGNMENT_ATTACHMENT'],
                        owner_id: ['requester-1'],
                        owner_company_id: ['company-1'],
                        name: ['ExampleAttachment.txt'],
                        user_id: ['user-1'],
                        created_gt: ['2000-01-01T12:57:01'],
                        sort: ['id'],
                        limit: ['50']), Times.once()
                )
                .respond(response(new JsonBuilder([items: [assignment_attachment_map()], has_more: false]) as String))

        when:
        def request = AttachmentSearchRequest.make()
                .filter()
                .byAttachmentType(AttachmentType.ASSIGNMENT_ATTACHMENT)
                .byOwnerId('requester-1')
                .byOwnerCompanyId('company-1')
                .byName('ExampleAttachment.txt')
                .byUserId('user-1').and()
                .range().byCreated(parseDate('2000-01-01 12:57:01')).gt().and()
                .sort().byId().asc().and()
                .limit(50)
                .done()
        def result = factory.attachmentClient.findAttachments(request)

        then:
        matches result.items.first(), assignment_attachment()
    }

    def "getAttachment"() {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/attachments/attachment-1'))
                .respond(response(new JsonBuilder(assignment_attachment_map()) as String))

        when:
        def result = factory.attachmentClient.getAttachment('attachment-1')

        then:
        matches result, assignment_attachment()
    }

    def "downloadAttachment"() {
        setup:
        def file = new byte[100]
        new Random().nextBytes(file)

        and:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/attachments/attachment-i1d/download'))
                .respond(
                        response().withBody(file)
                                .withHeader('Content-Type', 'image/png')
                                .withHeader('Content-Disposition', 'attachment')
                )

        when:
        def result = factory.attachmentClient.downloadAttachment('attachment-i1d')

        then:
        result.contentType == 'image/png'
        result.contentLength == '100'
        result.transferEncoding == null
        result.contentDisposition == 'attachment'
        IOUtils.toByteArray(result.entity) == file
    }

    def assignment_attachment_map() {
        [
                id             : 'assignment-attachment-1',
                owner          : [id: 'requester-1', myself: true, company_id: 'company-1'],
                attachment_type: 'ASSIGNMENT_ATTACHMENT',
                name           : 'ExampleAttachment.txt',
                media_type     : 'application/octet-stream',
                details        : [
                        user_id      : 'user-1',
                        assignment_id: 'assignment-1',
                        pool_id      : 'pool-1'
                ],
                created        : '2016-05-25T16:15:27.748'
        ]
    }

    def assignment_attachment() {
        new AssignmentAttachment(
                id: 'assignment-attachment-1',
                owner: new ai.toloka.client.v1.Owner(id: 'requester-1', myself: true, companyId: 'company-1'),
                attachmentType: AttachmentType.ASSIGNMENT_ATTACHMENT,
                name: 'ExampleAttachment.txt',
                mediaType: 'application/octet-stream',
                details: new AssignmentAttachment.Details(
                        userId: 'user-1',
                        assignmentId: 'assignment-1',
                        poolId: 'pool-1'
                ),
                created: parseDateWithMillis('2016-05-25 16:15:27.748')
        )
    }
}
