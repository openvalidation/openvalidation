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

package io.openvalidation.common.exceptions;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import java.util.ArrayList;
import java.util.List;

public class ASTValidationSummaryException extends OpenValidationException {
  private List<ASTValidationException> _exceptions = new ArrayList<>();
  private ASTModel model;

  public ASTValidationSummaryException() {
    super("");
  }

  public ASTValidationSummaryException(ASTModel model) {
    super("");
    this.model = model;
  }

  public void add(String message, ASTItem item) {
    add(new ASTValidationException(message, item));
  }

  public void add(ASTValidationException exception) {
    this._exceptions.add(exception);
  }

  public List<ASTValidationException> getErrors() {
    return _exceptions;
  }

  public boolean hasErrors() {
    return _exceptions.size() > 0;
  }

  public ASTModel getModel() {
    return model;
  }

  public void setModel(ASTModel model) {
    this.model = model;
  }
}
