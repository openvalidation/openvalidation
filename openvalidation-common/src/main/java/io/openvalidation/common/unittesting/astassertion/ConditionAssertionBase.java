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
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;

public class ConditionAssertionBase<T extends ASTConditionBase, S extends ASTAssertionBase>
    extends ASTItemAssertionBase<T, ASTAssertionBase, S> {

  public ConditionAssertionBase(T item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);
  }

  public ConditionAssertionBase(T item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  protected ConditionAssertionBase hasConnector(ASTConditionConnector connector) {
    shouldNotBeNull(connector, "CONNECTOR");
    shouldEquals(this.model.getConnector(), connector, "CONNECTOR");
    return this;
  }
}
