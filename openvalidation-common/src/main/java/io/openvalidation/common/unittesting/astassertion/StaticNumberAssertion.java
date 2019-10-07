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

package io.openvalidation.common.unittesting.astassertion;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.unittesting.astassertion.lists.OperandListAssertion;

public class StaticNumberAssertion extends StaticAssertion<ASTOperandStaticNumber> {

  public StaticNumberAssertion(ASTOperandStaticNumber item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public StaticNumberAssertion hasValue(Double value) {
    shouldEquals(model.getNumberValue(), value, "Double Value");
    return this;
  }

  public ArithmeticOperandAssertion parentArithmeticOperand() {
    shouldBeInstanceOf(this.parent(), ArithmeticOperandAssertion.class, "ARITHMETIC NUMBER ITEM");

    return (ArithmeticOperandAssertion) this.parent();
  }

  //    public ArithmeticAssertion parentArithmetic() {
  //        return this.parentArithmeticOperand().parentList();
  //    }

  public OperandAssertion parentOperand() {
    shouldBeInstanceOf(this.parent(), OperandAssertion.class, "PARENT OPERAND");

    return (OperandAssertion) this.parent();
  }

  public ConditionAssertion parentCondition() {
    return parentOperand().parentCondition();
  }

  public RuleAssertion parentRule() {
    return parentCondition().parentRule();
  }

  public OperandListAssertion parentList() {
    ASTAssertionBase p = parent;
    if (p instanceof OperandAssertion) {
      p = ((OperandAssertion) parent).parentList();
    }
    shouldBeInstanceOf(p, OperandListAssertion.class, "PARENT OPERAND LIST");
    return (OperandListAssertion) p;
  }
}
