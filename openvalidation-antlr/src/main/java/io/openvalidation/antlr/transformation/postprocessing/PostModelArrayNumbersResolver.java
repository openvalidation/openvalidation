package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.builder.ASTOperandArrayBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.NumberParsingUtils;
import java.util.function.Predicate;

public class PostModelArrayNumbersResolver
    extends PostProcessorSubelementBase<ASTModel, ASTCondition> {
  @Override
  protected Predicate<? super ASTCondition> getFilter() {
    return c ->
        c.hasLeftOperand()
            && c.getLeftOperand().getDataType() == DataPropertyType.Decimal
            && c.getRightOperand() instanceof ASTOperandArray
            && ((ASTOperandArray) c.getRightOperand()).getContentType() == DataPropertyType.Unknown;
  }

  @Override
  protected void processItem(ASTCondition item) {
    ASTOperandArrayBuilder arrayBuilder = new ASTOperandArrayBuilder(null);
    arrayBuilder.create();

    ASTOperandArray rightArray = (ASTOperandArray) item.getRightOperand();

    for (ASTOperandBase base : rightArray.getItems()) {
      if (base.getDataType() == DataPropertyType.Decimal) {
        arrayBuilder.addItem(base);
      } else if (base instanceof ASTOperandStaticString) {
        String s = ((ASTOperandStaticString) base).getValue();
        Double number = NumberParsingUtils.extractNumber(s);

        boolean numberExtracted = number != null;
        if (numberExtracted) {
          ASTOperandStaticNumber num = new ASTOperandStaticNumber(number);
          num.setSource(item.getOriginalSource());
          arrayBuilder.addItem(num);
        } else {
          // at least one operand not parsable as a numerical value
          return;
        }
      } else {
        // if the array not only contains ASTOperandStaticStrings and decimal types
        return;
      }
    }

    arrayBuilder.withSource(rightArray.getOriginalSource());
    item.setRightOperand(arrayBuilder.getModel());
  }
}
