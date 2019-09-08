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

package io.openvalidation.rest.model.dto.astDTO.operation;

import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectedOperationNode extends ConditionNode {
  private List<ConditionNode> conditions;

  public ConnectedOperationNode(ConditionNode condition, DocumentSection section) {
      super(section);
      this.conditions = Collections.singletonList(condition);
  }

  public ConnectedOperationNode(ASTConditionGroup conditionBase, DocumentSection section) {
    super(section);

    this.conditions =
        conditionBase.getAllConditions().stream()
            .map(
                condition -> {
                  DocumentSection newSection = new RangeGenerator(section).generate(condition);
                  return NodeMapper.createConditionNode(condition, newSection);
                })
            .collect(Collectors.toList());
  }

  public List<ConditionNode> getConditions() {
    return conditions;
  }

  public void setConditions(List<ConditionNode> conditions) {
    this.conditions = conditions;
  }
}
