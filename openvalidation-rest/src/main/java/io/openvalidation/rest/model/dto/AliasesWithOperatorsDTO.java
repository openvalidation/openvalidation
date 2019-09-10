package io.openvalidation.rest.model.dto;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.core.Aliases;
import java.util.HashMap;
import java.util.Map;

public class AliasesWithOperatorsDTO {
  private Map<String, String> aliases;
  private Map<ASTComparisonOperator, DataPropertyType> operators;

  public AliasesWithOperatorsDTO(String culture) {
    aliases = Aliases.getAvailableAliases(culture);
    operators = new HashMap<>();

    for (ASTComparisonOperator day : ASTComparisonOperator.values()) {
      operators.put(day, day.validDataType());
    }
  }

  public Map<String, String> getAliases() {
    return aliases;
  }

  public Map<ASTComparisonOperator, DataPropertyType> getOperators() {
    return operators;
  }
}
