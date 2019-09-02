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

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionGroup;

public class RuleAssertion extends ASTItemAssertionBase<ASTRule, ASTAssertionBase, RuleAssertion> {

  public RuleAssertion(ASTRule item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);
  }

  public RuleAssertion shouldNotBeEmpty() {
    super.shouldNotBeEmpty();
    shouldNotBeEmpty(model.getCondition(), "CONDITION");
    return this;
  }

  public ConditionAssertion condition() {
    this.shouldNotBeEmpty();
    shouldBeInstanceOf(model.getCondition(), ASTCondition.class, "CONDITION");
    return new ConditionAssertion((ASTCondition) model.getCondition(), ast, this);
  }

  public ConditionGroupAssertion conditionGroup() {
    this.shouldNotBeEmpty();
    shouldBeInstanceOf(model.getCondition(), ASTConditionGroup.class, "CONDITION");
    return new ConditionGroupAssertion((ASTConditionGroup) model.getCondition(), ast, this);
  }

  public RuleAssertion hasError(String error) {
    this.shouldNotBeEmpty();
    this.shouldBeInstanceOf(this.model.getAction(), ASTActionError.class, "ERROR");

    this.shouldNotBeNull(((ASTActionError) this.model.getAction()).getErrorMessage(), "ERROR");
    this.shouldEquals(
        ((ASTActionError) this.model.getAction()).getErrorMessage().toLowerCase(),
        error.toLowerCase(),
        "ERROR");

    return this;
  }

  public RuleAssertion hasError() {
    this.shouldNotBeEmpty();
    this.shouldBeInstanceOf(this.model.getAction(), ASTActionError.class, "ERROR");

    return this;
  }

  public RuleAssertion hasNoError() {
    shouldNotBeNull(this.model, "RULE");
    shouldBeNull(this.model.getAction(), "ERROR");

    return this;
  }

  public ErrorAssertion error() {
    hasError();

    return new ErrorAssertion((ASTActionError) this.model.getAction(), ast, this);
  }

  public RuleAssertion hasErrorCode(Integer code) {
    this.shouldNotBeEmpty();
    this.shouldBeInstanceOf(this.model.getAction(), ASTActionError.class, "ERROR");

    ASTActionError error = ((ASTActionError) this.model.getAction());

    shouldNotBeNull(error, "ERROR");
    shouldEquals(error.getErrorCode(), code, "ERRORCODE");

    return this;
  }
}
