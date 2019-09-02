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

import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;

public class ASTVariableBuilder
    extends ASTBuilderBase<ASTVariableBuilder, ASTModelBuilder, ASTVariable> {

  public ASTVariableBuilder() {
    super(null, ASTVariable.class);
  }

  public ASTVariableBuilder(ASTModelBuilder prntBldr) {
    super(prntBldr, ASTVariable.class);
  }

  public ASTVariableBuilder createVariable(String name) {
    this.create().withName(name);

    return this;
  }

  public ASTVariableBuilder withName(String name) {
    this.model.setName(name);

    return this;
  }

  public ASTVariableBuilder withValueAsString(String value) {
    this.model.setValue(new ASTOperandStaticString(value));

    return this;
  }

  public ASTVariableBuilder withValueAsNumber(double value) {
    this.model.setValue(new ASTOperandStaticNumber(value));

    return this;
  }

  public ASTVariableBuilder withValueAsProperty(String... path) {
    this.model.setValue(new ASTOperandProperty(path));

    return this;
  }

  public ASTPropertyBuilder createValueAsProperty() {
    ASTPropertyBuilder propertyBuilder = new ASTPropertyBuilder(this);
    propertyBuilder.create();

    this.model.setValue(propertyBuilder.getModel());

    return propertyBuilder;
  }

  public ASTConditionBuilder createValueAsCondition() {
    ASTConditionBuilder conditionBuilder = new ASTConditionBuilder(this);
    conditionBuilder.create();

    this.model.setValue(conditionBuilder.getModel());

    return conditionBuilder;
  }

  public ASTOperandFunctionBuilder createValueAsFunction(String functionName) {
    ASTOperandFunctionBuilder builder = this.createValueAsFunction();
    builder.withName(functionName);

    return builder;
  }

  public ASTOperandFunctionBuilder createValueAsFunction() {
    ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder(this);
    functionBuilder.create();

    this.model.setValue(functionBuilder.getModel());

    return functionBuilder;
  }

  public ASTVariableBuilder withValue(ASTOperandBase value) {
    this.model.setValue(value);

    return this;
  }
}
