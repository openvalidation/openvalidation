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

package io.openvalidation.common.ast.condition;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class ASTConditionGroup extends ASTConditionBase {

  private boolean isInverted;

  private List<ASTConditionBase> conditions = new ArrayList<>();

  public List<ASTConditionBase> getConditions() {
    return conditions;
  }

  public void setConditions(List<ASTConditionBase> conditions) {
    this.conditions = conditions;
  }

  public void addCondition(ASTConditionBase condition) {
    this.conditions.add(condition);
  }

  public void addConditions(List<ASTCondition> conditions) {
    this.conditions.addAll(conditions);
  }

  public boolean hasConstrainedCondition() {
    for (ASTConditionBase cnd : conditions) {
      if (cnd instanceof ASTConditionGroup && ((ASTConditionGroup) cnd).hasConstrainedCondition())
        return true;
      else if (cnd instanceof ASTCondition && ((ASTCondition) cnd).isConstrainedCondition())
        return true;
    }

    return false;
  }

  public void invertConnector() {
    if (!this.isInverted) {
      this.isInverted = true;
      for (ASTConditionBase cnd : conditions) {
        if (cnd.getConnector() != null && cnd.getConnector() != ASTConditionConnector.UNLESS) {
          cnd.setConnector(
              (cnd.getConnector() == ASTConditionConnector.AND)
                  ? ASTConditionConnector.OR
                  : ASTConditionConnector.AND);
        }

        if (cnd instanceof ASTConditionGroup) ((ASTConditionGroup) cnd).invertConnector();
      }
    }
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.addAll(getConditions());

    return items;
  }

  @Override
  public List<ASTOperandProperty> getAllParentProperties() {
    List<ASTOperandProperty> properties =
        getConditions().stream()
            .flatMap(c -> c.getAllParentProperties().stream())
            .collect(Collectors.toList());

    return sortPrecoditionProperties(properties);
  }

  @Override
  public void invertOperator() {
    getConditions().forEach(c -> c.invertOperator());
  }

  @Override
  public List<ASTOperandProperty> getProperties() {

    List<ASTOperandProperty> props =
        this.conditions.stream()
            .filter(c -> c.getProperties() != null)
            .flatMap(c -> c.getProperties().stream())
            .collect(Collectors.toList());

    return props;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    lst.addAll(
        this.conditions.stream()
            .flatMap(c -> c.collectItemsOfType(cls).stream())
            .collect(Collectors.toList()));

    return lst;
  }

  @Override
  public List<ASTCondition> getAllConditions() {
    List<ASTCondition> conditions = new ArrayList<>();

    for (ASTConditionBase condition : this.getConditions()) {
      conditions.addAll(condition.getAllConditions());
    }

    return conditions;
  }

  public List<ASTConditionGroup> filterConditionGroups() {
    List<ASTConditionGroup> conditionGroups = new ArrayList<>();

    for (ASTConditionBase base : this.getConditions()) {
      if (base instanceof ASTConditionGroup) {
        conditionGroups.add((ASTConditionGroup) base);
      }
    }

    return conditionGroups;
  }

  public List<ASTCondition> filterConditions() {
    List<ASTCondition> conditions = new ArrayList<>();

    for (ASTConditionBase base : this.getConditions()) {
      if (base instanceof ASTCondition) {
        conditions.add((ASTCondition) base);
      }
    }

    return conditions;
  }

  public void UpdateSources() {
    StringBuilder sb = new StringBuilder();
    for (ASTConditionBase condition : conditions) {
      if (condition instanceof ASTConditionGroup) {
        if (io.openvalidation.common.utils.StringUtils.isNullOrEmpty(
            condition.getPreprocessedSource())) ((ASTConditionGroup) condition).UpdateSources();
      }
      sb.append(condition.getPreprocessedSource());
    }

    if (StringUtils.isEmpty(this.getPreprocessedSource())) {
      this.setSource(sb.toString());
    }
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + getType() + "\n");
    sb.append(this.space(level + 1) + "CONNECTOR: " + this.getConnector() + "\n");

    for (ASTConditionBase condition : this.conditions) {
      sb.append(condition.print(level + 1));
    }

    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (getConnector() != null) {
      sb.append(this.getConnector()).append(" ");
    }
    sb.append("( ");

    for (ASTConditionBase element : conditions) {
      sb.append(element.toString());
    }

    sb.append(") ");
    return sb.toString();
  }
}
