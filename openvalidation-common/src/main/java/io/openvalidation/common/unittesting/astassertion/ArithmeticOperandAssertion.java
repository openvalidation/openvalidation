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

import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.arithmetical.*;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;

public class ArithmeticOperandAssertion
    extends ASTItemAssertionBase<
        ASTOperandArithmeticalItemBase, ASTAssertionBase, ArithmeticOperandAssertion> {

  public ArithmeticOperandAssertion(
      ASTOperandArithmeticalItemBase item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);
  }

  public ArithmeticOperandAssertion(
      ASTOperandArithmeticalItemBase item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public StaticNumberAssertion staticNumber() throws Exception {
    return getAssertion(
        StaticNumberAssertion.class,
        ASTOperandArithmeticalNumberItem.class,
        ASTOperandStaticNumber.class,
        "NUMBER");
  }

  public ArithmeticOperandAssertion hasOperator(ASTArithmeticalOperator operator) {
    shouldEquals(model.getOperator(), operator, "Operator Type");
    return this;
  }

  public ArithmeticOperandAssertion hasNoOperator() {
    shouldBeNull(model.getOperator(), "ARITHMETICAL OPERATOR");
    return this;
  }

  public ArithmeticOperandAssertion hasNoOperand() {
    shouldBeNull(this.model.getOperand(), "OPERAND OF ARITHMETICAL ITEM");
    return this;
  }

  public ArithmeticOperandAssertion staticNumber(Double number) throws Exception {
    return (ArithmeticOperandAssertion) (this.staticNumber().hasValue(number).parent());
  }

  public StaticStringAssertion staticString() throws Exception {
    return getAssertion(
        StaticStringAssertion.class,
        ASTOperandArithmeticalStringItem.class,
        ASTOperandStaticString.class,
        "STRING");
  }

  public ArithmeticOperandAssertion staticString(String value) throws Exception {
    return (ArithmeticOperandAssertion) (this.staticString().hasValue(value).parent());
  }

  public PropertyAssertion propertyValue() throws Exception {
    return getAssertion(
        PropertyAssertion.class,
        ASTOperandArithmeticalPropertyItem.class,
        ASTOperandProperty.class,
        "PROPERTY");
  }

  public VariableReferenceAssertion variableValue() throws Exception {
    return getAssertion(
        VariableReferenceAssertion.class,
        ASTOperandArithmeticalVariable.class,
        ASTOperandVariable.class,
        "VARIABLE");
  }

  public ArithmeticOperandAssertion arithmeticValue() throws Exception {
    return getAssertion(
        ArithmeticOperandAssertion.class,
        ASTOperandArithmeticalOperation.class,
        ASTOperandArithmetical.class,
        "ARITHMETICAL OPERATION");
  }

  public <S extends ASTAssertionBase, T extends ASTItem, O extends ASTOperandBase> S getAssertion(
      Class<S> assrt, Class<T> cls, Class<O> operandCls, String name) throws Exception {
    this.shouldNotBeEmpty();
    this.shouldNotBeNull(this.model.getOperand(), name);

    shouldBeInstanceOf(this.model, cls, name);
    shouldBeInstanceOf(this.model.getOperand(), operandCls, name);

    return (S)
        assrt.getDeclaredConstructors()[0].newInstance((O) this.model.getOperand(), ast, this);
  }

  //    public ArithmeticAssertion parentList() {
  //        shouldBeInstanceOf(this.parent(), ArithmeticAssertion.class, "PARENT LIST");
  //
  //        return (ArithmeticAssertion)this.parent();
  //    }

  @Override
  public OperandArithmeticalAssertion parentArithmetic() {
    shouldBeInstanceOf(this.parent(), OperandArithmeticalAssertion.class, "PARENT ARITHMETIC");

    return (OperandArithmeticalAssertion) this.parent();
  }

  public ASTArithmeticalOperationAssertion parentOperation() {
    shouldBeInstanceOf(this.parent(), ASTArithmeticalOperationAssertion.class, "PARENT OPERATION");

    return (ASTArithmeticalOperationAssertion) this.parent;
  }
}
