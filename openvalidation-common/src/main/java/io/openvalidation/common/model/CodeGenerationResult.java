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

package io.openvalidation.common.model;

import io.openvalidation.common.ast.ASTModel;

public class CodeGenerationResult {
  private String _name;
  private String _id;
  private String _code;
  private ASTModel _ast;
  private String _preprocessedRule;
  private CodeKind _codeKind;
  private String _codeFileName;

  public CodeGenerationResult(ASTModel ast, String code) {
    this._code = code;
    this._ast = ast;
  }

  public String getCode() {
    return _code;
  }

  public void setCode(String _code) {
    this._code = _code;
  }

  public ASTModel getAST() {
    return this._ast;
  }

  public void setAST(ASTModel ast) {
    this._ast = ast;
  }

  public CodeKind getCodeKind() {
    return _codeKind;
  }

  public void setCodeKind(CodeKind codeKind) {
    this._codeKind = codeKind;
  }

  public String getName() {
    return _name;
  }

  public String getId() {
    return _id;
  }

  public void setName(String name) {
    this._name = name;
  }

  public static CodeGenerationResult createFrameworkResult(ASTModel ast, String code) {
    CodeGenerationResult result = new CodeGenerationResult(ast, code);
    result.setCodeKind(CodeKind.Framework);
    result.setName("HUMLFramework");
    return result;
  }

  public static CodeGenerationResult createValidatorFactoryResult(String code) {
    CodeGenerationResult result = new CodeGenerationResult(null, code);
    result.setCodeKind(CodeKind.ValidatorFactory);
    result.setName("OpenValidatorFactory");
    return result;
  }

  public static CodeGenerationResult createImplementation(ASTModel ast, String code) {
    CodeGenerationResult result = new CodeGenerationResult(ast, code);
    result.setCodeKind(CodeKind.Implementation);
    return result;
  }

  public static CodeGenerationResult createCombined(ASTModel ast, String code) {
    CodeGenerationResult result = new CodeGenerationResult(ast, code);
    result.setCodeKind(CodeKind.Combined);
    return result;
  }

  public String getPreprocessedRule() {
    return _preprocessedRule;
  }

  public void setPreprocessedRule(String preprocessedRule) {
    this._preprocessedRule = preprocessedRule;
  }

  public String getCodeFileName() {
    return _codeFileName;
  }

  public void setCodeFileName(String _codeFileName) {
    this._codeFileName = _codeFileName;
  }
}
