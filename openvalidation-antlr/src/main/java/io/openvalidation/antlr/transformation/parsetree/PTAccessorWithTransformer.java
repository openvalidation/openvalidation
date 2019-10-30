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
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;

public class PTAccessorWithTransformer
    extends TransformerBase<
        PTAccessorWithTransformer, ASTConditionBase, mainParser.Accessor_withContext> {

  public PTAccessorWithTransformer(
      mainParser.Accessor_withContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTConditionBase transform() throws Exception {
    return transform("");
  }

  public ASTConditionBase transform(ASTOperandProperty arrayScopeProperty) throws Exception {
    ASTConditionBase condition;
    if (arrayScopeProperty == null) condition = transform();
    else condition = transform(arrayScopeProperty.getPathAsString());

    return condition;
  }

  public ASTConditionBase transform(String scope) throws Exception {
    ASTItem item = null;

    if (antlrTreeCntx.condition() != null) {
      item = this.createASTItem(antlrTreeCntx.condition());
    } else if (antlrTreeCntx.condition_group() != null) {
      item = this.createASTItem(antlrTreeCntx.condition_group());
    } else if (antlrTreeCntx.content() != null && antlrTreeCntx.content().size() > 0) {
      item = this.createASTItem(antlrTreeCntx.content().get(antlrTreeCntx.content().size() - 1));
    }

    if (item != null) {
      if (item instanceof ASTConditionBase) return (ASTConditionBase) item;
      //            else
      //            {
      //                return ASTCondition
      //            }
    }

    return null;
  }
}
