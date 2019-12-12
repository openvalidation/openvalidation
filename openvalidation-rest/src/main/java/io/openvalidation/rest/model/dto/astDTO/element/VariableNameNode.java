package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.common.utils.Constants;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.Position;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.Collections;
import java.util.List;

public class VariableNameNode extends GenericNode {
  private String name;
  private Range variableNameRange;

  public VariableNameNode(DocumentSection section, String name, TransformationParameter parameter) {
    super(section, parameter);
    this.name = name;

    // Generates the range of the explicit name
    Range newRange = new Range(this.getRange());
    if (this.getKeywords().size() > 0) {
      Position newStart = new Position(this.getKeywords().get(0).getRange().getEnd());
      newStart.setColumn(newStart.getColumn() + 1);
      newRange.setStart(newStart);
    }
    this.variableNameRange = this.getKeywords().size() > 0 ? newRange : this.getRange();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Range getVariableNameRange() {
    return variableNameRange;
  }

  public List<String> getPotentialKeywords() {
    return Collections.singletonList(Constants.AS_TOKEN);
  }
}
