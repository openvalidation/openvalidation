package io.openvalidation.test.rest.model.dto.astDTO.operation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperatorNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OperatorNodeTest {
  @Test
  public void OperatorNode_getPotentialKeywords() {
    OperatorNode comment = new OperatorNode(null, null, null);

    List<String> expected = new ArrayList<>();
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }
}
