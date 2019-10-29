package io.openvalidation.rest.model.dto.astDTO.transformation;

import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.element.CommentNode;
import io.openvalidation.rest.model.dto.astDTO.element.RuleNode;
import io.openvalidation.rest.model.dto.astDTO.element.UnkownNode;
import io.openvalidation.rest.model.dto.astDTO.element.VariableNode;
import io.openvalidation.rest.model.dto.astDTO.operation.NodeMapper;
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
      OperandNode tmpOperand = NodeMapper.createOperand((ASTOperandBase) element, section, culture);
      node = new UnkownNode(tmpOperand, section);
    } else if (element instanceof ASTUnknown) {
      node = new UnkownNode(section);
    }

    return node;
  }
}
