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

import org.json.JSONObject;

public class Language {
  private String _name;
  private String _extension;
  private String _shortName;

  public Language(String name, JSONObject languageInfo) {
    this(name, languageInfo.getString("extension"), languageInfo.getString("short"));
  }

  public Language(String name, String extension, String shortName) {
    _name = name;
    _extension = extension;
    _shortName = shortName;
  }

  public String getName() {
    return _name;
  }

  public String getExtension() {
    return _extension;
  }

  public String getShortName() {
    return _shortName;
  }
}
