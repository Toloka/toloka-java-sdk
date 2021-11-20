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

package ai.toloka.client.v1.attachment;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class AttachmentType extends FlexibleEnum<AttachmentType> {

    public static final AttachmentType ASSIGNMENT_ATTACHMENT = new AttachmentType("ASSIGNMENT_ATTACHMENT");

    private static final AttachmentType[] VALUES = {ASSIGNMENT_ATTACHMENT};
    private static final ConcurrentMap<String, AttachmentType> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static AttachmentType[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), AttachmentType.class);
    }

    @JsonCreator
    public static AttachmentType valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<AttachmentType>() {
            @Override public AttachmentType create(String name) {
                return new AttachmentType(name);
            }
        });
    }


    private AttachmentType(String name) {
        super(name);
    }
}
