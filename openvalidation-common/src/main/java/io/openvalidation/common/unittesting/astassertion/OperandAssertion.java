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

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmetical;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaExpression;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.unittesting.astassertion.lists.OperandListAssertion;

public class OperandAssertion
    extends ASTItemAssertionBase<ASTOperandBase, ASTAssertionBase, OperandAssertion> {

  public OperandAssertion(ASTOperandBase item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public <S extends ASTAssertionBase, T extends ASTItem> S getAssertion(
      Class<S> assrt, Class<T> cls, String name) throws Exception {
    this.shouldNotBeEmpty();
    shouldBeInstanceOf(this.model, cls, name);
    return (S) assrt.getDeclaredConstructors()[0].newInstance(this.model, ast, this);
  }

  public FunctionAssertion function() throws Exception {
    return function("");
  }

  public FunctionAssertion function(String name) throws Exception {
    return this.getAssertion(
        FunctionAssertion.class, ASTOperandFunction.class, name + " OPERAND FUNCTION");
  }

  public PropertyAssertion property() throws Exception {
    return property("");
  }

  public PropertyAssertion property(String name) throws Exception {
    return this.getAssertion(
        PropertyAssertion.class, ASTOperandProperty.class, name + " OPERAND PROPERTY");
  }

  public StaticStringAssertion string() throws Exception {
    return string("");
  }

  public StaticStringAssertion string(String name) throws Exception {
    return this.getAssertion(
        StaticStringAssertion.class, ASTOperandStaticString.class, name + " OPERAND STRING");
  }

  public StaticNumberAssertion number() throws Exception {
    return number("");
  }

  public StaticNumberAssertion number(String name) throws Exception {
    return this.getAssertion(
        StaticNumberAssertion.class, ASTOperandStaticNumber.class, name + " OPERAND NUMBER");
  }

  public StaticBoolAssertion booleanValue() throws Exception {
    return booleanValue("");
  }

  public StaticBoolAssertion booleanValue(String name) throws Exception {
    return this.getAssertion(
        StaticBoolAssertion.class, ASTOperandStatic.class, name + " OPERAND BOOLEAN");
  }

  public VariableReferenceAssertion variable() throws Exception {
    return this.getAssertion(
        VariableReferenceAssertion.class, ASTOperandVariable.class, " OPERAND VARIABLE");
  }

  public VariableReferenceAssertion variable(String name) throws Exception {
    return this.getAssertion(
        VariableReferenceAssertion.class, ASTOperandVariable.class, name + " OPERAND VARIABLE");
  }

  public OperandArithmeticalAssertion arithmetical() throws Exception {
    return arithmetical("");
  }

  public OperandArithmeticalAssertion arithmetical(String name) throws Exception {
    return this.getAssertion(
        OperandArithmeticalAssertion.class,
        ASTOperandArithmetical.class,
        name + " OPERAND ARITHMETICAL");
  }

  public ConditionAssertion condition() throws Exception {
    return condition("");
  }

  public ConditionAssertion condition(String name) throws Exception {
    return this.getAssertion(
        ConditionAssertion.class, ASTCondition.class, name + " OPERAND CONDITION");
  }

  public ConditionAssertion conditionGroup() throws Exception {
    return conditionGroup("");
  }

  public ConditionAssertion conditionGroup(String name) throws Exception {
    return this.getAssertion(
        ConditionAssertion.class, ASTCondition.class, name + " OPERAND CONDITION GROUP");
  }

  public LambdaAssertion lambda() throws Exception {
    return this.getAssertion(
        LambdaAssertion.class, ASTOperandLambdaExpression.class, " LAMBDA EXPRESSION");
  }

  public LambdaAssertion lambda(String name) throws Exception {
    return this.getAssertion(
        LambdaAssertion.class, ASTOperandLambdaExpression.class, name + " LAMBDA EXPRESSION");
  }

  public ConditionAssertion lambdaCondition() throws Exception {
    return this.lambda().condition();
  }

  public OperandListAssertion parentList() {
    shouldBeInstanceOf(this.parent(), OperandListAssertion.class, "PARENT LIST");

    return (OperandListAssertion) this.parent();
  }

  public ConditionAssertion parentCondition() {
    shouldBeInstanceOf(this.parent(), ConditionAssertion.class, "PARENT CONDITION");

    return (ConditionAssertion) this.parent();
  }

  public ConditionGroupAssertion parentConditionGroup() {
    shouldBeInstanceOf(this.parent(), ConditionGroupAssertion.class, "PARENT CONDITION");

    return (ConditionGroupAssertion) this.parent();
  }

  public VariableAssertion parentVariable() {
    shouldBeInstanceOf(this.parent(), VariableAssertion.class, "PARENT VARIABLE");
    return (VariableAssertion) this.parent();
  }

  public OperandArithmeticalAssertion parentArithmetic() {
    shouldBeInstanceOf(this.parent(), OperandArithmeticalAssertion.class, "PARENT ARITHMETIC");
    return (OperandArithmeticalAssertion) this.parent();
  }

  public FunctionAssertion parentFunction() {
    shouldBeInstanceOf(this.parent(), FunctionAssertion.class, "PARENT FUNCTION");
    return (FunctionAssertion) this.parent();
  }
}
