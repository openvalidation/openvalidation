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
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.utils.ArrayContentUtils;
import io.openvalidation.common.utils.LINQ;
import java.util.function.Predicate;

public class PostModelArrayOrReplacement
    extends PostProcessorSubelementBase<ASTModel, ASTConditionGroup> {

  @Override
  protected Predicate<? super ASTConditionGroup> getFilter() {
    return g ->
        g.filterConditions().size() > 1
            && LINQ.any(
                g.filterConditions(),
                c ->
                    c.getOperator() == ASTComparisonOperator.ONE_OF
                        || c.getOperator() == ASTComparisonOperator.NONE_OF
                        || c.getOperator() == ASTComparisonOperator.AT_LEAST_ONE_OF
                        || c.getOperator() == ASTComparisonOperator.ALL_OF)
            && LINQ.any(
                g.filterConditions(),
                c ->
                    c.getConnector() != null
                        && c.getLeftOperand() != null
                        && c.getRightOperand() == null);
  }

  @Override
  protected void processItem(ASTConditionGroup group) {
    ASTCondition condition =
        LINQ.findFirst(
            group.filterConditions(),
            c ->
                c.getOperator() == ASTComparisonOperator.ONE_OF
                    || c.getOperator() == ASTComparisonOperator.NONE_OF
                    || c.getOperator() == ASTComparisonOperator.AT_LEAST_ONE_OF
                    || c.getOperator() == ASTComparisonOperator.ALL_OF);

    if (condition != null) {
      int indx = group.filterConditions().indexOf(condition);
      if ((group.filterConditions().size() + 1) > indx) {
        ASTCondition cnd = group.filterConditions().get(indx + 1);

        if (condition.getRightOperand() instanceof ASTOperandArray) {
          if (cnd.getLeftOperand().isStatic()) {
            String val = ((ASTOperandStatic) cnd.getLeftOperand()).getValue();
            ASTOperandStatic staticOperand =
                ArrayContentUtils.resolveStaticArrayContent(
                    val, ((ASTOperandArray) condition.getRightOperand()).getContentType());
            ((ASTOperandArray) condition.getRightOperand()).add(staticOperand);
          } else {
            ((ASTOperandArray) condition.getRightOperand()).add(cnd.getLeftOperand());
          }

          group.getConditions().remove(cnd);
          // TODO: replace source with new elements...
        }
      }
    }
  }
}
