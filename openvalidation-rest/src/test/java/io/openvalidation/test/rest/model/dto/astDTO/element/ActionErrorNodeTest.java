package io.openvalidation.test.rest.model.dto.astDTO.element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.element.ActionErrorNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ActionErrorNodeTest {
  @Test
  public void ActionErrorNode_getPotentialKeywords() {
    ActionErrorNode comment = new ActionErrorNode(null, null, null);

    List<String> expected = Collections.singletonList(Constants.THEN_TOKEN);
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }

  @Test
  public void ActionErrorNode_constructor_generate_actionErrorRange() {
    TransformationParameter parameter = new TransformationParameter("de");
    DocumentSection section =
        new DocumentSection(new Range(0, 0, 0, 10), Collections.singletonList("DANN Error"), null);
    ActionErrorNode variable = new ActionErrorNode(section, new ASTActionError("Error"), parameter);

    Range expected = new Range(0, 5, 0, 10);
    Range actual = variable.getActionErrorRange();

    assertThat(actual, is(expected));
  }
}
