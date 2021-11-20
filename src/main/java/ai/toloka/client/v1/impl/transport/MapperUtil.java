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

package ai.toloka.client.v1.impl.transport;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MapperUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static DateFormat getTolokaDateFormat() {
        return new TlkCustomDateFormat();
    }

    public static ObjectWriter getObjectWriter() {
        return OBJECT_MAPPER.writer(new TlkCustomDateFormat());
    }

    public static ObjectReader getObjectReader() {
        return formatReader(OBJECT_MAPPER.reader());
    }

    public static ObjectReader getObjectReader(Class<?> c) {
        return formatReader(OBJECT_MAPPER.readerFor(c));
    }

    public static ObjectReader getObjectReader(TypeReference<?> type) {
        return formatReader(OBJECT_MAPPER.readerFor(type));
    }

    private static ObjectReader formatReader(ObjectReader reader) {
        return reader.with(OBJECT_MAPPER.getDeserializationConfig().with(new TlkCustomDateFormat()));
    }

    private static class TlkCustomDateFormat extends DateFormat {

        private static final int DATE_FORMAT_LENGTH = 19;

        private final SimpleDateFormat defaultDateFormat;
        private final SimpleDateFormat dateFormatWithMillis;

        private TlkCustomDateFormat() {
            defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            defaultDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            dateFormatWithMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            dateFormatWithMillis.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        @Override
        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            String withMillisFormat = dateFormatWithMillis.format(date);

            return withMillisFormat.endsWith("000")
                    ? defaultDateFormat.format(date, toAppendTo, fieldPosition)
                    : dateFormatWithMillis.format(date, toAppendTo, fieldPosition);
        }

        @Override
        public Date parse(String source, ParsePosition pos) {
            return source.length() == DATE_FORMAT_LENGTH
                    ? defaultDateFormat.parse(source, pos) : dateFormatWithMillis.parse(source, pos);
        }

        @Override
        public Object clone() {
            return new TlkCustomDateFormat();
        }
    }
}
