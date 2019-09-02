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

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.model.ASTWalkingContext;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.common.utils.ThrowingConsumer;
import java.util.ArrayList;
import java.util.List;

public abstract class ASTItem {
  protected String _preprocessedSource;
  private String _originalSource;
  private String _untrimmedSource;
  private int position;

  public String getPreprocessedSource() {
    return _preprocessedSource;
  }

  public boolean hasSource() {
    return !StringUtils.isNullOrEmpty(getPreprocessedSource());
  }

  public void setSource(String original) {
    if (original != null) {
      _untrimmedSource = original;
      _preprocessedSource =
          original.trim(); // todo jgeske 09.05.19 find why this gets called with empty original
      _originalSource = StringUtils.reverseKeywords(_preprocessedSource);
    }
  }

  public String getUntrimmedSource() {
    return _untrimmedSource;
  }

  public String getOriginalSource() {
    return _originalSource; //   \b_([a-zA-Z]+)((_[a-zA-Z]+)*|_)\b
  }

  public String getType() {
    return this.getClass().getSimpleName().toLowerCase();
  }

  public String getTypecommon() {
    return "../common/" + getType();
  }

  public String print(int level) {
    return this.space(level) + getType() + "\n";
  }

  public String space(int level) {
    return StringUtils.padLeft(" ", level * 4);
  }

  public String getGlobalPlain() {
    return _preprocessedSource;
  }

  @JsonIgnore
  public List<ASTCondition> getAllConditions() {
    return new ArrayList<>();
  }

  public void walk(ThrowingConsumer<ASTWalkingContext> action) {
    walk(action, null);
  }

  public void walk(ThrowingConsumer<ASTWalkingContext> action, Class<?> cls) {
    if (action != null) {
      if (this.children() != null && this.children().size() > 0) {
        this.walk(this.children(), action, cls);
      }
    }
  }

  public void walk(
      List<ASTItem> children, ThrowingConsumer<ASTWalkingContext> action, Class<?> cls) {
    if (action != null) {
      if (children != null && children.size() > 0) {
        children.forEach(
            c -> {
              if (c != null) {

                if (((cls != null) ? cls.isInstance(c) : true))
                  action.accept(new ASTWalkingContext(this, c));

                // recursive walk
                c.walk(action, cls);
              }
            });
      }
    }
  }

  public List<ASTItem> children() {
    return new ArrayList<>();
  }

  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> items = new ArrayList<T>();

    if (cls.isInstance(this)) items.add((T) this);

    return items;
  }

  @JsonIgnore
  public List<ASTOperandProperty> getProperties() {
    return new ArrayList<>();
  }

  public int getGlobalPosition() {
    return position;
  }

  public void setGlobalPosition(int position) {
    this.position = position;
  }
}
