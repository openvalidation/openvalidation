package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.List;

public class KeywordNode {
  private List<String> lines;
  private Range range;

  public KeywordNode(DocumentSection section) {
    this.lines = section.getLines();
    this.range = section.getRange();
  }

  public List<String> getLines() {
    return lines;
  }

  public Range getRange() {
    return range;
  }

  public String getType() {
    return this.getClass().getSimpleName();
  }
}
