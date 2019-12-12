package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.ArrayList;
import java.util.List;

public class UnknownNode extends GenericNode {
  private OperandNode content;

  public UnknownNode(OperandNode content, DocumentSection section) {
    super(section, null);
    this.content = content;
  }

  public UnknownNode(DocumentSection section) {
    super(section, null);
  }

  public OperandNode getContent() {
    return content;
  }

  public List<String> getPotentialKeywords() {
    return new ArrayList<>();
  }
}
