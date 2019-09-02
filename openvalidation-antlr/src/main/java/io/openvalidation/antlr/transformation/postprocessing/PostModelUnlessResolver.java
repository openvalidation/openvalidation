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
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.utils.LINQ;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PostModelUnlessResolver
    extends PostProcessorSubelementBase<ASTModel, ASTConditionGroup> {

  @Override
  protected Predicate<? super ASTConditionGroup> getFilter() {
    return g ->
        g.getConditions().stream().anyMatch(c -> c.getConnector() == ASTConditionConnector.UNLESS);
  }

  @Override
  protected void processItem(ASTConditionGroup group) {
    ASTConditionBase unlessCondition =
        LINQ.findFirst(
            group.getConditions(), c -> c.getConnector() == ASTConditionConnector.UNLESS);

    if (unlessCondition != null) {
      unlessCondition.setConnector(null);
      group.getConditions().remove(unlessCondition);

      List<ASTConditionBase> subconditions = new ArrayList<>();
      group.getConditions().forEach(c -> subconditions.add(c));
      group.getConditions().clear();

      // add unless-condition at start
      unlessCondition.invertOperator();
      group.addCondition(unlessCondition);

      // add existing out conditions as subcondition group
      ASTConditionGroup newSubGroup = new ASTConditionGroup();
      newSubGroup.setConditions(subconditions);
      newSubGroup.setConnector(ASTConditionConnector.AND);

      group.addCondition(newSubGroup);
    }
  }
}
