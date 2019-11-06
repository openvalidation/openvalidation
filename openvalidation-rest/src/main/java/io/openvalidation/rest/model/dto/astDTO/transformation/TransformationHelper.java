package io.openvalidation.rest.model.dto.astDTO.transformation;

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.operation.ConditionNode;
import io.openvalidation.rest.model.dto.astDTO.operation.OperationNode;
import java.util.List;

public class TransformationHelper {
  // Used for a lonely "and" / "or" inside a condition-group which currently can't be parsed properly
  public static ConditionNode getOwnConditionElement(
      String originalSource, ASTCondition condition, String culture) {
    if (originalSource == null || condition == null) return null;

    String[] splittedRule = originalSource.split(condition.getOriginalSource());
    if (splittedRule.length < 2) return null;

    List<String> thenKeyword = Aliases.getAliasByToken(culture, Constants.THEN_TOKEN);
    if (thenKeyword.size() == 0) return null;

    String[] newOperandString = splittedRule[1].split("(?i)" + thenKeyword.get(0));
    if (newOperandString.length < 1) return null;

    String newOriginalSource = newOperandString[0].replace("\n", "").trim();
    if (newOriginalSource.isEmpty()) return null;

    List<String> connectionKeywords = Aliases.getAliasByToken(culture, Constants.AND_TOKEN, Constants.OR_TOKEN);
    if (!connectionKeywords.stream().anyMatch(string -> newOriginalSource.toLowerCase().contains(string.toLowerCase())))
      return null;

    ASTCondition newCondition = new ASTCondition(newOriginalSource);
    DocumentSection newSection = new RangeGenerator(originalSource).generate(newCondition);

    return new OperationNode(newCondition, newSection, new TransformationParameter(culture));
  }
}
