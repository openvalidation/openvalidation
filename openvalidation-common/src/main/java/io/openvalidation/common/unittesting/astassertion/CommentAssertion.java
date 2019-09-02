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

import io.openvalidation.common.ast.ASTComment;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.unittesting.astassertion.lists.CommentListAssertion;
import java.util.ArrayList;
import java.util.List;

public class CommentAssertion
    extends ASTItemAssertionBase<ASTComment, ASTAssertionBase, CommentAssertion> {

  public CommentAssertion(ASTComment comment, int index, ASTModel ast, ASTAssertionBase parent) {
    super(comment, index, ast, parent);
  }

  public CommentAssertion shouldNotBeEmpty() {
    super.shouldNotBeEmpty();

    shouldNotBeEmpty(model.getContent(), "CONTENTS");

    return this;
  }

  public CommentAssertion hasLineSizeOf(int size) throws Exception {
    shouldNotBeEmpty();

    this.shouldHaveSizeOf(allComments(), size, "LINES");

    return this;
  }

  public CommentAssertion hasComment(String content) throws Exception {
    shouldNotBeEmpty();

    shouldContains(allComments(), content, "COMMENT");

    return this;
  }

  public List<String> allComments() {
    return (model != null && model.getContent() != null) ? model.getContent() : new ArrayList<>();
  }

  public CommentListAssertion parentList() {
    return parentAssertion(CommentListAssertion.class);
  }
}
