package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;

public class UnkownNode extends GenericNode {
  private OperandNode content;

  public UnkownNode(OperandNode content) {
    this.content = content;
    this.setRange(this.content.getRange());
    this.setLines(this.content.getLines());
  }

  public UnkownNode(DocumentSection section) {
    this.setRange(section.getRange());
    this.setLines(section.getLines());
  }

  public OperandNode getContent() {
    return content;
  }
}
