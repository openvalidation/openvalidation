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

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.rest.model.dto.astDTO.TransformationHelper;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.ArrayOperandNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.FunctionOperandNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;

public class NodeMapper {
  public static ConditionNode createConditionNode(
      ASTConditionBase conditionBase, DocumentSection section, String culture) {
    return NodeMapper.createConditionNode(conditionBase, section, null, culture);
  }

  public static ConditionNode createConditionNode(
      ASTConditionBase conditionBase,
      DocumentSection section,
      String outerSource,
      String culture) {
    if (conditionBase instanceof ASTCondition) {
      ConditionNode returnNode = new OperationNode((ASTCondition) conditionBase, section, culture);

      ConditionNode newNode =
              TransformationHelper.getOwnConditionElement(outerSource, ((ASTCondition) conditionBase), culture);
      if (newNode != null) {
        returnNode = new ConnectedOperationNode(section, returnNode, newNode);
      }

      return returnNode;
    }

    if (conditionBase instanceof ASTConditionGroup) {
      return new ConnectedOperationNode((ASTConditionGroup) conditionBase, section, culture);
    }

    return null;
  }

  public static OperandNode createOperand(
      ASTOperandBase operandBase, DocumentSection section, String culture) {
    if (operandBase instanceof ASTConditionBase) {
      return NodeMapper.createConditionNode((ASTConditionBase) operandBase, section, culture);
    } else if (operandBase instanceof ASTOperandFunction) {
      return new FunctionOperandNode((ASTOperandFunction) operandBase, section, culture);
    } else if (operandBase instanceof ASTOperandArray) {
      return new ArrayOperandNode((ASTOperandArray) operandBase, section, culture);
    }

    return new OperandNode(operandBase, section);
  }
}
