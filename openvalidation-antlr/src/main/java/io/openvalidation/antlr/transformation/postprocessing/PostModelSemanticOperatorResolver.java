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
import io.openvalidation.common.ast.operand.ASTSemanticOperator;
import java.util.function.Predicate;

public class PostModelSemanticOperatorResolver
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  @Override
  protected Predicate<ASTCondition> getFilter() {
    return c ->
        c.hasLeftOperand() && c.getLeftOperand().isSemanticOperator()
            || c.hasRightOperand() && c.getRightOperand().isSemanticOperator();
  }

  @Override
  protected void processItem(ASTCondition condition) {
    ASTSemanticOperator operator = null;

    if (condition.hasLeftOperand() && condition.getLeftOperand().isSemanticOperator()) {
      operator = (ASTSemanticOperator) condition.getLeftOperand();
    } else if (condition.hasRightOperand() && condition.getRightOperand().isSemanticOperator()) {
      operator = (ASTSemanticOperator) condition.getRightOperand();
    }

    if (operator != null) {
      condition.setSemanticOperatorName(operator.getSemanticOperator().getName());

      condition.setOperator(operator.getOperator());
      condition.setLeftOperand(operator.getOperand());
      condition.setRightOperand(operator.getSecondOperand());

      if (condition.hasToBeInverted()) condition.setOperator(condition.getOperator().invert());
    }
  }
}
