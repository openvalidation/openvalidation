package io.openvalidation.test.rest.model.dto.astDTO.operation.operand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OperandNodeTest {
  @Test
  public void OperandNode_getPotentialKeywords() {
    OperandNode comment = new OperandNode((DataPropertyType) null, null, null);

    List<String> expected = new ArrayList<>();
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }
}
