package io.openvalidation.core.validation;

import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.ASTValidationException;

public class ASTOperandArrayValidator extends ValidatorBase {

    private ASTOperandArray array;

    public ASTOperandArrayValidator(ASTOperandArray array) {
        this.array = array;
    }

    @Override
    public void validate() throws Exception {

      DataPropertyType arrayContentType = array.getContentType();

      for(int i = 0; i < array.getItems().size(); i++)
      {
        ASTOperandBase item = array.getItems().get(i);
          if(item.getDataType() != arrayContentType)
          {
            throw new ASTValidationException("The type of the element(" + item.getDataType() + ") does not match the type of the array(" + arrayContentType + ")", item, i);
          }
      }

      //validate items of array
      for(int i = 0; i < array.getItems().size(); i++)
      {
        ASTOperandBase item = array.getItems().get(i);
        validate(item);
      }
    }
}
