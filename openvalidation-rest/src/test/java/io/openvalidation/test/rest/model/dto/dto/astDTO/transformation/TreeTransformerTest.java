package io.openvalidation.test.rest.model.dto.dto.astDTO.transformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.rest.model.dto.UnkownElementParser;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.MainNode;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.element.UnkownNode;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.TreeTransformer;
import io.openvalidation.rest.service.OVParams;
import io.openvalidation.rest.service.OpenValidationServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TreeTransformerTest {

  @InjectMocks private OpenValidationServiceImpl ovService;

  @Test
  public void transform_null_expect_empty_mainNode() {
    TreeTransformer parser = new TreeTransformer(null, null, null);

    MainNode expected = new MainNode();
    MainNode actual = parser.transform(null);

    assertThat(actual, is(expected));
  }

  @Test
  public void transform_single_static_string_operand_expect_static_string_operandNode()
      throws Exception {
    String documentText = "Test";
    OVParams parameter = new OVParams(documentText, "{ Alter: 30 }", "en", "Java");
    OpenValidationResult result = ovService.generate(parameter);
    List<ASTItem> astItemList =
        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);

    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);

    MainNode expectedNode = new MainNode();
    expectedNode.setRange(new Range(0, 0, 0, 4));

    List<String> expectedLines = new ArrayList<>();
    expectedLines.add(documentText);

    DocumentSection expectedSection = new DocumentSection(new Range(0, 0, 0, 4), expectedLines);
    OperandNode expectedScope = new OperandNode(DataPropertyType.String, expectedSection);
    GenericNode wrapperScope = new UnkownNode(expectedScope, expectedSection);
    expectedNode.addScope(wrapperScope);

    MainNode actual = parser.transform(documentText);

    assertThat(actual, is(expectedNode));
  }
}
