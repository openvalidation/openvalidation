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

import io.openvalidation.antlr.generated.mainLexer;
import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.ParseTreeUtils;
import io.openvalidation.antlr.transformation.TransformerBase;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.builder.ASTConditionGroupBuilder;
import io.openvalidation.common.ast.builder.ASTRuleBuilder;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.utils.StringUtils;
import java.util.concurrent.atomic.AtomicReference;

public class PTRuleConstrainedTransformer
    extends TransformerBase<
        PTRuleConstrainedTransformer, ASTRule, mainParser.Rule_constrainedContext> {
  public PTRuleConstrainedTransformer(
      mainParser.Rule_constrainedContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTRule transform() throws Exception {
    ASTRuleBuilder builder = this.createBuilder(ASTRuleBuilder.class);

    if (this.antlrTreeCntx.COMBINATOR() == null || this.antlrTreeCntx.COMBINATOR().size() < 1) {
      this.eachTreeChild(
          c -> {
            ASTItem item = this.createASTItem(c);
            if (item != null && item instanceof ASTConditionBase) appendCondition(builder, item);
            // builder.withCondition((ASTConditionBase)item);
            else if (item != null) {
              ASTConditionBase condItem =
                  PTConditionTransformer.transformImplicit(item, c, factoryCntx);
              appendCondition(builder, condItem);
              // builder.withCondition(condItem);
            }
          });
    } else {
      ASTConditionGroupBuilder groupBuilder = builder.createConditionGroup();
      AtomicReference<ASTConditionConnector> lastConnector = new AtomicReference<>();

      this.eachTreeChild(
          c -> {
            if (ParseTreeUtils.getSymbol(c) == mainLexer.COMBINATOR) {

              lastConnector.set(ParseTreeUtils.getCombinator(c));
              // groupBuilder.withConnector(ParseTreeUtils.getCombinator(c));
              // groupBuilder.getModel().getConditions().get(groupBuilder.getModel().getConditions().size()-1).setConnector(ParseTreeUtils.getCombinator(c));
            } else {
              ASTItem item = this.createASTItem(c);
              ASTConditionBase condItem = null;

              if (item != null && item instanceof ASTConditionBase)
                condItem = (ASTConditionBase) item;
              else if (item != null) {
                condItem = PTConditionTransformer.transformImplicit(item, c, factoryCntx);
              }

              if (lastConnector.get() != null) condItem.setConnector(lastConnector.get());

              if (c instanceof mainParser.Condition_exprContext
                  && lastConnector.get() != ASTConditionConnector.UNLESS) {
                condItem.invertOperator();
              }
              groupBuilder.withCondition(condItem);
              lastConnector.set(null);
            }
          });
    }

    builder.withError(
        StringUtils.reverseKeywords(
            this.antlrTreeCntx
                .getText())); // this.antlrTreeCntx.getText().replaceAll("ʬconstraintʬmust", "MUSS")

    return builder.getModel();
  }

  private void appendCondition(ASTRuleBuilder builder, ASTItem condition) {
    if (builder.getModel().getCondition() == null)
      builder.withCondition((ASTConditionBase) condition);
    else if (condition != null)
      builder.getModel().addInvalidCondition((ASTConditionBase) condition);
  }
}
