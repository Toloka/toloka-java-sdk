package ai.toloka.client.v1.impl

import ai.toloka.client.v1.impl.transport.DefaultHttpClientConfiguration
import ai.toloka.client.v1.operation.Operation
import ai.toloka.client.v1.TolokaRequestIOException
import ai.toloka.client.v1.TlkException
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.json.JsonBuilder
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.mockserver.client.server.MockServerClient

import static java.util.Collections.singletonList
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response


class AbstractClientImplSpec extends AbstractClientSpec {
    def httpClient = HttpClientBuilder.create()
            .setMaxConnTotal(2)
            .setMaxConnPerRoute(2)
            .setDefaultRequestConfig(DefaultHttpClientConfiguration.getDefaultRequestConfig())
            .setUserAgent(DefaultHttpClientConfiguration.getUserAgent())
            .setDefaultHeaders(singletonList(DefaultHttpClientConfiguration.getDefaultAuthorizationHeader('abc')))
            .build()

    def testFactory = new TolokaClientFactoryImpl(new URI('http://localhost:8083/api/'), httpClient)
            .tune({ http ->  http.addHeader(new BasicHeader(headerName, headerValue)) })

    def abstractClient = new AbstractClientImpl(testFactory) {}

    def "return connection in pool; get with 404" () {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/test-get/1'))
                .respond(response(new JsonBuilder(test_map()).toString())
                .withStatusCode(404))

        when:
        abstractClient.get('1', 'test-get', TestEntity.class)
        abstractClient.get('1', 'test-get', TestEntity.class)
        abstractClient.get('1', 'test-get', TestEntity.class)

        then:
        noExceptionThrown()
    }

    def "return connection in pool; executeAction with 204" () {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/test-execute/1/action'))
                .respond(response(new JsonBuilder(test_map()).toString())
                .withStatusCode(204))

        when:
        abstractClient.executeAction('1', 'test-execute', 'action', Operation.class)
        abstractClient.executeAction('1', 'test-execute', 'action', Operation.class)
        abstractClient.executeAction('1', 'test-execute', 'action', Operation.class)


        then:
        noExceptionThrown()
    }

    def "return connection in pool; delete with not 404" () {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/test-del/1').withMethod('DELETE'))
                .respond(response(new JsonBuilder(test_map()).toString())
                .withStatusCode(204))

        when:
        abstractClient.delete('1', 'test-del')
        abstractClient.delete('1', 'test-del')
        abstractClient.delete('1', 'test-del')

        then:
        notThrown(TolokaRequestIOException)
    }

    def "return connection in pool in parseException method" () {
        setup:
        new MockServerClient('localhost', 8083)
                .when(request('/api/v1/test-exception/1'))
                .respond(response(new JsonBuilder(toloka_exception_map()).toString())
                .withStatusCode(203))

        when:
        abstractClient.get('1', 'test-exception', TestEntity.class)

        then:
        thrown(TlkException)

        when:
        abstractClient.get('1', 'test-exception', TestEntity.class)

        then:
        thrown(TlkException)

        when:
        abstractClient.get('1', 'test-exception', TestEntity.class)

        then:
        thrown(TlkException)

    }

    def test_map() {
        [
                id: '10'
        ]
    }

    def toloka_exception_map() {
        [
            code: 'NOT_ACCEPTABLE',
            request_id: '123',
            message : 'Not acceptable'
        ]
    }

    class TestEntity {
        int id;

        @JsonCreator
        TestEntity(@JsonProperty("id") id) {
            this.id = id
        }
    }

}
