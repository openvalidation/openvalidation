package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;

public class UnkownNode extends GenericNode {
  private OperandNode content;

  public UnkownNode(OperandNode content, DocumentSection section) {
    super(section, null);
    this.content = content;
  }

  public UnkownNode(DocumentSection section) {
    super(section, null);
  }

  public OperandNode getContent() {
    return content;
  }
}
