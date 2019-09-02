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

package io.openvalidation.common.unittesting.astassertion;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.unittesting.astassertion.lists.ConditionListAssertion;
import java.util.List;

public class ConditionGroupAssertion
    extends ConditionAssertionBase<ASTConditionGroup, ConditionGroupAssertion> {
  private ConditionListAssertion _conditions;

  public ConditionGroupAssertion(
      ASTConditionGroup item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);

    this._conditions = new ConditionListAssertion(item.getConditions(), ast, this);
  }

  public ConditionGroupAssertion(ASTConditionGroup item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);

    List<ASTConditionBase> cndsns = item.getConditions();
    this._conditions = new ConditionListAssertion(cndsns, ast, this);
  }

  public ConditionAssertion first() {
    shouldHaveMinSizeOf(this.model.filterConditions(), 1, "CONDITION");
    ASTCondition condition = this.model.filterConditions().get(0);

    return new ConditionAssertion(condition, 0, this.ast, this);
  }

  public ConditionAssertion second() {
    shouldHaveMinSizeOf(this.model.filterConditions(), 2, "CONDITION");
    ASTCondition condition = this.model.filterConditions().get(1);

    return new ConditionAssertion(condition, 1, this.ast, this);
  }

  public ConditionAssertion atIndex(int i) {
    shouldHaveMinSizeOf(this.model.filterConditions(), i + 1, "CONDITION");
    ASTCondition condition = this.model.filterConditions().get(i);

    return new ConditionAssertion(condition, i, this.ast, this);
  }

  public ConditionGroupAssertion firstConditionGroup() {
    shouldHaveMinSizeOf(this.model.filterConditionGroups(), 1, "CONDITION GROUP");
    ASTConditionGroup group = this.model.filterConditionGroups().get(0);
    return new ConditionGroupAssertion(group, 0, this.ast, this);
  }

  public ConditionGroupAssertion secondConditionGroup() {
    shouldHaveMinSizeOf(this.model.filterConditionGroups(), 2, "CONDITION GROUP");
    ASTConditionGroup group = this.model.filterConditionGroups().get(1);
    return new ConditionGroupAssertion(group, 1, this.ast, this);
  }

  public ConditionGroupAssertion hasSize(int i) {
    shouldEquals(this.model.getConditions().size(), i, "CONDITION LIST SIZE");

    return this;
  }

  @Override
  public ConditionGroupAssertion hasConnector(ASTConditionConnector connector) {
    return (ConditionGroupAssertion) super.hasConnector(connector);
  }

  public ConditionGroupAssertion parentConditionGroup() {
    shouldBeInstanceOf(this.parent, ConditionGroupAssertion.class, "PARENT");

    return (ConditionGroupAssertion) this.parent;
  }

  public ConditionGroupAssertion hasNoConnector() {
    shouldBeNull(this.model.getConnector(), "CONNECTOR");

    return this;
  }
}
