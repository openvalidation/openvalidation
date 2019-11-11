package io.openvalidation.rest.model.dto.astDTO.operation.operand;

import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.NodeGenerator;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayOperandNode extends OperandNode {
  private List<OperandNode> items;

  public ArrayOperandNode(
      ASTOperandArray operand, DocumentSection section, TransformationParameter parameter) {
    super(operand, section, parameter);

    if (operand.getItems().size() > 0) {
      this.items =
          operand.getItems().stream()
              .map(
                  item -> {
                    DocumentSection newSection = new RangeGenerator(section).generate(item);
                    return NodeGenerator.createOperand(item, newSection, parameter);
                  })
              .collect(Collectors.toList());

      this.setDataType(operand.getItems().get(0).getDataType());
    }
  }

  public List<OperandNode> getItems() {
    return items;
  }
}
