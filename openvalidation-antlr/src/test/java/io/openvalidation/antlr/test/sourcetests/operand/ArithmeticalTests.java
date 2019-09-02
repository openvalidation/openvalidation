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

package io.openvalidation.antlr.test.sourcetests.operand;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertVariable;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.data.DataVariableReference;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class ArithmeticalTests {

  // operand arithmetical
  @Test
  void test_operand_arithmetical_simple_with_numbers() throws Exception {
    String input = GrammarBuilder.create().with(5).ADD(15).AS("variable").getText();

    String expectedSource = GrammarBuilder.create().with(5).ADD(15).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmetic().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_operand_arithmetical_simple_with_multiple_operands_in_operation() throws Exception {
    String input =
        GrammarBuilder.create().with(5).ADD(15).MINUS(20).MULTIPLY(100).AS("variable").getText();

    String expectedSource =
        GrammarBuilder.create().with(5).ADD(15).MINUS(20).MULTIPLY(100).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmetic().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_operand_arithmetical_with_parentheses_in_operation() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(5")
            .ADD("(15")
            .MINUS("20))")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().with("(5").ADD("(15").MINUS("20))").MULTIPLY(100).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmetic().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_operand_arithmetical_with_spacing_whitespace() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(   5   ")
            .ADD("  (   15   ")
            .MINUS("    20 )  )  ")
            .MULTIPLY(100)
            .AS("  variable  ")
            .getText();

    String expectedSource =
        GrammarBuilder.create()
            .with("(   5   ")
            .ADD("  (   15   ")
            .MINUS("    20 )  )  ")
            .MULTIPLY(100)
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmetic().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_operand_arithmetical_with_spacing_newlines_UNIX() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(5\n")
            .ADD("(15\n")
            .MINUS("20\n)\n)")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().with("(5\n").ADD("(15\n").MINUS("20\n)\n)").MULTIPLY(100).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmetic().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_operand_arithmetical_with_spacing_newlines_WINDOWS() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(5\r\n")
            .ADD("(15\r\n")
            .MINUS("20\r\n)\r\n)")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create()
            .with("(5\r\n")
            .ADD("(15\r\n")
            .MINUS("20\r\n)\r\n)")
            .MULTIPLY(100)
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmetic().hasPreprocessedSource(expectedSource);
  }

  // ast operand arithmetical operation
  @Test
  void test_arithmetical_operation_simple_with_numbers() throws Exception {
    String input = GrammarBuilder.create().with(5).ADD(15).AS("variable").getText();

    String expectedSource = GrammarBuilder.create().with(5).ADD(15).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmeticalOperation().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_simple_with_multiple_operands_in_operation() throws Exception {
    String input =
        GrammarBuilder.create().with(5).ADD(15).MINUS(20).MULTIPLY(100).AS("variable").getText();

    String expectedSource =
        GrammarBuilder.create().with(5).ADD(15).MINUS(20).MULTIPLY(100).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmeticalOperation().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_with_parentheses() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(5")
            .ADD("(15")
            .MINUS("20))")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().with("(5").ADD("(15").MINUS("20))").MULTIPLY(100).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmeticalOperation().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_with_spacing_whitespace() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(   5   ")
            .ADD("  (   15   ")
            .MINUS("    20 )  )  ")
            .MULTIPLY(100)
            .AS("  variable  ")
            .getText();

    String expectedSource =
        GrammarBuilder.create()
            .with("(   5   ")
            .ADD("  (   15   ")
            .MINUS("    20 )  )  ")
            .MULTIPLY(100)
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().arithmeticalOperation().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_1() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(5")
            .ADD("(15")
            .MINUS("20))")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource = GrammarBuilder.create().with("(5").ADD("(15").MINUS("20))").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_2() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(5")
            .ADD("(15")
            .MINUS("20))")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource = GrammarBuilder.create().ADD("(15").MINUS("20)").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_1_with_spacing_whitespace()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(   5   ")
            .ADD("  (   15   ")
            .MINUS("    20 )  )  ")
            .MULTIPLY(100)
            .AS("  variable  ")
            .getText();

    String expectedSource =
        GrammarBuilder.create().with("(   5   ").ADD("  (   15   ").MINUS("    20 )  )").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_2_with_spacing_whitespace()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(   5   ")
            .ADD("  (   15   ")
            .MINUS("    20 )  )  ")
            .MULTIPLY(100)
            .AS("  variable  ")
            .getText();

    String expectedSource = GrammarBuilder.create().ADD("  (   15   ").MINUS("    20 )").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_1_with_spacing_newlines_UNIX()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(\n5")
            .ADD("(\n15\n")
            .MINUS("20\n)\n)\n")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().with("(\n5").ADD("(\n15\n").MINUS("20\n)\n)\n").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_2_with_spacing_newlines_UNIX()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(\n5")
            .ADD("(\n15\n")
            .MINUS("20\n)\n)\n")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource = GrammarBuilder.create().ADD("(\n15\n").MINUS("20\n)\n").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_1_with_spacing_newlines_WINDOWS()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(\r\n5")
            .ADD("(\r\n15\r\n")
            .MINUS("20\r\n)\r\n)\r\n")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create()
            .with("(\r\n5")
            .ADD("(\r\n15\r\n")
            .MINUS("20\r\n)\r\n)\r\n")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_arithmetical_operation_nested_with_parentheses_2_with_spacing_newlines_WINDOWS()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("(\r\n5")
            .ADD("(\r\n15\r\n")
            .MINUS("20\r\n)\r\n)\r\n")
            .MULTIPLY(100)
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().ADD("(\r\n15\r\n").MINUS("20\r\n)\r\n").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .firstSubOperation()
        .firstSubOperation()
        .hasPreprocessedSource(expectedSource);
  }

  // ast operand arithmetical number item
  @Test
  void test_arithmetical_number_item_with_simple_numbers() throws Exception {
    String input = GrammarBuilder.create().with("5").ADD(15).AS("variable").getText();

    String expectedSource = "5";

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource(expectedSource)
        .staticNumber();
  }

  @Test
  void test_arithmetical_number_item_with_decimal() throws Exception {
    String input = GrammarBuilder.create().with("3.1415").ADD(15).AS("variable").getText();

    String expectedSource = "3.1415";

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource(expectedSource)
        .staticNumber();
  }

  @Test
  void test_arithmetical_number_item_with_sugar() throws Exception {
    String input = GrammarBuilder.create().with("The big 5").ADD(15).AS("variable").getText();

    String expectedSource = "The big 5";

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource(expectedSource)
        .staticNumber();
  }

  @Test
  void test_arithmetical_number_item_with_sugar_and_operator() throws Exception {
    String input =
        GrammarBuilder.create().with("The big 5").ADD("The small 50").AS("variable").getText();

    String expectedSource = GrammarBuilder.create().ADD("The small 50").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .second()
        .hasPreprocessedSource(expectedSource)
        .staticNumber();
  }

  @Test
  void test_arithmetical_number_item_with_newlines_UNIX() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("The\n big\n 5\n")
            .ADD("The small 50")
            .AS("variable")
            .getText();

    String expectedSource = "The\n big\n 5";

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource(expectedSource)
        .staticNumber();
  }

  @Test
  void test_arithmetical_number_item_with_newlines_WINDOWS() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("The\r\n big\r\n 5\r\n")
            .ADD("The small 50")
            .AS("variable")
            .getText();

    String expectedSource = "The\r\n big\r\n 5";

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource(expectedSource)
        .staticNumber();
  }

  // ast operand arithmetical property item
  @Test
  void test_arithmetical_property_item_simple() throws Exception {
    String input = GrammarBuilder.create().with("Person.Age").ADD(1).AS("variable").getText();

    String expectedSource = "Person.Age";

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .first()
              .arithmeticalOperation()
              .first()
              .hasPreprocessedSource(expectedSource)
              .propertyValue();
        });
  }

  @Test
  void test_arithmetical_property_item_with_sugar() throws Exception {
    String input =
        GrammarBuilder.create().with("Das Person.Age sugar").ADD(1).AS("variable").getText();

    String expectedSource = "Das Person.Age sugar";

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .first()
              .arithmeticalOperation()
              .first()
              .hasPreprocessedSource(expectedSource)
              .propertyValue();
        });
  }

  @Test
  void test_arithmetical_property_item_with_sugar_and_operator_and_whitespace_UNIX()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("Das Person.Age sugar")
            .ADD("das  \n selbe   Person.Age \n  da")
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().ADD("das  \n selbe   Person.Age \n  da").getText();

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .first()
              .arithmeticalOperation()
              .second()
              .hasPreprocessedSource(expectedSource)
              .propertyValue();
        });
  }

  @Test
  void test_arithmetical_property_item_with_sugar_and_operator_and_whitespace_WINDOWS()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("Das Person.Age sugar")
            .ADD("das  \r\n selbe   Person.Age \r\n  da")
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().ADD("das  \r\n selbe   Person.Age \r\n  da").getText();

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .first()
              .arithmeticalOperation()
              .second()
              .hasPreprocessedSource(expectedSource)
              .propertyValue();
        });
  }

  // ast operand arithmetical string item
  @Test
  void test_arithmetical_string_item() throws Exception {
    String input = GrammarBuilder.create().with(10).ADD("a banana").AS("variable").getText();

    String expectedSource = GrammarBuilder.create().ADD("a banana").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .second()
        .hasPreprocessedSource(expectedSource)
        .staticString();
  }

  // ast operand arithmetical variable
  @Test
  void test_arithmetical_variable_item_simple() throws Exception {
    String input = GrammarBuilder.create().with("income").ADD(1).AS("variable").getText();

    String expectedSource = "income";

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("income", DataPropertyType.Decimal));

    ASTModel ast = ANTLRExecutor.run(input, schema);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource(expectedSource)
        .variableValue();
  }

  @Test
  void test_arithmetical_variable_item_with_sugar() throws Exception {
    String input = GrammarBuilder.create().with("Das income sugar").ADD(1).AS("variable").getText();

    String expectedSource = "Das income sugar";

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("income", DataPropertyType.Decimal));

    ASTModel ast = ANTLRExecutor.run(input, schema);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource(expectedSource)
        .variableValue();
  }

  @Test
  void test_arithmetical_variable_item_with_sugar_and_operator_and_whitespace_UNIX()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("Das income sugar")
            .ADD("das  \n selbe   income \n  da")
            .AS("variable")
            .getText();

    String expectedSource = GrammarBuilder.create().ADD("das  \n selbe   income \n  da").getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("income", DataPropertyType.Decimal));

    ASTModel ast = ANTLRExecutor.run(input, schema);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .second()
        .hasPreprocessedSource(expectedSource)
        .variableValue();
  }

  @Test
  void test_arithmetical_variable_item_with_sugar_and_operator_and_whitespace_WINDOWS()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("Das income sugar")
            .ADD("das  \r\n selbe   income \r\n  da")
            .AS("variable")
            .getText();

    String expectedSource =
        GrammarBuilder.create().ADD("das  \r\n selbe   income \r\n  da").getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("income", DataPropertyType.Decimal));

    ASTModel ast = ANTLRExecutor.run(input, schema);

    assertVariable(ast)
        .first()
        .arithmeticalOperation()
        .second()
        .hasPreprocessedSource(expectedSource)
        .variableValue();
  }
}
