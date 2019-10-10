package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;

public class ActionErrorNode extends GenericNode {
  private String errorMessage;

  public ActionErrorNode(DocumentSection section, ASTActionError error) {
    super(section);
    this.errorMessage = error.getErrorMessage();
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
