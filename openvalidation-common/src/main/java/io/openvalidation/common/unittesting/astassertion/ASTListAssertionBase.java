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

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ASTListAssertionBase<
        A extends ASTItem, T extends ASTAssertionBase, P extends ASTAssertionBase>
    extends ASTAssertionBase<P> {
  protected List<T> children = new ArrayList<>();

  public ASTListAssertionBase(String name, ASTModel ast, P parent) {
    super(name, -1, ast, parent);
  }

  protected void fillList(List<A> items, Function<A, T> action) {
    if (items != null) {
      items.forEach(
          c -> {
            children.add(action.apply(c));
          });
    }
  }

  public ASTListAssertionBase<A, T, P> hasSizeOf(int size) {
    shouldNotBeEmpty();

    shouldHaveSizeOf(children, size, "LIST");

    return this;
  }

  public ASTListAssertionBase<A, T, P> shouldNotBeEmpty() {
    children.forEach(
        c -> {
          try {
            c.shouldNotBeEmpty();
          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    return this;
  }

  public T first() {
    return atIndex(0);
  }

  public T second() {
    return atIndex(1);
  }

  public T third() {
    return atIndex(2);
  }

  public T atIndex(int index) {
    shouldNotBeEmpty();
    shouldNotBeOutOfRange(children, index);

    return children.get(index);
  }

  public ModelRootAssertion parentRoot() {
    shouldBeInstanceOf(this.parent, ModelRootAssertion.class, "PARENT VARIABLE");

    return (ModelRootAssertion) this.parent();
  }

  public ConditionAssertion parentCondition() {
    shouldBeInstanceOf(this.parent(), OperandAssertion.class, "PARENT CONDITION");

    return ((OperandAssertion) this.parent()).parentCondition();
  }
}
