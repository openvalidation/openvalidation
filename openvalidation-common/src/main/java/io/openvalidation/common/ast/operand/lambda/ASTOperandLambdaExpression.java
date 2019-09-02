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

package io.openvalidation.common.ast.operand.lambda;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.ArrayList;
import java.util.List;

public abstract class ASTOperandLambdaExpression extends ASTOperandBase {

  private ASTOperandBase _operand;
  protected String lambdaToken;

  public ASTOperandLambdaExpression() {
    super();
  }

  public ASTOperandBase getOperand() {
    return _operand;
  }

  public void setOperand(ASTOperandBase operand) {
    this._operand = operand;
    this.updateLambdaRecursively();
  }

  public String getLambdaToken() {
    return this.lambdaToken;
  }

  public void setLambdaToken(String token) {
    this.lambdaToken = token;
    this.updateLambdaRecursively();
  }

  public void updateLambdaRecursively() {
    // sets token recursivelly
    this.walk(
        ctx -> {
          ASTOperandProperty prop = ctx.getCurrentAs(ASTOperandProperty.class);
          if (prop != null) prop.setLambdaToken(getLambdaToken());
        },
        ASTOperandProperty.class);
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.getOperand() != null) lst.addAll(this.getOperand().collectItemsOfType(cls));

    return lst;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.add(getOperand());

    return items;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + this.getType() + "\n");
    sb.append(this.space(level + 1) + "DataType : " + this.getDataType() + "\n");

    return sb.toString();
  }
}
