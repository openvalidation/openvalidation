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

public class PostModelStripSpecialWords
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  @Override
  protected Predicate<? super ASTCondition> getFilter() {
    return c ->
        c.hasEqualityComparer()
            && c.hasRightAndLeftOperand()
            && (c.getLeftOperand().isStaticString() || c.getRightOperand().isStaticString());
  }

  @Override
  protected void processItem(ASTCondition condition) {
    if (condition.getLeftOperand().isStaticString()) {
      String val = StringUtils.stripSpecialWords(condition.getLeftOperandValueAsString());
      ((ASTOperandStaticString) condition.getLeftOperand()).setValue(val);
    }

    if (condition.getRightOperand().isStaticString()) {
      String val = StringUtils.stripSpecialWords(condition.getRightOperandValueAsString());
      ((ASTOperandStaticString) condition.getRightOperand()).setValue(val);
    }
  }
}
