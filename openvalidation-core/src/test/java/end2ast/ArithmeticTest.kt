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
import io.openvalidation.common.data.DataPropertyType
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ArithmeticTest {



    //TODO: currently, arithmetic operators have to be surrounded by spaces, so no 5*6/3. This will be changed later.
    @ParameterizedTest
    @ValueSource(strings = [
          "5 * 6 / 3"
        , "5 TIMES 6 / 3"
        , "5 * 6 DIVIDED BY 3"
        , "5 TIMES 6 DIVIDED BY 3"
        , "5 things * 6 things / 3 stuff"
        , "5 things TIMES 6 things DIVIDED BY 3 stuff"
    ])
    @Throws(Exception::class)
    fun multiplication_division_asterisk_slash_spaces(paramStr: String) {
        val input = """
            $paramStr AS more stuff
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("more stuff")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(3)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(5.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .staticNumber(6.0)
                            .parentOperation()
                                .atIndex(2)
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Division)
                                    .staticNumber(3.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        """5 
            *
             6"""
        ,"""
            5 *
            6
        """
        ,""""5 
            TIMES
             6"""
        , """5 
                * 6"""
        , """"5 TIMES 
            6"""
        , """"5 
            TIMES 6"""
        , """5 
            things 
            * 
            6 
            things"""
        , """5 
            things 
            TIMES 
            6 
            things"""
    ])
    @Throws(Exception::class)
    fun multiline_multiplication_operation_tests(paramStr: String) {
        val input = """
            $paramStr AS more stuff
            """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("more stuff")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(5.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .staticNumber(6.0)
            }
                
    }

    @ParameterizedTest
    @ValueSource(strings = [
          "50000 * 12 - 2000"
        , "50000 * 12 MINUS 2000"
        , "50000€ * 12 months - 2000€"
        , "50000 dollars * 12 months - 2000 dollars"
        , "50000 dollars TIMES 12 months MINUS 2000 dollars"
    ])
    @Throws(Exception::class)
    fun multiplication_minus(paramStr: String) {
        val input = """
            $paramStr AS lotta money
            """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("lotta money")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(3)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(50000.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .staticNumber(12.0)
                            .parentOperation()
                                .atIndex(2)
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .staticNumber(2000.0) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "50000 * (4000 - 2000)"
        , "50000 * (4000 MINUS 2000)"
        , "50000€ * (4000€ - 2000€)"
        , "50000 dollars * (4000€ months - 2000 dollars)"
        , "50000 dollars TIMES (4000 dollars MINUS 2000 dollars)"
    ])
    @Throws(Exception::class)
    fun multiplication_minus_plus(paramStr: String) {
        val input = """
            $paramStr AS lotta money
            """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                .hasSizeOf(1)
                .first()
                    .hasName("lotta money")
                    .arithmetic()
                        .hasPreprocessedSource()
                        .operation()
                            .hasPreprocessedSource()
                            .hasSizeOf(2)
                            .hasNoOperator()
                            .first()
                                .hasPreprocessedSource()
                                .hasNoOperator()
                                .staticNumber(50000.0)
                        .parentOperation()
                            .firstSubOperation()
                                .hasPreprocessedSource()
                                .hasSizeOf(2)
                                .hasOperator(ASTArithmeticalOperator.Multiplication)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(4000.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .staticNumber(2000.0)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "50000€ + ((12 - 2) * 2000€)"
        ,"50000€ PLUS ((12 MINUS 2) * 2000€)"
        ,"50000€ PLUS ((12 MINUS 2) TIMES 2000€)"
        ,"the 50000€ PLUS ((some 12 MINUS some 2) TIMES many 2000€)"
        ,"the 50000 euros PLUS another ((some 12 times MINUS some 2 things) TIMES many 2000 euros)"
    ])
    @Throws(Exception::class)
    fun multiplication_minus_plus_parentheses(paramStr: String) {
        val input = """
            $paramStr AS lotta money
            """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("lotta money")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasPreprocessedSource()
                                .hasSizeOf(2)
                                .hasNoOperator()
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(50000.0)
                            .parentOperation()
                                .firstSubOperation()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                    .hasSizeOf(2)
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasNoOperator()
                                            .staticNumber(12.0)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Subtraction)
                                            .staticNumber(2.0)
                                    .parentOperation()
                                .parentOperation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Multiplication)
                                        .staticNumber(2000.0)

        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
          "var1 * var2 / var3"
        , "var1 TIMES var2 / var3"
        , "var1 * var2 DIVIDED BY var3"
        , "var1 TIMES var2 DIVIDED BY var3"
        , "the var1 years * another var2 years / some var3 years"
    ])
    @Throws(Exception::class)
    fun multiplication_division_4variables_times_dividedby_using_previous_2variables(paramStr: String) {
        val input = """
            4 AS var1

            2 AS var2

            4  AS var3

            $paramStr AS var4
            """

        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(4)
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
                    .third()
                        .hasPreprocessedSource()
                        .hasName("var3")
                        .hasNumber(4.0)
                .parentList()
                    .atIndex(3)
                        .hasPreprocessedSource()
                        .hasName("var4")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(3)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .variableValue()
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
                            .parentOperation()
                                .atIndex(2)
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Division)
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("var3")
                                        .hasType(DataPropertyType.Decimal) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
          "(var1 + var2) / var3"
        , "(var1 PLUS var2) / var3"
        , "(var1 + var2) DIVIDED BY var3"
        , "(var1 PLUS var2) DIVIDED BY var3"
        , "(var1 stuff PLUS var2 stuff) / var3 stuff"
        , "(var1 stuff + var2 stuff) DIVIDED BY var3 stuff"
        , "(sugar var1 thing PLUS thing var2 sugar) DIVIDED BY var3 stuff"
        , "(sugar var1 PLUS var2 thing) DIVIDED BY var3 crap"
        , "(totally var1 PLUS var2 blob) DIVIDED BY var3 stuffy"
    ])
    @Disabled
    @Throws(Exception::class)
    fun addition_division_4variables_times_dividedby_parentheses(paramStr: String) {
        val input = """
            4 AS var1

            2 AS var2

            4  AS var3

            $paramStr AS stuff
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(4)
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
                    .third()
                        .hasPreprocessedSource()
                        .hasName("var3")
                        .hasNumber(4.0)
                .parentList()
                    .atIndex(3)
                        .hasPreprocessedSource()
                        .hasName("stuff")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(3)
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
                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                    .variableValue()
                                    .hasName("var2")
                                    .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .atIndex(2)
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Division)
                                    .variableValue()
                                        .hasName("var3")
                                        .hasType(DataPropertyType.Decimal) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
          "Age - 18"
        , "Age MINUS 18"
        , "Age - 18 Years"
        , "Age MINUS 18 Years"
        , "The Age MINUS 18 Years"
        , "The Age - 18 Years"
        , "The Age years MINUS 18 Years"
        , "The Age years - 18 Years"
    ])
    @Throws(Exception::class)
    fun subtraction_staticnumber_from_schema_variable(paramStr: String) {
        val input = """
             $paramStr AS var1
            """

        End2AstRunner.run(input, """{"Age": 17}""") {
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
                                                .hasPath("Age")
                                                .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Subtraction)
                                            .staticNumber(18.0) }

    }


    @ParameterizedTest
    @ValueSource(strings = [
         "65 - Age"
        , "65 MINUS Age"
        , "65 - Age Years"
        , "65 MINUS Age Years"
        , "The 65 MINUS Age Years"
        , "The 65 - the Age Years"
        , "The 65 years MINUS the Age Years"
        , "The 65 years - the Age Years"
    ])
    @Throws(Exception::class)
    fun subtraction_schema_variable_from_staticnumber(paramStr: String) {
        val input = """
             $paramStr AS var1
            """

        End2AstRunner.run(input, """{"Age": 17}""") { r ->
            r.variables()
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
                                        .staticNumber(65.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .propertyValue()
                                            .hasPreprocessedSource()
                                            .hasPath("Age")
                                            .hasType(DataPropertyType.Decimal)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
          "65 - Person.Age"
        , "65 MINUS Person.Age"
        , "65 - Person.Age Years"
        , "65 MINUS Person.Age Years"
        , "The 65 MINUS Person.Age Years"
        , "The 65 - the Person.Age Years"
        , "The 65 years MINUS the Person.Age Years"
        , "The 65 years - the Person.Age Years"
    ])
    @Throws(Exception::class)
    fun subtraction_nested_schema_variable_from_staticnumber(paramStr: String) {
        val input = """
             $paramStr AS var1
            """

            End2AstRunner.run(input, """{"Person": {"Age": 30}}""") {
                r ->
                    r.variables()
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
                                            .staticNumber(65.0)
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


    @ParameterizedTest
    @ValueSource(strings = [
          "Age - minAge"
        , "Age MINUS minAge"
        , "the Age MINUS the minAge"
        , "Age - minAge Years"
        , "the Age MINUS the minAge years"
        , "the Age years MINUS the minAge years"
    ])
    @Throws(Exception::class)
    fun subtraction_numbervariable_from_schema_variable(paramStr: String) {
        val input = """
            18 AS minAge

            $paramStr AS age difference
            """

        End2AstRunner.run(input, """{"Age": 17}""") {
                r -> r.variables()
                .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("minAge")
                        .hasNumber(18.0)
                .parentList()
                    .second()
                        .hasPreprocessedSource()
                        .hasName("age difference")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .propertyValue()
                                        .hasPreprocessedSource()
                                        .hasPath("Age")
                                        .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Subtraction)
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("minAge")
                                        .hasType(DataPropertyType.Decimal) }

    }

    @ParameterizedTest
    @ValueSource(strings = [
          "( Age * 2) - minAge"
        , "( Age * 2) MINUS minAge"
        , "(Age * 2) MINUS minAge"
        , "(the Age * 2) MINUS the minAge"
        , "( Age * 2) - minAge Years"
        , "(the Age * 2) MINUS the minAge years"
        , "(the Age * 2) years MINUS the minAge years"
    ])
    @Throws(Exception::class)
    fun subtraction_numbervariable_static_from_schema_variable(paramStr: String) {
        val input = """
            18 AS minAge

            $paramStr AS age difference
            """

        End2AstRunner.run(input, """{"Age": 17}""") {
            r -> r.variables()
                        .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("minAge")
                            .hasNumber(18.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("age difference")
                            .arithmetic()
                                .hasPreprocessedSource()
                                .operation()
                                    .hasNoOperator()
                                    .hasSizeOf(2)
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .propertyValue()
                                                .hasPath("Age")
                                                .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Multiplication)
                                            .staticNumber(2.0)
                                    .parentOperation()
                                .parentOperation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("minAge")
                                            .hasType(DataPropertyType.Decimal) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "5 MOD 3"
        , "5 things MOD 3 things"
        , "all 5 things MOD other 3 things"
    ])
    @Throws(Exception::class)
    fun modulo_numbers_mod(paramStr: String) {
        val input = """
            $paramStr AS mod rest
            """

        End2AstRunner.run(input, "{}") {
                r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("mod rest")
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(5.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Modulo)
                                    .staticNumber(3.0) }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "5 TO THE POWER OF 3"
        , "5 days TO THE POWER OF about 3 days"
        , "5 ^ 3"
        , "the 5 ^ the 3"
        , "the 5 things ^ all 3 other things"
    ])
    @Throws(Exception::class)
    fun power_numbers_simple(paramStr: String) {
        val input = """
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
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .staticNumber(5.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Power)
                                    .staticNumber(3.0) }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "5 TO THE POWER OF ThePower"
        , "5 days TO THE POWER OF about ThePower days"
        , "5 ^ ThePower"
        , "the 5 ^ the ThePower"
        , "the 5 things ^ all ThePower other things"
    ])
    @Throws(Exception::class)
    fun power_number_schema_simple(paramStr: String) {
        val input = """
            $paramStr AS var1
            """

        End2AstRunner.run(input, "{ThePower: 3}") { r ->
                r.variables()
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
                                    .staticNumber(5.0)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Power)
                                    .propertyValue()
                                        .hasPreprocessedSource()
                                        .hasPath("ThePower")
                                        .hasType(DataPropertyType.Decimal)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "(50000$ PLUS 2000$) TO THE POWER OF (10 things MINUS 8 items)"
        ,"(50000 PLUS 2000) ^ (the 10 MINUS some 8)"
        ,"(50000€ + 2000€) ^ (10 MINUS 8)"
        ,"(50000 euros + 2000 euros) ^ (the 10 items - another 8 things)"
    ])
    @Throws(Exception::class)
    fun power_numbers_(paramStr: String) {
        val input = """
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
                                        .staticNumber(50000.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .staticNumber(2000.0)
                                .parentOperation()
                            .parentOperation()
                                .secondSubOperation()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Power)
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(10.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(8.0)
            }
    }


    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "100 - Person.Age - ( 20 / var1 )"
        ,"100 - the Person.Age - (20 / var1 )"
        ,"100 - the Person.Age - (20 / var1)"
        ,"100 MINUS the Person.Age in years - (20 years DIVIDED BY var1 years)"
        ,"100 - Age OF Person MINUS (20 years / var1 years)"
        ,"100 Years MINUS Age OF the Person MINUS (20 years DIVIDED BY the var1 Years)"
//        ,"100 Years - the Age OF the Person MINUS (20 / var1 years)"
    ])
    @Throws(Exception::class)
    fun subtraction_property_with_root_variable_2static(paramStr: String) {

        val input = """
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
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .staticNumber(20.0)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Division)
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasName("var1")
        }
    }


    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "100 - var1 + ( 20 / Person.Age )"
        ,"100 - var1 + (20 / Person.Age )"
        ,"100 - var1 + (20 / Person.Age)"
        ,"100 MINUS the var1 in years + (20 years DIVIDED BY Person.Age years)"
        ,"100 - var1 PLUS (20 years / Person.Age years)"
        ,"100 Years MINUS var1 PLUS (20 years DIVIDED BY the Person.Age Years)"
//        ,"100 Years - var1 PLUS (20 years / the Age OF the Person)"
    ])
    @Throws(Exception::class)
    fun subtraction_property_with_variable_2static(paramStr: String) {

        val input = """
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
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasType(DataPropertyType.Decimal)
                                        .hasName("var1")
                            .parentOperation()
                                .firstSubOperation()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(20.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Division)
                                        .propertyValue()
                                            .hasPath("Person.Age")
                                            .hasType(DataPropertyType.Decimal)

        }
    }

    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "(10 ^ 2) - Person.Age - ( 20 / var1 )"
        ,"(10 ^ 2) - the Person.Age - (20 / var1 )"
        ,"(10 TO THE POWER OF 2) - the Person.Age - (20 / var1)"
        ,"(10 TO THE POWER OF 2) MINUS the Person.Age in years - (20 years DIVIDED BY var1 years)"
        ,"(10 TO THE POWER OF 2) - Age OF Person MINUS (20 years / var1 years)"
        ,"(10 ^ 2) Years MINUS Age OF the Person MINUS (20 years DIVIDED BY the var1 Years)"
//        ,"(10 TO THE POWER OF 2) Years - the Age OF the Person MINUS (20 / var1 years)"
    ])
    @Throws(Exception::class)
    fun subtraction_property_with_root_variable_2static_power(paramStr: String) {

        val input = """
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
                                    .hasPreprocessedSource()
                                    .hasSizeOf(3)
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .staticNumber(10.0)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Power)
                                            .staticNumber(2.0)
                                    .parentOperation()
                                .parentOperation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .propertyValue()
                                            .hasPath("Person.Age")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .secondSubOperation()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .staticNumber(20.0)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Division)
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasName("var1")
        }
    }


    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "( Person.Age + 100) * ((20 + var1 ) / 10)"
        ,"( Person.Age + 100) * ((20 + var1 ) / 10)"
        ,"(Person.Age + 100) * ((20 + var1) / 10)"
        ,"( Person.Age in years + 100) * ((20 years PLUS var1 years) DIVIDED BY 10)"
        ,"( Person.Age in years + 100) TIMES ((20 years + var1 years) DIVIDED BY 10)"
        ,"( Person.Age in years + 100 Years) TIMES ((20 years PLUS the var1 Years) DIVIDED BY 10)"
        ,"(Person.Age in years + 100)  TIMES ((20 + var1 years) DIVIDED BY 10)"
    ])
    @Throws(Exception::class)
    fun subtraction_property_with_root_variable_3static_parentheses(paramStr: String) {

        val input = """
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
                                .hasSizeOf(2)
                                .firstSubOperation()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
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
                                        .staticNumber(100.0)
                                .parentOperation()
                            .parentOperation()
                                .secondSubOperation()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .hasSizeOf(2)
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .staticNumber(20.0)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Addition)
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasName("var1")
                                    .parentOperation()
                                .parentOperation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Division)
                                        .staticNumber(10.0)

        }
    }


    //    TODO: The Age OF the Person geht nicht: Das The vor Age ist das Problem. Muss noch korrigiert werden (falls es gehen soll)
    @ParameterizedTest
    @ValueSource(strings = [
        "( Person.Age + 100) * ((20 + var1 ) / Person.Age )"
        ,"( Person.Age + 100) * ((20 + var1 ) / Person.Age )"
        ,"(Person.Age + 100) * ((20 + var1) / Person.Age)"
        ,"( Person.Age in years + 100) * ((20 years PLUS var1 years) DIVIDED BY Person.Age )"
        ,"( Person.Age in years + 100) TIMES ((20 years + var1 years) DIVIDED BY Person.Age )"
        ,"( Person.Age in years + 100 Years) TIMES ((20 years PLUS the var1 Years) DIVIDED BY Person.Age )"
        ,"(Person.Age in years + 100)  TIMES ((20 + var1 years) DIVIDED BY Person.Age)"
    ])
    @Throws(Exception::class)
    fun subtraction_property_after_root_variable_3static_parentheses(paramStr: String) {

        val input = """
            10 AS var1

            $paramStr AS some number
            """

        End2AstRunner.run(input, """{"Person": {"Age": 30}}""") {
            r ->
                r.variables()
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
                                .hasSizeOf(2)
                                .hasNoOperator()
                                .firstSubOperation()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
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
                                        .staticNumber(100.0)
                                .parentOperation()
                            .parentOperation()
                                .secondSubOperation()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .hasSizeOf(2)
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .staticNumber(20.0)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Addition)
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("var1")
                                                .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                .parentOperation()
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Division)
                                            .propertyValue()
                                                .hasPreprocessedSource()
                                                .hasPath("Person.Age")
                                                .hasType(DataPropertyType.Decimal)

        }
    }
    
    
    //todo 31.07.19 jgeske division by zero should not be possible, or should create NaN result - NaN is currently not supported in BigDecimal (generated source). maybe a flag in AST
    @Test
    fun division_by_zero(){
        var input =
                """
if number / 0 equals 0
then division by zero error
"""
        var schema = "{number:1}"

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .hasError("division by zero error")
        }
    }


}
