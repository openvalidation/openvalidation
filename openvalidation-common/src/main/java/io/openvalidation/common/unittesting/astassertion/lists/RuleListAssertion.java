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

package io.openvalidation.common.unittesting.astassertion.lists;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.unittesting.astassertion.*;
import java.util.List;

public class RuleListAssertion
    extends ASTListAssertionBase<ASTRule, RuleAssertion, ASTAssertionBase> {
  public RuleListAssertion(List<ASTRule> list, ASTModel ast, ASTAssertionBase parent) {
    super("RULES", ast, parent);
    fillList(list, c -> new RuleAssertion(c, list.indexOf(c), ast, this));
  }

  @Override
  public RuleListAssertion hasSizeOf(int size) {
    return (RuleListAssertion) super.hasSizeOf(size);
  }

  public ConditionAssertion firstCondition() {
    return first().condition();
  }

  public ConditionGroupAssertion firstConditionGroup() {
    return first().conditionGroup();
  }
}
