package io.openvalidation.core.validation;

import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.core.validation.functions.*;

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
//    FunctionValidatorBase subFunctionValidator = createFunctionSubValidator();
    ValidatorBase subFunctionValidator = createFunctionSubValidator();
    subFunctionValidator.setContext(this.context);
    subFunctionValidator.validate();
  }

//  private FunctionValidatorBase createFunctionSubValidator() throws Exception
  private ValidatorBase createFunctionSubValidator() throws Exception
  {
    switch (function.getName())
    {
      case "FIRST": return new FunctionFirstValidator(function);
      case "LAST": return new FunctionLastValidator(function);
      case "TAKE": return new FunctionTakeValidator(function);
      //todo add possibility of where function in sum_of to validator (third arg)
      case "SUM_OF": return new FunctionSumOfValidator(function);
      case "GET_ARRAY_OF": return new FunctionGetArrayOfValidator(function);
      case "WHERE" : return new FunctionWhereValidator(function);
      default:
        throw new ASTValidationException("Function with name '"+ function.getName() +"' is not known", function);
//        System.out.println("Nich jefundn");
//        return new EmptyValidator();
    }
  }
}
