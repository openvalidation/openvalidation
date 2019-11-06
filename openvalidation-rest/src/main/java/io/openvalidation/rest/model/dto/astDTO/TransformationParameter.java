package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.rest.service.OVParams;

import java.util.ArrayList;
import java.util.List;

public class TransformationParameter {
  private String culture;
  private String rule;
  private List<ASTItem> errorCauses;

  public TransformationParameter(String culture) {
    this.culture = culture;
    this.errorCauses = new ArrayList<>();
  }

  public TransformationParameter(OVParams ovParams) {
    this(ovParams.getCulture());
    this.rule = ovParams.getRule();
  }

  public TransformationParameter(String culture, List<ASTItem> errorCauses) {
    this(culture);
    this.errorCauses = errorCauses;
  }

  public String getCulture() {
    return culture;
  }

  public void setCulture(String culture) {
    this.culture = culture;
  }

  public List<ASTItem> getErrorCauses() {
    return errorCauses;
  }

  public void setErrorCauses(List<OpenValidationException> errorCauses) {
    for (OpenValidationException error : errorCauses) {
      if (error instanceof ASTValidationException) {
        this.errorCauses.add(((ASTValidationException) error).getItem());
      }
    }
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public void visitNode(ASTItem newItem) {
    for (ASTItem item: this.errorCauses) {
      boolean equals = newItem.equals(item);
      System.out.println(equals);
    }
  }
}
