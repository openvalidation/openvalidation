package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;

import java.util.List;

public class FunctionFirstValidator extends FunctionValidatorBase {

  public FunctionFirstValidator(ASTOperandFunction function) {
    this.function = function;
  }

  @Override
  public void validateFunction() throws Exception {
    List<ASTOperandBase> parameters = function.getParameters();

    if (parameters.isEmpty())
      throw new ASTValidationException(
          "The function " + function.getName() + " requires at least one parameter", function);
    else if (parameters.size() <= 2) {
      // validate first parameter
      // FIRST [array/function/variable]
      ASTOperandBase firstParam = parameters.get(0);
      if (!(firstParam instanceof ASTOperandProperty)
          && !(firstParam instanceof ASTOperandFunction)
          && !(firstParam instanceof ASTOperandVariable)) {
        throw new ASTValidationException(
            "The function "
                + function.getName()
                + " has to be applied on an array property or a nested function. Currently applied on "
                + firstParam.getClass().getSimpleName(),
            function);
      } else if (firstParam.getDataType() != DataPropertyType.Array) {
        throw new ASTValidationException(
            "The function "
                + function.getName()
                + " has to be applied on a property of type 'Array'. But is applied on property of type '"
                + firstParam.getDataType()
                + "'",
            function);
      }
      // FIRST [array/function/variable] [amount]
      if (parameters.size() == 2) {
        ASTOperandBase secondParam = parameters.get(1);
        if (!(secondParam instanceof ASTOperandStaticNumber)) {
          throw new ASTValidationException(
              "The function "
                  + function.getName()
                  + " either takes a number or a lambda condition as the second parameter. Current second parameter is "
                  + secondParam.getClass().getSimpleName(),
              function);
        }
        else if(((ASTOperandStaticNumber) secondParam).getNumberValue() < 1){
          throw new ASTValidationException(
              "The function "
                  + function.getName()
                  + " only takes numbers with a value greater or equal to 1 as the second parameter. Current value is "
                  + ((ASTOperandStaticNumber) secondParam).getNumberValue(),
              function);
        }
      }

    } else {
      throw new ASTValidationException(
          "Invalid number of parameters (" + parameters.size() + ")", function);
    }
  }
}
