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

package io.openvalidation.rest.service;

import io.openvalidation.common.utils.StringUtils;

public class OVParams {
  private String rule, schema, culture, language;

  public OVParams() {}

  public OVParams(String rule, String schema, String culture, String language) {
    this.rule = rule;
    this.schema = schema;
    this.culture = culture;
    this.language = language;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getCulture() {
    return culture;
  }

  public void setCulture(String culture) {
    this.culture = culture;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public boolean isEmpty() {
    return StringUtils.isNullOrEmpty(this.rule)
        && StringUtils.isNullOrEmpty(this.schema)
        && StringUtils.isNullOrEmpty(this.culture)
        && StringUtils.isNullOrEmpty(this.language);
  }
}
