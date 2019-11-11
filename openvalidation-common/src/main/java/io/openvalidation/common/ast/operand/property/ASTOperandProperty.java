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

package io.openvalidation.common.ast.operand.property;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ASTOperandProperty extends ASTOperandBase {
  protected List<ASTPropertyPart> path = new ArrayList<>();

  protected String lambdaToken;
  private DataPropertyType _arrayContentType;

  public ASTOperandProperty() {
    super();
  }

  public ASTOperandProperty(String... parts) {
    this();

    this.add(parts);
  }

  public ASTOperandProperty(ASTOperandProperty property) {
    this();

    if (property != null) {
      this.setPath(property.getPath());
      this.setLambdaToken(property.getLambdaToken());
      this.setDataType(property.getDataType());
    }
  }

  public String getLambdaToken() {
    return this.lambdaToken;
  }

  public void setLambdaToken(String token) {
    this.lambdaToken = token;
  }

  public DataPropertyType getArrayContentType() {
    return _arrayContentType;
  }

  public void setArrayContentType(DataPropertyType _arrayContentType) {
    this._arrayContentType = _arrayContentType;
  }

  public List<ASTPropertyPart> getPath() {
    return path;
  }

  public void setPath(List<ASTPropertyPart> path) {
    this.path = path;
  }

  public String getPathAsString() {
    if (this.path.size() > 0) {
      return String.join(
          ".", this.getPath().stream().map(p -> p.getPart()).collect(Collectors.toList()));
    }

    return "";
  }

  public ASTOperandProperty add(ASTPropertyPart part) {
    this.path.add(part);

    return this;
  }

  public ASTOperandProperty add(String... parts) {

    if (parts != null) {
      for (String p : parts) {
        ASTPropertyStaticPart propPart = new ASTPropertyStaticPart(p);
        propPart.setSource(p);
        this.path.add(propPart);
      }
    }

    return this;
  }

  public List<ASTOperandProperty> getAllParentProperties() {
    List<ASTOperandProperty> parents = new ArrayList<>();

    if (this.path.size() > 1) {
      ASTOperandProperty parent = getParentProperty();

      while (parent != null) {
        parents.add(new ASTOperandProperty(parent));
        parent = parent.getParentProperty();
      }
    }

    // shortest path first
    parents.sort(Comparator.comparing(c -> ((ASTOperandProperty) c).getPath().size()));

    return parents;
  }

  public ASTOperandProperty getParentProperty() {
    ASTOperandProperty prop = null;

    if (this.path != null && this.path.size() > 1) {
      int size = this.path.size();
      prop = new ASTOperandProperty();

      this.path.stream().limit(size - 1).forEach(prop::add);
    }

    return prop;
  }

  public String[] getPathAsArray() {
    if (this.getPath() != null)
      return this.getPath().stream()
          .map(p -> p.getPart().toString())
          .collect(Collectors.toList())
          .toArray(new String[0]);

    return null;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.addAll(getPath());

    return items;
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    List<ASTOperandProperty> props = new ArrayList<>();

    props.add(this);

    return props;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.path != null && this.path.size() > 1) {
      this.path.stream()
          .flatMap(c -> c.collectItemsOfType(cls).stream())
          .collect(Collectors.toList());
    }

    return lst;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + getType() + " : " + this.getPathAsString() + "\n");
    sb.append(this.space(level + 1) + "DataType : " + this.getDataType() + "\n");

    if (!StringUtils.isNullOrEmpty(this.getLambdaToken()))
      sb.append(this.space(level + 1) + "Lambda : " + this.getLambdaToken() + "\n");

    if (this.getPath() != null && this.getPath().size() > 0) {
      for (ASTPropertyPart part : this.getPath()) {
        if (part != null) sb.append(part.print(level + 2));
      }
    }

    return sb.toString();
  }
}
