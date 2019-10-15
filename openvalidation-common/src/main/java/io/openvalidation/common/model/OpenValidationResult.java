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

package io.openvalidation.common.model;

import static io.openvalidation.common.utils.LINQ.ofType;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.ASTValidationSummaryException;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.utils.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OpenValidationResult {

  private List<CodeGenerationResult> _results = new ArrayList<>();
  private List<OpenValidationException> _exceptions = new ArrayList<>();

  private ASTModel _model;
  private String _preprocessedRule = "";
  private String _plainRule = "";

  public OpenValidationResult() {}

  public List<CodeGenerationResult> getResults() {
    return this._results;
  }

  public void addResult(CodeGenerationResult result) {
    this._results.add(result);

    if (result.getAST() != null && this.getASTModel() == null) this.setASTModel(result.getAST());

    if (result.getPreprocessedRule() != null && this.getPreprocessedRule() == null)
      this.setPreprocessedRule(result.getPreprocessedRule());
  }

  public void addResults(List<CodeGenerationResult> results) {
    this._results.addAll(results);
  }

  public void addError(OpenValidationException exp) {
    this._exceptions.add(exp);
  }

  public void addErrors(List<OpenValidationException> exps) {
    this._exceptions.addAll(exps);
  }

  public List<OpenValidationException> getErrors() {
    return this._exceptions;
  }

  public List<String> getAllErrorMessages() {
    return this.getErrors().stream()
        .filter(e -> e.getUserMessage() != null)
        .map(e -> e.getUserMessage())
        .collect(Collectors.toList());
  }

  public List<String> getAllValidationErrorMessages() {
    return this.getValidationErros().stream()
        .filter(e -> e.getUserMessage() != null)
        .map(e -> e.getUserMessage())
        .collect(Collectors.toList());
  }

  public List<ASTValidationException> getValidationErros() {
    List<ASTValidationException> errors = ofType(_exceptions, ASTValidationException.class);
    List<ASTValidationSummaryException> summaryExceptions =
        ofType(_exceptions, ASTValidationSummaryException.class);

    List<ASTValidationException> summaryErrors =
        summaryExceptions.stream()
            .filter(e -> e.hasErrors())
            .flatMap(e -> e.getErrors().stream())
            .collect(Collectors.toList());

    errors.addAll(summaryErrors);
    return errors;
  }

  public boolean hasErrors() {
    return _exceptions.size() > 0;
  }

  public String getAllCodeContent() {
    return String.join(
        "\n", this.getResults().stream().map(r -> r.getCode()).collect(Collectors.toList()));
  }

  public String getImplementationCodeContent() {
    return String.join(
        "\n",
        this.getImplementationResults().stream()
            .map(r -> r.getCode())
            .collect(Collectors.toList()));
  }

  public CodeGenerationResult getFrameworkResult() {
    Optional<CodeGenerationResult> res =
        this.getResults().stream().filter(r -> r.getCodeKind() == CodeKind.Framework).findFirst();

    return (res.isPresent()) ? res.get() : null;
  }

  public List<CodeGenerationResult> getImplementationResults() {
    return this.getResults().stream()
        .filter(
            r -> r.getCodeKind() == CodeKind.Implementation || r.getCodeKind() == CodeKind.Combined)
        .collect(Collectors.toList());
  }

  public CodeGenerationResult getValidatorFactoryResult() {
    Optional<CodeGenerationResult> res =
        this.getResults().stream()
            .filter(r -> r.getCodeKind() == CodeKind.ValidatorFactory)
            .findFirst();

    return (res.isPresent()) ? res.get() : null;
  }

  public static OpenValidationResult createErrorResult(Exception exp) {
    OpenValidationResult result = new OpenValidationResult();
    result.addError(new OpenValidationException("", exp));
    return result;
  }

  public static OpenValidationResult createErrorResult(String errorMessage) {
    OpenValidationResult result = new OpenValidationResult();
    result.addError(new OpenValidationException(errorMessage));
    return result;
  }

  public void setASTModel(ASTModel model) {
    this._model = model;
  }

  public ASTModel getASTModel() {

    if (this._model == null && this._results != null && this._results.size() > 0) {
      this._model = this._results.get(0).getAST();
    }

    return this._model;
  }

  public List<String> getCreatedFiles() {
    return LINQ.select(getResults(), r -> r.getCodeFileName());
  }

  public void setPreprocessedRule(String rule) {
    this._preprocessedRule = rule;
  }

  public String getPreprocessedRule() {
    return this._preprocessedRule;
  }

  public void setPlainRule(String rule) {
    this._plainRule = rule;
  }

  public String getPlainRule() {
    return this._plainRule;
  }

  // prints
  public String toString(boolean verbose) {
    StringBuilder sb = new StringBuilder();

    if (verbose) {
      sb.append(getASTModelPrint());
      sb.append(
          ConsoleColors.GRAY
              + "\nPLAIN RULE \u2B9F \n\n"
              + ConsoleColors.RESET
              + this._plainRule
              + "\n\n");

      String preprrul =
          (this._preprocessedRule != null)
              ? this._preprocessedRule.replaceAll(
                  Constants.PARAGRAPH_TOKEN, "\n\n" + Constants.PARAGRAPH_TOKEN + "\n\n")
              : "";

      sb.append(
          ConsoleColors.GRAY
              + "\nPREPROCESSED RULE \u2B9F \n\n"
              + ConsoleColors.RESET
              + preprrul
              + "\n\n");
    }

    return sb.toString();
  }

  public String getErrorPrint(boolean verbose) {
    StringBuilder sb = new StringBuilder();

    this._exceptions.forEach(
        e -> {
          sb.append(e.toString(verbose));
        });

    return sb.toString();
  }

  public String getASTModelPrint() {
    if (this._model != null) {
      StringBuilder sb = new StringBuilder();

      sb.append(Console.getTitleStart("AST MODEL"));
      sb.append(this._model.print());
      sb.append("\n\n\n");

      return sb.toString();
    }

    return "";
  }

  public String getRuleSetPrint() {
    StringBuilder sb = new StringBuilder();

    sb.append(Console.getTitleStart("PLAIN RULE"));
    sb.append(this.getPlainRule() + "\n");
    sb.append(Console.getTitleStart("PREPROCESSED RULE"));
    sb.append(this.getPreprocessedRule() + "\n\n");

    return sb.toString();
  }

  public String getCreatedFilesPrint() {
    StringBuilder sb = new StringBuilder();

    if (getCreatedFiles().size() > 0) {
      sb.append(Console.getTitleStart("CREATED FILES"));
      sb.append(String.join("\n", getCreatedFiles()) + "\n\n");
    }

    return sb.toString();
  }

  public String getSummaryPrint() {
    return getSummaryPrint("");
  }

  public String getSummaryPrint(String name) {
    StringBuilder sb = new StringBuilder();

    ASTModel model = getASTModel();

    String factoryRes = (getValidatorFactoryResult() != null) ? "1" : "0";
    String factoryResState =
        (getValidatorFactoryResult() != null)
            ? Console.successString("SUCCESS")
            : Console.grayString("SKIPPED");

    String frameworkRes = (getFrameworkResult() != null) ? "1" : "0";
    String frameworkResState =
        (getFrameworkResult() != null)
            ? Console.successString("SUCCESS")
            : Console.grayString("SKIPPED");

    int implementationResults =
        (getImplementationResults() != null) ? getImplementationResults().size() : 0;
    String implementationResState =
        (implementationResults > 0)
            ? Console.successString("SUCCESS")
            : Console.errorString("FAILED");

    int variablesResults =
        (model != null && model.getVariables() != null) ? model.getVariables().size() : 0;
    String variablesResState =
        (variablesResults > 0) ? Console.successString("SUCCESS") : Console.grayString("SKIPPED");

    int rulesResults = (model != null && model.getRules() != null) ? model.getRules().size() : 0;
    String rulesResState =
        (rulesResults > 0) ? Console.successString("SUCCESS") : Console.errorString("FAILED");

    int errorsResults = (getErrors() != null) ? getErrors().size() : 0;
    String errorsResState =
        (errorsResults > 0) ? Console.errorString("FAILED") : Console.successString("SUCCESS");

    int filesResults = getCreatedFiles().size();
    String filesResState =
        (filesResults > 0) ? Console.successString("SUCCESS") : Console.grayString("SKIPPED");

    String summaryName = "code generation summary";
    if (!StringUtils.isNullOrEmpty(name)) summaryName += " : " + name;

    if (model != null) {

      sb.append(Console.grayString("\n\n------------    " + summaryName + "    ------------\n\n"))
          .append(
              "Rules  "
                  + Console.grayString("................................  ")
                  + StringUtils.padLeft(rulesResults, 4)
                  + "  "
                  + rulesResState
                  + "\n")
          .append(
              "Variables  "
                  + Console.grayString("............................  ")
                  + StringUtils.padLeft(variablesResults, 4)
                  + "  "
                  + variablesResState
                  + "\n")
          .append(
              "Implementation  "
                  + Console.grayString(".......................  ")
                  + StringUtils.padLeft(implementationResults, 4)
                  + "  "
                  + implementationResState
                  + "\n")
          .append(
              "Framework  "
                  + Console.grayString("............................  ")
                  + StringUtils.padLeft(frameworkRes, 4)
                  + "  "
                  + frameworkResState
                  + "\n")
          .append(
              "Factory  "
                  + Console.grayString("..............................  ")
                  + StringUtils.padLeft(factoryRes, 4)
                  + "  "
                  + factoryResState
                  + "\n")
          .append(
              "Files created  "
                  + Console.grayString("........................  ")
                  + StringUtils.padLeft(filesResults, 4)
                  + "  "
                  + filesResState
                  + "\n")
          .append(
              "Errors  "
                  + Console.grayString("...............................  ")
                  + StringUtils.padLeft(errorsResults, 4)
                  + "  "
                  + errorsResState
                  + "\n")
          .append(Console.grayString("\n-------------------------------------------------------\n"))
          .append("\n\n\n");
    }

    return sb.toString();
  }
}
