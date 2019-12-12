package io.openvalidation.test.rest.model.dto.astDTO.element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.rest.model.dto.astDTO.element.UnknownNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UnknownNodeTest {
  @Test
  public void UnknownNode_getPotentialKeywords() {
    UnknownNode comment = new UnknownNode(null, null);

    List<String> expected = new ArrayList<>();
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }
}
