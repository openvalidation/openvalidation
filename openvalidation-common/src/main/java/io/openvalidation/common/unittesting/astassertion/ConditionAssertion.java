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

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmetical;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.unittesting.astassertion.lists.RuleListAssertion;

public class ConditionAssertion extends ConditionAssertionBase<ASTCondition, ConditionAssertion> {

  public ConditionAssertion(ASTCondition item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);
  }

  public ConditionAssertion(ASTCondition item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public ConditionAssertion hasOperator() {
    shouldNotBeEmpty(this.model.getOperator(), "OPERATOR");

    return this;
  }

  public ConditionAssertion hasIndentationLevel(int indentationLevel) {
    shouldNotBeEmpty(this.model.getIndentationLevel(), "INDENTATION LEVEL");
    shouldEquals(this.model.getIndentationLevel(), indentationLevel, "INDENTATION LEVEL");

    return this;
  }

  public ConditionAssertion hasOperator(ASTComparisonOperator operator) {
    shouldNotBeEmpty(this.model.getOperator(), "OPERATOR");
    shouldEquals(this.model.getOperator(), operator, "OPERATOR");

    return this;
  }

  public OperandAssertion leftOperand() {
    shouldNotBeEmpty(this.model.getLeftOperand(), "LEFT OPERAND");

    return new OperandAssertion(this.model.getLeftOperand(), ast, this);
  }

  public OperandAssertion rightOperand() {
    shouldNotBeEmpty(this.model.getRightOperand(), "RIGHT OPERAND");
    return new OperandAssertion(this.model.getRightOperand(), ast, this);
  }

  // left operand
  public FunctionAssertion leftFunction() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        FunctionAssertion.class, ASTOperandFunction.class, "LEFT OPERAND FUNCTION");
  }

  public PropertyAssertion leftProperty() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        PropertyAssertion.class, ASTOperandProperty.class, "LEFT OPERAND PROPERTY");
  }

  public PropertyAssertion leftProperty(String path) throws Exception {
    return leftProperty().hasPath(path);
  }

  public StaticStringAssertion leftString() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        StaticStringAssertion.class, ASTOperandStaticString.class, "LEFT OPERAND STRING");
  }

  public StaticStringAssertion leftString(String string) throws Exception {
    return leftString().hasValue(string);
  }

  public StaticNumberAssertion leftNumber() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        StaticNumberAssertion.class, ASTOperandStaticNumber.class, "LEFT OPERAND NUMBER");
  }

  public StaticBoolAssertion leftBoolean() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    shouldEquals(
        this.model.getLeftOperand().getDataType(),
        DataPropertyType.Boolean,
        "LEFT OPERAND BOOLEAN");
    return leftOperand.getAssertion(
        StaticBoolAssertion.class, ASTOperandStatic.class, "LEFT OPERAND BOOLEAN");
  }

  public StaticBoolAssertion leftBoolean(boolean boolVal) throws Exception {
    return leftBoolean().hasValue(boolVal);
  }

  public VariableReferenceAssertion leftVariable() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        VariableReferenceAssertion.class, ASTOperandVariable.class, "LEFT OPERAND VARIABLE");
  }

  public VariableReferenceAssertion leftVariable(String name) throws Exception {
    return leftVariable().hasName(name);
  }

  public OperandArithmeticalAssertion leftArithmetical() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        OperandArithmeticalAssertion.class,
        ASTOperandArithmetical.class,
        "LEFT OPERAND ARITHMETICAL");
  }

  public ASTArithmeticalOperationAssertion leftArithmeticalOperation() throws Exception {
    return leftArithmetical().operation();
  }

  public ConditionAssertion leftSubCondition() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        ConditionAssertion.class, ASTCondition.class, "LEFT OPERAND CONDITION");
  }

  public ConditionAssertion leftSubConditionGroup() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        ConditionAssertion.class, ASTCondition.class, "LEFT OPERAND CONDITION GROUP");
  }

  public ArrayAssertion leftArray() throws Exception {
    OperandAssertion leftOperand = this.leftOperand();
    return leftOperand.getAssertion(
        ArrayAssertion.class, ASTOperandArray.class, "LEFT OPERAND ARRAY");
  }

  // right operand
  public FunctionAssertion rightFunction() throws Exception {
    OperandAssertion rigthOperand = this.rightOperand();
    return rigthOperand.getAssertion(
        FunctionAssertion.class, ASTOperandFunction.class, "RIGHT OPERAND FUNCTION");
  }

  public PropertyAssertion rightProperty() throws Exception {
    OperandAssertion rightOperand = this.rightOperand();
    return rightOperand.getAssertion(
        PropertyAssertion.class, ASTOperandProperty.class, "RIGHT OPERAND PROPERTY");
  }

  public StaticStringAssertion rightString() throws Exception {
    OperandAssertion rightOperand = this.rightOperand();
    return rightOperand.getAssertion(
        StaticStringAssertion.class, ASTOperandStaticString.class, "RIGHT OPERAND STRING");
  }

  public StaticStringAssertion rightString(String string) throws Exception {
    return rightString().hasValue(string);
  }

  public StaticNumberAssertion rightNumber() throws Exception {
    OperandAssertion rightOperand = this.rightOperand();
    return rightOperand.getAssertion(
        StaticNumberAssertion.class, ASTOperandStaticNumber.class, "RIGHT OPERAND NUMBER");
  }

  public StaticNumberAssertion rightNumber(Double value) throws Exception {
    return this.rightNumber().hasValue(value);
  }

  public StaticBoolAssertion rightBoolean(boolean expected) throws Exception {
    return rightBoolean().hasValue(expected);
  }

  public StaticBoolAssertion rightBoolean() throws Exception {
    OperandAssertion rightOperand = this.rightOperand();
    shouldEquals(
        rightOperand.model.getDataType(), DataPropertyType.Boolean, "RIGHT OPERAND BOOLEAN");
    return rightOperand.getAssertion(
        StaticBoolAssertion.class, ASTOperandStatic.class, "RIGHT OPERAND BOOLEAN");
  }

  public VariableReferenceAssertion rightVariable() throws Exception {
    OperandAssertion rightOperand = this.rightOperand();
    return rightOperand.getAssertion(
        VariableReferenceAssertion.class, ASTOperandVariable.class, "RIGHT OPERAND VARIABLE");
  }

  public VariableReferenceAssertion rightVariable(String name) throws Exception {
    return rightVariable().hasName(name);
  }

  public OperandArithmeticalAssertion rightArithmetical() throws Exception {
    OperandAssertion rightOperand = this.rightOperand();
    return rightOperand.getAssertion(
        OperandArithmeticalAssertion.class,
        ASTOperandArithmetical.class,
        "RIGHT OPERAND ARITHMETICAL");
  }

  public ASTArithmeticalOperationAssertion rightArithmeticalOperation() throws Exception {
    return rightArithmetical().operation();
  }

  public ConditionAssertion rightSubCondition() throws Exception {
    OperandAssertion rightOperand = this.leftOperand();
    return rightOperand.getAssertion(
        ConditionAssertion.class, ASTCondition.class, "RIGHT OPERAND CONDITION");
  }

  public ConditionAssertion rightSubConditionGroup() throws Exception {
    OperandAssertion rightOperand = this.rightOperand();
    return rightOperand.getAssertion(
        ConditionAssertion.class, ASTCondition.class, "RIGHT OPERAND CONDITION GROUP");
  }

  public ArrayAssertion rightArray() throws Exception {
    OperandAssertion operand = this.rightOperand();
    return operand.getAssertion(ArrayAssertion.class, ASTOperandArray.class, "RIGHT OPERAND ARRAY");
  }

  @Override
  public ConditionAssertion hasConnector(ASTConditionConnector connector) {
    return (ConditionAssertion) super.hasConnector(connector);
  }

  public ConditionAssertion hasNoConnector() {
    shouldBeNull(this.model.getConnector(), "CONNECTOR");

    return this;
  }

  public RuleAssertion parentRule() {
    shouldBeInstanceOf(this.parent(), RuleAssertion.class, "PARENT RULE");

    return (RuleAssertion) this.parent();
  }

  public ConditionGroupAssertion parentConditionGroup() {
    shouldBeInstanceOf(this.parent(), ConditionGroupAssertion.class, "PARENT CONDITION GROUP");

    return (ConditionGroupAssertion) parent();
  }

  public RuleListAssertion parentRuleList() {
    RuleAssertion rule = parentRule();
    shouldBeInstanceOf(rule.parent(), RuleListAssertion.class, "PARENT RULE LIST");

    return (RuleListAssertion) rule.parent();
  }

  public ModelRootAssertion parentModel() {
    RuleListAssertion rule = parentRuleList();
    shouldBeInstanceOf(rule.parent(), ModelRootAssertion.class, "PARENT ROOT");

    return (ModelRootAssertion) rule.parent();
  }

  public VariableAssertion parentVariable() {
    shouldBeInstanceOf(parent(), VariableAssertion.class, "PARENT VARIABLE");
    return (VariableAssertion) parent;
  }

  public LambdaAssertion parentLambda() {
    shouldBeInstanceOf(parent(), LambdaAssertion.class, "PARENT LAMBDA");
    return (LambdaAssertion) parent;
  }

  public FunctionAssertion parentFunction() {
    return this.parentLambda().parentFunction();
  }

  public ConditionAssertion hasNoRightOperand() {
    shouldBeNull(model.getRightOperand(), "RIGHT OPERAND");
    return this;
  }
}
