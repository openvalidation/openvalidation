package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.rest.model.dto.OpenValidationExceptionDTO;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.service.OVParams;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class TransformationParameter {
  private String culture;
  private String rule;
  private List<Entry<ASTItem, String>> itemMessagePair;
  private List<OpenValidationExceptionDTO> parsedErrors;

  public TransformationParameter(String culture) {
    this.culture = culture;
    this.itemMessagePair = new ArrayList<>();
    this.parsedErrors = new ArrayList<>();
  }

  public TransformationParameter(OVParams ovParams) {
    this(ovParams.getCulture());
    this.rule = ovParams.getRule();
  }

  public String getCulture() {
    return culture;
  }

  public void setCulture(String culture) {
    this.culture = culture;
  }

  public List<Entry<ASTItem, String>> getItemMessagePair() {
    return itemMessagePair;
  }

  public List<OpenValidationExceptionDTO> getParsedErrors() {
    return parsedErrors;
  }

  public void setItemMessagePair(List<OpenValidationException> errors) {
    for (OpenValidationException error : errors) {
      if (error instanceof ASTValidationException) {
        Entry<ASTItem, String> entry =
            new SimpleEntry<>(((ASTValidationException) error).getItem(), error.getMessage());
        this.itemMessagePair.add(entry);
      }
    }
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public void visitNode(DocumentSection section) {
    if (section == null || section.getItem() == null) return;

    ASTItem newItem = section.getItem();
    for (Entry<ASTItem, String> pair : this.itemMessagePair) {
      if (!newItem.equals(pair.getKey())) continue;

      OpenValidationExceptionDTO validationExceptionDTO =
          new OpenValidationExceptionDTO(pair.getValue(), section.getRange());
      this.parsedErrors.add(validationExceptionDTO);
    }
  }
}
