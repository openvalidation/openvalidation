package io.openvalidation.antlr.transformation.util;

import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.data.DataPropertyType;

public class FunctionUtils {

    public static DataPropertyType resolveFunctionReturnType(ASTOperandFunction function) {
        FunctionTypeResolverBase typeResolver = createFunctionTypeResolver(function);
        return typeResolver.resolveType();
    }

    private static FunctionTypeResolverBase createFunctionTypeResolver(ASTOperandFunction function) {
        switch (function.getName()) {
            case "FIRST":
                return new FunctionFirstTypeResolver(function);
            case "LAST":
                return new FunctionLastTypeResolver(function);
//            case "TAKE":
//                return new FunctionTakeValidator(function);
//            //todo add possibility of where function in sum_of to validator (third arg)
//            case "SUM_OF":
//                return new FunctionSumOfValidator(function);
//            case "GET_ARRAY_OF":
//                return new FunctionGetArrayOfValidator(function);
            case "WHERE":
                return new FunctionWhereTypeResolver(function);
            default:
                return new FunctionTypeResolverDefault(function);
        }
    }
}
