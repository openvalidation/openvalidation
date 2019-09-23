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

package io.openvalidation.common.ast.condition;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.NumberParsingUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ASTCondition extends ASTConditionBase {
  private int indentationLevel;
  private int relativeIndentationLevel;

  private ASTOperandBase leftOperand = null;
  private ASTOperandBase rightOperand = null;
  private ASTComparisonOperator operator = null;

  private boolean isConstrainedCondition;
  private List<ASTConditionBase> unresolvedConditions = new ArrayList<>();

  public ASTCondition() {
  }

  public ASTCondition(String originalSource) {
    this._originalSource = originalSource;
  }



  public List<ASTConditionBase> getUnresolvedConditions() {
    return unresolvedConditions;
  }

  public void setUnresolvedConditions(List<ASTConditionBase> unresolvedConditions) {
    this.unresolvedConditions = unresolvedConditions;
  }

  public int getIndentationLevel() {
    return indentationLevel;
  }

  public void setIndentationLevel(int indentationLevel) {
    this.indentationLevel = indentationLevel;
  }

  public int getRelativeIndentationLevel() {
    return relativeIndentationLevel;
  }

  public void setRelativeIndentationLevel(int relativeIndentationLevel) {
    this.relativeIndentationLevel = relativeIndentationLevel;
  }

  public ASTOperandBase getLeftOperand() {
    return leftOperand;
  }

  public ASTCondition setLeftOperand(ASTOperandBase leftOperand) {
    this.leftOperand = leftOperand;
    return this;
  }

  public ASTOperandBase getRightOperand() {
    return rightOperand;
  }

  public boolean hasOperator(ASTComparisonOperator operator) {
    return operator.equals(this.operator);
  }

  public boolean hasRightOperand() {
    return getRightOperand() != null;
  }

  public boolean hasLeftOperand() {
    return getLeftOperand() != null;
  }

  public boolean hasRightAndLeftOperand() {
    return this.hasLeftOperand() && this.hasRightOperand();
  }

  public ASTCondition setRightOperand(ASTOperandBase rightOperand) {
    this.rightOperand = rightOperand;
    return this;
  }

  public String getLeftOperandValueAsString() {
    return (this.hasLeftOperand() && this.getLeftOperand().isStaticString())
        ? ((ASTOperandStaticString) this.getLeftOperand()).getValue()
        : null;
  }

  public String getRightOperandValueAsString() {
    return (this.hasRightOperand() && this.getRightOperand().isStaticString())
        ? ((ASTOperandStaticString) this.getRightOperand()).getValue()
        : null;
  }

  public ASTComparisonOperator getOperator() {
    return operator;
  }

  public boolean hasEqualityComparer() {
    return operator != null
        && (operator == ASTComparisonOperator.EQUALS
            || operator == ASTComparisonOperator.NOT_EQUALS);
  }

  public boolean hasSimpleComparisonOperator() {
    return operator != null && operator.isSimpleComparisonOperator();
  }

  public ASTCondition setOperator(ASTComparisonOperator operator) {
    this.operator = operator;
    return this;
  }

  @Override
  public List<ASTItem> children() {
    List<ASTItem> items = new ArrayList<>();
    items.add(getLeftOperand());
    items.add(getRightOperand());
    items.addAll(getUnresolvedConditions());

    return items;
  }

  @Override
  public List<ASTOperandProperty> getAllParentProperties() {
    List<ASTOperandProperty> properties = this.getPreconditionPropertiesFromOperand(leftOperand);
    properties.addAll(this.getPreconditionPropertiesFromOperand(rightOperand));

    return sortPrecoditionProperties(properties);
  }

  @Override
  public void invertOperator() {
    if (this.operator != null) this.setOperator(this.getOperator().invert());
  }

  @Override
  public List<ASTOperandProperty> getProperties() {
    List<ASTOperandProperty> props = new ArrayList<>();

    if (this.leftOperand != null) {
      List<ASTOperandProperty> properties = this.leftOperand.getProperties();
      if (properties != null && properties.size() > 0) props.addAll(properties);
    }

    if (this.rightOperand != null) {
      List<ASTOperandProperty> properties = this.rightOperand.getProperties();
      if (properties != null && properties.size() > 0) props.addAll(properties);
    }

    return props;
  }

  public List<ASTOperandVariable> getVariables() {
    List<ASTOperandVariable> vars = new ArrayList<>();

    if (this.leftOperand instanceof ASTOperandVariable) {
      vars.add((ASTOperandVariable) this.leftOperand);
    }

    if (this.rightOperand instanceof ASTOperandVariable) {
      vars.add((ASTOperandVariable) this.rightOperand);
    }

    return vars;
  }

  @Override
  public <T extends ASTItem> List<T> collectItemsOfType(Class<T> cls) {
    List<T> lst = super.collectItemsOfType(cls);

    if (this.leftOperand != null) lst.addAll(this.leftOperand.collectItemsOfType(cls));

    if (this.rightOperand != null) lst.addAll(this.rightOperand.collectItemsOfType(cls));

    return lst;
  }

  public ASTCondition resolveImplicitBooleanCondition() {
    if (operator == ASTComparisonOperator.EQUALS || operator != ASTComparisonOperator.EQUALS) {

      if (leftOperand != null
          && leftOperand.getDataType() == DataPropertyType.Boolean
          && rightOperand != null
          && rightOperand.getDataType() == DataPropertyType.String) {

        rightOperand = new ASTOperandStatic("true");
        rightOperand.setDataType(DataPropertyType.Boolean);

      } else if (leftOperand != null
          && leftOperand.getDataType() == DataPropertyType.String
          && rightOperand != null
          && rightOperand.getDataType() == DataPropertyType.Boolean) {

        leftOperand = rightOperand;
        rightOperand = new ASTOperandStatic("true");
        rightOperand.setDataType(DataPropertyType.Boolean);
      }
    }

    return this;
  }

  public boolean hasUnresolvedValues() {
    return (!isOperandNumber(leftOperand) && isOperandNumber(rightOperand))
        || (isOperandNumber(leftOperand) && !isOperandNumber(rightOperand));
  }

  public void resolveNumberValue() {
    if (this.getLeftOperand() != null
        && this.getLeftOperand().isNumber()
        && rightOperand != null
        && rightOperand instanceof ASTOperandStaticString) {
      String oprndVal = ((ASTOperandStaticString) rightOperand).getValue();
      if (NumberParsingUtils.containsNumber(oprndVal)) {

        Double value = NumberParsingUtils.extractDouble(oprndVal);
        String source = rightOperand.getPreprocessedSource();
        if (value != null) {
          rightOperand = new ASTOperandStaticNumber(value);
          rightOperand.setSource(source);
        }
      }

    } else if (this.getRightOperand() != null
        && this.getRightOperand().isNumber()
        && leftOperand != null
        && leftOperand instanceof ASTOperandStaticString) {
      String oprndVal = ((ASTOperandStaticString) leftOperand).getValue();
      if (NumberParsingUtils.containsNumber(oprndVal)) {

        Double value = NumberParsingUtils.extractDouble(oprndVal);
        String source = leftOperand.getPreprocessedSource();
        if (value != null) {
          leftOperand = new ASTOperandStaticNumber(value);
          leftOperand.setSource(source);
        }
      }
    }

    // try to find numbers on the left or right side
    if (this.getLeftOperand() != null && this.getLeftOperand().isNumber() && rightOperand == null) {
      String value =
          (this.isConstrainedCondition)
              ? this.getPreprocessedSource()
              : this.getLeftOperand().getPreprocessedSource();

      if (NumberParsingUtils.containsNumber(value)) {
        Double dbl = NumberParsingUtils.extractDouble(value);
        if (dbl != null) {
          rightOperand = new ASTOperandStaticNumber(dbl);
          rightOperand.setSource(value);
        }
      }
    }

    if (this.getRightOperand() != null
        && this.getRightOperand().isNumber()
        && leftOperand == null) {
      String value =
          (this.isConstrainedCondition)
              ? this.getPreprocessedSource()
              : this.getRightOperand().getPreprocessedSource();
      if (NumberParsingUtils.containsNumber(value)) {
        Double dbl = NumberParsingUtils.extractDouble(value);
        if (dbl != null) {
          leftOperand = new ASTOperandStaticNumber(dbl);
          leftOperand.setSource(value);
        }
      }
    }
  }

  private boolean isOperandNumber(ASTOperandBase operand) {
    return (operand != null
        && operand.isNumber()); // && ((ASTOperandStaticString)operand).getValue().contains(" ")
  }

  public boolean isConstrainedCondition() {
    return isConstrainedCondition;
  }

  public void setConstrainedCondition(boolean constrainedCondition) {
    isConstrainedCondition = constrainedCondition;
  }

  @Override
  public List<ASTCondition> getAllConditions() {
    List<ASTCondition> conditions = new ArrayList<>();
    conditions.add(this);

    return conditions;
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();

    sb.append(this.space(level) + getType() + "\n");
    sb.append(this.space(level + 1) + "INDENTATION: " + this.getIndentationLevel() + "\n");
    if (this.getConnector() != null)
      sb.append(this.space(level + 1) + "CONNECTOR: " + this.getConnector() + "\n");
    sb.append(this.space(level + 1) + "LEFT: \n");
    if (leftOperand != null) sb.append(this.leftOperand.print(level + 2));
    sb.append(this.space(level + 1) + "OPERATOR: " + this.operator + "\n");
    sb.append(this.space(level + 1) + "RIGHT: \n");
    if (rightOperand != null) sb.append(this.rightOperand.print(level + 2));

    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ASTCondition)) return false;
    ASTCondition that = (ASTCondition) o;
    return getIndentationLevel() == that.getIndentationLevel()
        && Objects.equals(getLeftOperand(), that.getLeftOperand())
        && Objects.equals(getRightOperand(), that.getRightOperand())
        && getOperator() == that.getOperator()
        && Objects.equals(getConnector(), that.getConnector())
        && Objects.equals(getPreprocessedSource(), that.getPreprocessedSource());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (getConnector() != null) {
      sb.append(getConnector()).append(" ");
    }

    sb.append("[");

    if (leftOperand != null) {
      sb.append(leftOperand.toString()).append(" ");
    }
    if (operator != null) {
      sb.append(operator).append(" ");
    }
    if (rightOperand != null) {
      sb.append(rightOperand.toString()).append(" ");
    }
    return sb.toString().replaceAll("([ ]+)$", "] ");
  }
}
