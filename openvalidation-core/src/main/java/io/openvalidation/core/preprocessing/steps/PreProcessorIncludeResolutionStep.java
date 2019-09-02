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

import io.openvalidation.common.log.ProcessLogger;
import io.openvalidation.common.utils.FileSystemUtils;
import io.openvalidation.core.Aliases;
import io.openvalidation.core.preprocessing.PreProcessorStepBase;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class PreProcessorIncludeResolutionStep extends PreProcessorStepBase {

  @Override
  public String process(String rule) throws Exception {
    try {
      String processedRule = resolveIncludes(rule, new ArrayList<>(), 0);
      ProcessLogger.success(ProcessLogger.PREPROCESSOR_RESOLVE_INCLUDES);
      return processedRule;
    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.PREPROCESSOR);
      ProcessLogger.error(ProcessLogger.PREPROCESSOR_RESOLVE_INCLUDES, e);
      throw e;
    }
  }

  private String resolveIncludes(String plainRule, List<String> includeRegister, int level)
      throws Exception {
    String rule = Aliases.resolve(plainRule, "INCLUDE", this.getContext().getLocale());

    for (String line : rule.split(FileSystemUtils.NewLine)) {
      if (line.contains("INCLUDE")) {
        String includePath = line.replace("INCLUDE", "").trim();
        includePath = Matcher.quoteReplacement(includePath);

        // fallbacke to get include file
        if (!Paths.get(includePath).isAbsolute() && this.getContext().hasWorkingDirectory()) {
          String inclFile = includePath;

          for (String basePath : this.getContext().getWorkingDirectory()) {
            inclFile = Paths.get(basePath, includePath).toString();

            if (FileSystemUtils.fileExists(inclFile)) {
              includePath = inclFile;
              break;
            }
          }
        }

        FileSystemUtils.fileShouldExists(
            includePath,
            "an INCLUDE File: "
                + includePath
                + " could not be found."
                + System.lineSeparator()
                + "Rule Content: "
                + System.lineSeparator()
                + System.lineSeparator()
                + plainRule
                + System.lineSeparator());

        if (includeRegister.contains(includePath))
          throw new RuntimeException(
              "a circular reference detected while resolving INCLUDES. "
                  + includePath
                  + System.lineSeparator()
                  + System.lineSeparator()
                  + plainRule);

        String includePlainContent = FileSystemUtils.readFile(includePath);
        if (includePlainContent != null) {
          includeRegister.add(includePath);
          includePlainContent = resolveIncludes(includePlainContent, includeRegister, level + 2);

          String formatString = "%" + level * 4 + "s";
          String spaces = (level < 1) ? "" : String.format(formatString, " ");

          rule =
              rule.replaceAll(
                  Matcher.quoteReplacement(line),
                  // System.lineSeparator() + spaces + "//INCLUDE CONTENT FROM: " +
                  // Matcher.quoteReplacement(includePath) + System.lineSeparator() +
                  // System.lineSeparator() +
                  includePlainContent.trim() + System.lineSeparator() + System.lineSeparator()
                  //        spaces + "//END OF INCLUDE FROM: " +
                  // Matcher.quoteReplacement(includePath) + System.lineSeparator()
                  );
        }
      }
    }

    return rule;
  }
}
