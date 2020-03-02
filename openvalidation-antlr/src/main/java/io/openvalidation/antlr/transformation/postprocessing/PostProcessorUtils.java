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

  public static ASTOperandArray resolveArrayFromString(ASTOperandStaticString operand) {
    return resolveArrayFromString(operand, DataPropertyType.Decimal);
  }

  public static ASTOperandArray resolveArrayFromString(
      ASTOperandStaticString stringOperand, DataPropertyType resolutionType) {
    ASTOperandArray resultArray = null;
    String val = stringOperand.getValue();

    if (val != null && isParsableAsArray(val)) {
      val = StringUtils.trimSpecialChars(val);

      resultArray = new ASTOperandArray();
      resultArray.setContentType(resolutionType);

      for (String delimiter : Constants.ARRAY_DELIMITER_ALIASES) {
        val = val.replaceAll(" " + delimiter + " ", ",");
      }

      for (String v : val.split(",")) {
        ASTOperandBase extractedItem = resolveArrayElementString(v, resolutionType);
        resultArray.add(extractedItem);
      }

      resultArray.setSource(stringOperand.getPreprocessedSource());
    }

    return resultArray;
  }

  public static boolean isParsableAsArray(String s) {
    String cleanedString = StringUtils.trimSpecialChars(s);
    return cleanedString.contains(",");
  }

  public static boolean isParsableAsArray(ASTOperandStaticString staticString) {
    return isParsableAsArray(staticString.getValue());
  }
}
