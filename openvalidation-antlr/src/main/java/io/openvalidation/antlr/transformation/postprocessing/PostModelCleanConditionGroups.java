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

package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import java.util.function.Predicate;

public class PostModelCleanConditionGroups extends PostProcessorSubelementBase<ASTModel, ASTRule> {
  @Override
  protected Predicate<? super ASTRule> getFilter() {
    return r ->
        r.getCondition() instanceof ASTConditionGroup
            && ((ASTConditionGroup) r.getCondition()).getConditions().size() == 1;
  }

  @Override
  protected void processItem(ASTRule rule) {
    ASTCondition cond =
        (ASTCondition) ((ASTConditionGroup) rule.getCondition()).getConditions().get(0);
    rule.setCondition(cond);
  }
}
