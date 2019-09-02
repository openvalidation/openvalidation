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
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.unittesting.astassertion.lists.OperandListAssertion;

public class FunctionAssertion
    extends ASTItemAssertionBase<ASTOperandFunction, ASTAssertionBase, FunctionAssertion> {
  OperandListAssertion parameters = null;

  public FunctionAssertion(ASTOperandFunction item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);

    this.parameters = new OperandListAssertion("FUNCTION " + item.getName() + " ", ast, this);
    this.parameters.fillList(item.getParameters(), p -> new OperandAssertion(p, ast, parameters));
  }

  public FunctionAssertion hasName(String name) {
    this.shouldNotBeNull(this.model, "FUNCTION");
    this.shouldNotBeEmpty(model.getName(), "FUNCTION NAME");
    this.shouldEquals(model.getName(), name, "FUNCTION NAME");

    return this;
  }

  public FunctionAssertion hasType(DataPropertyType expectedType) {
    shouldEquals(this.model.getDataType(), expectedType, "DataType");

    return this;
  }

  public FunctionAssertion sizeOfParameters(int size) {
    this.shouldHaveSizeOf(model.getParameters(), size, "PARAMETERS");

    return this;
  }

  public OperandListAssertion parameters() {
    return this.parameters;
  }

  // first

  public PropertyAssertion firstProperty(String path) throws Exception {
    return firstProperty().hasPath(path);
  }

  public PropertyAssertion firstProperty() throws Exception {
    return this.parameters().first().property();
  }

  public FunctionAssertion firstFunction() throws Exception {
    shouldHaveMinSizeOf(model.getParameters(), 1, "PARAMETERS");

    OperandAssertion operand = new OperandAssertion(this.model.getParameters().get(0), ast, this);

    return operand.function();
  }

  public FunctionAssertion firstArrayOfFunction() throws Exception {
    return firstFunction("getArrayOf");
  }

  public FunctionAssertion firstWhereFunction() throws Exception {
    return firstFunction("where");
  }

  public FunctionAssertion firstFunction(String name) throws Exception {
    FunctionAssertion function = this.firstFunction();

    return function.hasName(name);
  }

  public VariableReferenceAssertion firstVariable() throws Exception {
    return this.parameters().first().variable();
  }

  // second
  public PropertyAssertion secondPropertyLambda(String path) throws Exception {
    return secondLambda().property().hasPath(path);
  }

  public PropertyAssertion secondProperty() throws Exception {
    return this.parameters().second().property();
  }

  public LambdaAssertion secondLambda() throws Exception {
    return this.parameters().second().lambda();
  }

  public VariableReferenceAssertion secondVariable() throws Exception {
    return this.parameters().second().variable();
  }

  public FunctionAssertion parentFunction() {
    return this.parentOperand().parentFunction();
  }
}
