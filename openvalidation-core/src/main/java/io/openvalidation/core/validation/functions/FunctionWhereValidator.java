package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.utils.StringUtils;
import java.util.List;

public class FunctionWhereValidator extends FunctionValidatorBase {

  public FunctionWhereValidator(ASTOperandFunction function) {
    this.function = function;
  }

  @Override
  public void validateFunction() throws Exception {
    /*
     parameters:
       1. ASTOperandProperty/ASTOperandVariable/ASTOperandFunction(WHERE) of type Array
       2. ASTOperandLambdaCondition
    */
    List<ASTOperandBase> parameters = function.getParameters();

    if (parameters.size() < 2)
      throw new ASTValidationException(
          "Invalid number of parameters. The function "
              + function.getName()
              + "needs 2 parameters but has"
              + parameters.size()
              + ". The first has to be a "
              + StringUtils.getUserFriendlyClassName(ASTOperandProperty.class)
              + "/"
              + StringUtils.getUserFriendlyClassName(ASTOperandVariable.class)
              + " of type Array and the second a "
              + StringUtils.getUserFriendlyClassName(ASTOperandLambdaCondition.class)
              + ".",
          function);
    else {
      ASTOperandBase firstParam = parameters.get(0);
      String firstParamClassName = StringUtils.getUserFriendlyClassName(firstParam);
      if (!(firstParam instanceof ASTOperandProperty
          || firstParam instanceof ASTOperandFunction
          || firstParam instanceof ASTOperandVariable)) {
        throw new ASTValidationException(
            "The first parameter of the function "
                + function.getName()
                + " has to be an array property or a nested function. Currently applied on "
                + firstParamClassName,
            function);
      } else {
        if (firstParam.getDataType() != DataPropertyType.Array) {
          throw new ASTValidationException(
              "The first parameter ("
                  + firstParamClassName
                  + ") of "
                  + function.getName()
                  + " has to be of type 'Array'. Type found: "
                  + firstParam.getDataType()
                  + ".",
              function);
        }
      }

      ASTOperandBase secondParam = parameters.get(1);
      String secondParamClassName = StringUtils.getUserFriendlyClassName(secondParam);
      if (!(secondParam instanceof ASTOperandLambdaCondition)) {
        throw new ASTValidationException(
            "The second parameter of the function "
                + function.getName()
                + " has to be a condition. Found: "
                + secondParamClassName,
            function);
      }
    }
  }
}
