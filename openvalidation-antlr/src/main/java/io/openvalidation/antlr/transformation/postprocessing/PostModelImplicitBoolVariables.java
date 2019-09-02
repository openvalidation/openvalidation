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
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.data.DataPropertyType;
import java.util.function.Predicate;

/*
   {contract_signed: true}

   contract_signed AS signed

   IF the contract is signed THEN error

   -> left operand is string and right is bool

   -------------------------------------------------------------------------

   {contract_signed: true}

   contract_signed ALS unterschrieben

   WENN der contract unterschrieben IST DANN error

   -> left operand is property and right is bool
*/

public class PostModelImplicitBoolVariables
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  @Override
  protected Predicate<? super ASTCondition> getFilter() {
    return c ->
        c.hasEqualityComparer()
            && (leftIsBoolRightIsStringOrObjectProperty(c)
                || rightIsBoolLeftIsStringOrObjectProperty(c));
  }

  @Override
  protected void processItem(ASTCondition item) {
    if (leftIsBoolRightIsStringOrObjectProperty(item)) {
      item.setRightOperand(createStaticBool());
    } else {
      item.setLeftOperand(createStaticBool());
    }
  }

  private boolean leftIsBoolRightIsStringOrObjectProperty(ASTCondition condition) {
    return condition.hasLeftOperand()
        && condition.getLeftOperand().isPropertyOrVariable()
        && condition.getLeftOperand().isBoolean()
        && (!condition.hasRightOperand()
            || condition.getRightOperand().isStaticString()
            || condition.getRightOperand().isObjectData());
  }

  private boolean rightIsBoolLeftIsStringOrObjectProperty(ASTCondition condition) {
    return condition.hasRightOperand()
        && condition.getRightOperand().isPropertyOrVariable()
        && condition.getRightOperand().isBoolean()
        && (!condition.hasLeftOperand()
            || condition.getLeftOperand().isStaticString()
            || condition.getLeftOperand().isObjectData());
  }

  private ASTOperandStatic createStaticBool() {
    ASTOperandStatic staticBool = new ASTOperandStatic(("" + true).toLowerCase());
    staticBool.setDataType(DataPropertyType.Boolean);
    return staticBool;
  }
}
