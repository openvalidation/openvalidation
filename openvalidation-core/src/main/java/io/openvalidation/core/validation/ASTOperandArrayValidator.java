package io.openvalidation.core.validation;

import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ASTOperandArrayValidator extends ValidatorBase {

  private ASTOperandArray array;

  public ASTOperandArrayValidator(ASTOperandArray array) {
    this.array = array;
  }

  @Override
  public void validate() throws Exception {
    // validate structure
    boolean arrElemsHaveDiffTypes = array.getContentType() == DataPropertyType.Unknown;
    if (arrElemsHaveDiffTypes) {
      Set<DataPropertyType> foundTypes = new HashSet<>();
      for (ASTOperandBase base : array.getItems()) {
        foundTypes.add(base.getDataType());
      }
      String foundTypesString = buildFoundTypeListString(foundTypes);

      throw new ASTValidationException(
          "Array contains elements of different types when it is supposed to only contain elements of a singular type. Found types "
              + foundTypesString,
          array);
    }

    // validate items of array
    for (int i = 0; i < array.getItems().size(); i++) {
      ASTOperandBase item = array.getItems().get(i);
      validate(item);
    }
  }

  private String buildFoundTypeListString(Set<DataPropertyType> types) {
    StringBuilder sb = new StringBuilder("[");
    Iterator<DataPropertyType> it = types.iterator();

    if (it.hasNext()) {
      sb.append(it.next());

      while (it.hasNext()) {
        sb.append(", ").append(it.next());
      }
    }
    sb.append("]");

    return sb.toString();
  }
}
