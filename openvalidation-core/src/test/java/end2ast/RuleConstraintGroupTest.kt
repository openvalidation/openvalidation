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

package end2ast

import io.openvalidation.common.ast.ASTArithmeticalOperator
import io.openvalidation.common.ast.ASTComparisonOperator
import io.openvalidation.common.ast.condition.ASTConditionConnector
import io.openvalidation.common.data.DataPropertyType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class RuleConstraintGroupTest {

    @Test
    fun nested_connector_test(){
        var input = """
            10 AS Employees

            5 AS Multiplier

            100 MUST be GREATER all Our Employees AND 100 MUST be FEWER Multiplier
        """.trimIndent()

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError("100 MUST be GREATER all Our Employees AND 100 MUST be FEWER Multiplier")
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasNoConnector()
                            .first()
                                .hasPreprocessedSource()
                                .hasNoConnector()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
        }
    }


    @ParameterizedTest
    @CsvSource(
             "NumVar MUST be AT LEAST 10 OR NumVar MUST be AT MOST 50, LESS_THAN, GREATER_THAN"
            ,"Some NumVar of someone SHOULD be GREATER OR EQUAL 10 OR NumVar MUST LESS OR EQUALS 50, LESS_THAN, GREATER_THAN"
            ,"NumVar HAS to be GREATER THAN 10 OR NumVar MUST be BIGGER THAN 50, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"NumVar HAVE to be MORE THAN 10 OR NumVar MUST EXCEED 50, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"the NumVar of a person MUST be SMALLER THAN 10 OR NumVar MUST be LESS THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"A NumVar SHOULD be SMALLER than 10 OR NumVar MUST be SMALLER THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAS to be LOWER THAN 10 OR NumVar MUST be FEWER THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAVE to be LESS OR EQUALS the 10 OR NumVar MUST be LOWER THAN 50, GREATER_THAN, GREATER_OR_EQUALS"
            ,"Variable NumVar MUST EQUALS the number 10 OR the variable NumVar MUST NOT EQUALS the critical 50 in number, NOT_EQUALS, EQUALS"
            ,"The NumVar MUST NOT EQUALS the forbidden number 10 OR the NumVar MUST be GREATER THAN the number 50, EQUALS, LESS_OR_EQUALS"
            ,"The NumVar SHOULD BE LESS THAN a number 10 OR NumVar SHOULD NOT EQUALS the number 50, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar HAVE NOT to be EQUALS the forbidden number 10 OR NumVar MUST be AT LEAST the number 50, EQUALS, LESS_THAN"
            ,"The NumVar HAVE TO BE SMALLER THAN a number 10 OR NumVar MUSTN'T EQUALS the number 50, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar SHOULDN'T be EQUALS the forbidden number 10 OR NumVar MUST be GREATER OR EQUALS the number 50, EQUALS, LESS_THAN"
            ,"Variable NumVar HAS TO EQUALS the number 10 OR the variable NumVar HAVE be LOWER than the critical 50 in number, NOT_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar MUSTN'T be EQUALS the number 10 OR NumVar HAS TO be AT MOST 50 in number, EQUALS, GREATER_THAN"
    )
    fun constraints_or_number_variable_static_number(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        var input = """
            15 AS NumVar

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasName("NumVar")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasNoConnector()
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(expectedOperator1)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasName("NumVar")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(expectedOperator2)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("NumVar")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(50.0)
                                    .hasPreprocessedSource()
        }


    }


    @ParameterizedTest
    @CsvSource(
             "NumVar MUST be AT LEAST 10 AND NumVar MUST be AT MOST 50, LESS_THAN, GREATER_THAN"
            ,"Some NumVar of someone SHOULD be GREATER OR EQUAL 10 AND NumVar MUST LESS OR EQUALS 50, LESS_THAN, GREATER_THAN"
            ,"NumVar HAS to be GREATER THAN 10 AND NumVar MUST be BIGGER THAN 50, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"NumVar HAVE to be MORE THAN 10 AND NumVar MUST EXCEED 50, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"the NumVar of a person MUST be SMALLER THAN 10 AND NumVar MUST be LESS THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"A NumVar SHOULD be SMALLER than 10 AND NumVar MUST be SMALLER THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAS to be LOWER THAN 10 AND NumVar MUST be FEWER THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAVE to be LESS OR EQUALS the 10 AND NumVar MUST be LOWER THAN 50, GREATER_THAN, GREATER_OR_EQUALS"
            ,"Variable NumVar MUST EQUALS the number 10 AND the variable NumVar MUST NOT EQUALS the critical 50 in number, NOT_EQUALS, EQUALS"
            ,"The NumVar MUST NOT EQUALS the forbidden number 10 AND the NumVar MUST be GREATER THAN the number 50, EQUALS, LESS_OR_EQUALS"
            ,"The NumVar SHOULD BE LESS THAN a number 10 AND NumVar SHOULD NOT EQUALS the number 50, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar HAVE NOT to be EQUALS the forbidden number 10 AND NumVar MUST be AT LEAST the number 50, EQUALS, LESS_THAN"
            ,"The NumVar HAVE TO BE SMALLER THAN a number 10 AND NumVar MUSTN'T EQUALS the number 50, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar SHOULDN'T be EQUALS the forbidden number 10 AND NumVar MUST be GREATER OR EQUALS the number 50, EQUALS, LESS_THAN"
            ,"Variable NumVar HAS TO EQUALS the number 10 AND the variable NumVar HAVE be LOWER than the critical 50 in number, NOT_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar MUSTN'T be EQUALS the number 10 AND NumVar HAS TO be AT MOST 50 in number, EQUALS, GREATER_THAN"
    )
    fun constraints_and_number_variable_static_number(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        var input = """
            15 AS NumVar

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("NumVar")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasNoConnector()
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(expectedOperator1)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasName("NumVar")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(expectedOperator2)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("NumVar")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(50.0)
                                    .hasPreprocessedSource()
        }


    }


//    TODO: TB 30.04.19: Tests mit 'new employee'  funktionieren nicht
    @ParameterizedTest
    @CsvSource(
         "Status MUST be new AND (5 * 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
        ,"Some Status of someone SHOULD be new AND (5 TIMES 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
        ,"Status HAS to be new AND (5 * 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
        ,"Status HAVE to be new AND (5 TIMES 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
        ,"the Status of a person MUST be a new AND (5 * 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
//        ,"A Status SHOULD new employee AND (5 * 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
        ,"The Status HAS to be the new AND (5 TIMES 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
//        ,"The Status HAVE to be a new employee AND (5 * 3) MUST be GREATER THAN 10, NOT_EQUALS, LESS_OR_EQUALS"
        ,"Status MUST EQUALS new AND (5 * 3) MUST NOT be GREATER THAN 10, NOT_EQUALS, GREATER_THAN"
        ,"The variable Status MUST NOT EQUALS new AND (5 TIMES 3) HAS to be GREATER THAN 10, EQUALS, LESS_OR_EQUALS"
        ,"The Status HAS TO EQUAL new AND (5 * 3) HAS to be AT MOST 10, NOT_EQUALS, GREATER_THAN"
        ,"The variable Status SHOULD EQUALS new AND (5 TIMES 3) SHOULDN'T be LESS OR EQUALS THE Number 10, NOT_EQUALS, LESS_OR_EQUALS"
        ,"The Status HAVE NOT TO EQUALS new AND (5 * 3) HAVE to be LOWER the 10 number, EQUALS, GREATER_OR_EQUALS"
        ,"The variable Status HAVE TO be EQUALS new AND (5 TIMES 3) HASN'T to be SMALLER THAN the Number 10, NOT_EQUALS, LESS_THAN"
        ,"variable Status MUSTN'T EQUALS new AND (5 * 3) HAVEN'T to be GREATER OR EQUAL 10, EQUALS, GREATER_OR_EQUALS"
        ,"variable Status SHOULDN'T EQUALS new AND (5 TIMES 3) MUSTN'T be AT LEAST 10, EQUALS, GREATER_OR_EQUALS"
    )
    fun constraints_and_variable_string_static_numbers(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        var input = """
            new AS Status

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasName("Status")
                        .hasString("new")
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasNoConnector()
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(expectedOperator1)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasName("Status")
                            .parentCondition()
                                .rightString()
                                    .hasValue("new")
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(expectedOperator2)
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .leftArithmetical()
                                    .operation()
                                        .hasSizeOf(2)
                                        .hasPreprocessedSource()
                                        .first()
                                            .hasOperator(null)
                                            .staticNumber(5.0)
                                            .hasPreprocessedSource()
                                    .parentOperation()
                                        .second()
                                            .staticNumber(3.0)
                                            .hasPreprocessedSource()
        }

    }


    @ParameterizedTest
    @CsvSource(
            "NumVar MUST be AT LEAST 10 AND (5 * NumVar ) MUST be GREATER THAN 50, LESS_THAN, LESS_OR_EQUALS"
            ,"Some NumVar of someone SHOULD be GREATER OR EQUAL 10 AND (5 TIMES the NumVar ) MUST be LARGER THAN 50, LESS_THAN, LESS_OR_EQUALS"
            ,"NumVar HAS to be GREATER THAN 10 AND (5 * NumVar ) MUST be BIGGER THAN 50, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"NumVar HAVE to be MORE THAN 10 AND (5 TIMES NumVar ) MUST EXCEED 50, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"the NumVar of a person MUST be SMALLER THAN 10 AND (5 * NumVar ) MUST be LESS THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"A NumVar SHOULD be SMALLER than 10 AND (5 * NumVar) MUST be SMALLER THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAS to be LOWER THAN 10 AND (5 TIMES NumVar ) MUST be FEWER THAN 50, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAVE to be LESS OR EQUALS the 10 AND (5 * NumVar) MUST be LOWER THAN 50, GREATER_THAN, GREATER_OR_EQUALS"
            ,"Variable NumVar MUST EQUALS the number 10 AND (the number 5 * the variable NumVar ) MUST NOT EQUALS the critical 50 in number, NOT_EQUALS, EQUALS"
            ,"The NumVar MUST NOT EQUALS the forbidden number 10 AND (5 TIMES the NumVar ) MUST be GREATER THAN the number 50, EQUALS, LESS_OR_EQUALS"
            ,"The NumVar SHOULD BE LESS THAN a number 10 AND (5 * NumVar ) SHOULD NOT EQUALS the number 50, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar HAVE NOT to be EQUALS the forbidden number 10 AND (5 TIMES NumVar ) MUST be AT LEAST the number 50, EQUALS, LESS_THAN"
            ,"The NumVar HAVE TO BE SMALLER THAN a number 10 AND (5 * NumVar ) MUSTN'T EQUALS the number 50, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar SHOULDN'T be EQUALS the forbidden number 10 AND (5 TIMES a NumVar ) MUST be GREATER OR EQUALS the number 50, EQUALS, LESS_THAN"
            ,"Variable NumVar HAS TO EQUALS the number 10 AND (the number 5 * the variable NumVar ) HAVE be LOWER than the critical 50 in number, NOT_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar MUSTN'T be EQUALS the number 10 AND (5 TIMES NumVar ) HAS TO be AT MOST 50 in number, EQUALS, GREATER_THAN"
    )
    fun constraints_and_number_variable_static_numbers(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        var input = """
            15 AS NumVar

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("NumVar")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasNoConnector()
                            .first()
                                .hasOperator(expectedOperator1)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasName("NumVar")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(expectedOperator2)
                                .rightNumber()
                                    .hasValue(50.0)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasOperator(null)
                                            .staticNumber(5.0)
                                    .parentOperation()
                                        .second()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("NumVar")
                                                .hasType(DataPropertyType.Decimal)
        }


    }


    @ParameterizedTest
    @CsvSource(
             "NumVar - WorkUnit.NumberOfPeople MUST be AT LEAST 0 AND (100 + NumVar ) DIVIDED BY ( WorkUnit.NumberOfPeople - 10) TIMES 1000 MUST be GREATER THAN 1000, LESS_THAN, LESS_OR_EQUALS"
            ,"Some NumVar - WorkUnit.NumberOfPeople SHOULD be GREATER OR EQUAL 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST be LARGER THAN 1000, LESS_THAN, LESS_OR_EQUALS"
            ,"NumVar - WorkUnit.NumberOfPeople HAS to be GREATER THAN 0 AND (100 + NumVar ) DIVIDED BY ( WorkUnit.NumberOfPeople - 10) TIMES 1000 MUST be BIGGER THAN 1000, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"NumVar - WorkUnit.NumberOfPeople HAVE to be MORE THAN 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST EXCEED 1000, LESS_OR_EQUALS, LESS_OR_EQUALS"
            ,"the NumVar - WorkUnit.NumberOfPeople of a person MUST be SMALLER THAN 0 AND (100 + NumVar ) DIVIDED BY ( WorkUnit.NumberOfPeople - 10) TIMES 1000 MUST be LESS THAN 1000, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"A NumVar - WorkUnit.NumberOfPeople SHOULD be SMALLER than 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST be SMALLER THAN 1000, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar - WorkUnit.NumberOfPeople HAS to be LOWER THAN 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST be FEWER THAN 1000, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar - WorkUnit.NumberOfPeople HAVE to be LESS OR EQUALS the 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST be LOWER THAN 1000, GREATER_THAN, GREATER_OR_EQUALS"
            ,"Variable NumVar - WorkUnit.NumberOfPeople MUST EQUALS the number 0 AND (the number 100 + the variable NumVar ) DIVIDED BY ( WorkUnit.NumberOfPeople - 10) TIMES 1000 MUST NOT EQUALS the critical 1000 in number, NOT_EQUALS, EQUALS"
            ,"The NumVar - WorkUnit.NumberOfPeople MUST NOT EQUALS the forbidden number 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST be GREATER THAN the number 1000, EQUALS, LESS_OR_EQUALS"
            ,"The NumVar - WorkUnit.NumberOfPeople SHOULD BE LESS THAN a number 0 AND (100 + NumVar ) DIVIDED BY ( WorkUnit.NumberOfPeople - 10) TIMES 1000 SHOULD NOT EQUALS the number 1000, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar - WorkUnit.NumberOfPeople HAVE NOT to be EQUALS the forbidden number 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST be AT LEAST the number 1000, EQUALS, LESS_THAN"
            ,"The NumVar - WorkUnit.NumberOfPeople HAVE TO BE SMALLER THAN a number 0 AND (100 + NumVar ) DIVIDED BY ( WorkUnit.NumberOfPeople - 10) TIMES 1000 MUSTN'T EQUALS the number 1000, GREATER_OR_EQUALS, EQUALS"
            ,"The NumVar - WorkUnit.NumberOfPeople SHOULDN'T be EQUALS the forbidden number 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 MUST be GREATER OR EQUALS the number 1000, EQUALS, LESS_THAN"
            ,"Variable NumVar - WorkUnit.NumberOfPeople HAS TO EQUALS the number 0 AND (the number 100 + the variable NumVar ) DIVIDED BY ( WorkUnit.NumberOfPeople - 10) TIMES 1000 HAVE be LOWER than the critical 1000 in number, NOT_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar - WorkUnit.NumberOfPeople MUSTN'T be EQUALS the number 0 AND (100 PLUS NumVar ) / ( WorkUnit.NumberOfPeople MINUS 10) * 1000 HAS TO be AT MOST 1000 in number, EQUALS, GREATER_THAN"
    )
    fun constraints_and_number_variable_nested_property_static_numbers(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        var input = """
            15 AS NumVar

            $paramStr
            """


        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 100}}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("NumVar")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasNoConnector()
                            .hasSize(2)
                            .first()
                                .hasPreprocessedSource()
                                .hasNoConnector()
                                .hasIndentationLevel(0)
                                .hasOperator(expectedOperator1)
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .variableValue()
                                                    .hasName("NumVar")
                                                    .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Subtraction)
                                            .propertyValue()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasPath("WorkUnit.NumberOfPeople")
                                    .parentOperation()
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(.0)
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasIndentationLevel(0)
                                .hasOperator(expectedOperator2)
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(3)
                                        .firstSubOperation()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .hasSizeOf(2)
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .staticNumber(100.0)
                                        .parentOperation()
                                            .second()
                                                .hasPreprocessedSource()
                                                .hasOperator(ASTArithmeticalOperator.Addition)
                                                .variableValue()
                                                    .hasPreprocessedSource()
                                                    .hasType(DataPropertyType.Decimal)
                                                    .hasName("NumVar")
                                        .parentOperation()
                                .parentOperation()
                                    .secondSubOperation()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Division)
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            //to add var
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Subtraction)
                                            .staticNumber(10.0)
                                    .parentOperation()
                                .parentOperation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Multiplication)
                                        .staticNumber(1000.0)
                                .parentOperation()
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(1000.0)
                                    .hasPreprocessedSource()

        }


    }

    @ParameterizedTest
    @CsvSource(
            "(Constant Value 80 + the Variable WorkUnit.NumberOfPeople ) / the Scale 100 MUST be GREATER all Our Employees * the Standard Multiplier AND (Another Constant 100 + Employees ) / 100 MUST be FEWER the Variable WorkUnit.NumberOfPeople * the Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD at MOST Employees TIMES Multiplier AND (100 + Employees ) / 100 MUST LESS OR EQUAL the WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, GREATER_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS to be MORE then Employees * Multiplier AND (Number 100 + Employees ) / 100 MUST SMALLER the value in WorkUnit.NumberOfPeople * the Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(The Number 80 PLUS the Value Of WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be at LEAST the number Employees TIMES Multiplier AND (Constant 100 + the Employees ) / number 100 MUST AT MOST the Property WorkUnit.NumberOfPeople * the Multiplier, LESS_THAN, GREATER_THAN"
            ,"(80 + the WorkUnit.NumberOfPeople ) / 100 MUST be at MOST Employees stuff * Multiplier AND (The Constant 100 + Employees ) / 100 MUST be AT LEAST the Value in WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD BIGGER then a Employees TIMES Multiplier AND (Another 100 + Employees ) / 100 MUST be LOWER THAN the Variable WorkUnit.NumberOfPeople * the Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(80 Number + The WorkUnit.NumberOfPeople Property ) / the 100 Standard HAS to LESS OR EQUAL the Employees Value * the new Multiplier AND (The 100 + Employees ) / 100 MUST be GREATER OR EQUAL the the WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be MORE than a Employees in number TIMES Multiplier AND (The Value 100 + Employees ) / 100 MUST be LESS the Variable WorkUnit.NumberOfPeople * the Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 MUST be LESS Employees * Multiplier AND (100 + the Employees ) / the 100 MUST be BIGGER THAN Property WorkUnit.NumberOfPeople * the Multiplier, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"(An Arbitrary Number 80 PLUS the Property WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD GREATER OR EQUAL Employees TIMES Multiplier AND (Number 100 + value of Employees ) / the second 100 MUST be LESS OR EQUALS the WorkUnit.NumberOfPeople * Multiplier, LESS_THAN, GREATER_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS to be LESS OR EQUAL Employees * Multiplier AND (Constant 100 + the Employees ) / 100 MUST be GREATER OR EQUAL WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be LESS OR EQUAL then the number Employees TIMES Multiplier AND (Some Constant 100 + value Employees ) / 100 MUST be AT LEAST the WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(The Constant 80 + the Number in WorkUnit.NumberOfPeople ) / Standardizer 100 MUST LARGER all Employees people * the Multiplier AND (Another 100 + number of Employees ) / literal 100 MUST be SMALLER THAN WorkUnit.NumberOfPeople * the Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD LESS than a Employees TIMES Multiplier AND (Another Constant 100 + Employees ) / 100 MUST be MORE the Variable WorkUnit.NumberOfPeople * the Multiplier, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"(80 + The WorkUnit.NumberOfPeople ) / 100 HAS to SMALLER the Employees * Multiplier AND (The literal 100 + our Employees ) / 100 MUST be LARGER the Database Property WorkUnit.NumberOfPeople * the Multiplier, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be MORE than a Employees in number TIMES Multiplier AND (Constant 100 + Employees ) / 100 MUST be LESS than the Variable WorkUnit.NumberOfPeople * Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(The Constant Number 80 + Property Value WorkUnit.NumberOfPeople ) / the Number 100 MUST be EQUALS the Variable Employees * the Big Multiplier AND (Constant 100 + the Employees ) / 100 MUST NOT be EQUALS the number in WorkUnit.NumberOfPeople * the Multiplier, NOT_EQUALS, EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD EQUALS Employees TIMES Multiplier AND (Another Constant 100 + our Employees ) / 100 SHOULD NOT EQUALS the WorkUnit.NumberOfPeople * the Multiplier, NOT_EQUALS, EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS to be EQUALS then Employees * Multiplier AND ( 100 + the Employees ) / 100 HAS NOT EQUALS the Variable WorkUnit.NumberOfPeople * the Multiplier, NOT_EQUALS, EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be at EQUALS the number Employees TIMES Multiplier AND (Another Number Literal 100 + our Employees ) / 100 HAVE NOT EQUALS WorkUnit.NumberOfPeople * the Multiplier, NOT_EQUALS, EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 MUST NOT be GREATER Employees * Multiplier AND (Another Constant 100 + Employees ) / 100 MUST NOT BE LESS the Variable WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(The 80 Again PLUS our good WorkUnit.NumberOfPeople ) DIVIDED BY Standard 100 Number SHOULD NOT be BIGGER than all our Employees TIMES Multiplier AND (100 + Employees ) / 100 MUST NOT BE SMALLER WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS NOT to EXCEED Employees * Multiplier AND (The 100 + Employees ) / 100 MUST NOT BE FEWER THAN the Value of WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE NOT to be LARGER the number Employees TIMES Multiplier AND (Another 100 + Employees ) / 100 MUST NOT BE LOWER the Property Value WorkUnit.NumberOfPeople * Multiplier, GREATER_THAN, LESS_THAN"
            ,"(Constant Value 80 + the Property Number WorkUnit.NumberOfPeople ) / Standard Value 100 MUST NOT be MORE than Employees stuff * Multiplier AND (100 + the value of Employees ) / 100 MUST NOT BE FEWER property WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD NOT LESS a Employees TIMES Multiplier AND (Constant 100 + Employees ) / 100 SHOULD NOT MORE the WorkUnit.NumberOfPeople * Multiplier, LESS_THAN, GREATER_THAN"
            ,"(80 + The WorkUnit.NumberOfPeople ) / 100 HAS NOT to be SMALLER than the Employees * Multiplier AND (Number 100 + Employees ) / 100 HAS NOT GREATER WorkUnit.NumberOfPeople again * Multiplier value, LESS_THAN, GREATER_THAN"
            ,"(The arbitrary Value 80 PLUS Database Value The WorkUnit.NumberOfPeople ) DIVIDED BY Some 100 HAVE NOT to be LOWER than a Employees in number TIMES Multiplier AND (100 + the variable Employees ) / 100 HAVE NOT to BE HIGHER the WorkUnit.NumberOfPeople * the Multiplier, LESS_THAN, GREATER_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 MUSTN'T be LESS OR EQUAL Employees * Multiplier AND (Constant 100 + the Employees ) / 100 MUSTN'T GREATER OR EQUALS Variable WorkUnit.NumberOfPeople * Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULDN'T be at MOST Employees TIMES Multiplier AND (Another Constant 100 + Employees ) / 100 SHOULDN'T be AT LEAST Variable WorkUnit.NumberOfPeople * the one Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HASN'T to be LESS OR EQUALS Employees * Multiplier AND (The Literal 100 + all Employees ) / literal 100 HASN'T to be GREATER OR EQUAL the Property WorkUnit.NumberOfPeople * the Multiplier, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"(Value of 80 PLUS the Number in WorkUnit.NumberOfPeople ) DIVIDED BY the Number 100 HAVEN'T to be GREATER OR EQUAL the number Employees TIMES Standard Multiplier AND (Again 100 + our Employees ) / Some 100 HAVEN'T be LESS OR EQUAL the WorkUnit.NumberOfPeople * the Multiplier, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"(80 + the WorkUnit.NumberOfPeople ) / 100 people MUSTN'T be at LEAST Employees stuff * Multiplier AND (Constant 100 + all Employees ) / 100 MUSTN'T be at MOST the Value WorkUnit.NumberOfPeople * the Multiplier, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULDN'T EXCEED Employees TIMES Multiplier AND (Another Constant 100 + the Employees ) / 100 SHOULDN'T be LESS THAN WorkUnit.NumberOfPeople * Multiplier, GREATER_THAN, LESS_THAN"
            ,"(The Number 80 + The Data Property WorkUnit.NumberOfPeople ) / 100 HASN'T to be MORE the Employees Value * the Global Multiplier AND (Base 100 + additional Employees ) / again 100 HASN'T be LESS the value in WorkUnit.NumberOfPeople * the Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVEN'T to be BIGGER than Employees in number TIMES Multiplier AND (constant 100 + variable Employees ) / 100 HAVEN'T be SMALLER the property WorkUnit.NumberOfPeople * some Multiplier, GREATER_THAN, LESS_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 MUST NOT EQUALS Employees * Multiplier AND (Another Constant 100 + Employees ) / 100 MUST EQUALS Prop WorkUnit.NumberOfPeople * the standard Multiplier value, EQUALS, NOT_EQUALS"
            ,"(The Base of 80 PLUS The Property Value WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD NOT EQUALS than Employees TIMES Multiplier AND (Number 100 + the Employees ) / 100 SHOULD EQUALS WorkUnit.NumberOfPeople * the Multiplier, EQUALS, NOT_EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS NOT to EQUALS Employees * Multiplier AND (Another Constant 100 + Employees ) / 100 HAS TO EQUALS the Property WorkUnit.NumberOfPeople * the Standard Multiplier, EQUALS, NOT_EQUALS"
            ,"(Base Value of 80 PLUS The WorkUnit.NumberOfPeople Property Value ) DIVIDED BY Divisor 100 HAVE NOT to be EQUALS the number Of Employees TIMES the Standard Multiplier AND (The Number 100 + all Employees ) / 100 HAVE to be EQUALS Variable WorkUnit.NumberOfPeople * the Multiplier, EQUALS, NOT_EQUALS"
            ,"(80 + the WorkUnit.NumberOfPeople ) / 100 MUST NOT be EQUALS than Employees stuff * Multiplier AND (Another Constant 100 + Employees ) / 100 MUST be EQUALS the Variable WorkUnit.NumberOfPeople * the Multiplier, EQUALS, NOT_EQUALS"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD NOT EQUALS a Employees TIMES Multiplier AND (Once again 100 + the Employees Variable ) / the number 100 SHOULD be EQUAL the WorkUnit.NumberOfPeople * the Multiplier, EQUALS, NOT_EQUALS"
            ,"(80 + The WorkUnit.NumberOfPeople ) / 100 HAS NOT to be EQUALS than the Employees * Multiplier AND (Literal 100 + Employees value ) / 100 HAS be EQUAL the database value WorkUnit.NumberOfPeople * the Multiplier value, EQUALS, NOT_EQUALS"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE NOT to be EQUALS than a Employees in number TIMES Multiplier AND (Another Constant 100 + Employees ) / 100 HAVE EQUAL the Property WorkUnit.NumberOfPeople * the Multiplier, EQUALS, NOT_EQUALS"
    )
    fun constraints_and_nested_property_variable_number_2arithmetic3_relational(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        var input = """
            10 AS Employees

            5 AS Multiplier

            $paramStr
            """

        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 15}}") {
            r ->
                r.variables()
                    .hasSizeOf(2)
                        .first()
                            .hasName("Employees")
                            .hasNumber(10.0)
                            .hasPreprocessedSource()
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("Multiplier")
                            .hasNumber(5.0)

            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                            .hasNoConnector()
                            .first()
                                .hasPreprocessedSource()
                                .hasNoConnector()
                                .hasOperator(expectedOperator1)
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasSizeOf(2)
                                        .hasNoOperator()
                                        .firstSubOperation()
                                            .hasPreprocessedSource()
                                            .hasSizeOf(2)
                                            .hasNoOperator()
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .staticNumber(80.0)
                                        .parentOperation()
                                            .second()
                                                .hasPreprocessedSource()
                                                .hasOperator(ASTArithmeticalOperator.Addition)
                                                .propertyValue()
                                                    .hasPreprocessedSource()
                                                    .hasPath("WorkUnit.NumberOfPeople")
                                                    .hasType(DataPropertyType.Decimal)
                                        .parentOperation()
                                .parentOperation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Division)
                                        .staticNumber(100.0)
                                .parentOperation()
                            .parentCondition()
                                .rightArithmetical()
                                    .operation()
                                    .hasPreprocessedSource()
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasName("Employees")
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Multiplication)
                                            .variableValue()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasName("Multiplier")
                                                .hasPreprocessedSource()
                                    .parentOperation()
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(expectedOperator2)
                                    .leftArithmetical()
                                        .operation()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .hasSizeOf(2)
                                            .firstSubOperation()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .hasSizeOf(2)
                                                .first()
                                                    .hasPreprocessedSource()
                                                    .hasNoOperator()
                                                    .staticNumber(100.0)
                                            .parentOperation()
                                                .second()
                                                    .hasPreprocessedSource()
                                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                                    .variableValue()
                                                        .hasPreprocessedSource()
                                                        .hasType(DataPropertyType.Decimal)
                                                        .hasName("Employees")
                                            .parentOperation()
                                        .parentOperation()
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasOperator(ASTArithmeticalOperator.Division)
                                                .staticNumber(100.0)
                                        .parentOperation()
                                .parentCondition()
                                    .rightArithmetical()
                                        .operation()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .hasSizeOf(2)
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .propertyValue()
                                                    .hasType(DataPropertyType.Decimal)
                                                    .hasPath("WorkUnit.NumberOfPeople")
                                                    .hasPreprocessedSource()
                                        .parentOperation()
                                            .second()
                                                .hasPreprocessedSource()
                                                .hasOperator(ASTArithmeticalOperator.Multiplication)
                                                .variableValue()
                                                    .hasPreprocessedSource()
                                                    .hasType(DataPropertyType.Decimal)
                                                    .hasName("Multiplier")
        }
    }


//------------------------------------ 2 Logical Operators ----------------------------------

    @ParameterizedTest
    @CsvSource(
             "NumVar MUST be AT LEAST 10 AND NumVar MUST be AT MOST 50 OR NumVar MUST be LESS THAN 0, LESS_THAN, GREATER_THAN, GREATER_OR_EQUALS"
            ,"Some NumVar of someone SHOULD be GREATER OR EQUAL 10 AND NumVar MUST LESS OR EQUALS 50 OR NumVar MUST be LESS OR EQUAL 0, LESS_THAN, GREATER_THAN, GREATER_THAN"
            ,"NumVar HAS to be GREATER THAN 10 AND NumVar MUST be BIGGER THAN 50 OR NumVar MUST be LESS THAN 0, LESS_OR_EQUALS, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"NumVar HAVE to be MORE THAN 10 AND NumVar MUST EXCEED 50 OR NumVar MUST be AT MOST 0, LESS_OR_EQUALS, LESS_OR_EQUALS, GREATER_THAN"
            ,"the NumVar of a person MUST be SMALLER THAN 10 AND NumVar MUST be LESS THAN 50 OR NumVar MUST be LESS THAN 0, GREATER_OR_EQUALS, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"A NumVar SHOULD be SMALLER than 10 AND NumVar MUST be SMALLER THAN 50 OR NumVar MUST be LESS THAN 0, GREATER_OR_EQUALS, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAS to be LOWER THAN 10 AND NumVar MUST be FEWER THAN 50 OR NumVar MUST be LESS THAN 0, GREATER_OR_EQUALS, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAVE to be LESS OR EQUALS the 10 AND NumVar MUST be LOWER THAN 50 OR NumVar MUST be LESS THAN 0, GREATER_THAN, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"Variable NumVar MUST EQUALS the number 10 AND the variable NumVar MUST NOT EQUALS the critical 50 OR NumVar MUST be LESS THAN 0 in number, NOT_EQUALS, EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar MUST NOT EQUALS the forbidden number 10 AND the NumVar MUST be GREATER THAN the number 50 OR NumVar MUST be LESS THAN 0, EQUALS, LESS_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar SHOULD BE LESS THAN a number 10 AND NumVar SHOULD NOT EQUALS the number 50 OR NumVar MUST be LESS THAN 0, GREATER_OR_EQUALS, EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar HAVE NOT to be EQUALS the forbidden number 10 AND NumVar MUST be AT LEAST the number 50 OR NumVar MUST be LESS THAN 0, EQUALS, LESS_THAN, GREATER_OR_EQUALS"
            ,"The NumVar HAVE TO BE SMALLER THAN a number 10 AND NumVar MUSTN'T EQUALS the number 50 OR NumVar MUST be LESS THAN 0, GREATER_OR_EQUALS, EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar SHOULDN'T be EQUALS the forbidden number 10 AND NumVar MUST be GREATER OR EQUALS the number 50 OR NumVar SHOULDN'T be GREATER THAN 0, EQUALS, LESS_THAN, GREATER_THAN"
            ,"Variable NumVar HAS TO EQUALS the number 10 AND the variable NumVar HAVE be LOWER than the critical 50 OR NumVar HASN'T be AT LEAST 0 in number, NOT_EQUALS, GREATER_OR_EQUALS, GREATER_OR_EQUALS"
            ,"The NumVar MUSTN'T be EQUALS the number 10 AND NumVar HAS TO be AT MOST 50 OR NumVar MUSTN'T be HIGHER THAN 0 in number, EQUALS, GREATER_THAN, GREATER_THAN"
    )
    fun constraints_and_or_number_variable_static_number(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator, expectedOperator3: ASTComparisonOperator) {
        var input = """
            15 AS NumVar

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("NumVar")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasNoConnector()
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(expectedOperator1)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasName("NumVar")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(expectedOperator2)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("NumVar")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(50.0)
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .atIndex(2)
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(expectedOperator3)
                                .leftVariable()
                                    .hasName("NumVar")
                                    .hasType(DataPropertyType.Decimal)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(0.0)
                                    .hasPreprocessedSource()
        }


    }


    @ParameterizedTest
    @CsvSource(
            "NumVar MUST be AT LEAST 10 AND (5 * NumVar ) MUST be GREATER THAN 50 OR NumVar MOD 5 MUST EQUALS 0, LESS_THAN, LESS_OR_EQUALS, NOT_EQUALS"
            ,"Some NumVar of someone SHOULD be GREATER OR EQUAL 10 AND (5 TIMES the NumVar ) MUST be LARGER THAN 50 OR NumVar MOD 5 MUST EQUALS 0, LESS_THAN, LESS_OR_EQUALS, NOT_EQUALS"
            ,"NumVar HAS to be GREATER THAN 10 AND (5 * NumVar ) MUST be BIGGER THAN 50 OR NumVar MOD 5 MUST EQUALS 0, LESS_OR_EQUALS, LESS_OR_EQUALS, NOT_EQUALS"
            ,"NumVar HAVE to be MORE THAN 10 AND (5 TIMES NumVar ) MUST EXCEED 50 OR NumVar MOD 5 MUST EQUALS 0, LESS_OR_EQUALS, LESS_OR_EQUALS, NOT_EQUALS"
            ,"the NumVar of a person MUST be SMALLER THAN 10 AND (5 * NumVar ) MUST be LESS THAN 50 OR NumVar MOD 5 MUST EQUALS 0, GREATER_OR_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
            ,"A NumVar SHOULD be SMALLER than 10 AND (5 * NumVar) MUST be SMALLER THAN 50 OR NumVar MOD 5 MUST EQUALS 0, GREATER_OR_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
            ,"The NumVar HAS to be LOWER THAN 10 AND (5 TIMES NumVar ) MUST be FEWER THAN 50 OR NumVar MOD 5 MUST EQUALS 0, GREATER_OR_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
            ,"The NumVar HAVE to be LESS OR EQUALS the 10 AND (5 * NumVar) MUST be LOWER THAN 50 OR NumVar MOD 5 MUST EQUALS 0, GREATER_THAN, GREATER_OR_EQUALS, NOT_EQUALS"
            ,"Variable NumVar MUST EQUALS the number 10 AND (the number 5 * the variable NumVar ) MUST NOT EQUALS the critical 50 in number OR NumVar MOD 5 MUST EQUALS 0, NOT_EQUALS, EQUALS, NOT_EQUALS"
            ,"The NumVar MUST NOT EQUALS the forbidden number 10 AND (5 TIMES the NumVar ) MUST be GREATER THAN the number 50 OR NumVar MOD 5 MUST EQUALS 0, EQUALS, LESS_OR_EQUALS, NOT_EQUALS"
            ,"The NumVar SHOULD BE LESS THAN a number 10 AND (5 * NumVar ) SHOULD NOT EQUALS the number 50 OR NumVar MOD 5 MUST EQUALS 0, GREATER_OR_EQUALS, EQUALS, NOT_EQUALS"
            ,"The NumVar HAVE NOT to be EQUALS the forbidden number 10 AND (5 TIMES NumVar ) MUST be AT LEAST the number 50 OR NumVar MOD 5 MUST EQUALS 0, EQUALS, LESS_THAN, NOT_EQUALS"
            ,"The NumVar HAVE TO BE SMALLER THAN a number 10 AND (5 * NumVar ) MUSTN'T EQUALS the number 50 OR NumVar MOD 5 MUST EQUALS 0, GREATER_OR_EQUALS, EQUALS, NOT_EQUALS"
            ,"The NumVar SHOULDN'T be EQUALS the forbidden number 10 AND (5 TIMES a NumVar ) MUST be GREATER OR EQUALS the number 50 OR NumVar MOD 5 MUST EQUALS 0, EQUALS, LESS_THAN, NOT_EQUALS"
            ,"Variable NumVar HAS TO EQUALS the number 10 AND (the number 5 * the variable NumVar ) HAVE be LOWER than the critical 50 in number OR NumVar MOD 5 MUST EQUALS 0, NOT_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
            ,"The NumVar MUSTN'T be EQUALS the number 10 AND (5 TIMES NumVar ) HAS TO be AT MOST 50 in number OR NumVar MOD 5 MUST EQUALS 0, EQUALS, GREATER_THAN, NOT_EQUALS"
    )
    fun constraints_and_or_number_variable_arithmetic_static_numbers(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator, expectedOperator3: ASTComparisonOperator) {
        var input = """
            15 AS NumVar

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("NumVar")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .conditionGroup()
                        .hasNoConnector()
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(expectedOperator1)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasName("NumVar")
                            .parentCondition()
                                .hasPreprocessedSource()
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(expectedOperator2)
                                .rightNumber()
                                    .hasValue(50.0)
                            .parentCondition()
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasOperator(null)
                                            .staticNumber(5.0)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("NumVar")
                                                .hasType(DataPropertyType.Decimal)
                r.rules()
                    .first()
                        .conditionGroup()
                            .atIndex(2)
                                .hasPreprocessedSource()
                                .hasOperator(expectedOperator3)
                                .hasConnector(ASTConditionConnector.AND)
                                .rightNumber()
                                    .hasValue(0.0)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasOperator(null)
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasName("NumVar")
                r.rules()
                    .first()
                        .conditionGroup()
                            .atIndex(2)
                                .hasPreprocessedSource()
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Modulo)
                                            .staticNumber(5.0)
        }
    }

//    TODO: TB, 08.05.2019: Klammer zu direkt nach Variablen-namen (und Propertynamen) wirft noch Fehler
    @ParameterizedTest
    @CsvSource(
                 "NumVar MUST be AT LEAST 10" +
                 " AND (5 * NumVar ) MUST be GREATER THAN 50" +
                 "     OR NumVar MOD 5 MUST EQUALS 0" +
                 ", LESS_THAN, LESS_OR_EQUALS, NOT_EQUALS"
               , "Some NumVar of someone SHOULD be GREATER OR EQUAL 10" +
                " AND (5 TIMES the NumVar ) MUST be LARGER THAN 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", LESS_THAN, LESS_OR_EQUALS, NOT_EQUALS"
              , "NumVar HAS to be GREATER THAN 10 AND (5 * NumVar ) MUST be BIGGER THAN 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", LESS_OR_EQUALS, LESS_OR_EQUALS, NOT_EQUALS"
              , "NumVar HAVE to be MORE THAN 10 AND (5 TIMES NumVar ) MUST EXCEED 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", LESS_OR_EQUALS, LESS_OR_EQUALS, NOT_EQUALS"
              , "the NumVar of a person MUST be SMALLER THAN 10 AND (5 * NumVar ) MUST be LESS THAN 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", GREATER_OR_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
              ,"A NumVar SHOULD be SMALLER than 10" +
                " AND (5 * NumVar) MUST be SMALLER THAN 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", GREATER_OR_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
              , "The NumVar HAS to be LOWER THAN 10" +
                " AND (5 TIMES NumVar ) MUST be FEWER THAN 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", GREATER_OR_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
//              ,"The NumVar HAVE to be LESS OR EQUALS the 10" +
//                " AND (5 * NumVar) MUST be LOWER THAN 50" +
//                "     OR NumVar MOD 5 MUST EQUALS 0" +
//                ", GREATER_THAN, GREATER_OR_EQUALS, NOT_EQUALS"
              , "Variable NumVar MUST EQUALS the number 10" +
                " AND (the number 5 * the variable NumVar ) MUST NOT EQUALS the critical 50 in number" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", NOT_EQUALS, EQUALS, NOT_EQUALS"
              , "The NumVar MUST NOT EQUALS the forbidden number 10" +
                " AND (5 TIMES the NumVar ) MUST be GREATER THAN the number 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", EQUALS, LESS_OR_EQUALS, NOT_EQUALS"
              , "The NumVar SHOULD BE LESS THAN a number 10" +
                " AND (5 * NumVar ) SHOULD NOT EQUALS the number 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", GREATER_OR_EQUALS, EQUALS, NOT_EQUALS"
              , "The NumVar HAVE NOT to be EQUALS the forbidden number 10" +
                " AND (5 TIMES NumVar ) MUST be AT LEAST the number 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", EQUALS, LESS_THAN, NOT_EQUALS"
              , "The NumVar HAVE TO BE SMALLER THAN a number 10" +
                " AND (5 * NumVar ) MUSTN'T EQUALS the number 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", GREATER_OR_EQUALS, EQUALS, NOT_EQUALS"
              , "The NumVar SHOULDN'T be EQUALS the forbidden number 10" +
                " AND (5 TIMES a NumVar ) MUST be GREATER OR EQUALS the number 50" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", EQUALS, LESS_THAN, NOT_EQUALS"
              , "Variable NumVar HAS TO EQUALS the number 10" +
                " AND (the number 5 * the variable NumVar ) HAVE be LOWER than the critical 50 in number" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", NOT_EQUALS, GREATER_OR_EQUALS, NOT_EQUALS"
              , "The NumVar MUSTN'T be EQUALS the number 10" +
                " AND (5 TIMES NumVar ) HAS TO be AT MOST 50 in number" +
                "     OR NumVar MOD 5 MUST EQUALS 0" +
                ", EQUALS, GREATER_THAN, NOT_EQUALS"
        )
        fun constraints_and_or_grouped_number_variable_arithmetic_static_numbers(paramStr: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator, expectedOperator3: ASTComparisonOperator) {
            var input = """
            15 AS NumVar

            $paramStr
            """
            End2AstRunner.run(input, "{}") {
                r ->
                    r.variables()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("NumVar")
                            .hasNumber(15.0)
                .parentModel()
                    .rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasError(paramStr)
                            .conditionGroup()
                                .hasPreprocessedSource()
                                .hasNoConnector()
                                .first()
                                    .hasPreprocessedSource()
                                    .hasOperator(expectedOperator1)
                                    .leftVariable()
                                        .hasPreprocessedSource()
                                        .hasType(DataPropertyType.Decimal)
                                        .hasName("NumVar")
                                .parentCondition()
                                    .rightNumber()
                                        .hasValue(10.0)
                                        .hasPreprocessedSource()
                            .parentConditionGroup()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasConnector(ASTConditionConnector.OR)
                                    .hasOperator(expectedOperator2)
                                    .rightNumber()
                                        .hasValue(50.0)
                                        .hasPreprocessedSource()
                                .parentCondition()
                                    .leftArithmetical()
                                        .operation()
                                            .hasPreprocessedSource()
                                            .hasSizeOf(2)
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .staticNumber(5.0)
                                        .parentOperation()
                                            .second()
                                                .hasPreprocessedSource()
                                                .variableValue()
                                                    .hasPreprocessedSource()
                                                    .hasName("NumVar")
                                                    .hasType(DataPropertyType.Decimal)
                    r.rules()
                        .first()
                            .conditionGroup()
                                .atIndex(2)
                                    .hasPreprocessedSource()
                                    .hasOperator(expectedOperator3)
                                    .hasConnector(ASTConditionConnector.AND)
                                    .rightNumber()
                                        .hasValue(0.0)
                                        .hasPreprocessedSource()
                                .parentCondition()
                                    .leftArithmetical()
                                        .operation()
                                            .hasPreprocessedSource()
                                            .hasSizeOf(2)
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .variableValue()
                                                    .hasPreprocessedSource()
                                                    .hasType(DataPropertyType.Decimal)
                                                    .hasName("NumVar")
                    r.rules()
                        .first()
                            .conditionGroup()
                                .atIndex(2)
                                    .leftArithmetical()
                                        .operation()
                                            .hasPreprocessedSource()
                                            .second()
                                                .hasPreprocessedSource()
                                                .hasOperator(ASTArithmeticalOperator.Modulo)
                                                .staticNumber(5.0)
            }

        }

    @Test
    fun constrained_inverse_operators_and_connectors_must_be_at_least_or_must_be(){
        var input = """
            Wuppertal AS location

            17 AS age

            your age MUST be AT LEAST 18 years old OR your location MUST be Wuppertal
        """.trimIndent()

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .conditionGroup()
                        .first()
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                    .parentConditionGroup()
                        .second()
                            .hasConnector(ASTConditionConnector.AND)
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        }
    }

    @Test
    fun constrained_inverse_operators_and_connectors_must_be_at_least_or_must_be_and_must_at_least(){
        var input = """
            your age MUST be AT LEAST 18 years old 
            OR your location MUST be Wuppertal
                AND you age MUST be AT LEAST 13 years old
                
            COMMENT in Wuppertal darf man auch minderjährig sein
        """

        End2AstRunner.run(input, "{location: \"wuppertal\", age: 17}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .conditionGroup()
                        .first()
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                    .parentConditionGroup()
                        .second()
                            .hasConnector(ASTConditionConnector.AND)
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                    .parentConditionGroup()
                        .atIndex(2)
                            .hasConnector(ASTConditionConnector.OR)
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
        }
    }

    @Test
    fun constrained_inverse_operators_and_connectors_must_not_be_less_and_must_not_be(){
        var input = """
            your age MUST NOT be LESS than 18
            AND your location MUST NOT be Wuppertal
        """

        End2AstRunner.run(input, "{location: \"Wuppertal\", age: 17}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .conditionGroup()
                        .first()
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                    .parentConditionGroup()
                        .second()
                            .hasConnector(ASTConditionConnector.OR)
                            .hasOperator(ASTComparisonOperator.EQUALS)
        }
    }

    @Test
    fun constrained_inverse_operators_and_connectors_must_not_be_less_or_must_not_be(){
        var input = """
            your age MUST NOT be LESS than 18
            OR your location MUST NOT be Wuppertal
        """

        End2AstRunner.run(input, "{location: \"Wuppertal\", age: 17}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .conditionGroup()
                        .first()
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                    .parentConditionGroup()
                        .second()
                            .hasConnector(ASTConditionConnector.AND)
                            .hasOperator(ASTComparisonOperator.EQUALS)
        }
    }

    @Test
    fun constrained_inverse_operators_and_connectors_must_not_be_less_or_must_not_be_and_must_not_be(){
        var input = """
            your age MUST NOT be LESS than 18
            OR your location MUST NOT be Wuppertal
            AND your location MUST NOT be Berlin
            
            COMMENT you need to be older than 18 if you are from Wuppertal, and are not allowed to be from Berlin
        """

        End2AstRunner.run(input, "{location: \"Wuppertal\", age: 17}") {
            r -> r.rules()
                .hasSizeOf(1)
                    .first()
                        .conditionGroup()
                            .first()
                                .hasOperator(ASTComparisonOperator.LESS_THAN)
                        .parentConditionGroup()
                            .second()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(ASTComparisonOperator.EQUALS)
                        .parentConditionGroup()
                            .atIndex(2)
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(ASTComparisonOperator.EQUALS)
        }
    }
}

