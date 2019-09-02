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
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataArrayProperty;
import io.openvalidation.common.data.DataPropertyBase;
import io.openvalidation.common.utils.LINQ;
import java.util.List;
import java.util.function.Predicate;

public class PostModelMissingPropertyTypeResolver
    extends PostProcessorSubelementBase<ASTModel, ASTOperandProperty> {

  @Override
  protected Predicate<? super ASTOperandProperty> getFilter() {
    return p -> p.getDataType() == null;
  }

  @Override
  protected void processItem(ASTOperandProperty operand) {

    DataPropertyBase prop = this.getContext().getSchema().resolve(operand.getPathAsString());
    List<DataArrayProperty> allarrayproperties = this.getContext().getSchema().getArrayProperties();

    if (prop == null && allarrayproperties != null && allarrayproperties.size() > 0) {
      prop = LINQ.findFirst(allarrayproperties, p -> p.getName().equals(operand.getName()));
    }

    if (prop != null) operand.setDataType(prop.getType());
  }
}
