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
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.utils.StringUtils;

public class PTAccessorOfTransformer
    extends TransformerBase<
        PTAccessorOfTransformer, ASTOperandBase, mainParser.Accessor_ofContext> {

  public PTAccessorOfTransformer(mainParser.Accessor_ofContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTOperandBase transform() throws Exception {
    ASTOperandBase operandBase = null;
    String entity = null;
    String property = null;

    if (antlrTreeCntx.content(0) != null && antlrTreeCntx.content(1) != null) {
      entity =
          factoryCntx.getSchema().filterPropertyString(antlrTreeCntx.content(1).getText().trim());
      property =
          factoryCntx
              .getSchema()
              .filterPropertyString(entity, antlrTreeCntx.content(0).getText().trim());
    }

    if (entity != null && property != null) {
      String propertyContent = entity + "." + property;

      operandBase = this.createProperty(propertyContent);
      if (operandBase != null) {
        operandBase.setSource(this.antlrTreeCntx.getText());
      }
    } else {
      String propertyContent = StringUtils.reverseKeywords(this.antlrTreeCntx.getText());

      PTContentTransformer ct = new PTContentTransformer(null, factoryCntx);
      operandBase =
          ct.resolveContentString(propertyContent); // propertyContent.replaceAll("\\.", " ")

      if (operandBase != null) operandBase.setSource(this.antlrTreeCntx.getText());
    }

    return operandBase;
  }
}
