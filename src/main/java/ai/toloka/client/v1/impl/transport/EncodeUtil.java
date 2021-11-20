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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EncodeUtil {
    private static final List<String> TO_REPLACES = Arrays.asList(
            "{",
            "}",
            ":",
            "\"",
            ",",
            "[",
            "]"
    );
    private static final Map<String, String> REPLACES = new HashMap<>();

    static {
        for (String raw : TO_REPLACES) {
            try {
                REPLACES.put(URLEncoder.encode(raw, "UTF8"), raw);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("really?", e);
            }
        }
    }

    private EncodeUtil() {
    }

    public static String encodeNonAscii(String string) {
        try {
            // just an URL encode, but keep some values in raw form for better looking json in headers
            String s = URLEncoder.encode(string, "UTF8");
            for (Map.Entry<String, String> replace : REPLACES.entrySet()) {
                s = s.replaceAll(replace.getKey(), replace.getValue());
            }
            return s;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("utf8 is gone - no way", e);
        }
    }
}
