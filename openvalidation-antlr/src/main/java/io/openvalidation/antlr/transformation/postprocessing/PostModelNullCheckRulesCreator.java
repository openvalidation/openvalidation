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

import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.utils.StreamUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PostModelNullCheckRulesCreator
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  @Override
  public void process(ASTItem model, PostProcessorContext context) {
    this.setContext(context);

    List<ASTOperandProperty> properties =
        model.collectItemsOfType(ASTCondition.class).stream()
            .flatMap(c -> c.getAllParentProperties().stream())
            .filter(StreamUtils.distinctByKey(p -> p.getPathAsString()))
            .collect(Collectors.toList());

    if (properties != null && properties.size() > 0) {
      List<ASTRule> nullCheckRules = new ArrayList<>();

      properties.forEach(
          p -> {
            ASTRule rule = new ASTRule();

            ASTCondition condition = new ASTCondition();
            condition.setLeftOperand(p);
            condition.setOperator(ASTComparisonOperator.EMPTY);
            rule.setCondition(condition);
            rule.setAction(
                new ASTActionError(
                    "field: '" + p.getPathAsString() + "' should not be null or empty"));

            nullCheckRules.add(rule);
          });

      ((ASTModel) model).setNullCheckRules(nullCheckRules);
    }
  }

  @Override
  protected Predicate<? super ASTCondition> getFilter() {
    return null;
  }

  @Override
  protected void processItem(ASTCondition item) {}
}
