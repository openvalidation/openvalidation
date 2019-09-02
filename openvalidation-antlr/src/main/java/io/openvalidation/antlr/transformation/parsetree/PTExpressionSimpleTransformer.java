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
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import java.util.concurrent.atomic.AtomicReference;

public class PTExpressionSimpleTransformer
    extends TransformerBase<
        PTExpressionSimpleTransformer, ASTOperandBase, mainParser.Expression_simpleContext> {

  public PTExpressionSimpleTransformer(
      mainParser.Expression_simpleContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTOperandBase transform() throws Exception {
    AtomicReference<ASTOperandBase> operand = new AtomicReference<>();

    this.eachTreeChild(
        c -> {
          ASTItem item = this.createASTItem(c);
          if (item != null && item instanceof ASTOperandBase) operand.set((ASTOperandBase) item);
        });

    return (operand != null) ? operand.get() : null;
  }
}
