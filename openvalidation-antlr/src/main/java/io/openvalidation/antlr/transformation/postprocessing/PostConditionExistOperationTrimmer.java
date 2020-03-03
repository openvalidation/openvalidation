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
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import java.util.function.Predicate;

public class PostConditionExistOperationTrimmer extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  @Override
  protected Predicate<ASTCondition> getFilter() {
    return c ->
        (c != null
            && c.hasRightOperand()
            && c.hasLeftOperand()
            && (c.hasOperator(ASTComparisonOperator.EXISTS)
                || c.hasOperator(ASTComparisonOperator.NOT_EXISTS))
            && c.getLeftOperand().isPropertyOrVariable());
  }

  @Override
  protected void processItem(ASTCondition condition) {
    condition.setRightOperand(null);
  }
}
