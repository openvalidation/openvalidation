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
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import java.util.*;
import java.util.stream.Collectors;

public class ASTOperandFunction extends ASTOperandBase {

  private String name;
  protected List<ASTOperandBase> parameters = new ArrayList<>();

  protected Map<String, DataPropertyType> _returnTypes = new HashMap<>();

  public ASTOperandFunction() {
    super();
    _returnTypes.put("SUM_OF", DataPropertyType.Decimal);
  }

  public ASTOperandFunction(String name, ASTOperandBase... parameters) {
    this();
    setName(name);

    if (parameters != null) setParameters(Arrays.stream(parameters).collect(Collectors.toList()));
  }

  public ASTOperandFunction addParameter(ASTOperandBase parameter) {
    this.parameters.add(parameter);
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (_returnTypes.containsKey(name)) this.setDataType(_returnTypes.get(name));

    this.name = name;
  }

  public List<ASTOperandBase> getParameters() {
    return parameters;
  }

  public void setParameters(List<ASTOperandBase> parameters) {
    this.parameters = parameters;
  }

  @Override
  public DataPropertyType getDataType() {
    return (super.getDataType() == null) ? DataPropertyType.Object : super.getDataType();
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.addAll(getParameters());

    return items;
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    List<ASTOperandProperty> props = new ArrayList<>();

    for (ASTOperandBase param : this.parameters) {
      List<ASTOperandProperty> availableProperties = param.getProperties();
      if (availableProperties != null && availableProperties.size() > 0)
        props.addAll(availableProperties);
    }

    return props;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.parameters != null && this.parameters.size() > 0) {

      lst.addAll(
          this.parameters.stream()
              .flatMap(c -> c.collectItemsOfType(cls).stream())
              .collect(Collectors.toList()));
    }

    return lst;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + getType() + "\n");
    sb.append(this.space(level + 1) + "NAME: " + this.getName() + " \n");
    sb.append(this.space(level + 1) + "DataType: " + getDataType() + "\n");

    for (ASTOperandBase param : this.getParameters()) {
      sb.append(param.print(level + 1));
    }

    sb.append("\n");

    return sb.toString();
  }

  public void replaceFirstParameter(ASTOperandFunction newParam) {
    if (this.getParameters() == null) this.setParameters(new ArrayList<>());

    if (this.getParameters().size() > 0) {
      this.getParameters().remove(0);
      this.getParameters().add(0, newParam);
    } else this.addParameter(newParam);
  }
}
