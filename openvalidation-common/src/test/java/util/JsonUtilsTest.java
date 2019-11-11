package util;

import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.JsonUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonUtilsTest {

    @ParameterizedTest
    @CsvSource(value = {
            "true;Boolean",
            "True;Boolean",
            "TrUe;Boolean",
            "false;Boolean",
            "False;Boolean",
            "FaLSe;Boolean",
            "[1,2,3];Array",
            "[abc,def,ghi];Array",
            "[[1,2,3],[1,2,3],[1,2,3]];Array",
            "1;Decimal",
            "1.5;Decimal",
            "Banana;String"
    }, delimiter = ';')
    void should_resolve_type_from_string_correctly(String input, String expected)
    {
        DataPropertyType resultType = JsonUtils.parseTypeFromString(input);
        assertThat(resultType.toString(), is(expected));
    }
}
