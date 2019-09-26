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

import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.exceptions.ASTValidationException;

public class ASTRuleValidator extends ValidatorBase {
  private ASTRule _rule;

  public ASTRuleValidator(ASTRule rule) {
    this._rule = rule;
  }

  @Override
  public void validate() throws Exception {
    ASTConditionBase condition = this._rule.getCondition();

    if (condition == null)
      throw new ASTValidationException(
          "a Rule should contains at least one condition.", this._rule);

    if (this._rule.getAction() == null)
      throw new ASTValidationException("a Rule should contains an error message.", this._rule);

    if (this._rule.getAction() != null) validate(this._rule.getAction(), this._rule.getGlobalPosition());

    validate(condition, this._rule.getGlobalPosition());

    if (this._rule.getInvalidConditions() != null && this._rule.getInvalidConditions().size() > 0)
      throw new ASTValidationException(
          "missing AND/OR connector in combined condition.",
          this._rule.getInvalidConditions().get(0));
  }
}
