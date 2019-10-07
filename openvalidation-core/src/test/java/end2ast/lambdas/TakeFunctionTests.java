package end2ast.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Test;

public class TakeFunctionTests {
    @Test
    void take_function_simple() throws Exception {
        String rule = "take 10 items from addresses as a last address";
        String schema = "{addresses:[]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .hasSizeOf(1)
                                .first()
                                .hasName("a last address")
                                .operandFunction()
                                .hasName("TAKE")
                                //.hasType(DataPropertyType.Array)
                                .sizeOfParameters(2)
                                .parameters()
                                .first()
                                .property("addresses")
                                .hasType(DataPropertyType.Array)
                                .parentList()
                                .second()
                                .number()
                                .hasValue(10.0)
        );
    }

    @Test
    void take_function_with_condition() throws Exception {
        String rule = "take 10 items from addresses with streetnumber greater than 100 as a last address";
        String schema = "{addresses:[{streetnumber: 1, city: Berlin}]}";

        End2AstRunner.run(
                rule,
                schema,
                r ->
                        r.variables()
                                .hasSizeOf(1)
                                .first()
                                .hasName("a last address")
                                .operandFunction()
                                .hasName("TAKE")
                                //.hasType(DataPropertyType.Array)
                                .sizeOfParameters(2)
                                .parameters()
                                .second()
                                .number()
                                .hasValue(10.0)
                                .parentList()
                                .first()
                                .function("WHERE")
                                .sizeOfParameters(2)
                                .parameters()
                                .first()
                                .property("addresses")
                                .hasType(DataPropertyType.Array)
                                .parentList()
                                .second()
                                .lambdaCondition()
                                .hasNoConnector()
                                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                                .leftProperty("streetnumber")
                                .parentCondition()
                                .rightNumber(100.0)
        );

    }
}
