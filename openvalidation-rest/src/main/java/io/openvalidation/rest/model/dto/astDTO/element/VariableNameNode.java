package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;

public class VariableNameNode extends GenericNode {
  private String name;

  public VariableNameNode(DocumentSection section, String name) {
    super(section);

    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
