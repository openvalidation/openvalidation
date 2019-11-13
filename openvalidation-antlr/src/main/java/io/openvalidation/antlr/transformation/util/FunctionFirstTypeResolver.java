package io.openvalidation.antlr.transformation.util;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;

import java.util.List;

public class FunctionFirstTypeResolver extends FunctionTypeResolverBase {

    public FunctionFirstTypeResolver(ASTOperandFunction function) {
        super(function);
    }

    @Override
    public DataPropertyType resolveType() {
        DataPropertyType functionReturnType = DataPropertyType.Object;
        List<ASTOperandBase> parameters = function.getParameters();

        if (parameters.size() > 0) {
            ASTOperandBase firstParam = parameters.get(0);
            if (firstParam instanceof ASTOperandProperty && firstParam.getDataType() == DataPropertyType.Array) {
                functionReturnType = ((ASTOperandProperty) firstParam).getArrayContentType();
            } else if (firstParam instanceof ASTOperandFunction) {
                functionReturnType = FunctionUtils.resolveFunctionReturnType((ASTOperandFunction) firstParam);
            }
        }

        function.setDataType(functionReturnType);
        return functionReturnType;
    }
}
