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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClassicTaskViewSpec extends BaseTaskViewSpec {

    private String script;
    private String markup;
    private String styles;
    private Map<String, Object> assets;

    @JsonCreator
    public ClassicTaskViewSpec(@JsonProperty("settings") Map<String, Object> settings) {
        super(settings, Type.CLASSIC);
    }

    public ClassicTaskViewSpec(ClassicTaskViewSpec original) {
        super(original, Type.CLASSIC);
        this.script = original.script;
        this.markup = original.markup;
        this.styles = original.styles;
        this.assets = new HashMap<>(original.assets);
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    public Map<String, Object> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, Object> assets) {
        this.assets = assets;
    }
}
