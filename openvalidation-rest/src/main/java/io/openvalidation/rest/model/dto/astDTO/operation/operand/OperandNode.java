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

package io.openvalidation.rest.model.dto.astDTO.operation.operand;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmetical;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.List;
import java.util.stream.Collectors;

public class OperandNode extends GenericNode {
  private DataPropertyType dataType;
  private String name;
  private boolean isStatic;

  public OperandNode(ASTOperandBase operand, DocumentSection section) {
    super(section);

    if (operand != null) {
      this.dataType = operand.getDataType();
      this.name = operand.getName();

      this.isStatic =
          operand instanceof ASTOperandStatic || operand instanceof ASTOperandArithmetical;
      if (operand instanceof ASTOperandStatic) {
        ASTOperandStatic operandStatic = (ASTOperandStatic) operand;
        if (operandStatic.isNumber()) {
          String tmpValue = ((ASTOperandStatic) operand).getValue();
          if (tmpValue.contains(".0") && !operandStatic.getOriginalSource().contains(".0")) {
            String[] splittedValue = tmpValue.split("\\.");
            this.name = splittedValue.length > 1 ? splittedValue[0] : tmpValue;
          } else {
            this.name = tmpValue;
          }
        } else {
          this.name = ((ASTOperandStatic) operand).getValue();
        }
      }

      if (operand instanceof ASTOperandArithmetical) this.name = operand.getOriginalSource();

      this.setRange(this.getRangeOnlyForOperand(section));
    }
  }

  public OperandNode(DataPropertyType dataType, DocumentSection section) {
    super(section);

    this.dataType = dataType;
    this.name = name;
  }

  public DataPropertyType getDataType() {
    return dataType;
  }

  public void setDataType(DataPropertyType dataType) {
    this.dataType = dataType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean getIsStatic() {
    return isStatic;
  }

  public void setIsStatic(boolean aStatic) {
    isStatic = aStatic;
  }

  private Range getRangeOnlyForOperand(DocumentSection section) {
    if (section == null) return null;

    List<String> lowerCaseLines =
        section.getLines().stream().map(String::toLowerCase).collect(Collectors.toList());
    DocumentSection lowerCaseSection = new DocumentSection(section.getRange(), lowerCaseLines);
    DocumentSection newSection =
        new RangeGenerator(lowerCaseSection).generate(this.getName().toLowerCase());

    if (newSection != null
        && newSection.getRange() != null
        && newSection.getRange().getStart() != null
        && newSection.getRange().getEnd() != null) {
      return newSection.getRange();
    } else {
      return this.getRange();
    }
  }
}
