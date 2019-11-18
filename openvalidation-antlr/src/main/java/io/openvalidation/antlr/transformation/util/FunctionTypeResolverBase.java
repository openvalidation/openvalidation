package io.openvalidation.antlr.transformation.util;

import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.data.DataPropertyType;

public abstract class FunctionTypeResolverBase {

  protected ASTOperandFunction function;

  public FunctionTypeResolverBase(ASTOperandFunction function) {
    this.function = function;
  }

  public abstract DataPropertyType resolveType();
}
