package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.builder.ASTOperandArrayBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.NumberParsingUtils;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
      ASTOperandArray array = resolveArrayFromConditionGroup((ASTConditionGroup) item.getValue());
      item.setValue(array);
    } else {
      throw new IllegalStateException();
    }
  }

  private ASTOperandArray resolveArrayFromConditionGroup(ASTConditionGroup conditionGroup) {

    // left operands of conditions can be cast to ASTOperand static string because of previous
    // filter
    List<ASTOperandStaticString> allLeftOperandStrings =
        conditionGroup.getAllConditions().stream()
            .map(c -> (ASTOperandStaticString) c.getLeftOperand())
            .collect(Collectors.toList());

    // initially parse all as Strings
    ASTOperandArray stringArray = extractStringArrayOperand(allLeftOperandStrings);

    // try to extract numerical values from the strings in the array
    ASTOperandArray numericalArray = tryExtractArrayOperand(stringArray);

    if (numericalArray != null) {
      return numericalArray;
    } else {
      return stringArray;
    }
  }

  private static ASTOperandArray tryExtractArrayOperand(ASTOperandArray stringArray) {
    ASTOperandArrayBuilder numericalArrayBuilder = new ASTOperandArrayBuilder(null);
    numericalArrayBuilder.create();

    for (ASTOperandBase base : stringArray.getItems()) {
      String s = ((ASTOperandStaticString) base).getValue();
      Double number = NumberParsingUtils.extractNumber(s);

      if (number != null) {
        numericalArrayBuilder.addItem(number);
      } else {
        return null;
      }
    }
    return numericalArrayBuilder.getModel();
  }

  private static ASTOperandArray extractStringArrayOperand(
      List<ASTOperandStaticString> allLeftOperandStrings) {
    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(null);
    arrayBuilder.create();

    for (ASTOperandStaticString s : allLeftOperandStrings) {
      if (PostProcessorUtils.isParsableAsArray(s)) {
        ASTOperandArray subArray =
            PostProcessorUtils.resolveArrayFromString(s, DataPropertyType.String);
        for (ASTOperandBase elem : subArray.getItems()) {
          arrayBuilder.addItem(elem);
        }
      } else {
        arrayBuilder.addItem(
            PostProcessorUtils.resolveArrayElementString(s.getValue(), DataPropertyType.String));
      }
    }

    return arrayBuilder.getModel();
  }

  private ASTOperandArray resolveArrFromString(ASTOperandStaticString stringOperand) {
    return PostProcessorUtils.resolveArrayFromString(stringOperand);
  }

  private boolean isVarWithArrParsedAsStaticString(ASTVariable variable) {
    ASTOperandBase varOp = variable.getValue();

    return varOp instanceof ASTOperandStaticString
        && ((ASTOperandStaticString) varOp).getValue().contains(",");
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
      isValid = cond.getLeftOperand() instanceof ASTOperandStaticString && !cond.hasRightOperand();
    } else if (varOp instanceof ASTConditionGroup) {
      List<ASTConditionBase> conditionBases = ((ASTConditionGroup) varOp).getConditions();
      isValid = conditionBases.stream().allMatch(base -> isValidConditionBase(base));
    }

    return isValid;
  }
}
