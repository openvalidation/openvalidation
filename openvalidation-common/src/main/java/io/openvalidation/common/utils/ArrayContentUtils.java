package io.openvalidation.common.utils;

import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.data.DataPropertyType;

public class ArrayContentUtils {
  public static ASTOperandStatic resolveStaticArrayContent(
      String source, DataPropertyType resType) {
    ASTOperandStatic resolvedOp = new ASTOperandStaticString(StringUtils.stripSpecialWords(source));

    if (!source.isEmpty()) {
      if (resType == DataPropertyType.Decimal) {
        Double number = NumberParsingUtils.extractDouble(source);
        if (number != null) resolvedOp = new ASTOperandStaticNumber(number);
      }
    }

    resolvedOp.setSource(source);
    return resolvedOp;
  }
}
