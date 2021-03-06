package io.openvalidation.generation.tests;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorArrayTest {
  private static Stream<Arguments> array_with_numbers() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.NONE_OF(model.Age, huml.CREATE_ARRAY(3.0,4.0,5.0))"),
        Arguments.of("csharp", "huml.NONE_OF(model.Age, huml.CREATE_ARRAY(3.0,4.0,5.0))"),
        Arguments.of("java", "huml.NONE_OF(model.getAge(), huml.CREATE_ARRAY(3.0,4.0,5.0))"),
        Arguments.of("python", "huml.NONE_OF(model.Age, [3.0,4.0,5.0])"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void array_with_numbers(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.NONE_OF)
              .withRightOperandAsArray(3, 4, 5);

          return builder.getModel();
        });
  }
}
