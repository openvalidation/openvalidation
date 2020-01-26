package io.openvalidation.common.ast.operand;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataSemanticOperator;
import io.openvalidation.common.utils.NumberParsingUtils;

public class ASTSemanticOperator extends ASTOperandBase {
  private DataSemanticOperator _semanticOperator;
  private ASTOperandBase _operand;
  private ASTOperandBase _secondOperand;

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

  public ASTOperandBase getSecondOperand() {
    return _secondOperand;
  }

  public void setSecondOperand(ASTOperandBase secondOperand, String value, String source) {

    if (secondOperand == null) {
      this._secondOperand = new ASTOperandStaticString(value);
      this._secondOperand.setSource(source);
    }

    if (this.getOperand().isNumber() && this._secondOperand.isStaticString()) {
      String strval = ((ASTOperandStaticString) this._secondOperand).getValue();
      if (NumberParsingUtils.containsNumber(strval)) {
        Double dval = NumberParsingUtils.extractDouble(strval);

        ASTOperandStaticNumber sop = new ASTOperandStaticNumber(dval);
        sop.setSource(this._secondOperand.getOriginalSource());
        this._secondOperand = sop;
      }
    }
  }
}
