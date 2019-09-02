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


import io.openvalidation.common.ast.ASTComparisonOperator
import io.openvalidation.common.ast.condition.ASTConditionConnector
import io.openvalidation.common.data.DataPropertyType
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class ConditionGroupTest {

    @ParameterizedTest
    @ValueSource(strings = [
        "var1 EQUALS var2 OR var1 GREATER THAN 2"
        , "var1 EQUALS var2 OR var1 GREATER THAN 2\n"
    ])
    fun compare_variables_number_1group(paramStr: String) {
        val errorStr = "Error Message"
        var input = """
            5 AS var1

            6 AS var2

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .conditionGroup()
                            .hasSize(2)
                            .hasPreprocessedSource()
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var2")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                                    .leftVariable()
                                        .hasPreprocessedSource()
                                        .hasName("var1")
                                        .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(2.0)
                                    .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @CsvSource(
        "EQUALS, EQUALS,' ',' '"
        , "IS,IS,' ',' '"
        ,"IS,EQUALS,' ',' '"
        ,"EQUALS,IS,' ',' '"
        ,"EQUALS,EQUALS,' ',thing"
        , "IS,IS,' ',stuff"
        ,"IS,EQUALS,' ',thing"
        ,"EQUALS,IS,' ',stuff"
        ,"EQUALS,EQUALS,the,' '"
        , "IS,IS,the,' '"
        ,"IS,EQUALS,the,' '"
        ,"EQUALS,IS,the,' '"
        ,"EQUALS,EQUALS,the,thing"
        , "IS,IS,the,stuff"
        ,"IS,EQUALS,the,thing"
        ,"EQUALS,IS,the,stuff"
    )
    fun compare_strings_1group(equals1: String, equals2: String, sugar1: String, sugar2: String) {
        val errorStr = "Error Message"

        var input = """
            Dortmund AS var1

            Hamburg AS var2

            IF Düsseldorf $equals1 $sugar1 var1 $sugar2 OR Düsseldorf $equals2 $sugar1 var2 $sugar2 THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .conditionGroup()
                            .hasSize(2)
                            .hasPreprocessedSource()
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftString()
                                    .hasPreprocessedSource()
                                    .hasValue("Düsseldorf")
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftString()
                                    .hasPreprocessedSource()
                                    .hasValue("Düsseldorf")
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var2")
                                    .hasType(DataPropertyType.String) }
    }


    @ParameterizedTest
    @CsvSource(
        "EQUALS, EQUALS, ' ', ' '"
        , "IS, IS, ' ', ' '"
        ,"IS, EQUALS, ' ', ' '"
        ,"EQUALS, IS, ' ', ' '"
        ,"EQUALS, EQUALS, ' ', that"
        , "IS, IS, ' ', the"
        ,"IS, EQUALS, ' ', that"
        ,"EQUALS, IS, ' ', this"
        ,"EQUALS, EQUALS, the, ' '"
        , "IS, IS, the, ' '"
        ,"IS, EQUALS, the, ' '"
        ,"EQUALS, IS, the, ' '"
        ,"EQUALS, EQUALS, the, that"
        , "IS, IS, the, this"
        ,"IS, EQUALS, the, the"
        ,"EQUALS, IS, the, this"
    )
    fun compare_strings_properties_1group(equals1: String, equals2: String, sugar1: String, sugar2: String) {
        val errorStr = "Error Message"

        var input = """
            Dortmund AS city1

            Hamburg AS city2

            IF $sugar1 Domicile $equals1 city1 OR $sugar2 Domicile $equals2  city2
             THEN $errorStr
            """

        End2AstRunner.run(input,
                """
                           {Domicile: "Dortmund"}
                        """) {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasSize(2)
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Domicile")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("city1")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Domicile")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("city2")
                                    .hasType(DataPropertyType.String) }
    }

    @ParameterizedTest
    @CsvSource(
        "EQUALS, EQUALS, GREATER, ' ', ' ', ' '"
        ,"IS, IS, BIGGER, ' ', ' ', ' 'my"
        ,"IS, EQUALS, LARGER, ' ', ' ', ' '"
        ,"EQUALS, IS, MORE, ' ', ' ', ' 'the"
        ,"EQUALS, EQUALS, GREATER, ' ', that, ' '"
        ,"IS, IS, BIGGER, ' ', the, ' '"
        ,"IS, EQUALS, LARGER, ' ', that, ' '"
        ,"EQUALS, IS, MORE, ' ', this, ' 'a"
        ,"EQUALS, EQUALS, GREATER, the, ' ', a"
        ,"IS, IS, BIGGER, the, ' ', ' '"
        ,"IS, EQUALS, LARGER, the, ' ', ' 'that"
        ,"EQUALS, IS, MORE, the, ' ', ' '"
        ,"EQUALS, EQUALS, GREATER, the, that, a"
        ,"IS, IS, BIGGER, the, this, the"
        ,"IS, EQUALS, LARGER, the, the, that"
        ,"EQUALS, IS, MORE, the, this, my"
    )
    fun compare_string_properties_number_1group(equals1: String, equals2: String, greater1: String, sugar1: String, sugar2: String, sugar3: String) {

        val errorStr = "Error Message"
        var input = """
            Dortmund AS city1

            Hamburg AS city2

            IF $sugar1 Domicile $equals1 city1 OR $sugar2 Domicile $equals2 city2 AND $sugar3 Age $greater1 17
             THEN $errorStr
            """

        End2AstRunner.run(input,
                """
                           {Domicile: "Dortmund", Age: 30}
                        """) {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasSize(3)
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Domicile")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("city1")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Domicile")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("city2")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                        .parentConditionGroup()
                            .atIndex(2)
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Age")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(17.0)
                                    .hasPreprocessedSource()
        }

    }

//    TODO: Test dürfte nicht funktionieren, da die oberste ConditionGroup nicht existieren dürfte (erstes .first)
    @ParameterizedTest
    @CsvSource(
        "EQUALS, EQUALS, GREATER, ' ', ' ', ' '"
        ,"IS, IS, BIGGER, ' ', ' ', ' 'THAN"
        ,"IS, EQUALS, LARGER, ' ', ' ', ' '"
        ,"EQUALS, IS, MORE, ' ', ' ', ' 'THAN"
        ,"EQUALS, EQUALS, GREATER, ' ', that, ' '"
        ,"IS, IS, BIGGER, ' ', the, ' '"
        ,"IS, EQUALS, LARGER, ' ', that, ' '"
        ,"EQUALS, IS, MORE, ' ', this, THAN"
        ,"EQUALS, EQUALS, GREATER, the, ' ', THAN"
        ,"IS, IS, BIGGER, the, ' ', ' '"
        ,"IS, EQUALS, LARGER, the, ' ', ' 'THAN"
        ,"EQUALS, IS, MORE, the, ' ', ' '"
        ,"EQUALS, EQUALS, GREATER, the, that, THAN"
        ,"IS, IS, BIGGER, the, this, ' '"
        ,"IS, EQUALS, LARGER, the, the, THAN"
        ,"EQUALS, IS, MORE, the, this, ' '"
    )
    fun compare_string_properties_number_2groups(equals1: String, equals2: String, greater1: String, sugar1: String, sugar2: String, sugar3: String) {
        val errorStr = "Error Message"
        var input = """
                Dortmund AS city1

                Hamburg AS city2

                IF $sugar1 Domicile $equals1 city1
                 AND $sugar2 Competence $equals2 Genius
                        OR Age $greater1 $sugar3 17
                 THEN $errorStr
            """

        End2AstRunner.run(input,
                """
                           {Domicile: "Dortmund", Age: 30, Competence: "Genius"}
                        """) {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .hasSize(2)
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Domicile")
                                    .hasType(DataPropertyType.String)
                                .parentCondition()
                                    .rightVariable()
                                        .hasPreprocessedSource()
                                        .hasName("city1")
                                        .hasType(DataPropertyType.String)
                                .parentCondition()
                        .parentConditionGroup()
                            .firstConditionGroup()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasSize(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTComparisonOperator.EQUALS)
                                    .leftProperty()
                                        .hasPreprocessedSource()
                                        .hasPath("Competence")
                                        .hasType(DataPropertyType.String)
                                .parentCondition()
                                    .rightString()
                                        .hasPreprocessedSource()
                                        .hasValue("Genius")
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Age")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(17.0)
                                    .hasPreprocessedSource()
        }
    }

    @ParameterizedTest
    @CsvSource(
        "EQUALS, EQUALS, GREATER, LESS, ' ', ' ', ' '"
        ,"IS, IS, BIGGER, SMALLER, ' ', ' ', THAN"
        ,"IS, EQUALS, LARGER, LOWER, ' ', ' ', ' '"
        ,"EQUALS, IS, MORE, LESS, ' ', ' ', THAN"
        ,"EQUALS, EQUALS, GREATER, SMALLER, ' ', that, ' '"
        ,"IS, IS, BIGGER, LOWER, ' ', the, ' '"
        ,"IS, EQUALS, LARGER, LESS, ' ', that, ' '"
        ,"EQUALS, IS, MORE, SMALLER, ' ', this, THAN"
        ,"EQUALS, EQUALS, GREATER, LOWER, the, ' ', THAN"
        ,"IS, IS, BIGGER, LESS, the, ' ', ' '"
        ,"IS, EQUALS, LARGER, SMALLER, the, ' ', THAN"
        ,"EQUALS, IS, MORE, LOWER, the, ' ', ' '"
        ,"EQUALS, EQUALS, GREATER, LESS, the, that, THAN"
        ,"IS, IS, BIGGER, SMALLER, the, this, ' '"
        ,"IS, EQUALS, LARGER, LOWER, the, the, THAN"
        ,"EQUALS, IS, MORE, LESS, the, this, ' '"
    )
    fun compare_string_properties_2x2groups(equals1: String, equals2: String, greater1: String, less1: String, sugar1: String, sugar2: String, sugar3: String) {

        val errorStr = "Error Message"

        var input = """
            Dortmund AS city1

            Hamburg AS city2

            2016 AS currentJobSince

            IF $sugar1 Domicile $equals1 city1
                    OR LastJob $less1 $sugar3 currentJobSince
            AND $sugar2 Competence $equals2 Genius
                    OR Age $greater1 $sugar3 30
             THEN $errorStr
            """

        End2AstRunner.run(input,
                """
                           {Domicile: "Dortmund", Age: 30, Competence: "Genius", LastJob: 1990; Alter: 51}
                        """) {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .conditionGroup()
                            .firstConditionGroup()
                                .hasPreprocessedSource()
                                .hasSize(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTComparisonOperator.EQUALS)
                                    .leftProperty()
                                        .hasPreprocessedSource()
                                        .hasPath("Domicile")
                                        .hasType(DataPropertyType.String)
                                .parentCondition()
                                    .rightVariable()
                                        .hasPreprocessedSource()
                                        .hasName("city1")
                                        .hasType(DataPropertyType.String)
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(ASTComparisonOperator.LESS_THAN)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasPath("LastJob")
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("currentJobSince")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                        .parentConditionGroup()
                    .parentConditionGroup()
                        .secondConditionGroup()
                            .hasPreprocessedSource()
                            .hasConnector(ASTConditionConnector.AND)
                            .hasSize(2)
                            .first()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Competence")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .rightString()
                                    .hasPreprocessedSource()
                                    .hasValue("Genius")
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .hasConnector(ASTConditionConnector.OR)
                                .leftProperty()
                                    .hasPath("Age")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(30.0)
                                    .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @CsvSource(
            "SOME var1 GREATER OR EQUAL 3, var1 LESS THAN 10, GREATER_OR_EQUALS, LESS_THAN"
            ,"var1 days MORE THAN 3 days, var1 FEWER THAN 10, GREATER_THAN, LESS_THAN"
            ,"var1 DAYS HIGHER THAN 3 DAYS, var1 SMALLER THAN 10, GREATER_THAN, LESS_THAN"
            ,"the var1 days GREATER OR EQUAL the 3 days, all var1 days LESS THAN 10 days, GREATER_OR_EQUALS, LESS_THAN"
            ,"Most var1 days AT LEAST the 3 days, all var1 days LESS OR EQUAL 10 days, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"the var1 days BIGGER the 3 days, all var1 days AT LESS THAN 10 days, GREATER_THAN, LESS_THAN"
            ,"ALL The var1 days GREATER THAN the 3 days, all var1 days AT MOST 10 days, GREATER_THAN, LESS_OR_EQUALS"
    )
    fun compare_condition_var_and_variable2static_number(condStr1: String, condStr2: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        val errorStr = "Condition is True"
        var input = """
            4 AS var1

            $condStr1 AS CondVar1

            IF CondVar1 AND $condStr2 THEN $errorStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasName("var1")
                        .hasNumber(4.0)
                        .hasPreprocessedSource()
                .parentList()
                    .second()
                        .hasPreprocessedSource()
                        .hasName("CondVar1")
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(expectedOperator1)
                            .leftVariable()
                                .hasName("var1")
                                .hasType(DataPropertyType.Decimal)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .rightNumber()
                                .hasValue(3.0)
                                .hasPreprocessedSource()
                        .parentCondition()
                    .parentVariable()
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError(errorStr)
                        .hasPreprocessedSource()
                        .conditionGroup()
                            .hasSize(2)
                            .hasPreprocessedSource()
                            .first()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasName("CondVar1")
                                    .hasType(DataPropertyType.Boolean)
                                    .hasPreprocessedSource()
                        .parentCondition()
                            .rightBoolean()
                                .isTrue
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(expectedOperator2)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @CsvSource(
            "SOME var1 GREATER OR EQUAL 3, var1 LESS THAN 10, GREATER_OR_EQUALS, LESS_THAN"
            ,"var1 days MORE THAN 3 days, var1 FEWER THAN 10, GREATER_THAN, LESS_THAN"
            ,"var1 DAYS HIGHER THAN 3 DAYS, var1 SMALLER THAN 10, GREATER_THAN, LESS_THAN"
            ,"the var1 days GREATER OR EQUAL the 3 days, all var1 days LESS THAN 10 days, GREATER_OR_EQUALS, LESS_THAN"
            ,"Most var1 days AT LEAST the 3 days, all var1 days LESS OR EQUAL 10 days, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"the var1 days BIGGER the 3 days, all var1 days LESS OR EQUAL 10 days, GREATER_THAN, LESS_OR_EQUALS"
            ,"ALL The var1 days GREATER THAN the 3 days, all var1 days AT MOST 10 days, GREATER_THAN, LESS_OR_EQUALS"
    )
    fun compare_variable2static_number_and_condition_var(condStr1: String, condStr2: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        val errorStr = "Condition is True"
        var input = """
            4 AS var1

            $condStr1 AS CondVar1

            IF $condStr2 AND CondVar1 THEN $errorStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasName("var1")
                        .hasNumber(4.0)
                        .hasPreprocessedSource()
                .parentList()
                    .second()
                        .hasName("CondVar1")
                        .condition()
                            .hasOperator(expectedOperator1)
                            .leftVariable()
                                .hasName("var1")
                                .hasType(DataPropertyType.Decimal)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .rightNumber()
                                .hasValue(3.0)
                               .hasPreprocessedSource()
                        .parentCondition()
                    .parentVariable()
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError(errorStr)
                        .hasPreprocessedSource()
                        .conditionGroup()
                            .hasSize(2)
                            .hasPreprocessedSource()
                            .first()
                                .hasOperator(expectedOperator2)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(10.0)
                                    .hasPreprocessedSource()
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasName("CondVar1")
                                    .hasType(DataPropertyType.Boolean)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .rightBoolean()
                                    .isTrue
//                                    .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @CsvSource(
            "SOME var1 GREATER OR EQUAL 3, var1 LESS THAN 10, GREATER_OR_EQUALS, LESS_THAN"
            ,"var1 days MORE THAN 3 days, var1 FEWER THAN 10, GREATER_THAN, LESS_THAN"
            ,"var1 DAYS HIGHER THAN 3 DAYS, var1 SMALLER THAN 10, GREATER_THAN, LESS_THAN"
            ,"the var1 days GREATER OR EQUAL the 3 days, all var1 days LESS THAN 10 days, GREATER_OR_EQUALS, LESS_THAN"
            ,"Most var1 days AT LEAST the 3 days, all var1 days LESS OR EQUAL 10 days, GREATER_OR_EQUALS, LESS_OR_EQUALS"
            ,"the var1 days BIGGER the 3 days, all var1 days LESS OR EQUAL 10 days, GREATER_THAN, LESS_OR_EQUALS"
            ,"ALL The var1 days GREATER THAN the 3 days, all var1 days AT MOST 10 days, GREATER_THAN, LESS_OR_EQUALS"
    )
    fun compare_2condition_var(condStr1: String, condStr2: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        val errorStr = "Condition is True"
        var input = """
            4 AS var1

            $condStr1 AS CondVar1

            $condStr2 AS CondVar2

            IF CondVar1 AND CondVar2 THEN $errorStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                    .hasSizeOf(3)
                    .first()
                        .hasName("var1")
                        .hasNumber(4.0)
                        .hasPreprocessedSource()
                .parentList()
                    .second()
                        .hasName("CondVar1")
                        .condition()
                            .hasOperator(expectedOperator1)
                            .hasPreprocessedSource()
                            .rightNumber()
                                .hasValue(3.0)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasName("var1")
                                .hasType(DataPropertyType.Decimal)
//                        .parentCondition()
//                            .rightNumber()
//                                .hasValue(3.0)
                        .parentCondition()
                    .parentVariable()
                .parentList()
                    .third()
                        .hasName("CondVar2")
                        .hasPreprocessedSource()
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(expectedOperator2)
                            .rightNumber()
                                .hasValue(10.0)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .leftVariable()
                                .hasName("var1")
                                .hasType(DataPropertyType.Decimal)
                                .hasPreprocessedSource()
//                        .parentCondition()
//                            .rightNumber()
//                                .hasValue(10.0)
                        .parentCondition()
                .parentVariable()
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError(errorStr)
                        .hasPreprocessedSource()
                        .conditionGroup()
                            .hasSize(2)
                            .hasPreprocessedSource()
                            .first()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasName("CondVar1")
                                    .hasType(DataPropertyType.Boolean)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .rightBoolean()
                                    .isTrue
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasName("CondVar2")
                                    .hasType(DataPropertyType.Boolean)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .rightBoolean()
                                    .isTrue
        }
    }


    @ParameterizedTest
    @CsvSource(
             "Person.Status EQUALS Student, var1 LESS THAN 3, EQUALS, LESS_THAN"
            ,"Person.Status EQUALS Student, var1 FEWER THAN 3, EQUALS, LESS_THAN"
            ,"Person.Status EQUALS Student, var1 SMALLER THAN 3, EQUALS, LESS_THAN"
            ,"Person.Status EQUALS Student, all var1 years LESS THAN 3 years, EQUALS, LESS_THAN"
            ,"Person.Status EQUALS Student, all var1 years LESS OR EQUAL 3 years, EQUALS, LESS_OR_EQUALS"
            ,"Person.Status EQUALS Student, all var1 years LESS OR EQUAL 3 years, EQUALS, LESS_OR_EQUALS"
            ,"Person.Status EQUALS Student, all var1 years AT MOST 3, EQUALS, LESS_OR_EQUALS"
    )
    fun compare_variable2static_number_string_and_condition_var(condStr1: String, condStr2: String, expectedOperator1: ASTComparisonOperator, expectedOperator2: ASTComparisonOperator) {
        val errorStr = "Condition is True"
        var input = """
            4 AS var1

            $condStr1 AS CondVar1

            IF $condStr2 OR CondVar1 THEN $errorStr
        """

        End2AstRunner.run(input, "{Person: {Status: 'Student'}}") {
            r -> r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasName("var1")
                        .hasNumber(4.0)
                .parentList()
                    .second()
                        .hasName("CondVar1")
                        .condition()
                            .hasOperator(expectedOperator1)
                            .hasPreprocessedSource()
                            .leftProperty()
                                .hasPath("Person.Status")
                                .hasType(DataPropertyType.String)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .rightString()
                                .hasValue("Student")
                        .parentCondition()
                    .parentVariable()
                .parentModel()
                    .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError(errorStr)
                        .hasPreprocessedSource()
                        .conditionGroup()
                            .hasSize(2)
                            .hasPreprocessedSource()
                            .first()
                                .hasOperator(expectedOperator2)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(3.0)
                                    .hasPreprocessedSource()
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .hasConnector(ASTConditionConnector.OR)
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasName("CondVar1")
                                    .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                                .rightBoolean()
                                    .isTrue
        }
    }


}
