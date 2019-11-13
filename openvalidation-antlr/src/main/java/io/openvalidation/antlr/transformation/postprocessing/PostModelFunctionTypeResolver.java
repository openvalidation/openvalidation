package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.antlr.transformation.util.FunctionUtils;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.operand.ASTOperandFunction;

import java.util.function.Predicate;

public class PostModelFunctionTypeResolver extends PostProcessorSubelementBase<ASTModel, ASTOperandFunction>{
    @Override
    protected Predicate<? super ASTOperandFunction> getFilter() {
        return x -> true;
    }

    @Override
    protected void processItem(ASTOperandFunction item) {
        FunctionUtils.resolveFunctionReturnType(item);
    }
}
