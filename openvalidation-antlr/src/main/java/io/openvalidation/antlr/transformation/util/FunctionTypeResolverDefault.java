package io.openvalidation.antlr.transformation.util;

import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.data.DataPropertyType;

public class FunctionTypeResolverDefault extends FunctionTypeResolverBase {

    public FunctionTypeResolverDefault(ASTOperandFunction function) {
        super(function);
    }

    @Override
    public DataPropertyType resolveType() {
        return function.getDataType();
    }
}
