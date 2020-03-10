package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import java.util.function.Predicate;

public class PostModelArrayComparisonResolver
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  @Override
  protected Predicate<? super ASTCondition> getFilter() {
    return c ->
        c.hasLeftOperand() && c.hasRightOperand() && c.getRightOperand() instanceof ASTOperandArray;
  }

  @Override
  protected void processItem(ASTCondition condition) {
    if (condition.getOperator() == ASTComparisonOperator.EQUALS)
      condition.setOperator(ASTComparisonOperator.AT_LEAST_ONE_OF);
    else if (condition.getOperator() == ASTComparisonOperator.NOT_EQUALS)
      condition.setOperator(ASTComparisonOperator.NONE_OF);
  }
}
