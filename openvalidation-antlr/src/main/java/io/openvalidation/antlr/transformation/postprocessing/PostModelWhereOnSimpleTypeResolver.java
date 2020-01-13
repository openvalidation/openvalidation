package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
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

            // first names with name equals Peter; is name or peter the lambda token?
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

      // todo lazevedo 13.1.20 Do operands with different data types exist?
      if (leftOperand.getDataType() == arrayContentType
          || rightOperand.getDataType() == arrayContentType) {
        ASTOperandProperty replacementProperty = new ASTOperandProperty();
        replacementProperty.setLambdaToken(lambdaToken);
        replacementProperty.setDataType(arrayContentType);

        // todo lazevedo 13.1.20 Replacement of static string comparison may cause ambiguity
        // problems
        if (leftOperand instanceof ASTOperandStaticString) {
          replacementProperty.setSource(leftOperand.getOriginalSource());
          condition.setLeftOperand(replacementProperty);
        } else if (rightOperand instanceof ASTOperandStaticString) {
          replacementProperty.setSource(rightOperand.getOriginalSource());
          condition.setRightOperand(replacementProperty);
        }
      }
    }
  }

  @Override
  protected void processItem(ASTOperandFunction item) {}
}
