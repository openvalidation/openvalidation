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

package io.openvalidation.common.ast.builder;

import io.openvalidation.common.ast.ASTItem;

public abstract class ASTBuilderBase<TBuilder, TParentBuilder, TModel extends ASTItem> {
  protected TParentBuilder parentBuilder;
  protected TModel model;
  protected Class<TModel> _self;

  public ASTBuilderBase(TParentBuilder prntBldr, Class<TModel> self) {
    this.parentBuilder = prntBldr;
    this._self = self;
  }

  public TBuilder create() {
    try {
      this.model = this._self.newInstance();
      return (TBuilder) this;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public TModel getModel() {
    return this.model;
  }

  public TParentBuilder parent() {
    return parentBuilder;
  }

  public TBuilder withSource(String source) {
    this.model.setSource(source);
    return (TBuilder) this;
  }
}
