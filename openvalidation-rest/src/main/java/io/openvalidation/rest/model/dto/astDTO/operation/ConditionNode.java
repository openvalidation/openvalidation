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

import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.List;

public abstract class ConditionNode extends OperandNode {
  private String connector;

  public ConditionNode(
      DocumentSection section, ASTConditionConnector connector, TransformationParameter parameter) {
    this(section, connector != null ? connector.name() : null, parameter);
  }

  public ConditionNode(
      DocumentSection section, String connector, TransformationParameter parameter) {
    super(DataPropertyType.Boolean, section, parameter);

    if (connector != null) {
      String connectorToken =
          Constants.KEYWORD_SYMBOL + connector.toLowerCase() + Constants.KEYWORD_SYMBOL;
      List<String> connectorAlias = Aliases.getAliasByToken(parameter.getCulture(), connectorToken);
      this.connector = connectorAlias.size() > 0 ? connectorAlias.get(0) : connector;
    } else {
      this.connector = null;
    }
  }

  public String getConnector() {
    return connector;
  }

  public void setConnector(String connector) {
    this.connector = connector;
  }
}
