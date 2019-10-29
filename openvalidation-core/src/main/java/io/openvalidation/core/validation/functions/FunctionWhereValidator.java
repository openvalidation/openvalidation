package io.openvalidation.core.validation.functions;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.exceptions.ASTValidationException;

import java.util.List;

public class FunctionWhereValidator extends FunctionValidatorBase{

    public FunctionWhereValidator(ASTOperandFunction function)
    {
        this.function = function;
    }

    @Override
    public void validate() throws Exception {
        List<ASTOperandBase> parameters = function.getParameters();

        throw new ASTValidationException("Validator for WHERE not implemented", function);

        //validateParameters();
    }
}
