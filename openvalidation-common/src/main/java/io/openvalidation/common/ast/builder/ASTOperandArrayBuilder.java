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

import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;

public class ASTOperandArrayBuilder
    extends ASTBuilderBase<ASTOperandArrayBuilder, ASTConditionBuilder, ASTOperandArray> {
  public ASTOperandArrayBuilder(ASTConditionBuilder prntBldr) {
    super(prntBldr, ASTOperandArray.class);
  }

  public ASTOperandArrayBuilder addItem(ASTOperandBase item) {
    this.model.add(item);

    return this;
  }

  public ASTOperandArrayBuilder addItem(String string) {
    return this.addItem(new ASTOperandStaticString(string));
  }
}
