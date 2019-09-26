package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.rest.model.dto.astDTO.operation.ConditionNode;
import io.openvalidation.rest.model.dto.astDTO.operation.OperationNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.List;

public class TransformationHelper {
  public static ConditionNode getOwnConditionElement(
      String originalSource, ASTCondition condition, String culture) {
    if (originalSource == null || condition == null) return null;

    String[] splittedRule = originalSource.split(condition.getOriginalSource());
    if (splittedRule.length < 2) return null;

    List<String> thenKeyword = Aliases.getSpecificAliasByToken(culture, Constants.THEN_TOKEN);
    if (thenKeyword.size() == 0) return null;

    String[] newOperandString = splittedRule[1].split("(?i)" + thenKeyword.get(0));
    if (newOperandString.length < 1) return null;

    String newOriginalSource = newOperandString[0].replace("\n", "").trim();
    if (newOriginalSource.isEmpty()) return null;

    ASTCondition newCondition = new ASTCondition(newOriginalSource);
    DocumentSection newSection = new RangeGenerator(originalSource).generate(newCondition);

    return new OperationNode(newCondition, newSection, culture);
  }
}
