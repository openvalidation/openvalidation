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

package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.data.DataVariableReference;
import java.util.function.Predicate;

public class PostModelDataSchemaVariableTypeResolver
    extends PostProcessorSubelementBase<ASTModel, ASTVariable> {
  @Override
  protected Predicate<? super ASTVariable> getFilter() {
    return p -> true;
  }

  @Override
  protected void processItem(ASTVariable item) {
    for (DataVariableReference varRef : this.getContext().getSchema().getVariableReferences()) {
      if (varRef.getFullName().equals(item.getName())) {
        varRef.setType(item.getDataType());
      }
    }
  }
}
