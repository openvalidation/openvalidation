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

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.core.OpenValidationOptions;

public class ValidationContext {

  private OpenValidationOptions _options;
  private ASTModel _ast;
  private String _plainRule;
  private String _preprocessedRule;

  public ValidationContext(
      OpenValidationOptions options, ASTModel ast, String plainRule, String preprocessedRule) {
    this._options = options;
    this._ast = ast;
    this._plainRule = plainRule;
    this._preprocessedRule = preprocessedRule;
  }

  public OpenValidationOptions getOptions() {
    return _options;
  }

  public ASTModel getAst() {
    return _ast;
  }

  public String getPlainRule() {
    return _plainRule;
  }

  public String getPreprocessedRule() {
    return _preprocessedRule;
  }
}
