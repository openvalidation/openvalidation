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

package io.openvalidation.common.ast.operand.arithmetical;

import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;

public class ASTOperandArithmeticalNumberItem extends ASTOperandArithmeticalItemBase {

  public ASTOperandArithmeticalNumberItem(double number) {
    this.operand = new ASTOperandStaticNumber(number);
    this._preprocessedSource =
        String.valueOf(
            number); // todo jgeske 09.05.19 get source object for number for inheritance of source
  }

  public ASTOperandArithmeticalNumberItem(ASTOperandStaticNumber operand) {
    this.operand = operand;
  }

  public ASTOperandStaticNumber getOperand() {
    return (ASTOperandStaticNumber) this.operand;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    double numberValue = (this.getOperand() != null) ? this.getOperand().getNumberValue() : -999999;

    if (this.getOperator() != null)
      sb.append("\n" + super.space(level) + "operation : " + this.getOperator().name());

    sb.append("\n" + this.space(level) + this.getType() + " : " + numberValue);

    return sb.toString();
  }
}
