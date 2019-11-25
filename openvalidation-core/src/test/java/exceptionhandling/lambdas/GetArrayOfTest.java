package exceptionhandling.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Test;

public class GetArrayOfTest {


    @Test
    void get_array_of_in_variable_has_correct_type() throws Exception {
        String rule = "numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .hasName("GET_ARRAY_OF")
                                .hasType(DataPropertyType.Array)
        );
    }

    @Test
    void get_array_of_in_variable_has_correct_array_content_type() throws Exception {
        String rule = "numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .hasName("GET_ARRAY_OF")
                                .hasArrayContentType(DataPropertyType.Decimal)
        );
    }

    @Test
    void get_array_of_nested_has_correct_type() throws Exception {
        String rule = "FIRST item FROM numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .parameters()
                                .first()
                                .function()
                                .hasName("GET_ARRAY_OF")
                                .parameters()
                                .first()
                                .property()
                                .hasType(DataPropertyType.Array)
        );
    }

    @Test
    void get_array_of_nested_has_correct_array_content_type() throws Exception {
        String rule = "FIRST item FROM numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .parameters()
                                .first()
                                .function()
                                .hasName("GET_ARRAY_OF")
                                .parameters()
                                .first()
                                .property()
                                .hasArrayContentType(DataPropertyType.Object)
        );
    }

    @Test
    void get_array_of_array_property_has_correct_type() throws Exception {
        String rule = "numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .hasName("GET_ARRAY_OF")
                                .parameters()
                                .first()
                                .property()
                                .hasPath("numberObjects")
                                .hasType(DataPropertyType.Array)
        );
    }

    @Test
    void get_array_of_array_property_has_correct_array_content_type() throws Exception {
        String rule = "numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .hasName("GET_ARRAY_OF")
                                .parameters()
                                .first()
                                .property()
                                .hasPath("numberObjects")
                                .hasArrayContentType(DataPropertyType.Object)
        );
    }

    @Test
    void get_array_of_lambda_has_correct_type() throws Exception {
        String rule = "numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .hasName("GET_ARRAY_OF")
                                .parameters()
                                .second()
                                .lambda()
                                .hasType(DataPropertyType.Boolean)
        );
    }

    @Test
    void get_array_of_lambda_has_lambda_token() throws Exception {
        String rule = "numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .hasName("GET_ARRAY_OF")
                                .parameters()
                                .second()
                                .lambda()
                                .tokenStartsWith("x_")
        );
    }

    @Test
    void get_array_of_lambda_property_has_correct_type() throws Exception {
        String rule = "numberObjects.value as var";
        String schema = "{numberObjects: [{value: 1}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .first()
                                .operandFunction()
                                .hasName("GET_ARRAY_OF")
                                .parameters()
                                .second()
                                .lambda()
                                .property()
                                .hasPath("value")
                                .hasType(DataPropertyType.Decimal)
                                .hasArrayContentType(null)
        );
    }
}
