package io.openvalidation.antlr.transformation.util;

import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.data.DataPropertyType;

public class FunctionWhereTypeResolver extends FunctionTypeResolverBase {
  public FunctionWhereTypeResolver(ASTOperandFunction function) {
    super(function);
  }

  @Override
  public DataPropertyType resolveType() {
    //        DataPropertyType functionReturnType = DataPropertyType.Object;
    //
    //        List<ASTOperandBase> parameters = function.getParameters();
    //
    //        if (parameters.size() > 0) {
    //            ASTOperandBase secondParam = parameters.get(1);
    //            if (secondParam instanceof ASTOperandLambdaCondition &&
    // ((ASTOperandLambdaCondition) secondParam).getCondition() != null) {
    //                List<ASTOperandProperty> properties = ((ASTOperandLambdaCondition)
    // secondParam).getCondition().getProperties();
    //                if (properties.size() > 0) {
    //                    functionReturnType = properties.get(0).getDataType();
    //                }
    //            }
    //        }
    //
    //        function.setDataType(functionReturnType);
    //        return functionReturnType;

    function.setDataType(DataPropertyType.Array);
    return DataPropertyType.Array;
  }
}
