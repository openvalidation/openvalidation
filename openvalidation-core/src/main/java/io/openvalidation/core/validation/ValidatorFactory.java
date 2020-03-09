/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.openvalidation.core.validation;

import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandArray;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmetical;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalItemBase;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalOperation;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.core.validation.operand.arithmetical.ASTArithmeticalOperationValidator;
import io.openvalidation.core.validation.operand.arithmetical.ASTArithmeticalValidator;

public class ValidatorFactory {
  public static ValidatorBase Create(ASTItem item) throws Exception {

    if (item instanceof ASTRule) return new ASTRuleValidator((ASTRule) item);
    if (item instanceof ASTActionError) return new ASTErrorValidator((ASTActionError) item);
    if (item instanceof ASTConditionGroup)
      return new ASTConditionGroupValidator((ASTConditionGroup) item);
    if (item instanceof ASTCondition) return new ASTConditionValidator((ASTCondition) item);
    if (item instanceof ASTVariable) return new ASTVariableValidator((ASTVariable) item);

    if (item instanceof ASTOperandArithmetical)
      return new ASTArithmeticalValidator((ASTOperandArithmetical) item);
    if (item instanceof ASTOperandArithmeticalOperation)
      return new ASTArithmeticalOperationValidator((ASTOperandArithmeticalOperation) item);

    if (item instanceof ASTOperandVariable)
      return new ASTOperandVariableValidator((ASTOperandVariable) item);

    if (item instanceof ASTOperandFunction)
      return new ASTOperandFunctionValidator((ASTOperandFunction) item);
    if (item instanceof ASTOperandLambdaCondition)
      return new ASTOperandLambdaConditionValidator((ASTOperandLambdaCondition) item);
    if (item instanceof ASTOperandArray)
      return new ASTOperandArrayValidator((ASTOperandArray) item);

    if (item instanceof ASTUnknown) return new ASTUnknownValidator((ASTUnknown) item);

    if (item instanceof ASTComment) return new EmptyValidator();
    if (item instanceof ASTOperandBase) return new EmptyValidator();
    if (item instanceof ASTOperandArithmeticalItemBase) return new EmptyValidator();

    throw new OpenValidationException("No Validator for type : " + item.getType() + " found");
  }
}
