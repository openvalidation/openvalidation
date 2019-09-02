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

package io.openvalidation.common.ast;

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.NameMasking;
import java.util.ArrayList;
import java.util.List;

public class ASTVariable extends ASTGlobalNamedElement {
  private ASTOperandBase value;

  private String codeSafeName;

  public ASTOperandBase getValue() {
    return value;
  }

  public void setValue(ASTOperandBase value) {
    this.value = value;
  }

  public DataPropertyType getDataType() {
    return (value != null) ? value.getDataType() : null;
  }

  @Override
  public void setName(String name) {
    this.name = name;

    this.setCodeSafeName(name);
  }

  private void setCodeSafeName(String name) {
    codeSafeName = NameMasking.maskName(name);
  }

  public String getCodeSafeName() {
    return codeSafeName;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.add(getValue());

    return items;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    //        lst.addAll(this.getValue().collectItemsOfType(cls));
    if (this.getValue() != null) {
      lst.addAll(this.getValue().collectItemsOfType(cls));
    }
    return lst;
  }

  @Override
  public List<ASTCondition> getAllConditions() {
    return this.value.getAllConditions();
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level))
        .append(super.print(level).trim())
        .append(" : ")
        .append(this.getName())
        .append("\n");

    if (value != null) sb.append(value.print(level + 1));

    return sb.toString();
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    return this.getValue().getProperties();
  }
}
