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

public class ASTUnknown extends ASTGlobalElement {

  public ASTUnknown() {
  }

  public ASTUnknown(String originalSource) {
    this.setSource(originalSource);
  }


  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(super.print(level));
    sb.append(this.space(level + 1) + getPreprocessedSource() + "\n");

    return sb.toString();
  }
}
