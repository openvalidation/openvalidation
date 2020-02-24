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

package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.StringUtils;
import java.util.function.Predicate;

public class PostConditionArrayResolver extends PostProcessorSelfBase<ASTCondition> {

  @Override
  protected Predicate<ASTCondition> getFilter() {
    return c ->
        c.hasLeftOperand()
            && c.hasRightOperand()
            && (c.getOperator() == ASTComparisonOperator.EQUALS
                || c.getOperator() == ASTComparisonOperator.NOT_EQUALS
                || c.getOperator() == ASTComparisonOperator.AT_LEAST_ONE_OF
                || c.getOperator() == ASTComparisonOperator.ONE_OF
                || c.getOperator() == ASTComparisonOperator.NONE_OF)
            && (c.getLeftOperand().isStringData() || c.getRightOperand().isStringData());
  }

  @Override
  protected void processItem(ASTCondition condition) {

    ASTOperandBase leftOp = condition.getLeftOperand();
    ASTOperandBase rightOp = condition.getRightOperand();

    ASTOperandArray leftOperandArray = null;
    ASTOperandArray rightOperandArray = null;

    if (rightOp != null) {
      leftOperandArray = this.resolveArrayInOperand(leftOp, rightOp.getDataType());
      if (leftOperandArray != null) condition.setLeftOperand(leftOperandArray);
    }
    if (leftOp != null) {
      rightOperandArray = this.resolveArrayInOperand(rightOp, leftOp.getDataType());
      if (rightOperandArray != null) condition.setRightOperand(rightOperandArray);
    }

    if (leftOperandArray != null || rightOperandArray != null) {
      if (condition.getOperator() == ASTComparisonOperator.EQUALS)
        condition.setOperator(ASTComparisonOperator.AT_LEAST_ONE_OF);
      else if (condition.getOperator() == ASTComparisonOperator.NOT_EQUALS)
        condition.setOperator(ASTComparisonOperator.NONE_OF);
    }
  }

  public ASTOperandArray resolveArrayInOperand(
      ASTOperandBase operand, DataPropertyType resolutionType) {
    ASTOperandArray resultArray = null;

    if (operand != null && operand instanceof ASTOperandStaticString) {
      String val = ((ASTOperandStaticString) operand).getValue();

      if (!StringUtils.isNullOrEmpty(val)) {
        val = StringUtils.trimSpecialChars(val);

        if (val.contains(",")) {
          resultArray = new ASTOperandArray();
          resultArray.setContentType(resolutionType);

          for (String delimiter : Constants.ARRAY_DELIMITER_ALIASES) {
            val = val.replaceAll(" " + delimiter + " ", ",");
          }

          for (String v : val.split(",")) {
            ASTOperandBase extractedItem =
                PostProcessorUtils.resolveArrayElementString(v, resolutionType);
            resultArray.add(extractedItem);
          }

          resultArray.setSource(operand.getPreprocessedSource());
        }
      }
    }

    return resultArray;
  }
}
