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
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.data.DataPropertyType;

public class StaticBoolAssertion
    extends ASTItemAssertionBase<ASTOperandStatic, ASTAssertionBase, VariableReferenceAssertion> {
  private boolean _value;

  public StaticBoolAssertion(ASTOperandStatic item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);

    shouldEquals(item.getDataType(), DataPropertyType.Boolean, "STATIC BOOLEAN");
    this._value = Boolean.parseBoolean(item.getValue());
  }

  public StaticBoolAssertion hasValue(boolean expected) {
    shouldEquals(this._value, expected, "STATIC BOOLEAN");
    return this;
  }

  public StaticBoolAssertion isTrue() {
    shouldEquals(this._value, true, "STATIC BOOLEAN");
    return this;
  }

  public StaticBoolAssertion isFalse() {
    shouldEquals(this._value, false, "STATIC BOOLEAN");
    return this;
  }
}
