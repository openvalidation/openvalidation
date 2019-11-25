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

import io.openvalidation.antlr.transformation.parsetree.PTContentTransformer;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.builder.ASTBuilderBase;
import io.openvalidation.common.ast.builder.ASTOperandFunctionBuilder;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.*;
import io.openvalidation.common.utils.ThrowingConsumer;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public abstract class TransformerBase<
    TFactory, TModel extends ASTItem, TPRContext extends ParserRuleContext> {
  protected TPRContext antlrTreeCntx;
  protected TransformerContext factoryCntx;
  protected Class<TFactory> cls;

  public TransformerBase(TPRContext treeCntx, TransformerContext fctx) {
    antlrTreeCntx = treeCntx;
    factoryCntx = fctx;
  }

  protected void eachTreeChild(ThrowingConsumer<? super ParseTree> action) throws Exception {
    if (antlrTreeCntx != null) eachTreeChild(antlrTreeCntx.children, action);
  }

  protected void eachTreeChild(List<ParseTree> children, ThrowingConsumer<? super ParseTree> action)
      throws Exception {
    if (children != null) {
      children.forEach(action);
    }
  }

  public abstract TModel transform() throws Exception;

  public <T extends TransformerBase> T loadFactory(ParseTree tree) {
    TransformerBase factory = TransformerFactory.create(tree, this.factoryCntx);

    return (factory != null) ? (T) factory : null;
  }

  public ASTItem createASTItem(ParseTree tree) throws Exception {
    TransformerBase factory = TransformerFactory.create(tree, this.factoryCntx);
    ASTItem item = (factory != null) ? factory.transform() : null;

    if (item != null) item.setSource(tree.getText());

    return item;
  }

  public <TBuilder extends ASTBuilderBase> TBuilder createBuilder(Class<TBuilder> bldrCls)
      throws Exception {
    return createBuilder(bldrCls, antlrTreeCntx.getText());
  }

  public <TBuilder extends ASTBuilderBase> TBuilder createBuilder(
      Class<TBuilder> bldrCls, String altSource) throws Exception {
    TBuilder builder = (TBuilder) bldrCls.newInstance();
    builder.create();
    builder.withSource(altSource);

    return builder;
  }

  public ASTOperandBase createProperty(String content) {
    ASTOperandBase operand = null;

    DataPropertyBase property = this.factoryCntx.resolveProperty(content);

    if (property instanceof DataProperty) {
      operand = new ASTOperandProperty(((DataProperty) property).getFullNameAsParts());
      operand.setDataType(property.getType());
      operand.setSource(content);
      if (property.getType() == DataPropertyType.Array) {
        ((ASTOperandProperty) operand).setArrayContentType(property.getArrayContentType());
      }
    } else if (property instanceof DataVariableReference) {
      operand = new ASTOperandVariable(property.getName());
      operand.setSource(content);
      operand.setDataType(property.getType());
    } else if (property instanceof DataArrayProperty) {
      DataArrayProperty p = (DataArrayProperty) property;

      ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder();
      functionBuilder.createArrayOfFunction(p);

      //            ASTOperandProperty arrayProperty = new
      // ASTOperandProperty(p.getArrayPathAsList());
      //            arrayProperty.setDataType(DataPropertyType.Array);
      //            arrayProperty.setSource(p.getArrayPath());
      //
      //            ASTOperandProperty lambdaProperty = new ASTOperandProperty(p.getName());
      //            lambdaProperty.setLambdaToken("s");
      //            lambdaProperty.setDataType(p.getType());
      //            lambdaProperty.setSource(p.getName());
      //
      //            ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder();
      //            functionBuilder.create()
      //                           .withName("GET_ARRAY_OF")
      //                           .addParameter(arrayProperty)
      //                           .addParameter(lambdaProperty);
      //
      operand = functionBuilder.getModel();
      operand.setSource(content);
      operand.setDataType(DataPropertyType.Array);
    }

    return operand;
  }

  public TModel postprocess(TModel model) {
    this.factoryCntx.getPostProcessor().process(model);

    return model;
  }

  public ASTItem resolveContent(String text) {
    PTContentTransformer ct = new PTContentTransformer(null, factoryCntx);
    return ct.resolveContentString(text);
  }
}
