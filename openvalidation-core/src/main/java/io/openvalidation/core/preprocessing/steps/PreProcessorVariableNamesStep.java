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

import io.openvalidation.common.data.DataPropertyBase;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.common.utils.NameMasking;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.core.Aliases;
import io.openvalidation.core.preprocessing.PreProcessorStepBase;
import java.util.ArrayList;
import java.util.List;

public class PreProcessorVariableNamesStep extends PreProcessorStepBase {

  @Override
  public String process(String rule) throws Exception {
    List<String> variableNames = parseVariableNames(rule);

    if (variableNames != null && variableNames.size() > 0) {

      // validate duplicate names...
      List<String> duplicatedNames = LINQ.findDuplicates(variableNames);
      if (duplicatedNames != null && duplicatedNames.size() > 0)
        throw new ASTValidationException(
            "duplicate variable names found: " + StringUtils.join(duplicatedNames, ","));

      // validate same names as in schema
      List<String> wrongNames = new ArrayList<>();
      for (String name : variableNames) {
        DataPropertyBase property = getContext().getSchema().getPropertyIfIsInPath(name);
        if (property != null) {
          wrongNames.add(name + " -> " + property.getFullNameLowerCase());
        }
      }
      if (wrongNames.size() > 0)
        throw new ASTValidationException(
            "variable names schould not be named as schema attributes: "
                + StringUtils.join(wrongNames, ","));

      // validate same names as in alias?? not needed?
      List<String> wrongNamesAsAlias = new ArrayList<>();
      for (String name : variableNames) {

        String unmaskedname = StringUtils.reverseKeywords(name);
        if (Aliases.hasAlias(unmaskedname, this.getContext().getLocale()))
          wrongNamesAsAlias.add(unmaskedname);
      }
      if (wrongNamesAsAlias.size() > 0)
        throw new ASTValidationException(
            "variable names schould not be named as reserved KEYWORD: "
                + StringUtils.join(wrongNamesAsAlias, ","));

      // mask names and replace...
      for (String name : variableNames) {
        rule = rule.replaceAll(name, StringUtils.reverseKeywords(name));
      }
    }

    return rule;
  }

  public List<String> parseVariableNames(String rule) {
    List<String> out = new ArrayList<>();

    if (!StringUtils.isNullOrEmpty(rule)) {
      List<String> itemss = StringUtils.splitAndRemoveEmpty(rule, Constants.PARAGRAPH_TOKEN);

      if (itemss != null) {
        for (String item : itemss) {
          if (!item.contains(Constants.IF_TOKEN)
              && !item.contains(Constants.MUST_TOKEN)
              && !item.contains(Constants.MUSTNOT_TOKEN)
              && item.contains(Constants.AS_TOKEN)) {
            int pos = item.indexOf(Constants.AS_TOKEN);
            if (pos > -1) {
              int start = item.indexOf(" ", pos);

              if (start > -1) {
                String varName = item.substring(start + 1).trim();
                if (!StringUtils.isNullOrEmpty(varName)) out.add(varName);
                // item = item.replace(varName, NameMasking.maskVariableName(varName));
              }
            }
          }
        }
      }
    }

    return out;
  }

  public String replaceVariableNames(String preprocessedRule) {
    String out = "";

    if (!StringUtils.isNullOrEmpty(preprocessedRule)) {
      List<String> itemss =
          StringUtils.splitAndRemoveEmpty(preprocessedRule, Constants.PARAGRAPH_TOKEN);

      if (itemss != null) {
        for (String item : itemss) {
          if (!item.contains(Constants.IF_TOKEN)
              && !item.contains(Constants.MUST_TOKEN)
              && !item.contains(Constants.MUSTNOT_TOKEN)
              && item.contains(Constants.AS_TOKEN)) {
            int pos = item.indexOf(Constants.AS_TOKEN);
            if (pos > -1) {
              int start = item.indexOf(" ", pos);

              if (start > -1) {
                String varName = item.substring(start + 1).trim();
                if (!StringUtils.isNullOrEmpty(varName))
                  item = item.replace(varName, NameMasking.maskVariableName(varName));
              }
            }
          }

          out += item + Constants.PARAGRAPH_TOKEN;
        }
      }
    } else {
      out = preprocessedRule;
    }

    return out;
  }
}
