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
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class RuleConstrainedTest {

    @Test
    @Throws(Exception::class)
    fun create_constrained_rule_with_implicit_condition() {
        val input = """
                 the age GREATER than 10 years
             AS  Senior

             IF  applicant is NOT Senior
           THEN  you are not senior.
        """

        End2AstRunner.run(input, "{age:0}") {
                    r -> r.variables()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("Senior")
                .parentModel()
                    .rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasError("you are not senior.")
                            .condition()
                                .leftBoolean()
                                    .isTrue()
                            .parentCondition()
                                .rightVariable()
                                    .hasPreprocessedSource()
                                    .hasName("Senior")
                                    .hasType(DataPropertyType.Boolean)
        }
    }


    //    -------------------------------------------
    //    TODO: TB 16:04.19: MUST BE funktioniert in der Konstellation nicht (Boolean mit MUST)
    @Disabled
    @ParameterizedTest
    @ValueSource(strings = [
        "applicant MUST be Student"
        ,"the applicant MUST be Student"
    ])
    fun constraint_must_variable_boolean(paramStr: String) {
        val input = "$paramStr"

        End2AstRunner.run(input, "{Student: true}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Student")
//                                    .hasType(DataPropertyType.Boolean)
                        .parentCondition()
                            .rightBoolean()
                                .isTrue
                                .hasPreprocessedSource()
        }
    }

//    --------------------------------------------------------------

//    TODO: TB 18.04.19: letzter Test mit 'new employee' am Ende funktioniert nicht
    @ParameterizedTest
    @ValueSource(strings = [
         "Status MUST be new"
        ,"Some Status of someone SHOULD be new"
        ,"Status HAS to be new"
        ,"Status HAVE to be new"
        ,"the Status of a person MUST be a new"
//        ,"A Status SHOULD new employee"
        ,"The Status HAS to be the new"
//        ,"The Status HAVE to be a new employee"
        ,"Status MUST EQUALS new"
    ])
    fun constraint_must_variable_string(paramStr: String) {
        val input = """
            new AS Status

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.String)
                                .hasName("Status")
                        .parentCondition()
                            .rightString()
                                .hasPreprocessedSource()
                                .hasValue("new")
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Status")
                        .hasString("new")
        }
    }

//    TODO: TB, 26.04.19: Sugar nach dem letzten "active" geht nicht
    @ParameterizedTest
    @ValueSource(strings = [
        "Status MUST NOT be active"
        ,"Status SHOULD NOT be active"
        ,"Status HAS NOT to be active"
        ,"Status HAVE NOT to be active"
        ,"the Status MUST NOT active"
        ,"A Status SHOULD NOT active"
        ,"The Status HAS NOT to be the active"
        ,"The Status HAVE NOT to be a active"
        ,"Status MUSTN'T be active"
        ,"Status SHOULDN'T be active"
        ,"Status HASN'T to be active"
        ,"Status HAVEN'T to be active"
        ,"the Status MUSTN'T active"
        ,"A Status of someone SHOULDN'T active"
        ,"The person Status HASN'T to be the active"
        ,"The Status HAVEN'T to be a active"
//        ,"The Status HAVEN'T to be a active status" // geht nicht (26.04.19)
    ])
    fun constraint_mustnot_variable_string(paramStr: String) {
        val input = """
            active AS Status

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
//                        .hasError(paramStr)
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftVariable()
                                    .hasPreprocessedSource()
                                    .hasType(DataPropertyType.String)
                                    .hasName("Status")
                        .parentCondition()
                            .rightString()
                                .hasValue("active")
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Status")
                        .hasString("active")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Employees MUST be 0"
        ,"Employees SHOULD be 0"
        ,"Employees HAS to be 0"
        ,"Employees HAVE to be the number 0"
        ,"the Employees MUST 0 stuff"
        ,"All Employees of the company SHOULD a 0"
        ,"The Employees HAS to be the 0"
        ,"The Employees HAVE to be a 0 in number"
        ,"The current Employees MUST EQUALS 0"
    ])
    fun constraint_must_variable_number(paramStr: String) {
        val input = """
            2 AS Employees

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                            .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(2.0)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Employees MUST NOT be 0"
        ,"Employees SHOULD NOT be 0"
        ,"Employees HAS NOT to be 0"
        ,"Employees HAVE NOT to be the number 0"
        ,"the Employees MUST NOT 0 stuff"
        ,"All Employees SHOULD NOT a 0"
        ,"The Employees HAS NOT to be the 0"
        ,"The Employees HAVE NOT to be a 0 in number"
        ,"Employees MUSTN'T be 0"
        ,"The Employees of the company SHOULDN'T be 0"
        ,"All Employees we have HASN'T to be 0"
        ,"Employees HAVEN'T to be the number 0"
        ,"the Employees stuff people MUSTN'T 0 stuff"
        ,"All Employees SHOULDN'T a 0 thing"
        ,"The Employees HASN'T to be the 0"
        ,"The Employees HAVEN'T to be a 0 in number"
    ])
    fun constraint_mustnot_variable_number(paramStr: String) {
        val input = """
            0 AS Employees

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
              .parentModel()
                    .variables()
                        .hasSizeOf(1)
                        .first()
                            .hasName("Employees")
                            .hasNumber(0.0)
        }
    }



    @ParameterizedTest
    @CsvSource(
        "Employees MUST GREATER 0, LESS_OR_EQUALS"
        ,"Employees SHOULD be GREATER THAN 0, LESS_OR_EQUALS"
        ,"Employees HAS to be LESS 0, GREATER_OR_EQUALS"
        ,"Employees HAVE to be LESS OR EQUAL the number 0, GREATER_THAN"
        ,"the Employees MUST GREATER OR EQUALS 0 stuff, LESS_THAN"
        ,"All Employees SHOULD be SMALLER THAN 0, GREATER_OR_EQUALS"
        ,"The Employees HAS to be BIGGER THAN the 0, LESS_OR_EQUALS"
        ,"The Employees HAVE to be LARGER 0 in number, LESS_OR_EQUALS"
        ,"Employees MUST be LESS OR EQUAL 0, GREATER_THAN"
        ,"Employees SHOULD be MORE THAN 0, LESS_OR_EQUALS"
        ,"Employees HAS to be AT MOST 0, GREATER_THAN"
        ,"Employees HAVE to be AT LEAST the number 0, LESS_THAN"
        ,"the Employees MUST be FEWER THAN 0 people, GREATER_OR_EQUALS"
        ,"All Employees SHOULD FEWER THAN 0, GREATER_OR_EQUALS"
        ,"The Employees HAS to be BIGGER THAN 0, LESS_OR_EQUALS"
        ,"The Employees HAVE to SMALLER THAN a 0 in number, GREATER_OR_EQUALS"
    )
    fun constraint_must_variable_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            2 AS Employees

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("Employees")
                            .hasNumber(2.0)
                }
    }

    @ParameterizedTest
    @CsvSource(
         "Employees MUST NOT be FEWER THAN 0, LESS_THAN"
        ,"Employees SHOULD NOT be LESS THAN 0, LESS_THAN"
        ,"Employees HAS NOT to be SMALLER THAN 0, LESS_THAN"
        ,"Employees HAVE NOT to be LARGER than the number 0, GREATER_THAN"
        ,"the Employees MUST NOT EXCEED 0 people, GREATER_THAN"
        ,"All Employees SHOULD NOT LESS OR EQUAL a 0, LESS_OR_EQUALS"
        ,"The Employees HAS NOT to be AT MOST the 0, LESS_OR_EQUALS"
        ,"The Employees HAVE NOT to be a GREATER OR EQUALS 0 in number, GREATER_OR_EQUALS"
        ,"Employees MUSTN'T be AT LEAST 0 for our company, GREATER_OR_EQUALS"
        ,"Employees SHOULDN'T be GREATER THAN 0, GREATER_THAN"
        ,"The Employees of our company HASN'T to be MORE THAN 0, GREATER_THAN"
        ,"Employees HAVEN'T to be SMALLER THAN the number 0, LESS_THAN"
        ,"the Employees people MUSTN'T be MORE THAN 0 people, GREATER_THAN"
    )
    fun constraint_mustnot_variable_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            0 AS Employees

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(0.0)
        }
    }



    //    TODO: TB 18.04.19: letzter Test mit 'new stuff' am Ende funktioniert nicht
    @ParameterizedTest
    @ValueSource(strings = [
        "Status MUST be new"
        ,"Status SHOULD be new"
        ,"Status HAS to be new"
        ,"Status HAVE to be new"
        ,"the Status of the employee MUST be new"
        ,"The Status SHOULD be the new"
//        ,"the Status HAS to be new stuff"
//        ,"The Status HAVE to be a new stuff"
    ])
    fun constraint_must_property_string(paramStr: String) {
        val input = "$paramStr"


        End2AstRunner.run(input, "{Status: 'new'}") {
            r -> r.rules()
                    .hasSizeOf(1)
                        .first()
                            .condition()
                                .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Status")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .rightString()
                                    .hasValue("new")
                                    .hasPreprocessedSource()
        }
    }

    //    TODO: TB 18.04.19: alle Tests, bei denen nach dem Variablen-Inhalt "new" etwas nachfolgt ("stuff", "thing", etc.) funktioniert nicht
    @ParameterizedTest
    @ValueSource(strings = [
        "Status MUST NOT be new"
        ,"Status MUSTN'T be new"
        ,"Status SHOULD NOT be new"
        ,"Status SHOULDN'T be new"
        ,"Status HAS NOT to be new"
        ,"Status HASN'T to be new"
        ,"Status HAVE NOT to be new"
        ,"Status HAVEN'T to be new"
        ,"the Status MUST NOT be new"
        ,"A Status MUSTN'T be a new"
//        ,"The Status SHOULD NOT be new stuff"
//        ,"My Status thing SHOULDN'T be a new thing"
//        ,"Status HAS NOT to be new stuff"
//        ,"Status HASN'T to be the new thing"
//        ,"Some Status thing HAVE NOT to be a new thing"
//        ,"My Status stuff HAVEN'T to be the new stuff"
    ])
    fun constraint_mustnot_property_string(paramStr: String) {
        val input = "$paramStr"


        End2AstRunner.run(input, "{Status: 'new'}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Status")
                                .hasType(DataPropertyType.String)
                        .parentCondition()
                            .rightString()
                                .hasValue("new")
                                .hasPreprocessedSource()
        }
    }


//  TODO: TB 18.04.19: '0 years' am Ende funktioniert nicht
    @ParameterizedTest
    @ValueSource(strings = [
        "Years MUST be 0"
        ,"Years SHOULD be 0"
        ,"Years HAS to be 0"
        ,"Years HAVE to be 0"
        ,"the Years MUST be 0"
        ,"The Years SHOULD be the 0"
        ,"the Years HAS to be 0 stuff"
        ,"The Years HAVE to be a 0 stuff"
        ,"the Years MUST be 0"
        ,"The Years SHOULD be exactly 0"
//        ,"All Years HAS to be 0 years" //tries to assign a property to sugar 'years'
        ,"The Years thing HAVE to be 0 thing"
        ,"the Years in our company MUST be 0"
        ,"The Years SHOULD be the 0"
        ,"the Years HAS to be 0 stuff"
        ,"The Years HAVE to be a 0 stuff"
    ])
    fun constraint_must_property_number(paramStr: String) {
        val input = "$paramStr"


        End2AstRunner.run(input, "{Years: 0}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Years")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
        }
    }

//    TODO: TB  26.04.19: "0 years" führt zu Fehler. "years" kleingeschrieben wird nicht als Sugar erkannt.
    @ParameterizedTest
    @ValueSource(strings = [
        "Years MUST NOT be 0"
        ,"Years SHOULD NOT be 0"
        ,"Years HAS NOT to be 0"
        ,"Years HAVE NOT to be 0"
        ,"Years MUSTN'T be 0"
        ,"Years SHOULDN'T be 0"
        ,"Years HASN'T to be 0"
        ,"Years HAVEN'T to be 0"
//        ,"the Years MUST NOT be 0 years"
        ,"The Years in our company SHOULD NOT be a 0"
        ,"A Years HAS NOT a 0"
        ,"The Years HAVE NOT to be 0 things"
        ,"Some Years MUSTN'T be 0 stuff"
        ,"The Years SHOULDN'T be a 0"
        ,"My Years HASN'T be a 0"
        ,"Years HAVEN'T to be a 0"
    ])
    fun constraint_mustnot_property_number(paramStr: String) {
        val input = "$paramStr"


        End2AstRunner.run(input, "{Years: 0}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .condition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Years")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
        }
    }


    //    TODO: TB  26.04.19: "0 years" führt zu Fehler. "years" kleingeschrieben wird nicht als Sugar erkannt.
    @ParameterizedTest
    @ValueSource(strings = [
        "Years MUST NOT EQUALS 0"
        ,"Years SHOULD NOT EQUALS 0"
        ,"Years HAS NOT to EQUALS 0"
        ,"Years HAVE NOT to EQUALS 0"
        ,"Years MUSTN'T EQUALS 0"
        ,"Years SHOULDN'T EQUALS 0"
        ,"Years HASN'T to EQUALS 0"
        ,"Years HAVEN'T to EQUALS 0"
        ,"the Years MUST NOT EQUALS 0"
//        ,"the Years MUST NOT EQUALS 0 years"
        ,"The Years SHOULD NOT EQUALS  a 0"
        ,"A Years HAS NOT EQUALS a 0"
        ,"The Years HAVE NOT to EQUALS 0 things"
        ,"Some Years MUSTN'T EQUALS 0 stuff"
        ,"The Years SHOULDN'T EQUALS a 0"
        ,"My Years HASN'T EQUALS a 0"
        ,"Years HAVEN'T to EQUALS a 0"
    ])
    fun constraint_mustnot_equals_property_number(paramStr: String) {
        val input = "$paramStr"


        End2AstRunner.run(input, "{Years: 0}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Years")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
        }
    }


    //  TODO: TB 18.04.19: '0 years' am Ende funktioniert nicht. "years" kleingeschrieben wird nicht als Sugar erkannt
    @ParameterizedTest
    @CsvSource(
        "Years MUST be GREATER THAN 0, LESS_OR_EQUALS"
        ,"Years SHOULD be BIGGER 0, LESS_OR_EQUALS"
        ,"Years HAS to be LARGER THAN 0, LESS_OR_EQUALS"
        ,"Years HAVE to be MORE 0, LESS_OR_EQUALS"
        ,"the Years MUST be LESS 0, GREATER_OR_EQUALS"
        ,"The Years SHOULD be SMALLER the 0, GREATER_OR_EQUALS"
        ,"the Years HAS to be LOWER 0 stuff, GREATER_OR_EQUALS"
        ,"The Years HAVE to be FEWER a 0 stuff, GREATER_OR_EQUALS"
        ,"the Years MUST be LESS OR EQUAL 0, GREATER_THAN"
        ,"The Years SHOULD be AT MOST 0, GREATER_THAN"
//        ,"All Years HAS to be LESS OR EQUALS 0 years" //tries to assign a property to sugar 'years'
        ,"The Years thing HAVE to be GREATER OR EQUALS 0 thing, LESS_THAN"
        ,"the Years MUST be AT LEAST 0, LESS_THAN"
    )
    fun constraint_must_property_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = "$paramStr"


        End2AstRunner.run(input, "{Years: 0}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Years")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @CsvSource(
        "Years MUST NOT be GREATER THAN 0, GREATER_THAN"
        ,"Years SHOULD NOT be BIGGER 0, GREATER_THAN"
        ,"Years HAS NOT to be LARGER 0, GREATER_THAN"
        ,"Years HAVE NOT to be MORE THAN 0, GREATER_THAN"
        ,"Years MUSTN'T be LESS 0, LESS_THAN"
        ,"Years SHOULDN'T be SMALLER THAN exactly 0, LESS_THAN"
        ,"Years in our company HASN'T to be LOWER 0 completely, LESS_THAN"
        ,"Years HAVEN'T to be FEWER THAN 0, LESS_THAN"
        ,"the Years MUST NOT be LESS OR EQUALS 0, LESS_OR_EQUALS"
        ,"The Years SHOULD NOT be AT LEAST a 0, GREATER_OR_EQUALS"
        ,"A Years HAS NOT GREATER OR EQUALS a 0, GREATER_OR_EQUALS"
        ,"The Years HAVE NOT to be AT LEAST 0 things, GREATER_OR_EQUALS"
    )
    fun constraint_mustnot_property_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = "$paramStr"


        End2AstRunner.run(input, "{Years: 0}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Years")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Status MUST be var1"
        ,"Status SHOULD be var1"
        ,"Status HAS to be var1"
        ,"Status HAVE to be var1"
        ,"the Status of the employee MUST var1 thing"
        ,"A Status SHOULD be surely var1"
        ,"The Status HAS to be the var1 stuff"
        ,"The Status HAVE to be a var1"
    ])
    fun constraint_must_property_variable_string(paramStr: String) {
        val input = """
            notnew AS var1

            $paramStr
            """


        End2AstRunner.run(input, "{Status: 'new'}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Status")
                                .hasType(DataPropertyType.String)
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasName("var1")
                                .hasType(DataPropertyType.String)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .hasString("notnew")
             }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Status MUST NOT be var1"
        ,"Status SHOULD NOT be var1"
        ,"Status HAS NOT to be var1"
        ,"Status HAVE NOT to be var1"
        ,"Status SHOULDN'T be var1"
        ,"Status HASN'T to be var1"
        ,"Status HAVEN'T to be var1"
        ,"the Status MUST NOT be var1"
        ,"the Status of the employee SHOULD NOT be the var1"
        ,"A Status in our files HAS NOT to be a var1"
        ,"The Status HAVE NOT to be a var1 thing"
        ,"A Status SHOULDN'T be the var1 thing"
        ,"Some Status HASN'T to be a var1"
        ,"Status HAVEN'T to be var1 thing"
    ])
    fun constraint_mustnot_property_variable_string(paramStr: String) {
        val input = """
            notnew AS var1

            $paramStr
            """


        End2AstRunner.run(input, "{Status: new}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Status")
                                .hasType(DataPropertyType.String)
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasName("var1")
                                .hasType(DataPropertyType.String)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                     .first()
                        .hasName("var1")
                        .hasString("notnew")
                        .hasPreprocessedSource()
        }
    }



    @ParameterizedTest
    @ValueSource(strings = [
        "var1 MUST be Status"
        ,"var1 SHOULD be Status"
        ,"var1 HAS to be Status"
        ,"var1 HAVE to be Status"
        ,"the var1 MUST Status thing"
        ,"A var1 in the files SHOULD Status"
        ,"The var1 HAS to be the Status stuff"
        ,"The var1 HAVE to be a Status we know"
    ])
    fun constraint_must_variable_property_string(paramStr: String) {
        val input = """
            notnew AS var1

            $paramStr
            """


        End2AstRunner.run(input, "{Status: 'new'}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .hasPreprocessedSource()
                    .condition()
                        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                        .leftVariable()
                            .hasPreprocessedSource()
                            .hasName("var1")
                            .hasType(DataPropertyType.String)
                    .parentCondition()
                        .rightProperty()
                            .hasPreprocessedSource()
                            .hasPath("Status")
                            .hasType(DataPropertyType.String)
        .parentModel()
            .variables()
                .hasSizeOf(1)
                .first()
                    .hasPreprocessedSource()
                    .hasName("var1")
                    .hasString("notnew")
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "var1 MUST NOT be Status"
        ,"var1 SHOULD NOT be Status"
        ,"var1 HAS NOT to be Status"
        ,"var1 HAVE NOT to be Status"
        ,"var1 SHOULDN'T be Status"
        ,"var1 HASN'T to be Status"
        ,"var1 HAVEN'T to be Status"
        ,"the var1 MUST NOT be Status"
        ,"the var1 in our files SHOULD NOT be the Status"
        ,"A var1 HAS NOT to be a Status"
        ,"The var1 HAVE NOT to be a Status thing"
        ,"A var1 in our files SHOULDN'T be the Status we don't know"
        ,"Some var1 HASN'T to be a Status"
        ,"var1 HAVEN'T to be Status thing"
    ])
    fun constraint_mustnot_variable_property_string(paramStr: String) {
        val input = """
            notnew AS var1

            $paramStr
            """


        End2AstRunner.run(input, "{Status: new}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasName("var1")
                                .hasType(DataPropertyType.String)
                        .parentCondition()
                            .rightProperty()
                                .hasPreprocessedSource()
                                .hasPath("Status")
                                .hasType(DataPropertyType.String)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("var1")
                        .hasString("notnew")
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Minimum MUST be numberVar"
        ,"Minimum SHOULD be numberVar"
        ,"Minimum HAS to be numberVar"
        ,"Minimum HAVE to be numberVar"
        ,"the Minimum MUST numberVar thing"
        ,"A Minimum of our people SHOULD be numberVar in numbers"
        ,"The Minimum HAS to be the numberVar stuff"
        ,"The Minimum HAVE to be a numberVar"
    ])
    fun constraint_must_property_variable_number(paramStr: String) {
        val input = """
            5 AS numberVar

            $paramStr
            """


        End2AstRunner.run(input, "{Minimum: 20}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Minimum")
                                .hasType(DataPropertyType.Decimal)
                            .parentCondition()
                        .rightVariable()
                            .hasPreprocessedSource()
                            .hasName("numberVar")
                            .hasType(DataPropertyType.Decimal)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasName("numberVar")
                        .hasNumber(5.0)
                        .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "Minimum MUST NOT be numberVar"
        ,"Minimum SHOULD NOT be numberVar"
        ,"Minimum HAS NOT to be numberVar"
        ,"Minimum HAVE NOT to be numberVar"
        ,"Minimum SHOULDN'T be numberVar"
        ,"Minimum HASN'T to be numberVar"
        ,"Minimum HAVEN'T to be numberVar"
        ,"the Minimum MUST NOT be numberVar"
        ,"the Minimum SHOULD NOT be the numberVar"
        ,"A Minimum HAS NOT to be a numberVar"
        ,"The Minimum HAVE NOT to be a numberVar thing"
        ,"A Minimum SHOULDN'T be the numberVar thing"
        ,"Some Minimum of people HASN'T to be a numberVar people"
        ,"Minimum HAVEN'T to be numberVar thing"
    ])
    fun constraint_mustnot_property_variable_number(paramStr: String) {
        val input = """
            5 AS numberVar

            $paramStr
            """


        End2AstRunner.run(input, "{Minimum: 5}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Minimum")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightVariable()
                                .hasName("numberVar")
                                .hasType(DataPropertyType.Decimal)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("numberVar")
                        .hasNumber(5.0)
        }
    }


    @ParameterizedTest
    @CsvSource(
        "Minimum MUST be GREATER THAN numberVar, LESS_OR_EQUALS"
        ,"Minimum SHOULD be BIGGER numberVar, LESS_OR_EQUALS"
        ,"Minimum HAS to be MORE THAN numberVar, LESS_OR_EQUALS"
        ,"Minimum HAVE to be LARGER numberVar, LESS_OR_EQUALS"
        ,"the Minimum MUST LESS THAN numberVar thing, GREATER_OR_EQUALS"
        ,"A Minimum SHOULD be FEWER THAN numberVar, GREATER_OR_EQUALS"
        ,"The Minimum HAS to be SMALLER the numberVar stuff, GREATER_OR_EQUALS"
        ,"The Minimum HAVE to be LOWER THAN a numberVar, GREATER_OR_EQUALS"
        ,"Minimum MUST be GREATER OR EQUALS THAN numberVar, LESS_THAN"
        ,"Minimum SHOULD be AT LEAST numberVar, LESS_THAN"
        ,"Minimum HAS to be LESS OR EQUALS numberVar, GREATER_THAN"
        ,"Minimum HAVE to be AT MOST numberVar, GREATER_THAN"
        ,"the Minimum MUST LESS OR EQUAL the numberVar thing, GREATER_THAN"
        ,"A Minimum SHOULD be GREATER OR EQUAL numberVar, LESS_THAN"
        ,"The Minimum of people HAS to be AT MOST the numberVar amount of people, GREATER_THAN"
        ,"The Minimum in our company HAVE to be AT LEAST a numberVar in numbers, LESS_THAN"
    )
    fun constraint_must_property_variable_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            5 AS numberVar

            $paramStr
            """


        End2AstRunner.run(input, "{Minimum: 20}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .hasPreprocessedSource()
                    .hasError(paramStr)
                    .condition()
                        .hasOperator(expectedOperator)
                        .leftProperty()
                            .hasPreprocessedSource()
                            .hasPath("Minimum")
                            .hasType(DataPropertyType.Decimal)
                    .parentCondition()
                        .rightVariable()
                            .hasPreprocessedSource()
                            .hasName("numberVar")
                            .hasType(DataPropertyType.Decimal)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("numberVar")
                        .hasNumber(5.0)
        }
    }


    @ParameterizedTest
    @CsvSource(
        "Minimum MUST NOT be GREATER THAN numberVar, GREATER_THAN"
        ,"Minimum SHOULD NOT be BIGGER numberVar, GREATER_THAN"
        ,"Minimum HAS NOT to be LARGER THAN numberVar, GREATER_THAN"
        ,"Minimum HAVE NOT to be MORE numberVar, GREATER_THAN"
        ,"Minimum SHOULDN'T be LESS THAN numberVar, LESS_THAN"
        ,"Minimum HASN'T to be SMALLER numberVar, LESS_THAN"
        ,"Minimum HAVEN'T to be FEWER THAN numberVar, LESS_THAN"
        ,"the Minimum MUST NOT be LOWER THAN numberVar, LESS_THAN"
        ,"the Minimum SHOULD NOT be GREATER OR EQUAL the numberVar, GREATER_OR_EQUALS"
        ,"A Minimum HAS NOT to be AT LEAST a numberVar, GREATER_OR_EQUALS"
        ,"The Minimum of people HAVE NOT to be LESS OR EQUAL the numberVar amount in our files, LESS_OR_EQUALS"
        ,"A Minimum we found SHOULDN'T be the AT MOST numberVar thingy we wrote down, LESS_OR_EQUALS"
        ,"Some Minimum HASN'T to be a GREATER OR EQUAL numberVar in our files, GREATER_OR_EQUALS"
        ,"Minimum of people HAVEN'T to be LESS OR EQUALS our numberVar thing, LESS_OR_EQUALS"
    )
    fun constraint_mustnot_property_variable_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            5 AS numberVar

            $paramStr
            """


        End2AstRunner.run(input, "{Minimum: 5}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("Minimum")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasName("numberVar")
                                .hasType(DataPropertyType.Decimal)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                        .first()
                            .hasName("numberVar")
                            .hasNumber(5.0)
                            .hasPreprocessedSource()
        }
    }

    @ParameterizedTest
    @CsvSource(
            "AllValues.Minimum MUST be GREATER THAN numberVar, LESS_OR_EQUALS"
            ,"AllValues.Minimum SHOULD be BIGGER numberVar, LESS_OR_EQUALS"
            ,"AllValues.Minimum HAS to be MORE THAN numberVar, LESS_OR_EQUALS"
            ,"AllValues.Minimum HAVE to be LARGER numberVar, LESS_OR_EQUALS"
            ,"the AllValues.Minimum MUST LESS THAN numberVar thing, GREATER_OR_EQUALS"
            ,"A AllValues.Minimum SHOULD be FEWER THAN numberVar, GREATER_OR_EQUALS"
            ,"The AllValues.Minimum HAS to be SMALLER the numberVar stuff, GREATER_OR_EQUALS"
            ,"The AllValues.Minimum HAVE to be LOWER THAN a numberVar, GREATER_OR_EQUALS"
            ,"AllValues.Minimum MUST be GREATER OR EQUALS THAN numberVar, LESS_THAN"
            ,"AllValues.Minimum SHOULD be AT LEAST numberVar, LESS_THAN"
            ,"AllValues.Minimum HAS to be LESS OR EQUALS numberVar, GREATER_THAN"
            ,"AllValues.Minimum HAVE to be AT MOST numberVar, GREATER_THAN"
            ,"the AllValues.Minimum MUST LESS OR EQUAL the numberVar thing, GREATER_THAN"
            ,"A AllValues.Minimum in our files SHOULD be GREATER OR EQUAL the numberVar in another file, LESS_THAN"
            ,"The AllValues.Minimum we have HAS to be AT MOST the numberVar stuff, GREATER_THAN"
            ,"The AllValues.Minimum number we have HAVE to be AT LEAST a numberVar value in our files, LESS_THAN"
    )
    fun constraint_must_nested_property_variable_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            5 AS numberVar

            $paramStr
            """


        End2AstRunner.run(input, "{AllValues: {Minimum: 20}}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("AllValues.Minimum")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasName("numberVar")
                                .hasType(DataPropertyType.Decimal)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("numberVar")
                        .hasNumber(5.0)
        }
    }

    @ParameterizedTest
    @CsvSource(
            "AllValues.Minimum MUST NOT be GREATER THAN numberVar, GREATER_THAN"
            ,"AllValues.Minimum SHOULD NOT be BIGGER numberVar, GREATER_THAN"
            ,"AllValues.Minimum HAS NOT to be LARGER THAN numberVar, GREATER_THAN"
            ,"AllValues.Minimum HAVE NOT to be MORE numberVar, GREATER_THAN"
            ,"AllValues.Minimum MUST NOT EXCEED numberVar, GREATER_THAN"
            ,"AllValues.Minimum SHOULDN'T be LESS THAN numberVar, LESS_THAN"
            ,"AllValues.Minimum HASN'T to be SMALLER numberVar, LESS_THAN"
            ,"AllValues.Minimum HAVEN'T to be FEWER THAN numberVar, LESS_THAN"
            ,"the AllValues.Minimum MUST NOT be LOWER THAN numberVar, LESS_THAN"
            ,"the AllValues.Minimum SHOULD NOT be GREATER OR EQUAL the numberVar, GREATER_OR_EQUALS"
            ,"A AllValues.Minimum HAS NOT to be AT LEAST a numberVar, GREATER_OR_EQUALS"
            ,"The AllValues.Minimum HAVE NOT to be LESS OR EQUAL a numberVar thing, LESS_OR_EQUALS"
            ,"A AllValues.Minimum in our files SHOULDN'T be the AT MOST the numberVar number we have, LESS_OR_EQUALS"
            ,"Some AllValues.Minimum in the report HASN'T to be a GREATER OR EQUAL numberVar from former reports, GREATER_OR_EQUALS"
            ,"AllValues.Minimum number HAVEN'T to be LESS OR EQUALS the numberVar data in our files, LESS_OR_EQUALS"
    )
    fun constraint_mustnot_nested_property_variable_number_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            5 AS numberVar

            $paramStr
            """


        End2AstRunner.run(input, "{AllValues: {Minimum: 5}}") {
            r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftProperty()
                                .hasPreprocessedSource()
                                .hasPath("AllValues.Minimum")
                                .hasType(DataPropertyType.Decimal)
                        .parentCondition()
                            .rightVariable()
                                .hasPreprocessedSource()
                                .hasName("numberVar")
                                .hasType(DataPropertyType.Decimal)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("numberVar")
                        .hasNumber(5.0)
        }
    }

//    TODO Frage TB 09.05.2019: Soll hasPreprocessedSource() auch bei leftArithmetical() und rightArithmetical() funktionieren? Tut es nicht.
    @ParameterizedTest
    @ValueSource(strings = [
        "SomeNumber - 10 MUST be 0"
        ,"SomeNumber MINUS 10 SHOULD be 0"
        ,"SomeNumber - 10 HAS to be 0"
        ,"SomeNumber MINUS 10 HAVE to be the number 0"
        ,"the SomeNumber - 10 MUST 0 stuff"
        ,"All SomeNumber MINUS 10 SHOULD a 0"
        ,"The SomeNumber - the number 10 HAS to be the 0 value"
        ,"The SomeNumber value in our files MINUS 10 HAVE to be a 0 in number"
    ])
    fun constraint_must_variable_number_arithmetic(paramStr: String) {
        val input = """
            15 AS SomeNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
//                        .hasError(paramStr) // Funktioniert nicht wegen Minuszeichen in "SomeNumber - 10")
                        .condition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .hasPreprocessedSource()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasOperator(null)
                                        .variableValue()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "SomeNumber - 10 MUST NOT be 0"
        ,"SomeNumber MINUS 10 SHOULD NOT be 0"
        ,"SomeNumber - 10 HAS NOT to be 0"
        ,"SomeNumber MINUS 10 HAVE NOT to be the number 0"
        ,"the SomeNumber - 10 MUST NOT 0 stuff"
        ,"All SomeNumber MINUS 10 SHOULD NOT a 0"
        ,"The SomeNumber - 10 HAS NOT to be the 0"
        ,"The SomeNumber MINUS 10 HAVE NOT to be a 0 in number"
        ,"SomeNumber - 10 MUSTN'T be 0"
        ,"SomeNumber MINUS 10 SHOULDN'T be 0"
        ,"SomeNumber - 10 HASN'T to be 0"
        ,"SomeNumber MINUS 10 HAVEN'T to be the number 0"
        ,"the SomeNumber - 10 people MUSTN'T 0 stuff"
        ,"All SomeNumber MINUS 10 SHOULDN'T a 0"
        ,"The SomeNumber we ave - value 10 HASN'T to be the number 0"
        ,"The SomeNumber value in our report MINUS 10 HAVEN'T to be a 0 value"
    ])
    fun constraint_mustnot_variable_number_arithmetic(paramStr: String) {
        val input = """
            15 AS SomeNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                        .hasError(paramStr)
                            .condition()
                                .rightNumber()
                                    .hasValue(0.0)
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .hasOperator(ASTComparisonOperator.EQUALS)
                                .leftArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("SomeNumber")
                                                .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                        .second()
                                            .hasOperator(ASTArithmeticalOperator.Subtraction)
                                                .staticNumber(10.0)
                                                .hasPreprocessedSource()
            }
    }


    @ParameterizedTest
    @CsvSource(
         "SomeNumber - 10 MUST be GREATER 0, LESS_OR_EQUALS"
        ,"SomeNumber MINUS 10 SHOULD BIGGER 0, LESS_OR_EQUALS"
        ,"SomeNumber - 10 HAS to be LARGER 0, LESS_OR_EQUALS"
        ,"SomeNumber MINUS 10 HAVE to be MORE the number 0, LESS_OR_EQUALS"
        ,"the SomeNumber - 10 MUST EXCEED 0 stuff, LESS_OR_EQUALS"
        ,"All SomeNumber MINUS 10 SHOULD LESS a 0, GREATER_OR_EQUALS"
        ,"The SomeNumber - 10 HAS to be SMALLER THAN the 0, GREATER_OR_EQUALS"
        ,"The SomeNumber MINUS 10 HAVE to be LOWER 0 in number, GREATER_OR_EQUALS"
        ,"SomeNumber - 10 MUST be FEWER THAN 0, GREATER_OR_EQUALS"
        ,"SomeNumber MINUS 10 SHOULD be GREATER OR EQUALS 0, LESS_THAN"
        ,"SomeNumber - 10 HAS to be AT LEAST 0, LESS_THAN"
        ,"SomeNumber MINUS 10 HAVE to be LESS OR EQUAL the number 0, GREATER_THAN"
        ,"the SomeNumber - 10 MUST AT MOST 0 stuff, GREATER_THAN"
        ,"SomeNumber - 10 MUST be EQUALS 0, NOT_EQUALS"
        ,"SomeNumber MINUS 10 SHOULD EQUALS 0, NOT_EQUALS"
        ,"The SomeNumber we ave in our files - the value 10 HAS to be EQUALS the value 0, NOT_EQUALS"
        ,"A SomeNumber from the report MINUS integer 10 HAVE to be EQUALS the number 0, NOT_EQUALS"
    )
    fun constraint_must_variable_number_arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            15 AS SomeNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
        }
    }


    @ParameterizedTest
    @CsvSource(
        "SomeNumber - 10 MUST NOT be GREATER THAN 0, GREATER_THAN"
        ,"SomeNumber MINUS 10 SHOULD NOT be BIGGER 0, GREATER_THAN"
        ,"SomeNumber - 10 HAS NOT to be LARGER THAN 0, GREATER_THAN"
        ,"SomeNumber MINUS 10 HAVE NOT to be MORE the number 0, GREATER_THAN"
        ,"the SomeNumber - 10 MUST NOT EXCEED 0 stuff, GREATER_THAN"
        ,"All SomeNumber MINUS 10 SHOULD NOT LESS 0, LESS_THAN"
        ,"The SomeNumber - 10 HAS NOT to be SMALLER the 0, LESS_THAN"
        ,"The SomeNumber MINUS 10 HAVE NOT to be LOWER a 0 in number, LESS_THAN"
        ,"SomeNumber - 10 MUSTN'T be FEWER 0, LESS_THAN"
        ,"SomeNumber MINUS 10 SHOULDN'T be GREATER OR EQUAL 0, GREATER_OR_EQUALS"
        ,"SomeNumber from the files - the integer 10 HASN'T to be AT LEAST the number 0 value, GREATER_OR_EQUALS"
        ,"SomeNumber MINUS 10 HAVEN'T to be LESS OR EQUALS the number 0, LESS_OR_EQUALS"
        ,"the SomeNumber - 10 people in our records MUSTN'T be AT MOST the number 0 we have, LESS_OR_EQUALS"
        ,"SomeNumber - 10 MUST NOT be EQUALS 0, EQUALS"
        ,"SomeNumber MINUS 10 SHOULD NOT be EQUALS 0, EQUALS"
        ,"SomeNumber - 10 HAS NOT to be EQUALS 0, EQUALS"
        ,"A SomeNumber we found MINUS a 10 from our number stash HAVE NOT to be EQUALS the number 0, EQUALS"
        ,"SomeNumber - 10 MUSTN'T be EQUALS 0, EQUALS"
        ,"SomeNumber MINUS 10 SHOULDN'T be EQUALS 0, EQUALS"
        ,"SomeNumber - 10 HASN'T to be EQUALS 0, EQUALS"
        ,"The SomeNumber from our reports MINUS 10 HAVEN'T to be EQUALS the number 0 we have, EQUALS"
    )
    fun constraint_mustnot_variable_number_arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            15 AS SomeNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .first()
                        .hasPreprocessedSource()
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .rightNumber()
                                .hasValue(0.0)
                                .hasPreprocessedSource()
                        .parentCondition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "SomeNumber - 10 MUST be 10 PLUS 5"
        ,"SomeNumber MINUS 10 SHOULD be 10 + 5"
        ,"SomeNumber - 10 HAS to be  10 PLUS 5"
        ,"SomeNumber MINUS 10 HAVE  the number 10 + 5"
        ,"A SomeNumber - the 10 MUST be 10 PLUS 5 in total"
        ,"The SomeNumber MINUS the 10 SHOULD be the sum 10 + 5"
        ,"The SomeNumber - the integer 10 from the integer bag HAS to be the sum 10 PLUS 5"
        ,"The SomeNumber value MINUS the 10 value HAVE to be the number 10 + 5"
    ])
    fun constraint_must_equals_variable_2number_2arithmetic(paramStr: String) {
        val input = """
            15 AS SomeNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
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
                                            .hasName("SomeNumber")
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
                r.rules()
                    .first()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(10.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .staticNumber(5.0)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "SomeNumber - 10 MUST NOT GREATER THAN 10 PLUS 5, GREATER_THAN"
        ,"SomeNumber MINUS 10 SHOULD NOT BIGGER 10 + 5, GREATER_THAN"
        ,"SomeNumber - 10 HAS NOT LARGER THAN 10 PLUS 5, GREATER_THAN"
        ,"SomeNumber MINUS 10 HAVE NOT MORE the number 10 + 5, GREATER_THAN"
        ,"the SomeNumber in our data - about 10 MUST NOT EXCEED the 10 number PLUS the 5 stuff, GREATER_THAN"
        ,"All SomeNumber MINUS 10 SHOULD NOT be LESS a 10 + 5, LESS_THAN"
        ,"The SomeNumber - 10 HAS NOT LOWER the 10 PLUS 5, LESS_THAN"
        ,"The SomeNumber value MINUS a nice clean 10 value HAVE NOT to SMALLER THAN another nice 10 number + the 5 in number, LESS_THAN"
        ,"A SomeNumber thing - 10 things MUSTN'T be FEWER THAN the 10 value PLUS imaginary 5, LESS_THAN"
        ,"SomeNumber MINUS 10 SHOULDN'T be GREATER OR EQUAL 10 + 5, GREATER_OR_EQUALS"
        ,"SomeNumber - 10 HASN'T to be AT LEAST 10 PLUS 5, GREATER_OR_EQUALS"
        ,"The SomeNumber of people we could find MINUS the made up 10 value HAVEN'T LESS OR EQUALS the number 10 + 5 sum, LESS_OR_EQUALS"
        ,"the SomeNumber value in our files - the integer value 10 people MUSTN'T be AT MOST the 10 value PLUS the 5 stuff, LESS_OR_EQUALS"
        ,"SomeNumber - 10 MUST NOT EQUALS 10 PLUS 5, EQUALS"
        ,"SomeNumber MINUS 10 SHOULD NOT EQUALS 10 + 5, EQUALS"
        ,"SomeNumber - 10 HAS NOT EQUALS 10 PLUS 5, EQUALS"
        ,"SomeNumber MINUS 10 HAVE NOT EQUALS the number 10 + 5, EQUALS"
        ,"the SomeNumber - 10 people MUSTN'T EQUALS 10 PLUS 5 stuff, EQUALS"
        ,"All SomeNumber from our data MINUS the 10 value we made up SHOULDN'T EQUALS a 10 + 5 sum, EQUALS"
        ,"The SomeNumber - 10 HASN'T EQUALS the 10 PLUS 5, EQUALS"
        ,"The SomeNumber MINUS 10 HAVEN'T EQUALS a 10 + 5 in number, EQUALS"
    )
    fun constraint_mustnot_equals_variable_2number_2arithmetic(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            15 AS SomeNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
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
                                            .hasName("SomeNumber")
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
                r.rules()
                    .first()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(10.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .staticNumber(5.0)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
         "SomeNumber - 10 MUST be OtherNumber + 5"
        ,"SomeNumber MINUS 10 SHOULD be OtherNumber PLUS 5"
        ,"SomeNumber - 10 HAS to be OtherNumber PLUS 5"
        ,"SomeNumber MINUS 10 HAVE to be the number OtherNumber PLUS 5"
        ,"the SomeNumber - 10 MUST OtherNumber PLUS 5 stuff"
        ,"All SomeNumber MINUS 10 SHOULD a OtherNumber PLUS 5"
        ,"The SomeNumber from the report - the 10 value really HAS to be the OtherNumber PLUS a number 5"
        ,"The SomeNumber from excel MINUS 10 HAVE to be an OtherNumber we found PLUS 5 in number"
        ,"The SomeNumber value from the Data - the made up number 10 MUST EQUALS be dodgy OtherNumber + an imaginary 5 value"
    ])
    fun constraint_must_2variable_number_2arithmetic(paramStr: String) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("OtherNumber")
                            .hasNumber(20.0)
            .parentModel()
                    .rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .hasError(paramStr)
                            .condition()
                                .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                                .leftArithmetical()
                                    .hasPreprocessedSource()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .hasNoOperator()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("SomeNumber")
                                                .hasType(DataPropertyType.Decimal)
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Subtraction)
                                            .staticNumber(10.0)
                    r.rules()
                            .first()
                                .hasPreprocessedSource()
                                .condition()
                                    .rightArithmetical()
                                        .operation()
                                            .hasPreprocessedSource()
                                            .hasSizeOf(2)
                                            .first()
                                                .hasPreprocessedSource()
                                                .hasNoOperator()
                                                .variableValue()
                                                    .hasPreprocessedSource()
                                                    .hasName("OtherNumber")
                                                    .hasType(DataPropertyType.Decimal)
                                        .parentOperation()
                                            .second()
                                                .hasPreprocessedSource()
                                                .hasOperator(ASTArithmeticalOperator.Addition)
                                                .staticNumber(5.0)
        }
    }



    @ParameterizedTest
    @ValueSource(strings = [
        "SomeNumber - 10 MUST NOT be OtherNumber PLUS 5"
        ,"SomeNumber MINUS 10 SHOULD NOT be OtherNumber PLUS 5"
        ,"SomeNumber - 10 HAS NOT to be OtherNumber PLUS 5"
        ,"SomeNumber MINUS 10 HAVE NOT to be the number OtherNumber PLUS 5"
        ,"the SomeNumber - 10 MUST NOT OtherNumber PLUS 5 stuff"
        ,"All SomeNumber things MINUS 10 things SHOULD NOT an OtherNumber PLUS a little 5"
        ,"The unbelievable SomeNumber number - the sturdy number 10 HAS NOT to be the smallestz OtherNumber PLUS a strange 5 thing"
        ,"The SomeNumber MINUS 10 HAVE NOT to be a OtherNumber PLUS 5 in number"
        ,"SomeNumber - 10 MUSTN'T be OtherNumber PLUS 5"
        ,"SomeNumber stuff MINUS the Number 10 SHOULDN'T be the OtherNumber from the file, but PLUS the number 5"
        ,"SomeNumber - 10 HASN'T to be OtherNumber PLUS 5"
        ,"SomeNumber fortyfour MINUS some random 10 HAVEN'T to be the unreliable number OtherNumber PLUS the tiny number 5"
        ,"the SomeNumber - 10 people MUSTN'T OtherNumber PLUS 5 stuff"
        ,"All SomeNumber MINUS 10 SHOULDN'T a OtherNumber PLUS 5"
        ,"The SomeNumber - 10 HASN'T to be the OtherNumber PLUS 5"
        ,"The SomeNumber Of Yesterday MINUS groovy number 10 really HAVEN'T to be a bad OtherNumber stuff PLUS a 5 in number"
    ])
    fun constraint_mustnot_2variable_number_2arithmetic(paramStr: String) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("SomeNumber")
                            .hasNumber(15.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("OtherNumber")
                            .hasNumber(20.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
                r.rules()
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("OtherNumber")
                                                .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .staticNumber(5.0)
        }
    }


    @ParameterizedTest
    @CsvSource(
         "SomeNumber - 10 MUST be GREATER OtherNumber + 5, LESS_OR_EQUALS"
        ,"SomeNumber MINUS 10 SHOULD be BIGGER OtherNumber PLUS 5, LESS_OR_EQUALS"
        ,"SomeNumber - 10 HAS to be LARGER THAN OtherNumber PLUS 5, LESS_OR_EQUALS"
        ,"SomeNumber MINUS 10 HAVE to be MORE the number OtherNumber PLUS 5, LESS_OR_EQUALS"
        ,"the SomeNumber - 10 MUST LESS THAN OtherNumber PLUS 5 stuff, GREATER_OR_EQUALS"
        ,"All SomeNumber MINUS 10 SHOULD SMALLER THAN a OtherNumber PLUS 5, GREATER_OR_EQUALS"
        ,"The SomeNumber - 10 HAS to be LOWER the OtherNumber PLUS 5, GREATER_OR_EQUALS"
        ,"The SomeNumber MINUS 10 HAVE to be FEWER THAN a OtherNumber PLUS 5 in number, GREATER_OR_EQUALS"
        ,"SomeNumber - 10 MUST EXCEED OtherNumber + 5, LESS_OR_EQUALS"
        ,"Some Number SomeNumber MINUS some Other number 10 SHOULD be LESS OR EQUAL another OtherNumber PLUS the little 5, GREATER_THAN"
        ,"The SomeNumber from the data - 10 value HAS to be AT MOST the OtherNumber PLUS number 5, GREATER_THAN"
        ,"A SomeNumber value from the files MINUS some even number 10 really HAVE to be GREATER OR EQUALS the number OtherNumber PLUS the value of 5, LESS_THAN"
        ,"the SomeNumber - 10 MUST LEAST OtherNumber PLUS 5 stuff, LESS_THAN"
        ,"SomeNumber - 10 MUST be EQUALS OtherNumber + 5, NOT_EQUALS"
        ,"Some SomeNumber MINUS 10 SHOULD be EQUALS OtherNumber PLUS 5, NOT_EQUALS"
        ,"SomeNumber - 10 HAS to be EQUALS OtherNumber PLUS 5, NOT_EQUALS"
        ,"SomeNumber MINUS 10 HAVE to be EQUALS the number OtherNumber PLUS 5, NOT_EQUALS"
        ,"SomeNumber - 10 MUST be EQUALS OtherNumber + 5, NOT_EQUALS"
        ,"SomeNumber MINUS 10 SHOULD be EQUALS OtherNumber PLUS 5, NOT_EQUALS"
        ,"Value SomeNumber - Value 10 HAS to be EQUALS the value OtherNumber PLUS the Number 5, NOT_EQUALS"
        ,"The SomeNumber value from the data MINUS the number 10 HAVE to be EQUALS the number OtherNumber PLUS the value of 5, NOT_EQUALS"
    )
    fun constraint_must_2variable_number_2arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("OtherNumber")
                            .hasNumber(20.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
                r.rules()
                    .first()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("OtherNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .staticNumber(5.0)
        }
    }


    @ParameterizedTest
    @CsvSource(
         "SomeNumber - 10 MUST NOT be GREATER THAN OtherNumber PLUS 5, GREATER_THAN"
        ,"SomeNumber MINUS 10 SHOULD NOT be BIGGER OtherNumber PLUS 5, GREATER_THAN"
        ,"SomeNumber - 10 HAS NOT to be LARGER OtherNumber PLUS 5, GREATER_THAN"
        ,"The Value Of SomeNumber MINUS The Number 10 HAVE NOT to be MORE THAN the number OtherNumber PLUS the number 5, GREATER_THAN"
        ,"the SomeNumber - 10 MUST NOT EXCEED OtherNumber PLUS 5 stuff, GREATER_THAN"
        ,"All SomeNumber MINUS 10 SHOULD NOT BE LESS THAN a OtherNumber PLUS 5, LESS_THAN"
        ,"The SomeNumber - 10 HAS NOT to be SMALLER the OtherNumber PLUS 5, LESS_THAN"
        ,"The SomeNumber MINUS the nice number 10 HAVE NOT to be LOWER a strange OtherNumber Value PLUS 5 in number, LESS_THAN"
        ,"SomeNumber - 10 MUSTN'T be FEWER THAN OtherNumber PLUS 5, LESS_THAN"
        ,"SomeNumber MINUS 10 SHOULDN'T be GREATER OR EQUAL OtherNumber PLUS 5, GREATER_OR_EQUALS"
        ,"SomeNumber - 10 HASN'T to be AT LEAST OtherNumber PLUS 5, GREATER_OR_EQUALS"
        ,"The Value Of SomeNumber from the Report MINUS the Value 10 HAVEN'T to be LESS OR EQUALS the unchecked number OtherNumber PLUS some arbitrary number 5, LESS_OR_EQUALS"
        ,"the SomeNumber - 10 people MUSTN'T be AT MOST OtherNumber PLUS 5 stuff, LESS_OR_EQUALS"
        ,"Number SomeNumber - Value 10 MUST NOT be EQUALS the OtherNumber we made up PLUS 5, EQUALS"
        ,"SomeNumber MINUS 10 SHOULD NOT be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber - 10 HAS NOT to be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber MINUS 10 HAVE NOT to be EQUALS the number OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber - 10 MUSTN'T be EQUALS  OtherNumber PLUS 5, EQUALS"
        ,"the SomeNumber value MINUS 10 in number SHOULDN'T be EQUALS the OtherNumber PLUS the value 5, EQUALS"
        ,"SomeNumber - 10 HASN'T to be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber MINUS 10 HAVEN'T to be EQUALS the number OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber - 10 MUST NOT be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"A sommy numby SomeNumber balla MINUS hobby 10 boom SHOULD NOT be EQUALS snuffy OtherNumber PLUS cute 5, EQUALS"
        ,"SomeNumber - 10 HAS NOT to be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber MINUS 10 HAVE NOT to be EQUALS the number OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber - 10 MUSTN'T be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber MINUS 10 SHOULDN'T be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"SomeNumber - 10 HASN'T to be EQUALS OtherNumber PLUS 5, EQUALS"
        ,"The SomeNumber value from the file MINUS The 10 Value HAVEN'T to be EQUALS the number OtherNumber PLUS the number 5, EQUALS"
    )
    fun constraint_mustnot_2variable_number_2arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("OtherNumber")
                            .hasNumber(20.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasName("SomeNumber")
                                                .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .staticNumber(10.0)
                r.rules()
                    .first()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("OtherNumber")
                                            .hasType(DataPropertyType.Decimal)
                        .parentOperation()
                            .second()
                                .hasPreprocessedSource()
                                .hasOperator(ASTArithmeticalOperator.Addition)
                                    .staticNumber(5.0)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "50 - SomeNumber MUST be OtherNumber + FinalNumber"
        ,"50 MINUS SomeNumber SHOULD be OtherNumber PLUS FinalNumber"
        ,"A 50 - the number SomeNumber HAS to be the same value of OtherNumber PLUS the FinalNumber"
        ,"A Number 50 MINUS the SomeNumber Value HAVE to be the number OtherNumber PLUS the other number FinalNumber"
        ,"50 - the SomeNumber MUST OtherNumber PLUS FinalNumber stuff"
        ,"50 MINUS All SomeNumber SHOULD a OtherNumber PLUS FinalNumber"
        ,"The Value 50 - The Value of SomeNumber HAS to be the Value Of OtherNumber PLUS the FinalNumber number"
        ,"50 MINUS The SomeNumber HAVE to be a OtherNumber PLUS FinalNumber in number"
    ])
    fun constraint_must_3variable_number_2arithmetic(paramStr: String) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            25 AS FinalNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(3)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("SomeNumber")
                            .hasNumber(15.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("OtherNumber")
                            .hasNumber(20.0)
                        .parentList()
                            .third()
                                .hasPreprocessedSource()
                                .hasName("FinalNumber")
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(50.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
                r.rules()
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("OtherNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("FinalNumber")
                                            .hasType(DataPropertyType.Decimal)
        }
    }



    @ParameterizedTest
    @ValueSource(strings = [
        "50 - SomeNumber MUST NOT be OtherNumber PLUS FinalNumber"
        ,"50 MINUS SomeNumber SHOULD NOT be OtherNumber PLUS FinalNumber"
        ,"50 - SomeNumber HAS NOT to be OtherNumber PLUS FinalNumber"
        ,"50 MINUS SomeNumber HAVE NOT to be the number OtherNumber PLUS FinalNumber"
        ,"the 50 - SomeNumber MUST NOT OtherNumber PLUS FinalNumber stuff"
        ,"All 50 MINUS SomeNumber SHOULD NOT a OtherNumber PLUS FinalNumber"
        ,"The 50 - SomeNumber HAS NOT to be the OtherNumber PLUS FinalNumber"
        ,"The 50 MINUS SomeNumber HAVE NOT to be a OtherNumber PLUS FinalNumber in number"
        ,"50 - SomeNumber MUSTN'T be OtherNumber PLUS FinalNumber"
        ,"50 MINUS SomeNumber SHOULDN'T be OtherNumber PLUS FinalNumber"
        ,"50 - SomeNumber HASN'T to be OtherNumber PLUS FinalNumber"
        ,"50 MINUS SomeNumber HAVEN'T to be the number OtherNumber PLUS FinalNumber"
        ,"the Number 50 - the variable SomeNumber of people MUSTN'T the variable OtherNumber PLUS the Variable FinalNumber stuff"
        ,"All the Of The Number 50 MINUS the Value SomeNumber SHOULDN'T an OtherNumber PLUS the FinalNumber"
        ,"The 50 - SomeNumber Value HASN'T to be the OtherNumber PLUS the FinalNumber"
        ,"The Number 50 MINUS the other number SomeNumber HAVEN'T to be an OtherNumber from the file PLUS the unnecessary number FinalNumber in number"
    ])
    fun constraint_mustnot_3variable_number_2arithmetic(paramStr: String) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            25 AS FinalNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(3)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
                .parentList()
                    .second()
                        .hasPreprocessedSource()
                        .hasName("OtherNumber")
                        .hasNumber(20.0)
                .parentList()
                    .third()
                        .hasPreprocessedSource()
                        .hasName("FinalNumber")
                        .hasNumber(25.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(ASTComparisonOperator.EQUALS)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(50.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
                r.rules()
                    .first()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("OtherNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("FinalNumber")
                                            .hasType(DataPropertyType.Decimal)
        }
    }


    @ParameterizedTest
    @CsvSource(
         "50 - SomeNumber MUST be GREATER THAN OtherNumber + FinalNumber, LESS_OR_EQUALS"
        ,"50 MINUS SomeNumber SHOULD be BIGGER OtherNumber PLUS FinalNumber, LESS_OR_EQUALS"
        ,"50 - SomeNumber HAS to be LARGER THAN OtherNumber PLUS FinalNumber, LESS_OR_EQUALS"
        ,"50 MINUS SomeNumber HAVE to be MORE THAN the number OtherNumber PLUS FinalNumber, LESS_OR_EQUALS"
        ,"50 the number - the SomeNumber of some person MUST be LESS than the OtherNumber in the files PLUS some FinalNumber stuff, GREATER_OR_EQUALS"
        ,"50 things MINUS All SomeNumber SHOULD SMALLER a some OtherNumber PLUS some other FinalNumber, GREATER_OR_EQUALS"
        ,"50 - The SomeNumber HAS to be FEWER the OtherNumber PLUS FinalNumber, GREATER_OR_EQUALS"
        ,"50 MINUS The SomeNumber HAVE to be LOWER THAN a OtherNumber PLUS FinalNumber in number, GREATER_OR_EQUALS"
        ,"50 things - SomeNumber stuff MUST EXCEED the OtherNumber from the data + the made-up FinalNumber, LESS_OR_EQUALS"
        ,"The number 50 MINUS the value in the variable SomeNumber SHOULD be AT LEAST the value in OtherNumber from our files PLUS the value in  FinalNumber we made up, LESS_THAN"
        ,"A 50 number - the variable SomeNumber HAS to be GREATER OR EQUAL the number in OtherNumber PLUS the FinalNumber value, LESS_THAN"
        ,"50 MINUS SomeNumber HAVE to be LESS OR EQUAL the number OtherNumber PLUS FinalNumber, GREATER_THAN"
        ,"The number 50 - the SomeNumber variable MUST AT MOST the value in OtherNumber PLUS the FinalNumber variable, GREATER_THAN"
        ,"50 - SomeNumber MUST be EQUALS THAN OtherNumber + FinalNumber, NOT_EQUALS"
        ,"50 MINUS SomeNumber SHOULD be EQUALS OtherNumber PLUS FinalNumber, NOT_EQUALS"
        ,"50 - SomeNumber HAS to be EQUALS OtherNumber PLUS FinalNumber, NOT_EQUALS"
        ,"The 50 value MINUS a SomeNumber value HAVE to be EQUALS to the number OtherNumber PLUS the FinalNumber variable, NOT_EQUALS"
    )
    fun constraint_must_3variable_number_2arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            25 AS FinalNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(3)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("OtherNumber")
                            .hasNumber(20.0)
                    .parentList()
                        .third()
                            .hasPreprocessedSource()
                            .hasName("FinalNumber")
                            .hasNumber(25.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(50.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
            r.rules()
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .rightArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("OtherNumber")
                                            .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("FinalNumber")
                                            .hasType(DataPropertyType.Decimal)
        }
    }


    @ParameterizedTest
    @CsvSource(
        "50 - SomeNumber MUST NOT be GREATER OtherNumber PLUS FinalNumber, GREATER_THAN"
        ,"50 MINUS SomeNumber SHOULD NOT be BIGGER THAN  OtherNumber PLUS FinalNumber, GREATER_THAN"
        ,"The number 50 - the SomeNumber  HAS NOT to be LARGER OtherNumber PLUS FinalNumber, GREATER_THAN"
        ,"A 50 number from the file MINUS some SomeNumber from the data HAVE NOT to be MORE the number OtherNumber PLUS FinalNumber, GREATER_THAN"
        ,"the 50 - SomeNumber MUST NOT LESS OtherNumber PLUS FinalNumber stuff, LESS_THAN"
        ,"All 50 MINUS a few SomeNumber cars SHOULD NOT SMALLER a OtherNumber we found PLUS the important FinalNumber, LESS_THAN"
        ,"The 50 - SomeNumber HAS NOT to be FEWER the OtherNumber PLUS FinalNumber, LESS_THAN"
        ,"The 50 of hundred things MINUS the couple of SomeNumber things HAVE NOT to be LOWER THAN a lowest OtherNumber PLUS the greatest FinalNumber in number, LESS_THAN"
        ,"50 - SomeNumber MUSTN'T be GREATER OR EQUAL OtherNumber PLUS FinalNumber, GREATER_OR_EQUALS"
        ,"A 50 MINUS SomeNumber things SHOULDN'T be AT LEAST the Big OtherNumber value PLUS the Tiny FinalNumber Value, GREATER_OR_EQUALS"
        ,"50 - SomeNumber HASN'T to be LESS OR EQUALS OtherNumber PLUS FinalNumber, LESS_OR_EQUALS"
        ,"50 MINUS SomeNumber HAVEN'T to be AT MOST the number OtherNumber PLUS FinalNumber, LESS_OR_EQUALS"
        ,"50 Ding Dongs - a SomeNumber stuff MUST NOT be EQUALS the OtherNumber thngy PLUS the big FinalNumber we love, EQUALS"
        ,"50 MINUS SomeNumber SHOULD NOT be EQUALS THAN  OtherNumber PLUS FinalNumber, EQUALS"
        ,"50 - SomeNumber HAS NOT to be EQUALS OtherNumber PLUS FinalNumber, EQUALS"
        ,"50 MINUS SomeNumber HAVE NOT to be EQUALS the number OtherNumber PLUS FinalNumber, EQUALS"
        ,"50 - SomeNumber MUSTN'T be EQUALS OtherNumber PLUS FinalNumber, EQUALS"
        ,"50 MINUS SomeNumber SHOULDN'T be EQUALS OtherNumber PLUS FinalNumber, EQUALS"
        ,"50 - SomeNumber HASN'T to be EQUALS OtherNumber PLUS FinalNumber, EQUALS"
        ,"The 50 things MINUS the SomeNumber value HAVEN'T to be EQUALS the other number OtherNumber PLUS a FinalNumber thing, EQUALS"
    )
    fun constraint_mustnot_3variable_number_2arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            15 AS SomeNumber

            20 AS OtherNumber

            25 AS FinalNumber

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(3)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("SomeNumber")
                        .hasNumber(15.0)
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasName("OtherNumber")
                            .hasNumber(20.0)
                    .parentList()
                        .third()
                            .hasPreprocessedSource()
                            .hasName("FinalNumber")
                            .hasNumber(25.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .staticNumber(50.0)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Subtraction)
                                        .variableValue()
                                            .hasPreprocessedSource()
                                            .hasName("SomeNumber")
                                            .hasType(DataPropertyType.Decimal)
            r.rules()
                .first()
                    .condition()
                        .rightArithmetical()
                            .operation()
                                .hasPreprocessedSource()
                                .hasSizeOf(2)
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("OtherNumber")
                                        .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .second()
                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasName("FinalNumber")
                                        .hasType(DataPropertyType.Decimal)
        }
    }


//    -----------------------------------------------


    @ParameterizedTest
    @CsvSource(
        "Employees MUST be GREATER 5, LESS_OR_EQUALS"
        ,"In one shift the Employees doing home office SHOULD at MOST 5 people, GREATER_THAN"
        ,"Employees HAS to be MORE then 5, LESS_OR_EQUALS"
        ,"Employees working in our department HAVE to be at LEAST 5 in numbers, LESS_THAN"
        ,"the Employees MUST be at MOST 5 stuff, GREATER_THAN"
        ,"All Employees working in our building SHOULD BIGGER then exactly 5 people, LESS_OR_EQUALS"
        ,"The Employees HAS to LESS OR EQUAL the 5, GREATER_THAN"
        ,"The Employees for our analysis HAVE to be MORE than a 5 people in number, LESS_OR_EQUALS"
        ,"Employees MUST be LESS 5, GREATER_OR_EQUALS"
        ,"The Employees in New York SHOULD GREATER OR EQUAL 5 people, LESS_THAN"
        ,"Employees HAS to be LESS OR EQUAL 5, GREATER_THAN"
        ,"Alle Employees HAVE to be LESS OR EQUAL then our miximum value 5 , GREATER_THAN"
        ,"the Employees MUST LARGER 5 stuff, LESS_OR_EQUALS"
        ,"All the lazy Employees we have SHOULD LESS than a 5 if at all, GREATER_OR_EQUALS"
        ,"The Employees HAS to SMALLER the 5, GREATER_OR_EQUALS"
        ,"The Employees HAVE to be MORE than a 5 in number, LESS_OR_EQUALS"
        ,"The Lazy Employees of our company MUST be EQUALS 5 years, NOT_EQUALS"
        ,"Employees SHOULD EQUALS 5, NOT_EQUALS"
        ,"Employees HAS to be EQUALS then 5, NOT_EQUALS"
        ,"Employees HAVE to be EQUALS the number 5, NOT_EQUALS"
    )
    fun constraint_must_variable_number_greater_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            2 AS Employees

            $paramStr
            """


        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftVariable()
                        .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                        .parentCondition()
                            .rightNumber()
                                .hasValue(5.0)
                                .hasPreprocessedSource()
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(2.0)
        }
    }


    @ParameterizedTest
    @CsvSource(
            "Employees MUST NOT be GREATER 5,GREATER_THAN"
            ,"Employees in Smoking Room SHOULD NOT be BIGGER than 5 Smokers,GREATER_THAN"
            ,"Employees HAS NOT to EXCEED 5,GREATER_THAN"
            ,"In our Department the Employees off work HAVE NOT to be LARGER THAN the number 5,GREATER_THAN"
            ,"the Employees MUST NOT be MORE than 5 stuff,GREATER_THAN"
            ,"All Employees coming to Our Department SHOULD NOT LESS a a minimum of 5 people,LESS_THAN"
            ,"The Employees HAS NOT to be SMALLER than the 5,LESS_THAN"
            ,"The Employees HAVE NOT to be LOWER than a 5 in number,LESS_THAN"
            ,"Our Companys Employees MUSTN'T be LESS OR EQUAL 5 in presence,LESS_OR_EQUALS"
            ,"Employees SHOULDN'T be at MOST 5,LESS_OR_EQUALS"
            ,"The In-Office Employees HASN'T to be LESS OR EQUALS 5 people,LESS_OR_EQUALS"
            ,"Employees HAVEN'T to be GREATER OR EQUAL the number 5,GREATER_OR_EQUALS"
            ,"the Employees people MUSTN'T be at LEAST 5 stuff,GREATER_OR_EQUALS"
            ,"All our Employees staying at home SHOULDN'T EXCEED 5 people,GREATER_THAN"
            ,"The Employees staying at home HASN'T to be MORE the 5 people in number,GREATER_THAN"
            ,"The Employees HAVEN'T to be BIGGER than 5 in number,GREATER_THAN"
            ,"The Employees in our study MUST NOT be EQUALS 5 persons,EQUALS"
            ,"Employees SHOULD NOT be EQUALS than 5,EQUALS"
            ,"Employees HAS NOT to EQUALS 5,EQUALS"
            ,"Half of Employees in one team HAVE NOT to be EQUALS the number of 5,EQUALS"
            ,"Employees MUSTN'T be EQUALS 5,EQUALS"
            ,"Employees SHOULDN'T be EQUALS 5,EQUALS"
            ,"Employees HASN'T to be EQUALS 5,EQUALS"
            ,"Employees HAVEN'T to be EQUALS the number 5,EQUALS"
    )
    fun constraint_mustnot_variable_number_greater_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            0 AS Employees

            $paramStr
            """

        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                        .parentCondition()
                            .rightNumber()
                                .hasValue(5.0)
                                .hasPreprocessedSource()
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(0.0)
        }
    }


//TODO: TB 25.04.19: Auch hier nach der entsprechenden Implementierung NumberOfPeople OF WorkUnit hinzufügen und testen
    @ParameterizedTest
    @CsvSource(
             "Employees MUST be GREATER WorkUnit.NumberOfPeople, LESS_OR_EQUALS"
            ,"Employees SHOULD at MOST WorkUnit.NumberOfPeople, GREATER_THAN"
            ,"Our group of Employees HAS to be MORE then number defined in WorkUnit.NumberOfPeople, LESS_OR_EQUALS"
            ,"Employees HAVE to be at LEAST the number WorkUnit.NumberOfPeople, LESS_THAN"
            ,"the Employees MUST be at MOST WorkUnit.NumberOfPeople stuff, GREATER_THAN"
            ,"All Employees coming to the meeting SHOULD BIGGER then a WorkUnit.NumberOfPeople in Number, LESS_OR_EQUALS"
            ,"The Employees HAS to LESS OR EQUAL the WorkUnit.NumberOfPeople, GREATER_THAN"
            ,"The Employees HAVE to be MORE than a WorkUnit.NumberOfPeople in number, LESS_OR_EQUALS"
            ,"the Number Of Employees in one Unit_of_Work MUST be LESS WorkUnit.NumberOfPeople, GREATER_OR_EQUALS"
            ,"Employees SHOULD GREATER OR EQUAL WorkUnit.NumberOfPeople, LESS_THAN"
            ,"Employees HAS to be LESS OR EQUAL WorkUnit.NumberOfPeople, GREATER_THAN"
            ,"Employees HAVE to be LESS OR EQUAL then the number WorkUnit.NumberOfPeople, GREATER_THAN"
            ,"the Employees in Each Department MUST LARGER WorkUnit.NumberOfPeople in the database, LESS_OR_EQUALS"
            ,"All Employees SHOULD LESS than a WorkUnit.NumberOfPeople, GREATER_OR_EQUALS"
            ,"The Employees HAS to SMALLER the WorkUnit.NumberOfPeople, GREATER_OR_EQUALS"
            ,"The Working Employees inhouse HAVE to be MORE than the Variable WorkUnit.NumberOfPeople in number, LESS_OR_EQUALS"
            ,"Employees MUST be EQUALS WorkUnit.NumberOfPeople, NOT_EQUALS"
            ,"Employees SHOULD EQUALS WorkUnit.NumberOfPeople, NOT_EQUALS"
            ,"Employees HAS to be EQUALS then WorkUnit.NumberOfPeople, NOT_EQUALS"
            ,"Employees HAVE to be EQUALS the number WorkUnit.NumberOfPeople, NOT_EQUALS"
    )
    fun constraint_must_variable_property_number_greater_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            2 AS Employees

            $paramStr
            """


        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 15}}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                        .parentCondition()
                            .rightProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("WorkUnit.NumberOfPeople")
                                    .hasType(DataPropertyType.Decimal)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(2.0)
        }
    }


    //TODO: TB 25.04.19: Auch hier nach der entsprechenden Implementierung "NumberOfPeople OF WorkUnit" hinzufügen und testen
    @ParameterizedTest
    @CsvSource(
            "Employees MUST NOT be GREATER WorkUnit.NumberOfPeople,GREATER_THAN"
            ,"The most of our Employees at home SHOULD NOT be BIGGER than the WorkUnit.NumberOfPeople Value,GREATER_THAN"
            ,"Employees HAS NOT to EXCEED WorkUnit.NumberOfPeople,GREATER_THAN"
            ,"Employees HAVE NOT to be LARGER the number WorkUnit.NumberOfPeople,GREATER_THAN"
            ,"the Employees of our Project MUST NOT be MORE than WorkUnit.NumberOfPeople in number,GREATER_THAN"
            ,"All Employees SHOULD NOT LESS a WorkUnit.NumberOfPeople,LESS_THAN"
            ,"The Employees in one Department HAS NOT to be SMALLER than the WorkUnit.NumberOfPeople At a Time,LESS_THAN"
            ,"The Employees HAVE NOT to be LOWER than a WorkUnit.NumberOfPeople in number,LESS_THAN"
            ,"Employees who are working in our department MUSTN'T be LESS OR EQUAL the Value in WorkUnit.NumberOfPeople,LESS_OR_EQUALS"
            ,"Employees SHOULDN'T be at MOST WorkUnit.NumberOfPeople,LESS_OR_EQUALS"
            ,"Employees HASN'T to be LESS OR EQUALS WorkUnit.NumberOfPeople,LESS_OR_EQUALS"
            ,"Employees HAVEN'T to be GREATER OR EQUAL the number WorkUnit.NumberOfPeople,GREATER_OR_EQUALS"
            ,"the Employees people MUSTN'T be at LEAST WorkUnit.NumberOfPeople stuff,GREATER_OR_EQUALS"
            ,"All Employees SHOULDN'T EXCEED WorkUnit.NumberOfPeople,GREATER_THAN"
            ,"The Employees HASN'T to be MORE the WorkUnit.NumberOfPeople,GREATER_THAN"
            ,"The Employees HAVEN'T to be BIGGER than WorkUnit.NumberOfPeople in number,GREATER_THAN"
            ,"Employees MUST NOT be EQUALS WorkUnit.NumberOfPeople,EQUALS"
            ,"Employees SHOULD NOT be EQUALS than WorkUnit.NumberOfPeople,EQUALS"
            ,"Employees in Insurance Project HAS NOT to EQUALS the Number WorkUnit.NumberOfPeople,EQUALS"
            ,"Employees HAVE NOT to be EQUALS the number WorkUnit.NumberOfPeople,EQUALS"
            ,"Employees MUSTN'T be EQUALS WorkUnit.NumberOfPeople,EQUALS"
            ,"Employees SHOULDN'T be  EQUALS WorkUnit.NumberOfPeople,EQUALS"
            ,"Employees HASN'T to be EQUALS WorkUnit.NumberOfPeople,EQUALS"
            ,"Employees HAVEN'T to be EQUALS the number WorkUnit.NumberOfPeople,EQUALS"
    )
    fun constraint_mustnot_variable_property_number_greater_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            0 AS Employees

            $paramStr
            """

        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 15}}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftVariable()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasName("Employees")
                        .parentCondition()
                            .rightProperty()
                                .hasPreprocessedSource()
                                .hasType(DataPropertyType.Decimal)
                                .hasPath("WorkUnit.NumberOfPeople")
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(0.0)
        }
    }


    //TODO: TB 25.04.19: Auch hier nach der entsprechenden Implementierung NumberOfPeople OF WorkUnit hinzufügen und testen
    @ParameterizedTest
    @CsvSource(
            "WorkUnit.NumberOfPeople + 15 MUST be GREATER Employees * 2, LESS_OR_EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 SHOULD at MOST Employees TIMES 2, GREATER_THAN"
            ,"The WorkUnit.NumberOfPeople + 15 other people HAS to be MORE then Employees * the factor of 2, LESS_OR_EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 HAVE to be at LEAST the number Employees TIMES 2, LESS_THAN"
            ,"the WorkUnit.NumberOfPeople + 15 MUST be at MOST Employees stuff * 2, GREATER_THAN"
            ,"All WorkUnit.NumberOfPeople PLUS another 15 number of them SHOULD BIGGER then a Employees TIMES 2 Number, LESS_OR_EQUALS"
            ,"The WorkUnit.NumberOfPeople + 15 HAS to LESS OR EQUAL the Employees * 2, GREATER_THAN"
            ,"The WorkUnit.NumberOfPeople PLUS 15 HAVE to be MORE than a Employees in number TIMES 2, LESS_OR_EQUALS"
            ,"All the WorkUnit.NumberOfPeople we ave + the 15 people from somewhere MUST be LESS the Employees * the Fanatasy Number 2, GREATER_OR_EQUALS"
            ,"WorkUnit.NumberOfPeople in the house PLUS the Magic 15 SHOULD GREATER OR EQUAL the Variable Employees TIMES the Number 2, LESS_THAN"
            ,"WorkUnit.NumberOfPeople + 15 HAS to be LESS OR EQUAL Employees * 2, GREATER_THAN"
            ,"The Number in WorkUnit.NumberOfPeople PLUS the number 15 HAVE to be LESS OR EQUAL the number in the Variable Employees TIMES number 2, GREATER_THAN"
            ,"the WorkUnit.NumberOfPeople + 15 MUST LARGER Employees stuff * 2, LESS_OR_EQUALS"
            ,"All WorkUnit.NumberOfPeople from our Database PLUS 15 from somewhere else SHOULD LESS than a Employees TIMES the Number Of 2, GREATER_OR_EQUALS"
            ,"The WorkUnit.NumberOfPeople + 15 HAS to SMALLER the Employees * 2, GREATER_OR_EQUALS"
            ,"The WorkUnit.NumberOfPeople PLUS 15 HAVE to be MORE than a Employees in number TIMES 2, LESS_OR_EQUALS"
            ,"Our WorkUnit.NumberOfPeople + 15 from India MUST be EQUALS necessary Employees * Safety Factor 2, NOT_EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 SHOULD EQUALS Employees TIMES 2, NOT_EQUALS"
            ,"WorkUnit.NumberOfPeople + 15 HAS to be EQUALS Employees * 2, NOT_EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 HAVE to be EQUALS the number Employees TIMES 2, NOT_EQUALS"
    )
    fun constraint_must_nested_property_variable_number_2arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            2 AS Employees

            $paramStr
            """


        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 15}}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(2.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .propertyValue()
                                                .hasPreprocessedSource()
                                                .hasPath("WorkUnit.NumberOfPeople")
                                                .hasType(DataPropertyType.Decimal)
                                .parentOperation()
                                    .second()
                                        .hasPreprocessedSource()
                                        .hasOperator(ASTArithmeticalOperator.Addition)
                                        .staticNumber(15.0)
                    r.rules()
                        .first()
                            .condition()
                                .rightArithmetical()
                                    .operation()
                                        .hasPreprocessedSource()
                                        .hasSizeOf(2)
                                        .first()
                                            .hasPreprocessedSource()
                                            .variableValue()
                                                .hasPreprocessedSource()
                                                .hasType(DataPropertyType.Decimal)
                                                .hasName("Employees")
                                    .parentOperation()
                                        .second()
                                            .hasPreprocessedSource()
                                            .hasOperator(ASTArithmeticalOperator.Multiplication)
                                            .staticNumber(2.0)
        }
    }

    //TODO: TB 25.04.19: Auch hier nach der entsprechenden Implementierung "NumberOfPeople OF WorkUnit" hinzufügen und testen
    @ParameterizedTest
    @CsvSource(
             "WorkUnit.NumberOfPeople + 15 MUST NOT be GREATER Employees * 2,GREATER_THAN"
            ,"WorkUnit.NumberOfPeople in the Group PLUS another group of 15 SHOULD NOT be BIGGER than Employees group TIMES the number 2,GREATER_THAN"
            ,"WorkUnit.NumberOfPeople + 15 HAS NOT to EXCEED Employees * 2,GREATER_THAN"
            ,"WorkUnit.NumberOfPeople PLUS 15 HAVE NOT to be LARGER the number Employees TIMES 2,GREATER_THAN"
            ,"the WorkUnit.NumberOfPeople Number + the Number 15 MUST NOT be MORE than the entire Employees * the Value Of 2,GREATER_THAN"
            ,"All WorkUnit.NumberOfPeople PLUS 15 SHOULD NOT LESS a Employees TIMES 2,LESS_THAN"
            ,"The WorkUnit.NumberOfPeople + 15 HAS NOT to be SMALLER than the Employees * 2,LESS_THAN"
            ,"The Value in WorkUnit.NumberOfPeople PLUS 15 HAVE NOT to be LOWER than our Employees Number TIMES 2,LESS_THAN"
            ,"WorkUnit.NumberOfPeople + 15 MUSTN'T be LESS OR EQUAL Employees * 2,LESS_OR_EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 SHOULDN'T be at MOST Employees TIMES 2,LESS_OR_EQUALS"
            ,"In the WorkUnit.NumberOfPeople Variable + A Constant of 15 HASN'T to be LESS OR EQUALS to the Employees Variable * Factor 2,LESS_OR_EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 HAVEN'T to be GREATER OR EQUAL the number Employees TIMES 2,GREATER_OR_EQUALS"
            ,"the WorkUnit.NumberOfPeople + 15 people MUSTN'T be at LEAST Employees stuff * 2,GREATER_OR_EQUALS"
            ,"All the People in WorkUnit.NumberOfPeople PLUS the Constant Value 15 SHOULDN'T EXCEED all our Employees TIMES the Number 2,GREATER_THAN"
            ,"The WorkUnit.NumberOfPeople Property + the 15 constant HASN'T to be MORE then the Value in Employees * the Number 2,GREATER_THAN"
            ,"The WorkUnit.NumberOfPeople PLUS 15 HAVEN'T to be BIGGER than Employees in number TIMES 2,GREATER_THAN"
            ,"The Number in WorkUnit.NumberOfPeople + the Value 15 MUST NOT be EQUALS all Employees * contant Factor 2,EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 SHOULD NOT be EQUALS than Employees TIMES 2,EQUALS"
            ,"WorkUnit.NumberOfPeople + 15 HAS NOT to EQUALS Employees * 2,EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 HAVE NOT to be EQUALS the number Employees TIMES 2,EQUALS"
            ,"WorkUnit.NumberOfPeople + 15 MUSTN'T be EQUALS Employees * 2,EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 SHOULDN'T be EQUALS Employees TIMES 2,EQUALS"
            ,"WorkUnit.NumberOfPeople + 15 HASN'T to be EQUALS Employees * 2,EQUALS"
            ,"WorkUnit.NumberOfPeople PLUS 15 HAVEN'T to be EQUALS the number Employees TIMES 2,EQUALS"
    )
    fun constraint_mustnot_nested_property_variable_number_2arithmetic_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            10 AS Employees

            $paramStr
            """

        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 15}}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(10.0)
            .parentModel()
                .rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasError(paramStr)
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .first()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .propertyValue()
                                            .hasPreprocessedSource()
                                            .hasPath("WorkUnit.NumberOfPeople")
                                            .hasType(DataPropertyType.Decimal)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource()
                                    .hasOperator(ASTArithmeticalOperator.Addition)
                                    .staticNumber(15.0)
                r.rules()
                    .first()
                        .condition()
                            .rightArithmetical()
                                .hasPreprocessedSource()
                                .operation()
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
                                        .staticNumber(2.0)
        }
    }


    //TODO: TB 25.04.19: Auch hier nach der entsprechenden Implementierung NumberOfPeople OF WorkUnit hinzufügen und testen
//    TODO: TB 25.04.19: Auch hier führt Klammer zu direkt nach Property noch zu Fehler. Klammern werden auch nicht unterstützt
    @ParameterizedTest
    @CsvSource(
            "(Constant Value 80 + the Variable WorkUnit.NumberOfPeople ) / the Scale 100 MUST be GREATER all Our Employees * the Standard Multiplier, LESS_OR_EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD at MOST Employees TIMES Multiplier, GREATER_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS to be MORE then Employees * Multiplier, LESS_OR_EQUALS"
            ,"(The Number 80 PLUS the Value Of WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be at LEAST the number Employees TIMES Multiplier, LESS_THAN"
            ,"(80 + the WorkUnit.NumberOfPeople ) / 100 MUST be at MOST Employees stuff * Multiplier, GREATER_THAN"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD BIGGER then a Employees TIMES Multiplier, LESS_OR_EQUALS"
            ,"(80 Number + The WorkUnit.NumberOfPeople Property ) / the 100 Standard HAS to LESS OR EQUAL the Employees Value * the new Multiplier, GREATER_THAN"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be MORE than a Employees in number TIMES Multiplier, LESS_OR_EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 MUST be LESS Employees * Multiplier, GREATER_OR_EQUALS"
            ,"(An Arbitrary Number 80 PLUS the Property WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD GREATER OR EQUAL Employees TIMES Multiplier, LESS_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS to be LESS OR EQUAL Employees * Multiplier, GREATER_THAN"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be LESS OR EQUAL then the number Employees TIMES Multiplier, GREATER_THAN"
            ,"(The Constant 80 + the Number in WorkUnit.NumberOfPeople ) / Standardizer 100 MUST LARGER all Employees people * the Multiplier, LESS_OR_EQUALS"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD LESS than a Employees TIMES Multiplier, GREATER_OR_EQUALS"
            ,"(80 + The WorkUnit.NumberOfPeople ) / 100 HAS to SMALLER the Employees * Multiplier, GREATER_OR_EQUALS"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be MORE than a Employees in number TIMES Multiplier, LESS_OR_EQUALS"
            ,"(The Constant Number 80 + Property Value WorkUnit.NumberOfPeople ) / the Number 100 MUST be EQUALS the Variable Employees * the Big Multiplier, NOT_EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD EQUALS Employees TIMES Multiplier, NOT_EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS to be EQUALS then Employees * Multiplier, NOT_EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE to be at EQUALS the number Employees TIMES Multiplier, NOT_EQUALS"
    )
    fun constraint_must_nested_property_variable_number_2arithmetic3_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            2 AS Employees

            5 AS Multiplier

            $paramStr
            """


        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 15}}") {
            r ->
                r.variables()
                    .hasSizeOf(2)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("Employees")
                        .hasNumber(2.0)
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
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
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
                                    .hasSizeOf(2)
                                    .hasNoOperator()
                                .first()
                                    .hasPreprocessedSource()
                                    .hasNoOperator()
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasType(DataPropertyType.Decimal)
                                        .hasName("Employees")
                            .parentOperation()
                                .second()
                                    .hasOperator(ASTArithmeticalOperator.Multiplication)
                                    .variableValue()
                                        .hasPreprocessedSource()
                                        .hasType(DataPropertyType.Decimal)
                                        .hasName("Multiplier")
        }
    }


    //TODO: TB 25.04.19: Auch hier nach der entsprechenden Implementierung "NumberOfPeople OF WorkUnit" hinzufügen und testen
    //    TODO: TB 25.04.19: Auch hier führt Klammer zu direkt nach Property noch zu Fehler. Klammern werden auch nicht unterstützt
    @ParameterizedTest
    @CsvSource(
             "(80 + WorkUnit.NumberOfPeople ) / 100 MUST NOT be GREATER Employees * Multiplier,GREATER_THAN"
            ,"(The 80 Again PLUS our good WorkUnit.NumberOfPeople ) DIVIDED BY Standard 100 Number SHOULD NOT be BIGGER than all our Employees TIMES Multiplier,GREATER_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS NOT to EXCEED Employees * Multiplier,GREATER_THAN"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE NOT to be LARGER the number Employees TIMES Multiplier,GREATER_THAN"
            ,"(Constant Value 80 + the Property Number WorkUnit.NumberOfPeople ) / Standard Value 100 MUST NOT be MORE than Employees stuff * Multiplier,GREATER_THAN"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD NOT LESS a Employees TIMES Multiplier,LESS_THAN"
            ,"(80 + The WorkUnit.NumberOfPeople ) / 100 HAS NOT to be SMALLER than the Employees * Multiplier,LESS_THAN"
            ,"(The arbitrary Value 80 PLUS Database Value The WorkUnit.NumberOfPeople ) DIVIDED BY Some 100 HAVE NOT to be LOWER than a Employees in number TIMES Multiplier,LESS_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 MUSTN'T be LESS OR EQUAL Employees * Multiplier,LESS_OR_EQUALS"
            ,"(80 PLUS WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULDN'T be at MOST Employees TIMES Multiplier,LESS_OR_EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HASN'T to be LESS OR EQUALS Employees * Multiplier,LESS_OR_EQUALS"
            ,"(Value of 80 PLUS the Number in WorkUnit.NumberOfPeople ) DIVIDED BY the Number 100 HAVEN'T to be GREATER OR EQUAL the number Employees TIMES Standard Multiplier,GREATER_OR_EQUALS"
            ,"(80 + the WorkUnit.NumberOfPeople ) / 100 people MUSTN'T be at LEAST Employees stuff * Multiplier,GREATER_OR_EQUALS"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULDN'T EXCEED Employees TIMES Multiplier,GREATER_THAN"
            ,"(The Number 80 + The Data Property WorkUnit.NumberOfPeople ) / 100 HASN'T to be MORE the Employees Value * the Global Multiplier,GREATER_THAN"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVEN'T to be BIGGER than Employees in number TIMES Multiplier,GREATER_THAN"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 MUST NOT EQUALS Employees * Multiplier,EQUALS"
            ,"(The Base of 80 PLUS The Property Value WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD NOT EQUALS than Employees TIMES Multiplier,EQUALS"
            ,"(80 + WorkUnit.NumberOfPeople ) / 100 HAS NOT to EQUALS Employees * Multiplier,EQUALS"
            ,"(Base Value of 80 PLUS The WorkUnit.NumberOfPeople Property Value ) DIVIDED BY Divisor 100 HAVE NOT to be EQUALS the number Of Employees TIMES the Standard Multiplier,EQUALS"
            ,"(80 + the WorkUnit.NumberOfPeople ) / 100 MUST NOT be EQUALS than Employees stuff * Multiplier,EQUALS"
            ,"(80 PLUS All WorkUnit.NumberOfPeople ) DIVIDED BY 100 SHOULD NOT EQUALS a Employees TIMES Multiplier,EQUALS"
            ,"(80 + The WorkUnit.NumberOfPeople ) / 100 HAS NOT to be EQUALS than the Employees * Multiplier,EQUALS"
            ,"(80 PLUS The WorkUnit.NumberOfPeople ) DIVIDED BY 100 HAVE NOT to be EQUALS than a Employees in number TIMES Multiplier,EQUALS"
    )
    fun constraint_mustnot_nested_property_variable_number_2arithmetic3_relational(paramStr: String, expectedOperator: ASTComparisonOperator) {
        val input = """
            10 AS Employees

            5 AS Multiplier

            $paramStr
            """

        End2AstRunner.run(input, "{WorkUnit: {NumberOfPeople: 15}}") {
            r ->
                r.variables()
                    .hasSizeOf(2)
                        .first()
                            .hasPreprocessedSource()
                            .hasName("Employees")
                            .hasNumber(10.0)
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
                        .condition()
                            .hasOperator(expectedOperator)
                            .leftArithmetical()
                                .operation()
                                    .hasPreprocessedSource()
                                    .hasSizeOf(2)
                                    .firstSubOperation()
                                        .hasPreprocessedSource()
                                        .hasNoOperator()
                                        .hasSizeOf(2)
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
                                    .hasSizeOf(2)
                                    .hasNoOperator()
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
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
         "Applicant.Status must not exist",
         "The Applicant.Status really must not exist",
         "The Applicant.Status really must not exist in this case"
    ])
    fun constrained_mustnot_with_exists_operator(paramStr: String) {

        val input = """
               $paramStr
            """

        End2AstRunner.run(input, "{Applicant: {Status: Student}}") {
                r ->
                    r.rules()
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
         "Applicant.Status must exist",
         "The Applicant.Status really must exist",
         "The Applicant.Status really must exist in this case"
    ])
    fun constrained_must_with_exists_operator(paramStr: String) {

        val input = """
               $paramStr
            """

        End2AstRunner.run(input, "{Applicant: {Status: Student}}") {
                r ->
                    r.rules()
                        .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                            .condition()
                                .hasPreprocessedSource()
                                .hasOperator(ASTComparisonOperator.NOT_EXISTS)
                                .leftProperty()
                                    .hasPreprocessedSource()
                                    .hasPath("Applicant.Status")
                                    .hasType(DataPropertyType.String)
                            .parentCondition()
                                .hasNoRightOperand()
        }
    }

    //TODO jgeske 21.8.19 implement missing test
    @ParameterizedTest
    @ValueSource(strings = [
        "your foobar has to be greater than 3.01z"
    ])
    fun rule_should_not_be_identified_as_filepath(paramStr: String) {

        val input = """
               $paramStr
            """

        End2AstRunner.run(input, "{ foobar : 'bizbaz' }") {
            r ->
            r.rules()
                    .first()
                    .condition()
                    .leftProperty()
        }
    }
    
    @ParameterizedTest
    @ValueSource(strings = [
        "der name muss peter und die stadt gleich Berlin sein",
        "der name muss peter sein und die stadt gleich Berlin"
    ])
    fun condition_group_culture_de_after_constrained_should_be_implicid_negated(paramStr: String) {

        val input = """
               $paramStr
            """

        End2AstRunner.run(input,"{ name : peter, stadt: berlin }", "de") {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                        .conditionGroup()
                        .hasSize(2)
                        .first()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                        .parentConditionGroup()
                        .second()
                            .hasOperator(ASTComparisonOperator.NOT_EQUALS)

        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "der name muss peter und die stadt nicht gleich Berlin sein",
        "der name muss peter sein und die stadt nicht gleich Berlin"
    ])
    fun condition_group_culture_de_after_constrained_should_be_implicid_negated_reverse(paramStr: String) {

        val input = """
               $paramStr
            """

        End2AstRunner.run(input,"{ name : peter, stadt: berlin }", "de") {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .conditionGroup()
                    .hasSize(2)
                    .first()
                    .hasOperator(ASTComparisonOperator.NOT_EQUALS)
                    .parentConditionGroup()
                    .second()
                    .hasOperator(ASTComparisonOperator.EQUALS)

        }
    }
}


