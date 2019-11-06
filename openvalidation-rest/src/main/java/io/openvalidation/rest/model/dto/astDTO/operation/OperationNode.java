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

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperatorNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.NodeGenerator;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OperationNode extends ConditionNode {
  private OperandNode leftOperand;
  private OperandNode rightOperand;
  private OperatorNode operator;
  private boolean constrained;

  public OperationNode(ASTCondition conditionBase, DocumentSection section, TransformationParameter parameter) {
    super(section, conditionBase.getConnector(), parameter);

    List<String> foundAliases = new ArrayList<>();
    if (!conditionBase.isConstrainedCondition() && conditionBase.getOriginalSource() != null) {
      String mustKeyword = Constants.MUST_TOKEN;
      List<String> mustAliases = Aliases.getAliasByToken(parameter.getCulture(), mustKeyword);
      foundAliases =
          mustAliases.stream()
              .filter(
                  alias ->
                      conditionBase.getOriginalSource().toLowerCase().contains(alias.toLowerCase()))
              .collect(Collectors.toList());
    }
    this.constrained = conditionBase.isConstrainedCondition() || foundAliases.size() > 0;

    if (section == null || section.isEmpty()) return;

    if (conditionBase.getLeftOperand() != null) {
      DocumentSection leftSection =
          new RangeGenerator(section).generate(conditionBase.getLeftOperand());

      this.leftOperand =
          NodeGenerator.createOperand(conditionBase.getLeftOperand(), leftSection, parameter);
    }

    if (conditionBase.getRightOperand() != null) {
      DocumentSection rightSection =
          new RangeGenerator(section).generate(conditionBase.getRightOperand());

      this.rightOperand =
          NodeGenerator.createOperand(conditionBase.getRightOperand(), rightSection, parameter);
    }

    if (conditionBase.getOperator() != null) {
      if (this.rightOperand != null
          && this.rightOperand.getDataType() == DataPropertyType.Boolean
          && this.rightOperand.getName().equals("true")
          && conditionBase.getOperator() == ASTComparisonOperator.EQUALS) {
        this.operator = new OperatorNode(conditionBase, null, parameter);
      } else {
        String keyword =
            Constants.COMPOPERATOR_TOKEN + conditionBase.getOperator().name().toLowerCase();
        List<String> possibleAliases = Aliases.getAliasByToken(parameter.getCulture(), keyword);
        possibleAliases.sort(Comparator.comparingInt(String::length).reversed());
        List<String> operatorLines = section.getLines();
        DocumentSection operatorSection =
            this.generateValidOperator(
                new Range(section.getRange()), operatorLines, foundAliases, possibleAliases);
        if (operatorSection != null) {
          this.operator = new OperatorNode(conditionBase, operatorSection, parameter);
        }
      }
    }
  }

  private DocumentSection generateValidOperator(
      Range range,
      List<String> returnList,
      List<String> constrainedKeywords,
      List<String> possibleAliases) {
    String operator = String.join("", returnList);
    for (String alias : possibleAliases) {
      int index = operator.toLowerCase().indexOf(alias.toLowerCase());
      if (index != -1) {
        range.getStart().setColumn(range.getStart().getColumn() + index);
        range.getEnd().setColumn(range.getStart().getColumn() + alias.length());
        returnList.clear();
        returnList.add(operator.substring(index, index + alias.length()));
        break;
      }
    }

    return new DocumentSection(range, returnList, null).trimLine();
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

  public OperatorNode getOperator() {
    return operator;
  }

  public void setOperator(OperatorNode operator) {
    this.operator = operator;
  }

  public boolean isConstrained() {
    return constrained;
  }

  public void setConstrained(boolean constrained) {
    this.constrained = constrained;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof OperationNode) {
      return this.operator.equals(((OperationNode) obj).operator)
          && this.rightOperand.equals(((OperationNode) obj).rightOperand)
          && this.leftOperand.equals(((OperationNode) obj).leftOperand)
          && this.constrained == ((OperationNode) obj).constrained;
    }
    return false;
  }
}
