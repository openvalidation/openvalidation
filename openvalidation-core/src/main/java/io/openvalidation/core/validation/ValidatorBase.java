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

package io.openvalidation.core.validation;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;

public abstract class ValidatorBase {
  protected ASTModel ast;
  protected ValidationContext context;

  public ValidatorBase() {}

  public ValidatorBase(ValidationContext context) {
    this.setContext(context);
  }

  public void setContext(ValidationContext context) {
    this.context = context;
    this.ast = context.getAst();
  }

  public abstract void validate() throws Exception;

  protected void validate(ASTItem item) throws Exception {
    ValidatorBase validator = ValidatorFactory.Create(item);
    validator.setContext(context);
    validator.validate();
  }
}
