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
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.utils.NumberParsingUtils;

public class PTContentTransformer
    extends TransformerBase<PTContentTransformer, ASTOperandBase, mainParser.ContentContext> {

  public PTContentTransformer(mainParser.ContentContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTOperandBase transform() throws Exception {
    ASTOperandBase operand = null;

    String function =
        (this.antlrTreeCntx.FUNCTION() != null && this.antlrTreeCntx.FUNCTION().size() > 0)
            ? this.antlrTreeCntx.FUNCTION().get(0).getText()
            : null;

    String content = antlrTreeCntx.getText();

    if (function != null) content = content.replaceAll(function, "");

    // resolve content string....
    ASTOperandBase cntOperand = resolveContentString(content);

    if (function != null) {
      operand = new ASTOperandFunction(ParseTreeUtils.extractFunctionName(function), cntOperand);
    } else operand = cntOperand;

    return operand;
  }

  public ASTOperandBase resolveContentString(String content) {
    ASTOperandBase operand = null;

    if (content != null) {

      if (NumberParsingUtils.isNumber(content)) {
        operand = new ASTOperandStaticNumber(Double.parseDouble(content));
        operand.setSource(content);
      } else {
        operand = this.createProperty(content);

        if (operand == null) {
          // String cnt = StringUtils.stripWords(content, new String[]{" ist", "be ", "sein",
          // "heißen", "heißt", " ", "\n", "\n", "\n\r"});
          if (!content.trim().isEmpty()) {
            operand = new ASTOperandStaticString(content);
            operand.setSource(content);
          }
        }
      }
    }

    return operand;
  }
}
