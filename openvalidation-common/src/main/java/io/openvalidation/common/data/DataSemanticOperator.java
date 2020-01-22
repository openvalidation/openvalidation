package io.openvalidation.common.data;

import io.openvalidation.common.ast.ASTComparisonOperator;

public class DataSemanticOperator extends DataPropertyBase {

  private ASTComparisonOperator _operator;
  private String _operandName;

  public DataSemanticOperator(
      String name, String operandName, DataPropertyType type, ASTComparisonOperator operator) {
    super(name, type);
    this._operandName = (operandName != null) ? operandName.trim() : "";
    this._operator = operator;
  }

  public ASTComparisonOperator getOperator() {
    return _operator;
  }

  public String getOperandName() {
    return this._operandName;
  }
}
