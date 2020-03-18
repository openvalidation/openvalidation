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

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;

public class ArrayAssertion
    extends ASTItemAssertionBase<ASTOperandArray, ASTAssertionBase, ArrayAssertion> {

  public ArrayAssertion(ASTOperandArray item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public ArrayAssertion hasSize(int value) {
    shouldEquals(model.getItems().size(), value, "Array Size");
    return this;
  }

  public StaticStringAssertion StringAtPosition(int position) {
    shouldNotBeEmpty(this.model.getItems(), "Array Items");
    shouldNotBeEmpty(this.model.getItems().get(position), "Array Item At Position");
    shouldEquals(
        this.model.getItems().get(position).getDataType(),
        DataPropertyType.String,
        "Array Item Type");

    StaticStringAssertion staticString =
        new StaticStringAssertion(
            (ASTOperandStaticString) this.model.getItems().get(position), ast, this);
    return staticString;
  }

  public StaticNumberAssertion numberAtPosition(int position) {
    shouldNotBeEmpty(this.model.getItems(), "Array Items");
    shouldNotBeEmpty(this.model.getItems().get(position), "Array Item At Position");
    shouldEquals(
        this.model.getItems().get(position).getDataType(),
        DataPropertyType.Decimal,
        "Array Item Type");

    StaticNumberAssertion staticString =
        new StaticNumberAssertion(
            (ASTOperandStaticNumber) this.model.getItems().get(position), ast, this);
    return staticString;
  }

  public ArrayAssertion numberAtPosition(double number, int index) {
    StaticNumberAssertion numberAssertion = this.numberAtPosition(index);
    numberAssertion.hasValue(number);

    return this;
  }

  public ArrayAssertion stringAtPosition(String s, int index) {
    StaticStringAssertion numberAssertion = this.StringAtPosition(index);
    numberAssertion.hasValue(s);

    return this;
  }

  public PropertyAssertion propertyAtPosition(int index) {
    shouldNotBeEmpty(this.model.getItems(), "Array Items");
    shouldNotBeEmpty(this.model.getItems().get(index), "Array Item At Position");

    shouldBeInstanceOf(
        this.model.getItems().get(index), ASTOperandProperty.class, "ITEM AS OPERAND PROPERTY");

    return new PropertyAssertion((ASTOperandProperty) this.model.getItems().get(index), ast, this);
  }

  public VariableReferenceAssertion variableAtPosition(int index) {
    shouldNotBeEmpty(this.model.getItems(), "Array Items");
    shouldNotBeEmpty(this.model.getItems().get(index), "Array Item At Position");

    shouldBeInstanceOf(
        this.model.getItems().get(index), ASTOperandVariable.class, "ITEM AS OPERAND VARIABLE");

    return new VariableReferenceAssertion(
        (ASTOperandVariable) this.model.getItems().get(index), ast, this);
  }

  public VariableAssertion parentVariable() {
    shouldBeInstanceOf(this.parent, VariableAssertion.class, "PARENT VARIABLE");
    return (VariableAssertion) parent;
  }

  public ArrayAssertion hasContentType(DataPropertyType type) {
    shouldEquals(this.model.getContentType(), type, "ARRAY OPERAND CONTENT TYPE");
    return this;
  }
}
