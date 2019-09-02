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

package io.openvalidation.common.ast.operand.property;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import java.util.List;

public class ASTPropertyConstraintPart extends ASTPropertyPart {
  private ASTConditionBase condition;

  public ASTPropertyConstraintPart(String prt) {
    this.part = prt;
  }

  public ASTConditionBase getCondition() {
    return condition;
  }

  public void setCondition(ASTConditionBase condition) {
    this.condition = condition;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.condition != null) lst.addAll(this.condition.collectItemsOfType(cls));

    return lst;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + getType() + " : " + this.getPart() + "\n");

    if (this.getCondition() != null) {
      sb.append(this.space(level + 1) + "LambdaCondition : \n");
      sb.append(this.getCondition().print(level + 2));
    }

    return sb.toString();
  }
}
