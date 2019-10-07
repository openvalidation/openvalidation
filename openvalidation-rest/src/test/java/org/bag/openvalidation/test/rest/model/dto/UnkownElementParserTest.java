package org.bag.openvalidation.test.rest.model.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.rest.model.dto.UnkownElementParser;
import io.openvalidation.rest.service.OpenValidationServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UnkownElementParserTest {

  @InjectMocks private OpenValidationServiceImpl ovService;

  @BeforeEach
  private void setUpMocks() throws Exception {}

  @Test
  public void generate_with_null_arguments_expected_no_error() throws Exception {
    UnkownElementParser parser = new UnkownElementParser(null, null);

    List<ASTItem> expected = new ArrayList<>();
    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual, is(expected));
  }

  //    @Test
  //    public void generate_with_variableOperand_expect_operand() throws Exception {
  //        OVParams parameter = new OVParams("Alter", "{ Alter: 30 }", "de", "Java");
  //        OpenValidationResult result = ovService.generate(parameter);
  //        ASTModel astModel = result.getASTModel();
  //
  //        UnkownElementParser parser = new UnkownElementParser(astModel, parameter);
  //
  //        List<ASTItem> expected = new ArrayList<>();
  //        List<ASTItem> actual = parser.generate(ovService);
  //
  //        assertThat(actual, is(expected));
  //    }
}
