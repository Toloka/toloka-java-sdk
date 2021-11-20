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

package ai.toloka.client.v1.project.spec;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "type", visible = true,
        defaultImpl = FieldSpec.UnknownSpec.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FieldSpec.BooleanSpec.class, name = "boolean"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayBooleanSpec.class, name = "array_boolean"),
        @JsonSubTypes.Type(value = FieldSpec.StringSpec.class, name = "string"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayStringSpec.class, name = "array_string"),
        @JsonSubTypes.Type(value = FieldSpec.IntegerSpec.class, name = "integer"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayIntegerSpec.class, name = "array_integer"),
        @JsonSubTypes.Type(value = FieldSpec.FloatSpec.class, name = "float"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayFloatSpec.class, name = "array_float"),
        @JsonSubTypes.Type(value = FieldSpec.UrlSpec.class, name = "url"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayUrlSpec.class, name = "array_url"),
        @JsonSubTypes.Type(value = FieldSpec.FileSpec.class, name = "file"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayFileSpec.class, name = "array_file"),
        @JsonSubTypes.Type(value = FieldSpec.CoordinatesSpec.class, name = "coordinates"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayCoordinatesSpec.class, name = "array_coordinates"),
        @JsonSubTypes.Type(value = FieldSpec.JsonSpec.class, name = "json"),
        @JsonSubTypes.Type(value = FieldSpec.ArrayJsonSpec.class, name = "array_json")
})
public abstract class FieldSpec {

    private FieldType type;

    private boolean required;

    private boolean hidden;

    @JsonCreator
    private FieldSpec(@JsonProperty("type") FieldType type,
                      @JsonProperty("required") boolean required,
                      @JsonProperty("hidden") boolean hidden) {
        this.type = type;
        this.required = required;
        this.hidden = hidden;
    }

    public FieldType getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public static class BooleanSpec extends FieldSpec {

        @JsonProperty("allowed_values")
        private Set<Boolean> allowedValues;

        public BooleanSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public BooleanSpec(@JsonProperty("required") boolean required,
                           @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            this(FieldType.BOOLEAN, required, hidden);
        }

        private BooleanSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }
    }

    public static class ArrayBooleanSpec extends BooleanSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayBooleanSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public ArrayBooleanSpec(@JsonProperty("required") boolean required,
                                @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_BOOLEAN, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class StringSpec extends FieldSpec {

        @JsonProperty("min_length")
        private Integer minLength;

        @JsonProperty("max_length")
        private Integer maxLength;

        private String pattern;

        @JsonProperty("allowed_values")
        private Set<String> allowedValues;

        public StringSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public StringSpec(@JsonProperty("required") boolean required,
                          @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            this(FieldType.STRING, required, hidden);
        }

        private StringSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }

        public Integer getMinLength() {
            return minLength;
        }

        public void setMinLength(Integer minLength) {
            this.minLength = minLength;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public Set<String> getAllowedValues() {
            return allowedValues;
        }

        public void setAllowedValues(Set<String> allowedValues) {
            this.allowedValues = allowedValues;
        }
    }

    public static class ArrayStringSpec extends StringSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayStringSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public ArrayStringSpec(@JsonProperty("required") boolean required,
                               @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_STRING, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class IntegerSpec extends FieldSpec {

        @JsonProperty("min_value")
        private Long minValue;

        @JsonProperty("max_value")
        private Long maxValue;

        @JsonProperty("allowed_values")
        private Set<Long> allowedValues;

        public IntegerSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public IntegerSpec(@JsonProperty("required") boolean required,
                           @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.INTEGER, required, hidden);
        }

        private IntegerSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }

        public Long getMinValue() {
            return minValue;
        }

        public void setMinValue(Long minValue) {
            this.minValue = minValue;
        }

        public Long getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(Long maxValue) {
            this.maxValue = maxValue;
        }

        public Set<Long> getAllowedValues() {
            return allowedValues;
        }

        public void setAllowedValues(Set<Long> allowedValues) {
            this.allowedValues = allowedValues;
        }
    }

    public static class ArrayIntegerSpec extends IntegerSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayIntegerSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public ArrayIntegerSpec(@JsonProperty("required") boolean required,
                                @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_INTEGER, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class FloatSpec extends FieldSpec {

        @JsonProperty("min_value")
        private Double minValue;

        @JsonProperty("max_value")
        private Double maxValue;

        public FloatSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public FloatSpec(@JsonProperty("required") boolean required,
                         @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            this(FieldType.FLOAT, required, hidden);
        }

        private FloatSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }

        public Double getMinValue() {
            return minValue;
        }

        public void setMinValue(Double minValue) {
            this.minValue = minValue;
        }

        public Double getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(Double maxValue) {
            this.maxValue = maxValue;
        }
    }

    public static class ArrayFloatSpec extends FloatSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayFloatSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public ArrayFloatSpec(@JsonProperty("required") boolean required,
                              @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_FLOAT, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class UrlSpec extends FieldSpec {

        public UrlSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public UrlSpec(@JsonProperty("required") boolean required,
                       @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            this(FieldType.URL, required, hidden);
        }

        private UrlSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }
    }

    public static class ArrayUrlSpec extends UrlSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayUrlSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public ArrayUrlSpec(@JsonProperty("required") boolean required,
                            @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_URL, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class FileSpec extends FieldSpec {

        public FileSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public FileSpec(@JsonProperty("required") boolean required,
                        @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            this(FieldType.FILE, required, hidden);
        }

        private FileSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }
    }

    public static class ArrayFileSpec extends FileSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayFileSpec(boolean required) {
            this(required, false);
        }

        public ArrayFileSpec(@JsonProperty("required") boolean required,
                             @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_FILE, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class CoordinatesSpec extends FieldSpec {

        @JsonProperty("current_location")
        private Boolean currentLocation;

        public CoordinatesSpec(boolean required) {
            this(required, false);
        }

        public CoordinatesSpec(@JsonProperty("required") boolean required,
                               @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            this(FieldType.COORDINATES, required, hidden);
        }

        private CoordinatesSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }

        public Boolean getCurrentLocation() {
            return currentLocation;
        }

        public void setCurrentLocation(Boolean currentLocation) {
            this.currentLocation = currentLocation;
        }
    }

    public static class ArrayCoordinatesSpec extends CoordinatesSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayCoordinatesSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public ArrayCoordinatesSpec(@JsonProperty("required") boolean required,
                                    @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_COORDINATES, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class JsonSpec extends FieldSpec {

        public JsonSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public JsonSpec(@JsonProperty("required") boolean required,
                        @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            this(FieldType.JSON, required, hidden);
        }

        private JsonSpec(FieldType type, boolean required, boolean hidden) {
            super(type, required, hidden);
        }
    }

    public static class ArrayJsonSpec extends JsonSpec {

        @JsonProperty("min_size")
        private Integer minSize;

        @JsonProperty("max_size")
        private Integer maxSize;

        public ArrayJsonSpec(boolean required) {
            this(required, false);
        }

        @JsonCreator
        public ArrayJsonSpec(@JsonProperty("required") boolean required,
                             @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(FieldType.ARRAY_JSON, required, hidden);
        }

        public Integer getMinSize() {
            return minSize;
        }

        public void setMinSize(Integer minSize) {
            this.minSize = minSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class UnknownSpec extends FieldSpec {

        public UnknownSpec(FieldType type, boolean required) {
            this(type, required, false);
        }

        @JsonCreator UnknownSpec(@JsonProperty("type") FieldType type, @JsonProperty("required") boolean required,
                                 @JsonProperty(value = "hidden", defaultValue = "false") boolean hidden) {
            super(type, required, hidden);
        }
    }
}
