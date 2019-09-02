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

package io.openvalidation.core.preprocessing;

import io.openvalidation.common.interfaces.IOpenValidationPreprocessor;
import io.openvalidation.common.log.ProcessLogger;
import io.openvalidation.common.model.PreProcessorContext;

public class DefaultPreProcessor implements IOpenValidationPreprocessor {

  public String process(String plainRule, PreProcessorContext ctx) throws Exception {

    String processedRule = plainRule;

    for (PreProcessorStepBase step : PreProcessorStepFactory.create(ctx)) {
      processedRule = step.process(processedRule);
    }

    ProcessLogger.success(ProcessLogger.PREPROCESSOR);

    return processedRule;

    //        String ruleContent;

    //        try
    //        {
    //            ruleContent = resolveIncludes(plainRule, new ArrayList<>(), locale,
    // workingDirectory, 0);
    //            ProcessLogger.success(ProcessLogger.PREPROCESSOR_RESOLVE_INCLUDES);
    //        }
    //        catch (Exception e){
    //            ProcessLogger.error(ProcessLogger.PREPROCESSOR);
    //            ProcessLogger.error(ProcessLogger.PREPROCESSOR_RESOLVE_INCLUDES, e);
    //            throw e;
    //        }
    //
    //
    //        try {
    //            ruleContent = ruleContent.replaceAll("\r\n","\n")
    //                                     .replaceAll(PARAGRAPH_REGEX, " " +
    // Constants.PARAGRAPH_TOKEN + " ");
    //
    //
    //
    //            ruleContent = Aliases.resolve(ruleContent, locale);
    //
    //            ruleContent = resolveCollisions(ruleContent);
    //
    //            ProcessLogger.success(ProcessLogger.PREPROCESSOR_RESOLVE_ALIASES);
    //        }
    //        catch (Exception e)
    //        {
    //            ProcessLogger.error(ProcessLogger.PREPROCESSOR);
    //            ProcessLogger.error(ProcessLogger.PREPROCESSOR_RESOLVE_ALIASES);
    //            throw e;
    //        }
    //
    //
    //        ProcessLogger.success(ProcessLogger.PREPROCESSOR);
    //        return ruleContent.replaceAll(" " + Constants.PARAGRAPH_TOKEN + " ", " " +
    // Constants.PARAGRAPH_TOKEN )
    //                          .replaceAll(Constants.PARAGRAPH_TOKEN + " ", " " +
    // Constants.PARAGRAPH_TOKEN );
  }

  // private static Pattern

  /*
  public String resolveCollisions(String rule){
      StringBuilder sb = new StringBuilder();

      //replace collisions
      int x=0;
      String[] items = rule.split(Constants.PARAGRAPH_TOKEN);
      for (String item : items) {

          //... SHOULD be BIGGER then ...
          if (item.contains(Constants.CONSTRAINT_TOKEN) && item.contains(Constants.THEN_TOKEN)) {

              int constraintPosition = item.indexOf(Constants.CONSTRAINT_TOKEN);
              int thenPosition = item.indexOf(Constants.THEN_TOKEN);

              if (constraintPosition < thenPosition) {
                  item = item.replaceAll(Constants.THEN_TOKEN, "");
              }
          }

          if (item.contains(Constants.CONSTRAINT_TOKEN) && item.contains(Constants.FROM_TOKEN)) {
              item = item.replaceAll(Constants.FROM_TOKEN, "");
          }

          //... SHOULD be BIGGER if ...
          if (item.contains(Constants.CONSTRAINT_TOKEN) && item.contains(Constants.IF_TOKEN)) {

              int constraintPosition = item.indexOf(Constants.CONSTRAINT_TOKEN);
              int thenPosition = item.indexOf(Constants.IF_TOKEN);

              if (constraintPosition < thenPosition) {
                  item = item.replaceAll(Constants.IF_TOKEN, "");
              }
          }

          //... 12 times MINUS some 2 things ...
          if (RegExUtils.hasArithmeticalTimesCollision(item))
              item = RegExUtils.fixArithmeticalTimesCollision(item);


          String prg = ((x++) < (items.length-1))? Constants.PARAGRAPH_TOKEN : "";

          sb.append(item + prg );
      }


      return sb.toString();
  }

  private String resolveIncludes(String plainRule, List<String> includeRegister, Locale locale, String[] workingDirectory, int level)throws Exception {
      String rule = Aliases.resolve(plainRule, "INCLUDE", locale);

      for (String line : rule.split(FileSystemUtils.NewLine)){
          if (line.contains("INCLUDE")){
              String includePath = line.replace("INCLUDE", "").trim();
              includePath = Matcher.quoteReplacement(includePath);

              //fallbacke to get include file
              if (!Paths.get(includePath).isAbsolute() && workingDirectory != null && workingDirectory.length > 0) {
                  String inclFile = includePath;

                  for(String basePath : workingDirectory) {
                      inclFile = Paths.get(basePath, includePath).toString();

                      if(FileSystemUtils.fileExists(inclFile)){
                          includePath = inclFile;
                          break;
                      }
                  }
              }

              FileSystemUtils.fileShouldExists(includePath, "an INCLUDE File: " + includePath + " could not be found." + System.lineSeparator() + "Rule Content: " + System.lineSeparator() + System.lineSeparator() + plainRule + System.lineSeparator());


              if (includeRegister.contains(includePath))
                  throw new RuntimeException("a circular reference detected while resolving INCLUDES. " + includePath + System.lineSeparator() + System.lineSeparator() + plainRule);


              String includePlainContent = FileSystemUtils.readFile(includePath);
              if (includePlainContent != null){
                  includeRegister.add(includePath);
                  includePlainContent = resolveIncludes(includePlainContent, includeRegister, locale, workingDirectory,level+2);

                  String formatString = "%"+level*4+"s";
                  String spaces = (level<1)? "" : String.format(formatString, " ");

                  rule = rule.replaceAll(Matcher.quoteReplacement(line),
                          //System.lineSeparator() + spaces + "//INCLUDE CONTENT FROM: " + Matcher.quoteReplacement(includePath) + System.lineSeparator() + System.lineSeparator() +
                                  includePlainContent.trim() + System.lineSeparator() + System.lineSeparator()
                          //        spaces + "//END OF INCLUDE FROM: " + Matcher.quoteReplacement(includePath) + System.lineSeparator()
                  );
              }
          }
      }

      return rule;
  }

  public String replaceVariableNames(String preprocessedRule){
      String out = "";

      if (!StringUtils.isNullOrEmpty(preprocessedRule)){
          List<String> itemss = StringUtils.splitAndRemoveEmpty(preprocessedRule, Constants.PARAGRAPH_TOKEN);

          if (itemss != null){
              for(String item : itemss){
                  if (!item.contains(Constants.IF_TOKEN) && !item.contains(Constants.MUST_TOKEN) && !item.contains(Constants.MUSTNOT_TOKEN) && item.contains(Constants.AS_TOKEN)){
                      int pos = item.indexOf(Constants.AS_TOKEN);
                      if (pos > -1){
                          int start = item.indexOf(" ", pos);

                          if (start > -1){
                              String varName = item.substring(start+1).trim();
                              if (!StringUtils.isNullOrEmpty(varName))
                                  item = item.replace(varName, NameMasking.maskVariableName(varName));
                          }
                      }
                  }

                  out += item + Constants.PARAGRAPH_TOKEN;
              }
          }
      }else {
          out = preprocessedRule;
      }

      return out;
  }
  */
}
