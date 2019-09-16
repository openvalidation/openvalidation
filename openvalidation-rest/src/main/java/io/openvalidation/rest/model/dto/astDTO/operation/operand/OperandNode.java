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

public class OperandNode extends GenericNode {
  private DataPropertyType dataType;
  private String name;

  public OperandNode(ASTOperandBase operand, DocumentSection section) {
    super.initializeElement(section);

    if (operand != null) {
      this.dataType = operand.getDataType();
      this.name = operand.getName();

      if (operand instanceof ASTOperandStatic) this.name = ((ASTOperandStatic) operand).getValue();

      if (operand instanceof ASTOperandArithmetical) this.name = operand.getOriginalSource();

      this.setRange(this.getRangeOnlyForOperand(section));
    }
  }

  private Range getRangeOnlyForOperand(DocumentSection section) {
    DocumentSection newSection = new RangeGenerator(section).generate(this.getName());
    if (newSection != null
        && newSection.getRange() != null
        && newSection.getRange().getStart() != null
        && newSection.getRange().getEnd() != null) {
      return newSection.getRange();
    } else {
      return this.getRange();
    }
  }

  public OperandNode(DocumentSection section, String name, DataPropertyType dataType) {
    super.initializeElement(section);

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
}
