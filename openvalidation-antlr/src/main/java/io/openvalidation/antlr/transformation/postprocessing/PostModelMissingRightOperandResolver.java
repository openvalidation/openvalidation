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

package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.ast.operand.property.ASTPropertyStaticPart;
import io.openvalidation.common.data.DataProperty;
import io.openvalidation.common.data.DataPropertyBase;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.data.DataVariableReference;
import io.openvalidation.common.utils.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PostModelMissingRightOperandResolver
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {

  private List<ASTVariable> variableDefinitions = null;

  @Override
  public void process(ASTItem item, PostProcessorContext context) {
    this.variableDefinitions = item.collectItemsOfType(ASTVariable.class);
    super.process(item, context);
  }

  @Override
  protected Predicate<? super ASTCondition> getFilter() {
    return c ->
        c.hasSimpleComparisonOperator()
            && c.hasLeftOperand()
            && !c.hasRightOperand()
            && c.getLeftOperand().isPropertyOrVariable();
  }

  @Override
  protected void processItem(ASTCondition item) {
    DataSchema schema = this.getContext().getSchema();
    ASTOperandBase leftOperand = item.getLeftOperand();

    List<DataPropertyBase> allPossibleResolutions =
        schema.resolveAll(leftOperand.getPreprocessedSource());
    ASTOperandBase rightOperand = extractOperand(leftOperand, allPossibleResolutions);

    item.setRightOperand(rightOperand);

    if (rightOperand != null) {
      rearrangeOperands(item);
    }
  }

  private ASTOperandBase extractOperand(
      ASTOperandBase leftOperand, List<DataPropertyBase> allPossibleResolutions) {
    String leftOperandFullName =
        leftOperand instanceof ASTOperandProperty
            ? ((ASTOperandProperty) leftOperand).getPathAsString()
            : leftOperand.getName();

    List<DataPropertyBase> possibleResolutions =
        allPossibleResolutions.stream()
            .filter(
                p ->
                    p.getType() == leftOperand.getDataType()
                        && !p.getFullNameLowerCase().equals(leftOperandFullName.toLowerCase()))
            .collect(Collectors.toList());

    ASTOperandBase rightOperand = null;

    if (possibleResolutions.size() == 1) {
      DataPropertyBase foundProperty = possibleResolutions.get(0);
      rightOperand = createOperandBase(foundProperty, leftOperand.getPreprocessedSource());
    }
    return rightOperand;
  }

  //    private ASTOperandBase extractForOperandProperty(ASTOperandProperty leftProperty,
  // List<DataPropertyBase> allPossibleResolutions) {
  //        List<DataPropertyBase> possibleResolutions = allPossibleResolutions.stream()
  //                .filter(p -> p.getType() == leftProperty.getDataType()
  //                        &&
  // !p.getFullNameLowerCase().equals(leftProperty.getPathAsString().toLowerCase())).collect(Collectors.toList());
  //
  //        ASTOperandBase rightOperand = null;
  //
  //        if(possibleResolutions.size() == 1)
  //        {
  //            DataPropertyBase foundProperty = possibleResolutions.get(0);
  //            rightOperand = createOperandBase(foundProperty,
  // leftProperty.getPreprocessedSource());
  //        }
  //        return rightOperand;
  //    }

  private void rearrangeOperands(ASTCondition condition) {
    String sharedPrepSource = condition.getLeftOperand().getPreprocessedSource();
    int indexInSourceLeft = getIndexOf(condition.getLeftOperand(), sharedPrepSource);
    int indexInSourceRight = getIndexOf(condition.getRightOperand(), sharedPrepSource);

    boolean rightOccursInSourceFirst = indexInSourceRight < indexInSourceLeft;
    if (rightOccursInSourceFirst) {
      // swap
      ASTOperandBase temp = condition.getLeftOperand();
      condition.setLeftOperand(condition.getRightOperand());
      condition.setRightOperand(temp);
    }
  }

  private int getIndexOf(ASTOperandBase leftOperand, String sharedPrepSource) {
    int index = -1;

    if (leftOperand instanceof ASTOperandProperty) {
      index =
          StringUtils.indexOfIgnoreCase(
              sharedPrepSource, ((ASTOperandProperty) leftOperand).getPathAsString());
    } else if (leftOperand instanceof ASTOperandVariable) {
      index =
          StringUtils.indexOfIgnoreCase(
              sharedPrepSource, ((ASTOperandVariable) leftOperand).getVariableName());
    }

    return index;
  }

  private ASTOperandBase createOperandBase(DataPropertyBase propertyBase, String preproSource) {
    ASTOperandBase operandBase = null;

    if (propertyBase instanceof DataProperty) {
      ASTOperandProperty operandProperty = new ASTOperandProperty();
      // todo lazevedo 14.8.19 Sources of parts need to be set
      operandProperty.setPath(
          Arrays.stream(((DataProperty) propertyBase).getFullNameAsParts())
              .map(part -> new ASTPropertyStaticPart(part))
              .collect(Collectors.toList()));
      operandProperty.setDataType(propertyBase.getType());
      operandProperty.setSource(preproSource);
      operandBase = operandProperty;
    } else if (propertyBase instanceof DataVariableReference) {
      ASTOperandVariable operandVariable = new ASTOperandVariable(propertyBase.getName());
      operandVariable.setSource(preproSource);
      operandVariable.setDataType(propertyBase.getType());
      operandVariable.setVariable(lookUpVariableByName(propertyBase.getName()));
      operandBase = operandVariable;
    }

    return operandBase;
  }

  private ASTVariable lookUpVariableByName(String name) {
    ASTVariable var = null;
    for (ASTVariable v : variableDefinitions) {
      if (v.getName().equals(name)) {
        var = v;
        break;
      }
    }
    return var;
  }
}
