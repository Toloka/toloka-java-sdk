package ai.toloka.client.v1.impl.transport

import spock.lang.Specification

class DefaultHttpClientConfigurationSpec extends Specification {

    def "default client should recognize auth format and provide correct header; #_authInput"() {
        expect:
        DefaultHttpClientConfiguration.recognizeAuthFormat(_authInput)

        where:
        _authInput                                  | _expected
        'ALPHANUMERICSTRING123'                     | 'OAuth ALPHANUMERICSTRING123'
        '   ALPHANUMERICSTRING123+SPACES  '         | 'OAuth ALPHANUMERICSTRING123'
        'XXXXXXXXXXXXXXXXXXXXXX.XXXXX'              | 'ApiKey XXXXXXXXXXXXXXXXXXXXXX.XXXXX'
        'XXXXXXXXXXXXXXXXXXXXXXXX.XXXXX'            | 'ApiKey XXXXXXXXXXXXXXXXXXXXXXXX.XXXXX'
        '   XXXXXXXXXXXXXXXXXXXXXXXX.WITH+SPACES  ' | 'ApiKey XXXXXXXXXXXXXXXXXXXXXXXX.XXXXX'
        'XXXXXXXXXXXXXXXXXXXXXXX.XX.XXX'            | 'OAuth XXXXXXXXXXXXXXXXXXXXXXX.XX.XXX'
        'XXXXX.XXXXX'                               | 'OAuth XXXXX.XXXXX'
        'OAuth TYPE+PROVIDED'                       | 'OAuth TYPE+PROVIDED'
        'ApiKey TYPE+PROVIDED'                      | 'ApiKey TYPE+PROVIDED'
        '  ApiKey MANY+SPACES+AROUND  '             | 'ApiKey TYPE+PROVIDED'
    }

    def "malformed auth formats lead to Argument Exception"() {
        when:
        DefaultHttpClientConfiguration.recognizeAuthFormat(_wrongInput)

        then:
        thrown(IllegalArgumentException)

        where:
        _wrongInput << ['WrongApiKey WRONG+TYPE+PROVIDED',
                        'OAuth MANY PARTS']
    }
}
