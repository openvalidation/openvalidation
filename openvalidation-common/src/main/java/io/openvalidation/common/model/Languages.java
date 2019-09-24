/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.openvalidation.common.model;

import io.openvalidation.common.utils.JsonUtils;
import io.openvalidation.common.utils.ResourceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.json.JSONObject;

public class Languages {
  private static Languages singleInstance;

  private List<Language> languages;

  private Languages() {
    String json = ResourceUtils.getResourceConent("languages.json", getClass());
    JSONObject languagesJson = JsonUtils.loadJson(json);
    languages = new ArrayList<>();
    languagesJson
        .keys()
        .forEachRemaining(
            language ->
                languages.add(new Language(language, languagesJson.getJSONObject(language))));
  }

  private static Languages getInstance() {
    if (singleInstance == null) {
      singleInstance = new Languages();
    }
    return singleInstance;
  }

  public static Language getLanguage(String name) {
    return getInstance().languages.stream()
        .filter(language -> name.toLowerCase().equals(language.getName().toLowerCase()))
        .findAny()
        .orElse(null);
  }

  public static Stream<Language> getLanguageStream() {
    return getInstance().languages.stream();
  }
}
