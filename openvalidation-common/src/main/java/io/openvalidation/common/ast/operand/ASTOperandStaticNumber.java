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

package io.openvalidation.common.ast.operand;

import io.openvalidation.common.data.DataPropertyType;
import java.util.Objects;

public class ASTOperandStaticNumber extends ASTOperandStatic {
  private double numberValue;

  //    public ASTOperandStaticNumber(float number){
  //        super();
  //        this.numberValue = number;
  //        this.setValue("" + number);
  //        this.setDataType(DataPropertyType.Decimal);
  //    }

  // todo lazevedo 22.3.19 ilja fragen ob Konstruktor mit float überflüssig und ob parsing lieber
  // im konstruktor
  public ASTOperandStaticNumber(double number) {
    super();
    this.numberValue = number;
    this.setValue("" + number);
    this.setDataType(DataPropertyType.Decimal);
  }

  public double getNumberValue() {
    return numberValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ASTOperandStaticNumber)) return false;
    ASTOperandStaticNumber that = (ASTOperandStaticNumber) o;
    return Double.compare(that.getNumberValue(), getNumberValue()) == 0
        && this.getPreprocessedSource().equals(that.getPreprocessedSource());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getNumberValue());
  }

  @Override
  public String toString() {
    return String.valueOf(numberValue);
  }
}
