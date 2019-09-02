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

package io.openvalidation.common.ast.operand.lambda;

import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import java.util.List;

public class ASTOperandLambdaProperty extends ASTOperandLambdaExpression {

  public ASTOperandLambdaProperty() {
    super();
  }

  public ASTOperandLambdaProperty(ASTOperandProperty property) {
    this.setProperty(property);
    this.setLambdaToken(generateLambdaToken(this));
  }

  public void setProperty(ASTOperandProperty property) {
    this.setOperand(property);
  }

  public ASTOperandProperty getProperty() {
    return (this.getOperand() != null && this.getOperand() instanceof ASTOperandProperty)
        ? (ASTOperandProperty) this.getOperand()
        : null;
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    return (this.getProperty() != null)
        ? this.getProperty().getProperties()
        : super.getProperties();
  }

  @Override
  public DataPropertyType getDataType() {
    return DataPropertyType.Boolean;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();
    sb.append(super.print(level));

    if (this.getProperty() != null) sb.append(this.getProperty().print(level + 1));

    return sb.toString();
  }
}
