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

import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.arithmetical.*;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;

public class ASTOperandArithmeticalBuilder
    extends ASTBuilderBase<
        ASTOperandArithmeticalBuilder, ASTConditionBuilder, ASTOperandArithmetical> {
  private ASTOperandArithmeticalBuilder _parentBuilder;

  public ASTOperandArithmeticalBuilder() {
    super(null, ASTOperandArithmetical.class);
  }

  public ASTOperandArithmeticalBuilder(ASTConditionBuilder prntBldr) {
    super(prntBldr, ASTOperandArithmetical.class);
  }

  public ASTOperandArithmeticalBuilder(ASTOperandArithmeticalBuilder prntBldr) {
    super(null, ASTOperandArithmetical.class);
    this._parentBuilder = prntBldr;
  }

  public ASTOperandArithmeticalBuilder parentOperation() {
    return this._parentBuilder;
  }

  public ASTOperandArithmeticalBuilder withOperation(ASTOperandArithmeticalOperation operation) {
    return withOperation(operation, null);
  }

  public ASTOperandArithmeticalBuilder withOperation(
      ASTOperandArithmeticalOperation operation, ASTArithmeticalOperator operator) {
    operation.setOperator(operator);
    this.model.getOperation().add(operation);

    return this;
  }

  public ASTOperandArithmeticalBuilder withOperation(
      ASTOperandArithmeticalOperation operation,
      ASTArithmeticalOperator operator,
      String operatorSource) {
    operation.setOperator(operator);
    operation.setSource(operatorSource + operation.getUntrimmedSource());
    this.model.getOperation().add(operation);

    return this;
  }

  public ASTOperandArithmeticalBuilder withNumber(double number) {
    this.model.getOperation().add(new ASTOperandArithmeticalNumberItem(number));

    return this;
  }

  public ASTOperandArithmeticalBuilder withNumber(double number, ASTArithmeticalOperator operator) {
    ASTOperandArithmeticalNumberItem prop = new ASTOperandArithmeticalNumberItem(number);
    prop.setOperator(operator);

    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withNumber(
      ASTOperandStaticNumber number, ASTArithmeticalOperator operator, String operatorSource) {
    ASTOperandArithmeticalNumberItem prop = new ASTOperandArithmeticalNumberItem(number);
    prop.setOperator(operator);

    prop.setSource(operatorSource + " " + number.getPreprocessedSource());
    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withString(String value) {
    this.model.getOperation().add(new ASTOperandArithmeticalStringItem(value));

    return this;
  }

  public ASTOperandArithmeticalBuilder withString(String value, ASTArithmeticalOperator operator) {
    ASTOperandArithmeticalStringItem prop = new ASTOperandArithmeticalStringItem(value);
    prop.setOperator(operator);

    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withString(
      ASTOperandStaticString stringItem, ASTArithmeticalOperator operator, String operatorSource) {
    ASTOperandArithmeticalStringItem prop = new ASTOperandArithmeticalStringItem(stringItem);
    prop.setOperator(operator);

    prop.setSource(operatorSource + " " + stringItem.getPreprocessedSource());
    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withProperty(
      ASTArithmeticalOperator operator, String... path) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalPropertyItem(new ASTOperandProperty(path)), operator);

    return this;
  }

  public ASTOperandArithmeticalBuilder withProperty(String... path) {
    return withProperty(null, path);
  }

  public ASTOperandArithmeticalBuilder withProperty(ASTOperandProperty property) {
    return withProperty(property, null);
  }

  public ASTOperandArithmeticalBuilder withProperty(
      ASTOperandProperty property, ASTArithmeticalOperator operator) {
    ASTOperandArithmeticalPropertyItem prop = new ASTOperandArithmeticalPropertyItem(property);
    prop.setOperator(operator);

    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withProperty(
      ASTOperandProperty property, ASTArithmeticalOperator operator, String operatorSource) {
    ASTOperandArithmeticalPropertyItem prop = new ASTOperandArithmeticalPropertyItem(property);
    prop.setOperator(operator);

    prop.setSource(operatorSource + " " + prop.getPreprocessedSource());
    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withVariable(ASTOperandVariable variable) {
    this.model.getOperation().add(new ASTOperandArithmeticalVariable(variable));

    return this;
  }

  public ASTOperandArithmeticalBuilder withVariable(
      ASTOperandVariable property, ASTArithmeticalOperator operator) {
    ASTOperandArithmeticalVariable prop = new ASTOperandArithmeticalVariable(property);
    prop.setOperator(operator);

    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withVariable(
      ASTOperandVariable property, ASTArithmeticalOperator operator, String operatorSource) {
    ASTOperandArithmeticalVariable prop = new ASTOperandArithmeticalVariable(property);
    prop.setOperator(operator);

    prop.setSource(operatorSource + " " + prop.getPreprocessedSource());
    this.model.getOperation().add(prop);

    return this;
  }

  public ASTOperandArithmeticalBuilder withVariable(String name, ASTArithmeticalOperator operator) {
    return withVariable(new ASTOperandVariable(name), operator);
  }

  public ASTOperandArithmeticalBuilder withVariable(String name) {
    return withVariable(new ASTOperandVariable(name), null);
  }

  public ASTOperandArithmeticalBuilder mod(double number) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalNumberItem(number), ASTArithmeticalOperator.Modulo);

    return this;
  }

  public ASTOperandArithmeticalBuilder mod(ASTOperandArithmeticalItemBase item) {
    this.model.getOperation().add(item, ASTArithmeticalOperator.Modulo);

    return this;
  }

  public ASTOperandArithmeticalBuilder power_of(double number) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalNumberItem(number), ASTArithmeticalOperator.Power);

    return this;
  }

  public ASTOperandArithmeticalBuilder power_of(ASTOperandProperty inProperty) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalPropertyItem(inProperty), ASTArithmeticalOperator.Power);
    return this;
  }

  public ASTOperandArithmeticalBuilder power_of(ASTOperandArithmeticalOperation operation) {
    this.model.getOperation().add(operation, ASTArithmeticalOperator.Power);
    return this;
  }

  public ASTOperandArithmeticalBuilder plus(double number) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalNumberItem(number), ASTArithmeticalOperator.Addition);

    return this;
  }

  public ASTOperandArithmeticalBuilder plus(ASTOperandProperty inProperty) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalPropertyItem(inProperty), ASTArithmeticalOperator.Addition);
    return this;
  }

  public ASTOperandArithmeticalBuilder plus(ASTOperandArithmeticalOperation operation) {
    this.model.getOperation().add(operation, ASTArithmeticalOperator.Addition);
    return this;
  }

  public ASTOperandArithmeticalBuilder minus(double number) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalNumberItem(number), ASTArithmeticalOperator.Subtraction);
    return this;
  }

  public ASTOperandArithmeticalBuilder minus(ASTOperandProperty inProperty) {
    this.model
        .getOperation()
        .add(
            new ASTOperandArithmeticalPropertyItem(inProperty),
            ASTArithmeticalOperator.Subtraction);
    return this;
  }

  public ASTOperandArithmeticalBuilder minus(ASTOperandArithmeticalOperation operation) {
    this.model.getOperation().add(operation, ASTArithmeticalOperator.Subtraction);
    return this;
  }

  public ASTOperandArithmeticalBuilder divide(double number) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalNumberItem(number), ASTArithmeticalOperator.Division);
    return this;
  }

  public ASTOperandArithmeticalBuilder divide(ASTOperandProperty inProperty) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalPropertyItem(inProperty), ASTArithmeticalOperator.Division);
    return this;
  }

  public ASTOperandArithmeticalBuilder divide(ASTOperandArithmeticalOperation operation) {
    this.model.getOperation().add(operation, ASTArithmeticalOperator.Division);
    return this;
  }

  public ASTOperandArithmeticalBuilder multiply(double number) {
    this.model
        .getOperation()
        .add(new ASTOperandArithmeticalNumberItem(number), ASTArithmeticalOperator.Multiplication);
    return this;
  }

  public ASTOperandArithmeticalBuilder multiply(ASTOperandProperty inProperty) {
    this.model
        .getOperation()
        .add(
            new ASTOperandArithmeticalPropertyItem(inProperty),
            ASTArithmeticalOperator.Multiplication);
    return this;
  }

  public ASTOperandArithmeticalBuilder multiply(ASTOperandArithmeticalOperation operation) {
    this.model.getOperation().add(operation, ASTArithmeticalOperator.Multiplication);
    return this;
  }

  public ASTOperandArithmeticalBuilder multiplySuboperation() {
    ASTOperandArithmeticalBuilder subBuilder = new ASTOperandArithmeticalBuilder(this);
    subBuilder.create();
    this.multiply(subBuilder.getModel().getOperation());

    return subBuilder;
  }

  public ASTOperandArithmeticalBuilder addSuboperation() {
    ASTOperandArithmeticalBuilder subBuilder = new ASTOperandArithmeticalBuilder(this);
    subBuilder.create();
    this.plus(subBuilder.getModel().getOperation());

    return subBuilder;
  }

  public ASTOperandArithmeticalBuilder subOperation() {
    return subOperation(null);
  }

  public ASTOperandArithmeticalBuilder subOperation(ASTArithmeticalOperator operator) {
    ASTOperandArithmeticalBuilder subBuilder = new ASTOperandArithmeticalBuilder(this);
    subBuilder.create();
    this.withOperation(subBuilder.getModel().getOperation(), operator);

    return subBuilder;
  }

  public ASTOperandArithmeticalBuilder withEmptyOperand(
      String nullItemSource, ASTArithmeticalOperator operator, String operatorSource) {
    ASTOperandArithmeticalNumberItem emptyItem = new ASTOperandArithmeticalNumberItem(null);
    emptyItem.setOperator(operator);
    emptyItem.setSource(operatorSource + " " + nullItemSource);

    this.model.getOperation().add(emptyItem);

    return this;
  }
  //    public ASTOperandArithmeticalBuilder addOperator(ASTArithmeticalOperator operator){
  //        this.model.getOperation().add(new ASTOperandArithmeticalNumberItem(number),
  // ASTArithmeticalOperator.Multiplication);
  //
  //
  //
  //        return this;
  //    }
}
