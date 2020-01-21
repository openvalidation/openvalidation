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

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.StringUtils;
import java.util.List;

public class ASTConditionBuilder
    extends ASTBuilderBase<ASTConditionBuilder, ASTRuleBuilder, ASTCondition> {
  private ASTConditionGroupBuilder parentGroup;
  private ASTPropertyBuilder parentProperty;
  private ASTVariableBuilder parentVariableBuilder;

  public ASTConditionBuilder() {
    super(null, ASTCondition.class);
  }

  public ASTConditionBuilder(ASTRuleBuilder prntBldr) {
    super(prntBldr, ASTCondition.class);
  }

  public ASTConditionBuilder(ASTPropertyBuilder parent) {
    super(null, ASTCondition.class);
    this.parentProperty = parent;
  }

  public ASTConditionBuilder(ASTConditionGroupBuilder conditionGroup) {
    super(null, ASTCondition.class);
    this.parentGroup = conditionGroup;
  }

  public ASTConditionBuilder(ASTVariableBuilder prntBldr) {
    super(null, ASTCondition.class);
    this.parentVariableBuilder = prntBldr;
  }

  public ASTPropertyBuilder parentProperty() {
    return this.parentProperty;
  }

  public ASTVariableBuilder parentVariable() {
    return this.parentVariableBuilder;
  }

  public ASTConditionBuilder withConnector(ASTConditionConnector connector) {
    this.model.setConnector(connector);
    return this;
  }

  public ASTConditionBuilder withOperator(ASTComparisonOperator operator) {
    this.model.setOperator(operator);
    return this;
  }

  public ASTConditionBuilder withLeftOperandAsProperty(String... property) {
    this.model.setLeftOperand(new ASTOperandProperty(property));

    return this;
  }

  public ASTConditionBuilder withLeftOperandAsPropertyWithLambdayToken(
      String property, String lambdaToken) {

    String[] prop = property.isEmpty() ? new String[0] : new String[] {property};

    return withLeftOperandAsPropertyWithLambdayToken(prop, lambdaToken);
  }

  public ASTConditionBuilder withLeftOperandAsPropertyWithLambdayToken(
      String[] property, String lambdaToken) {
    ASTOperandProperty prop = new ASTOperandProperty(property);
    if (!StringUtils.isNullOrEmpty(lambdaToken)) prop.setLambdaToken(lambdaToken);
    this.model.setLeftOperand(prop);

    return this;
  }

  public ASTConditionBuilder withLeftOperandAsString(String value) {
    this.model.setLeftOperand(new ASTOperandStaticString(value));

    return this;
  }

  public ASTConditionBuilder withLeftOperandAsNumber(double number) {
    this.model.setLeftOperand(new ASTOperandStaticNumber((double) number));

    return this;
  }

  public ASTConditionBuilder withLeftOperandAsVariable(String variable) {
    this.model.setLeftOperand(new ASTOperandVariable(variable));

    return this;
  }

  public ASTConditionBuilder withLeftOperand(ASTOperandBase operand) {
    this.model.setLeftOperand(operand);
    return this;
  }

  public ASTConditionBuilder withLeftOperandAsBoolean(boolean value) {
    this.model.setLeftOperand(new ASTOperandStatic(("" + value).toLowerCase()));
    return this;
  }

  public ASTConditionBuilder markAsConstrainedCondition() {
    this.model.setConstrainedCondition(true);
    return this;
  }

  public ASTConditionBuilder unmarkAsConstrainedCondition() {
    this.model.setConstrainedCondition(false);
    return this;
  }

  public ASTOperandArrayBuilder withLeftOperandAsArray() {
    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(this);
    arrayBuilder.create();

    this.model.setLeftOperand(arrayBuilder.getModel());

    return arrayBuilder;
  }

  public ASTOperandArrayBuilder withLeftOperandAsArray(float... numbers) {
    ASTOperandArrayBuilder arrayBuilder = this.withLeftOperandAsArray();

    if (numbers != null && numbers.length > 0) {
      for (float num : numbers) {
        arrayBuilder.addItem(new ASTOperandStaticNumber(num));
      }
    }

    return arrayBuilder;
  }

  public ASTOperandArithmeticalBuilder createLeftOperandAsArithmeticalOperation() {
    ASTOperandArithmeticalBuilder arithmeticalBuilder = new ASTOperandArithmeticalBuilder(this);
    arithmeticalBuilder.create();

    this.model.setLeftOperand(arithmeticalBuilder.getModel());

    return arithmeticalBuilder;
  }

  public ASTOperandFunctionBuilder createLeftOperandAsFunction() {
    ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder(this);
    functionBuilder.create();

    this.model.setLeftOperand(functionBuilder.getModel());

    return functionBuilder;
  }

  public ASTOperandFunctionBuilder createLeftOperandAsFunction(String name) {
    ASTOperandFunctionBuilder functionBuilder = this.createLeftOperandAsFunction();
    functionBuilder.withName(name);

    return functionBuilder;
  }

  public ASTConditionBuilder withRightOperandAsProperty(String... property) {
    this.model.setRightOperand(new ASTOperandProperty(property));

    return this;
  }

  public ASTConditionBuilder withRightOperandAsNumber(double number) {
    this.model.setRightOperand(new ASTOperandStaticNumber((double) number));

    return this;
  }

  public ASTConditionBuilder withRightOperandAsString(String value) {
    this.model.setRightOperand(new ASTOperandStaticString(value));

    return this;
  }

  public ASTConditionBuilder withRightOperandAsVariable(String variable) {
    this.model.setRightOperand(new ASTOperandVariable(variable));

    return this;
  }

  public ASTConditionBuilder withRightOperand(ASTOperandBase operand) {
    this.model.setRightOperand(operand);
    return this;
  }

  public ASTConditionBuilder withRightOperandAsBoolean(boolean value) {
    ASTOperandStatic staticBool = new ASTOperandStatic(("" + value).toLowerCase());
    staticBool.setDataType(DataPropertyType.Boolean);
    this.model.setRightOperand(staticBool);
    return this;
  }

  public ASTOperandArrayBuilder withRightOperandAsArray() {
    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(this);
    arrayBuilder.create();

    this.model.setRightOperand(arrayBuilder.getModel());

    return arrayBuilder;
  }

  public ASTOperandArrayBuilder withRightOperandAsArray(float... numbers) {
    ASTOperandArrayBuilder arrayBuilder = this.withRightOperandAsArray();

    if (numbers != null && numbers.length > 0) {
      for (float num : numbers) {
        arrayBuilder.addItem(new ASTOperandStaticNumber(num));
      }
    }

    return arrayBuilder;
  }

  public ASTOperandArrayBuilder withRightOperandAsArray(String... strings) {
    ASTOperandArrayBuilder arrayBuilder = this.withRightOperandAsArray();

    if (strings != null && strings.length > 0) {
      for (String s : strings) {
        arrayBuilder.addItem(new ASTOperandStaticString(s));
      }
    }

    return arrayBuilder;
  }

  public ASTOperandArithmeticalBuilder createRightOperandAsArithmeticalOperation() {
    ASTOperandArithmeticalBuilder arithmeticalBuilder = new ASTOperandArithmeticalBuilder(this);
    arithmeticalBuilder.create();

    this.model.setRightOperand(arithmeticalBuilder.getModel());

    return arithmeticalBuilder;
  }

  public ASTOperandFunctionBuilder createRightOperandAsFunction() {
    ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder(this);
    functionBuilder.create();

    this.model.setRightOperand(functionBuilder.getModel());

    return functionBuilder;
  }

  public void withUnknownConditions(List<ASTConditionBase> unknownConditions) {
    this.model.setUnresolvedConditions(unknownConditions);
  }

  public void withUnknownCondition(ASTConditionBase unknownCondition) {
    this.model.getUnresolvedConditions().add(unknownCondition);
  }

  public ASTConditionGroupBuilder parentGroup() {
    return this.parentGroup;
  }
}
