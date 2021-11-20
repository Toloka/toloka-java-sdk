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

package ai.toloka.client.v1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class FlexibleEnum<E extends FlexibleEnum<E>> implements Comparable<E> {

    private final String name;

    protected FlexibleEnum(String name) {
        this.name = name;
    }

    @JsonValue
    public String name() {
        return name;
    }

    @Override public String toString() {
        return name();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlexibleEnum that = (FlexibleEnum) o;
        return Objects.equals(name, that.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name);
    }

    @Override public int compareTo(E o) {
        return name.compareTo(o.name());
    }

    protected static <T extends FlexibleEnum<T>> T[] values(T[] knownValues,
                                                            Collection<T> discoveredValues,
                                                            Class<T> clazz) {
        // make a snapshot of concurrent structure to prevent out of bound
        List<T> copy = new ArrayList<>(discoveredValues);

        @SuppressWarnings("unchecked")
        T[] out = (T[]) Array.newInstance(clazz, knownValues.length + copy.size());

        copy.toArray(out);
        for (int i = 0; i < knownValues.length; i++) {
            out[i + copy.size()] = knownValues[i];
        }
        return out;
    }

    protected interface NewEnumCreator<T extends FlexibleEnum<T>> {
        T create(String name);
    }

    protected static <T extends FlexibleEnum<T>> T valueOf(T[] knownValues,
                                                           ConcurrentMap<String, T> discoveredValues,
                                                           String name,
                                                           NewEnumCreator<T> enumCreator) {
        if (name == null) {
            throw new NullPointerException("Name is null");
        }

        // try to find existing one
        for (T t : knownValues) {
            if (t.name().equals(name)) {
                return t;
            }
        }
        T value = discoveredValues.get(name);
        if (value != null) {
            return value;
        }

        // enum missed. Create new one and cache it
        T created = enumCreator.create(name);
        discoveredValues.putIfAbsent(name, created);

        return discoveredValues.get(name);
    }

}
