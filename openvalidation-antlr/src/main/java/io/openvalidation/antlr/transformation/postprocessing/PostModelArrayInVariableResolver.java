package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.builder.ASTOperandArrayBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.*;
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

    // filter all left operands of conditions
    List<ASTOperandBase> allLeftOperands =
        conditionGroup.getAllConditions().stream()
            .map(c -> c.getLeftOperand())
            .collect(Collectors.toList());

    // initially parse all as ASTOperandBase
    ASTOperandArray generalArray = extractGeneralArrayOperand(allLeftOperands);

    // try parse as numbers
    ASTOperandArray numericalArray = tryParseEachElementAsNumerical(generalArray);

    if (numericalArray != null) {
      return numericalArray;
    } else {
      return parseAsStringArray(generalArray);
    }
  }

  private static ASTOperandArray parseAsStringArray(ASTOperandArray generalArray) {
    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(null);
    arrayBuilder.create().withContentType(DataPropertyType.String);

    for (ASTOperandBase item : generalArray.getItems()) {
      if (item.getDataType() == DataPropertyType.String) {
        arrayBuilder.addItem(item);
      } else if (item instanceof ASTOperandStatic) {
        ASTOperandStaticString staticString;
        staticString = new ASTOperandStaticString(((ASTOperandStatic) item).getOriginalSource());
        staticString.setSource(item.getOriginalSource());
        arrayBuilder.addItem(staticString);
      } else {
        // if item is for example a variable of type Decimal it will simply be added. The falsely
        // occurring variable will then be validated as false by the validator since it doesnt
        // match the arrays content type which is String
        arrayBuilder.addItem(item);
      }
    }

    return arrayBuilder.getModel();
  }

  private static ASTOperandArray tryParseEachElementAsNumerical(ASTOperandArray generalArray) {
    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(null);
    arrayBuilder.create().withContentType(DataPropertyType.Decimal);

    for (ASTOperandBase item : generalArray.getItems()) {
      if (item.getDataType() == DataPropertyType.Decimal) {
        arrayBuilder.addItem(item);
      } else {
        boolean isParsableStaticOperand = item instanceof ASTOperandStaticString;
        if (isParsableStaticOperand) {
          String s = ((ASTOperandStaticString) item).getValue();
          Double number = NumberParsingUtils.extractNumber(s);

          boolean numberExtracted = number != null;
          if (numberExtracted) {
            ASTOperandStaticNumber num = new ASTOperandStaticNumber(number);
            num.setSource(item.getOriginalSource());
            arrayBuilder.addItem(num);
          } else {
            // at least one operand not parsable as a numerical value
            return null;
          }
        } else {
          // at least one operand not parsable as a numerical value
          return null;
        }
      }
    }

    return arrayBuilder.getModel();
  }

  private static ASTOperandArray extractGeneralArrayOperand(List<ASTOperandBase> allLeftOperands) {
    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(null);
    arrayBuilder.create().withContentType(DataPropertyType.Unknown);

    for (ASTOperandBase base : allLeftOperands) {
      if (base instanceof ASTOperandStaticString) {
        ASTOperandStaticString staticString = (ASTOperandStaticString) base;
        if (PostProcessorUtils.isParsableAsArray(staticString)) {
          ASTOperandArray subArray =
              PostProcessorUtils.resolveArrayFromString(staticString, DataPropertyType.String);
          for (ASTOperandBase elem : subArray.getItems()) {
            arrayBuilder.addItem(elem);
          }
        } else {
          arrayBuilder.addItem(staticString);
        }
      } else {
        arrayBuilder.addItem(base);
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
      isValid = cond.getLeftOperand() instanceof ASTOperandStatic && !cond.hasRightOperand();
    } else if (varOp instanceof ASTConditionGroup) {
      List<ASTConditionBase> conditionBases = ((ASTConditionGroup) varOp).getConditions();
      isValid = conditionBases.stream().allMatch(base -> isValidConditionBase(base));
    }

    return isValid;
  }
}
