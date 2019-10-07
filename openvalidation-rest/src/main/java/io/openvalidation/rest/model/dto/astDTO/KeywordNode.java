package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;

public class KeywordNode extends GenericNode {
  public KeywordNode(DocumentSection section) {
    super.initializeElement(section);
  }
}
