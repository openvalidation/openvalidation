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

package io.openvalidation.common.ast.operand;

import io.openvalidation.common.ast.ASTItem;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTOperandArray extends ASTOperandBase {
  private List<ASTOperandBase> _items = new ArrayList<>();

  public List<ASTOperandBase> getItems() {
    return _items;
  }

  public void setItems(List<ASTOperandBase> items) {
    _items = items;
  }

  public void add(ASTOperandBase item) {
    this._items.add(item);
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.addAll(getItems());

    return items;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.getItems() != null && this.getItems().size() > 1) {
      return this.getItems().stream()
          .flatMap(c -> c.collectItemsOfType(cls).stream())
          .collect(Collectors.toList());
    }

    return lst;
  }
}
