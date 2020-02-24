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

import io.openvalidation.antlr.generated.mainLexer;
import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.LINQ;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public class ParseTreeUtils {

  public static int getSymbol(ParseTree node) {
    if (isSymbol(node)) return ((TerminalNodeImpl) node).getSymbol().getType();

    return -1;
  }

  public static boolean isSymbol(ParseTree node) {
    if (node instanceof TerminalNodeImpl) {
      Token symbol = ((TerminalNodeImpl) node).getSymbol();
      return (symbol != null && symbol.getType() > -1);
    }

    return false;
  }

  public static ASTConditionConnector getCombinator(ParseTree c) throws Exception {
    String str =
        c.getText()
            .replaceAll(
                Constants.KEYWORD_SYMBOL + Constants.TOKEN_SOURCE_TAIL_PATTERN + "$",
                Constants.KEYWORD_SYMBOL);
    // todo lazevedo 15.4.19 Source mapping for the combinator (affix is currently cut out)

    if (str.equals(Constants.AND_TOKEN)) return ASTConditionConnector.AND;
    if (str.equals(Constants.OR_TOKEN)) return ASTConditionConnector.OR;
    if (str.equals(Constants.UNLESS_TOKEN)) return ASTConditionConnector.UNLESS;

    throw new OpenValidationException("unknown Connector: " + str);
  }

  public static ASTComparisonOperator getOperator(TerminalNode terminalNode) throws Exception {
    String str = terminalNode.getText();

    if (str != null) {
      str =
          str.replace(Constants.COMPOPERATOR_TOKEN, "")
              .replaceAll(Constants.KEYWORD_SYMBOL + Constants.TOKEN_SOURCE_TAIL_PATTERN, "")
              .toUpperCase();

      return ASTComparisonOperator.valueOf(str);
    }

    throw new OpenValidationException(" UNKNOWN COMPARISON OPERATOR: " + str);
  }

  public static ASTArithmeticalOperator getArithmeticalOperator(ParseTree node)
      throws OpenValidationException {
    String str = node.getText();

    if (str != null) {
      str =
          str.replace(Constants.ARITHMETIC_OPERATOR_TOKEN, "")
              .replaceAll(Constants.KEYWORD_SYMBOL + Constants.TOKEN_SOURCE_TAIL_PATTERN, "")
              .toLowerCase();

      switch (str) {
        case "add":
          return ASTArithmeticalOperator.Addition;
        case "subtract":
          return ASTArithmeticalOperator.Subtraction;
        case "multiply":
          return ASTArithmeticalOperator.Multiplication;
        case "divide":
          return ASTArithmeticalOperator.Division;
        case "modulo":
          return ASTArithmeticalOperator.Modulo;
        case "power":
          return ASTArithmeticalOperator.Power;
      }
    }

    throw new OpenValidationException("unknown Arithmetical Operator: " + str);
  }

  public static String extractFunctionName(TerminalNode function) {
    if (function != null) return extractFunctionName(function.getText());

    return null;
  }

  public static String extractFunctionName(String function) {

    if (function != null) {
      // todo lazevedo 15.4.19 source map affix of FUNCTION token
      return function
          .replace(Constants.FUNCTION_TOKEN, "")
          .replaceFirst(Constants.KEYWORD_SYMBOL + Constants.TOKEN_SOURCE_TAIL_PATTERN, "")
          .toUpperCase();
    }

    return function;
  }

  public static String extractCONSTRAINT(TerminalNode constraint) {
    // todo lazevedo 15.4.19 source map affix of MUST token here (or even necessary at all)?
    return constraint
        .toString()
        .replace(Constants.CONSTRAINT_TOKEN, "")
        .replaceAll(Constants.KEYWORD_SYMBOL + Constants.TOKEN_SOURCE_TAIL_PATTERN, "");
  }

  public static boolean isMUSTConstraint(TerminalNode constraint) {
    String constr = extractCONSTRAINT(constraint);

    return (constr.equals(Constants.MUST) || constr.equals(Constants.MUSTNOT));
  }

  public static boolean isMUSTExceptNotConstraint(TerminalNode constraint) {
    String constr = extractCONSTRAINT(constraint);

    return constr.equals(Constants.MUST);
  }

  public static String getTextFromConnectorAndNode(mainParser.Condition_exprContext tree) {
    String value = tree.getText();

    mainParser.Condition_groupContext mother = (mainParser.Condition_groupContext) tree.parent;
    List<ParseTree> children = mother.children;
    int pos = -1;

    for (int i = 0; i < children.size(); i++) {
      if (children.get(i).equals(tree)) {
        pos = i;
      }
    }

    if (pos == 0) {
      value = tree.getText();
    } else if (ParseTreeUtils.getSymbol(children.get(pos - 1)) == mainLexer.COMBINATOR) {
      if (pos < 2) {
        String temp = mother.getText();
        value = temp.substring(0, temp.indexOf(tree.getText()) + tree.getText().length());
      } else {
        String previous = getPreviousConditionText(mother, pos);
        value =
            mother
                .getText()
                .substring(
                    mother.getText().indexOf(previous) + previous.length(),
                    mother.getText().indexOf(value) + value.length());
      }
    }

    return value;
  }

  public static ASTComparisonOperator getEffectiveOperator(List<TerminalNode> operators)
      throws Exception {
    if (operators != null && operators.size() > 0) {
      List<TerminalNode> tnoperators =
          LINQ.where(operators, o -> (ParseTreeUtils.getSymbol(o) == mainParser.OPERATOR_COMP));

      if (tnoperators != null && tnoperators.size() > 0) {
        if (tnoperators.size() == 1) return getOperator(tnoperators.get(0));

        List<ASTComparisonOperator> comps =
            LINQ.select(
                tnoperators,
                o -> {
                  try {
                    return getOperator(o);
                  } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                  }
                });

        return (LINQ.any(comps, c -> c != ASTComparisonOperator.EQUALS))
            ? LINQ.findFirst(comps, c -> c != ASTComparisonOperator.EQUALS)
            : comps.get(0);
      }
    }

    return null;
  }

  private static String getPreviousConditionText(
      mainParser.Condition_groupContext parent, int pos) {
    String value = "";

    for (pos = pos - 1; pos > -1; pos--) {
      if (parent.children.get(pos) instanceof mainParser.Condition_exprContext) {
        value = parent.children.get(pos).getText();
        break;
      }
    }

    return value;
  }

  public static String getEffectiveRuleIndicator(List<TerminalNode> indicators) {
    if (indicators != null && indicators.size() > 0) {
      List<String> strindicators =
          LINQ.where(indicators, o -> (ParseTreeUtils.getSymbol(o) == mainParser.CONSTRAINT))
              .stream()
              .map(t -> ParseTreeUtils.extractCONSTRAINT(t))
              .collect(Collectors.toList());

      if (strindicators != null && strindicators.size() > 0) {
        if (strindicators.size() == 1) return strindicators.get(0);

        return LINQ.any(strindicators, i -> i.equals(Constants.MUSTNOT))
            ? LINQ.findFirst(strindicators, i -> i.equals(Constants.MUSTNOT))
            : strindicators.get(0);
      }
    }

    return null;
  }

  //    public static String getTakeKind(mainParser.Linq_takeContext linq_takeContext) {
  //        String kind = null;
  //
  //        if (linq_takeContext.AMOUNT() != null) {
  //            kind = linq_takeContext.AMOUNT().getText();
  //        }
  //
  //        return kind;
  //    }
}
