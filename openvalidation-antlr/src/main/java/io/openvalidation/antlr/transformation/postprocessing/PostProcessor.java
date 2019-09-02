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
import io.openvalidation.common.data.DataSchema;
import java.util.List;

public class PostProcessor {
  private PostProcessorFactory _factory;
  private PostProcessorContext _context;

  public PostProcessor(DataSchema schema) {
    this._context = new PostProcessorContext();
    this._context.setSchema(schema);

    this._factory = new PostProcessorFactory();
  }

  public void process(ASTItem item) {
    List<PostProcessorBase> processors = this._factory.create(item);
    if (processors != null && processors.size() > 0) {
      for (PostProcessorBase processor : processors) {
        processor.process(item, this._context);
      }
    }
  }
}
