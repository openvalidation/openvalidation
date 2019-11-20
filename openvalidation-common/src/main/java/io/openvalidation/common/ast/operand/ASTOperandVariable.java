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
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.ast.operand.property.ASTPropertyPart;
import io.openvalidation.common.data.DataPropertyType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTOperandVariable extends ASTOperandBase {

  private String variableName;
  private ASTVariable _variable;

  private List<ASTPropertyPart> path = new ArrayList<>();

  public ASTOperandVariable(String varname) {
    this.setVariableName(varname);
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = (variableName != null) ? variableName.trim() : null;
  }

  public String getCodeSafeName() {
    return (_variable != null) ? _variable.getCodeSafeName() : variableName;
  }

  public ASTVariable getVariable() {
    return _variable;
  }

  public void setVariable(ASTVariable _variable) {
    this._variable = _variable;
  }

  public void add(ASTPropertyPart part) {
    this.path.add(part);
  }

  public boolean isVariableUnresolved() {
    return (this.getVariableName() != null
        && this.getVariableName().trim().length() > 0
        && this.getVariable() == null);
  }

  public List<ASTOperandProperty> getAllParentProperties() {
    List<ASTOperandProperty> properties = new ArrayList<>();

    if (this._variable != null && this._variable.getValue() != null)
      properties.addAll(this.getPreconditionPropertiesFromOperand(this._variable.getValue()));

    return sortPrecoditionProperties(properties);
  }

  public List<ASTPropertyPart> getPath() {
    return path;
  }

  public void setPath(List<ASTPropertyPart> path) {
    this.path = path;
  }

  public String[] getPathAsArray() {
    if (this.getPath() != null)
      return this.getPath().stream()
          .map(p -> p.getPart().toString())
          .collect(Collectors.toList())
          .toArray(new String[0]);

    return null;
  }

  public String getPathAsString() {
    if (this.path.size() > 0) {
      return String.join(
          ".", this.getPath().stream().map(p -> p.getPart()).collect(Collectors.toList()));
    }

    return "";
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.addAll(getPath());
    items.add(getVariable());

    return items;
  }

  @Override
  public DataPropertyType getDataType() {
    return (this._variable != null) ? this._variable.getDataType() : super.getDataType();
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + this.getType() + " : " + this.getVariableName() + "\n");
    sb.append(this.space(level) + "DataType : " + this.getDataType() + "\n");

    if (this.getPath() != null && this.getPath().size() > 0) {
      for (ASTPropertyPart part : this.getPath()) {
        if (part != null) sb.append(part.print(level + 1));
      }
    }

    return sb.toString();
  }

  public DataPropertyType getArrayContentType() {
    DataPropertyType arrayContentType = null;

    if (this.getDataType() == DataPropertyType.Array) {
      ASTOperandBase content = _variable.getValue();
      if (content instanceof ASTOperandVariable) {
        arrayContentType = ((ASTOperandVariable) content).getArrayContentType();
      } else if (content instanceof ASTOperandProperty) {
        arrayContentType = ((ASTOperandProperty) content).getArrayContentType();
      } else if (content instanceof ASTOperandFunction) {
        arrayContentType = ((ASTOperandFunction) content).getArrayContentType();
      }
    }

    return arrayContentType;
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    return (this.isVariableUnresolved())
        ? super.getProperties()
        : this.getVariable().getProperties();
  }
}
