package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.operand.*;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.Constants;
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

  public static ASTOperandArray resolveArrayInOperand(ASTOperandBase operand) {
    return resolveArrayInOperand(operand, DataPropertyType.Decimal);
  }

  public static ASTOperandArray resolveArrayInOperand(
      ASTOperandBase operand, DataPropertyType resolutionType) {
    ASTOperandArray resultArray = null;

    if (operand != null && operand instanceof ASTOperandStaticString) {
      String val = ((ASTOperandStaticString) operand).getValue();

      if (!StringUtils.isNullOrEmpty(val)) {
        val = StringUtils.trimSpecialChars(val);

        if (val.contains(",")) {
          resultArray = new ASTOperandArray();
          resultArray.setContentType(resolutionType);

          for (String delimiter : Constants.ARRAY_DELIMITER_ALIASES) {
            val = val.replaceAll(" " + delimiter + " ", ",");
          }

          for (String v : val.split(",")) {
            ASTOperandBase extractedItem = resolveArrayElementString(v, resolutionType);
            resultArray.add(extractedItem);
          }

          resultArray.setSource(operand.getPreprocessedSource());
        }
      }
    }

    return resultArray;
  }
}
