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
import io.openvalidation.common.utils.LINQ;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class PostProcessorSelfBase<TPostItem extends ASTItem> extends PostProcessorBase {
  protected abstract Predicate<? super TPostItem> getFilter();

  protected abstract void processItem(TPostItem item);

  @Override
  public void process(ASTItem item, PostProcessorContext context) {
    this.setContext(context);

    for (TPostItem itm : getRelevantItems((TPostItem) item)) {
      this.processItem(itm);
    }
  }

  private List<TPostItem> getRelevantItems(TPostItem i) {
    List<TPostItem> items = new ArrayList();
    items.add(i);

    return LINQ.where(items, getFilter());
  }
}
