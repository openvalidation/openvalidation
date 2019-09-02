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

package io.openvalidation.antlr.transformation;

import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.parsetree.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class TransformerFactory {

  public static TransformerBase create(ParseTree tree, TransformerContext fctx) {
    if (tree != null) {
      if (tree instanceof mainParser.CommentContext) {
        return new PTCommentTransformer((mainParser.CommentContext) tree, fctx);
      } else if (tree instanceof mainParser.VariableContext) {
        return new PTVariableTransformer((mainParser.VariableContext) tree, fctx);
      } else if (tree instanceof mainParser.ContentContext) {
        return new PTContentTransformer((mainParser.ContentContext) tree, fctx);
      } else if (tree instanceof mainParser.ExpressionContext) {
        return new PTExpressionTransformer((mainParser.ExpressionContext) tree, fctx);
      } else if (tree instanceof mainParser.Expression_simpleContext) {
        return new PTExpressionSimpleTransformer((mainParser.Expression_simpleContext) tree, fctx);
      } else if (tree instanceof mainParser.AccessorContext) {
        return new PTAccessorTransformer((mainParser.AccessorContext) tree, fctx);
      } else if (tree instanceof mainParser.Accessor_ofContext) {
        return new PTAccessorOfTransformer((mainParser.Accessor_ofContext) tree, fctx);
      } else if (tree instanceof mainParser.Accessor_withContext) {
        return new PTAccessorWithTransformer((mainParser.Accessor_withContext) tree, fctx);
      } else if (tree instanceof mainParser.ActionContext) {
        return new PTActionTransformer((mainParser.ActionContext) tree, fctx);
      } else if (tree instanceof mainParser.ErrorContext) {
        return new PTErrorTransformer((mainParser.ErrorContext) tree, fctx);
      } else if (tree instanceof mainParser.ArithmeticContext) {
        return new PTArithmeticTransformer((mainParser.ArithmeticContext) tree, fctx);
      } else if (tree instanceof mainParser.Condition_exprContext) {
        return new PTConditionExprTransformer((mainParser.Condition_exprContext) tree, fctx);
      } else if (tree instanceof mainParser.ConditionContext) {
        return new PTConditionTransformer((mainParser.ConditionContext) tree, fctx);
      } else if (tree instanceof mainParser.Condition_groupContext) {
        return new PTConditionGroupTransformer((mainParser.Condition_groupContext) tree, fctx);
      } else if (tree instanceof mainParser.Rule_definitionContext) {
        return new PTRuleTransformer((mainParser.Rule_definitionContext) tree, fctx);
      } else if (tree instanceof mainParser.Rule_constrainedContext) {
        return new PTRuleConstrainedTransformer((mainParser.Rule_constrainedContext) tree, fctx);
      } else if (tree instanceof mainParser.Condition_constrainedContext) {
        return new PTConditionConstrainedTransformer(
            (mainParser.Condition_constrainedContext) tree, fctx);
      } else if (tree instanceof mainParser.UnknownContext) {
        return new PTUnknownTransformer((mainParser.UnknownContext) tree, fctx);
      } else if (tree instanceof mainParser.LambdaContext) {
        return new PTLambdaTransformer((mainParser.LambdaContext) tree, fctx);
      }
    }

    return null;
  }
}
