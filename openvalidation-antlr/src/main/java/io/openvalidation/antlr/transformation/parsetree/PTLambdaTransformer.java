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
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTOperandFunctionBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaProperty;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.ast.operand.property.ASTPropertyStaticPart;
import io.openvalidation.common.data.DataArrayProperty;
import io.openvalidation.common.data.DataPropertyBase;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.NumberParsingUtils;
import io.openvalidation.common.utils.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PTLambdaTransformer
    extends TransformerBase<PTLambdaTransformer, ASTOperandBase, mainParser.LambdaContext> {
  public PTLambdaTransformer(mainParser.LambdaContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTOperandBase transform() throws Exception {
    ASTOperandFunction outerFunction = null;
    ASTOperandBase arrayItem = null;
    ASTOperandFunction mapFunction = null;
    ASTOperandFunction whereFunction = null;
    ASTOperandStaticNumber amountParameter = null;

    ASTConditionBase condition = null;

    if (antlrTreeCntx.content() != null) {
      String outerFunctionCnt = null;
      outerFunction = createOuterFunction(antlrTreeCntx.content().FUNCTION());

      arrayItem = createFromArray(antlrTreeCntx.lambda_from());
      amountParameter =
          extractAmountParameter(
              antlrTreeCntx.content().getText().replaceAll(Constants.FUNCTION_TOKEN_REGEX, ""));

      if (outerFunction != null)
        outerFunctionCnt =
            antlrTreeCntx
                .content()
                .getText()
                .replace(antlrTreeCntx.content().FUNCTION().get(0).getText(), "");
      // getValueOfFirstFunctionParameter(outerFunction);

      // try to get property from content of function
      if (arrayItem == null) {
        String content =
            (outerFunction != null && outerFunction.getParameters().size() > 0)
                ? outerFunctionCnt
                : antlrTreeCntx.content().getText();

        arrayItem = createFromContent(content);
      }

      if (arrayItem != null
          && !StringUtils.isNullOrEmpty(outerFunctionCnt)
          && antlrTreeCntx.lambda_from() != null) {
        mapFunction = this.createMapFunction(arrayItem, outerFunctionCnt);
      }
    }

    condition = createLambdaCondition(antlrTreeCntx.accessor_with(), arrayItem);

    whereFunction = this.createWhereFunction(arrayItem, condition);
    ASTOperandFunction out =
        this.createResultFunction(
            outerFunction, amountParameter, mapFunction, whereFunction, arrayItem);

    return (out != null) ? out : arrayItem;
  }

  private ASTOperandStaticNumber extractAmountParameter(String content) {
    ASTOperandStaticNumber number = null;
    if (NumberParsingUtils.containsNumber(content)) {
      number = new ASTOperandStaticNumber(NumberParsingUtils.extractNumber(content));
      number.setSource(content);
    }
    return number;
  }

  private ASTOperandProperty resolveLambdaProperty(
      ASTOperandStaticString staticOperand, ASTOperandBase arrayScope) {
    ASTOperandProperty operand = null;

    if (staticOperand != null && arrayScope != null) {
      DataPropertyBase property;

      if (arrayScope instanceof ASTOperandProperty) {
        property =
            factoryCntx
                .getSchema()
                .resolve(
                    staticOperand.getValue(), ((ASTOperandProperty) arrayScope).getPathAsString());

        if (property instanceof DataArrayProperty) {
          operand =
              new ASTOperandProperty(
                  ((DataArrayProperty) property).getFullPathExceptArrayPathAsArray());
          operand.setDataType(property.getType());
          operand.setSource(staticOperand.getValue());
          return operand;
        }
      } else if (arrayScope instanceof ASTOperandVariable) {
        // Because the ASTOperandVariable is not yet resolved we assume the property
        // is a property of the array hidden behind the ASTOperandVariable. If that's not the
        // case it will be caught later in the validation layer.
        if (this.factoryCntx.getSchema().isLambdaPropertyOfArray(staticOperand.getValue())) {
          operand = new ASTOperandProperty(staticOperand.getValue());
          operand.setSource(staticOperand.getValue());
          return operand;
        }
      }
    }

    return operand;
  }

  private ASTConditionBase createLambdaCondition(
      mainParser.Accessor_withContext withContext, ASTOperandBase arrayScope) throws Exception {
    ASTConditionBase condition = null;
    ASTOperandProperty scope =
        arrayScope instanceof ASTOperandProperty ? (ASTOperandProperty) arrayScope : null;

    if (withContext != null) {
      ASTItem lambda = createASTItem(antlrTreeCntx.accessor_with());

      if (lambda instanceof ASTConditionBase) {
        condition = (ASTConditionBase) lambda;

        // transform static properties

        condition.walk(
            w -> {
              if (w.getParent() instanceof ASTCondition) {
                ASTOperandProperty property =
                    this.resolveLambdaProperty(
                        w.getCurrentAs(ASTOperandStaticString.class), arrayScope);

                if (property != null) {
                  ASTCondition cnd = w.getParentAs(ASTCondition.class);
                  if (cnd.hasLeftOperand() && cnd.getLeftOperand().equals(w.getCurrent())) {
                    cnd.setLeftOperand(property);
                  } else if (cnd.hasRightOperand()
                      && cnd.getRightOperand().equals(w.getCurrent())) {
                    cnd.setRightOperand(property);
                  }
                }
              }
            },
            ASTOperandStaticString.class);
      } else if (lambda instanceof ASTOperandStaticString) {
        // lazevedo 21.1.20 if the lambda is no condition but a static string a check on whether it
        // is
        // an implicit bool comparison.
        DataPropertyBase prop =
            factoryCntx
                .getSchema()
                .resolve(((ASTOperandStaticString) lambda).getValue(), scope.getPathAsString());
        if (prop instanceof DataArrayProperty) {
          ASTOperandProperty astProperty = new ASTOperandProperty();
          astProperty.setDataType(prop.getType());
          astProperty.setPath(
              Arrays.stream(((DataArrayProperty) prop).getFullPathExceptArrayPathAsArray())
                  .map(part -> new ASTPropertyStaticPart(part))
                  .collect(Collectors.toList()));

          ASTOperandBase operand = this.createProperty(prop, lambda.getOriginalSource());
          if (operand instanceof ASTOperandFunction && operand.getName().equals("GET_ARRAY_OF")) {
            ASTOperandProperty opProp =
                ((ASTOperandLambdaProperty) ((ASTOperandFunction) operand).getParameters().get(1))
                    .getProperty();
            opProp.setSource(lambda.getOriginalSource());

            ASTConditionBuilder conditionBuilder = new ASTConditionBuilder().create();
            conditionBuilder
                .withLeftOperand(opProp)
                .withOperator(ASTComparisonOperator.EQUALS)
                .withRightOperandAsBoolean(true)
                .withSource(lambda.getOriginalSource());
            condition = conditionBuilder.getModel();
          }
        }
      }
    }

    return condition;
  }

  private ASTOperandFunction createOuterFunction(List<TerminalNode> functions) {
    ASTOperandFunction outFunction = null;

    if (functions != null && functions.size() > 0) {
      String outerFunctionName = ParseTreeUtils.extractFunctionName(functions.get(0));

      ASTOperandFunctionBuilder outerFunctionBuilder = new ASTOperandFunctionBuilder();
      outerFunctionBuilder.create().withName(outerFunctionName);

      outFunction = outerFunctionBuilder.getModel();
    }

    return outFunction;
  }

  private ASTOperandBase createFromArray(mainParser.Lambda_fromContext fromContext)
      throws Exception {
    ASTItem arrayProperty = null;

    if (fromContext != null && fromContext.content() != null) {
      arrayProperty = this.createASTItem(fromContext.content());

      if (arrayProperty != null && arrayProperty instanceof ASTOperandBase)
        return (ASTOperandBase) arrayProperty;
    }

    return null;
  }

  private ASTOperandBase createFromContent(String content) throws Exception {

    if (!StringUtils.isNullOrEmpty(content)) {
      return this.createProperty(content);
    }

    return null;
  }

  private ASTOperandFunction createWhereFunction(
      ASTOperandBase arrayItem, ASTConditionBase condition) {
    ASTOperandFunction whereFunction = null;

    if (arrayItem != null && condition != null) {
      ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
      builder.createWhereFunction(arrayItem).addLambdaConditionParamenter(condition);

      whereFunction = builder.getModel();
    } else if (arrayItem != null) { // implicid condition equals null
      ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
      // parse property from antlrTreeCntx.accessor_with().content() and fill as condition
    }

    return whereFunction;
  }

  private ASTOperandFunction createMapFunction(
      ASTOperandBase arrayProperty, String lambdaProperty) {

    if (arrayProperty != null
        && arrayProperty instanceof ASTOperandBase
        && !StringUtils.isNullOrEmpty(lambdaProperty)
        && this.factoryCntx.getSchema().isLambdaPropertyOfArray(lambdaProperty)) {

      String[] path = null;
      String[] lambdaProperties = {
        !StringUtils.isNullOrEmpty(lambdaProperty) ? lambdaProperty.trim() : null
      };

      if (arrayProperty instanceof ASTOperandVariable) {
        path = ((ASTOperandVariable) arrayProperty).getPathAsArray();
      } else if (arrayProperty instanceof ASTOperandProperty) {
        path = ((ASTOperandProperty) arrayProperty).getPathAsArray();
      }

      ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
      builder.createArrayOfFunction(path, lambdaProperties);

      return builder.getModel();
    }

    return null;
  }

  private String getValueOfFirstFunctionParameter(ASTOperandFunction function) {
    if (function != null
        && function.getParameters() != null
        && function.getParameters().size() > 0
        && function.getParameters().get(0) instanceof ASTOperandStaticString) {
      return ((ASTOperandStaticString) function.getParameters().get(0)).getValue();
    }

    return null;
  }

  protected ASTOperandFunction createResultFunction(
      ASTOperandFunction outerFunction,
      ASTOperandStaticNumber amount,
      ASTOperandFunction mapFunction,
      ASTOperandFunction whereFunction,
      ASTOperandBase arrayItem) {
    ASTOperandFunction resultFunction = whereFunction;

    if (outerFunction != null) {
      resultFunction = outerFunction;

      if (mapFunction != null) {
        if (whereFunction != null) mapFunction.replaceFirstParameter(whereFunction);
        resultFunction.replaceFirstParameter(mapFunction);

      } else if (whereFunction != null) resultFunction.replaceFirstParameter(whereFunction);
      else if (arrayItem != null) {
        resultFunction.addParameter(arrayItem);
      }

      if (amount != null) {
        outerFunction.addParameter(amount);
      }
    } else if (mapFunction != null) {
      if (whereFunction != null) mapFunction.replaceFirstParameter(whereFunction);

      resultFunction = mapFunction;
    }

    return resultFunction;
  }
}
