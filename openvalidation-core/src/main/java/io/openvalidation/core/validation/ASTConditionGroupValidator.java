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

import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.exceptions.ASTValidationException;

public class ASTConditionGroupValidator extends ValidatorBase {
  private ASTConditionGroup _conditionGroup;
  private int _level;

  public ASTConditionGroupValidator(ASTConditionGroup conditionGroup) {
    this._conditionGroup = conditionGroup;
  }

  public void setLevel(int level) {
    this._level = level;
  }

  @Override
  public void validate() throws Exception {

    if (_level > 0 && this._conditionGroup.getConnector() == null)
      throw new ASTValidationException(
          "missing AND/OR connector in combined condition.", _conditionGroup, this.globalPosition);

    if (this._conditionGroup.getConditions().size() == 1
        && this._conditionGroup.getConditions().get(0) instanceof ASTConditionGroup)
      throw new ASTValidationException(
          "condition group size is 1 and contains a single condition group. Unnecessary wrapping.",
          _conditionGroup,
          this.globalPosition);

    for (ASTConditionBase cond : this._conditionGroup.getConditions()) {
      validate(cond, this.globalPosition);
    }
  }
}
