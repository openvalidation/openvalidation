package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.NumberParsingUtils;
import io.openvalidation.common.utils.StringUtils;

public class PostProcessorUtils {
  public static ASTOperandStatic resolveArrayElementString(
      String content, DataPropertyType resType) {
    ASTOperandStatic resolvedOp =
        new ASTOperandStaticString(StringUtils.stripSpecialWords(content));

    if (!content.isEmpty()) {
      if (resType == DataPropertyType.Decimal) {
        Double number = NumberParsingUtils.extractDouble(content);
        if (number != null) resolvedOp = new ASTOperandStaticNumber(number);
      }
    }

    resolvedOp.setSource(content);
    return resolvedOp;
  }
}
