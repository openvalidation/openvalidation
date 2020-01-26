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

package io.openvalidation.antlr;

import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.ParseTreeUtils;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyBase;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSemanticOperator;
import io.openvalidation.common.data.DataVariableReference;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

public class NamesExtractor {

  public static List<DataPropertyBase> getNames(ParseTree ctx) {
    return getDataProperties(ctx);
  }

  //  private static List<DataVariableReference> getNamesFromContext(ParseTree ctx) {
  //    List<DataVariableReference> result = new ArrayList<>();
  //
  //    if (ctx instanceof mainParser.NameContext &&
  //        ((mainParser.NameContext) ctx).unknown() != null &&
  //        ctx.getParent() instanceof mainParser.VariableContext
  //    ) {
  //      DataPropertyType type;
  //      String name = ((mainParser.NameContext) ctx).unknown().getText().trim();
  //
  //      type = determineDataPropertyType(ctx);
  //
  //
  //
  //      mainParser.VariableContext variableContext = (mainParser.VariableContext) ctx.getParent();
  //      String originText =
  //          variableContext.expression() != null ? variableContext.expression().getText() : "";
  //
  //      result.add(new DataVariableReference(name, type, originText));
  //    }
  //
  //    for (int i = 0; i < ctx.getChildCount(); i++) {
  //      ParseTree elem = ctx.getChild(i);
  //      if (elem != null) {
  //        List<DataVariableReference> childVars = getNamesFromContext(elem);
  //        if (childVars != null && childVars.size() > 0) result.addAll(childVars);
  //      }
  //    }
  //
  //    return result;
  //  }

  public static List<DataPropertyBase> getDataProperties(ParseTree ctx) {
    List<DataPropertyBase> result = new ArrayList<>();

    if (ctx instanceof mainParser.NameContext && ((mainParser.NameContext) ctx).unknown() != null) {

      if (ctx.getParent() instanceof mainParser.Semantic_operatorContext)
        result.add(createDataSemanticOperator(ctx));
      else if (ctx.getParent() instanceof mainParser.VariableContext)
        result.add(createDataVariableReference(ctx));
    }

    for (int i = 0; i < ctx.getChildCount(); i++) {
      ParseTree elem = ctx.getChild(i);
      if (elem != null) {
        List<DataPropertyBase> childVars = getDataProperties(elem);
        if (childVars != null && childVars.size() > 0) result.addAll(childVars);
      }
    }

    return result;
  }

  private static DataPropertyBase createDataVariableReference(ParseTree ctx) {
    DataPropertyType type;
    String name = ((mainParser.NameContext) ctx).unknown().getText().trim();

    type = determineDataPropertyType(ctx);

    mainParser.VariableContext variableContext = (mainParser.VariableContext) ctx.getParent();
    String originText =
        variableContext.expression() != null ? variableContext.expression().getText() : "";

    return new DataVariableReference(name, type, originText);
  }

  private static DataPropertyBase createDataSemanticOperator(ParseTree ctx) {
    DataPropertyType type;
    String name = ((mainParser.NameContext) ctx).unknown().getText().trim();
    String operandName = null;

    name = StringUtils.reverseKeywords(name);

    type = determineDataPropertyType(ctx);

    mainParser.Semantic_operatorContext semanticOperatorContext =
        (mainParser.Semantic_operatorContext) ctx.getParent();
    // String originText = variableContext.expression() != null ?
    // variableContext.expression().getText() : "";

    ASTComparisonOperator operator = null;
    if (semanticOperatorContext.unknown() != null
        && semanticOperatorContext.unknown().OPERATOR_COMP() != null) {

      try {
        operator =
            ParseTreeUtils.getEffectiveOperator(semanticOperatorContext.unknown().OPERATOR_COMP());
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (semanticOperatorContext.unknown().STRING().size() > 0)
        operandName = semanticOperatorContext.unknown().STRING(0).getText();
    }

    return new DataSemanticOperator(name, operandName, type, operator);
  }

  private static DataPropertyType determineDataPropertyType(ParseTree ctx) {
    mainParser.ExpressionContext expressionContext;
    DataPropertyType type = DataPropertyType.Unknown;

    if (ctx.getParent() instanceof mainParser.VariableContext) {
      expressionContext = ((mainParser.VariableContext) ctx.getParent()).expression();
      if (expressionContext != null
          && expressionContext.children != null
          && expressionContext.children.size() > 0) {

        if (expressionContext.condition() != null || expressionContext.condition_group() != null) {
          // if ((expressionContext.condition() != null && expressionContext.condition().size() > 0)
          // || expressionContext.condition_group() != null) {
          type = DataPropertyType.Boolean;
        } else if (expressionContext.arithmetic() != null) {
          type = DataPropertyType.Decimal;
        } else if (expressionContext.accessor() != null) {
          // lazevedo 23.7.19 Resolutions of unknowns happen later inside Dataschema
          type = DataPropertyType.Unknown;
        } else {
          type = DataPropertyType.Unknown;
        }
      } else {
        type = DataPropertyType.Unknown;
      }
    }
    return type;
  }
}
