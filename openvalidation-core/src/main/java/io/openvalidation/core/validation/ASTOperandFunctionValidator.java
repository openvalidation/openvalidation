package io.openvalidation.core.validation;

import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.core.validation.functions.FunctionFirstValidatorBase;
import io.openvalidation.core.validation.functions.FunctionLastValidatorBase;
import io.openvalidation.core.validation.functions.FunctionValidatorBase;
import io.openvalidation.core.validation.functions.FunctionTakeValidatorBase;

public class ASTOperandFunctionValidator extends ValidatorBase {
  private ASTOperandFunction function;

  public ASTOperandFunctionValidator(ASTOperandFunction function) {
    this.function = function;
  }

  @Override
  public void validate() throws Exception {
    if (function.getName() == null || function.getName().isEmpty())
      throw new ASTValidationException("The function must have a name", function);

    //validate the specific function
    FunctionValidatorBase subFunctionValidator = createFunctionSubValidator();
    subFunctionValidator.setContext(this.context);
    subFunctionValidator.validate();
  }

  private FunctionValidatorBase createFunctionSubValidator() throws Exception
  {
    switch (function.getName())
    {
      case "FIRST": return new FunctionFirstValidatorBase(function);
      case "LAST": return new FunctionLastValidatorBase(function);
      case "TAKE": return new FunctionTakeValidatorBase(function);
      default: throw new ASTValidationException("Function with name '"+ function.getName() +"' is not known", function);
    }
  }
}
