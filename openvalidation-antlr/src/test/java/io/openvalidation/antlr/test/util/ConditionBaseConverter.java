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

package io.openvalidation.antlr.test.util;

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import java.util.List;

public class ConditionBaseConverter {
  public static String toBracedExpressionString(ASTConditionBase base) {
    if (base instanceof ASTCondition) {
      return toBracedExpressionC((ASTCondition) base);
    } else {
      return toBracedExpressionG((ASTConditionGroup) base);
    }
  }

  private static String toBracedExpressionG(ASTConditionGroup group) {
    StringBuilder sb = new StringBuilder();

    if (group.getConnector() != null) {
      sb.append(connectorToString(group.getConnector())).append(" ");
    }

    sb.append("( ");

    List<ASTConditionBase> conditions = group.getConditions();

    for (int i = 0; i < conditions.size(); i++) {
      sb.append(toBracedExpressionString(conditions.get(i)));

      if (i != conditions.size() - 1) {
        sb.append(" ");
      }
    }

    sb.append(" )");

    return sb.toString();
  }

  private static String toBracedExpressionC(ASTCondition condition) {
    StringBuilder sb = new StringBuilder();

    if (condition.getConnector() != null) {
      sb.append(connectorToString(condition.getConnector())).append(" ");
    }

    sb.append(condition.getLeftOperand());

    return sb.toString();
  }

  private static String connectorToString(ASTConditionConnector connector) {
    if (connector.equals(ASTConditionConnector.AND)) {
      return "&&";
    } else if (connector.equals(ASTConditionConnector.OR)) {
      return "||";
    } else {
      return "FAULTY OPERATOR";
    }
  }
}
