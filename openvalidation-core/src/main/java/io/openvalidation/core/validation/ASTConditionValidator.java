/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.openvalidation.core.validation;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.exceptions.ASTValidationException;

public class ASTConditionValidator extends ValidatorBase {
  private ASTCondition _condition;
  private int _level;

  public ASTConditionValidator(ASTCondition condition) {
    this._condition = condition;
  }

  public void setLevel(int level) {
    this._level = level;
  }

  @Override
  public void validate() throws Exception {
    ASTOperandBase leftOperand = this._condition.getLeftOperand();
    ASTOperandBase rightOperand = this._condition.getRightOperand();
    ASTComparisonOperator operator = _condition.getOperator();

    if (_level > 0 && this._condition.getConnector() == null)
      throw new ASTValidationException(
          "missing Connector in combined condition.", this._condition, this.globalPosition);

    if (this._condition.getOperator() == null)
      throw new ASTValidationException(
          "invalid condition. missing comparison operator and operand.",
          this._condition,
          this.globalPosition);

    if (leftOperand == null && rightOperand == null)
      throw new ASTValidationException(
          "at least one operand should be present.", this._condition, this.globalPosition);

    if (operator.equals(ASTComparisonOperator.EXISTS)
        || operator.equals(ASTComparisonOperator.NOT_EXISTS)) {
      if (leftOperand == null || !leftOperand.hasValue())
        throw new ASTValidationException(
            "missing left operand in EXISTS expression", _condition, this.globalPosition);
      else if (leftOperand.hasValue() && leftOperand instanceof ASTOperandStatic) {
        if (leftOperand instanceof ASTOperandStaticString)
          throw new ASTValidationException(
              "Exists-operator cannot be used on plain text. Property required instead.",
              _condition);
        else
          throw new ASTValidationException(
              "Exists-operator cannot be used on numbers. Property required instead.",
              _condition,
              this.globalPosition);
      }
      // conditions with the (NOT)EXISTS operator must not have a right operand
      if (rightOperand != null)
        throw new ASTValidationException(
            "right operand in EXISTS expression is not allowed", _condition, this.globalPosition);
    } else {
      if (leftOperand == null || !leftOperand.hasValue())
        throw new ASTValidationException(
            "missing left operand in condition.", _condition, this.globalPosition);
      if (rightOperand == null || !rightOperand.hasValue())
        throw new ASTValidationException(
            "missing right operand in condition.", _condition, this.globalPosition);
    }

    if (leftOperand != null) validate(leftOperand, this.globalPosition);

    if (rightOperand != null) validate(rightOperand, this.globalPosition);

    // todo jgeske 23.05.2019 create special validation case for array operators
    if (rightOperand != null
        && leftOperand.getDataType() != rightOperand.getDataType()
        && !(operator == ASTComparisonOperator.AT_LEAST_ONE_OF
            || operator == ASTComparisonOperator.NONE_OF
            || operator == ASTComparisonOperator.ONE_OF)) {

      if (!(leftOperand.isEnumData() && rightOperand.isStringData())
          && !(rightOperand.isEnumData() && leftOperand.isStringData())) {
        throw new ASTValidationException(
            "comparison contains different DataTypes. \n"
                + "left operand is of type: '"
                + leftOperand.getDataType()
                + "' and "
                + "right operand is of type: '"
                + rightOperand.getDataType()
                + "'",
            _condition,
            this.globalPosition);
      }
    }

    if (leftOperand instanceof ASTOperandStatic && rightOperand instanceof ASTOperandStatic) {
      throw new ASTValidationException(
          "at least one operand in comparison should not be static\n"
              + "left operand is of type: '"
              + leftOperand.getType()
              + "' and "
              + "right operand is of type: '"
              + rightOperand.getType()
              + "'",
          _condition);
    }

    if (this._condition.getUnresolvedConditions() != null
        && this._condition.getUnresolvedConditions().size() > 0) {
      throw new ASTValidationException(
          "missing AND/OR connector in combined condition.",
          this._condition.getUnresolvedConditions().get(0));
    }
  }
}
