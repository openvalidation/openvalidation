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

package io.openvalidation.common.utils;

import io.openvalidation.common.ast.condition.ASTConditionConnector;

public class GrammarBuilder {
  private StringBuilder _builder = new StringBuilder();

  public GrammarBuilder with(String value) {
    _builder.append(value);
    return this;
  }

  public GrammarBuilder with(Number value) {
    _builder.append(value.toString());
    return this;
  }

  public GrammarBuilder AS() {
    return AS("AS", "");
  }

  public GrammarBuilder AS(String name) {
    return AS("AS", name);
  }

  public GrammarBuilder AS(String affixForToken, String name) {
    this._builder.append(" " + Constants.AS_TOKEN + affixForToken + " ").append(name);
    return this;
  }

  public GrammarBuilder TAKE() {
    return TAKE("TAKE", "");
  }

  public GrammarBuilder TAKE(String value) {
    return TAKE("TAKE", value);
  }

  public GrammarBuilder TAKE(String affixForToken, String value) {
    this._builder.append(" " + Constants.TAKE_TOKEN + affixForToken + " ").append(value);
    return this;
  }

  public GrammarBuilder WITH() {
    return WITH("WITH", "");
  }

  public GrammarBuilder WITH(String value) {
    return WITH("WITH", value);
  }

  public GrammarBuilder WITH(String affixForToken, String value) {
    this._builder.append(" " + Constants.WITH_TOKEN + affixForToken + " ").append(value);
    return this;
  }

  public GrammarBuilder ORDERBY() {
    return FIRST("ORDERBY", "");
  }

  public GrammarBuilder ORDERBY(String value) {
    return ORDERBY("ORDERBY", value);
  }

  public GrammarBuilder ORDERBY(String affixForToken, String value) {
    this._builder.append(" " + Constants.ORDERED_TOKEN + affixForToken + " ").append(value);
    return this;
  }

  public GrammarBuilder FROM() {
    return FIRST("FROM", "");
  }

  public GrammarBuilder FROM(String value) {
    return FROM("FROM", value);
  }

  public GrammarBuilder FROM(String affixForToken, String value) {
    this._builder.append(" " + Constants.FROM_TOKEN + affixForToken + " ").append(value);
    return this;
  }

  public GrammarBuilder FIRST() {
    return FIRST("FIRST", "");
  }

  public GrammarBuilder FIRST(String value) {
    return FIRST("FIRST", value);
  }

  public GrammarBuilder FIRST(String affixForToken, String value) {
    this._builder.append(" " + Constants.FIRST_TOKEN + affixForToken + " ").append(value);
    return this;
  }

  public GrammarBuilder LAST() {
    return LAST("LAST", "");
  }

  public GrammarBuilder LAST(String value) {
    return LAST("LAST", value);
  }

  public GrammarBuilder LAST(String affixForToken, String value) {
    this._builder.append(" " + Constants.LAST_TOKEN + affixForToken + " ").append(value);
    return this;
  }

  public GrammarBuilder IF() {
    return IF("IF", "");
  }

  public GrammarBuilder IF(String value) {
    return IF("IF", value);
  }

  public GrammarBuilder IF(String affixForToken, String value) {
    this._builder.append(Constants.IF_TOKEN + affixForToken + " ").append(value + " ");
    return this;
  }

  public GrammarBuilder THEN() {
    return THEN("THEN", "");
  }

  public GrammarBuilder THEN(String value) {
    return THEN("THEN", value);
  }

  public GrammarBuilder THEN(String affixForToken, String value) {
    this._builder.append(" " + Constants.THEN_TOKEN + affixForToken + " ").append(value);
    return this;
  }

  public GrammarBuilder PARAGRAPH() {
    this._builder.append(" " + Constants.PARAGRAPH_TOKEN);
    return this;
  }

  public GrammarBuilder EQ() {
    return EQ("EQUALS");
  }

  public GrammarBuilder EQ(String affixForToken) {
    return EQ(affixForToken, "", "");
  }

  public GrammarBuilder EQ(String left, String right) {
    return EQ("EQUALS", left, right);
  }

  public GrammarBuilder EQ(String affixForToken, String left, String right) {
    this._builder
        .append(left)
        .append(" " + Constants.EQ_OPERATOR_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " ")
        .append(right);
    return this;
  }

  public GrammarBuilder NEQ() {
    return NEQ("", "");
  }

  public GrammarBuilder NEQ(String left, String right) {
    return NEQ("NOT_EQUAL", left, right);
  }

  public GrammarBuilder NEQ(String affixForToken, String left, String right) {
    this._builder
        .append(left)
        .append(" " + Constants.NOT_EQUALS_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " ")
        .append(right);
    return this;
  }

  public String getText() {
    return this._builder.toString().trim();
  }

  public GrammarBuilder AND() {
    return AND("AND");
  }

  public GrammarBuilder AND(String affixForToken) {
    this._builder.append(" " + Constants.AND_TOKEN + affixForToken + " ");

    return this;
  }

  public GrammarBuilder OR() {
    return OR("OR");
  }

  public GrammarBuilder OR(String affixForToken) {
    this._builder.append(" " + Constants.OR_TOKEN + affixForToken + " ");
    return this;
  }

  public GrammarBuilder UNLESS() {
    return UNLESS("UNLESS");
  }

  public GrammarBuilder UNLESS(String affixForToken) {
    this._builder.append(" " + Constants.UNLESS_TOKEN + affixForToken + " ");

    return this;
  }

  public GrammarBuilder EXISTS() {
    return EXISTS("EXISTS");
  }

  public GrammarBuilder EXISTS(String affixForToken) {
    this._builder.append(
        " " + Constants.EXISTS_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " ");

    return this;
  }

  public GrammarBuilder NOT_EXISTS() {
    return NOT_EXISTS("NOT_EXISTS");
  }

  public GrammarBuilder NOT_EXISTS(String affixForToken) {
    this._builder.append(
        " " + Constants.NOT_EXISTS_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " ");

    return this;
  }

  /**
   * @param indentationLevel indentation depth represented by number of spaces in front of the
   *     combinator/connector (AND/OR)
   */
  public GrammarBuilder indentationDepth(int indentationLevel, ASTConditionConnector connector) {
    return indentationDepth(indentationLevel, connector, null);
  }

  public GrammarBuilder indentationDepth(
      int indentationLevel, ASTConditionConnector connector, String affixForConnector) {
    String trimmedContent = this._builder.toString().trim();
    this._builder = new StringBuilder(trimmedContent);

    this._builder.append("\n");
    for (int i = 0; i < indentationLevel; i++) {
      this._builder.append(" ");
    }

    switch (connector) {
      case AND:
        return affixForConnector != null ? AND(affixForConnector) : AND();
      case OR:
        return affixForConnector != null ? OR(affixForConnector) : OR();
      case UNLESS:
        return affixForConnector != null ? UNLESS(affixForConnector) : UNLESS();
    }

    //        if(connector.equals(ASTConditionConnector.AND)) {
    //            return affixForConnector != null? AND(affixForConnector) : AND();
    //        }
    //        else {
    //            return affixForConnector != null? OR(affixForConnector) : OR();
    //        }

    throw new RuntimeException("UNKNOWN CONNECTOR: " + affixForConnector);
  }

  public GrammarBuilder ERRORCODE(int errorCode) {
    return ERRORCODE("ERRORCODE", errorCode);
  }

  public GrammarBuilder ERRORCODE(String affixForToken, int errorCode) {
    this._builder.append(" " + Constants.ERRORCODE_TOKEN + affixForToken + " " + errorCode + " ");
    return this;
  }

  public GrammarBuilder VARIABLE(String content, String name) {
    return this.with(content + " ").AS(name);
  }

  public GrammarBuilder MUST() {
    return MUST("MUST");
  }

  public GrammarBuilder MUST(String affixForToken) {
    this._builder.append(
        " " + Constants.MUST_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " ");
    return this;
  }

  public GrammarBuilder MUST_NOT() {
    return MUST_NOT(NameMasking.maskName("MUST NOT"));
  }

  public GrammarBuilder MUST_NOT(String affixForToken) {
    this._builder.append(
        " " + Constants.MUSTNOT_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " ");
    return this;
  }

  public GrammarBuilder LESS_THAN() {
    return LESS_THAN("");
  }

  public GrammarBuilder LESS_THAN(int value) {
    return LESS_THAN("LESS_THAN", String.valueOf(value));
  }

  public GrammarBuilder LESS_THAN(String value) {
    return LESS_THAN("LESS_THAN", value);
  }

  public GrammarBuilder LESS_THAN(String affixForToken, String value) {
    this._builder.append(
        " "
            + Constants.LESS_THAN_TOKEN
            + Constants.KEYWORD_SYMBOL
            + affixForToken
            + " "
            + value
            + " ");
    return this;
  }

  public GrammarBuilder GREATER_THAN() {
    return GREATER_THAN("");
  }

  public GrammarBuilder GREATER_THAN(int value) {
    return GREATER_THAN("GREATER_THAN", String.valueOf(value));
  }

  public GrammarBuilder GREATER_THAN(String value) {
    return GREATER_THAN("GREATER_THAN", value);
  }

  public GrammarBuilder GREATER_THAN(String affixForToken, String value) {
    this._builder.append(
        " "
            + Constants.GREATER_THAN_TOKEN
            + Constants.KEYWORD_SYMBOL
            + affixForToken
            + " "
            + value
            + " ");
    return this;
  }

  public GrammarBuilder GREATER_OR_EQUALS() {
    return GREATER_OR_EQUALS("");
  }

  public GrammarBuilder GREATER_OR_EQUALS(int value) {
    return GREATER_OR_EQUALS("GREATER_OR_EQUALS", String.valueOf(value));
  }

  public GrammarBuilder GREATER_OR_EQUALS(String value) {
    return GREATER_OR_EQUALS("GREATER_OR_EQUALS", value);
  }

  public GrammarBuilder GREATER_OR_EQUALS(String affixForToken, String value) {
    this._builder.append(
        " "
            + Constants.GREATER_OR_EQUALS_TOKEN
            + Constants.KEYWORD_SYMBOL
            + affixForToken
            + " "
            + value
            + " ");
    return this;
  }

  public GrammarBuilder AT_LEAST_ONE_OF() {
    return AT_LEAST_ONE_OF("");
  }

  public GrammarBuilder AT_LEAST_ONE_OF(String value) {
    return AT_LEAST_ONE_OF("ONE_OF", value);
  }

  public GrammarBuilder AT_LEAST_ONE_OF(String affixForToken, String value) {
    this._builder.append(
        " "
            + Constants.AT_LEAST_ONE_OF
            + Constants.KEYWORD_SYMBOL
            + affixForToken
            + " "
            + value
            + " ");
    return this;
  }

  public GrammarBuilder NONE_OF() {
    return NONE_OF("");
  }

  public GrammarBuilder NONE_OF(String value) {
    return NONE_OF("ONE_OF", value);
  }

  public GrammarBuilder NONE_OF(String affixForToken, String value) {
    this._builder.append(
        " " + Constants.NONE_OF + Constants.KEYWORD_SYMBOL + affixForToken + " " + value + " ");
    return this;
  }

  public GrammarBuilder COMMENT() {
    return COMMENT(new String[0]);
  }

  public GrammarBuilder COMMENT(String... comments) {
    this._builder.append(Constants.COMMENT_TOKEN + "COMMENT" + " " + String.join("\n", comments));
    return this;
  }

  // arithmetics...
  public GrammarBuilder MULTIPLY() {
    return MULTIPLY("");
  }

  public GrammarBuilder MULTIPLY(Number value) {
    return MULTIPLY("MULTIPLY", String.valueOf(value));
  }

  public GrammarBuilder MULTIPLY(String value) {
    return MULTIPLY("MULTIPLY", value);
  }

  public GrammarBuilder MULTIPLY(String affixForToken, String value) {
    this._builder.append(
        " " + Constants.MULTIPLY_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " " + value);
    return this;
  }

  public GrammarBuilder ADD() {
    return ADD("");
  }

  public GrammarBuilder ADD(int i) {
    return ADD("PLUS", String.valueOf(i));
  }

  public GrammarBuilder ADD(String value) {
    return ADD("PLUS", value);
  }

  public GrammarBuilder ADD(String affixForToken, String value) {
    this._builder.append(
        " " + Constants.ADD_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " " + value);
    return this;
  }

  public GrammarBuilder MINUS() {
    return MINUS("");
  }

  public GrammarBuilder MINUS(int i) {
    return MINUS("MINUS", String.valueOf(i));
  }

  public GrammarBuilder MINUS(String value) {
    return MINUS("MINUS", value);
  }

  public GrammarBuilder MINUS(String affixForToken, String value) {
    this._builder.append(
        " " + Constants.SUBTRACT_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " " + value);
    return this;
  }

  public GrammarBuilder POWEROF() {
    return POWEROF("");
  }

  public GrammarBuilder POWEROF(int i) {
    return POWEROF("POWEROF", String.valueOf(i));
  }

  public GrammarBuilder POWEROF(String value) {
    return POWEROF("POWEROF", value);
  }

  public GrammarBuilder POWEROF(String affixForToken, String value) {
    this._builder.append(
        " " + Constants.POWER_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " " + value);
    return this;
  }

  public GrammarBuilder SUMOF() {
    return SUMOF("");
  }

  public GrammarBuilder SUMOF(String value) {
    return SUMOF("SUM_OF", value);
  }

  public GrammarBuilder SUMOF(String affixForToken, String value) {
    this._builder.append(
        " " + Constants.SUM_OF_TOKEN + Constants.KEYWORD_SYMBOL + affixForToken + " " + value);
    return this;
  }

  public GrammarBuilder OF() {
    return OF("OF", "");
  }

  public GrammarBuilder OF(String entity) {
    return OF("OF", entity);
  }

  public GrammarBuilder OF(String affixForToken, String entity) {
    this._builder.append(" " + Constants.OF_TOKEN + affixForToken + " " + entity + " ");
    return this;
  }

  public GrammarBuilder NL() {
    this._builder.append("\n");
    return this;
  }

  public GrammarBuilder CR() {
    this._builder.append("\r");
    return this;
  }

  @Override
  public String toString() {
    return this.getText();
  }

  // static

  public static GrammarBuilder create() {
    return new GrammarBuilder();
  }

  public static GrammarBuilder createRule() {
    return (new GrammarBuilder()).IF();
  }

  public static GrammarBuilder createComment(String... comments) {
    return (new GrammarBuilder()).COMMENT(comments);
  }

  public static GrammarBuilder createComment() {
    return (new GrammarBuilder()).COMMENT();
  }
}
