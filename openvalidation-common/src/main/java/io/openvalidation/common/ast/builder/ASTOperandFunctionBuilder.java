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
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaProperty;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.StringUtils;

public class ASTOperandFunctionBuilder
    extends ASTBuilderBase<ASTOperandFunctionBuilder, ASTConditionBuilder, ASTOperandFunction> {
  private ASTOperandFunctionBuilder _parentFuncBuilder;
  private ASTVariableBuilder _parentVariableBuilder;

  public ASTOperandFunctionBuilder() {
    super(null, ASTOperandFunction.class);
  }

  public ASTOperandFunctionBuilder(ASTConditionBuilder prntBldr) {
    super(prntBldr, ASTOperandFunction.class);
  }

  public ASTOperandFunctionBuilder(ASTOperandFunctionBuilder prntBldr) {
    super(null, ASTOperandFunction.class);
    this._parentFuncBuilder = prntBldr;
  }

  public ASTOperandFunctionBuilder(ASTVariableBuilder prntBldr) {
    super(null, ASTOperandFunction.class);
    this._parentVariableBuilder = prntBldr;
  }

  public ASTOperandFunctionBuilder addParameter(ASTOperandBase item) {
    this.model.addParameter(item);

    return this;
  }

  public ASTOperandFunctionBuilder addPropertyParameter(String... propertyPath) {
    return this.addParameter(new ASTOperandProperty(propertyPath));
  }

  public ASTOperandFunctionBuilder addNumberParameter(double number) {
    return this.addParameter(new ASTOperandStaticNumber(number));
  }

  public ASTOperandFunctionBuilder addLambdaConditionParameter(
      ASTOperandLambdaCondition condition) {
    return this.addParameter(condition);
  }

  public ASTOperandFunctionBuilder addLambdaConditionParameter(
      ASTConditionBase condition, String lambdaToken) {
    ASTOperandLambdaCondition lambdaCondition = new ASTOperandLambdaCondition(condition);
    lambdaCondition.setLambdaToken(lambdaToken);
    return addLambdaConditionParameter(lambdaCondition);
  }

  public ASTOperandFunctionBuilder addLambdaParameter(String labdaToken, String... propertyPath) {
    ASTOperandProperty property = new ASTOperandProperty(propertyPath);
    property.setLambdaToken(labdaToken);

    return this.addParameter(property);
  }

  public ASTOperandFunctionBuilder withName(String name) {
    this.model.setName(name);
    return this;
  }

  public ASTOperandFunctionBuilder withDataType(DataPropertyType type) {
    this.model.setDataType(type);
    return this;
  }

  public ASTOperandFunctionBuilder addParameterAsFunction() {
    ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder(this);
    functionBuilder.create();

    this.addParameter(functionBuilder.getModel());

    return functionBuilder;
  }

  public ASTOperandFunctionBuilder addParameterAsFunction(String name) {
    ASTOperandFunctionBuilder functionBuilder = this.addParameterAsFunction();
    functionBuilder.withName(name);

    return functionBuilder;
  }

  public ASTVariableBuilder getParentVariableBuilder() {
    return _parentVariableBuilder;
  }

  public ASTOperandFunctionBuilder getParentFuncBuilder() {
    return _parentFuncBuilder;
  }

  public ASTOperandFunctionBuilder createFunction(String functionName) {
    return this.create().withName(functionName);
  }

  // where presets
  // where

  public ASTOperandFunctionBuilder createWhereFunction(String arrayProperty) {
    String[] addressPath = {arrayProperty};

    return createWhereFunction(addressPath);
  }

  public ASTOperandFunctionBuilder createWhereFunction(String[] arrayProperty) {
    return this.createFunction("WHERE")
        .withDataType(DataPropertyType.Array)
        .addPropertyParameter(arrayProperty);
  }

  public ASTOperandFunctionBuilder createWhereFunction(ASTOperandBase operand) {
    return this.createFunction("WHERE").withDataType(DataPropertyType.Array).addParameter(operand);
  }

  public ASTOperandFunctionBuilder createMapFunction(ASTOperandBase operand) {
    return this.createFunction("GET_ARRAY_OF")
        .withDataType(DataPropertyType.Array)
        .addParameter(operand);
  }

  public ASTOperandFunctionBuilder addLambdaConditionParamenter(ASTConditionBase condition) {
    return addLambdaConditionParamenter(condition, null);
  }

  public ASTOperandFunctionBuilder addLambdaConditionParamenter(
      ASTConditionBase condition, String lambdaToken) {
    ASTOperandLambdaCondition lambdaExpression = new ASTOperandLambdaCondition(condition);

    if (!StringUtils.isNullOrEmpty(lambdaToken)) lambdaExpression.setLambdaToken(lambdaToken);
    this.addParameter(lambdaExpression);

    return this;
  }

  // array of function presets:
  public ASTOperandFunctionBuilder addParameterAsArrayOfFunction(
      String[] arrayProperties, String[] lambdaProperties) {
    return addParameterAsArrayOfFunction(
        arrayProperties, ASTOperandProperty.generateLambdaToken(this), lambdaProperties);
  }

  public ASTOperandFunctionBuilder addParameterAsArrayOfFunction(
      String arrayProperty, String lambdaProperties) {
    return addParameterAsArrayOfFunction(
        arrayProperty, ASTOperandProperty.generateLambdaToken(this), lambdaProperties);
  }

  public ASTOperandFunctionBuilder addParameterAsArrayOfFunction(
      String arrayProperty, String lambdaToken, String... lambdaProperties) {
    String[] addressPath = {arrayProperty};
    return addParameterAsArrayOfFunction(addressPath, lambdaToken, lambdaProperties);
  }

  public ASTOperandFunctionBuilder addParameterAsArrayOfFunction(
      String[] arrayProperty, String lambdaToken, String[] lambdaProperties) {
    ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder(this);
    functionBuilder.createArrayOfFunction(arrayProperty, lambdaToken, lambdaProperties);

    this.addParameter(functionBuilder.getModel());

    return functionBuilder;
  }

  // create

  public ASTOperandFunctionBuilder createArrayOfFunction(
      String[] arrayProperties, String[] lambdaProperties) {
    return createArrayOfFunction(
        arrayProperties, ASTOperandProperty.generateLambdaToken(this), lambdaProperties);
  }

  public ASTOperandFunctionBuilder createArrayOfFunction(
      String arrayProperty, String lambdaProperties) {
    return createArrayOfFunction(
        arrayProperty, ASTOperandProperty.generateLambdaToken(this), lambdaProperties);
  }

  public ASTOperandFunctionBuilder createArrayOfFunction(
      String arrayProperty, String lambdaToken, String... lambdaProperties) {
    String[] addressPath = {arrayProperty};

    return createArrayOfFunction(addressPath, lambdaToken, lambdaProperties);
  }

  public ASTOperandFunctionBuilder createArrayOfFunction(
      String[] arrayProperty, String lambdaToken, String[] lambdaProperties) {
    ASTOperandLambdaProperty lambda = new ASTOperandLambdaProperty();
    ASTOperandProperty property = new ASTOperandProperty(lambdaProperties);
    property.setLambdaToken(lambdaToken);
    lambda.setLambdaToken(lambdaToken);
    lambda.setProperty(property);

    return this.createFunction("GET_ARRAY_OF")
        .withDataType(DataPropertyType.Array)
        .addPropertyParameter(arrayProperty)
        .addParameter(lambda);
  }

  public ASTOperandFunctionBuilder createFirstFunction(String[] arrayProperty) {
    return createFirstFunction(arrayProperty, 1);
  }

  public ASTOperandFunctionBuilder createFirstFunction(String[] arrayProperty, int amount) {
    return this.createFunction("FIRST")
        .addPropertyParameter(arrayProperty)
        .addNumberParameter(amount);
  }

  public ASTOperandFunctionBuilder createLastFunction(String[] arrayProperty) {
    return createLastFunction(arrayProperty, 1);
  }

  public ASTOperandFunctionBuilder createLastFunction(String[] arrayProperty, int amount) {
    return this.createFunction("LAST")
        .addPropertyParameter(arrayProperty)
        .addNumberParameter(amount);
  }
}
