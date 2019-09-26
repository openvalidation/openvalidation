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
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.rest.model.dto.astDTO.operation.NodeMapper;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.ArrayList;
import java.util.List;

public class FunctionOperandNode extends OperandNode {
  private List<OperandNode> parameters;
  private DataPropertyType acceptedType;

  public FunctionOperandNode(ASTOperandFunction operand, DocumentSection section, String culture) {
    super(operand, section);
    this.parameters = new ArrayList<>();
    //this.returnType = operand.

    for (ASTOperandBase parameter : operand.getParameters()) {
      if (parameter == null) continue;

      DocumentSection newSection = new RangeGenerator(section).generate(parameter);
      this.parameters.add(NodeMapper.createOperand(parameter, newSection, culture));
    }

    this.acceptedType = this.parameters.size() > 0
            ? this.parameters.get(0).getDataType()
            : DataPropertyType.Object;
  }

  public List<OperandNode> getParameters() {
    return parameters;
  }

  public void setParameters(List<OperandNode> parameters) {
    this.parameters = parameters;
  }

  public DataPropertyType getAcceptedType() {
    return acceptedType;
  }

  public void setAcceptedType(DataPropertyType returnType) {
    this.acceptedType = returnType;
  }
}
