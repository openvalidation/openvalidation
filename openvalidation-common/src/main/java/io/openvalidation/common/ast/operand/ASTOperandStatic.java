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

import io.openvalidation.common.utils.StringUtils;

public class ASTOperandStatic extends ASTOperandBase {

  private String value;

  public ASTOperandStatic() {
    super();
  }

  public ASTOperandStatic(String val) {
    super();
    this.value = val;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String print(int level) {
    return this.space(level) + getType() + " : " + this.value + "\n";
  }

  @Override
  public boolean hasValue() {
    return !StringUtils.isNullOrEmpty(getValue());
  }
}
