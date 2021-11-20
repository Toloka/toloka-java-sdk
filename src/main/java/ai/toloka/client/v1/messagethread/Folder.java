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

package ai.toloka.client.v1.messagethread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import ai.toloka.client.v1.FlexibleEnum;

public final class Folder extends FlexibleEnum<Folder> {

    private Folder(String name) {
        super(name);
    }

    public static final Folder INBOX = new Folder("INBOX");
    public static final Folder OUTBOX = new Folder("OUTBOX");
    public static final Folder AUTOMATIC_NOTIFICATION = new Folder("AUTOMATIC_NOTIFICATION");
    public static final Folder IMPORTANT = new Folder("IMPORTANT");
    public static final Folder UNREAD = new Folder("UNREAD");

    private static final Folder[] VALUES = {INBOX, OUTBOX, AUTOMATIC_NOTIFICATION, IMPORTANT, UNREAD};
    private static final ConcurrentMap<String, Folder> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static Folder[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), Folder.class);
    }

    @JsonCreator
    public static Folder valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new NewEnumCreator<Folder>() {
            @Override public Folder create(String name) {
                return new Folder(name);
            }
        });
    }
}
