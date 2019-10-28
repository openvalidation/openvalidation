package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaProperty;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;

import java.util.List;

public class FunctionSumOfValidator extends FunctionValidatorBase {

    public FunctionSumOfValidator(ASTOperandFunction function) {
        this.function = function;
    }

    @Override
    public void validate() throws Exception {
        List<ASTOperandBase> parameters = function.getParameters();

        if (parameters.size() != 1)
            throw new ASTValidationException("The function '" + function.getName() + "' takes exactly one parameter. Found " + parameters.size() + " parameters", function);

        else {
            //SUM OF [array property]
            ASTOperandBase firstParam = parameters.get(0);
            if (!(firstParam instanceof ASTOperandProperty || firstParam instanceof ASTOperandFunction || firstParam instanceof ASTOperandVariable))
                throw new ASTValidationException("The first parameter of the function "
                        + function.getName()
                        + " has to be an array property or a nested function. Currently applied on "
                        + firstParam.getClass().getSimpleName(), function);
            else if (firstParam.getDataType() != DataPropertyType.Array) {
                throw new ASTValidationException("The first parameter of the function "
                        + function.getName()
                        + " has to be an array property of type 'Array'. Type found: " + firstParam.getDataType(), function);
            }
        }

        validateParameters();
    }
}
