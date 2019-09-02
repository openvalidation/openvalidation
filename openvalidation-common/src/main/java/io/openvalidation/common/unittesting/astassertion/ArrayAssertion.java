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
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
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
}
