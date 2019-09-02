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

package io.openvalidation.common.ast.condition;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import java.util.List;

public abstract class ASTConditionBase extends ASTOperandBase {
  private ASTConditionConnector connector;
  private String modelToken = "model";

  public ASTConditionConnector getConnector() {
    return connector;
  }

  public void setConnector(ASTConditionConnector connector) {
    this.connector = connector;
  }

  public String getModelToken() {
    return modelToken;
  }

  public void setModelToken(String modelToken) {
    this.modelToken = modelToken;
  }

  public abstract List<ASTOperandProperty> getAllParentProperties();

  public abstract void invertOperator();

  @Override
  public DataPropertyType getDataType() {
    return DataPropertyType.Boolean;
  }
}
