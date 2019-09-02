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

import io.openvalidation.common.ast.operand.property.ASTOperandProperty;

public class ASTPropertyBuilder
    extends ASTBuilderBase<ASTPropertyBuilder, ASTVariableBuilder, ASTOperandProperty> {

  public ASTPropertyBuilder() {
    super(null, ASTOperandProperty.class);
  }

  public ASTPropertyBuilder(ASTVariableBuilder prntBldr) {
    super(prntBldr, ASTOperandProperty.class);
  }

  public ASTPropertyBuilder appendAccessor(String part) {
    this.model.add(part);

    return this;
  }

  public ASTPropertyBuilder appendAccessors(String[] parts) {
    for (String part : parts) {
      this.model.add(part);
    }

    return this;
  }
}
