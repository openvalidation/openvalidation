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

import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.rest.model.dto.astDTO.operation.NodeMapper;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionOperandNode extends OperandNode {
  private List<OperandNode> parameters;

  public FunctionOperandNode(ASTOperandFunction operator, DocumentSection section, String culture) {
    super(operator, section);
    this.parameters =
        operator.getParameters().stream()
            .map(
                condition -> {
                  DocumentSection newSection = new RangeGenerator(section).generate(condition);
                  return NodeMapper.createOperand(condition, newSection, culture);
                })
            .collect(Collectors.toList());
  }

  public List<OperandNode> getParameters() {
    return parameters;
  }

  public void setParameters(List<OperandNode> parameters) {
    this.parameters = parameters;
  }
}
