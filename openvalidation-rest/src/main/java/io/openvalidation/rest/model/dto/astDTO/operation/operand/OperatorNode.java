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

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.ArrayList;
import java.util.List;

public class OperatorNode extends GenericNode {
  private DataPropertyType dataType;
  private DataPropertyType validType;
  private ASTComparisonOperator operator;

  public OperatorNode(
      ASTCondition astCondition, DocumentSection section, TransformationParameter parameter) {
    super(section, parameter);

    if (astCondition != null) {
      this.operator = astCondition.getOperator();
      this.validType = astCondition.getOperator().validDataType();
      this.dataType = astCondition.getDataType();
    }
  }

  public DataPropertyType getDataType() {
    return dataType;
  }

  public void setDataType(DataPropertyType dataType) {
    this.dataType = dataType;
  }

  public ASTComparisonOperator getOperator() {
    return operator;
  }

  public void setOperator(ASTComparisonOperator operator) {
    this.operator = operator;
  }

  public DataPropertyType getValidType() {
    return validType;
  }

  public void setValidType(DataPropertyType validType) {
    this.validType = validType;
  }

  public List<String> getPotentialKeywords() {
    return new ArrayList<>();
  }
}
