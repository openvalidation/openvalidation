package end2ast;

import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ArrayOperandContentTypeTest {

  @ParameterizedTest
  @CsvSource(
      value = {
        "banana;String",
        "banana, apple;String",
        "banana, 1, 1.5, true, [1,2,3];String",
        "1;Decimal",
        "1, 2, 3;Decimal",
        "1, banana, 1.5, true, [1,2,3];Decimal",
        "1.5;Decimal",
        "1.5, 2.5, 3.5;Decimal",
        "1.5, banana, 1, true, [1,2,3];Decimal",
        // "date;Date",
        "[1,2,3];Array",
        "[1,2,3], [4,5,6];Array",
        "[1,2,3], banana, 1.5, true;Array",
        "true;Boolean",
        "true, false;Boolean",
        "true, 1.5, banana, 1, [1,2,3];Boolean"
      },
      delimiter = ';')
  void test_array_property_with_all_content_types(String arrayInput, String expectedType)
      throws Exception {
    String input = "dataArray AS var";
    String schema = "{dataArray: [" + arrayInput + "]}";

    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .first()
                .operandProperty()
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.valueOf(expectedType)));
  }
}
