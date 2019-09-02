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

package io.openvalidation.antlr.test.transformation;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertVariable;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.data.DataVariableReference;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class PTArithmeticTransformerTest {

  @Test
  void test_simple_addition_with_2_numbers() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(2).ADD(3).AS("varx").getText();

    // act
    ASTModel astActual = ANTLRExecutor.run(input);

    // assert
    assertVariable(astActual)
        .hasSizeOf(1)
        .first()
        .hasName("varx")
        .arithmetic()
        .hasPreprocessedSource()
        .operation()
        .hasSizeOf(2)
        .first()
        .staticNumber(2.0)
        .hasOperator(null)
        .parentOperation()
        .second()
        .staticNumber(3.0)
        .hasOperator(ASTArithmeticalOperator.Addition);
  }

  @Test
  void test_simple_addition_with_multiple_numbers() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(1).ADD(2).ADD(3).ADD(4).AS("varx").getText();

    // act
    ASTModel astActual = ANTLRExecutor.run(input);

    // assert
    assertVariable(astActual)
        .hasSizeOf(1)
        .first()
        .hasName("varx")
        .arithmetic()
        .hasPreprocessedSource()
        .operation()
        .hasSizeOf(4)
        .first()
        .staticNumber(1.0)
        .hasOperator(null)
        .parentOperation()
        .second()
        .staticNumber(2.0)
        .hasOperator(ASTArithmeticalOperator.Addition)
        .parentOperation()
        .atIndex(2)
        .staticNumber(3.0)
        .hasOperator(ASTArithmeticalOperator.Addition)
        .parentOperation()
        .atIndex(3)
        .staticNumber(4.0)
        .hasOperator(ASTArithmeticalOperator.Addition);
  }

  @Test
  void test_arithmetic_with_number_and_property() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with(2).ADD().with("person.age").AS("varname").toString();

    String schema = "{person: {age: 25}}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .hasSizeOf(1)
              .first()
              .hasName("varname")
              .arithmetic()
              .hasPreprocessedSource()
              .operation()
              .hasSizeOf(2)
              .first()
              .staticNumber(2.0)
              .hasOperator(null)
              .parentOperation()
              .second()
              .hasOperator(ASTArithmeticalOperator.Addition)
              .propertyValue()
              .hasPath("person.age");
        });
  }

  @Test
  void test_arithmetic_with_number_and_variable_reference() throws Exception {
    // assemble
    // 2 + personsage AS varname
    String input =
        GrammarBuilder.create().with(2).ADD().with("personsage").AS("varname").toString();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("personsage", DataPropertyType.Decimal));

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("varname")
        .arithmetic()
        .hasPreprocessedSource()
        .operation()
        .hasSizeOf(2)
        .first()
        .staticNumber(2.0)
        .hasOperator(null)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .variableValue()
        .hasName("personsage");
  }

  @Test
  void test_arithmetic_with_variable_reference_and_property() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("person.dog.siblings")
            .AS("personsage")
            .PARAGRAPH()
            .with("person.dog.siblings")
            .ADD()
            .with("personsage")
            .AS("varname")
            .toString();

    String schema = "{person: {dog: {siblings: 10}}}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          // assert
          r.variables()
              .hasSizeOf(2)
              .second()
              .hasName("varname")
              .arithmetic()
              .hasPreprocessedSource()
              .operation()
              .hasSizeOf(2)
              .first()
              .propertyValue()
              .hasPath("person.dog.siblings")
              .hasType(DataPropertyType.Decimal)
              .parentOperation()
              .second()
              .hasOperator(ASTArithmeticalOperator.Addition)
              .variableValue()
              .hasName("personsage")
              .hasType(DataPropertyType.Decimal);
        });
  }

  @Test
  void test_arithmetic_with_variable_reference_and_semantic_sugar() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with(18)
            .ADD()
            .with("das angegebene personsage und so")
            .AS("varname")
            .toString();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("personsage", DataPropertyType.Decimal));

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("varname")
        .arithmetic()
        .hasPreprocessedSource()
        .operation()
        .hasSizeOf(2)
        .first()
        .staticNumber(18.0)
        //                            .hasPreprocessedSource("18")
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .variableValue()
        .hasName("personsage")
        .hasType(DataPropertyType.Decimal);
  }

  @Test
  void test_arithmetic_with_number_and_property_and_semantic_sugar_reference() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with("the age").ADD().with("18 years old").AS("newage").toString();

    String schema = "{age: 25}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          // assert
          r.variables()
              .hasSizeOf(1)
              .first()
              .hasName("newage")
              .arithmetic()
              .hasPreprocessedSource()
              .operation()
              .hasSizeOf(2)
              .first()
              .hasOperator(null)
              .propertyValue()
              .hasPath("age")
              .parentOperation()
              .second()
              .hasOperator(ASTArithmeticalOperator.Addition)
              .staticNumber(18.0);
        });
  }

  @Test
  void grouping_simple_subgroup() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with("1").MULTIPLY().with("(2").ADD("3)").AS("newage").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasOperator(null)
        .staticNumber(1.0)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Multiplication)
        .hasSizeOf(2)
        .first()
        .hasOperator(null)
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .staticNumber(3.0);
  }

  @Test
  void _grouping_multiple_sub_operations() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("(10")
            .POWEROF()
            .with("2)")
            .MINUS("Person.Age")
            .MINUS()
            .with("(20")
            .ADD()
            .with("2)")
            .AS("variable")
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(3)
        .firstSubOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(10d)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(2d)
        .parentOperation()
        .parentOperation()
        .first()
        .hasOperator(ASTArithmeticalOperator.Subtraction)
        .staticString("Person.Age")
        .parentOperation()
        .secondSubOperation()
        .hasOperator(ASTArithmeticalOperator.Subtraction)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(20d)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .staticNumber(2d);
  }

  @Test
  void grouping_operation_as_first_element_in_suboperation() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("(1")
            .POWEROF()
            .with("2)")
            .POWEROF()
            .with("((3")
            .POWEROF()
            .with("4)")
            .POWEROF()
            .with("5)")
            .AS("variable")
            .getText();

    // (1^2)^((3^4)^5)
    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .firstSubOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(1d)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(2d)
        .parentOperation()
        .parentOperation()
        .secondSubOperation()
        .hasOperator(ASTArithmeticalOperator.Power)
        .hasSizeOf(2)
        .firstSubOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(3d)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(4d)
        .parentOperation()
        .parentOperation()
        .first()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(5d);
  }

  @Test
  void grouping_triple_nesting_left_aligned() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .POWEROF()
            .with("(((2")
            .POWEROF()
            .with("3)")
            .POWEROF()
            .with("4)")
            .POWEROF()
            .with("5)")
            .AS("variable")
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .staticNumber(1d)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Power)
        .hasSizeOf(2)
        .first()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(5d)
        .parentOperation()
        .firstSubOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(4d)
        .parentOperation()
        .firstSubOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(2d)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(3d);
  }

  @Test
  void grouping_triple_nesting_right_aligned() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .POWEROF()
            .with("(2")
            .POWEROF()
            .with("(3")
            .POWEROF()
            .with("(4")
            .POWEROF()
            .with("5)))")
            .AS("variable")
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .staticNumber(1d)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Power)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(2d)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Power)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(3d)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Power)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(4d)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(5d);
  }

  @Test
  void grouping_triple_nesting_alternating() throws Exception {
    // assemble
    // 1 ^ ( ( 2 ^ ( 3 ^ 4 ) ) ^ 5 )
    String input =
        GrammarBuilder.create()
            .with("1")
            .POWEROF()
            .with("((2")
            .POWEROF()
            .with("(3")
            .POWEROF()
            .with("4))")
            .POWEROF()
            .with("5)")
            .AS("variable")
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .staticNumber(1d)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Power)
        .hasSizeOf(2)
        .firstSubOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(2d)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Power)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(3d)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(4d)
        .parentOperation()
        .parentOperation()
        .parentOperation()
        .first()
        .hasOperator(ASTArithmeticalOperator.Power)
        .staticNumber(5d);
  }

  @Test
  void grouping_with_global_parentheses() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("(1").ADD().with("2)").AS("variable").getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .staticNumber(1d)
        .parentOperation()
        .second()
        .staticNumber(2d);
  }

  @Test
  void grouping_with_global_parentheses_double() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("((1").ADD().with("2))").AS("variable").getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .staticNumber(1d)
        .parentOperation()
        .second()
        .staticNumber(2d);
  }

  @Test
  void grouping_with_subgroup_double_parentheses() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with("1").MULTIPLY().with("((2").ADD("3))").AS("newage").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasOperator(null)
        .staticNumber(1.0)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Multiplication)
        .hasSizeOf(2)
        .first()
        .hasOperator(null)
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .staticNumber(3.0);
  }

  @Test
  void grouping_atom_with_parentheses() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("1").ADD().with("(2)").AS("newage").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(1.0)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .staticNumber(2.0);
  }

  @Test
  void grouping_with_global_parentheses_and_redundant_parentheses() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("((1)").ADD().with("(2))").AS("variable").getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .staticNumber(1d)
        .parentOperation()
        .second()
        .staticNumber(2d);
  }

  @Test
  void grouping_with_global_parentheses_double_and_redundant_parentheses() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with("(((1)").ADD().with("(2)))").AS("variable").getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variable")
        .arithmeticalOperation()
        .hasNoOperator()
        .hasSizeOf(2)
        .first()
        .staticNumber(1d)
        .parentOperation()
        .second()
        .staticNumber(2d);
  }

  @Test
  void grouping_subgroup_with_variable_directly_after_open_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with("1").MULTIPLY().with("(var").ADD("3)").AS("newage").toString();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("var", DataPropertyType.Decimal));

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(1.0)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Multiplication)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .variableValue()
        .hasType(DataPropertyType.Decimal)
        .hasName("var")
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .staticNumber(3.0);
  }

  @Test
  void grouping_subgroup_with_variable_with_space_after_open_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .MULTIPLY()
            .with("( var")
            .ADD("3)")
            .AS("newage")
            .toString();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("var", DataPropertyType.Decimal));

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(1.0)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Multiplication)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .variableValue()
        .hasType(DataPropertyType.Decimal)
        .hasName("var")
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .staticNumber(3.0);
  }

  @Test
  void grouping_subgroup_with_variable_directly_before_closing_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with("1").MULTIPLY().with("(2").ADD("var)").AS("newage").toString();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("var", DataPropertyType.Decimal));

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(1.0)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Multiplication)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .variableValue()
        .hasType(DataPropertyType.Decimal)
        .hasName("var");
  }

  @Test
  void grouping_subgroup_with_variable_with_spacing_before_closing_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .MULTIPLY()
            .with("(2")
            .ADD("var )")
            .AS("newage")
            .toString();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("var", DataPropertyType.Decimal));

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(1.0)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Multiplication)
        .hasSizeOf(2)
        .first()
        .hasNoOperator()
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .variableValue()
        .hasType(DataPropertyType.Decimal)
        .hasName("var");
  }

  @Test
  void grouping_with_noise_text() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .MULTIPLY()
            .with("hallo(2")
            .ADD("3 )hallo")
            .AS("newage")
            .toString();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("var", DataPropertyType.Decimal));

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("newage")
        .arithmeticalOperation()
        .hasSizeOf(2)
        .first()
        .hasOperator(null)
        .staticNumber(1.0)
        .parentOperation()
        .firstSubOperation()
        .hasOperator(ASTArithmeticalOperator.Multiplication)
        .hasSizeOf(2)
        .first()
        .hasOperator(null)
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasOperator(ASTArithmeticalOperator.Addition)
        .staticNumber(3.0);
  }

  @Test
  void grouping_subgroup_with_property_directly_after_open_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .MULTIPLY()
            .with("(Person.Age")
            .ADD("3)")
            .AS("newage")
            .toString();

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .hasSizeOf(1)
              .first()
              .hasName("newage")
              .arithmeticalOperation()
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .staticNumber(1.0)
              .parentOperation()
              .firstSubOperation()
              .hasOperator(ASTArithmeticalOperator.Multiplication)
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .propertyValue()
              .hasType(DataPropertyType.Decimal)
              .hasPath("Person.Age")
              .parentOperation()
              .second()
              .hasOperator(ASTArithmeticalOperator.Addition)
              .staticNumber(3.0);
        });
  }

  @Test
  void grouping_subgroup_with_property_with_space_after_open_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .MULTIPLY()
            .with("( Person.Age")
            .ADD("3)")
            .AS("newage")
            .toString();

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .hasSizeOf(1)
              .first()
              .hasName("newage")
              .arithmeticalOperation()
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .staticNumber(1.0)
              .parentOperation()
              .firstSubOperation()
              .hasOperator(ASTArithmeticalOperator.Multiplication)
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .propertyValue()
              .hasType(DataPropertyType.Decimal)
              .hasPath("Person.Age")
              .parentOperation()
              .second()
              .hasOperator(ASTArithmeticalOperator.Addition)
              .staticNumber(3.0);
        });
  }

  @Test
  void grouping_subgroup_with_property_directly_before_closing_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .MULTIPLY()
            .with("(2")
            .ADD("Person.Age)")
            .AS("newage")
            .toString();

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .hasSizeOf(1)
              .first()
              .hasName("newage")
              .arithmeticalOperation()
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .staticNumber(1.0)
              .parentOperation()
              .firstSubOperation()
              .hasOperator(ASTArithmeticalOperator.Multiplication)
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .staticNumber(2.0)
              .parentOperation()
              .second()
              .hasOperator(ASTArithmeticalOperator.Addition)
              .propertyValue()
              .hasType(DataPropertyType.Decimal)
              .hasPath("Person.Age");
        });
  }

  @Test
  void grouping_subgroup_with_property_with_spacing_before_closing_parenthesis() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("1")
            .MULTIPLY()
            .with("(2")
            .ADD("Person.Age )")
            .AS("newage")
            .toString();

    String schema = "{Person: {Age: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .hasSizeOf(1)
              .first()
              .hasName("newage")
              .arithmeticalOperation()
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .staticNumber(1.0)
              .parentOperation()
              .firstSubOperation()
              .hasOperator(ASTArithmeticalOperator.Multiplication)
              .hasSizeOf(2)
              .first()
              .hasNoOperator()
              .staticNumber(2.0)
              .parentOperation()
              .second()
              .hasOperator(ASTArithmeticalOperator.Addition)
              .propertyValue()
              .hasType(DataPropertyType.Decimal)
              .hasPath("Person.Age");
        });
  }

  @Test
  void static_numbers_in_arithmetic_have_source() throws Exception {
    String input = GrammarBuilder.create().with(1).MULTIPLY(2).AS("variable").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource("1")
        .staticNumber()
        .hasValue(1.0)
        .hasPreprocessedSource("1")
        .parentOperation()
        .second()
        .hasPreprocessedSource(Constants.MULTIPLY_TOKEN + Constants.KEYWORD_SYMBOL + "MULTIPLY 2")
        .staticNumber()
        .hasValue(2.0)
        .hasPreprocessedSource("2");
  }

  @Test
  void static_numbers_with_sugar_in_arithmetic_have_source() throws Exception {
    String input = GrammarBuilder.create().with("1€").MULTIPLY(2).AS("variable").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource("1€")
        .staticNumber()
        .hasValue(1.0)
        .hasPreprocessedSource("1€")
        .parentOperation()
        .second()
        .hasPreprocessedSource(Constants.MULTIPLY_TOKEN + Constants.KEYWORD_SYMBOL + "MULTIPLY 2")
        .staticNumber()
        .hasValue(2.0)
        .hasPreprocessedSource("2");
  }

  @Test
  void static_strings_in_arithmetic_have_source() throws Exception {
    String input =
        GrammarBuilder.create().with("A banana").MULTIPLY("an apple").AS("variable").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource("A banana")
        .staticString()
        .hasValue("A banana")
        .hasPreprocessedSource("A banana")
        .parentOperation()
        .second()
        .hasPreprocessedSource(
            Constants.MULTIPLY_TOKEN + Constants.KEYWORD_SYMBOL + "MULTIPLY an apple")
        .staticString()
        .hasValue("an apple")
        .hasPreprocessedSource("an apple");
  }

  @Test
  void static_properties_in_arithmetic_have_source() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("Person.Age")
            .MULTIPLY("Giraffe.Height")
            .AS("variable")
            .getText();

    String schema = "{Person: {Age: 25}, Giraffe: {Height: 5}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .hasSizeOf(1)
              .first()
              .arithmeticalOperation()
              .first()
              .hasPreprocessedSource("Person.Age")
              .propertyValue()
              .hasPath("Person.Age")
              .hasType(DataPropertyType.Decimal)
              .hasPreprocessedSource("Person.Age")
              .parentOperation()
              .second()
              .hasPreprocessedSource(
                  Constants.MULTIPLY_TOKEN + Constants.KEYWORD_SYMBOL + "MULTIPLY Giraffe.Height")
              .propertyValue()
              .hasPath("Giraffe.Height")
              .hasType(DataPropertyType.Decimal)
              .hasPreprocessedSource("Giraffe.Height");
        });
  }

  @Test
  void static_variables_in_arithmetic_have_source() throws Exception {
    String input = GrammarBuilder.create().with("Age").MULTIPLY("Height").AS("variable").getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("Age", DataPropertyType.Decimal));
    schema.addVariable(new DataVariableReference("Height", DataPropertyType.Decimal));

    ASTModel ast = ANTLRExecutor.run(input, schema);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .hasPreprocessedSource("Age")
        .variableValue()
        .hasName("Age")
        .hasType(DataPropertyType.Decimal)
        .hasPreprocessedSource("Age")
        .parentOperation()
        .second()
        .hasPreprocessedSource(
            Constants.MULTIPLY_TOKEN + Constants.KEYWORD_SYMBOL + "MULTIPLY Height")
        .variableValue()
        .hasName("Height")
        .hasType(DataPropertyType.Decimal)
        .hasPreprocessedSource("Height");
  }

  @Test
  void arithmetic_operand_on_right_side_should_be_null_using_spaces() throws Exception {
    String input = GrammarBuilder.create().with("2").MULTIPLY("   ").AS("variable").toString();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasPreprocessedSource()
        .hasNoOperand();
  }

  @Test
  void arithmetic_operand_on_right_side_should_be_null_using_UNIX_NL() throws Exception {
    String input = GrammarBuilder.create().with("2").MULTIPLY("\n\n").AS("variable").toString();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasPreprocessedSource()
        .hasNoOperand();
  }

  @Test
  void arithmetic_operand_on_right_side_should_be_null_using_UNIX_NL_and_spaces() throws Exception {
    String input =
        GrammarBuilder.create().with("2").MULTIPLY("   \n  \n  ").AS("variable").toString();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasPreprocessedSource()
        .hasNoOperand();
  }

  @Test
  void arithmetic_operand_on_right_side_should_be_null_using_WINDOWS_NL_and_spaces()
      throws Exception {
    String input =
        GrammarBuilder.create().with("2").MULTIPLY("   \r\n  \r\n   ").AS("variable").toString();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .arithmeticalOperation()
        .first()
        .staticNumber(2.0)
        .parentOperation()
        .second()
        .hasPreprocessedSource()
        .hasNoOperand();
  }
}
