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
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmetical;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.unittesting.astassertion.lists.VariableListAssertion;
import java.lang.reflect.Constructor;

public class VariableAssertion
    extends ASTItemAssertionBase<ASTVariable, ASTAssertionBase, VariableAssertion> {

  public VariableAssertion(ASTVariable item, ASTModel ast, ASTAssertionBase parent) {
    super(item, 0, ast, parent);
  }

  public VariableAssertion(ASTVariable item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);
  }

  public VariableAssertion shouldNotBeEmpty() {
    super.shouldNotBeEmpty();

    shouldNotBeEmpty(model.getName(), "Name");
    shouldNotBeEmpty(model.getValue(), "Value");

    return this;
  }

  public VariableAssertion shouldBeEmpty() {
    super.shouldNotBeEmpty();

    shouldNotBeEmpty(model.getName(), "Name");
    shouldBeNull(model.getValue(), "Value");

    return this;
  }

  public VariableAssertion hasName(String name) {
    this.shouldNotBeEmpty();

    shouldEquals(model.getName(), name, "Name");

    return this;
  }

  public VariableAssertion hasCodeSafeName(String name) {
    this.shouldNotBeEmpty();

    shouldEquals(model.getCodeSafeName(), name, "Codesafe Name");

    return this;
  }

  public VariableReferenceAssertion variable() throws Exception {
    return getAssertion(VariableReferenceAssertion.class, ASTOperandVariable.class);
  }

  public StaticStringAssertion string() throws Exception {
    return getAssertion(StaticStringAssertion.class, ASTOperandStaticString.class);
  }

  public VariableAssertion hasString(String value) throws Exception {
    string().hasValue(value);

    return this;
  }

  public StaticNumberAssertion number() throws Exception {
    return getAssertion(StaticNumberAssertion.class, ASTOperandStaticNumber.class);
  }

  public VariableAssertion hasNumber(Double value) throws Exception {
    number().hasValue(value);

    return this;
  }

  public VariableAssertion hasType(DataPropertyType type) {
    shouldNotBeEmpty();
    shouldEquals(model.getDataType(), type, "DATA VARIABLE TYPE");

    return this;
  }

  public PropertyAssertion operandProperty() throws Exception {
    return getAssertion(PropertyAssertion.class, ASTOperandProperty.class);
  }

  public VariableReferenceAssertion operandVariable() throws Exception {
    return getAssertion(VariableReferenceAssertion.class, ASTOperandVariable.class);
  }

  public FunctionAssertion operandWhereFunction() throws Exception {
    return this.operandFunction().hasName("where");
  }

  public FunctionAssertion operandFunction() throws Exception {
    return getAssertion(FunctionAssertion.class, ASTOperandFunction.class);
  }

  public OperandArithmeticalAssertion arithmetic() throws Exception {
    return getAssertion(OperandArithmeticalAssertion.class, ASTOperandArithmetical.class);
  }

  public ASTArithmeticalOperationAssertion arithmeticalOperation() throws Exception {
    return arithmetic().operation();
  }

  public ConditionAssertion condition() throws Exception {
    return getAssertion(ConditionAssertion.class, ASTCondition.class);
  }

  public ConditionGroupAssertion conditionGroup() throws Exception {
    return getAssertion(ConditionGroupAssertion.class, ASTConditionGroup.class);
  }

  public FunctionAssertion function() throws Exception {
    return getAssertion(FunctionAssertion.class, ASTOperandFunction.class);
  }

  public FunctionAssertion function(String name) throws Exception {
    FunctionAssertion function = this.function();
    return function.hasName(name);
  }

  private <S extends ASTAssertionBase, T extends ASTItem> S getAssertion(
      Class<S> assrt, Class<T> itemcls) throws Exception {
    this.shouldNotBeEmpty();

    shouldBeInstanceOf(model.getValue(), itemcls, "VALUE");

    for (Constructor constructor : assrt.getDeclaredConstructors()) {
      try {
        return (S) constructor.newInstance((T) model.getValue(), ast, this);
      } catch (IllegalArgumentException e) {
        // intentionally left blank
        // try next constructor of available
      }
    }

    throw new IllegalArgumentException("No constructor with matching signature found.");
  }

  //    TODO: parentList in VariableAssertion hinzugefügt. Noch in einer Superklasse hinzufügen?
  public VariableListAssertion parentList() {
    shouldBeInstanceOf(this.parent(), VariableListAssertion.class, "PARENT VARIABLES");
    return (VariableListAssertion) this.parent();
  }

  @Override
  public ModelRootAssertion parentModel() {
    return parentList().parentRoot();
  }
}
