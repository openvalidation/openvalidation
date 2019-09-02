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

package io.openvalidation.common.ast.operand.arithmetical;

import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.ArrayList;
import java.util.List;

public abstract class ASTOperandArithmeticalItemBase extends ASTItem {
  protected ASTArithmeticalOperator operator;
  protected ASTOperandBase operand;

  public ASTArithmeticalOperator getOperator() {
    return operator;
  }

  public void setOperator(ASTArithmeticalOperator operator) {
    this.operator = operator;
  }

  public ASTOperandBase getOperand() {
    return operand;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.add(getOperand());

    return items;
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    return null;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.operand != null) lst.addAll(operand.collectItemsOfType(cls));

    return lst;
  }

  public boolean isNumber() {
    return (this.getOperand() != null) && this.getOperand().isNumber();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (operator != null) sb.append(" ").append(operator.toString());

    if (getOperand() != null) sb.append(" ").append(operand.toString());
    else sb.append(" null");

    return sb.toString();
  }
}
