package io.openvalidation.common.ast.operand;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataSemanticOperator;

public class ASTSemanticOperator extends ASTOperandBase {
  private DataSemanticOperator _semanticOperator;
  private ASTOperandBase _operand;

  public ASTSemanticOperator(DataSemanticOperator operator, ASTOperandBase operand) {
    this._semanticOperator = operator;
    this._operand = operand;
  }

  public DataSemanticOperator getSemanticOperator() {
    return this._semanticOperator;
  }

  public ASTComparisonOperator getOperator() {
    return this.getSemanticOperator() != null ? this.getSemanticOperator().getOperator() : null;
  }

  public ASTOperandBase getOperand() {
    return this._operand;
  }

  public String getOperandName() {
    return this.getSemanticOperator() != null ? this.getSemanticOperator().getOperandName() : null;
  }
}
