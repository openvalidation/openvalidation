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

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.data.DataPropertyType;
import java.util.function.Predicate;

/*
 *
 *
 *        WENN der VErtrag Unterschrieben IST
 *        DANN ..
 *
 *
 *        if (Unterschrieben != true)
 *           throw ...
 *
 *
 *
 */
public class PostConditionImplicitBoolOperand extends PostProcessorSelfBase<ASTCondition> {

  @Override
  protected Predicate<ASTCondition> getFilter() {
    return c ->
        (c != null
            && !c.hasRightOperand()
            && c.hasLeftOperand()
            && c.hasEqualityComparer()
            && c.getLeftOperand().isPropertyOrVariable()
            && c.getLeftOperand().isBoolean());
  }

  @Override
  protected void processItem(ASTCondition condition) {

    if (!condition.hasRightOperand()) {

      ASTOperandStatic staticBool = new ASTOperandStatic("true");
      staticBool.setDataType(DataPropertyType.Boolean);
      staticBool.setSource("");
      condition.setRightOperand(staticBool);
    }
  }
}
