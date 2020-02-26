package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import java.nio.channels.NotYetBoundException;
import java.util.function.Predicate;

/*
This class is used to resolve Arrays have been parsed incorrectly as an ASTStaticString(1) or an incomplete ASTConditionGroup(2).
Depending if (1) or (2) an attempt to resolve the array in a specific way is initiated.

Example for (1):
    Berlin, Paris, London as capital cities
    -> the variable's value will initially be parsed as a string with a value of 'Berlin, Paris, London'

Example for (2):
    Berlin, Paris or London as capital cities
    -> the variable's value will be initially parsed as a condition group with the first condition only containing the left string
        operand 'Berlin, Paris' and the second condition containing the connector 'OR' and the left string operand 'London'.
 */
public class PostModelArrayInVariableResolver
    extends PostProcessorSubelementBase<ASTModel, ASTVariable> {
  @Override
  protected Predicate<? super ASTVariable> getFilter() {
    return v -> isVarWithArrParsedAsStaticString(v) || isVarWithArrParsedAsCondGrp(v);
  }

  @Override
  protected void processItem(ASTVariable item) {
    if (isVarWithArrParsedAsStaticString(item)) {
      ASTOperandArray array = resolveArrFromString((ASTOperandStaticString) item.getValue());
      item.setValue(array);
    } else if (isVarWithArrParsedAsCondGrp(item)) {
      throw new NotYetBoundException();
    } else {
      throw new IllegalStateException();
    }
  }

  private ASTOperandArray resolveArrFromString(ASTOperandStaticString stringOperand) {
    return PostProcessorUtils.resolveArrayInOperand(stringOperand);
  }

  private boolean isVarWithArrParsedAsStaticString(ASTVariable variable) {
    ASTOperandBase varOp = variable.getValue();

    return varOp instanceof ASTOperandStaticString
        && ((ASTOperandStaticString) varOp).getValue().contains(",");
  }

  private boolean isVarWithArrParsedAsCondGrp(ASTVariable variable) {
    ASTOperandBase varOp = variable.getValue();

    return false;
    //        return varOp instanceof ASTConditionGroup
    //                && ((ASTConditionGroup) varOp).getConditions().size() > 1
    //                && ((ASTConditionGroup) varOp).getConditions().stream().allMatch(c -> c.gel)
  }
}
