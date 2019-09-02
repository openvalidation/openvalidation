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

package io.openvalidation.common.ast;

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.utils.LINQ;
import java.util.*;
import java.util.stream.Collectors;

public class ASTModel extends ASTItem {

  private Map<String, Object> _params = new HashMap<>();
  private List<ASTGlobalElement> _elements = new ArrayList<>();
  private List<ASTRule> _nullCheckRules = null;

  public List<ASTGlobalElement> getElements() {
    return _elements;
  }

  public boolean hasElements() {
    return (this.getElements() == null || this.getElements().size() < 1);
  }

  public boolean hasValidElements() {
    return this.getValidElements().size() > 0;
  }

  public List<ASTGlobalElement> getValidElements() {
    return this._elements.stream()
        .filter(s -> !(s instanceof ASTUnknown))
        .collect(Collectors.toList());
  }

  public List<ASTUnknown> getUnknownElements() {
    return this._elements.stream()
        .filter(s -> s instanceof ASTUnknown)
        .map(s -> (ASTUnknown) s)
        .collect(Collectors.toList());
  }

  public void add(ASTGlobalElement element) {
    this._elements.add(element);
  }

  public List<ASTComment> getComments() {
    return this._elements.stream()
        .filter(s -> s instanceof ASTComment)
        .map(s -> (ASTComment) s)
        .collect(Collectors.toList());
  }

  public List<ASTGlobalNamedElement> getNamedElements() {
    return this._elements.stream()
        .filter(s -> s instanceof ASTGlobalNamedElement)
        .map(s -> (ASTGlobalNamedElement) s)
        .collect(Collectors.toList());
  }

  public List<ASTVariable> getVariables() {
    return this._elements.stream()
        .filter(s -> s instanceof ASTVariable)
        .map(s -> (ASTVariable) s)
        .collect(Collectors.toList());
  }

  public List<ASTRule> getRules() {
    return this._elements.stream()
        .filter(s -> s instanceof ASTRule)
        .map(s -> (ASTRule) s)
        .collect(Collectors.toList());
  }

  public List<ASTRule> getNullCheckRules() {
    return _nullCheckRules;
  }

  public void setNullCheckRules(List<ASTRule> rules) {
    this._nullCheckRules = rules;
  }

  public List<ASTGlobalElement> getRulesAndComments() {
    return this._elements.stream()
        .filter(s -> s instanceof ASTRule || s instanceof ASTComment)
        .collect(Collectors.toList());
  }

  public ASTComment addComment(String cmt) {
    ASTComment comment = new ASTComment(cmt);
    this._elements.add(comment);

    return comment;
  }

  public Map<String, Object> getParams() {
    return this._params;
  }

  public ASTModel addParam(String name, Object value) {
    this._params.put(name, value);

    return this;
  }

  public ASTModel addParams(Map<String, Object> params) {
    if (params != null && params.size() > 0) this._params.putAll(params);

    return this;
  }

  public ASTModel setDefault(String name, String value) {
    if (!this._params.containsKey(name)) {
      this.addParam(name, value);
    }

    return this;
  }

  @Override
  public List<ASTItem> children() {
    return LINQ.ofType(this._elements, ASTItem.class);
  }

  @Override
  public List<ASTCondition> getAllConditions() {
    return this._elements.stream()
        .map(e -> e.getAllConditions())
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = new ArrayList<>();

    lst.addAll(
        this._elements.stream()
            .map(e -> e.collectItemsOfType(cls))
            .flatMap(List<T>::stream)
            .collect(Collectors.toList()));

    return lst;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ASTModel)) return false;
    ASTModel astModel = (ASTModel) o;
    return Objects.equals(_elements, astModel._elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_elements);
  }

  public String print() {
    StringBuilder sb = new StringBuilder();
    sb.append("ROOT\n");

    for (ASTItem item : this._elements) {
      sb.append(item.print(1));
    }

    return sb.toString();
  }
}
