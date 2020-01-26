package end2ast;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Test;

public class SemanticOperatorTests {

  @Test
  public void should_resolve_semantic_operator() throws Exception {
    End2AstRunner.run(
        "age greater as operator older \n\n user should not be older than 18",
        "{age:0}",
        r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("user should not be older than 18")
                .condition()
                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                .leftProperty("age")
                .parentCondition()
                .rightNumber(18.0));
  }

  @Test
  public void should_resolve_semantic_operator_string_comp() throws Exception {
    End2AstRunner.run(
        "location equals as operator come from \n\n user must not come from Dortmund",
        "{location:''}",
        r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("user must not come from Dortmund")
                .condition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .leftProperty("location")
                .parentCondition()
                .rightString("Dortmund"));
  }

  @Test
  public void should_resolve_semantic_operator_opposite() throws Exception {
    End2AstRunner.run(
        "age greater as operator older \n\n user older than 18 years should not be blabla",
        "{age:0}",
        r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("user older than 18 years should not be blabla")
                .condition()
                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                .leftProperty("age")
                .parentCondition()
                .rightNumber(18.0));
  }

  @Test
  public void should_resolve_semantic_operator_in_multicondition_rule() throws Exception {
    End2AstRunner.run(
        "age greater as operator older \n\n user should not be older than 18 years and his name must be Alex",
        "{age:0, name:''}",
        r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("user should not be older than 18 years and his name must be Alex")
                .conditionGroup()
                .hasSize(2)
                .first()
                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                .leftProperty("age")
                .parentCondition()
                .rightNumber(18.0)
                .parentConditionGroup()
                .second()
                .hasConnector(ASTConditionConnector.OR)
                .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                .leftProperty("name")
                .parentCondition()
                .rightString("Alex"));
  }

  @Test
  public void should_resolve_semantic_operator_with_variable() throws Exception {
    End2AstRunner.run(
        "0 as age \n\n age greater as operator older \n\n user should not be older than 18 years",
        "{}",
        r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("user should not be older than 18 years")
                .condition()
                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                .leftVariable("age")
                .parentCondition()
                .rightNumber(18.0));
  }

  @Test
  public void should_resolve_semantic_operator_with_variable_recursive() throws Exception {
    End2AstRunner.run(
        "alter as age \n\n age greater as operator older \n\n user should not be older than 18 years",
        "{alter:0}",
        r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("user should not be older than 18 years")
                .condition()
                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                .leftVariable("age")
                .parentCondition()
                .rightNumber(18.0)
                .parentModel()
                .variables()
                .hasSizeOf(1)
                .first()
                .hasName("age")
                .operandProperty()
                .hasPath("alter")
                .hasType(DataPropertyType.Decimal));
  }
}
