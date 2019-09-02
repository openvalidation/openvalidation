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
import io.openvalidation.common.data.DataPropertyType
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ConditionTest {

//    TODO:
    @ParameterizedTest
    @ValueSource(strings = [
         "var1 GREATER OR EQUAL 3 "
        ,"var1 days GREATER OR EQUAL 3 days "
        ,"var1 days GREATER OR EQUAL 3 days "
        ,"the var1 days GREATER OR EQUAL the 3 days "
    ])
    fun compare_variable2static_number_greater_equals(paramStr: String) {
        val errorStr = "First Number is greater"
        var input = """
            4 AS var1

            IF $paramStr THEN $errorStr
        """

        End2AstRunner.run(input, "{}") {
                r -> r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasError(errorStr)
                            .hasPreprocessedSource()
                            .condition()
                                .hasOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(3.0)
                                    .hasPreprocessedSource()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "4 LESS OR EQUAL var1"
       ,"4 LESS OR EQUALS var1\n"
        ,"4 days LESS OR EQUAL var1 days"
        ,"the 4 days LESS OR EQUAL the var1 days"
        ,"the 4 days LESS OR EQUALS the var1 days\n"
    ])
    fun compare_variable2static_number_less_equals(paramStr: String) {
        val errorStr = "First Number is greater"

        var input = """
            3 AS var1

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
                            .leftNumber()
                                .hasValue(4.0)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("var1") }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "var1 GREATER OR EQUALS var2 "
        ,"var1 days GREATER OR EQUAL var2 days "
        ,"var1 days GREATER OR EQUALS var2 days "
        ,"the var1 days GREATER OR EQUAL the var2 days "
    ])
    fun compare_2variables_greater_equals(paramStr: String) {
        val errorStr = "First Number is greater"
        var input = """
            4 AS var1

            3 AS var2

            IF $paramStr THEN $errorStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("var1")
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("var2") }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "var2 LESS OR EQUAL var1"
        ,"var2 LESS OR EQUALS var1\n"
        ,"var2 days LESS OR EQUAL var1 days"
        ,"the var2 days LESS OR EQUAL the var1 days"
        ,"the var2 days LESS OR EQUALS the var1 days\n"
    ])
    fun compare_2variables_less_equals(paramStr: String) {
        val errorStr = "First Number is greater"
        var input = """
            4 AS var2

            3 AS var1

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("var2")
                        .parentCondition()
                            .hasPreprocessedSource()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("var1") }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Age GREATER OR EQUALS THAN 18"
        ,"the Age GREATER OR EQUAL THAN 18"
        ,"the Age GREATER OR EQUALS THAN 18 years"
        ,"the Age GREATER OR EQUALS THAN 18 years"
        ,"the Age in years GREATER OR EQUAL THAN 18 years"
        ,"the Age in years\n GREATER OR EQUALS THAN\n 18 years\n"
    ])
    fun compare_property_greater_equal_number(paramStr: String) {
        val errorStr = "Old enough!"

        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 18}""") {
            r -> r.rules()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Age")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .hasPreprocessedSource()
                                .rightNumber()
                                    .hasValue(18.0) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Age LESS OR EQUALS THAN 17"
        ,"the Age LESS OR EQUAL THAN 17"
        ,"the Age LESS OR EQUALS THAN 17 years"
        ,"the Age LESS OR EQUALS THAN 17 years"
        ,"the Age in years LESS OR EQUAL THAN 17 years"
        ,"the Age in years\n LESS OR EQUALS THAN\n 17 years\n"
    ])
    fun compare_property_less_equal_number(paramStr: String) {
        val errorStr = "Not old enough!"

        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 18}""") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
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
    @ValueSource(strings = [
        "Age GREATER OR EQUALS THAN MinAge"
        ,"The Age GREATER OR EQUAL THAN MinAge"
        ,"The Age GREATER OR EQUALS THAN MinAge Years"
        ,"The Age GREATER OR EQUAL THAN MinAge Years"
        ,"The Age GREATER OR EQUALS THAN MinAge Years\n"
    ])
    fun compare_2number_propertes_greater_equal(paramStr: String) {
        val erroStr = "Old enough!"
        var input = """
            IF $paramStr THEN $erroStr
            """

        End2AstRunner.run(input, """{"Age": 17, "MinAge": 18}""") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(erroStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Age")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightProperty()
                                .hasPreprocessedSource()
                                .hasPath("MinAge")
                                .hasType(DataPropertyType.Decimal) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Age LESS OR EQUALS THAN MinAge"
        ,"The Age LESS OR EQUAL THAN MinAge"
        ,"The Age LESS OR EQUALS THAN MinAge Years"
        ,"The Age LESS OR EQUAL THAN MinAge Years"
        ,"The Age LESS OR EQUALS THAN MinAge Years\n"
    ])
    fun compare_2number_propertes_less_equal(paramStr: String) {
        val errorStr = "Not old enough!"
        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 17, "MinAge": 18}""") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Age")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightProperty()
                                .hasPreprocessedSource()
                                .hasPath("MinAge")
                                .hasType(DataPropertyType.Decimal) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "var1 GREATER THAN 3"
        ,"var1 MORE THAN 3"
        ,"var1 BIGGER THAN 3"
        ,"var1 LARGER THAN 3"
        ,"var1 GREATER THAN 3\n"
        ,"var1 GREATER THAN 3 things"
        ,"the var1 GREATER THAN 3 things"
        ,"the var1 MORE THAN 3 things"
        ,"the var1 thing GREATER THAN 3 things"
        ,"the var1 thing MORE THAN 3 things"
        ,"the var1 thing LARGER THAN 3 things"
        ,"the var1 thing GREATER THAN the 3 things"
        ,"the var1 thing BIGGER THAN the 3 things"
        ,"the var1 thing GREATER THAN the 3 things\n"
        ,"the var1 thing\n GREATER THAN\n the 3 things\n"
    ])
    fun compare_variable_greater_static_number(paramStr: String) {
        val errorStr = "Variable is greater"
        var input = """
            5 AS var1

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasNumber(5.0)
                        .hasPreprocessedSource()
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(3.0)
                                    .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "4 LESS THAN var1"
        ,"4 SMALLER THAN var1"
        ,"4 LOWER THAN var1"
        ,"4 LESS THAN var1\n"
        ,"4 SMALLER THAN var1 things"
        ,"the 4 LOWER THAN var1 things"
        ,"the 4 LESS THAN var1 things"
        ,"the 4 thing SMALLER THAN var1 things"
        ,"the 4 thing LOWER THAN var1 things"
        ,"the 4 thing LESS THAN var1 things"
        ,"the 4 thing LOWER THAN the var1 things"
        ,"the 4 thing SMALLER THAN the var1 things"
        ,"the 4 thing LESS THAN the var1 things\n"
        ,"the 4 thing\n SMALLER THAN\n the var1 things\n"
    ])
    fun compare_variable_less_static_number(paramStr: String) {
        val errorStr = "Variable is greater"
        var input = """
            5 AS var1

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(5.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasName("var1")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .leftNumber()
                                .hasValue(4.0)
                                .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "var1 GREATER THAN var2"
        ,"var1 MORE THAN var2"
        ,"var1 LARGER THAN var2"
        ,"var1 MORE THAN var2"
        ,"the var1 MORE THAN the var2"
        ,"the var1 BIGGER THAN the var2"
        ,"var1\n GREATER THAN\n var2\n"
    ])
    fun compare_2number_variables_greater(paramStr: String) {
        val errorStr = "Variable var1 is greater"
        var input = """
            5 AS var1

            3 AS var2

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(5.0)
                .parentList()
                    .second()
                        .hasNumber(3.0)
                        .hasPreprocessedSource()
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError(errorStr)
                        .hasPreprocessedSource()
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.GREATER_THAN)
                            .leftVariable()
                                .hasName("var1")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasName("var2")
                                .hasType(DataPropertyType.Decimal) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "var1 LESS THAN var2"
        ,"var1 SMALLER THAN var2"
        ,"var1 LOWER THAN var2"
        ,"var1 LESS THAN var2"
        ,"the var1 SMALLER THAN the var2"
        ,"the var1 LOWER THAN the var2"
        ,"var1\n LESS THAN\n var2\n"
    ])
    fun compare_2number_variables_less(paramStr: String) {
        val errorStr = "Variable var1 is greater"
        var input = """
            5 AS var1

            3 AS var2

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                        .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasNumber(5.0)
                        .parentList()
                            .second()
                                .hasNumber(3.0)
                                .hasPreprocessedSource()
                .parentModel()
                    .rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasError(errorStr)
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.LESS_THAN)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasName("var1")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                        .hasName("var2")
                                        .hasType(DataPropertyType.Decimal)
                                        .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Age GREATER THAN 17"
       ,"the Age GREATER THAN 17"
        ,"the Age GREATER THAN 17 years"
        ,"the Age MORE THAN 17 years"
        ,"the Age BIGGER THAN 17 years"
        ,"the Age in years GREATER THAN 17 years"
        ,"the Age in years\n GREATER THAN\n 17 years\n"
    ])
    fun compare_property_greater_number(paramStr: String) {
        val errorStr = "Old enough!"
        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 18}""") {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
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

    @ParameterizedTest
    @ValueSource(strings = [
        "Age LESS THAN 18"
        ,"the Age SMALLER THAN 18"
        ,"the Age LOWER THAN 18 years"
        ,"the Age LESS THAN 18 years"
        ,"the Age SMALLER THAN 18 years"
        ,"the Age in years LOWER THAN 18 years"
        ,"the Age in years\n LESS THAN\n 18 years\n"
    ])
    fun compare_property_less_number(paramStr: String) {
        val errorStr = "Not old enough!"
        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 17}""") {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Age")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightNumber()
                                .hasValue(18.0)
                                .hasPreprocessedSource()
            }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Age GREATER THAN Age1"
        ,"The Age GREATER THAN Age1"
        ,"The Age LARGER THAN Age1"
        ,"The Age BIGGER THAN Age1"
        ,"The Age GREATER THAN Age1 Years"
        ,"The Age MORE THAN Age1 Years"
        ,"The Age MORE THAN Age1 Years\n"
    ])
    fun compare_number_property_greater_variable(paramStr: String) {
        val errorStr = "Old enough!"

        var input = """
            18 AS Age1

            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 17}""") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(18.0)
                        .hasName("Age1")
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasError(errorStr)
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Age")
                                    .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("Age1")
                                    .hasType(DataPropertyType.Decimal) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Age GREATER THAN MinAge"
        ,"The Age GREATER THAN MinAge"
        ,"The Age LARGER THAN MinAge"
        ,"The Age BIGGER THAN MinAge"
        ,"The Age GREATER THAN MinAge Years"
        ,"The Age MORE THAN MinAge Years"
        ,"The Age MORE THAN MinAge Years\n"
    ])
    fun compare_2number_propertes_greater(paramStr: String) {
        val errorStr = "Old enough!"

        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 17, "MinAge": 18}""") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.GREATER_THAN)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Age")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightProperty()
                                .hasPreprocessedSource()
                                .hasPath("MinAge")
                                .hasType(DataPropertyType.Decimal) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Age LESS THAN MinAge"
        ,"The Age SMALLER THAN MinAge"
        ,"The Age LOWER THAN MinAge"
        ,"The Age LESS THAN MinAge"
        ,"The Age SMALLER THAN MinAge Years"
        ,"The Age LOWER THAN MinAge Years"
        ,"The Age LESS THAN MinAge Years\n"
    ])
    fun compare_2number_propertes_less(paramStr: String) {
        val errorStr = "Not old enough!"

        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Age": 17, "MinAge": 18}""") {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Age")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightProperty()
                                .hasPreprocessedSource()
                                .hasPath("MinAge")
                                .hasType(DataPropertyType.Decimal) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "Language EQUALS german"
        ,"Language EQUAL german"
        ,"Language IS german"
        ,"The Language EQUAL german"
        ,"The Language IS german"
        ,"His/Her Language IS german"
        ])
    fun compare_string_property_equals_variable(paramStr: String) {
        val errorStr = "speaks german"

        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Language": "german"}""") {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Language")
                                .hasType(DataPropertyType.String)
                        .parentCondition()
                            .rightString()
                                .hasValue("german")
                                .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Language NOT EQUALS german"
        ,"Language NOT EQUAL german"
        ,"Language IS NOT german"
        ,"The Language NOT EQUAL german"
        ,"The Language ISN'T german"
        ,"His/Her Language ISN'T german"
    ])
    fun compare_string_property_not_equals_variable(paramStr: String) {
        val errorStr = "does not speaks german"

        var input = """
            IF $paramStr THEN $errorStr
            """

        End2AstRunner.run(input, """{"Language": "german"}""") {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(errorStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Language")
                                .hasType(DataPropertyType.String)
                        .parentCondition()
                            .rightString()
                               .hasValue("german")
                                .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "Applicant.JobStatus MUST be Student"
        ,"Applicant.JobStatus MUST be Student"
        ,"Applicant.JobStatus MUST be Student"
    ])
    fun compare_boolean_propertystring(paramStr: String) {
        var input = "$paramStr"

        End2AstRunner.run(input, "{Applicant: {JobStatus: Student}}") { r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                    .hasPreprocessedSource()
                    .condition()
                        .hasPreprocessedSource()
                        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                        .leftProperty()
                            .hasPreprocessedSource()
                            .hasPath("Applicant.JobStatus")
                            .hasType(DataPropertyType.String)
                    .parentCondition()
                        .rightString()
                            .hasValue("Student")
                            .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "Applicant.Status IS Student AS var1"
        ,"Applicant.Status IS a Student AS var1"
        ,"the Applicant.Status IS a Student AS var1"
    ])
    fun compare_boolean_propertystring_is(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{Applicant: {Status: Student}}") {
                r ->
                    r.variables()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Applicant.Status")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .rightString()
                                    .hasValue("Student")
                                    .hasPreprocessedSource()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Applicant.Status EXISTS AS var"
        ,"Applicant.Status does indeed EXIST AS var"
    ])
    fun exists_operator_without_right_operand(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{Applicant: {Status: Student}}") {
                r ->
                    r.variables()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EXISTS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Applicant.Status")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .hasNoRightOperand()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Applicant.Status EXISTS noise string AS var"
        ,"Applicant.Status does indeed EXIST Applicant.Status AS var"
        ,"Applicant.Status does indeed EXIST with an actual noise property Applicant.Status AS var"
        ,"The Applicant.Status does indeed EXIST with an actual noise property Applicant.Status AS var"
    ])
    fun exists_operator_right_operand_should_be_cut_out(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{Applicant: {Status: Student}}") {
                r ->
                    r.variables()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EXISTS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Applicant.Status")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .hasNoRightOperand()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "sgnd_property AS signed\n\n" +
         "IF signed THEN error "
    ])
    fun compare_boolean_Variable_implicitly(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{sgnd_property: true}") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable("signed")
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                                .rightBoolean(true)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "sgnd_property AS signed\n\n" +
         "IF the contract is signed THEN error "
    ])
    fun compare_boolean_Variable_implicitly_two(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{sgnd_property: true}") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .rightVariable("signed")
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                                .leftBoolean(true)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "sgnd_property AS unterschrieben\n\n" +
         "IF NOT unterschrieben IS THEN error "
    ])
    fun implicit_boolean_negated_german_grammar_style(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{sgnd_property: true}") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                                .leftBoolean(true)
                            .parentCondition()
                                .rightVariable("unterschrieben")
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Boolean)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "sgnd_property AS unterschrieben\n\n" +
         "IF unterschrieben IS THEN error "
    ])
    fun implicit_boolean_german_grammar_style(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{sgnd_property: true}") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable("unterschrieben")
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                                .rightBoolean(true)
        }
    }

    //Todo lazevedo 13.8.19 Not working yet because the property "contract" is chosen before the variable "signed"
    // (Debug in DataSchema.resolve() for more info). This behaviour is currently random
    @Disabled
    @ParameterizedTest
    @ValueSource(strings = [
         "con_signed AS signed\n\n" +
         "IF the contract signed IS THEN error "
    ])
    fun implicit_bool_with_actual_noise_objectProperty_german_grammar_style(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{contract: {id: 25}, con_signed: true}") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable("unterschrieben")
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                                .rightBoolean(true)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "sgnd_property ALS unterschrieben\n\n" +
         "Wenn der Vertrag unterschrieben IST Dann error "
    ])
    fun implicit_boolean_comparison_german_grammar_style(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{sgnd_property: true}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable("unterschrieben")
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                                .rightBoolean(true)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Wenn die Farbe Gelb IST Dann error ",
         "Wenn die farbe Gelb IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_with_string_property_and_static(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{farbe: Gelb}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasPath("farbe")
                            .parentCondition()
                                .rightString("Gelb")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Wenn die Nummer 1337 IST Dann error ",
         "Wenn die nummer 1337 IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_with_number_property_and_static(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{nummer: 1337}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasPath("nummer")
                            .parentCondition()
                                .rightNumber(1337.0)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Wenn die farbe lieblingsfarbe IST Dann error ",
         "Wenn die farbe Lieblingsfarbe IST Dann error ",
         "Wenn die Farbe lieblingsfarbe IST Dann error ",
         "Wenn die Farbe Lieblingsfarbe IST Dann error ",
         "Wenn die Farbe da meine Lieblingsfarbe Gelb IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_with_two_string_properties(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{farbe: Gelb, lieblingsfarbe: Gelb}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasPath("farbe")
                            .parentCondition()
                                .rightProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasPath("lieblingsfarbe")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Wenn die lieblingsfarbe farbe IST Dann error ",
         "Wenn die Lieblingsfarbe farbe IST Dann error ",
         "Wenn die lieblingsfarbe Farbe IST Dann error ",
         "Wenn die Lieblingsfarbe Farbe IST Dann error ",
         "Wenn die Lieblingsfarbe da meine Farbe Gelb IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_with_two_string_properties_other_way_around(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{farbe: Gelb, lieblingsfarbe: Gelb}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .rightProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasPath("farbe")
                            .parentCondition()
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasPath("lieblingsfarbe")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Wenn die Farbe da in der Anzeige meine Lieblingsfarbe überhaupt IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_with_two_string_properties_special_case_ofAccessor(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{farbe: Gelb, lieblingsfarbe: Gelb}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasPath("farbe")
                            .parentCondition()
                                .rightProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasPath("lieblingsfarbe")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Wenn die nummer geheimzahl IST Dann error ",
         "Wenn die nummer Geheimzahl IST Dann error ",
         "Wenn die Nummer geheimzahl IST Dann error ",
         "Wenn die Nummer Geheimzahl IST Dann error ",
         "Wenn die Nummer da auf dem Blatt die Geheimzahl der Tür IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_two_number_properties(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{nummer: 1337, geheimzahl: 1337}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasPath("nummer")
                            .parentCondition()
                                .rightProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasPath("geheimzahl")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "name ALS var1\n\nWenn var1 Peter IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_with_string_variable_and_static(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{nummer: 1337, geheimzahl: 1337}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasName("var1")
                            .parentCondition()
                                .rightString("Peter")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "name ALS var1\n\nname ALS var2\n\nWenn var1 var2 IST Dann error ",
         "name ALS var1\n\nname ALS var2\n\nWenn die var1 die var2 IST Dann error "
    ])
    fun both_operands_on_left_side_of_operator_with_two_string_variables(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{nummer: 1337, geheimzahl: 1337}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasName("var1")
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasName("var2")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "nummer ALS var1\n\nWenn var1 geheimzahl IST Dann error"
    ])
    fun both_operands_on_left_side_of_operator_with_number_variable_and_number_property(paramStr: String) {

        var input = """
               $paramStr
            """

        End2AstRunner.run(input, "{nummer: 1337, geheimzahl: 1337}","de") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasName("var1")
                            .parentCondition()
                                .rightProperty()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.Decimal)
                                    .hasPath("geheimzahl")
        }
    }
}