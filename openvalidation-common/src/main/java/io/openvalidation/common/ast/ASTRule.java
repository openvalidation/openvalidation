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
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.ArrayList;
import java.util.List;

public class ASTRule extends ASTGlobalNamedElement {
  private List<ASTConditionBase> _invalidConditions = new ArrayList<>();
  private ASTConditionBase condition;

  private ASTActionBase action;

  public ASTConditionBase getCondition() {
    return condition;
  }

  public void setCondition(ASTConditionBase con) {
    condition = con;
  }

  public ASTActionBase getAction() {
    return action;
  }

  public void setAction(ASTActionBase action) {
    this.action = action;
  }

  public List<ASTOperandProperty> getRelatedProperties() {
    return this.condition.getProperties();
  }

  public List<ASTOperandProperty> getAllParentProperties() {
    List<ASTOperandProperty> properties = new ArrayList<>();

    if (this.getCondition() != null)
      properties.addAll(this.getCondition().getAllParentProperties());

    return properties;
  }

  public void addInvalidCondition(ASTConditionBase condition) {
    this._invalidConditions.add(condition);
  }

  public List<ASTConditionBase> getInvalidConditions() {
    return this._invalidConditions;
  }

  public boolean isConstrainedRule() {
    if (condition instanceof ASTCondition)
      return ((ASTCondition) condition).isConstrainedCondition();
    else if (condition instanceof ASTConditionGroup)
      return ((ASTConditionGroup) condition).hasConstrainedCondition();

    return false;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.add(getCondition());
    items.addAll(getInvalidConditions());
    items.add(getAction());

    return items;
  }

  @Override
  public List<ASTCondition> getAllConditions() {
    if (this.getCondition() == null) return super.getAllConditions();

    return this.getCondition().getAllConditions();
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + getType()); // + " : " + getName());
    if (this.action != null) sb.append(this.action.print(level + 1));
    sb.append("\n");

    if (this.condition != null) sb.append(this.condition.print(level + 1));

    return sb.toString();
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.condition != null) lst.addAll(this.condition.collectItemsOfType(cls));

    if (this.action != null) lst.addAll(this.action.collectItemsOfType(cls));

    return lst;
  }
}
