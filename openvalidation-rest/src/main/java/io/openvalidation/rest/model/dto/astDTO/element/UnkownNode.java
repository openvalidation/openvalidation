package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;

import java.util.List;

public class UnkownNode extends GenericNode {
  private GenericNode content;

  public UnkownNode(GenericNode content) {
    this.content = content;
    this.setRange(this.content.getRange());
    this.setLines(this.content.getLines());
  }

  public UnkownNode(DocumentSection section) {
    this.setRange(section.getRange());
    this.setLines(section.getLines());
  }

  public GenericNode getContent() {
    return content;
  }
}
