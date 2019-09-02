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

package io.openvalidation.common.ast;

import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASTComment extends ASTGlobalElement {
  private List<String> _content = new ArrayList<>();

  public ASTComment(String content) {

    if (content != null) {

      content = StringUtils.reverseKeywords(content);

      for (String c : content.replace("\r\n", "\n").split("\n")) {
        if (c != null && c.trim().length() > 0) {
          this._content.add(c.trim());
        }
      }
    }
  }

  public ASTComment(String... commentLines) {
    Arrays.stream(commentLines).forEach(c -> this._content.add(StringUtils.reverseKeywords(c)));
  }

  public ASTComment(List<String> linesOfContent) {
    linesOfContent.forEach(c -> this._content.add(StringUtils.reverseKeywords(c)));
  }

  public List<String> getContent() {
    return this._content;
  }

  public void add(String comment) {
    comment = StringUtils.reverseKeywords(comment);
    this._content.add(comment);
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(super.print(level));
    for (String val : this._content) {
      sb.append(this.space(level + 1) + val.trim() + "\n");
    }

    return sb.toString();
  }
}
