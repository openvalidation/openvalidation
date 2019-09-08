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

package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.operation.NodeMapper;
import io.openvalidation.rest.model.dto.astDTO.operation.ConditionNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;

import java.util.List;

public class RuleNode extends GenericNode {
  private String errorMessage;
  private ConditionNode condition;

  public RuleNode(ASTRule rule, DocumentSection section, String culture) {
    super.initializeElement(section);

    ASTActionError actionError = (ASTActionError) rule.getAction();
    if (actionError != null) {
      this.errorMessage = actionError.getErrorMessage();
    }

    if (rule.getCondition() != null) {
      DocumentSection newSection = new RangeGenerator(section).generate(rule.getCondition());
      List<String> operators = Aliases.getSpecificAliasByToken(culture, Constants.OR_TOKEN, Constants.AND_TOKEN);

      int conditionIndex = rule.getOriginalSource().indexOf(rule.getCondition().getOriginalSource());
      String compareString = rule.getOriginalSource()
              .substring(conditionIndex + rule.getCondition().getOriginalSource().length()).trim();
      boolean isConnectedOperation = operators.stream().anyMatch(operator -> compareString.toUpperCase().startsWith(operator));
      this.condition = NodeMapper.createConditionNode(rule.getCondition(), newSection, isConnectedOperation);
    }
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ConditionNode getCondition() {
    return condition;
  }

  public void setCondition(ConditionNode condition) {
    this.condition = condition;
  }
}
