package io.openvalidation.test.rest.model.dto.astDTO.element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.rest.model.dto.astDTO.element.VariableNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VariableNodeTest {
  @Test
  public void VariableNode_getPotentialKeywords() {
    VariableNode comment = new VariableNode(null, null, null);

    List<String> expected = new ArrayList<>();
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }
}
