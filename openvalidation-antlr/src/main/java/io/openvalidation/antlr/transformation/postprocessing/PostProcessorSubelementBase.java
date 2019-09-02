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
import io.openvalidation.common.utils.GenericsUtils;
import io.openvalidation.common.utils.LINQ;
import java.util.List;
import java.util.function.Predicate;

public abstract class PostProcessorSubelementBase<
        TPostItem extends ASTItem, TSubelement extends ASTItem>
    extends PostProcessorBase<TPostItem> {

  protected abstract Predicate<? super TSubelement> getFilter();

  protected abstract void processItem(TSubelement item);

  protected ASTItem _item;

  protected TPostItem getItem() {
    return (TPostItem) this._item;
  }

  @Override
  public void process(ASTItem item, PostProcessorContext context) {
    this._item = item;
    this.setContext(context);

    Class<TSubelement> cls = GenericsUtils.getGenericClass(this.getClass(), 1);
    List<TSubelement> list = item.collectItemsOfType(cls);

    for (TSubelement itm : LINQ.where(list, getFilter())) {

      this.processItem(itm);
    }
  }
}
