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

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTRule;

public class ASTBuilder extends ASTBuilderBase<ASTBuilder, ASTBuilder, ASTModel> {
  private ASTModelBuilder modelBuilder;

  public ASTBuilder() {
    super(null, ASTModel.class);
    this.modelBuilder = new ASTModelBuilder(this);
  }

  public ASTModelBuilder createModel() {
    this.modelBuilder.create();

    this.model = this.modelBuilder.getModel();
    return modelBuilder;
  }

  public ASTRule getFirstRule() {
    return this.modelBuilder.getFirstRule();
  }

  public static ASTBuilder defaultASTBuilder() {
    return new ASTBuilder();
  }

  //    @Override
  //    public ASTModel getModel(){
  //        ASTModel model = super.getModel();
  //
  //        return (model != null)? model.postProcessing() : model;
  //    }
}
