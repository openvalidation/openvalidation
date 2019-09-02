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

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.utils.StringUtils;
import java.util.function.Predicate;

/*
 *
 *
 *        WENN der Name Validaria IST
 *        DANN ..
 *
 *
 *        if (Name != Validaria)
 *           throw ...
 *
 *
 *
 */
public class PostConditionSingleOperand
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  @Override
  protected Predicate<ASTCondition> getFilter() {
    return c ->
        (c != null
            && !c.hasRightOperand()
            && c.hasLeftOperand()
            && c.hasEqualityComparer()
            && c.getLeftOperand().isPropertyOrVariable()
            && c.getLeftOperand().isStringData());
  }

  @Override
  protected void processItem(ASTCondition condition) {
    String source = condition.getLeftOperand().getOriginalSource();
    String operandValue = condition.getLeftOperand().getName();

    if (!StringUtils.isNullOrEmpty(operandValue)) {
      int nElm = source.toLowerCase().indexOf(operandValue);
      if (nElm > -1) {
        String right = source.substring(nElm + operandValue.length());

        if (!StringUtils.isNullOrEmpty(right)) {
          ASTOperandStaticString rightOperand =
              new ASTOperandStaticString(StringUtils.stripSpecialWords(right));
          rightOperand.setSource(condition.getPreprocessedSource());
          condition.setRightOperand(rightOperand);
        }
      }
    }
  }
}
