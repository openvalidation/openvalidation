package io.openvalidation.rest.model.dto.astDTO.transformation;

import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.element.CommentNode;
import io.openvalidation.rest.model.dto.astDTO.element.RuleNode;
import io.openvalidation.rest.model.dto.astDTO.element.UnkownNode;
import io.openvalidation.rest.model.dto.astDTO.element.VariableNode;
import io.openvalidation.rest.model.dto.astDTO.operation.ConditionNode;
import io.openvalidation.rest.model.dto.astDTO.operation.ConnectedOperationNode;
import io.openvalidation.rest.model.dto.astDTO.operation.OperationNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.ArrayOperandNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.FunctionOperandNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;

public class NodeGenerator {
  public static GenericNode generateNode(ASTItem element, DocumentSection section, String culture) {
    GenericNode node = null;

    if (element instanceof ASTRule) {
      node = new RuleNode((ASTRule) element, section, culture);
    } else if (element instanceof ASTVariable) {
      node = new VariableNode((ASTVariable) element, section, culture);
    } else if (element instanceof ASTComment) {
      node = new CommentNode((ASTComment) element, section);
    } else if (element instanceof ASTOperandBase) {
      OperandNode tmpOperand =
          NodeGenerator.createOperand((ASTOperandBase) element, section, culture);
      node = new UnkownNode(tmpOperand, section);
    } else if (element instanceof ASTUnknown) {
      node = new UnkownNode(section);
    }

    return node;
  }

  public static ConditionNode createConditionNode(
      ASTConditionBase conditionBase, DocumentSection section, String culture) {
    return NodeGenerator.createConditionNode(conditionBase, section, culture, null);
  }

  public static ConditionNode createConditionNode(
      ASTConditionBase conditionBase,
      DocumentSection section,
      String culture,
      ASTItem outerSource) {
    if (conditionBase instanceof ASTCondition) {
      ConditionNode returnNode = new OperationNode((ASTCondition) conditionBase, section, culture);

      ConditionNode newNode =
          TransformationHelper.getOwnConditionElement(
              outerSource == null ? "" : outerSource.getOriginalSource(),
              ((ASTCondition) conditionBase),
              culture);
      if (newNode != null) {
        returnNode = new ConnectedOperationNode(section, culture, returnNode, newNode);
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
      return NodeGenerator.createConditionNode((ASTConditionBase) operandBase, section, culture);
    } else if (operandBase instanceof ASTOperandFunction) {
      return new FunctionOperandNode((ASTOperandFunction) operandBase, section, culture);
    } else if (operandBase instanceof ASTOperandArray) {
      return new ArrayOperandNode((ASTOperandArray) operandBase, section, culture);
    }

    return new OperandNode(operandBase, section);
  }
}
