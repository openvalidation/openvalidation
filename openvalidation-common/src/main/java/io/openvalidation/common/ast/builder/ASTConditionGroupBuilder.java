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

package io.openvalidation.common.ast.builder;

import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.condition.ASTConditionGroup;

public class ASTConditionGroupBuilder
    extends ASTBuilderBase<ASTConditionGroupBuilder, ASTRuleBuilder, ASTConditionGroup> {
  private ASTConditionBuilder conditionBuilder;

  private ASTConditionGroupBuilder parentGroupBuilder;

  public ASTConditionGroupBuilder() {
    super(null, ASTConditionGroup.class);
    this.conditionBuilder = new ASTConditionBuilder(this);
  }

  public ASTConditionGroupBuilder(ASTRuleBuilder prntBldr) {
    super(prntBldr, ASTConditionGroup.class);
    this.conditionBuilder = new ASTConditionBuilder(this);
  }

  public ASTConditionGroupBuilder(ASTConditionGroupBuilder prntBldr) {
    super(null, ASTConditionGroup.class);
    parentGroupBuilder = prntBldr;
    this.conditionBuilder = new ASTConditionBuilder(this);
  }

  public ASTConditionBuilder createCondition() {
    this.conditionBuilder.create();
    this.model.addCondition(this.conditionBuilder.getModel());

    //        this.model.UpdateSources();

    return conditionBuilder;
  }

  public ASTConditionGroupBuilder createConditionGroup() {
    ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(this);
    builder.model = new ASTConditionGroup();

    this.model.addCondition(builder.model);

    //        this.model.UpdateSources();

    return builder;
  }

  public ASTConditionBuilder appendCondition(ASTConditionConnector connector) {
    return this.createCondition().withConnector(connector);
  }

  public ASTConditionGroupBuilder appendConditionGroup(ASTConditionConnector connector) {
    return this.createConditionGroup().withConnector(connector);
  }

  public ASTConditionGroupBuilder withConnector(ASTConditionConnector connector) {
    this.model.setConnector(connector);

    //        this.model.UpdateSources();

    return this;
  }

  public ASTConditionGroupBuilder withCondition(ASTConditionBase condition) {
    if (condition != null) this.model.addCondition(condition);

    this.model.UpdateSources();

    return this;
  }
}
