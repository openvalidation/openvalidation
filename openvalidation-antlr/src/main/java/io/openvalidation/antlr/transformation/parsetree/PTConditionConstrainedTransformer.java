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

package io.openvalidation.antlr.transformation.parsetree;

import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.ParseTreeUtils;
import io.openvalidation.antlr.transformation.TransformerBase;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PTConditionConstrainedTransformer
    extends TransformerBase<
        PTConditionConstrainedTransformer, ASTCondition, mainParser.Condition_constrainedContext> {
  public PTConditionConstrainedTransformer(
      mainParser.Condition_constrainedContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTCondition transform() throws Exception {
    ASTConditionBuilder builder = this.createBuilder(ASTConditionBuilder.class);
    boolean hasLeft = false;
    boolean hasRight = false;
    List<ASTItem> unknowncond_expressions = new ArrayList<>();

    if (this.antlrTreeCntx.expression_simple() != null) {
      ASTItem item = this.createASTItem(this.antlrTreeCntx.expression_simple());

      if (item != null && item instanceof ASTOperandBase) {
        builder.withLeftOperand((ASTOperandBase) item);
        hasLeft = true;
      }
    }

    if (this.antlrTreeCntx.condition_expr() != null
        && this.antlrTreeCntx.condition_expr().size() > 0) {
      ASTItem item = null;

      if (this.antlrTreeCntx.condition_expr().size() == 1)
        item = this.createASTItem(this.antlrTreeCntx.condition_expr().get(0));
      else {
        List<ASTItem> allItems = new ArrayList<>();

        this.antlrTreeCntx
            .condition_expr()
            .forEach(
                c -> {
                  try {
                    ASTItem i = this.createASTItem(c);
                    if (i != null) allItems.add(i);
                  } catch (Exception e) {
                    // e.printStackTrace();
                  }
                });

        if (allItems.size() > 0) {
          if (LINQ.any(allItems, i -> !(i instanceof ASTOperandStaticString))) {
            item = LINQ.findFirst(allItems, i -> !(i instanceof ASTOperandStaticString));

            for (ASTItem i : allItems) {
              if (i != item) {
                unknowncond_expressions.add(i);
              }
            }
          } else {
            String src =
                StringUtils.join(
                    allItems.stream()
                        .map(i -> ((ASTOperandStaticString) i).getValue())
                        .collect(Collectors.toList()),
                    "");
            item = new ASTOperandStaticString(src);
            item.setSource(src);
          }
        }
      }

      ASTCondition condition = null;
      if (unknowncond_expressions.size() > 0)
        builder.withUnknownConditions(LINQ.ofType(unknowncond_expressions, ASTConditionBase.class));

      if (item != null && item instanceof ASTConditionBase) {
        condition = (ASTCondition) item;
        builder.withOperator(condition.getOperator());
        builder.withRightOperand(condition.getRightOperand());
        hasRight = true;
      } else if (item != null && item instanceof ASTOperandBase) {

        // TODO: missing variable check
        // builder.withOperator(ASTComparisonOperator.EQUALS);
        builder.withRightOperand((ASTOperandBase) item);
        hasRight = true;
      }
    }

    // boolean hasImplicitOperator = false;
    ASTComparisonOperator operator = builder.getModel().getOperator();

    String ruleIndicator = ParseTreeUtils.getEffectiveRuleIndicator(antlrTreeCntx.CONSTRAINT());

    // invert comparison operation if MUST Contrain was used...
    if (!StringUtils.isNullOrEmpty(ruleIndicator)) {

      ASTComparisonOperator operatorInverted = null;

      if (ruleIndicator.equals(Constants.MUST)) {
        operatorInverted = ASTComparisonOperator.NOT_EQUALS;
        builder.markAsHasToBeInverted();

        if (operator != null) {

          operatorInverted = operator.invert();
        }
      } else {

        if (operator != null) operatorInverted = operator;
        else operatorInverted = ASTComparisonOperator.EQUALS;
      }

      builder.getModel().setOperator(operatorInverted);
    } else if (operator == null) {
      builder.getModel().setOperator(ASTComparisonOperator.NOT_EQUALS);
    }

    // add right operand as static bool on implicit comparison operation...
    if (builder.getModel().getRightOperand() == null
        && builder.getModel().getLeftOperand() != null
        && builder.getModel().getLeftOperand().getDataType() == DataPropertyType.Boolean) {
      builder.withRightOperandAsBoolean(true);
    }
    //        else if ( hasImplicitOperator )
    //        {
    //            //builder.getModel().setOperator(null);
    //        }

    builder.markAsConstrainedCondition();

    return this.postprocess(builder.getModel().resolveImplicitBooleanCondition());
  }
}
