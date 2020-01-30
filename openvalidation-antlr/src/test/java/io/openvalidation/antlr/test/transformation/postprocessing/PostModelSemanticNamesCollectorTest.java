package io.openvalidation.antlr.test.transformation.postprocessing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class PostModelSemanticNamesCollectorTest {

  @Test
  public void should_collect_semantic_operator_names() {
    String input =
        GrammarBuilder.createRule()
            .with("age")
            .GREATER_THAN()
            .AS_OPERATOR("older")
            .PARAGRAPH()
            .with("location")
            .EQ()
            .AS_OPERATOR("come from")
            .PARAGRAPH()
            .with("user")
            .MUST()
            .with("be older than 18")
            .PARAGRAPH()
            .with("user")
            .MUST_NOT()
            .with("come from Dortmund")
            .toString();

    DataSchema schema = new DataSchema();
    schema.addProperty("age", null, DataPropertyType.Decimal);
    schema.addProperty("location", null, DataPropertyType.String);
    ASTModel ast = ANTLRExecutor.run(input, schema);

    assertThat(ast, notNullValue());
    assertThat(ast.getSemanticOperatorNames(), notNullValue());
    assertThat(ast.getSemanticOperatorNames().size(), is(2));
    assertThat(ast.getSemanticOperatorNames().get(0), is("older"));
    assertThat(ast.getSemanticOperatorNames().get(1), is("come from"));
  }
}
