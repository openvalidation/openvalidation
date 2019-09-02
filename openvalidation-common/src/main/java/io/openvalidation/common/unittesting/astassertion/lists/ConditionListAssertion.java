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
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.unittesting.astassertion.*;
import java.util.List;

public class ConditionListAssertion
    extends ASTListAssertionBase<ASTConditionBase, ConditionAssertionBase, ASTAssertionBase> {

  public ConditionListAssertion(
      List<ASTConditionBase> list, ASTModel ast, ASTAssertionBase parent) {
    super("CONDITIONS", ast, parent);
    fillList(
        list,
        c -> {
          return (c instanceof ASTCondition)
              ? new ConditionAssertion((ASTCondition) c, list.indexOf(c), ast, this)
              : new ConditionGroupAssertion((ASTConditionGroup) c, list.indexOf(c), ast, this);
        });
  }
}
