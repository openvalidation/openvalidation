package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.rest.model.dto.astDTO.GenericNode;

public class UnkownNode extends GenericNode {
  private GenericNode content;

  public UnkownNode(GenericNode content) {
    this.content = content;
    this.setRange(this.content.getRange());
    this.setLines(this.content.getLines());
  }

  public GenericNode getContent() {
    return content;
  }
}
