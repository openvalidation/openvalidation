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
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.MainNode;
import io.openvalidation.rest.model.dto.astDTO.Position;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import io.openvalidation.rest.model.dto.astDTO.transformation.TreeTransformer;
import io.openvalidation.rest.model.dto.schema.SchemaDTO;
import io.openvalidation.rest.service.OVParams;
import java.util.ArrayList;
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
      OpenValidationResult ovResult, OVParams parameters, List<ASTItem> astItemList) {
    if (ovResult == null)
      throw new IllegalArgumentException("OpenValidationResult should not be null");

    TreeTransformer transformer = new TreeTransformer(ovResult, astItemList, parameters);
    MainNode node = transformer.transform(parameters.getRule());
    this.setMainAstNode(node);

    try {
      DataSchema schema = SchemaConverterFactory.convert(parameters.getSchema());
      this.setSchema(new SchemaDTO(schema));
    } catch (Exception ex) {
      System.err.print("Schema could not be generated");
    }

    if (node.getRange() == null) {
      String[] splittedDocument = parameters.getRule().split("\n");
      Position startPosition = new Position(0, 0);
      Position endPosition =
          new Position(
              splittedDocument.length - 1, splittedDocument[splittedDocument.length - 1].length());
      node.setRange(new Range(startPosition, endPosition));
    }

    this.errors = new ArrayList<>();
    for (OpenValidationException error : ovResult.getErrors()) {
      this.generateAndAddErrorDto(error, node, parameters.getRule());
    }
  }

  private void generateAndAddErrorDto(
      OpenValidationException error, MainNode node, String ruleParameter) {
    if (error instanceof ASTValidationSummaryException) {
      this.generateAndAddErrorDto((ASTValidationSummaryException) error, ruleParameter);
    } else if (error instanceof ASTValidationException) {
      this.generateAndAddErrorDto((ASTValidationException) error, node, ruleParameter);
    } else {
      errors.add(new OpenValidationExceptionDTO(error.getMessage(), node.getRange()));
    }
  }

  private void generateAndAddErrorDto(
      ASTValidationException error, MainNode node, String ruleParameter) {
    // default position for errors that can't be mapped to a specific element
    ASTItem item = error.getItem();
    if (item == null) {
      errors.add(new OpenValidationExceptionDTO(error.getMessage(), node.getRange()));
      return;
    }

    String sourceString = item.getOriginalSource();

    // Sometimes the position is not zero-based
    int position = item.getGlobalPosition() == 0 ? 1 : item.getGlobalPosition();
    if (position > node.getScopes().size() || node.getScopes().size() == 0) {
      if (sourceString.isEmpty()) return;
      DocumentSection newSection = new RangeGenerator(ruleParameter).generate(sourceString);
      errors.add(new OpenValidationExceptionDTO(error.getMessage(), newSection.getRange()));
      return;
    }

    GenericNode generatedNode = node.getScopes().get(position - 1);
    sourceString =
        sourceString.isEmpty() ? String.join("\n", generatedNode.getLines()) : sourceString;
    DocumentSection newSection =
        new RangeGenerator(generatedNode.getLines(), generatedNode.getRange())
            .generate(sourceString + "\r");
    errors.add(new OpenValidationExceptionDTO(error.getMessage(), newSection.getRange()));
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
