package io.openvalidation.test.rest.model.dto.astDTO.element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.utils.Constants;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.element.VariableNameNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VariableNameNodeTest {
  @Test
  public void VariableNameNode_getPotentialKeywords() {
    TransformationParameter parameter = new TransformationParameter("de");
    DocumentSection section =
        new DocumentSection(new Range(0, 0, 0, 0), Collections.singletonList("Hallo"), null);
    VariableNameNode comment = new VariableNameNode(section, null, parameter);

    List<String> expected = Collections.singletonList(Constants.AS_TOKEN);
    List<String> actual = comment.getPotentialKeywords();

    assertThat(actual, is(expected));
  }

  @Test
  public void VariableNameNode_constructor_generate_variableNameRange() {
    TransformationParameter parameter = new TransformationParameter("de");
    DocumentSection section =
        new DocumentSection(new Range(0, 0, 0, 8), Collections.singletonList("ALS Test"), null);
    VariableNameNode variable = new VariableNameNode(section, "Test", parameter);

    Range expected = new Range(0, 4, 0, 8);
    Range actual = variable.getVariableNameRange();

    assertThat(actual, is(expected));
  }
}
