package org.bag.openvalidation.test.rest.model.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.rest.model.dto.UnkownElementParser;
import io.openvalidation.rest.service.OVParams;
import io.openvalidation.rest.service.OpenValidationServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnkownElementParserTest {

  @InjectMocks private OpenValidationServiceImpl ovService;

  @Test
  public void generate_with_null_arguments_expected_no_error() throws Exception {
    UnkownElementParser parser = new UnkownElementParser(null, null);

    List<ASTItem> expected = new ArrayList<>();
    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual, is(expected));
  }

  @Test
  public void generate_with_string_expect_static_string_operand() throws Exception {
    OVParams parameter = new OVParams("Test", "{ Alter: 30 }", "en", "Java");
    OpenValidationResult result = ovService.generate(parameter);
    ASTModel astModel = result.getASTModel();

    UnkownElementParser parser = new UnkownElementParser(astModel, parameter);

    List<ASTItem> expected = new ArrayList<>();
    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual.get(0), instanceOf(ASTOperandStaticString.class));
  }

  @Test
  public void generate_with_number_expect_operand() throws Exception {
    OVParams parameter = new OVParams("42", "{ Alter: 30 }", "en", "Java");
    OpenValidationResult result = ovService.generate(parameter);
    ASTModel astModel = result.getASTModel();

    UnkownElementParser parser = new UnkownElementParser(astModel, parameter);

    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual.get(0), instanceOf(ASTOperandStaticNumber.class));
  }

  @Test
  public void generate_with_variableOperand_expect_schema_operand() throws Exception {
    OVParams parameter = new OVParams("age", "{ age: 30 }", "en", "Java");
    OpenValidationResult result = ovService.generate(parameter);
    ASTModel astModel = result.getASTModel();

    UnkownElementParser parser = new UnkownElementParser(astModel, parameter);

    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual.get(0), instanceOf(ASTOperandProperty.class));
  }

  @Test
  public void generate_with_operand_and_operator_expect_operation() throws Exception {
    OVParams parameter = new OVParams("age smaller", "{ age: 30 }", "en", "Java");
    OpenValidationResult result = ovService.generate(parameter);
    ASTModel astModel = result.getASTModel();

    UnkownElementParser parser = new UnkownElementParser(astModel, parameter);

    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual.get(0), instanceOf(ASTCondition.class));
    assertThat(
        ((ASTCondition) actual.get(0)).getLeftOperand(), instanceOf(ASTOperandProperty.class));
    assertThat(((ASTCondition) actual.get(0)).getOperator(), is(ASTComparisonOperator.LESS_THAN));
  }

  @Test
  public void generate_with_operands_and_operator_expect_complete_operation() throws Exception {
    OVParams parameter = new OVParams("age smaller 20", "{ age: 30 }", "en", "Java");
    OpenValidationResult result = ovService.generate(parameter);
    ASTModel astModel = result.getASTModel();

    UnkownElementParser parser = new UnkownElementParser(astModel, parameter);

    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual.get(0), instanceOf(ASTCondition.class));
    assertThat(
        ((ASTCondition) actual.get(0)).getLeftOperand(), instanceOf(ASTOperandProperty.class));
    assertThat(((ASTCondition) actual.get(0)).getOperator(), is(ASTComparisonOperator.LESS_THAN));
    assertThat(
        ((ASTCondition) actual.get(0)).getRightOperand(), instanceOf(ASTOperandStaticNumber.class));
  }

  @Test
  public void generate_with_parsable_rule_expect_unkownElementParser_to_equal() throws Exception {
    OVParams parameter = new OVParams("age smaller 20 then error", "{ age: 30 }", "en", "Java");
    OpenValidationResult result = ovService.generate(parameter);
    ASTModel astModel = result.getASTModel();

    UnkownElementParser parser = new UnkownElementParser(astModel, parameter);

    List<ASTItem> actual = parser.generate(ovService);

    assertThat(actual, equalTo(astModel.getElements()));
  }
}
