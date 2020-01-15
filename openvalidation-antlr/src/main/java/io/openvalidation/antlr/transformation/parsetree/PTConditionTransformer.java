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
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.StringUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;

public class PTConditionTransformer
    extends TransformerBase<PTConditionTransformer, ASTCondition, mainParser.ConditionContext> {
  public PTConditionTransformer(mainParser.ConditionContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTCondition transform() throws Exception {
    ASTConditionBuilder builder = this.createBuilder(ASTConditionBuilder.class);

    if (antlrTreeCntx.expression_simple() != null && antlrTreeCntx.expression_simple().size() > 0) {

      mainParser.Expression_simpleContext leftOperand = antlrTreeCntx.expression_simple().get(0);
      ASTItem item = this.createASTItem(leftOperand);

      //            if (ParseTreeUtils.getSymbol(antlrTreeCntx.OPERATOR_COMP()) ==
      // mainParser.OPERATOR_COMP)
      //
      // builder.withOperator(ParseTreeUtils.getOperator(antlrTreeCntx.OPERATOR_COMP()));

      ASTComparisonOperator operator =
          ParseTreeUtils.getEffectiveOperator(antlrTreeCntx.OPERATOR_COMP());

      if (operator != null) builder.withOperator(operator);

      if (antlrTreeCntx.expression_simple().size() > 1) {

        if (item != null && item instanceof ASTOperandBase)
          builder.withLeftOperand((ASTOperandBase) item);

        if (operator != null
            && !(operator.equals(ASTComparisonOperator.EXISTS)
                || operator.equals(ASTComparisonOperator.NOT_EXISTS))) {
          mainParser.Expression_simpleContext rightOperand =
              antlrTreeCntx.expression_simple().get(1);
          ASTItem itemRight = this.createASTItem(rightOperand);

          if (itemRight != null && itemRight instanceof ASTOperandBase) {
            builder.withRightOperand((ASTOperandBase) itemRight);

            //                    if (antlrTreeCntx.OPERATOR_COMP().size() > 1 &&
            // antlrTreeCntx.expression_simple().size() > 2){
            //                        builder.withUnknownCondition(builder.getModel());
            //                    }
          } else // try to resolve ast item from joined content of all simple expressions...
          {
            List<String> tmp =
                antlrTreeCntx.expression_simple().stream()
                    .map(e -> e.getText())
                    .collect(Collectors.toList());
            String content =
                StringUtils.join(tmp.stream().skip(1).collect(Collectors.toList()), "");

            PTContentTransformer ct = new PTContentTransformer(null, factoryCntx);
            itemRight = ct.resolveContentString(content);

            if (itemRight != null) builder.withRightOperand((ASTOperandBase) itemRight);
          }
        }

      } else if (builder.getModel().getOperator() != null) {
        if (item != null && antlrTreeCntx.children.indexOf(leftOperand) == 0)
          builder.withLeftOperand((ASTOperandBase) item);

        if (item != null && antlrTreeCntx.children.indexOf(leftOperand) > 0)
          builder.withRightOperand((ASTOperandBase) item);

        // return builder.getModel();
      } else {
        return transformImplicitCondition(item, leftOperand);
      }
    }

    ASTCondition condition = builder.getModel();

    return this.postprocess(condition.resolveImplicitBooleanCondition());
  }

  public ASTCondition transformImplicitCondition(ASTItem item, ParseTree source) throws Exception {
    ASTConditionBuilder builder = new ASTConditionBuilder();
    builder.create();
    builder.withSource(source.getText());

    if (item != null && item instanceof ASTOperandBase) {
      builder.withLeftOperand((ASTOperandBase) item);
      if (((ASTOperandBase) item).getDataType() == DataPropertyType.Boolean) {
        builder.withOperator(ASTComparisonOperator.EQUALS);
      }
    }

    return builder.getModel();
  }

  public static ASTCondition transformImplicit(
      ASTItem astItem, ParseTree treeItem, TransformerContext factoryCntx) throws Exception {
    PTConditionTransformer conditionTrns = new PTConditionTransformer(null, factoryCntx);
    ASTItem item = conditionTrns.transformImplicitCondition(astItem, treeItem);

    if (item != null && item instanceof ASTCondition) return (ASTCondition) item;

    return null;
  }
}
