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

import static io.openvalidation.common.utils.LINQ.ofType;

import io.openvalidation.antlr.OpenValidationANTLRParser;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.ASTValidationSummaryException;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.interfaces.IOpenValidationGenerator;
import io.openvalidation.common.interfaces.IOpenValidationParser;
import io.openvalidation.common.interfaces.IOpenValidationPreprocessor;
import io.openvalidation.common.model.CodeGenerationResult;
import io.openvalidation.common.model.CodeKind;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.FileSystemUtils;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.common.validation.Validator;
import io.openvalidation.core.preprocessing.DefaultPreProcessor;
import io.openvalidation.generation.OpenValidationGenerator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenValidation {

  private OpenValidationOptions _options;
  private Engine _engine;

  public OpenValidation(
      IOpenValidationPreprocessor preprocessor,
      IOpenValidationParser parser,
      IOpenValidationGenerator generator) {
    this._options = new OpenValidationOptions();
    _engine = new Engine(preprocessor, parser, generator, this._options);
  }

  public OpenValidationResult generate() throws Exception {
    return generate(false);
  }

  public OpenValidationResult generate(boolean disableFrameworkGeneration) throws Exception {
    OpenValidationResult result =
        (_options.hasFileOutput())
            ? this.generateCodeFile(disableFrameworkGeneration)
            : this.generateCode(disableFrameworkGeneration);
    return result;
  }

  public OpenValidationResult generateCode(boolean disableFrameworkGeneration) {
    OpenValidationResult result = new OpenValidationResult();
    String plainRule = null;

    try {
      validateOptions();

      plainRule = this.getPlainRule();

      Validator.shouldNotBeEmpty(plainRule, "PlainRule");

      result.setPlainRule(plainRule);

      CodeGenerationResult res =
          this.generateCode(plainRule, _options.getLanguage(), _options.isSingleFile());
      result.addResult(res);

      if (!disableFrameworkGeneration && !_options.isSingleFile()) {
        CodeGenerationResult frameworkRes =
            this._engine.generateFramework(res.getAST(), _options.getLanguage());
        result.addResult(frameworkRes);
      }
    } catch (Throwable e) {
      if (e instanceof ASTValidationSummaryException) {
        ASTValidationSummaryException se = (ASTValidationSummaryException) e;
        result.addErrors(ofType(se.getErrors(), OpenValidationException.class));
        result.setASTModel(se.getModel());
      } else if (e instanceof OpenValidationException) result.addError((OpenValidationException) e);
      else if (e instanceof Exception) {
        if (e.getCause() instanceof ASTValidationSummaryException) {
          ASTValidationSummaryException se = (ASTValidationSummaryException) e.getCause();
          result.addErrors(ofType(se.getErrors(), OpenValidationException.class));
          result.setASTModel(se.getModel());
        } else if (e.getCause() instanceof ASTValidationException) {
          result.addError((ASTValidationException) e.getCause());
        } else
          result.addError(
              new OpenValidationException(
                  "ERROR while compile language " + _options.getLanguage().name() + ":",
                  (Exception) e));
      } else
        result.addError(
            new OpenValidationException(
                "ERROR while compile language " + _options.getLanguage().name() + ":",
                new Exception(e)));

    } finally {
      // append preprocessed rule als debug info
      try {
        result.setPreprocessedRule(this._engine.preprocess(plainRule, this._options.getLocale()));
      } catch (Throwable exp) {
        int x = 0;
      }
    }

    return result;
  }

  private void validateOptions() throws OpenValidationException {

    Validator.shouldNotBeEmpty(_options, "Options");
    Validator.shouldNotBeEmpty(_options.getLanguage(), "Language");
    Validator.shouldNotBeEmpty(_options.getRuleContent(), "Rule");
    Validator.shouldNotBeEmpty(_options.getRuleOptionKind(), "RuleKind");
    Validator.shouldNotBeEmpty(_options.getSchema(), "Schema");
    Validator.shouldNotBeEmpty(_options.getLocale(), "Culture");
  }

  private CodeGenerationResult generateCode(
      String plainRule, Language language, boolean isSingleFile) throws Exception {
    // TODO: called twice, and takes more than 300ms. remove second call!!!
    // Aliases.validateAliases();

    return this._engine.generateCode(plainRule, this._options.getLocale(), language, isSingleFile);
  }

  public OpenValidationResult generateCodeFile(boolean disableFrameworkGeneration)
      throws Exception {
    OpenValidationResult result = this.generateCode(disableFrameworkGeneration);

    if (result != null && !result.hasErrors()) {
      for (CodeGenerationResult res : result.getResults()) {
        res.setCodeFileName(this.writeCodeFile(res));
      }
    }

    return result;
  }

  public String writeCodeFile(CodeGenerationResult result) throws Exception {
    String name =
        (result.getCodeKind() == CodeKind.Implementation
                && !StringUtils.isNullOrEmpty(_options.getOutImplementationClassName()))
            ? _options.getOutImplementationClassName()
            : result.getName();

    String codeFileName = this._options.resolveCodeFileName(name);
    FileSystemUtils.writeFile(codeFileName, result.getCode());
    return codeFileName;
  }

  public CodeGenerationResult generateFramework(ASTModel ast) throws Exception {
    CodeGenerationResult result = this._engine.generateFramework(ast, this._options.getLanguage());

    if (this._options.hasFileOutput()) this.writeCodeFile(result);

    return result;
  }

  public CodeGenerationResult generateFrameworkFile(String outputDir) throws Exception {
    ASTModel ast = new ASTModel();
    ast.addParams(_options.getParams());

    CodeGenerationResult result = this.generateFramework(ast);

    String outFileName =
        ((Paths.get(outputDir, result.getName())).toString()
            + "."
            + _options.getOutCodeFileExtension(this._options.getLanguage()));
    FileSystemUtils.writeFile(outFileName, result.getCode());

    return result;
  }

  public CodeGenerationResult generateValidatorFactoryFile(String outputDir) throws Exception {
    CodeGenerationResult result = this.generateValidatorFactory(_options.getParams());

    String outFileName =
        ((Paths.get(outputDir, result.getName())).toString()
            + "."
            + _options.getOutCodeFileExtension(this._options.getLanguage()));
    FileSystemUtils.writeFile(outFileName, result.getCode());

    return result;
  }

  public CodeGenerationResult generateValidatorFactory(Map<String, Object> params)
      throws Exception {
    CodeGenerationResult result =
        this._engine.generateValidatorFactory(params, this._options.getLanguage());

    if (this._options.hasFileOutput()) this.writeCodeFile(result);

    return result;
  }

  public ASTModel parse() throws Exception {
    List<ASTModel> out = new ArrayList<>();
    String plainRule = this.getPlainRule();

    return this.parseAST(plainRule);
  }

  private ASTModel parseAST(String plainRule) throws Exception {
    return this._engine.parse(plainRule, this._options.getLocale());
  }

  public String preprocess() throws Exception {
    return this._engine.preprocess(this.getPlainRule(), this._options.getLocale());
  }

  // Builder Methods

  public OpenValidation appendCustomAliasesFromFile(String file) throws FileNotFoundException {
    Aliases.appendCustomAliasesFromFile(file);

    return this;
  }

  public OpenValidation appendCustomAliases(Map<String, String> aliases) {
    Aliases.appendCustomAliases(aliases);
    return this;
  }

  public OpenValidation appendCustomAliases(String key, String value) {
    Aliases.appendCustomAliases(key, value);
    return this;
  }

  public OpenValidation setLocale(String language) throws Exception {
    this._options.setLocale(language);
    return this;
  }

  public OpenValidation setLanguage(Language language) {
    this._options.setLanguage(language);
    return this;
  }

  public OpenValidation setOutputCodeFileName(String fileName) {
    this._options.setOutputCodeFileName(fileName);
    return this;
  }

  public OpenValidation setRule(String ruleContentOrURLOrFilePath) throws Exception {
    this._options.setRuleOption(ruleContentOrURLOrFilePath);
    return this;
  }

  public OpenValidation setOutput(String fileOrFolderPath) {
    this._options.setOutputDirectory(fileOrFolderPath);
    return this;
  }

  public OpenValidation setVerbose(boolean verbose) {
    this._options.setVerbose(verbose);
    return this;
  }

  public OpenValidation setSingleFile(boolean isSingle) {
    this._options.setSingleFile(isSingle);
    return this;
  }

  public OpenValidation setParams(Map<String, Object> params) {
    this._options.setParams(params);
    return this;
  }

  public OpenValidationOptions getOptions() {
    return this._options;
  }

  public String getPlainRule() throws Exception {
    switch (this._options.getRuleOptionKind()) {
      case Content:
        return this._options.getRuleContent();
      case URL:
      case FilePath:
        try {
          return FileSystemUtils.readFile(this._options.getRuleContent());
        } catch (IOException e) {
          // TODO 21.8.19 SB establish logging: warn that file has not been found and kind reverts
          // to content
        }
        return this._options.getRuleContent();
      default:
        throw new OpenValidationException("Unknown RuleKind.");
    }
  }

  public void setSchema(String schema) throws Exception {
    this._options.setSchema(schema);
  }

  public DataSchema getSchema() {
    return this._options.getSchema();
  }

  public OpenValidation setOptions(OpenValidationOptions options) {
    this._options = options;
    this._engine.setOptions(options);
    return this;
  }

  public OpenValidation setParam(String paramName, String value) {
    this._options.setParam(paramName, value);
    return this;
  }

  public static OpenValidation createDefault() {
    return new OpenValidation(
        new DefaultPreProcessor(), new OpenValidationANTLRParser(), new OpenValidationGenerator());
  }
}
