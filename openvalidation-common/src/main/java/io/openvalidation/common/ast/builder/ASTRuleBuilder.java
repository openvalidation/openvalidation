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

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.condition.ASTConditionBase;

public class ASTRuleBuilder extends ASTBuilderBase<ASTRuleBuilder, ASTModelBuilder, ASTRule> {
  private ASTConditionBuilder conditionBuilder;
  private ASTConditionGroupBuilder conditionGroupBuilder;
  // private ASTConditionFunctionBuilder conditionFunctionBuilder;

  public ASTRuleBuilder() {
    this(null);
  }

  public ASTRuleBuilder(ASTModelBuilder prntBldr) {
    super(prntBldr, ASTRule.class);
    this.conditionBuilder = new ASTConditionBuilder(this);
    this.conditionGroupBuilder = new ASTConditionGroupBuilder(this);
    // this.conditionFunctionBuilder = new ASTConditionFunctionBuilder(this);
  }

  public ASTRuleBuilder createRule(String name, String error) {
    this.create().withName(name).withError(error);

    return this;
  }

  public ASTConditionBuilder createCondition() {
    this.conditionBuilder.create();
    this.model.setCondition(this.conditionBuilder.getModel());
    return conditionBuilder;
  }

  public ASTConditionGroupBuilder createConditionGroup() {
    this.conditionGroupBuilder.create();
    this.model.setCondition(this.conditionGroupBuilder.getModel());
    return conditionGroupBuilder;
  }

  //    public ASTConditionFunctionBuilder createConditionFunction(String functionName) throws
  // Exception{
  //        this.conditionFunctionBuilder.create()
  //                                            .withName(functionName);
  //        this.model.setCondition(this.conditionFunctionBuilder.getModel());
  //        return conditionFunctionBuilder;
  //    }

  public ASTRuleBuilder withName(String name) {
    this.model.setName(name);
    return this;
  }

  public ASTRuleBuilder withError(String error) {
    this.model.setAction(new ASTActionError(error));
    return this;
  }

  public ASTRuleBuilder withError(ASTActionError error) {
    this.model.setAction(error);
    return this;
  }

  public ASTRuleBuilder withCondition(ASTConditionBase condition) {
    this.model.setCondition(condition);

    return this;
  }
}
