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

package io.openvalidation.common.unittesting.astassertion.lists;

import io.openvalidation.common.ast.ASTComment;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.unittesting.astassertion.ASTAssertionBase;
import io.openvalidation.common.unittesting.astassertion.ASTListAssertionBase;
import io.openvalidation.common.unittesting.astassertion.CommentAssertion;
import java.util.List;
import java.util.stream.Collectors;

public class CommentListAssertion
    extends ASTListAssertionBase<ASTComment, CommentAssertion, ASTAssertionBase> {

  public CommentListAssertion(List<ASTComment> comments, ASTModel ast, ASTAssertionBase parent) {
    super("COMMENTS", ast, parent);
    fillList(comments, c -> new CommentAssertion(c, comments.indexOf(c), ast, this));
  }

  public CommentListAssertion hasComment(String content) throws Exception {
    shouldNotBeEmpty();

    shouldContains(allComments(), content, "COMMENT");

    return this;
  }

  public List<String> allComments() {
    return children.stream().flatMap(c -> c.allComments().stream()).collect(Collectors.toList());
  }
}
