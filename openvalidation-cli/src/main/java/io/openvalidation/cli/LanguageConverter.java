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

package io.openvalidation.cli;

import com.google.devtools.common.options.Converter;
import com.google.devtools.common.options.OptionsParsingException;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.Languages;

public class LanguageConverter implements Converter<Language> {
  @Override
  public Language convert(String input) throws OptionsParsingException {
    Language result = Languages.getLanguage(input);
    if (result != null) {
      return result;
    }
    throw new OptionsParsingException("Target language " + input + " not found.");
  }

  @Override
  public String getTypeDescription() {
    return "target programming language";
  }
}
