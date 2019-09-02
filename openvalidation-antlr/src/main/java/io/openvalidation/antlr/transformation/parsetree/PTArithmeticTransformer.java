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
import io.openvalidation.antlr.transformation.ParseTreeUtils;
import io.openvalidation.antlr.transformation.TransformerBase;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.builder.ASTOperandArithmeticalBuilder;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmetical;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalOperation;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.utils.NumberParsingUtils;
import io.openvalidation.common.utils.StringUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.antlr.v4.runtime.tree.ParseTree;

public class PTArithmeticTransformer
    extends TransformerBase<
        PTArithmeticTransformer, ASTOperandArithmetical, mainParser.ArithmeticContext> {
  public PTArithmeticTransformer(mainParser.ArithmeticContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  private AtomicReference<ASTArithmeticalOperator> lastOperator;
  private String lastOperatorSource = "";
  private String input = antlrTreeCntx.getText();

  @Override
  public ASTOperandArithmetical transform() throws Exception {
    // checkFormatOfExpression();
    ASTOperandArithmeticalOperation operation =
        extractArithmeticalOperation(antlrTreeCntx.children);
    ASTOperandArithmetical arithmetical = new ASTOperandArithmetical();
    arithmetical.setOperation(operation);
    if (input != null)
      arithmetical.setSource(input); // todo jgeske 09.05.19 REMOVE IF BEREAKING CHANGE

    return arithmetical;
  }

  //   private void checkFormatOfExpression() throws Exception
  //   {
  //       //same number of open and closed parentheses
  //       int openCloseBalance = parenthesisCountDifference(antlrTreeCntx.getText());
  //       String userMessage;
  //       String techMessage;
  //       if(openCloseBalance < 0) {
  //           userMessage = "The given arithmetic expression contains an unequal number of open and
  // closed parentheses. There are " + Math.abs(openCloseBalance) + "more closed parentheses than
  // there are open ones.\n" +
  //                   "Source: " + antlrTreeCntx.getText(); //remap to original
  //           techMessage = "The given arithmetic expression contains an unequal number of open and
  // closed parentheses. There are " + Math.abs(openCloseBalance) + "more closed parentheses than
  // there are open ones.\n" +
  //                   "Source: " + antlrTreeCntx.getText(); //remap to original
  //           throw new OpenValidationException(userMessage, techMessage);
  //       }
  //       if (openCloseBalance > 0) {
  //           userMessage = "The given arithmetic expression contains an unequal number of open and
  // closed parentheses. There are " + Math.abs(openCloseBalance) + "more open parentheses than
  // there are closed ones.\n" +
  //                   "Source: " + antlrTreeCntx.getText(); //remap to original
  //           techMessage = "The given arithmetic expression contains an unequal number of open and
  // closed parentheses. There are " + Math.abs(openCloseBalance) + "more open parentheses than
  // there are closed ones.\n" +
  //                   "Source: " + antlrTreeCntx.getText(); //remap to original
  //           throw new OpenValidationException(userMessage, techMessage);
  //       }
  //   }

  private ASTOperandArithmeticalOperation extractArithmeticalOperation(List<ParseTree> children)
      throws Exception {
    return extractArithmeticalOperation(children, 0);
  }

  private ASTOperandArithmeticalOperation extractArithmeticalOperation(
      List<ParseTree> children, int currentNestingDepth) throws Exception {
    ASTOperandArithmeticalBuilder builder = this.createBuilder(ASTOperandArithmeticalBuilder.class);
    List<ParseTree> remainingChildren = children;

    int startIndexOfSubArithmetical =
        getFirstOpenParenChildIndex(remainingChildren, currentNestingDepth);
    int endIndexOfSubArithmetical =
        getIndexOfMatchingClosedParen(
            remainingChildren, startIndexOfSubArithmetical, currentNestingDepth);

    if (startIndexOfSubArithmetical == 0 && endIndexOfSubArithmetical == remainingChildren.size()) {
      startIndexOfSubArithmetical =
          getFirstOpenParenChildIndex(remainingChildren, 1, currentNestingDepth);
      endIndexOfSubArithmetical =
          getIndexOfMatchingClosedParen(
              remainingChildren, startIndexOfSubArithmetical, currentNestingDepth);
    }
    boolean subArithmeticalExists = startIndexOfSubArithmetical != -1;

    while (subArithmeticalExists) {
      List<ParseTree> onSameLevel = remainingChildren.subList(0, startIndexOfSubArithmetical);
      addToArithmetical(builder, onSameLevel);

      List<ParseTree> subOperationChildren =
          remainingChildren.subList(startIndexOfSubArithmetical, endIndexOfSubArithmetical);
      AtomicReference<ASTArithmeticalOperator> localLastOperator = lastOperator;
      String localLastOperatorSource = lastOperatorSource;
      lastOperator = null;
      lastOperatorSource = "";
      ASTOperandArithmeticalOperation subOperation =
          extractArithmeticalOperation(subOperationChildren, currentNestingDepth + 1);

      builder.withOperation(subOperation, localLastOperator.get(), localLastOperatorSource);

      remainingChildren =
          remainingChildren.subList(endIndexOfSubArithmetical, remainingChildren.size());

      startIndexOfSubArithmetical =
          getFirstOpenParenChildIndex(remainingChildren, currentNestingDepth);
      endIndexOfSubArithmetical =
          getIndexOfMatchingClosedParen(
              remainingChildren, startIndexOfSubArithmetical, currentNestingDepth);
      if (startIndexOfSubArithmetical == 0
          && endIndexOfSubArithmetical == remainingChildren.size()) {
        startIndexOfSubArithmetical =
            getFirstOpenParenChildIndex(remainingChildren, 1, currentNestingDepth);
        endIndexOfSubArithmetical =
            getIndexOfMatchingClosedParen(
                remainingChildren, startIndexOfSubArithmetical, currentNestingDepth);
      }
      subArithmeticalExists = startIndexOfSubArithmetical != -1;
    }

    addToArithmetical(builder, remainingChildren);

    setSource(builder.getModel().getOperation(), children);
    return builder.getModel().getOperation();
  }

  private void addToArithmetical(ASTOperandArithmeticalBuilder builder, List<ParseTree> toAdd)
      throws Exception {
    lastOperator = new AtomicReference<>();
    // TODO: lazevedo 13.5.19 Implement operators as complex type with information about their
    // source
    // the original source has to be given to the builder manually because he has no access to the
    // parse tree and also because operators are just enums that do not carry any information about
    // their original source

    for (ParseTree c : toAdd) {
      if (ParseTreeUtils.getSymbol(c) == mainParser.OPERATOR_ARITH) {
        ASTArithmeticalOperator operator = ParseTreeUtils.getArithmeticalOperator(c);
        lastOperator.set(operator);
        lastOperatorSource = c.getText();
      } else {
        ASTItem item = this.createASTItem(c);

        if (item != null) {

          if (item instanceof ASTOperandProperty) {
            builder.withProperty((ASTOperandProperty) item, lastOperator.get(), lastOperatorSource);
          } else if (item instanceof ASTOperandStaticNumber) {
            builder.withNumber(
                (ASTOperandStaticNumber) item, lastOperator.get(), lastOperatorSource);
          } else if (item instanceof ASTOperandVariable) {
            builder.withVariable((ASTOperandVariable) item, lastOperator.get(), lastOperatorSource);
          } else if (item instanceof ASTOperandStaticString) {

            Double value =
                NumberParsingUtils.extractDouble(((ASTOperandStaticString) item).getValue());

            if (value != null) {
              ASTOperandStaticNumber num = new ASTOperandStaticNumber(value);
              num.setSource(item.getPreprocessedSource());
              builder.withNumber(num, lastOperator.get(), lastOperatorSource);
            } else {
              builder.withString(
                  (ASTOperandStaticString) item, lastOperator.get(), lastOperatorSource);
            }

          } else {
            throw new ASTValidationException(
                "arithmetical operand has unsupported type: '" + item.getType() + "'", item);
          }

        } else {
          builder.withEmptyOperand(c.getText(), lastOperator.get(), lastOperatorSource);
        }

        lastOperatorSource = "";
        lastOperator.set(null);
      }
    }
  }

  private int getIndexOfMatchingClosedParen(
      List<ParseTree> children, int indexOfFirstOpenParen, int nestingDepth) {
    int numberOfOpenParens;
    // for debugging to visualize content of each child
    //        List<String> childrenText = new ArrayList<>();
    //        children.stream().forEach(c ->
    // childrenText.add(c.getText().replaceAll("ʬarithmoperatorʬ[a-zA-z]+ʬ","")));

    if (indexOfFirstOpenParen >= 0) {
      numberOfOpenParens =
          parenthesisCountDifference(children.get(indexOfFirstOpenParen).getText()) - nestingDepth;
    } else {
      return -1;
    }

    for (int i = indexOfFirstOpenParen + 1; i < children.size(); i++) {
      int parenCountDifference = parenthesisCountDifference(children.get(i).getText());
      numberOfOpenParens += parenCountDifference;
      // when we go back to nestinglevel 0 and we know that the parencount was reduced (we actually
      // returned from deeper nesting and did not stay at the same level)
      if (numberOfOpenParens <= 0 && parenCountDifference < 0) {
        return i + 1;
      }
    }

    // if no matching closing parenthesis exists
    return -1;
  }

  private int getFirstOpenParenChildIndex(
      List<ParseTree> children, int searchStartIndex, int nestingDepth) {
    for (int i = searchStartIndex; i < children.size(); i++) {
      // find first opening parenthesis that matters (exclude redundant parentheses that do not
      // indicate the beginning of a group e.g. in "(a number)"
      if (parenthesisCountDifference(children.get(i).getText()) > 0) {
        if (i == 0) {
          if (parenthesisCountDifference(children.get(0).getText()) > nestingDepth) {
            return 0;
          }
        } else {
          return i;
        }
      }
    }
    // if no open parenthesis exists
    return -1;
  }

  private int getFirstOpenParenChildIndex(List<ParseTree> children, int nestingDepth) {
    return getFirstOpenParenChildIndex(children, 0, nestingDepth);
  }

  private int parenthesisCountDifference(String text) {
    int openParentheses = countOpenParentheses(text);
    int closedParentheses = countClosedParentheses(text);

    return openParentheses - closedParentheses;
  }

  private int countClosedParentheses(String text) {
    return text.length() - text.replaceAll("\\)", "").length();
  }

  private int countOpenParentheses(String text) {
    return text.length() - text.replaceAll("\\(", "").length();
  }

  private void setSource(ASTOperandArithmeticalOperation operation, List<ParseTree> children) {
    StringBuilder sb = new StringBuilder();

    for (ParseTree child : children) {
      sb.append(child.getText());
    }

    String sourceWithCorrectNumberOfParens = correctNumOfParens(sb.toString());
    operation.setSource(sourceWithCorrectNumberOfParens);
  }

  private String correctNumOfParens(String source) {
    String result = source;
    int parenDiff = parenthesisCountDifference(source);

    // more closed than open parens (notice that parens do not get mirrored -> closed parens dont
    // turn into open ones))
    if (parenDiff < 0) {
      // mirror, remove, mirror again
      result = StringUtils.mirrorString(result);
      for (int i = 0; i < -parenDiff; i++) {
        result = result.replaceFirst("[^)]*\\)", "");
      }
      result = StringUtils.mirrorString(result);
    }
    // more open than closed parens
    else if (parenDiff > 0) {
      for (int i = 0; i < parenDiff; i++) {
        result = result.replaceFirst("[^(]*\\(", "");
      }
    }

    return result;
  }
}
