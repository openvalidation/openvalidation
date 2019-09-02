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

package io.openvalidation.core;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.interfaces.IOpenValidationGenerator;
import io.openvalidation.common.interfaces.IOpenValidationParser;
import io.openvalidation.common.interfaces.IOpenValidationPreprocessor;
import io.openvalidation.common.model.CodeGenerationResult;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.PreProcessorContext;
import io.openvalidation.common.validation.Validator;
import io.openvalidation.core.validation.ASTModelValidator;
import io.openvalidation.core.validation.ValidationContext;
import java.util.*;

public class Engine {

  private IOpenValidationParser _parser;
  private IOpenValidationGenerator _generator;
  private IOpenValidationPreprocessor _preProcessor;
  private OpenValidationOptions _options;
  private PreProcessorContext _preprocessorCtx = null;

  public Engine(
      IOpenValidationPreprocessor preprocessor,
      IOpenValidationParser parser,
      IOpenValidationGenerator generator,
      OpenValidationOptions options) {
    this._parser = parser;
    this._generator = generator;
    this._preProcessor = preprocessor;
    this._options = options;

    this._preprocessorCtx = new PreProcessorContext();

    //        this._preprocessorCtx.setLocale(_options.getLocale());
    //        this._preprocessorCtx.setWorkingDirectory(_options.getWorkingDirectories());
    //        this._preprocessorCtx.setSchema(_options.getSchema());
  }

  public CodeGenerationResult generateFramework(ASTModel ast, Language language) throws Exception {
    String framework = this._generator.generateFramework(ast, language);

    return CodeGenerationResult.createFrameworkResult(ast, framework);
  }

  public CodeGenerationResult generateValidatorFactory(
      Map<String, Object> params, Language language) throws Exception {
    String code = this._generator.generateValidatorFactory(params, language);

    return CodeGenerationResult.createValidatorFactoryResult(code);
  }

  public CodeGenerationResult generateCode(
      String plainRule, Locale locale, Language language, boolean isSingleFile) throws Exception {
    Validator.shouldNotBeEmpty(plainRule, "the Rule Set");

    // 500-600ms
    List<String> parsedRule = new ArrayList<>();
    ASTModel ast = this.parse(plainRule, locale, parsedRule);

    if (ast != null && this._options.getParams() != null) ast.addParams(this._options.getParams());

    this.validate(new ValidationContext(_options, ast, plainRule, parsedRule.get(0)));

    // 400ms
    String code = this._generator.generate(ast, language);

    CodeGenerationResult result =
        (isSingleFile)
            ? CodeGenerationResult.createCombined(ast, code)
            : CodeGenerationResult.createImplementation(ast, code);
    result.setName(_options.getOutputCodeFileName());

    return result;
  }

  public ASTModel parse(String plainRule, Locale locale) throws Exception {
    return parse(plainRule, locale, null);
  }

  public ASTModel parse(String plainRule, Locale locale, List<String> preprocessedRules)
      throws Exception {
    String ruleContent = this.preprocess(plainRule, locale);
    // TODO: parser takes 500ms
    ASTModel model = this._parser.parse(ruleContent, this._options.getSchema());

    if (preprocessedRules != null) preprocessedRules.add(ruleContent);

    //        if (this._options.isVerbose()){
    //            Console.print("\n\n-== PLAIN RULE ==-\n\n" + ruleContent + "\n\n");
    //            Console.print("\n\n-== AST MODEL ==-\n\n" + model.print()+ "\n\n");
    //        }

    // add additional params
    if (model != null) model.addParam("single_file", this._options.isSingleFile());

    return model;
  }

  public void validate(ValidationContext context) throws Exception {
    Validator.shouldNotBeEmpty(context, "ValidationContext");
    Validator.shouldNotBeEmpty(context.getAst(), "ASTModel");

    ASTModelValidator validator = new ASTModelValidator(context);
    validator.validate();
  }

  public String preprocess(String plainRule, Locale locale) throws Exception {
    Validator.contentsShouldNotBeEmpty(this._options.getWorkingDirectories(), "WorkingDirectories");

    this._preprocessorCtx.setWorkingDirectory(_options.getWorkingDirectories());
    this._preprocessorCtx.setLocale(locale);
    this._preprocessorCtx.setSchema(_options.getSchema());

    return this._preProcessor.process(plainRule, this._preprocessorCtx);
  }

  public void setOptions(OpenValidationOptions options) {
    this._options = options;
  }
}
