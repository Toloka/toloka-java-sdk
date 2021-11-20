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

package ai.toloka.client.v1.webhooksubscription.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class, validates {@code Toloka-Signature} header signature
 */
public class SignatureValidator {
    /**
     * HMAC Algorithm version.
     */
    private static final String ALGORITHM = "HmacSHA256";

    /**
     * Generates signature using request payload and {@code Toloka-Signature} header fields.
     *
     * @param secretKey      Secret key, which was used during creation of webhook subscriptions
     * @param ts             Timestamp ("ts") from {@code Toloka-Signature} header
     * @param v              Version ("v") from {@code Toloka-Signature} header
     * @param requestPayload Payload of request from Toloka service
     * @return signature
     * @throws InvalidKeyException      InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String generateSignature(String secretKey, Long ts, Integer v, String requestPayload)
            throws NoSuchAlgorithmException, InvalidKeyException {
        String data = ts + "." + v + "." + requestPayload;
        return doHmac(secretKey, data);
    }

    /**
     * Encrypts provided data with user's secret using hmac-SHA 256 algorithm.
     *
     * @param secretKey encryption key
     * @param data      text to be encrypted
     * @return encrypted data.
     * @throws InvalidKeyException      InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    private static String doHmac(String secretKey, String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac macAlgorithm = Mac.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey
                .getBytes(StandardCharsets.UTF_8), ALGORITHM);
        macAlgorithm.init(secretKeySpec);
        return encodeHex(macAlgorithm.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Encodes data to HEX string.
     *
     * @param data text to be encoded
     * @return HEX string
     */
    private static String encodeHex(byte[] data) {
        return String.format("%040x", new BigInteger(1, data));
    }
}
