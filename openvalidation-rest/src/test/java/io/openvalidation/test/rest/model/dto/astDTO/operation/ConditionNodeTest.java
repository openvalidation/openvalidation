package io.openvalidation.test.rest.model.dto.astDTO.operation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.rest.model.dto.astDTO.operation.ConditionNode;
import io.openvalidation.rest.model.dto.astDTO.operation.OperationNode;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConditionNodeTest {
  @Test
  public void ConditionNode_getPotentialKeywords() {
    ConditionNode comment = new OperationNode(new ASTCondition(), null, null);

    List<String> expected = Arrays.asList(Constants.AND_TOKEN, Constants.OR_TOKEN);
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }
}
