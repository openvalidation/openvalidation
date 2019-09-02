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

package io.openvalidation.integration.generator;

import io.openvalidation.common.model.CodeGenerationResult;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.Console;
import io.openvalidation.core.OpenValidation;
import io.openvalidation.integration.generator.model.IntegrationTest;
import java.util.ArrayList;
import java.util.List;

public class RuleGenerator {
  public static List<OpenValidationResult> generate(List<IntegrationTest> tests, String outputDir)
      throws Exception {
    boolean freameworkGeneration = false;

    List<OpenValidationResult> allResults = new ArrayList<>();

    for (IntegrationTest test : tests) {

      Console.printl("generated test case: " + test.getTestName());

      String name = test.getMaskedTestName();

      OpenValidation ov = OpenValidation.createDefault();
      ov.setLocale("en");
      ov.setLanguage(Language.Java);
      ov.setRule(test.getRule());
      ov.setSchema(test.getSchema());
      ov.setOutput(outputDir + "io.openvalidation/integration/tests/");
      ov.setVerbose(true);

      String packageName = "io.openvalidation.integration.tests";
      String packageModelName = packageName + "." + name;

      ov.setParam("model_type", packageModelName + ".Model");
      ov.setParam("generated_class_namespace", packageName);
      ov.setParam("generated_class_name", "TESTValidator_" + name);

      OpenValidationResult result = ov.generate(true);

      if (result.hasErrors()) {
        Console.error(
            "\n\n################# ERROR IN" + test.getTestName() + "######################\n\n");
        Console.error("###\n");
        Console.error("### " + test.getTestFile() + "\n");
        Console.error("###\n");
        Console.print(result.getRuleSetPrint());
        Console.print(result.getASTModelPrint());
        Console.print(result.getErrorPrint(true));
        Console.error(
            "\n\n############# END OF ERROR" + test.getTestName() + "##################\n\n");
      }

      /*
      Console.print(result.getCreatedFilesPrint());
      Console.print(result.getASTModelPrint());
      Console.print(result.getRuleSetPrint());
      */

      ModelGenerator.generate(test.getSchema(), outputDir, packageModelName, "Model");

      if (!freameworkGeneration) {
        OpenValidation ov2 = OpenValidation.createDefault();
        ov2.setLocale("en");
        ov2.setLanguage(Language.Java);
        ov2.setParam("generated_class_namespace", packageName);

        CodeGenerationResult frameworkResult =
            ov2.generateFrameworkFile(outputDir + "io.openvalidation/integration/tests/");
        result.addResult(frameworkResult);

        freameworkGeneration = true;
      }
      allResults.add(result);
    }

    return allResults;
  }
}
