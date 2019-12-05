package io.openvalidation.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.openvalidation.common.ast.*;
import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.ASTValidationSummaryException;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.rest.model.dto.astDTO.*;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import io.openvalidation.rest.model.dto.astDTO.transformation.TreeTransformer;
import io.openvalidation.rest.model.dto.schema.SchemaDTO;
import io.openvalidation.rest.service.OVParams;
import java.util.List;

public class LintingResultDTO {

  @JsonAlias("main_ast_node")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MainNode mainAstNode;

  @JsonAlias("schema_list")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private SchemaDTO schema;

  private List<OpenValidationExceptionDTO> errors;

  public LintingResultDTO() {
    // serializable
  }

  public LintingResultDTO(
      OpenValidationResult ovResult, OVParams ovParams, List<ASTItem> astItemList) {
    if (ovResult == null)
      throw new IllegalArgumentException("OpenValidationResult should not be null");

    TransformationParameter parameter = new TransformationParameter(ovParams);
    parameter.setItemMessagePair(ovResult.getErrors());

    TreeTransformer transformer = new TreeTransformer(ovResult, astItemList, parameter);
    MainNode node = transformer.transform();
    this.setMainAstNode(node);

    try {
      DataSchema schema = SchemaConverterFactory.convert(ovParams.getSchema());
      this.setSchema(new SchemaDTO(schema));
    } catch (Exception ex) {
      System.err.print("Schema could not be generated");
    }

    if (node.getRange() == null) {
      String[] splittedDocument = ovParams.getRule().split("\n");
      if (splittedDocument.length > 0) {
        Position startPosition = new Position(0, 0);
        Position endPosition =
            new Position(
                splittedDocument.length - 1,
                splittedDocument[splittedDocument.length - 1].length());
        node.setRange(new Range(startPosition, endPosition));
      }
    }

    this.errors = parameter.getParsedErrors();
    for (OpenValidationException error : ovResult.getErrors()) {
      // These exceptions where generated during the tree-transformation
      if (error instanceof ASTValidationException && this.errors.size() > 0) continue;

      if (error instanceof ASTValidationSummaryException) {
        this.generateAndAddErrorDto((ASTValidationSummaryException) error, ovParams.getRule());
      } else {
        errors.add(new OpenValidationExceptionDTO(error.getMessage(), node.getRange()));
      }
    }
  }

  private void generateAndAddErrorDto(
      OpenValidationException error, MainNode node, String ruleParameter) {
    if (error instanceof ASTValidationSummaryException) {
      this.generateAndAddErrorDto((ASTValidationSummaryException) error, ruleParameter);
    } else {
      errors.add(new OpenValidationExceptionDTO(error.getMessage(), node.getRange()));
    }
  }

  private void generateAndAddErrorDto(ASTValidationSummaryException error, String ruleParameter) {
    String sourceString = error.getModel().getOriginalSource();
    if (sourceString.isEmpty()) return;

    DocumentSection newSection = new RangeGenerator(ruleParameter).generate(sourceString);
    errors.add(new OpenValidationExceptionDTO(error.getMessage(), newSection.getRange()));
  }

  public SchemaDTO getSchema() {
    return schema;
  }

  private void setSchema(SchemaDTO schema) {
    this.schema = schema;
  }

  public MainNode getMainAstNode() {
    return mainAstNode;
  }

  private void setMainAstNode(MainNode mainAstNode) {
    this.mainAstNode = mainAstNode;
  }

  public List<OpenValidationExceptionDTO> getErrors() {
    return errors;
  }

  public void setErrors(List<OpenValidationExceptionDTO> errors) {
    this.errors = errors;
  }
}
