package io.openvalidation.core.validation;

import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.exceptions.ASTValidationException;

public class ASTOperandLambdaConditionValidator extends ValidatorBase {

  private ASTOperandLambdaCondition lambdaCondition;

  public ASTOperandLambdaConditionValidator(ASTOperandLambdaCondition lambdaCondition) {
    this.lambdaCondition = lambdaCondition;
  }

  @Override
  public void validate() throws Exception {
    if (lambdaCondition.getLambdaToken() == null || lambdaCondition.getLambdaToken().isEmpty()) {
      throw new ASTValidationException(
          "A lambda condition requires a lambda token. Current token has value '"
              + lambdaCondition.getLambdaToken()
              + "'",
          lambdaCondition);
    }

    if (lambdaCondition.getOperand() == null) {
      throw new ASTValidationException("A lambda condition requires an operand", lambdaCondition);
    } else if (!(lambdaCondition.getOperand() instanceof ASTConditionBase)) {
      throw new ASTValidationException(
          "A lambda condition requires an instance of type ASTConditionBase as the operator. Current operand is instance of type '"
              + lambdaCondition.getOperand().getClass().getSimpleName()
              + "'",
          lambdaCondition);
    } else {
      validate(lambdaCondition.getOperand());
    }
  }
}
