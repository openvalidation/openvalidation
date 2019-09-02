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

package io.openvalidation.antlr.test.transformation.postprocessing;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertRules;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class ArrayOperationTest {
  @Test
  void test_array_implicit_at_least_one_of() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .EQ()
            .with("Boris, Donald, Jimmy")
            .THEN("mööp")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
        .leftString()
        .hasValue("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void test_array_explicit_at_least_one_of() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .AT_LEAST_ONE_OF("Boris, Donald, Jimmy")
            .THEN("mööp")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
        .leftString()
        .hasValue("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void test_array_implicit_none_of() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .NEQ()
            .with("Boris, Donald, Jimmy")
            .THEN("mööp")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.NONE_OF)
        .leftString()
        .hasValue("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void test_array_explicit_none_of() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .NONE_OF("Boris, Donald, Jimmy")
            .THEN("mööp")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.NONE_OF)
        .leftString()
        .hasValue("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void test_array_implicit_at_least_one_of_with_sugar_surrounding_array() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .EQ()
            .with("to Boris, Donald, Jimmy")
            .THEN("mööp")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
        .leftString()
        .hasValue("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void test_array_implicit_at_least_one_of_with_sugar_after_left_operand() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name is")
            .EQ()
            .with("Boris, Donald, Jimmy")
            .THEN("mööp")
            .toString();

    DataSchema schema = new DataSchema();
    schema.addVariable("name", DataPropertyType.String);
    ASTModel astActual = ANTLRExecutor.run(input, schema);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
        .leftVariable()
        .hasName("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void test_array_implicit_at_least_one_of_very_semantic() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name is")
            .EQ()
            .with("to Boris, Donald or Jimmy")
            .THEN("mööp")
            .toString();

    DataSchema schema = new DataSchema();
    schema.addVariable("name", DataPropertyType.String);
    ASTModel astActual = ANTLRExecutor.run(input, schema);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
        .leftVariable()
        .hasName("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void test_array_implicit_at_least_one_of_with_semantic_array_delimiter() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .EQ()
            .with("to Boris, Donald or Jimmy")
            .THEN("mööp")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
        .leftString()
        .hasValue("name")
        .parentCondition()
        .rightArray()
        .hasSize(3)
        .StringAtPosition(0)
        .hasValue("Boris")
        .parentArray()
        .StringAtPosition(1)
        .hasValue("Donald")
        .parentArray()
        .StringAtPosition(2)
        .hasValue("Jimmy");
  }

  @Test
  void should_not_recognize_an_array() throws Exception {

    //        IF  the income of the applicant is SMALLER 79000
    //        AND  the pseudonym of the applicant IS Mycroft Holmes,
    //            OR his pseudonym EQUALS Sherlock Holmes
    //        THEN  the applicant is a genius

    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("the income of the applicant is")
            .LESS_THAN(79000)
            .AND()
            .with("the pseudonym of the applicant")
            .EQ()
            .with("Mycroft Holmes,")
            .THEN("the applicant is a genius")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(2)
        .second()
        .hasOperator(ASTComparisonOperator.EQUALS);
  }
}
