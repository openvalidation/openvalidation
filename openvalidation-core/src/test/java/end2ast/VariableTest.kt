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
import io.openvalidation.common.data.DataPropertyType
import org.junit.jupiter.api.Test

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class VariableTest {

//    TODO: Soll 5 years/$ oder 5.0($) funktionieren?
    @ParameterizedTest
    @ValueSource(strings = [
          "5"
        , "5.0"
//        , "5.0$"
//        ,"5$"
//        ,"5 years"
    ])
    @Throws(Exception::class)
    internal fun integer_variable(paramStr: String) {
        var input = "$paramStr AS many years"
        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(5.0)
                        .hasName("many years") }
    }

    @Test
    @Throws(Exception::class)
    internal fun string_variable() {
        var input =
                """
                   Haus AS var1 
                """
        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                   .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasString("Haus")
                        .hasName("var1") }
    }

    @Test
    @Throws(Exception::class)
    internal fun multiline_string_variable() {
        var input =
                """
                   Haus
                    Boot
                    Verein AS var1 
                """
        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                .hasSizeOf(1)
                .first()
                    .hasPreprocessedSource()
                    .hasString("Haus\n" +
                            "                    Boot\n" +
                            "                    Verein")
                    .hasName("var1") }
    }

    @ParameterizedTest
    @ValueSource(strings = [
          "5 * 4"
        , "5 TIMES 4"
        , "5 days * 4 days"
        , "all 5 * 4 days"
        , "all 5 days TIMES only 4 days"
    ])
    @Throws(Exception::class)
    internal fun multiplication_variable_times(paramStr: String) {
        var input =
                """
                    $paramStr AS var1
                """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(5.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .staticNumber(4.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
          "4 / 2"
        , "all 4 / 2"
        , "4 DIVIDED BY 2"
    ])
    @Throws(Exception::class)
    internal fun division_numbers_variable_dividedby(paramStr: String) {
        var input =
                """
                   $paramStr AS var1
                """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .arithmetic()
                                .hasPreprocessedSource()
                                .operation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(4.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Division)
                                        .staticNumber(2.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["5 * 4 * 2", "5 TIMES 4 * 2", "5 * 4 TIMES 2", "5 TIMES 4 TIMES 2"])
    @Throws(Exception::class)
    internal fun multiplication_3operands_asterisk(paramStr: String) {
        var input =
                """
                   $paramStr AS var1
                """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasName("var1")
                        .hasPreprocessedSource()
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(3)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasOperator(null)
                                    .staticNumber(5.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .staticNumber(4.0)
                            .parentOperation()
                                .atIndex(2)
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .staticNumber(2.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
          "5 * 4 / 2"
        , "5 TIMES 4 / 2"
        , "5 * 4 DIVIDED BY 2"
        , "5 TIMES 4 DIVIDED BY 2"
    ])
    @Throws(Exception::class)
    internal fun multiplication_division_3operands_asterisk_dividedby(paramStr: String) {
        var input =
                """
                   $paramStr AS var1
                """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .arithmetic()
                                .hasPreprocessedSource()
                                .operation()
                                    .hasSizeOf(3)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(null)
                                        .staticNumber(5.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Multiplication)
                                        .staticNumber(4.0)
                                .parentOperation()
                                    .atIndex(2)
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Division)
                                        .staticNumber(2.0) }
    }

    @Test
    @Throws(Exception::class)
    internal fun two_integer_variables() {
        var input = """
            5 AS var1

            4 AS var2
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                        .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasNumber(5.0)
                            .hasName("var1")
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasNumber(4.0)
                            .hasName("var2") }
    }

    @Test
    @Throws(Exception::class)
    internal fun two_string_variables() {
        var input = """
            Haus AS var1

            Hund AS var2
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                            .hasSizeOf(2)
                            .first()
                                .hasPreprocessedSource()
                                .hasString("Haus")
                                .hasName("var1")
                        .parentList()
                            .second()
                                .hasPreprocessedSource()
                                .hasString("Hund")
                                .hasName("var2") }
    }

    @Test
    @Throws(Exception::class)
    internal fun one_integer_one_string_variables() {
        var input = """
            3 AS var1

            Hund AS var2
            """

        End2AstRunner.run(input, "{}") {
                r-> r.variables()
                        .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasNumber(3.0)
                            .hasName("var1")
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasString("Hund")
                            .hasName("var2") }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "4 things + 2 items",
        "4 PLUS 2",
        "stuff 4 + thing 2",
        "stuff 4 PLUS thing 2",
        "4 stuff + 2 things",
        "4 stuff PLUS 2 things",
        "stuff 4 stuff + things 2 things",
        "stuff 4 stuff PLUS things 2 things"
    ])
    @Throws(Exception::class)
    internal fun addition_variable_plus_literal(paramStr: String) {
        End2AstRunner.run("$paramStr AS var1", "{}") {
                r -> r.variables()
                .hasSizeOf(1)
                .first()
                    .hasPreprocessedSource()
                    .hasName("var1")
                    .arithmetic()
                        .hasPreprocessedSource()
                        .operation()
                            .hasSizeOf(2)
                            .first()
                                .hasPreprocessedSource()
                                .hasNoOperator()
                                .staticNumber(4.0)
                        .parentOperation()
                            .second()
                                .hasPreprocessedSource()
                                .hasOperator(ASTArithmeticalOperator.Addition)
                                .staticNumber(2.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "4 - 2",
        "4 MINUS 2",
        "4 stuff MINUS 2 things",
        "4 stuff - 2 things",
        "stuff 4 MINUS things 2",
        "stuff 4 - things 2",
        "stuff 4 stuff MINUS things 2 things",
        "stuff 4 stuff - things 2 things"
    ])
    @Throws(Exception::class)
    internal fun subtraction_variable_minus_literal(paramStr: String) {
        End2AstRunner.run("$paramStr AS var1", "{}") {
                r -> r.variables()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .arithmetic()
                                .hasPreprocessedSource()
                                .operation()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(4.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(2.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "var1 + 2",
        "var1 PLUS 2",
        "stuff var1 + stuff 2",
        "stuff var1 PLUS stuff 2",
        "var1 stuff + 2 things",
        "var1 stuff PLUS 2 things",
        "things var1 stuff + stuff 2 things",
        "things var1 stuff PLUS stuff 2 things"
    ])
    @Throws(Exception::class)
    internal fun addition_2variables_plus_last_using_previous_variable(paramStr: String) {
        var input = """
            4 AS var1

            $paramStr AS var2
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .hasNumber(4.0)
                .parentList()
                    .second()
                        .hasPreprocessedSource()
                        .hasName("var2")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("var1")
                                        .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                    .staticNumber(2.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2 * var1",
        "2 TIMES var1",
        "2 other TIMES var1",
        "2 other TIMES different var1",
        "2 other * different var1",
        "yet 2 other * different var1 things",
        "yet 2 other TIMES different var1 things"
    ])
    @Throws(Exception::class)
    internal fun multiplication_variable_asterisk_using_previous_variable(paramStr: String) {
        var input = """
            4 AS var1

            $paramStr AS var2
            """

        End2AstRunner.run(input, "{}") {
                r-> r.variables()
                    .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .hasNumber(4.0)
                .parentList()
                    .second()
                        .hasPreprocessedSource()
                        .hasName("var2")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .staticNumber(2.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("var1")
                                        .hasType(DataPropertyType.Decimal)
        }

    }

    @ParameterizedTest
    @ValueSource(strings = ["var1 * var2", "var1 TIMES var2"])
    @Throws(Exception::class)
    internal fun multiplication_3variables_times_using_previous_2variables(paramStr: String) {

        var input = """
            4 AS var1

            2 AS var2

            $paramStr AS var3
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                        .hasSizeOf(3)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .hasNumber(4.0)
                        .parentList()
                            .second()
                                .hasPreprocessedSource()
                                .hasName("var2")
                                .hasNumber(2.0)
                        .parentList()
                            .atIndex(2)
                                .hasPreprocessedSource()
                                .hasName("var3")
                                .arithmetic()
                                    .hasPreprocessedSource()
                                    .operation()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("var1")
                                                .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Multiplication)
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("var2")
                                                .hasType(DataPropertyType.Decimal)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "The Age + 20 years"
        ,"Age + 20"
        ,"The Age in years PLUS 20 years"
    ])
    @Throws(Exception::class)
    internal fun addition_property_static(paramStr: String) {

        End2AstRunner.run("$paramStr AS var1", """{"Age": 30}""") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasOperator(null)
                                    .propertyValue()
                                        .hasPreprocessedSource()
                                        .hasPath("Age")
                                        .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                    .staticNumber(20.0) }
    }

//    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "The Person.Age + 20 years"
        ,"Person.Age + 20"
        ,"The Person.Age in years PLUS 20 years"
        ,"Age OF Person + 20"
        ,"Age OF the Person + 20 years"
//        ,"The Age OF the Person + 20 years"
    ])
    @Throws(Exception::class)
    internal fun addition_property_with_root_static(paramStr: String) {

        End2AstRunner.run("$paramStr AS var1", """{"Person": {"Age": 30}}""") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                            .hasSizeOf(2)
                            .first()
                                .hasPreprocessedSource()
                                .hasNoOperator()
                                .propertyValue()
                                    .hasPreprocessedSource()
                                    .hasPath("Person.Age")
                                    .hasType(DataPropertyType.Decimal)
                        .parentOperation()
                            .second()
                                .hasPreprocessedSource()
                                .hasOperator(ASTArithmeticalOperator.Addition)
                                .staticNumber(20.0) }
    }

    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "100 - the Person.Age"
        ,"100 - Person.Age"
        ,"100 MINUS the Person.Age in years"
        ,"100 - Age OF Person"
        ,"100 Years MINUS Age OF the Person"
//        ,"100 Years - the Age OF the Person"
    ])
    @Throws(Exception::class)
    internal fun subtraction_property_with_root_static(paramStr: String) {

        End2AstRunner.run("$paramStr AS remaining years", """{"Person": {"Age": 30}}""") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("remaining years")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(100.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .propertyValue()
                                        .hasPreprocessedSource()
                                        .hasPath("Person.Age")
                                        .hasType(DataPropertyType.Decimal)
        }
    }

    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "100 - the Person.Age - 20"
        ,"100 - Person.Age - 20"
        ,"100 MINUS the Person.Age in years - 20 years"
        ,"100 - Age OF Person MINUS 20 years"
        ,"100 Years MINUS Age OF the Person MINUS 20 Years"
//        ,"100 Years - the Age OF the Person MINUS 20 years"
    ])
    @Throws(Exception::class)
    internal fun subtraction_property_with_root_2static(paramStr: String) {

        End2AstRunner.run("$paramStr AS remaining years", """{"Person": {"Age": 30}}""") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("remaining years")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(3)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(100.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .propertyValue()
                                        .hasPreprocessedSource()
                                        .hasPath("Person.Age")
                                        .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .atIndex(2)
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .staticNumber(20.0)
        }
    }

    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
         "100 - Person.Age - var1"
        , "100 - the Person.Age - var1"
        ,"100 MINUS the Person.Age in years - var1 years"
        ,"100 - Age OF Person MINUS var1 years"
        ,"100 Years MINUS Age OF the Person MINUS var1 Years"
//        ,"100 Years - the Age OF the Person MINUS var1 years"
    ])
    @Throws(Exception::class)
    internal fun subtraction_property_with_root_variable_static(paramStr: String) {

        var input = """
            10 AS var1

            $paramStr AS some number
            """

        End2AstRunner.run(input, """{"Person": {"Age": 30}}""") {
            r -> r.variables()
                    .hasSizeOf(2)
                .first()
                    .hasPreprocessedSource()
                    .hasName("var1")
                    .hasNumber(10.0)
            .parentModel()
                .variables()
                    .second()
                            .hasPreprocessedSource()
                        .hasName("some number")
                            .arithmetic()
                                .hasPreprocessedSource()
                                .operation()
                                    .hasSizeOf(3)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                            .staticNumber(100.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .propertyValue()
                                            .hasPreprocessedSource()
                                            .hasPath("Person.Age")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .atIndex(2)
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasType(DataPropertyType.Decimal)
                                            .hasName("var1")
        }
    }

    @Test
    @Throws(Exception::class)
    internal fun string_from_schema_variable() {

        End2AstRunner.run("""Name AS var1""", """{"Name": "John Watson"}""") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .operandProperty()
                            .hasPreprocessedSource()
                            .hasType(DataPropertyType.String)
                            .hasPath("Name") }
    }

    @Test
    @Throws(Exception::class)
    internal fun string_integer_from_schema_variable() {

        var input = """
            Name AS var1

            Age AS var2
            """

        End2AstRunner.run(input, """{"Name": "John Watson", "Age": 50}""") {
                r -> r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .operandProperty()
                            .hasPreprocessedSource()
                            .hasType(DataPropertyType.String)
                            .hasPath("Name")
                .parentModel()
                    .variables()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("var2")
                            .operandProperty()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasPath("Age") }

    }


    @ParameterizedTest
    @ValueSource(strings = [
        "var1 - 25"
        ,"var1 MINUS 25"
        ,"var1 years - 25 years"
        ,"var1 years MINUS 25 years"
        ,"years var1 - years 25"
        ,"years var1 MINUS years 25"
        ,"all var1 years - the 25 years"
        ,"all var1 years MINUS the 25 years"
    ])
    @Throws(Exception::class)
    internal fun addition_static_integer_schema_variable(paramStr: String) {

        var input = """
            Age AS var1

            $paramStr AS NotWorking
            """

        End2AstRunner.run(input, """{"Age": 50}""") {
            r -> r.variables()
                        .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .operandProperty()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasPath("Age")
                .parentModel()
                    .variables()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("NotWorking")
                            .arithmetic()
                                .hasPreprocessedSource()
                                .operation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("var1")
                                            .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Subtraction)
                                            .staticNumber(25.0)
        }

    }

    @ParameterizedTest
    @ValueSource(strings = [
         "var2 - var1"
        ,"var2 MINUS var1"
        ,"var2 years MINUS var1 years"
        ,"all var2 years MINUS the var1 years"
        ,"all var2 years - the var1 years"
    ])
    @Throws(Exception::class)
    internal fun addition_schema_variables(paramStr: String) {

        var input = """
            Experience AS var1

            Age AS var2

            $paramStr AS NotWorking
            """

        End2AstRunner.run(input, """{"Name": "John Watson", "Age": 50, "Experience": 25}""") {
            r -> r.variables()
                .hasSizeOf(3)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .operandProperty()
                            .hasPreprocessedSource()
                            .hasType(DataPropertyType.Decimal)
                            .hasPath("Experience")
            .parentModel()
                .variables()
                    .second()
                        .hasPreprocessedSource()
                        .hasName("var2")
                        .operandProperty()
                            .hasPreprocessedSource()
                            .hasType(DataPropertyType.Decimal)
                            .hasPath("Age")
            .parentModel()
                .variables()
                    .atIndex(2)
                        .hasPreprocessedSource()
                        .hasName("NotWorking")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .first()
                                    .hasPreprocessedSource()
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("var2")
                                        .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("var1")
                                        .hasType(DataPropertyType.Decimal)
        }

    }

    @ParameterizedTest
    @ValueSource(strings = [
        "var2 - var1"
        ,"var2 MINUS var1"
        ,"var2 years MINUS var1 years"
        ,"all var2 years MINUS the var1 years"
        ,"all var2 years - the var1 years"
    ])
    @Throws(Exception::class)
    internal fun subtraction_schema_variables_properties(paramStr: String) {

        var input = """
            Person.Experience AS var1

            Person.Age AS var2

            $paramStr AS NotWorking
            """

        End2AstRunner.run(input, """{"Person": {"Name": "John Watson", "Age": 50, "Experience": 25}}""") {
            r -> r.variables()
                    .hasSizeOf(3)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .operandProperty()
                            .hasPreprocessedSource()
                            .hasType(DataPropertyType.Decimal)
                            .hasPath("Person.Experience")
                .parentModel()
                    .variables()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("var2")
                            .operandProperty()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasPath("Person.Age")
            .parentModel()
                .variables()
                    .atIndex(2)
                        .hasPreprocessedSource()
                        .hasName("NotWorking")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("var2")
                                        .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("var1")
                                        .hasType(DataPropertyType.Decimal)
        }

    }

    @ParameterizedTest
    @ValueSource(strings = [
        "Berlin, Paris, London as capital cities"
    ])
    @Throws(Exception::class)
    internal fun variable_with_operand_array(paramStr: String) {

        var input = paramStr;

        End2AstRunner.run(input, """{"location": ""}""") {
            r -> r.variables()
                .first()
                .hasName("capital cities")
                .operandArray()
                .hasSize(3)
                .stringAtPosition("Berlin", 0)
                .stringAtPosition("Paris", 1)
                .stringAtPosition("London", 2)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "Berlin, Paris, London or Madrid as capital cities",
        "Berlin, Paris or London or Madrid as capital cities",
        "Berlin or Paris or London or Madrid as capital cities",
        "Berlin or Paris, London or Madrid as capital cities"
    ])
    @Throws(Exception::class)
    internal fun variable_with_operand_array_with_or(paramStr: String) {

        var input = paramStr;

        End2AstRunner.run(input, """{"location": ""}""") {
            r -> r.variables()
                .first()
                .hasName("capital cities")
                .operandArray()
                .hasSize(4)
                .stringAtPosition("Berlin", 0)
                .stringAtPosition("Paris", 1)
                .stringAtPosition("London", 2)
                .stringAtPosition("Madrid", 3)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "1, 2, 3 or 4 as capital cities",
        "1, 2 or 3 or 4 as capital cities",
        "1 or 2 or 3 or 4 as capital cities",
        "1 or 2, 3 or 4 as capital cities"
    ])
    @Throws(Exception::class)
    internal fun variable_with_operand_number_array_with_or(paramStr: String) {

        var input = paramStr;

        End2AstRunner.run(input, """{"location": ""}""") {
            r -> r.variables()
                .first()
                .hasName("capital cities")
                .operandArray()
                .hasSize(4)
                .numberAtPosition(1.0, 0)
                .numberAtPosition(2.0, 1)
                .numberAtPosition(3.0, 2)
                .numberAtPosition(4.0, 3)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "    Berlin, Paris or London as capital cities\n\nthe location must be one of the capital cities"
    ])
    @Throws(Exception::class)
    internal fun variable_with_operand_array_with_or_and_rule(paramStr: String) {

        var input = paramStr;

        End2AstRunner.run(input, """{"location": ""}""") {
            r -> r.variables()
                .first()
                .hasName("capital cities")
                .operandArray()
                .hasSize(3)
                .stringAtPosition("Berlin", 0)
                .stringAtPosition("Paris", 1)
                .stringAtPosition("London", 2)
                .parentVariable()
                .parentModel()
                .rules()
                .firstCondition()
                .hasOperator(ASTComparisonOperator.NONE_OF)
                .leftProperty("location")
                .parentCondition()
                .rightVariable("capital cities")

        }
    }


}
