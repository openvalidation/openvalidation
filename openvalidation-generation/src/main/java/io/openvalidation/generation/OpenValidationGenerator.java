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

package io.openvalidation.generation;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.interfaces.IOpenValidationGenerator;
import io.openvalidation.common.log.ProcessLogger;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.utils.ResourceUtils;
import java.util.Map;

public class OpenValidationGenerator implements IOpenValidationGenerator {

  @Override
  public String generate(ASTModel ast, Language language) throws Exception {
    return generate(ast, language, "main");
  }

  @Override
  public String generateFramework(ASTModel ast, Language language) throws Exception {
    return generate(ast, language, "framework");
  }

  @Override
  public String generateValidatorFactory(Map<String, Object> params, Language language)
      throws Exception {
    ASTModel ast = new ASTModel();
    ast.addParams(params);
    return generate(ast, language, "validatorfactory");
  }

  private String generate(ASTModel ast, Language language, String template) throws Exception {

    try {
      String templateFile = "/" + language.getName().toLowerCase() + "/" + template + ".hbs";

      if (!ResourceUtils.exists(templateFile)) {
        if (template.equalsIgnoreCase("main"))
          throw new OpenValidationException(
              "Generation of '"
                  + language.getName()
                  + "' Language is not implemented. Missing main.hbs template.");
        else
          throw new OpenValidationException(
              "Generation of '"
                  + language.getName()
                  + "' Language failed. Missing '"
                  + templateFile
                  + "' template.");
      }

      // set default params
      ast.setDefault("generated_class_name", "HUMLValidator");

      String code = CodeGenerator.generate(language.getName().toLowerCase(), template, ast, false);

      ProcessLogger.success(ProcessLogger.GENERATOR);
      return code;
    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.GENERATOR);
      throw e;
    }
  }
}
