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

package io.openvalidation.common.ast.operand;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.StreamUtils;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ASTOperandBase extends ASTItem {

  private DataPropertyType _type = null;

  public List<ASTOperandProperty> getProperties() {
    return null;
  }

  public boolean isNumber() {
    return (this.getDataType() != null && (this.getDataType() == DataPropertyType.Decimal));
  }

  public boolean isBoolean() {
    return (this.getDataType() != null && (this.getDataType() == DataPropertyType.Boolean));
  }

  public void setDataType(DataPropertyType type) {
    this._type = type;
  }

  public DataPropertyType getDataType() {
    return this._type;
  }

  protected List<ASTOperandProperty> getPreconditionPropertiesFromOperand(ASTOperandBase operand) {
    List<ASTOperandProperty> properties = new ArrayList<>();

    if (operand != null && operand instanceof ASTOperandProperty)
      properties.addAll(((ASTOperandProperty) operand).getAllParentProperties());

    if (operand != null && operand instanceof ASTOperandVariable)
      properties.addAll(((ASTOperandVariable) operand).getAllParentProperties());

    if (operand != null && operand instanceof ASTConditionGroup)
      properties.addAll(((ASTConditionGroup) operand).getAllParentProperties());

    return properties;
  }

  protected List<ASTOperandProperty> sortPrecoditionProperties(
      List<ASTOperandProperty> properties) {
    return properties.stream()
        .filter(StreamUtils.distinctByKey(p -> p.getPathAsString()))
        .collect(Collectors.toList());
  }

  public boolean hasValue() {
    return !StringUtils.isNullOrEmpty(this.getPreprocessedSource());
  }

  public boolean isPropertyOrVariable() {
    return this instanceof ASTOperandProperty || this instanceof ASTOperandVariable;
  }

  public boolean isObjectData() {
    return this.getDataType() == DataPropertyType.Object;
  }

  public boolean isStringData() {
    return this.getDataType() == DataPropertyType.String;
  }

  public boolean isStaticString() {
    return this instanceof ASTOperandStaticString;
  }

  public boolean isEnumData() {
    return this.getDataType() == DataPropertyType.Enum;
  }

  public String getName() {
    if (this instanceof ASTOperandProperty) return ((ASTOperandProperty) this).getPathAsString();
    else if (this instanceof ASTOperandVariable)
      return ((ASTOperandVariable) this).getVariableName();

    return null;
  }

  public static String generateLambdaToken(Object instance) {
    return "x_" + System.identityHashCode(instance);
  }
}
