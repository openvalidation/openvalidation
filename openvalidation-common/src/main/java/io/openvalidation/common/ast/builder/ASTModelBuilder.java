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

import io.openvalidation.common.ast.*;

public class ASTModelBuilder extends ASTBuilderBase<ASTModelBuilder, ASTBuilder, ASTModel> {
  private ASTRuleBuilder ruleBuilder;
  private ASTVariableBuilder variableBuilder;

  public ASTModelBuilder() {
    super(null, ASTModel.class);
    this.create();
    this.ruleBuilder = new ASTRuleBuilder(this);
    this.variableBuilder = new ASTVariableBuilder(this);
  }

  public ASTModelBuilder(ASTBuilder parentBuilder) {
    super(parentBuilder, ASTModel.class);
    this.create();
    this.ruleBuilder = new ASTRuleBuilder(this);
    this.variableBuilder = new ASTVariableBuilder(this);
  }

  public ASTModelBuilder createComment(String... commentLines) {
    ASTComment comment = new ASTComment(commentLines);
    this.withComment(comment);

    return this;
  }

  public ASTModelBuilder withComment(ASTComment comment) {
    this.model.add(comment);

    return this;
  }

  public ASTModelBuilder with(ASTGlobalElement element) {
    this.model.add(element);

    return this;
  }

  public ASTRuleBuilder createRule() {
    this.ruleBuilder.create();
    this.withRule(this.ruleBuilder.getModel());

    return this.ruleBuilder;
  }

  public ASTRuleBuilder createRule(String name, String error) {
    this.ruleBuilder.createRule(name, error);
    this.withRule(this.ruleBuilder.getModel());

    return this.ruleBuilder;
  }

  public ASTRuleBuilder withRule(ASTRule rule) {
    this.model.add(rule);

    return this.ruleBuilder;
  }

  public ASTVariableBuilder createVariable(String name) {
    this.variableBuilder.createVariable(name);
    this.withVariable(this.variableBuilder.getModel());

    return this.variableBuilder;
  }

  public ASTVariableBuilder withVariable(ASTVariable variable) {
    this.model.add(variable);

    return this.variableBuilder;
  }

  public ASTRule getFirstRule() {
    return this.getModel().getRules().get(0);
  }

  //    @Override
  //    public ASTModel getModel(){
  //        ASTModel model = super.getModel();
  //
  //        return (model != null)? model.postProcessing() : model;
  //    }
}
