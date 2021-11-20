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

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.message.BasicHeader
import org.mockserver.integration.ClientAndServer
import org.unitils.reflectionassert.ReflectionComparatorFactory
import org.unitils.reflectionassert.ReflectionComparatorMode
import org.unitils.reflectionassert.report.impl.DefaultDifferenceView
import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat

import static java.util.TimeZone.getTimeZone
import static org.mockserver.integration.ClientAndServer.startClientAndServer

class AbstractClientSpec extends Specification {
    def headerName = "tlk_request_id"
    def headerValue = "some_test_request_id"

    def factory = new TolokaClientFactoryImpl(new URI('http://localhost:8083/api/'), 'abc')
            .tune({ http ->  http.addHeader(new BasicHeader(headerName, headerValue)) })

    @Shared protected ClientAndServer mockServer
    @Shared protected m = new ObjectMapper()

    def setup() {
        if (mockServer == null) {
            mockServer = startClientAndServer(8083)

            // Need to wait a little to ensure that all listeners are configured correctly. The problem seems to be
            // connected with https://github.com/jamesdbloom/mockserver/issues/200
            sleep(1000)
        }
    }

    def cleanupSpec() {
        if (mockServer != null) {
            mockServer.stop()
        }
    }

    protected static Date parseDate(String date) {
        new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').with { timeZone = getTimeZone('UTC'); it }.parse(date)
    }

    protected static Date parseDateWithMillis(String date) {
        new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.SSS').with { timeZone = getTimeZone('UTC'); it }.parse(date)
    }

    protected static matches(actual, expected, ReflectionComparatorMode... modes) {
        new Expando() {

            boolean asBoolean() {
                def difference = ReflectionComparatorFactory
                        .createRefectionComparator(modes).getDifference(expected, actual)

                if (difference) {
                    setProperty 'report', new DefaultDifferenceView().createView(difference)
                }

                !difference
            }

            @Override
            String toString() {
                getProperty 'report'
            }
        }
    }
}
