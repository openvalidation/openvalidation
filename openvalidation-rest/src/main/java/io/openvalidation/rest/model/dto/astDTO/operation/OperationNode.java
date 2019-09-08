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
import io.openvalidation.rest.model.dto.astDTO.Position;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.Operator;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.ArrayList;
import java.util.List;

public class OperationNode extends ConditionNode {
  private OperandNode leftOperand;
  private OperandNode rightOperand;
  private Operator operator;

  public OperationNode(ASTCondition conditionBase, DocumentSection section) {
    super(section);

    List<String> leftLines = new ArrayList<>();
    List<String> rightLines = new ArrayList<>();

    if (conditionBase.getLeftOperand() != null) {
      DocumentSection leftSection =
          new RangeGenerator(section).generate(conditionBase.getLeftOperand());
      if (leftSection != null) {
        leftLines = leftSection.getLines();
      }

      this.leftOperand = NodeMapper.createOperand(conditionBase.getLeftOperand(), leftSection);
    }

    if (conditionBase.getRightOperand() != null) {
      DocumentSection rightSection =
          new RangeGenerator(section).generate(conditionBase.getRightOperand());
      if (rightSection != null) {
        rightLines = rightSection.getLines();
      }

      this.rightOperand = NodeMapper.createOperand(conditionBase.getRightOperand(), rightSection);
    }

    if (conditionBase.getOperator() != null) {
      List<String> operatorLines = section.getLines();
      DocumentSection operatorSection =
          this.generateOperatorString(operatorLines, leftLines, rightLines, section.getRange());
      if (operatorSection != null) {
        this.operator = new Operator(conditionBase, operatorSection);
      }
    }
  }

  public DocumentSection generateOperatorString(
      List<String> outerString, List<String> leftLines, List<String> rightLines, Range outerRange) {
    ArrayList<String> returnList = new ArrayList<>();

    Position start = new Position(outerRange.getStart());
    Position end = new Position(outerRange.getEnd());

    for (String outerLine : outerString) {
      String tmpString = outerLine;

      for (String innerLine : leftLines) {
        if (!tmpString.contains(innerLine)) continue;

        int startIndex = tmpString.indexOf(innerLine);
        tmpString = tmpString.substring(startIndex + innerLine.length());

        start.setColumn(start.getColumn() + startIndex + innerLine.length());
      }

      for (String innerLine : rightLines) {
        if (!tmpString.contains(innerLine) || innerLine.isEmpty()) continue;

        int startIndex = tmpString.indexOf(innerLine);
        String cuttedString = tmpString.substring(startIndex, tmpString.length());
        tmpString = tmpString.substring(0, startIndex);

        end.setColumn(end.getColumn() - cuttedString.length());
      }

      if (!tmpString.trim().isEmpty()) {
        returnList.add(tmpString);
      } else {
        end.setLine(end.getLine() - 1);

        // TODO: Strange Position when Operator spans over multiple lines
        //                String lastLine = returnList.get(returnList.size() - 1);
        //                end.setColumn(lastLine.length() - 1);
      }
    }

    return new DocumentSection(new Range(start, end), returnList).trimLine();
  }

  public OperandNode getLeftOperand() {
    return leftOperand;
  }

  public void setLeftOperand(OperandNode leftOperand) {
    this.leftOperand = leftOperand;
  }

  public OperandNode getRightOperand() {
    return rightOperand;
  }

  public void setRightOperand(OperandNode rightOperand) {
    this.rightOperand = rightOperand;
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }
}
