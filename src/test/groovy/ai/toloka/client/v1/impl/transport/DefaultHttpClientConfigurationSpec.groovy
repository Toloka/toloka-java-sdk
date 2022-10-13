package ai.toloka.client.v1.impl.transport

import spock.lang.Specification

class DefaultHttpClientConfigurationSpec extends Specification {

    def "default client should recognize auth format and provide correct header; #_authInput"() {
        expect:
        DefaultHttpClientConfiguration.recognizeAuthFormat(_authInput) == _expected

        where:
        _authInput                                      | _expected
        'ALPHANUMERICSTRING123'                         | 'OAuth ALPHANUMERICSTRING123'
        '   ALPHANUMERICSTRING123+SPACES  '             | 'OAuth ALPHANUMERICSTRING123%2BSPACES'
        'XXXXXXXXXXXXXXXXXXXXXX.YYY.XXXXX'              | 'ApiKey XXXXXXXXXXXXXXXXXXXXXX.YYY.XXXXX'
        'XXXXXXXXXXXXXXXXXXXXXXXX.YYY.XXXXX'            | 'ApiKey XXXXXXXXXXXXXXXXXXXXXXXX.YYY.XXXXX'
        '   XXXXXXXXXXXXXXXXXXXXXXXX.YYY.WITH+SPACES  ' | 'ApiKey XXXXXXXXXXXXXXXXXXXXXXXX.YYY.WITH%2BSPACES'
        'XXXXXXXXXXXXXXXXXXXXXXX.XX.X.XX'               | 'OAuth XXXXXXXXXXXXXXXXXXXXXXX.XX.X.XX'
        'XXXXX.XXXXX'                                   | 'OAuth XXXXX.XXXXX'
        'OAuth TYPE+PROVIDED'                           | 'OAuth TYPE%2BPROVIDED'
        'ApiKey TYPE+PROVIDED'                          | 'ApiKey TYPE%2BPROVIDED'
        '  ApiKey MANY+SPACES+AROUND  '                 | 'ApiKey MANY%2BSPACES%2BAROUND'
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
