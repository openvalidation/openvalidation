package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.NumberParsingUtils;
import java.util.function.Predicate;

public class PostModelWhereOnSimpleTypeResolver
    extends PostProcessorSubelementBase<ASTModel, ASTOperandFunction> {

  @Override
  protected Predicate<ASTOperandFunction> getFilter() {
    return function -> {
      boolean result = false;
      DataPropertyType arrayContentType = DataPropertyType.Unknown;

      if (function.getName().equals("WHERE") && function.getParameters().size() > 1) {
        ASTOperandBase firstParam = function.getParameters().get(0);

        arrayContentType = getArrayContentType(firstParam);
        if (arrayContentType != DataPropertyType.Unknown) {
          ASTItem secondParam = function.getParameters().get(1);
          if (secondParam instanceof ASTOperandLambdaCondition) {
            ASTConditionBase conditionBase =
                ((ASTOperandLambdaCondition) secondParam).getCondition();

            resolve(
                conditionBase,
                ((ASTOperandLambdaCondition) secondParam).getLambdaToken(),
                arrayContentType);
          }
        }
      }
      return result;
    };
  }

  private DataPropertyType getArrayContentType(ASTOperandBase firstParam) {
    DataPropertyType arrayContentType = DataPropertyType.Unknown;
    if (firstParam.getDataType() == DataPropertyType.Array) {
      if (firstParam instanceof ASTOperandProperty) {
        arrayContentType = ((ASTOperandProperty) firstParam).getArrayContentType();
      } else if (firstParam instanceof ASTOperandVariable) {
        arrayContentType = ((ASTOperandVariable) firstParam).getArrayContentType();
      }
    }
    return arrayContentType;
  }

  private void resolve(
      ASTConditionBase conditionBase, String lambdaToken, DataPropertyType arrayContentType) {
    if (conditionBase instanceof ASTConditionGroup) {
      ASTConditionGroup group = (ASTConditionGroup) conditionBase;
      for (ASTConditionBase child : group.getConditions()) {
        resolve(child, lambdaToken, arrayContentType);
      }
    } else if (conditionBase instanceof ASTCondition) {
      ASTCondition condition = (ASTCondition) conditionBase;

      ASTOperandBase leftOperand = condition.getLeftOperand();
      ASTOperandBase rightOperand = condition.getRightOperand();

      // special case for static numbers in comparison
      if (arrayContentType == DataPropertyType.Decimal
          && leftOperand instanceof ASTOperandStaticString
          && rightOperand instanceof ASTOperandStaticString) {
        resolveStaticNumbers(condition, leftOperand, rightOperand);

        // update operand variables
        leftOperand = condition.getLeftOperand();
        rightOperand = condition.getRightOperand();
      }

      // todo lazevedo 13.1.20 Do operands with different data types exist?
      if ((leftOperand != null && leftOperand.getDataType() == arrayContentType)
          || (rightOperand != null && rightOperand.getDataType() == arrayContentType)) {
        ASTOperandProperty replacementProperty = new ASTOperandProperty();
        replacementProperty.setLambdaToken(lambdaToken);
        replacementProperty.setDataType(arrayContentType);

        // todo lazevedo 13.1.20 Replacement of static string comparison may cause ambiguity
        // problems
        // first names with name equals Peter; is name or peter the lambda token?

        /* todo lazevedo 15.1.20 '...value 5 equal to age' causes a condition in lambda that is independent of lambda token. Because
         *   'age' is a decimal prop the PostModelNumbersResolver will parse 'value 1' as 1. Add validation check for those cases? */
        // ^- jgeske 17.01.20 is this a problem? sounds like intended behaviour to me.
        //      the only case where this would be detrimental, would be a property/variable named
        // 'value 1' not being
        //      recognized, and the static interpreter being selected over the variable/property
        // resolution.

        /* uncomment this bracket for a more generic solution to the table below.
        if(leftOperand != null && rightOperand != null && //only one operand is staticString
                ((leftOperand instanceof ASTOperandStaticString) != (rightOperand instanceof ASTOperandStaticString))){
        }
        */

        ASTOperandBase newLeftOperand = leftOperand;
        ASTOperandBase newRightOperand = rightOperand;

        if (leftOperand instanceof ASTOperandStaticString) {
          replacementProperty.setSource(leftOperand.getOriginalSource());
          newLeftOperand = replacementProperty;
        } else if (leftOperand == null) {
          replacementProperty.setSource("");
          newLeftOperand = replacementProperty;
        } else if (rightOperand instanceof ASTOperandStaticString) {
          replacementProperty.setSource(rightOperand.getOriginalSource());
          newRightOperand = replacementProperty;
        } else if (rightOperand == null) {
          replacementProperty.setSource("");
          newRightOperand = replacementProperty;
        }

        condition.setLeftOperand(newLeftOperand);
        condition.setRightOperand(newRightOperand);
      }
    }
  }

  private void resolveStaticNumbers(
      ASTCondition condition, ASTOperandBase leftOperand, ASTOperandBase rightOperand) {
    // special case with static number comparison
    // ... with value equal to 5
    // second operand will be read as ASTOperandStaticString: 'to 5' and not be resolved

    ASTOperandBase newLeft = leftOperand;
    String leftStringValue = ((ASTOperandStaticString) leftOperand).getValue();
    ASTOperandBase newRight = rightOperand;
    String rightStringValue = ((ASTOperandStaticString) rightOperand).getValue();

    if (NumberParsingUtils.containsNumber(leftStringValue)) {
      newLeft = new ASTOperandStaticNumber(NumberParsingUtils.extractNumber(leftStringValue));
      newLeft.setSource(leftOperand.getOriginalSource());
    }

    if (NumberParsingUtils.containsNumber(rightStringValue)) {
      newRight = new ASTOperandStaticNumber(NumberParsingUtils.extractNumber(rightStringValue));
      newRight.setSource(rightOperand.getOriginalSource());
    }

    condition.setLeftOperand(newLeft);
    condition.setRightOperand(newRight);
  }

  @Override
  protected void processItem(ASTOperandFunction item) {}
}
