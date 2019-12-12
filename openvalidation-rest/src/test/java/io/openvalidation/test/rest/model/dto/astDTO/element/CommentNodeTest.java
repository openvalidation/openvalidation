package io.openvalidation.test.rest.model.dto.astDTO.element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.rest.model.dto.astDTO.element.CommentNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CommentNodeTest {
  @Test
  public void CommentNode_getPotentialKeywords() {
    CommentNode comment = new CommentNode(null, null, null);

    List<String> expected = new ArrayList<>();
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }
}
