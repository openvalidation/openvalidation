package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;
import java.util.List;

public class FunctionWhereValidator extends FunctionValidatorBase {

  public FunctionWhereValidator(ASTOperandFunction function) {
    this.function = function;
  }

  @Override
  public void validate() throws Exception {
    List<ASTOperandBase> parameters = function.getParameters();

    if (parameters.size() != 2)
      throw new ASTValidationException(
          "Invalid number of parameters. The function "
              + function.getName()
              + "needs an array property/variable and a condition as a parameter",
          function);
    else {
      ASTOperandBase firstParam = parameters.get(0);
      if (!(firstParam instanceof ASTOperandProperty
          || firstParam instanceof ASTOperandFunction
          || firstParam instanceof ASTOperandVariable)) {
        throw new ASTValidationException(
            "The first parameter of the function "
                + function.getName()
                + " has to be an array property or a nested function. Currently applied on "
                + firstParam.getClass().getSimpleName(),
            function);
      } else {
        if (firstParam.getDataType() != DataPropertyType.Array) {
          throw new ASTValidationException(
              "The first parameter of the function "
                  + function.getName()
                  + " has to be an array property of type 'Array'. Type found: "
                  + firstParam.getDataType(),
              function);
        }
      }

      ASTOperandBase secondParam = parameters.get(1);
      if (!(secondParam instanceof ASTOperandLambdaCondition)) {
        throw new ASTValidationException(
            "The second parameter of the function "
                + function.getName()
                + " has to be a condition. Found: "
                + secondParam.getClass().getSimpleName(),
            function);
      }
    }
    validateParameters();
  }
}
