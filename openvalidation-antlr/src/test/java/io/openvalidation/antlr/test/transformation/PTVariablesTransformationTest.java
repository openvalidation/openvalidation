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
import static io.openvalidation.common.utils.NameMasking.getMaskOf;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PTVariablesTransformationTest {

  @Test
  void simple_variable_with_string_as_value() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("a value").AS("variableName").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variableName")
        .hasCodeSafeName("variableName")
        .hasString("a value");
  }

  @Test
  void simple_variable_with_integer_as_value() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("1337").AS("variableName").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("variableName")
        .hasCodeSafeName("variableName")
        .hasNumber(1337d);
  }

  @Test
  void simple_variable_with_decimal_as_value() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(3.1415).AS("Pie").getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("Pie")
        .hasCodeSafeName("Pie")
        .hasNumber(3.1415);
  }

  @Test
  void simple_variable_with_simple_accessor_as_value() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create().with("Baum.Stamm.Ast.Zweig.Blatt").AS("Baumblatt").getText();

    DataSchema schema = new DataSchema();
    schema.addProperty("Blatt", "Baum.Stamm.Ast.Zweig", DataPropertyType.Object);

    // act
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("Baumblatt")
        .hasCodeSafeName("Baumblatt")
        .operandProperty()
        .hasPath("Baum.Stamm.Ast.Zweig.Blatt")
        .hasType(DataPropertyType.Object);
  }

  @Test
  void simple_variable_with_spaces_in_name() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("value").AS("A name").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("A name")
        .hasCodeSafeName("A" + getMaskOf(' ') + "name")
        .hasString("value");
  }

  @Test
  void simple_variable_with_ampersand_in_name() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("value").AS("Tom&Jerry").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("Tom&Jerry")
        .hasCodeSafeName("Tom" + getMaskOf('&') + "Jerry")
        .hasString("value");
  }

  @Test
  void simple_variable_with_hash_in_name() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("value").AS("#namesAreCool").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("#namesAreCool")
        .hasCodeSafeName(getMaskOf('#') + "namesAreCool")
        .hasString("value");
  }

  @Test
  void simple_variable_with_uncommon_characters() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("value").AS("Übergröße").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("Übergröße")
        .hasCodeSafeName(getMaskOf('Ü') + "bergr" + getMaskOf('ö') + getMaskOf('ß') + "e")
        .hasString("value");
  }

  @Test
  void simple_variable_with_condition_as_value() throws Exception {
    // assemble
    String input = GrammarBuilder.create().EQ("leftOperand", "rightOperand").AS("name").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("name")
        .hasCodeSafeName("name")
        .hasPreprocessedSource(input)
        .condition();
  }

  @Test
  void simple_variable_with_condition_group_as_value() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .EQ("leftOperand", "rightOperand")
            .AND()
            .EQ("leftOperand2", "rightOperand2")
            .AS("name")
            .toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("name")
        .hasCodeSafeName("name")
        .hasPreprocessedSource(input)
        .conditionGroup();
  }

  @Test
  void simple_variable_with_arithmetic_as_value() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(123).ADD(456).AS("name").toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("name")
        .hasCodeSafeName("name")
        .hasPreprocessedSource(input)
        .arithmetic();
  }

  @Test
  void static_numbers_have_source() throws Exception {
    String input = GrammarBuilder.create().with(1).AS("variable").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).hasSizeOf(1).first().number().hasValue(1.0).hasPreprocessedSource("1");
  }

  @Test
  void static_string_has_source() throws Exception {
    String input = GrammarBuilder.create().with("This is a banana").AS("variable").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .string()
        .hasValue("This is a banana")
        .hasPreprocessedSource("This is a banana");
  }

  @ParameterizedTest
  @CsvSource({"age", "the age", "the age in years"})
  void accessor_on_unique_property(String s) throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(s).AS("person's age").toString();

    String schema = "{Memberlist: {Employee: {Person: {Age: 20}}}}";

    // act
    ANTLRRunner.run(
        input,
        schema,
        r -> {

          // assert
          r.variables()
              .hasSizeOf(1)
              .first()
              .shouldNotBeEmpty()
              .hasName("person's age")
              .operandProperty()
              .hasPath("Memberlist.Employee.Person.Age");
        });
  }

  @Disabled
  @ParameterizedTest
  @CsvSource({
    "age",
    "the age",
    "the age in years",
  })
  void accessor_on_unique_array_property(String s) throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(s).AS("Günther's age").toString();

    String schema =
        "{Company: {Memberlist: [{Name: Otto, Backwards: ottO}, {Name: Peter, Haircolour: brown}, {Name: Günther, Age: 25}]}}}";

    // act
    ANTLRRunner.run(
        input,
        schema,
        r -> {

          // assert
          r.variables()
              .hasSizeOf(1)
              .first()
              .shouldNotBeEmpty()
              .hasName("Günther's age")
              .operandProperty()
              .hasPath("Company.Memberlist.Age");
        });
  }

  @ParameterizedTest
  @CsvSource({"Günther.Age", "the Günther.Age", "the Günther.Age in years"})
  void accessor_on_unique_property_with_additional_path(String s) throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(s).AS("Günther's age").toString();

    String schema =
        "{Memberlist: {Otto: {Backwards: ottO}, Peter: {Haircolour: brown}, Günther: {Age: 25}}}";

    // act
    ANTLRRunner.run(
        input,
        schema,
        r -> {

          // assert
          r.variables()
              .hasSizeOf(1)
              .first()
              .shouldNotBeEmpty()
              .hasName("Günther's age")
              .operandProperty()
              .hasPath("Memberlist.Günther.Age");
        });
  }
}
