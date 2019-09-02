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
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.builder.ASTVariableBuilder;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.utils.StringUtils;

public class PTVariableTransformer
    extends TransformerBase<PTVariableTransformer, ASTVariable, mainParser.VariableContext> {

  public PTVariableTransformer(mainParser.VariableContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTVariable transform() throws Exception {
    ASTVariableBuilder builder = this.createBuilder(ASTVariableBuilder.class);

    if (antlrTreeCntx.name() != null && antlrTreeCntx.name().unknown() != null) {
      String name =
          StringUtils.reverseKeywords(this.antlrTreeCntx.name().unknown().getText().trim());
      builder.withName(name);
    }

    if (antlrTreeCntx.expression() != null) {

      this.eachTreeChild(
          antlrTreeCntx.expression().children,
          c -> {
            ASTItem item = this.createASTItem(c);

            if (item != null && item instanceof ASTOperandBase) {
              builder.withValue((ASTOperandBase) item);
            }
          });
    }

    if (antlrTreeCntx.lambda() != null) {
      ASTItem item = this.createASTItem(antlrTreeCntx.lambda());

      if (item != null && item instanceof ASTOperandBase) builder.withValue((ASTOperandBase) item);
    }

    return builder.getModel();
  }
}
