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

public class ASTItemAssertionBase<
        T extends ASTItem, P extends ASTAssertionBase, S extends ASTAssertionBase>
    extends ASTAssertionBase<P> {
  protected T model;

  public ASTItemAssertionBase(T item, ASTModel ast, P parent) {
    super(item != null ? item.getClass().getSimpleName() : "UNKNOWN", ast, parent);
    this.model = item;
  }

  public ASTItemAssertionBase(T item, String name, ASTModel ast, P parent) {
    super(name, ast, parent);
    this.model = item;
  }

  public ASTItemAssertionBase(T item, String name, int index, ASTModel ast, P parent) {
    super(name, index, ast, parent);
    this.model = item;
  }

  public ASTItemAssertionBase(T item, int index, ASTModel ast, P parent) {
    super(item != null ? item.getClass().getSimpleName() : "UNKNOWN", index, ast, parent);
    this.model = item;
  }

  @Override
  protected ASTItemAssertionBase<T, P, S> shouldNotBeEmpty() {
    shouldNotBeNull(model, "");
    return this;
  }

  public S hasOriginalSource(String source) throws Exception {
    hasOriginalSource();
    shouldEquals(model.getOriginalSource(), source, "ORIGINAL SOURCE");

    return (S) this;
  }

  public S hasOriginalSource() throws Exception {
    shouldNotBeEmpty();

    if (model != null && model.getPreprocessedSource() != null)
      shouldNotBeEmpty(
          model.getPreprocessedSource(),
          "ORIGINAL SOURCE"); // TODO jgeske 09.05.19: model.getPreprocessedSource() should not be
    // empty
    else writeExpected("PREPROCESSED SOURCE should not be null");

    return (S) this;
  }

  public S hasPreprocessedSource(String source) throws Exception {
    hasPreprocessedSource();
    shouldEquals(model.getPreprocessedSource(), source, "PREPROCESSED SOURCE");

    return (S) this;
  }

  public S hasPreprocessedSource() throws Exception {
    shouldNotBeEmpty();

    if (model != null && model.getPreprocessedSource() != null)
      shouldNotBeEmpty(
          model.getPreprocessedSource(),
          "PREPROCESSED SOURCE"); // TODO jgeske 09.05.19: model.getPreprocessedSource() should not
    // be empty
    else writeExpected("PREPROCESSED SOURCE should not be null");

    return (S) this;
  }

  public OperandAssertion parentOperand() {
    shouldBeInstanceOf(this.parent(), OperandAssertion.class, "PARENT OPERAND");

    return (OperandAssertion) this.parent();
  }

  public ASTArithmeticalOperationAssertion parentOperation() {
    return parentArithmeticOperand().parentOperation();
  }

  public ConditionAssertion parentCondition() {
    return parentOperand().parentCondition();
  }

  public ModelRootAssertion parentModel() {
    return parentCondition().parentModel();
  }

  public ArithmeticOperandAssertion parentArithmeticOperand() {
    shouldBeInstanceOf(
        this.parent(), ArithmeticOperandAssertion.class, "PARENT ARITHMETIC OPERAND");

    return (ArithmeticOperandAssertion) this.parent();
  }

  public OperandArithmeticalAssertion parentArithmetic() {
    shouldBeInstanceOf(
        parentArithmeticOperand().parent(),
        OperandArithmeticalAssertion.class,
        "PARENT ARITHMETIC");

    return (OperandArithmeticalAssertion) parentArithmeticOperand().parent();
  }
}
