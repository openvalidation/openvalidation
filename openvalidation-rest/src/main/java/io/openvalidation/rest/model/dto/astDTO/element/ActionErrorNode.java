package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.Position;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.Collections;
import java.util.List;

public class ActionErrorNode extends GenericNode {
  private String errorMessage;
  private Range actionErrorRange;

  public ActionErrorNode(
      DocumentSection section, ASTActionError error, TransformationParameter parameter) {
    super(section, parameter);
    if (error == null) return;

    this.errorMessage = error.getErrorMessage();

    // Generates the range of the explicit name
    if (this.getRange() != null) {
      Range newRange = new Range(this.getRange());
      if (this.getKeywords().size() > 0) {
        Position newStart = new Position(this.getKeywords().get(0).getRange().getEnd());
        newStart.setColumn(newStart.getColumn() + 1);
        newRange.setStart(newStart);
      }
      this.actionErrorRange = this.getKeywords().size() > 0 ? newRange : this.getRange();
    }
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Range getActionErrorRange() {
    return actionErrorRange;
  }

  public List<String> getPotentialKeywords() {
    return Collections.singletonList(Constants.THEN_TOKEN);
  }
}
