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

package io.openvalidation.antlr.transformation;

import io.openvalidation.antlr.transformation.postprocessing.PostProcessor;
import io.openvalidation.common.data.DataPropertyBase;
import io.openvalidation.common.data.DataSchema;

public class TransformerContext {
  private DataSchema _schema;
  private PostProcessor _postProcessor;

  private int currentConditionIndentation;

  public TransformerContext(DataSchema schema) {
    this._schema = (schema != null) ? schema : new DataSchema();

    this._postProcessor = new PostProcessor(this._schema);
    currentConditionIndentation = 0;
  }

  public DataSchema getSchema() {
    return this._schema;
  }

  public DataPropertyBase resolveProperty(String content) {
    return getSchema().resolve(content);
  }

  public void setTrailingWhitespaceAsCurrentIndentation(String text) {
    int trailingWhitespaceStartIndex = 0;
    for (int i = text.length() - 1; i >= 0; i--) {
      if (!(text.charAt(i) == ' ' || text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
        trailingWhitespaceStartIndex = i + 1;
        i = -1;
      }
    }

    String trailingWhitespace = text.substring(trailingWhitespaceStartIndex);
    trailingWhitespace = trailingWhitespace.replaceAll("\\r\\n", "");
    trailingWhitespace = trailingWhitespace.replaceAll("\\n", "");
    int indentation =
        trailingWhitespace.length() / 4 * 4; // e.g. indentation of 4-7 is indentationlevel 4

    setCurrentConditionIndentation(indentation);
  }

  public int getCurrentConditionIndentation() {
    return currentConditionIndentation;
  }

  public void setCurrentConditionIndentation(int currentConditionIndentation) {
    this.currentConditionIndentation = currentConditionIndentation;
  }

  public PostProcessor getPostProcessor() {
    return _postProcessor;
  }
}
