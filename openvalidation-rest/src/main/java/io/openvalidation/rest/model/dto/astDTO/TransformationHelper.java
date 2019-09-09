package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;

import java.util.List;

public class TransformationHelper {
    public static boolean isConditionGroup(String originalSource, ASTConditionBase condition, String culture) {
        List<String> operators = Aliases.getSpecificAliasByToken(culture, Constants.OR_TOKEN, Constants.AND_TOKEN);

        int conditionIndex = originalSource.indexOf(condition.getOriginalSource());
        String compareString = originalSource
                .substring(conditionIndex + condition.getOriginalSource().length()).trim();
        return operators.stream().anyMatch(operator -> compareString.toUpperCase().startsWith(operator));
    }
}
