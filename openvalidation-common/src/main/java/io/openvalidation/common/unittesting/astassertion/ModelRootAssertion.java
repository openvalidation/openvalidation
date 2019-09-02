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

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.unittesting.astassertion.lists.CommentListAssertion;
import io.openvalidation.common.unittesting.astassertion.lists.RuleListAssertion;
import io.openvalidation.common.unittesting.astassertion.lists.VariableListAssertion;
import io.openvalidation.common.utils.Console;

public class ModelRootAssertion extends ASTAssertionBase<ModelRootAssertion> {
  private ASTModel _model;
  private CommentListAssertion _comments;
  private VariableListAssertion _variables;
  private RuleListAssertion _rules;
  private UnknownListAssertion _unknowns;
  private RuleListAssertion _nullCheckRules;

  public ModelRootAssertion() {
    super("RESULT", null, null);
  }

  public ModelRootAssertion(ASTModel model) {
    super("ROOT", model, null);
    this._model = model;
    this._comments = new CommentListAssertion(this._model.getComments(), model, this);
    this._variables = new VariableListAssertion(this._model.getVariables(), model, this);
    this._rules = new RuleListAssertion(this._model.getRules(), model, this);
    this._unknowns = new UnknownListAssertion(this._model.getUnknownElements(), model, this);
    this._nullCheckRules = new RuleListAssertion(this._model.getNullCheckRules(), model, this);
  }

  public CommentListAssertion comments() {
    return this._comments;
  }

  public VariableListAssertion variables() {
    return this._variables;
  }

  public RuleListAssertion rules() {
    return this._rules;
  }

  public UnknownListAssertion unknowns() {
    return this._unknowns;
  }

  public RuleListAssertion nullCheckRules() {
    return this._nullCheckRules;
  }

  public ModelRootAssertion sizeOfElements(int size) {
    this.shouldNotBeNull(this._model, "AST MODEL");
    this.shouldHaveSizeOf(this._model.getElements(), size, "ELEMENTS");

    return this;
  }

  public ModelRootAssertion sizeOfRules(int size) {
    this.shouldNotBeNull(this._model, "AST MODEL");
    this.shouldHaveSizeOf(this._model.getRules(), size, "RULES");

    return this;
  }

  public ModelRootAssertion sizeOfNullCheckRules(int size) {
    this.shouldNotBeNull(this._model, "AST MODEL");
    this.shouldHaveSizeOf(this._model.getNullCheckRules(), size, "NULL CHECK RULES");

    return this;
  }

  public ModelRootAssertion sizeOfComments(int size) {
    this.shouldNotBeNull(this._model, "AST MODEL");
    this.shouldHaveSizeOf(this._model.getComments(), size, "COMMENTS");

    return this;
  }

  public ModelRootAssertion sizeOfVariables(int size) {
    this.shouldNotBeNull(this._model, "AST MODEL");
    this.shouldHaveSizeOf(this._model.getVariables(), size, "VARIABLES");

    return this;
  }

  public VariableAssertion firstVariable(String name) throws Exception {
    return this.variables().first().hasName(name);
  }

  public VariableAssertion firstVariable() throws Exception {
    return this.variables().first();
  }

  // static methods
  public static CommentListAssertion assertComments(ASTModel model) {
    ModelRootAssertion assertion = new ModelRootAssertion(model);
    return assertion.comments();
  }

  public static VariableListAssertion assertVariable(ASTModel model) {
    ModelRootAssertion assertion = new ModelRootAssertion(model);
    return assertion.variables();
  }

  public static RuleListAssertion assertRules(ASTModel model) {
    ModelRootAssertion assertion = new ModelRootAssertion(model);
    return assertion.rules();
  }

  public static ModelRootAssertion assertAST(ASTModel model) {
    ModelRootAssertion assertion = new ModelRootAssertion(model);
    return assertion;
  }

  public static UnknownListAssertion assertUnknown(ASTModel model) {
    ModelRootAssertion assertion = new ModelRootAssertion(model);
    return assertion.unknowns();
  }

  public static ModelRootAssertion assertResult(OpenValidationResult result) throws Exception {
    ModelRootAssertion asr = new ModelRootAssertion();
    asr.shouldNotBeNull(result, "RESULT");

    if (result.hasErrors()) {
      Console.error(result.getErrorPrint(true));
    }
    asr.shouldEquals(result.hasErrors(), false, "HAS ERRORS");

    return new ModelRootAssertion(result.getASTModel());
  }

  public ModelRootAssertion hasOriginalSource(String source) throws Exception {
    hasOriginalSource();
    shouldEquals(ast.getOriginalSource(), source, "ORIGINAL SOURCE");

    return this;
  }

  public ModelRootAssertion hasOriginalSource() throws Exception {
    shouldNotBeNull(_model.getOriginalSource(), "ORIGINAL SOURCE");

    if (ast != null && ast.getPreprocessedSource() != null)
      shouldNotBeEmpty(
          ast.getPreprocessedSource(),
          "ORIGINAL SOURCE"); // TODO jgeske 09.05.19: ast.getPreprocessedSource() should not be
    // empty
    else writeExpected("PREPROCESSED SOURCE should not be null");

    return this;
  }

  public ModelRootAssertion hasPreprocessedSource(String source) throws Exception {
    hasPreprocessedSource();
    shouldEquals(ast.getPreprocessedSource(), source, "PREPROCESSED SOURCE");

    return this;
  }

  public ModelRootAssertion hasPreprocessedSource() throws Exception {
    shouldNotBeNull(ast.getPreprocessedSource(), "AST PREPROCESSED SOURCE");

    //        if(ast != null && ast.getPreprocessedSource() == null)
    //            shouldNotBeEmpty(ast.getPreprocessedSource(), "PREPROCESSED SOURCE"); // TODO
    // jgeske 09.05.19: ast.getPreprocessedSource() should not be empty
    //        else
    //            writeExpected("PREPROCESSED SOURCE should not be null");

    return this;
  }
}
