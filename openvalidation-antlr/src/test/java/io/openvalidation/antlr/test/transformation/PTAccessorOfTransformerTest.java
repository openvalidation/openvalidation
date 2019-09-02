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

import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PTAccessorOfTransformerTest {

  @Test
  void test_simple_ofAccessor_in_variable_without_schema() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("age").OF("person").AS("Person's age").toString();

    // act
    ANTLRRunner.run(
        input,
        "{}",
        r -> {
          // assert
          r.variables().hasSizeOf(1).first().hasName("Person's age").hasString("age OF person");
        });
  }

  @Test
  void test_simple_ofAccessor_in_variable_with_schema() throws Exception {
    // assemble
    String input = GrammarBuilder.create().with("age").OF("person").AS("person's age").toString();

    String schema = "{Person: {Age: 20}}";

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
              .hasPath("Person.Age")
              .hasPreprocessedSource(GrammarBuilder.create().with("age").OF("person").toString());
        });
  }

  @Test
  void of_should_be_text_if_not_exists() throws Exception {

    String input =
        GrammarBuilder.create().with("age").OF("of", "a person").AS("persons age").toString();

    // act
    ANTLRRunner.run(
        input,
        "{}",
        r -> {
          r.variables().hasSizeOf(1).first().hasName("persons age").hasString("age of a person");
        });
  }

  @ParameterizedTest
  @CsvSource({
    "age, the person",
    "age, person number 1",
    "age, the person number 1",
    "the age, the person",
    "the age, person number 1",
    "the age, the person number 1",
    "the age in years, the person",
    "the age in years, person number 1",
    "the age in years, the person number 1"
  })
  void of_accessor_with_sugar_and_both_parts_as_single_element_paths(String left, String right)
      throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(left).OF(right).AS("person's age").toString();

    String schema = "{Person: {Age: 20}}";

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
              .hasPath("Person.Age");
        });
  }

  @ParameterizedTest
  @CsvSource({
    "age, the employee.person",
    "age, employee.person number 1",
    "age, the employee.person number 1",
    "the age, the employee.person",
    "the age, employee.person number 1",
    "the age, the employee.person number 1",
    "the age in years, the employee.person",
    "the age in years, employee.person number 1",
    "the age in years, the employee.person number 1",
    "person.age, the employee",
    "person.age, employee number 1",
    "person.age, the employee number 1",
    "the person.age, the employee",
    "the person.age, employee number 1",
    "the person.age, the employee number 1",
    "the person.age in years, the employee",
    "the person.age in years, employee number 1",
    "the person.age in years, the employee number 1"
  })
  void of_accessor_with_sugar_and_one_part_as_multi_element_path(String left, String right)
      throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(left).OF(right).AS("person's age").toString();

    String schema = "{Employee: {Person: {Age: 20}}}";

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
              .hasPath("Employee.Person.Age");
        });
  }

  @ParameterizedTest
  @CsvSource({
    "person.age, the memberlist.employee",
    "person.age, memberlist.employee number 1",
    "person.age, the memberlist.employee number 1",
    "the person.age, the memberlist.employee",
    "the person.age, memberlist.employee number 1",
    "the person.age, the memberlist.employee number 1",
    "the person.age in years, the memberlist.employee",
    "the person.age in years, memberlist.employee number 1",
    "the person.age in years, the memberlist.employee number 1"
  })
  void of_accessor_with_sugar_and_both_parts_as_multi_element_paths(String left, String right)
      throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(left).OF(right).AS("person's age").toString();

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

  @ParameterizedTest
  @CsvSource({
    "of person.age, the memberlist.employee",
    "of person.age, memberlist.employee number 1",
    "of person.age, the memberlist.employee number 1",
    "of the of person.age, the memberlist.employee",
    "of the of person.age, memberlist.employee number 1",
    "of the of person.age, the memberlist.employee number 1",
    "of the of person.age of in of years, the memberlist.employee",
    "of the of person.age of in of years, memberlist.employee number 1",
    "of the of person.age of in of years, the memberlist.employee number 1"
  })
  void of_accessor_with_of_keyword_as_sugar_on_property_side(String left, String right)
      throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(left).OF(right).AS("person's age").toString();

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

  @ParameterizedTest
  @CsvSource({
    "person.age, of the memberlist.employee",
    "person.age, of memberlist.employee of number of 1",
    "person.age, of the of memberlist.employee of number of 1",
    "the person.age, of the of memberlist.employee",
    "the person.age, of memberlist.employee of number of 1",
    "the person.age, of the of memberlist.employee of number of 1",
    "the person.age in years, of the of memberlist.employee",
    "the person.age in years, of memberlist.employee of number of 1",
    "the person.age in years, of the memberlist.employee of number of 1"
  })
  void of_accessor_with_of_keyword_as_sugar_on_entity_side(String left, String right)
      throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(left).OF(right).AS("person's age").toString();

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

  @ParameterizedTest
  @CsvSource({
    "age, the person",
    "age, person number 1",
    "age, the person number 1",
    "the age, the person",
    "the age, person number 1",
    "the age, the person number 1",
    "the age in years, the person",
    "the age in years, person number 1",
    "the age in years, the person number 1"
  })
  @Disabled
  void of_accessor_with_unique_property(String left, String right) throws Exception {
    // assemble
    String input = GrammarBuilder.create().with(left).OF(right).AS("person's age").toString();

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

  // TODO: lazevedo 16.7.19 not yet working, resolution of the variable name is necessary here

  @ParameterizedTest
  @CsvSource({
    "person.age, the employee",
    "person.age, employee number 1",
    "person.age, the employee number 1",
    "the person.age, the employee",
    "the person.age, employee number 1",
    "the person.age, the employee number 1",
    "the person.age in years, the employee",
    "the person.age in years, employee number 1",
    "the person.age in years, the employee number 1",
    "age, the employee.person",
    "age, employee.person number 1",
    "age, the employee.person number 1",
    "the age, the employee.person",
    "the age, employee.person number 1",
    "the age, the employee.person number 1",
    "the age in years, the employee.person",
    "the age in years, employee.person number 1",
    "the age in years, the employee.person number 1"
  })
  @Disabled
  void of_accessor__with_variable_with_sugar(String left, String variableContent) throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .VARIABLE(variableContent, "var")
            .PARAGRAPH()
            .with(left)
            .OF("var")
            .AS("person's age")
            .toString();

    String schema = "{Employee: {Person: {Age: 20}}}";

    // act
    ANTLRRunner.run(
        input,
        schema,
        a -> {

          // assert
          a.variables()
              .hasSizeOf(2)
              .second()
              .shouldNotBeEmpty()
              .hasName("person's age")
              .operandProperty()
              .hasPath("employee.person.age");
        });
  }
}
