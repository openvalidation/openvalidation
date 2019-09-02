/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package end2ast;

import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CaseInsensitivityTests {

  private String schema =
      "{ Text: abcde, Number: 1337, Person : { Name: Peter, Age: 18 }, Primes: [2, 3, 5, 7, 11]}";

  @ParameterizedTest
  @ValueSource(
      strings = {
        "Comment This is a beautiful Comment",
        "Comment This is a beautiful\nmultiline comment",
        "cOmMenT This is a beautiful Comment",
        "cOmMenT This is a beautiful\nmultiline comment"
      })
  void comment_test(String input) throws Exception {
    End2AstRunner.run(input, schema, r -> r.comments().hasSizeOf(1));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "If Number is greater than 10 Then error Errorcode 1337",
        "if Number is greater than 10 Then error errorcode 1337",
        "if Number is greater than 10 then error errorcode 1337",
      })
  void rule_with_simple_comparison(String input) throws Exception {
    End2AstRunner.run(
        input, schema, r -> r.rules().hasSizeOf(1).first().hasError("error").hasErrorCode(1337));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "The Number Must be 10",
        "The Number must be 10",
        "the Number mUST be 10",
        "the Number shOulD be 10",
      })
  void constrained_rule(String input) throws Exception {
    End2AstRunner.run(input, schema, r -> r.rules().hasSizeOf(1).first().hasError(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Number is GREATER THAN 10 As var", "Number is GREATER THAN 10 as var"})
  void variable_with_simple_comparison(String input) throws Exception {
    End2AstRunner.run(input, schema, r -> r.variables().hasSizeOf(1).first().hasName("var"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "Number is LESS THAN 10 And Text EQUALS Peter Or Number IS 18 AS var",
        "Number is LESS THAN 10 and Text EQUALS Peter or Number IS 18 AS var",
      })
  void variable_with_condition_group(String input) throws Exception {
    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .conditionGroup()
                .hasSize(3)
                .first()
                .hasNoConnector()
                .parentConditionGroup()
                .second()
                .hasConnector(ASTConditionConnector.AND)
                .parentConditionGroup()
                .atIndex(2)
                .hasConnector(ASTConditionConnector.OR));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "Person.Age AS var",
        "person.age AS var",
        "PERSON.AGE AS var",
        "pERSON.aGE AS var"
      })
  void variable_with_accessor(String input) throws Exception {
    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables().hasSizeOf(1).first().operandProperty().hasType(DataPropertyType.Decimal));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "Number * 3 - Person.Age AS var",
        "Number * 3 Minus Person.Age AS var",
        "Number * 3 minus Person.Age AS var",
        "Number Times 3 - Person.Age AS var",
        "Number Times 3 Minus Person.Age AS var",
        "Number Times 3 minus Person.Age AS var",
        "Number times 3 - Person.Age AS var",
        "Number times 3 Minus Person.Age AS var",
        "Number times 3 minus Person.Age AS var",
      })
  void variable_with_arithmetic_simple(String input) throws Exception {
    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .arithmeticalOperation()
                .hasSizeOf(3)
                .first()
                .propertyValue()
                .hasType(DataPropertyType.Decimal)
                .parentOperation()
                .second()
                .hasOperator(ASTArithmeticalOperator.Multiplication)
                .staticNumber(3.0)
                .parentOperation()
                .atIndex(2)
                .hasOperator(ASTArithmeticalOperator.Subtraction)
                .propertyValue()
                .hasType(DataPropertyType.Decimal));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "Sum of Primes AS var",
        "sum of Primes AS var",
        "The Sum of Primes AS var",
        "The sUm oF Primes AS var"
      })
  void variable_with_function(String input) throws Exception {
    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .function()
                .hasName("SUM_OF")
                .firstProperty()
                .hasType(DataPropertyType.Array));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Variable as var2", "variable as var2", "The VarIAbLe as var2"})
  void variable_using_another_variable(String input) throws Exception {
    input = "1 + 1 AS VARIABLE \n\n" + input;

    End2AstRunner.run(
        input, schema, r -> r.variables().hasSizeOf(2).second().variable().hasName("VARIABLE"));
  }
}
