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

package io.openvalidation.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.CodeGenerationResult;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.rest.model.dto.astDTO.MainNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.TreeTransformer;
import io.openvalidation.rest.model.dto.schema.SchemaDTO;
import io.openvalidation.rest.service.OVParams;
import java.util.ArrayList;
import java.util.List;

public class GenerationResultDTO {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<OpenValidationException> errors = new ArrayList<>();

  @JsonAlias("implementation_result")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String implementationResult;

  @JsonAlias("framework_result")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String frameworkResult;

  @JsonAlias("variable_names")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<String> variableNames;

  @JsonAlias("static_strings")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<String> staticStrings;

  @JsonAlias("rule_errors")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<String> ruleErrors;

  @JsonAlias("main_ast_node")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MainNode mainAstNode;

  @JsonAlias("schema_list")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private SchemaDTO schema;

  public GenerationResultDTO() {
    // serializable
  }

  public GenerationResultDTO(
      OpenValidationResult ovResult, OVParams parameters, List<ASTItem> astItemList) {
    if (ovResult == null)
      throw new IllegalArgumentException("OpenValidationResult should not be null");
    if (ovResult.hasErrors()) errors = ovResult.getErrors();

    CodeGenerationResult frameworkGenerationResult = ovResult.getFrameworkResult();

    this.setFrameworkResult(
        frameworkGenerationResult == null ? "" : ovResult.getFrameworkResult().getCode());
    this.setImplementationResult(ovResult.getImplementationCodeContent());

    TreeTransformer transformator = new TreeTransformer(ovResult, astItemList, parameters);
    MainNode node = transformator.transform(parameters.getRule());
    this.setMainAstNode(node);

    try {
      DataSchema schema = SchemaConverterFactory.convert(parameters.getSchema());
      this.setSchema(new SchemaDTO(schema));
    } catch (Exception ex) {
      System.err.print("Schema could not be generated");
    }

    ASTModel ast = ovResult.getASTModel();

    if (ast != null) {
      this.setVariableNames(LINQ.select(ast.getVariables(), v -> v.getName()));
      this.setStaticStrings(
          LINQ.select(ast.collectItemsOfType(ASTOperandStaticString.class), s -> s.getValue()));

      this.setRuleErrors(
          LINQ.select(
              LINQ.where(
                  ast.collectItemsOfType(ASTRule.class),
                  r ->
                      !r.isConstrainedRule()
                          && r.getAction() != null
                          && r.getAction() instanceof ASTActionError),
              r -> ((ASTActionError) r.getAction()).getErrorMessage()));
    }
  }

  public List<OpenValidationException> getErrors() {
    return errors;
  }

  public void setErrors(List<OpenValidationException> errors) {
    this.errors = errors;
  }

  public SchemaDTO getSchema() {
    return schema;
  }

  public void setSchema(SchemaDTO schema) {
    this.schema = schema;
  }

  public String getImplementationResult() {
    return implementationResult;
  }

  public void setImplementationResult(String implementationResult) {
    this.implementationResult = implementationResult;
  }

  public String getFrameworkResult() {
    return frameworkResult;
  }

  public void setFrameworkResult(String frameworkResult) {
    this.frameworkResult = frameworkResult;
  }

  public List<String> getVariableNames() {
    return variableNames;
  }

  public void setVariableNames(List<String> variableNames) {
    this.variableNames = variableNames;
  }

  public List<String> getStaticStrings() {
    return staticStrings;
  }

  public void setStaticStrings(List<String> staticStrings) {
    this.staticStrings = staticStrings;
  }

  public List<String> getRuleErrors() {
    return ruleErrors;
  }

  public void setRuleErrors(List<String> ruleErrors) {
    this.ruleErrors = ruleErrors;
  }

  public MainNode getMainAstNode() {
    return mainAstNode;
  }

  public void setMainAstNode(MainNode mainAstNode) {
    this.mainAstNode = mainAstNode;
  }
}
