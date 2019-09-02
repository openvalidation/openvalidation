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
import io.openvalidation.antlr.transformation.TransformerBase;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTRuleBuilder;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import org.antlr.v4.runtime.tree.ParseTree;

public class PTRuleTransformer
    extends TransformerBase<PTRuleTransformer, ASTRule, mainParser.Rule_definitionContext> {
  public PTRuleTransformer(mainParser.Rule_definitionContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTRule transform() throws Exception {
    ASTRuleBuilder builder = this.createBuilder(ASTRuleBuilder.class);

    if (this.antlrTreeCntx.expression() != null
        && this.antlrTreeCntx.expression().children != null
        && this.antlrTreeCntx.expression().children.size() > 0) {

      int x = 0;
      for (ParseTree pt : this.antlrTreeCntx.expression().children) {
        ASTItem item = this.createASTItem(pt);
        ASTConditionBase condition = null;

        if (item != null && item instanceof ASTConditionBase) condition = (ASTConditionBase) item;
        else if (item != null) {
          ASTConditionBuilder s = new ASTConditionBuilder();
          s.create().withLeftOperand((ASTOperandBase) item).withSource(item.getUntrimmedSource());
          condition = s.getModel();
        }

        if (condition != null) {
          if (x == 0) builder.withCondition(condition);
          else builder.getModel().addInvalidCondition(condition);
        }

        x++;
      }
    }

    if (this.antlrTreeCntx.action() != null) {
      ASTItem item = this.createASTItem(this.antlrTreeCntx.action());
      if (item != null && item instanceof ASTActionError) builder.withError((ASTActionError) item);
    }

    return builder.getModel();
  }
}
