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
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTOperandArithmeticalOperation extends ASTOperandArithmeticalItemBase {
  private List<ASTOperandArithmeticalItemBase> items = new ArrayList<>();

  public ASTOperandArithmeticalOperation add(
      ASTOperandArithmeticalItemBase item, ASTArithmeticalOperator operator) {
    item.operator = operator;
    return add(item);
  }

  public ASTOperandArithmeticalOperation add(ASTOperandArithmeticalItemBase item) {
    this.items.add(item);
    this._preprocessedSource = item.getPreprocessedSource();
    return this;
  }

  public List<ASTOperandArithmeticalItemBase> getItems() {
    return items;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.addAll(getItems());

    return items;
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    List<ASTOperandProperty> props = new ArrayList<>();

    if (this.items != null) {
      for (int x = 0; x < this.items.size(); x++) {
        List<ASTOperandProperty> properties = this.items.get(x).getProperties();
        if (properties != null && properties.size() > 0) props.addAll(properties);
      }
    }

    return props;
  }

  public List<ASTOperandArithmeticalOperation> filterOperations() {
    List<ASTOperandArithmeticalOperation> operations = new ArrayList<>();

    for (ASTOperandArithmeticalItemBase item : this.getItems()) {
      if (item instanceof ASTOperandArithmeticalOperation) {
        operations.add((ASTOperandArithmeticalOperation) item);
      }
    }

    return operations;
  }

  public List<ASTOperandArithmeticalItemBase> filterNonOperations() {
    List<ASTOperandArithmeticalItemBase> nonOperations = new ArrayList<>();

    for (ASTOperandArithmeticalItemBase item : this.getItems()) {
      if (!(item instanceof ASTOperandArithmeticalOperation)) {
        nonOperations.add(item);
      }
    }

    return nonOperations;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    if (this.getOperator() != null)
      sb.append(super.space(level) + this.getOperator().name() + "\n");

    for (ASTOperandArithmeticalItemBase item : this.getItems()) {
      sb.append(item.print(level + 1));
    }

    sb.append("\n");

    return sb.toString();
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.getItems() != null && this.getItems().size() > 1) {
      lst.addAll(
          this.getItems().stream()
              .flatMap(c -> c.collectItemsOfType(cls).stream())
              .collect(Collectors.toList()));
    }

    return lst;
  }

  @Override
  public boolean isNumber() {
    return items.stream().allMatch(i -> i.isNumber());
  }

  public void updateSources() {
    StringBuilder sb = new StringBuilder();

    for (ASTOperandArithmeticalItemBase item : items) {
      if (item instanceof ASTOperandArithmeticalOperation) {
        ((ASTOperandArithmeticalOperation) item).updateSources();
      }
      sb.append(item.getPreprocessedSource());
    }

    this.setSource(sb.toString());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (ASTOperandArithmeticalItemBase item : items) {
      if (item instanceof ASTOperandArithmeticalOperation) {
        if (item.getOperator() != null) {
          sb.append(item.getOperator().toString()).append(" ");
        }
        sb.append("(").append(item.toString()).append(") ");
      } else {
        sb.append(item).append(" ");
      }
    }

    return sb.toString();
  }
}
