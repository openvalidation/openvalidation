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

package io.openvalidation.common.model;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.utils.GenericsUtils;

public class ASTWalkingContext {

  private ASTItem parent;
  private ASTItem current;

  public ASTWalkingContext() {}

  public ASTWalkingContext(ASTItem parent, ASTItem current) {
    this.parent = parent;
    this.current = current;
  }

  public ASTItem getParent() {
    return parent;
  }

  public void setParent(ASTItem parent) {
    this.parent = parent;
  }

  public ASTItem getCurrent() {
    return current;
  }

  public void setCurrent(ASTItem current) {
    this.current = current;
  }

  public <T extends ASTItem> T getCurrentAs(Class<T> cls) {
    return GenericsUtils.cast(getCurrent(), cls);
  }

  public <T extends ASTItem> T getParentAs(Class<T> cls) {
    return GenericsUtils.cast(getParent(), cls);
  }
}
