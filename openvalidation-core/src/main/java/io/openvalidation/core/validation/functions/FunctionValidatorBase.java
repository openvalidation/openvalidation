package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.core.validation.ValidatorBase;
import java.util.List;

public abstract class FunctionValidatorBase extends ValidatorBase {
  protected ASTOperandFunction function;

  @Override
  public void validate() throws Exception {
    validateFunction();
    // calls validation on nested functions etc. in the current function's parameter list
    validateParameters();
  }

  public abstract void validateFunction() throws Exception;

  protected void validateParameters() throws Exception {
    // validate the parameters
    List<ASTOperandBase> parameters = function.getParameters();

    for (ASTOperandBase operandBase : parameters) {
      validate(operandBase);
    }
  }
}
