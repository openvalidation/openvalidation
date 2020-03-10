package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.builder.ASTOperandArrayBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
This class is used to resolve ASTOperandArrays in ASTVariables that have been parsed incorrectly as an incomplete ASTConditionGroup.

    Berlin, Paris or London as capital cities
    -> the variable's value will be initially parsed as a condition group with the first condition only containing the left string
        operand 'Berlin, Paris' and the second condition containing the connector 'OR' and the left string operand 'London'.
 */
public class PostModelArrayConditionGroupResolver
    extends PostProcessorSubelementBase<ASTModel, ASTVariable> {
  @Override
  protected Predicate<? super ASTVariable> getFilter() {
    return v -> isVarWithArrParsedAsCondGrp(v);
  }

  @Override
  protected void processItem(ASTVariable item) {
    ASTConditionGroup conditionGroup = (ASTConditionGroup) item.getValue();

    List<ASTOperandBase> allLeftOperands =
        conditionGroup.getAllConditions().stream()
            .map(c -> c.getLeftOperand())
            .collect(Collectors.toList());

    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(null);
    arrayBuilder.create();

    for (ASTOperandBase leftOp : allLeftOperands) {
      if (leftOp instanceof ASTOperandArray) {
        for (ASTOperandBase innerArrayItem : ((ASTOperandArray) leftOp).getItems()) {
          arrayBuilder.addItem(innerArrayItem);
        }
      } else {
        arrayBuilder.addItem(leftOp);
      }
    }

    item.setValue(arrayBuilder.getModel());
  }

  private boolean isVarWithArrParsedAsCondGrp(ASTVariable variable) {
    ASTOperandBase varOp = variable.getValue();

    boolean isValidConditionGroup = false;

    if (varOp instanceof ASTConditionGroup) {
      isValidConditionGroup = isValidConditionBase((ASTConditionGroup) varOp);
    }

    return isValidConditionGroup;
  }

  private boolean isValidConditionBase(ASTConditionBase varOp) {
    boolean isValid = false;
    if (varOp instanceof ASTCondition) {
      ASTCondition cond = (ASTCondition) varOp;
      isValid = // (cond.getLeftOperand() instanceof ASTOperandStatic || cond.getLeftOperand()
          // instanceof ASTOperandArray) &&
          !cond.hasRightOperand();
    } else if (varOp instanceof ASTConditionGroup) {
      List<ASTConditionBase> conditionBases = ((ASTConditionGroup) varOp).getConditions();
      isValid = conditionBases.stream().allMatch(base -> isValidConditionBase(base));
    }

    return isValid;
  }
}
