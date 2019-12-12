package io.openvalidation.test.rest.model.dto.astDTO.operation.operand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.utils.Constants;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.FunctionOperandNode;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FunctionOperandNodeTest {
  @Test
  public void FunctionOperandNode_getPotentialKeywords() {
    FunctionOperandNode comment = new FunctionOperandNode(null, null, null);

    List<String> expected = Collections.singletonList(Constants.FUNCTION_TOKEN);
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }
}
