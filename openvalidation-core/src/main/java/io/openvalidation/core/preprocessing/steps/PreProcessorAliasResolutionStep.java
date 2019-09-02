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

package io.openvalidation.core.preprocessing.steps;

import static io.openvalidation.common.utils.RegExUtils.PARAGRAPH_REGEX;

import io.openvalidation.common.log.ProcessLogger;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.core.preprocessing.PreProcessorStepBase;

public class PreProcessorAliasResolutionStep extends PreProcessorStepBase {

  @Override
  public String process(String rule) throws Exception {

    try {
      String ruleContent =
          rule.replaceAll("\r\n", "\n")
              .replaceAll(PARAGRAPH_REGEX, " " + Constants.PARAGRAPH_TOKEN + " ");

      ruleContent = Aliases.resolve(ruleContent, getContext().getLocale());

      ProcessLogger.success(ProcessLogger.PREPROCESSOR_RESOLVE_ALIASES);

      return ruleContent;
    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.PREPROCESSOR);
      ProcessLogger.error(ProcessLogger.PREPROCESSOR_RESOLVE_ALIASES);
      throw e;
    }
  }
}
