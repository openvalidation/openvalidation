package io.openvalidation.rest.model.dto.astDTO.transformation;

import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
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
  public static GenericNode generateNode(
      ASTItem element, DocumentSection section, TransformationParameter parameter) {
    GenericNode node = null;

    if (element instanceof ASTRule) {
      node = new RuleNode((ASTRule) element, section, parameter);
    } else if (element instanceof ASTVariable) {
      node = new VariableNode((ASTVariable) element, section, parameter);
    } else if (element instanceof ASTComment) {
      node = new CommentNode((ASTComment) element, section, parameter);
    } else if (element instanceof ASTOperandBase) {
      OperandNode tmpOperand =
          NodeGenerator.createOperand((ASTOperandBase) element, section, parameter);
      node = new UnkownNode(tmpOperand, section);
    } else if (element instanceof ASTUnknown) {
      node = new UnkownNode(section);
    }

    return node;
  }

  public static ConditionNode createConditionNode(
      ASTConditionBase conditionBase, DocumentSection section, TransformationParameter parameter) {
    return NodeGenerator.createConditionNode(conditionBase, section, null, parameter);
  }

  public static ConditionNode createConditionNode(
      ASTConditionBase conditionBase,
      DocumentSection section,
      ASTItem outerSource,
      TransformationParameter parameter) {
    if (conditionBase instanceof ASTCondition) {
      ConditionNode returnNode =
          new OperationNode((ASTCondition) conditionBase, section, parameter);

      ConditionNode newNode =
          TransformationHelper.getOwnConditionElement(
              outerSource == null ? "" : outerSource.getOriginalSource(),
              ((ASTCondition) conditionBase),
              parameter.getCulture());
      if (newNode != null) {
        returnNode = new ConnectedOperationNode(section, parameter, returnNode, newNode);
      }

      return returnNode;
    }

    if (conditionBase instanceof ASTConditionGroup) {
      return new ConnectedOperationNode((ASTConditionGroup) conditionBase, section, parameter);
    }

    return null;
  }

  public static OperandNode createOperand(
      ASTOperandBase operandBase, DocumentSection section, TransformationParameter parameter) {
    if (operandBase instanceof ASTConditionBase) {
      return NodeGenerator.createConditionNode((ASTConditionBase) operandBase, section, parameter);
    } else if (operandBase instanceof ASTOperandFunction) {
      return new FunctionOperandNode((ASTOperandFunction) operandBase, section, parameter);
    } else if (operandBase instanceof ASTOperandArray) {
      return new ArrayOperandNode((ASTOperandArray) operandBase, section, parameter);
    }

    return new OperandNode(operandBase, section, parameter);
  }
}
