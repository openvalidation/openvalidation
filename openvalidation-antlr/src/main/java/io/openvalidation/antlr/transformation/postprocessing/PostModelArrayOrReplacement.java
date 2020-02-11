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

/**
 * * This postprocessor is used to resolve special cases of array functions. For example the input
 * "The age must be 20 or 25" will initially be parsed as ((age must be 20) || (25)) resulting in
 * the second condition to only consist of the number 25. However the actual semantics are ((age
 * must be 20) || (age must be 25))
 */
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
                        || c.getOperator() == ASTComparisonOperator.ALL_OF
                        || c.getOperator() == ASTComparisonOperator.EQUALS
                        || c.getOperator() == ASTComparisonOperator.NOT_EQUALS)
            && LINQ.any(
                g.filterConditions(),
                c ->
                    c.getConnector() != null
                        && c.getLeftOperand() != null
                        && c.getRightOperand() == null);
  }

  @Override
  protected void processItem(ASTConditionGroup group) {
    ASTCondition currentCondition =
        LINQ.findFirst(
            group.filterConditions(),
            c ->
                c.getOperator() == ASTComparisonOperator.ONE_OF
                    || c.getOperator() == ASTComparisonOperator.NONE_OF
                    || c.getOperator() == ASTComparisonOperator.AT_LEAST_ONE_OF
                    || c.getOperator() == ASTComparisonOperator.ALL_OF
                    || c.getOperator() == ASTComparisonOperator.EQUALS
                    || c.getOperator() == ASTComparisonOperator.NOT_EQUALS);

    if (currentCondition != null) {
      int indx = group.filterConditions().indexOf(currentCondition);
      if ((group.filterConditions().size() - 1) > indx) {
        ASTCondition followingCondition = group.filterConditions().get(indx + 1);

        if (currentCondition.getRightOperand() instanceof ASTOperandArray) {
          ASTOperandArray currentRightArrayOp =
              (ASTOperandArray) currentCondition.getRightOperand();
          if (followingCondition.getLeftOperand().isStatic()) {
            String val = ((ASTOperandStatic) followingCondition.getLeftOperand()).getValue();
            ASTOperandStatic staticOperand =
                ArrayContentUtils.resolveStaticArrayContent(
                    val, currentRightArrayOp.getContentType());
            currentRightArrayOp.add(staticOperand);
          } else {
            currentRightArrayOp.add(followingCondition.getLeftOperand());
          }

          group.getConditions().remove(followingCondition);
          // TODO: replace source with new elements...
        } else if (currentCondition.getRightOperand() instanceof ASTOperandStatic
            && currentCondition.hasEqualityComparer()) {
          ASTOperandStatic currentRightStaticOp =
              (ASTOperandStatic) currentCondition.getRightOperand();
          ASTOperandArray array = new ASTOperandArray();
          array.setContentType(currentCondition.getLeftOperand().getDataType());
          // TODO lazevedo 11.02.20 preliminary source of this artificially created element hard to
          // reconstruct. additionally updating of parent sources required
          array.setSource(
              currentCondition.getRightOperand().getOriginalSource()
                  + " "
                  + followingCondition.getConnector()
                  + " "
                  + followingCondition.getOriginalSource());

          // fill array with right operand and the one from the following condition
          array.add(currentRightStaticOp);

          if (followingCondition.getLeftOperand().isStatic()) {
            String val = ((ASTOperandStatic) followingCondition.getLeftOperand()).getValue();
            ASTOperandStatic staticOperand =
                ArrayContentUtils.resolveStaticArrayContent(val, array.getContentType());
            array.add(staticOperand);
          } else {
            array.add(followingCondition.getLeftOperand());
          }

          if (currentCondition.hasOperator(ASTComparisonOperator.EQUALS))
            currentCondition.setOperator(ASTComparisonOperator.AT_LEAST_ONE_OF);
          else if (currentCondition.hasOperator(ASTComparisonOperator.NOT_EQUALS))
            currentCondition.setOperator(ASTComparisonOperator.NONE_OF);
          currentCondition.setRightOperand(array);
          group.getConditions().remove(followingCondition);
        }
      }
    }
  }
}
