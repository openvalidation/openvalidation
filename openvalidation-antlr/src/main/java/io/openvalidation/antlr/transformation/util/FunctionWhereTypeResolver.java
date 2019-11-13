package io.openvalidation.antlr.transformation.util;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;

import java.util.List;

public class FunctionWhereTypeResolver extends FunctionTypeResolverBase {
    public FunctionWhereTypeResolver(ASTOperandFunction function) {
        super(function);
    }

    @Override
    public DataPropertyType resolveType() {
        DataPropertyType functionReturnType = DataPropertyType.Object;

        List<ASTOperandBase> parameters = function.getParameters();

        if (function.getName().equals("WHERE")) {
            if (parameters.size() > 0) {
                ASTOperandBase secondParam = parameters.get(1);
                if (secondParam instanceof ASTOperandLambdaCondition && ((ASTOperandLambdaCondition) secondParam).getCondition() != null) {
                    List<ASTOperandProperty> properties = ((ASTOperandLambdaCondition) secondParam).getCondition().getProperties();
                    if(properties.size() > 0)
                    {
                        functionReturnType = properties.get(0).getDataType();
                    }
                }
            }
        }

        function.setDataType(functionReturnType);
        return functionReturnType;
    }
}
