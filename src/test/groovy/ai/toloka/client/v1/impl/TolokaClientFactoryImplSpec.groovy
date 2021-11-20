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

import ai.toloka.client.v1.TolokaClientFactory
import ai.toloka.client.v1.impl.transport.MapperUtil
import spock.lang.Specification
import spock.lang.Unroll

import static AbstractClientSpec.parseDate
import static AbstractClientSpec.parseDateWithMillis

class TolokaClientFactoryImplSpec extends Specification {

    @Unroll
    def "check dates deserialization; #_msg"() {
        setup:
        TolokaClientFactory factory = new TolokaClientFactoryImpl(new URI('http://localhost/api'), 'sample')

        expect:
        _out == MapperUtil.getObjectReader(SampleDateHolder).readValue(_in).date

        where:
        _in                                   | _out                                           | _msg
        '{"date": "2016-03-05T12:57:21"}'     | parseDate('2016-03-05 12:57:21')               | 'full datetime'
        '{"date": "2016-03-05T12:57:12.333"}' | parseDateWithMillis('2016-03-05 12:57:12.333') | 'with millis'
    }

    public static class SampleDateHolder {

        public Date date
    }
}
