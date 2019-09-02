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

package io.openvalidation.antlr.transformation.parsetree;

import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.TransformerBase;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.common.ast.ASTComment;
import io.openvalidation.common.utils.StringUtils;

public class PTCommentTransformer
    extends TransformerBase<PTCommentTransformer, ASTComment, mainParser.CommentContext> {

  public PTCommentTransformer(mainParser.CommentContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTComment transform() {
    ASTComment comment = null;
    if (this.antlrTreeCntx.unknown() != null) {
      String content = StringUtils.reverseKeywords(this.antlrTreeCntx.unknown().getText());
      comment = new ASTComment(content);
      comment.setSource(this.antlrTreeCntx.getText());
    }

    return comment;
  }
}
