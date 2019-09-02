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

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import java.util.ArrayList;
import java.util.List;

public class ASTOperandArithmetical extends ASTOperandBase {
  private ASTOperandArithmeticalOperation operation;

  public ASTOperandArithmetical() {
    this.operation = new ASTOperandArithmeticalOperation();
    this.setDataType(DataPropertyType.Decimal);
  }

  public ASTOperandArithmeticalOperation getOperation() {
    return operation;
  }

  public void setOperation(ASTOperandArithmeticalOperation operation) {
    this.operation = operation;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.add(getOperation());

    return items;
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    List<ASTOperandProperty> props = new ArrayList<>();

    if (this.operation != null) {
      List<ASTOperandProperty> properties = this.operation.getProperties();
      if (properties != null && properties.size() > 0) props.addAll(properties);
    }

    return props;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(super.print(level));
    sb.append(this.getOperation().print(level + 1));

    return sb.toString();
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.operation != null) lst.addAll(this.operation.collectItemsOfType(cls));

    return lst;
  }
}
