package end2ast;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SemanticOperatorTests {

    @Test
    public void should_be_done() throws Exception {

        End2AstRunner.run("age greater as operator older \n\n user should not be older than 18",
                "{age:0}",
                r -> r.rules()
                        .hasSizeOf(1)
                        .first()
                        .hasError("user should not be older than 18")
        );
    }
}
