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

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class DocumentationTest {

    @Test
    @Throws(Exception::class)
    fun hello_world() {
        val input =
                """
                     IF the message IS Hello
                   THEN hello openVALIDATION!
                """
        val schema =
                """
                    {
                        Message : 'Text'
                    } 
                """

        End2AstRunner.run(input, schema) {
            r ->
                r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                    .hasOperator(ASTComparisonOperator.EQUALS)
                    .leftProperty()
                        .hasPath("Message")
                    .parentCondition()
                    .rightString()
                        .hasValue("Hello")
                    .parentCondition()
                .parentRule()
                .hasError("hello openVALIDATION!") }
    }

    @Test
    @Throws(Exception::class)
    fun age_of_applicant_less_18() {
val input =
                """
                     IF the age of the applicant is LESS than 18
                   THEN you must be at least 18 years old
                """
        val schema =
                """
                    {
		                age: 18
                    } 
                """

        End2AstRunner.run(input, schema) {
            r-> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                    .hasOperator(ASTComparisonOperator.LESS_THAN)
                    .leftProperty()
                        .hasPath("age")
                    .parentCondition()
                    .rightNumber()
                        .hasValue(18.0)
                    .parentCondition().parentRule()
                    .hasError("you must be at least 18 years old")}
    }

    @Test
    @Throws(Exception::class)
    fun age_less_18() {
        val input =
                """
                       IF age LESS 18
                     THEN you must be at least 18 years old
                """
        val schema =
                """
                    {
                        age : 0
                    }
                """

        End2AstRunner.run(input, schema) {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                    .hasOperator(ASTComparisonOperator.LESS_THAN)
                    .leftProperty()
                        .hasPath("age")
                    .parentCondition()
                    .rightNumber()
                        .hasValue(18.0)
                    .parentCondition()
                .parentRule()
                .hasError("you must be at least 18 years old")}
    }


    @Test
    @Throws(Exception::class)
    fun age_of_applicant_less_18_and_residence_rule(){
        val input =
                """
                        IF the age of the applicant is LESS than 18
                       AND his place of residence IS NOT Dortmund
                      THEN You must be at least 18 years old and live in Dortmund. 
                """
        val schema =
                """
                    {
                        age: 0,
                        place: 'whatever'
                    }
                """

        End2AstRunner.run(input, schema) {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .hasError("You must be at least 18 years old and live in Dortmund.")
                    .conditionGroup()
                        .hasSize(2)
                        .first()
                            .leftProperty()
                                .hasPath("age")
                            .parentCondition()
                            .rightNumber()
                                .hasValue(18.0)
                        .parentCondition()
                    .parentConditionGroup()
                        .second()
                            .leftProperty()
                                .hasPath("place")
                        .parentCondition()
                            .rightString()
                                .hasValue("Dortmund")}
    }


    @Test
    @Throws(Exception::class)
    fun age_of_applicant_less_18_and_residence_rule_simple(){
        val input =
                """
                        the age of the applicant SHOULD NOT be LESS than 18 years old
                    AND his place OF residence MUST be Dortmund
                """
        val schema =
                """
                    {
                        age: 0,
                        place: 'whatever'
                    }
                """

        End2AstRunner.run(input, schema) {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .conditionGroup()
                            .hasSize(2)
                            .first()
                                .leftProperty()
                                .hasPath("age")
                            .parentCondition()
                                .rightNumber()
                                    .hasValue(18.0)
                            .parentCondition()
                        .parentConditionGroup()
                            .second()
                                .leftProperty()
                                    .hasPath("place")
                            .parentCondition()
                                .rightString()
                                    .hasValue("Dortmund")}
    }


    @Test
    @Throws(Exception::class)
    fun age_less_18_and_name_is_string_or_name_is_other_string(){
        val input =
                """
IF  the age of the applicant is GREATER 18
 AND  the name of the applicant IS Mycroft Holmes.
    OR his name IS Sherlock Holmes.
 THEN  the applicant is a genius
                """
        val schema =
                """
                    {
                        age: 0,
                        name: 'Watson'
                    }
                """


        End2AstRunner.run(input, schema) { r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .hasError("the applicant is a genius")
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .leftProperty()
                    .hasPath("age")
                    .parentCondition()
                    .rightNumber()
                    .hasValue(18.0)
                    .parentCondition()
                    .parentConditionGroup()
                    .firstConditionGroup()
                    .hasSize(2)
                    .first()
                    .leftProperty()
                    .hasPath("name")
                    .parentCondition()
                    .rightString()
                    .hasValue("Mycroft Holmes")
                    .parentCondition()
                    .hasOperator(ASTComparisonOperator.EQUALS)
                    .parentConditionGroup()
                    .second()
                    .leftProperty()
                    .hasPath("name")
                    .parentCondition()
                    .rightString()
                    .hasValue("Sherlock Holmes")
                    .parentCondition()
                    .hasOperator(ASTComparisonOperator.EQUALS)
                    .hasConnector(ASTConditionConnector.OR)
        }
    }

    @Test
    @Throws(Exception::class)
    fun age_less_18_and_place_is_not_dortmund(){
        val input =
                """
                        IF the age of the applicant is LESS than 18
                       AND his place of residence IS NOT Dortmund
                      THEN You must be at least 18 years old and live in Dortmund.
                """
        val schema =
                """
                    {
                        age: 0,
                        place: 'London'
                    }
                """
        End2AstRunner.run(input, schema) {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError("You must be at least 18 years old and live in Dortmund.")
                        .conditionGroup()
                        .hasSize(2)
                            .first()
                                .hasOperator(ASTComparisonOperator.LESS_THAN)
                                .leftProperty()
                                    .hasPath("age")
                                .parentCondition()
                                .rightNumber()
                                    .hasValue(18.0)
                        .parentCondition().parentConditionGroup()
                            .second()
                                .hasConnector(ASTConditionConnector.AND)
                                .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                                .leftProperty()
                                    .hasPath("place")
                            .parentCondition()
                                .rightString()
                                    .hasValue("Dortmund")}
        
    }

    @Test
    @Throws(Exception::class)
    fun age_less_18_or_exp_shorter_5_double_quoted_string(){
        val input =
                """
                             the age of the applicant is LESS than 18 years old
                          AS underage

                          IF the applicant IS underage
                         AND his residence IS NOT Dortmund
                        THEN You must be at least 18 years old and come from Dortmund.

                          IF the applicant IS underage
                          OR his professional experience is LESS than 5 years
                        THEN You must be at least 18 years old and have work experience of at least 5 years
                """
        val schema =
                """
                    {
                        age: 0,
                        "professional experience" : 1,
                        residence: "hamburg"
                    }
                """
        End2AstRunner.run(input, schema) { r ->
            r.rules()
                    .hasSizeOf(2)
                    .first()
                    .hasError("You must be at least 18 years old and come from Dortmund.")
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .leftVariable()
                    .hasName("underage")
                    .hasType(DataPropertyType.Boolean)
                    .parentCondition()
                    .rightOperand()
                    .parentCondition()
                    .parentConditionGroup()
                    .second()
                    .hasConnector(ASTConditionConnector.AND)
                    .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                    .leftProperty()
                    .hasPath("residence")
                    .parentCondition()
                    .rightString()
                    .hasValue("Dortmund")
                    
                    r.rules()
                            .second()
                            .hasError("You must be at least 18 years old and have work experience of at least 5 years")
                            .conditionGroup()
                            .hasSize(2)
                            .first()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftVariable()
                            .hasName("underage")
                            .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                            .rightOperand()//staticOperands do not have any checks, like .IsStatic()
                            .parentCondition()
                            .parentConditionGroup()
                            .second()
                            .hasConnector(ASTConditionConnector.OR)
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                            .leftProperty()
                            .hasPath("professional experience")
                            .parentCondition()
                            .rightNumber()
                            .hasValue(5.0)
            
            r.variables()
                    .hasSizeOf(1)
                    .first()
                    .hasName("underage")
                    .condition()
                    .leftProperty()
                    .hasPath("age")
                    .parentCondition()
                    .rightNumber()
                    .hasValue(18.0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun age_less_18_or_exp_shorter_5_single_quoted_string(){
        val input =
                """
                             the age of the applicant is LESS than 18 years old
                          AS underage

                          IF the applicant IS underage
                         AND his residence IS NOT Dortmund
                        THEN You must be at least 18 years old and come from Dortmund.

                          IF the applicant IS underage
                          OR his professional experience is LESS than 5 years
                        THEN You must be at least 18 years old and have work experience of at least 5 years
                """
        val schema =
                """
                    {
                        age: 0,
                        "professional experience" : 1,
                        residence: 'hamburg'
                    }
                """
        End2AstRunner.run(input, schema) { r ->
            r.rules()
                    .hasSizeOf(2)
                    .first()
                    .hasError("You must be at least 18 years old and come from Dortmund.")
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .leftVariable()
                    .hasName("underage")
                    .hasType(DataPropertyType.Boolean)
                    .parentCondition()
                    .rightOperand()
                    .parentCondition()
                    .parentConditionGroup()
                    .second()
                    .hasConnector(ASTConditionConnector.AND)
                    .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                    .leftProperty()
                    .hasPath("residence")
                    .parentCondition()
                    .rightString()
                    .hasValue("Dortmund")

                    r.rules()
                            .second()
                            .hasError("You must be at least 18 years old and have work experience of at least 5 years")
                            .conditionGroup()
                            .hasSize(2)
                            .first()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftVariable()
                            .hasName("underage")
                            .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                            .rightOperand()//staticOperands do not have any checks, like .IsStatic()
                            .parentCondition()
                            .parentConditionGroup()
                            .second()
                            .hasConnector(ASTConditionConnector.OR)
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                            .leftProperty()
                            .hasPath("professional experience")
                            .parentCondition()
                            .rightNumber()
                            .hasValue(5.0)

            r.variables()
                    .hasSizeOf(1)
                    .first()
                    .hasName("underage")
                    .condition()
                    .leftProperty()
                    .hasPath("age")
                    .parentCondition()
                    .rightNumber()
                    .hasValue(18.0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun age_less_18_or_exp_shorter_5_unquoted_string(){
        val input =
                """
                             the age of the applicant is LESS than 18 years old
                          AS underage

                          IF the applicant IS underage
                         AND his residence IS NOT Dortmund
                        THEN You must be at least 18 years old and come from Dortmund.

                          IF the applicant IS underage
                          OR his professional experience is LESS than 5 years
                        THEN You must be at least 18 years old and have work experience of at least 5 years
                """
        val schema =
                """
                    {
                        age: 0,
                        "professional experience" : 1,
                        residence: hamburg
                    }
                """
        End2AstRunner.run(input, schema) { r ->
            r.rules()
                    .hasSizeOf(2)
                    .first()
                    .hasError("You must be at least 18 years old and come from Dortmund.")
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .leftVariable()
                    .hasName("underage")
                    .hasType(DataPropertyType.Boolean)
                    .parentCondition()
                    .rightOperand()
                    .parentCondition()
                    .parentConditionGroup()
                    .second()
                    .hasConnector(ASTConditionConnector.AND)
                    .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                    .leftProperty()
                    .hasPath("residence")
                    .parentCondition()
                    .rightString()
                    .hasValue("Dortmund")

                    r.rules()
                            .second()
                            .hasError("You must be at least 18 years old and have work experience of at least 5 years")
                            .conditionGroup()
                            .hasSize(2)
                            .first()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftVariable()
                            .hasName("underage")
                            .hasType(DataPropertyType.Boolean)
                            .parentCondition()
                            .rightOperand()//staticOperands do not have any checks, like .IsStatic()
                            .parentCondition()
                            .parentConditionGroup()
                            .second()
                            .hasConnector(ASTConditionConnector.OR)
                            .hasOperator(ASTComparisonOperator.LESS_THAN)
                            .leftProperty()
                            .hasPath("professional experience")
                            .parentCondition()
                            .rightNumber()
                            .hasValue(5.0)

            r.variables()
                    .hasSizeOf(1)
                    .first()
                    .hasName("underage")
                    .condition()
                    .leftProperty()
                    .hasPath("age")
                    .parentCondition()
                    .rightNumber()
                    .hasValue(18.0)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
    "\n",
    "\n      ",
    "           \n",
    "    \n   "
    ])
    @Throws(Exception::class)
    fun variable_spacing_with_linefeeds_in_then_text(paramStr : String){
        val input =
                """
                             the age of the applicant is LESS than 18 years old
                          AS underage
                          
                          IF the applicant IS underage
                          OR his professional experience is LESS than 5 years
                        THEN You must be at least 18 years old and have work"""
                        .plus(paramStr)
                        .plus("""experience of at least 5 years
                """)
        val schema =
                """
                    {
                        age: 0,
                        "professional experience" : 1,
                        residence: "hamburg"
                    }
                """
        End2AstRunner.run(input, schema) { r ->
            r.rules()
                    .first()
                    .hasError("You must be at least 18 years old and have work" + paramStr.replace("\n", " ") + "experience of at least 5 years")
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .hasOperator(ASTComparisonOperator.EQUALS)
                    .leftVariable()
                    .hasName("underage")
                    .hasType(DataPropertyType.Boolean)
                    .parentCondition()
                    .rightOperand()//staticOperands do not have any checks, like .IsStatic()
                    .parentCondition()
                    .parentConditionGroup()
                    .second()
                    .hasConnector(ASTConditionConnector.OR)
                    .hasOperator(ASTComparisonOperator.LESS_THAN)
                    .leftProperty()
                    .hasPath("professional experience")
                    .parentCondition()
                    .rightNumber()
                    .hasValue(5.0)

            r.variables()
                    .hasSizeOf(1)
                    .first()
                    .hasName("underage")
                    .condition()
                    .leftProperty()
                    .hasPath("age")
                    .parentCondition()
                    .rightNumber()
                    .hasValue(18.0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun shopping_list_function() {
        val input =
                """
                SUM OF shopping_list.price
                    AS expenses
                    
                the expenses MUST NOT EXCEED the budget of 20 €.
                """
        val schema =
                """
                    {
                        shopping_list: [
                            {
                                article : "text",
                                price : 0
                            }
                        ]
                    }
                """

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(1)
                .first()
                    .hasName("expenses")
                    .function()
                        .hasName("SUM_OF")
                        .parameters()
                        .hasSizeOf(1)
                        .first()
                            .function()
                                .hasName("GET_ARRAY_OF")}

        End2AstRunner.run(input, schema) {
                r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                        .rightNumber()
                            .hasValue(20.0)
                    .parentCondition()
                        .leftVariable()
                            .hasName("expenses")
                            .hasType(DataPropertyType.Decimal) } //depending on lifecycle, type property in debugger might have different value}
    }





    @Test
    @Throws(Exception::class)
    fun power_of_engine_less_or_equals() {
        val input =
                """
                    IF the power of engine is LESS OR EQUALS 30 kW
                    THEN the power should be at least 31 kW WITH CODE 666
                """
        val schema = "{ power : 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError("the power should be at least 31 kW")
                        .hasErrorCode(666)
                        .condition()
                            .leftProperty()
                                .hasPath("power")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
                            .rightNumber()
                                .hasValue(30.0)
        }
    }
    
    @Test
    @Throws(Exception::class)
    fun complexity_test_used_variable() {
        val input =
                """
                    Age AS var1

                    var1 - 25 AS NotWorking
                """
        val schema = "{ Age : 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(2)
                .first()
                .hasName("var1")
                .operandProperty()
                .hasType(DataPropertyType.Decimal)
                .hasPath("Age")
                .parentModel()
                .variables()
                .second()
                .hasName("NotWorking")
                .arithmetic()
                .hasPreprocessedSource()
                .operation()
                .first()
                .variableValue()
                .hasName("var1")
                .hasType(DataPropertyType.Decimal)
                .parentOperation()
                .second()
                .hasOperator(ASTArithmeticalOperator.Subtraction)
                .staticNumber(25.0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun complexity_test_arithmetic_used_variable() {
        val input =
                """
                    Age
                     TIMES
                     25 AS var1

                    var1 - 25 AS NotWorking
                """
        val schema = "{ Age : 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(2)
                .first()
                .hasName("var1")
                .parentModel()
                .variables()
                .second()
                .hasName("NotWorking")
                .arithmetic()
                .hasPreprocessedSource()
                .operation()
                .first()
                .variableValue()
                .hasName("var1")
                .hasType(DataPropertyType.Decimal)
                .parentOperation()
                .second()
                .hasOperator(ASTArithmeticalOperator.Subtraction)
                .staticNumber(25.0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun complexity_test_arithmetic_used_variable_in_rule() {
        val input =
                """
                    Age
                     TIMES
                     25 AS var1

                    var1 - 25 AS NotWorking
                    
                    IF NotWorking AT LEAST 1
                    THEN do literally nothing
                """
        val schema = "{ Age : 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(2)
                .first()
                .hasName("var1")
                .parentModel()
                .variables()
                .second()
                .hasName("NotWorking")
                .arithmetic()
                .hasPreprocessedSource()
                .operation()
                .first()
                .variableValue()
                .hasName("var1")
                .hasType(DataPropertyType.Decimal)
                .parentOperation()
                .second()
                .hasOperator(ASTArithmeticalOperator.Subtraction)
                .staticNumber(25.0)
            
                r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("do literally nothing")
        }
    }

    @Test
    @Throws(Exception::class)
    fun complexity_test_2_layered_rule() {
        val input =
                """
                    Age
                     TIMES
                     25 AS var1

                    var1 - 25 AS NotWorking
                    
                    Whatever AS Whatever1
                    
                    IF NotWorking AT LEAST 1
                   AND Whatever1 EQUALS Whatever
                    THEN do literally nothing
                """
        val schema = "{ Age : 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(3)
                .first()
                .hasName("var1")
                .parentModel()
                .variables()
                .third()
                .hasString("Whatever")
                .parentList()
                .second()
                .hasName("NotWorking")
                .arithmetic()
                .hasPreprocessedSource()
                .operation()
                .first()
                .variableValue()
                .hasName("var1")
                .hasType(DataPropertyType.Decimal)
                .parentOperation()
                .second()
                .hasOperator(ASTArithmeticalOperator.Subtraction)
                .staticNumber(25.0)

            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .hasError("do literally nothing")
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .hasOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
                    .parentConditionGroup()
                    .second()
                    .hasOperator(ASTComparisonOperator.EQUALS)
        }
    }

    @Test
    @Throws(Exception::class)
    fun complexity_test_3_layered_rule() {
        val input =
                """
                    Age
                     TIMES
                     25 AS var1

                    var1 - 25 AS NotWorking
                    
                    Whatever AS Whatever1
                    
                    IF NotWorking AT LEAST 1
                   AND Whatever1 EQUALS Whatever
                        OR NotWorking LESS 125
                    THEN do literally nothing
                """
        val schema = "{ Age : 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(3)
                .first()
                .hasName("var1")
                .parentModel()
                .variables()
                .third()
                .hasString("Whatever")
                .parentList()
                .second()
                .hasName("NotWorking")
                .arithmetic()
                .hasPreprocessedSource()
                .operation()
                .first()
                .variableValue()
                .hasName("var1")
                .hasType(DataPropertyType.Decimal)
                .parentOperation()
                .second()
                .hasOperator(ASTArithmeticalOperator.Subtraction)
                .staticNumber(25.0)

            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .hasError("do literally nothing")
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .hasOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
                    .parentConditionGroup()
                    .hasSize(2)
                    .firstConditionGroup()
                    .hasSize(2)
                    .first()
                    .hasOperator(ASTComparisonOperator.EQUALS)
                    .leftVariable()
                    .hasName("Whatever1")
                    .parentCondition()
                    .rightString()
                    .hasValue("Whatever")
                    .parentCondition()
                    .parentConditionGroup()
                    .second()
                    .hasConnector(ASTConditionConnector.OR)
                    .rightNumber()
                    .hasValue(125.0)
                    .parentCondition()
                    .leftVariable()
                    .hasName("NotWorking")
            
        }
    }

    @Test
    @Throws(Exception::class)
    fun complexity_test_nested_variables() {
        val input =
                """
                    Age
                     TIMES
                     25 AS var1
                     
                     var1 + 1 AS var2
                     
                     var2 + 1 AS var3
                     
                     var3 + 1 AS var4

                    var4 - 3
                    MUST Age / 25
                """
        val schema = "{ Age : 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(4)
                .atIndex(0)
                .hasName("var1")
                .parentList()
                .atIndex(1)
                .hasName("var2")
                .parentList()
                .atIndex(2)
                .hasName("var3")
                .parentList()
                .atIndex(3)
                .hasName("var4")
                .arithmetic()
                .hasPreprocessedSource()
                .operation()
                .hasSizeOf(2)
                .first()
                .variableValue()
                .hasName("var3")
                .parentOperation()
                .second()
                .staticNumber(1.0)
            //framework currently limits how deep we can go into recursively checking variable chains
            // - it is properly resolved in the ASTModel
            
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .condition()
                    .rightArithmetical()
                    .hasPreprocessedSource()
                    .operation()
                    .hasSizeOf(2)
                    .first()
                    .propertyValue()
                    .hasPath("Age")
                    .parentOperation()
                    .second()
                    .staticNumber(25.0)
            
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .condition()
                    .leftArithmetical()
                    .hasPreprocessedSource()
                    .operation()
                    .hasSizeOf(2)
                    .first()
                    .variableValue()
                    .hasName("var4")
                    .hasType(DataPropertyType.Decimal)
        }
    }

    @Test
    @Disabled
    @Throws(Exception::class)
    fun preprocessor_testcase_testcase() {
        val input =
                """
                     A TESTCASE AS var1

                    var1
                    MUST TESTCASE
                """
        val schema = "{ }"

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                .hasSizeOf(1)
                .first()
                .string()
                .hasValue("TESTCASE_20_TESTCASE") 
            //documentation needed - magic replacement token - parser cannot properly read "TESTCASEʬA_20_TESTCASE"
                
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .condition()
                    .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                    .leftVariable()
                    .hasName("var1")
                    .parentCondition()
                    .rightString()
                    .hasValue("TESTCASE")
        }
    }

    @Test
    @Throws(Exception::class)
    fun must_not_equal_test() {
        val input =
                """
                     age MUSTN'T EQUAL 700 / 2
                """
        val schema = "{ age: 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                .hasError("age MUSTN'T EQUAL 700 / 2")
        }
    }

    @Test
    @Throws(Exception::class)
    fun must_with_keywords_error_test() {
        val input =
                """
                     a MUST b AND c GREATER THAN d
                """
        val schema = "{ a: 0, b: 0, c: 0, d: 0 }"

        End2AstRunner.run(input, schema) {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .hasError("a MUST b AND c GREATER THAN d")
        }
    }
}