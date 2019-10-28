package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaProperty;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;

import java.util.List;

public class FunctionGetArrayOfValidator extends FunctionValidatorBase {

    public FunctionGetArrayOfValidator(ASTOperandFunction function) {
        this.function = function;
    }

    @Override
    public void validate() throws Exception {

        List<ASTOperandBase> parameters = function.getParameters();

        if (parameters.size() != 2) {
            throw new ASTValidationException(
                    "Incorrect parameter. The function " + function.getName() + " requires a property parameter formatted like this: '[array property containing objects].[attribute]' (where attribute is a property of each object contained in the array)" +
                            "\n<TECHNICAL ERROR> ARGUMENTS REQUIRED: 2, FOUND: " + parameters.size(), function);
        } else {
            ASTOperandBase firstParam = parameters.get(0);
            if (!(firstParam instanceof ASTOperandProperty || firstParam instanceof ASTOperandFunction)) {
                throw new ASTValidationException("The first parameter of the function "
                        + function.getName()
                        + " has to be an array property or a nested function. Currently applied on "
                        + firstParam.getClass().getSimpleName(), function);
            } else {
                if (firstParam.getDataType() != DataPropertyType.Array) {
                    throw new ASTValidationException("The first parameter of the function "
                            + function.getName()
                            + " has to be an array property of type 'Array'. Type found: " + firstParam.getDataType(), function);
                }
            }


            //GET ARRAY OF [array property].[subproperty of array property]
            //e.g. SUM OF persons.age (where persons is an array with objects, each containing an age)
            ASTOperandBase secondParam = parameters.get(1);
            if (!(secondParam instanceof ASTOperandLambdaProperty))
                throw new ASTValidationException("The second parameter of the function "
                        + function.getName()
                        + " has to be a subproperty of the objects contained in the first parameter", function);
            else {
                if (((ASTOperandLambdaProperty) secondParam).getProperty().getDataType() != DataPropertyType.Decimal)
                    throw new ASTValidationException("The second parameter of the function "
                            + function.getName()
                            + " has to be of type 'Decimal'. Type found: " + firstParam.getDataType(), function);
            }

        }
    }
}
