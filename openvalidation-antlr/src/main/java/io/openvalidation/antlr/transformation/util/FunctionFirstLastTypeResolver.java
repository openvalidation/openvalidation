package io.openvalidation.antlr.transformation.util;

import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;
import java.util.List;

public class FunctionFirstLastTypeResolver extends FunctionTypeResolverBase {

  public FunctionFirstLastTypeResolver(ASTOperandFunction function) {
    super(function);
  }

  @Override
  public DataPropertyType resolveType() {
    DataPropertyType functionReturnType = DataPropertyType.Unknown;
    List<ASTOperandBase> parameters = function.getParameters();

    if (parameters.size() == 2) {
      ASTOperandBase secondParam = parameters.get(1);
      // if number param is 1 return type of contained elements
      if (secondParam instanceof ASTOperandStaticNumber
          && ((ASTOperandStaticNumber) secondParam).getNumberValue() == 1) {
        ASTOperandBase firstParam = parameters.get(0);
        if (firstParam instanceof ASTOperandProperty
            && firstParam.getDataType() == DataPropertyType.Array) {
          functionReturnType = ((ASTOperandProperty) firstParam).getArrayContentType();
        } else if (firstParam instanceof ASTOperandFunction) {
          functionReturnType =
              ((ASTOperandFunction) firstParam)
                  .getArrayContentType(); // FunctionUtils.resolveFunctionReturnType((ASTOperandFunction) firstParam);
        }
      }
      // else return type Array
      else {
        functionReturnType = DataPropertyType.Array;
      }
    } else {
      if (parameters.size() == 1) {
        ASTOperandBase firstParam = parameters.get(0);
        if (firstParam instanceof ASTOperandProperty
            && firstParam.getDataType() == DataPropertyType.Array) {
          functionReturnType = ((ASTOperandProperty) firstParam).getArrayContentType();
        } else if (firstParam instanceof ASTOperandFunction) {
          functionReturnType =
              ((ASTOperandFunction) firstParam)
                  .getArrayContentType(); // FunctionUtils.resolveFunctionReturnType((ASTOperandFunction) firstParam);
        } else if (firstParam instanceof ASTOperandVariable && firstParam.getDataType() == DataPropertyType.Array) {
          functionReturnType = ((ASTOperandVariable) firstParam).getArrayContentType();
        }
      }
    }

    function.setDataType(functionReturnType);
    return functionReturnType;
  }
}
