package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;

import java.util.List;

public class FunctionTakeValidator extends FunctionValidatorBase {

    public FunctionTakeValidator(ASTOperandFunction function) {
        this.function = function;
    }

    @Override
    public void validate() throws Exception {
        List<ASTOperandBase> parameters = function.getParameters();

        if (parameters.isEmpty() || parameters.size() == 1)
            throw new ASTValidationException(
                    "The function " + function.getName() + " requires at least two parameters", function);
        else {
            ASTOperandBase firstParam = parameters.get(0);

            if (parameters.size() <= 3) {
                //assert first parameter (the array/function)
                if (!(firstParam instanceof ASTOperandProperty)
                        && !(firstParam instanceof ASTOperandFunction)) {
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
                // TAKE [array/function] [amount]
                if (parameters.size() == 2) {
                    ASTOperandBase secondParam = parameters.get(1);
                    if (!(secondParam instanceof ASTOperandStaticNumber)) {
                        throw new ASTValidationException(
                                "The function "
                                        + function.getName()
                                        + " takes an integer as the second parameter. Current second parameter is "
                                        + secondParam.getClass().getSimpleName(),
                                function);
                    }
                }
                // TAKE [array/function] [condition] [amount]
                else {
                    ASTOperandBase secondParam = parameters.get(1);
                    if (!(secondParam instanceof ASTOperandLambdaCondition)) {
                        throw new ASTValidationException(
                                "The function "
                                        + function.getName()
                                        + " takes a lambda condition as the second parameter. Current second parameter is "
                                        + secondParam.getClass().getSimpleName(),
                                function);
                    }

                    ASTOperandBase thirdParam = parameters.get(2);
                    if (!(thirdParam instanceof ASTOperandStaticNumber)) {
                        throw new ASTValidationException(
                                "The function "
                                        + function.getName()
                                        + " takes an integer as the third parameter. Current third parameter is "
                                        + thirdParam.getClass().getSimpleName(),
                                function);
                    }
                }
            }
        }

        validateParameters();
    }
}
