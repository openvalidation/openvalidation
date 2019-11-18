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

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.utils.LINQ;
import java.util.ArrayList;
import java.util.List;

public class PostProcessorFactory {
  private List<PostProcessorBase> _postprocessors = new ArrayList<>();

  public PostProcessorFactory() {
    _postprocessors.add(new PostConditionArrayResolver());
    _postprocessors.add(new PostConditionImplicitBoolOperand());
    _postprocessors.add(new PostConditionExistOperationTrimmer());

    // final fallback model post processors ...
    _postprocessors.add(new PostModelMissingPropertyTypeResolver());
    _postprocessors.add(new PostModelVariableResolver());

    _postprocessors.add(new PostModelDataTypeResolver());
//    _postprocessors.add(new PostModelFunctionTypeResolver());

    _postprocessors.add(new PostModelDataSchemaVariableTypeResolver());
    _postprocessors.add(new PostModelConstrainedGroupConnectorInversion());
    _postprocessors.add(new PostModelStripSpecialWords());
    _postprocessors.add(new PostModelNumbersResolver());
    _postprocessors.add(new PostModelNullCheckRulesCreator());
    _postprocessors.add(new PostModelUnlessResolver());
    _postprocessors.add(new PostModelArrayOrReplacement());
    _postprocessors.add(new PostModelCleanConditionGroups());
    _postprocessors.add(new PostModelRemoveEmptyUnknownItem());
    _postprocessors.add(new PostModelImplicitBoolOperand());
    _postprocessors.add(new PostModelImplicitBoolVariables());
    _postprocessors.add(new PostModelMissingRightOperandResolver());
    _postprocessors.add(new PostConditionSingleOperand());
  }

  public List<PostProcessorBase> create(ASTItem item) {
    return LINQ.where(this._postprocessors, p -> p.ofType(item.getClass()));
  }
}
