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

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SourceTest {

    @ParameterizedTest
    @CsvSource(
            "10 AS Employees, 10 ʬasʬAS Employees, 10",
            "22 AS Value, 22 ʬasʬAS Value, 22",
            "333.3 AS Value, 333.3 ʬasʬAS Value, 333.3",
            "-1 AS Value, -1 ʬasʬAS Value, -1",
            "-1.1 AS Value, -1.1 ʬasʬAS Value, -1.1"
    )
    fun number_as_variable(paramStr: String, expectedsource: String, expectedNumberSource: String){
        var input = """
            $paramStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                .hasSizeOf(1)
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedsource)
                .number()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedNumberSource)
        }
    }

    @ParameterizedTest
    @CsvSource(
            "10 + 10 AS Employees, 10 ʬarithmoperatorʬaddʬ_2b_ 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬaddʬ_2b_ 10, 10, ʬarithmoperatorʬaddʬ_2b_ 10",
            "10 ADDED TO 10 AS Employees, 10 ʬarithmoperatorʬaddʬADDED_20_TO 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬaddʬADDED_20_TO 10, 10, ʬarithmoperatorʬaddʬADDED_20_TO 10",
            "1.1 + 1.1 AS Employees, 1.1 ʬarithmoperatorʬaddʬ_2b_ 1.1 ʬasʬAS Employees, 1.1 ʬarithmoperatorʬaddʬ_2b_ 1.1, 1.1, ʬarithmoperatorʬaddʬ_2b_ 1.1",
            "-1.1 + -1.1 AS Employees, -1.1 ʬarithmoperatorʬaddʬ_2b_ -1.1 ʬasʬAS Employees, -1.1 ʬarithmoperatorʬaddʬ_2b_ -1.1, -1.1, ʬarithmoperatorʬaddʬ_2b_ -1.1",
            "10 PLUS 10 AS Employees, 10 ʬarithmoperatorʬaddʬPLUS 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬaddʬPLUS 10, 10, ʬarithmoperatorʬaddʬPLUS 10",
            "10 - 10 AS Employees, 10 ʬarithmoperatorʬsubtractʬ_2d_ 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬsubtractʬ_2d_ 10, 10, ʬarithmoperatorʬsubtractʬ_2d_ 10",
            "10 MINUS 10 AS Employees, 10 ʬarithmoperatorʬsubtractʬMINUS 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬsubtractʬMINUS 10, 10, ʬarithmoperatorʬsubtractʬMINUS 10",
            "10 * 10 AS Employees, 10 ʬarithmoperatorʬmultiplyʬ_2a_ 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬmultiplyʬ_2a_ 10, 10, ʬarithmoperatorʬmultiplyʬ_2a_ 10",
            "10 MULTIPLIED WITH 10 AS Employees, 10 ʬarithmoperatorʬmultiplyʬMULTIPLIED_20_WITH 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬmultiplyʬMULTIPLIED_20_WITH 10, 10, ʬarithmoperatorʬmultiplyʬMULTIPLIED_20_WITH 10",
            "10 TIMES 10 AS Employees, 10 ʬarithmoperatorʬmultiplyʬTIMES 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬmultiplyʬTIMES 10, 10, ʬarithmoperatorʬmultiplyʬTIMES 10",
            "10 / 10 AS Employees, 10 ʬarithmoperatorʬdivideʬ_2f_ 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬdivideʬ_2f_ 10, 10, ʬarithmoperatorʬdivideʬ_2f_ 10",
            "10 DIVIDED BY 10 AS Employees, 10 ʬarithmoperatorʬdivideʬDIVIDED_20_BY 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬdivideʬDIVIDED_20_BY 10, 10, ʬarithmoperatorʬdivideʬDIVIDED_20_BY 10",
            "10 MOD 10 AS Employees, 10 ʬarithmoperatorʬmoduloʬMOD 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬmoduloʬMOD 10, 10, ʬarithmoperatorʬmoduloʬMOD 10",
            "10 MODULO 10 AS Employees, 10 ʬarithmoperatorʬmoduloʬMODULO 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬmoduloʬMODULO 10, 10, ʬarithmoperatorʬmoduloʬMODULO 10",
            "10 ^ 10 AS Employees, 10 ʬarithmoperatorʬpowerʬ_5e_ 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬpowerʬ_5e_ 10, 10, ʬarithmoperatorʬpowerʬ_5e_ 10",
            "10 TO THE POWER OF 10 AS Employees, 10 ʬarithmoperatorʬpowerʬTO_20_THE_20_POWER_20_OF 10 ʬasʬAS Employees, 10 ʬarithmoperatorʬpowerʬTO_20_THE_20_POWER_20_OF 10, 10, ʬarithmoperatorʬpowerʬTO_20_THE_20_POWER_20_OF 10"
    )
    fun arithmetic_operators_as_variable(paramStr: String, expectedsource: String, expectedArithmeticSource: String, expectedFirstNumberSource: String, expectedSecondNumberSource: String){
        var input = """
            $paramStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.variables()
                .hasSizeOf(1)
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedsource)
                .arithmetic()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedArithmeticSource)
                .operation()
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedFirstNumberSource)
                .parentOperation()
                .second()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedSecondNumberSource)
        }
    }

    @ParameterizedTest
    @CsvSource(
            "IF Value GREATER 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_thanʬGREATER 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_thanʬGREATER 1",
            "IF Value BIGGER 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_thanʬBIGGER 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_thanʬBIGGER 1",
            "IF Value LARGER 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_thanʬLARGER 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_thanʬLARGER 1",
            "IF Value MORE 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_thanʬMORE 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_thanʬMORE 1",
            "IF Value EXCEEDS 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_thanʬEXCEEDS 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_thanʬEXCEEDS 1",
            "IF Value EXCEED 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_thanʬEXCEED 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_thanʬEXCEED 1",
            "IF Value HIGHER 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_thanʬHIGHER 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_thanʬHIGHER 1",
            "IF Value IS 1 THEN error, ʬifʬIF Value ʬoperatorʬequalsʬIS 1 ʬthenʬTHEN error, Value ʬoperatorʬequalsʬIS 1",
            "IF Value EQUAL 1 THEN error, ʬifʬIF Value ʬoperatorʬequalsʬEQUAL 1 ʬthenʬTHEN error, Value ʬoperatorʬequalsʬEQUAL 1",
            "IF Value EQUALS 1 THEN error, ʬifʬIF Value ʬoperatorʬequalsʬEQUALS 1 ʬthenʬTHEN error, Value ʬoperatorʬequalsʬEQUALS 1",
            "IF Value ISN'T 1 THEN error, ʬifʬIF Value ʬoperatorʬnot_equalsʬISN_27_T 1 ʬthenʬTHEN error, Value ʬoperatorʬnot_equalsʬISN_27_T 1",
            "IF Value IS NOT 1 THEN error, ʬifʬIF Value ʬoperatorʬnot_equalsʬIS_20_NOT 1 ʬthenʬTHEN error, Value ʬoperatorʬnot_equalsʬIS_20_NOT 1",
            "IF Value NOT EQUAL 1 THEN error, ʬifʬIF Value ʬoperatorʬnot_equalsʬNOT_20_EQUAL 1 ʬthenʬTHEN error, Value ʬoperatorʬnot_equalsʬNOT_20_EQUAL 1",
            "IF Value NOT 1 THEN error, ʬifʬIF Value ʬoperatorʬnot_equalsʬNOT 1 ʬthenʬTHEN error, Value ʬoperatorʬnot_equalsʬNOT 1",
            "IF Value LESS 1 THEN error, ʬifʬIF Value ʬoperatorʬless_thanʬLESS 1 ʬthenʬTHEN error, Value ʬoperatorʬless_thanʬLESS 1",
            "IF Value SMALLER 1 THEN error, ʬifʬIF Value ʬoperatorʬless_thanʬSMALLER 1 ʬthenʬTHEN error, Value ʬoperatorʬless_thanʬSMALLER 1",
            "IF Value LOWER 1 THEN error, ʬifʬIF Value ʬoperatorʬless_thanʬLOWER 1 ʬthenʬTHEN error, Value ʬoperatorʬless_thanʬLOWER 1",
            "IF Value FEWER 1 THEN error, ʬifʬIF Value ʬoperatorʬless_thanʬFEWER 1 ʬthenʬTHEN error, Value ʬoperatorʬless_thanʬFEWER 1",
            "IF Value SHORTER 1 THEN error, ʬifʬIF Value ʬoperatorʬless_thanʬSHORTER 1 ʬthenʬTHEN error, Value ʬoperatorʬless_thanʬSHORTER 1",
            "IF Value GREATER OR EQUAL 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUAL 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUAL 1",
            "IF Value GREATER OR EQUALS 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUALS 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUALS 1",
            "IF Value LEAST 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_or_equalsʬLEAST 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_or_equalsʬLEAST 1",
            "IF Value AT LEAST 1 THEN error, ʬifʬIF Value ʬoperatorʬgreater_or_equalsʬAT_20_LEAST 1 ʬthenʬTHEN error, Value ʬoperatorʬgreater_or_equalsʬAT_20_LEAST 1"
    )
    fun rule_operators_in_condition(paramStr: String, expectedSource: String, expectedConditionSource: String){
        var input = """
            1 AS Value
            
            $paramStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedSource)
                .condition()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedConditionSource)
        }
    }

    @ParameterizedTest
    @CsvSource(
            "IF Value IS 1 THEN error ERRORCODE 1, ʬifʬIF Value ʬoperatorʬequalsʬIS 1 ʬthenʬTHEN error ʬerrorcodeʬERRORCODE 1",
            "IF Value IS 1 THEN error WITH ERRORCODE 1, ʬifʬIF Value ʬoperatorʬequalsʬIS 1 ʬthenʬTHEN error ʬerrorcodeʬWITH_20_ERRORCODE 1",
            "IF Value IS 1 THEN error WITH CODE 1, ʬifʬIF Value ʬoperatorʬequalsʬIS 1 ʬthenʬTHEN error ʬerrorcodeʬWITH_20_CODE 1",
            "IF Value IS 1 THEN error WITH ERROR 1, ʬifʬIF Value ʬoperatorʬequalsʬIS 1 ʬthenʬTHEN error ʬerrorcodeʬWITH_20_ERROR 1"
            
    )
    fun errorcode(paramStr: String, expectedsource: String){
        var input = """
            1 AS Value
            
            $paramStr
        """

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedsource)
        }
    }

    @Test
    fun conditionGroup_2_children(){
        var input = 
"""
1 AS Value

IF Value IS 1 
    OR Value IS 16
THEN error
"""
        var expectedSource =
"""ʬifʬIF Value ʬoperatorʬequalsʬIS 1 
    ʬorʬOR Value ʬoperatorʬequalsʬIS 16
ʬthenʬTHEN error"""
        
        var expectedConditionGroupSource =
"""Value ʬoperatorʬequalsʬIS 1 
    ʬorʬOR Value ʬoperatorʬequalsʬIS 16"""

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .hasPreprocessedSource()
                    .hasPreprocessedSource(expectedSource)
                    .conditionGroup()
                        .hasPreprocessedSource(expectedConditionGroupSource)
                        .first()
                            .hasPreprocessedSource("Value ʬoperatorʬequalsʬIS 1")
                    .parentConditionGroup()
                        .second()
                            .hasPreprocessedSource("ʬorʬOR Value ʬoperatorʬequalsʬIS 16")
                
        }
    }

    @Test
    fun conditionGroup_subgroup(){
        var input =
                """
1 AS Value

IF Value IS 1
    OR Value IS 16
        AND Value IS NOT 1024
THEN error
"""
        var expectedSource =
                """ʬifʬIF Value ʬoperatorʬequalsʬIS 1
    ʬorʬOR Value ʬoperatorʬequalsʬIS 16
        ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024
ʬthenʬTHEN error"""

        var expectedConditionGroupSource =
                """Value ʬoperatorʬequalsʬIS 1
    ʬorʬOR Value ʬoperatorʬequalsʬIS 16
        ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024"""

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedSource)
                .conditionGroup()
                .hasPreprocessedSource(expectedConditionGroupSource)
                .first()
                .hasPreprocessedSource("Value ʬoperatorʬequalsʬIS 1")
                .parentConditionGroup()
                .firstConditionGroup()
                .hasPreprocessedSource("ʬorʬOR Value ʬoperatorʬequalsʬIS 16\n        ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024")

        }
    }

    @Test
    fun conditionGroup_multiple_subgroups(){
        var input =
                """
1 AS Value

IF Value IS 1
    AND Value IS NOT 128
OR Value IS 16
    AND Value IS NOT 1024
OR Value IS NOT 2048
THEN error
"""
        var expectedSource = """ʬifʬIF Value ʬoperatorʬequalsʬIS 1
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 128
ʬorʬOR Value ʬoperatorʬequalsʬIS 16
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024
ʬorʬOR Value ʬoperatorʬnot_equalsʬIS_20_NOT 2048
ʬthenʬTHEN error"""

        var expectedConditionGroupSource = """Value ʬoperatorʬequalsʬIS 1
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 128
ʬorʬOR Value ʬoperatorʬequalsʬIS 16
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024
ʬorʬOR Value ʬoperatorʬnot_equalsʬIS_20_NOT 2048"""

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedSource)
                .conditionGroup()
                .hasPreprocessedSource(expectedConditionGroupSource)
                .first()
                .hasPreprocessedSource("ʬorʬOR Value ʬoperatorʬnot_equalsʬIS_20_NOT 2048")
                .parentConditionGroup()
                .firstConditionGroup()
                .hasPreprocessedSource("Value ʬoperatorʬequalsʬIS 1\n" +
                        "    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 128")
                .parentConditionGroup()
                .secondConditionGroup()
                .hasPreprocessedSource("ʬorʬOR Value ʬoperatorʬequalsʬIS 16\n    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024")

        }
    }

    @Test
    fun conditionGroup_three_layers_subgroup(){
        var input =
                """
1 AS Value

IF Value IS 1
    AND Value IS NOT 128
    OR Value IS 2
        AND Value IS NOT 42
OR Value IS 16
    AND Value IS NOT 1024
OR Value IS NOT 2048
THEN error
"""
        var expectedSource = """ʬifʬIF Value ʬoperatorʬequalsʬIS 1
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 128
    ʬorʬOR Value ʬoperatorʬequalsʬIS 2
        ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 42
ʬorʬOR Value ʬoperatorʬequalsʬIS 16
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024
ʬorʬOR Value ʬoperatorʬnot_equalsʬIS_20_NOT 2048
ʬthenʬTHEN error"""

        var expectedConditionGroupSource = """Value ʬoperatorʬequalsʬIS 1
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 128
    ʬorʬOR Value ʬoperatorʬequalsʬIS 2
        ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 42
ʬorʬOR Value ʬoperatorʬequalsʬIS 16
    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024
ʬorʬOR Value ʬoperatorʬnot_equalsʬIS_20_NOT 2048"""

        End2AstRunner.run(input, "{}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .hasPreprocessedSource()
                .hasPreprocessedSource(expectedSource)
                .conditionGroup()
                .hasPreprocessedSource(expectedConditionGroupSource)
                .first()
                .hasPreprocessedSource("ʬorʬOR Value ʬoperatorʬnot_equalsʬIS_20_NOT 2048")
                .parentConditionGroup()
                .firstConditionGroup()
                .hasPreprocessedSource("Value ʬoperatorʬequalsʬIS 1\n" +
                        "    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 128\n" +
                        "    ʬorʬOR Value ʬoperatorʬequalsʬIS 2\n" +
                        "        ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 42")
                .hasSize(3)
                .first()
                .hasPreprocessedSource("Value ʬoperatorʬequalsʬIS 1")
                .parentConditionGroup()
                .second()
                .hasPreprocessedSource("ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 128")
                .parentConditionGroup()
                .firstConditionGroup()
                .hasPreprocessedSource("ʬorʬOR Value ʬoperatorʬequalsʬIS 2\n        ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 42")
                .parentConditionGroup()
                .parentConditionGroup()
                .secondConditionGroup()
                .hasPreprocessedSource("ʬorʬOR Value ʬoperatorʬequalsʬIS 16\n    ʬandʬAND Value ʬoperatorʬnot_equalsʬIS_20_NOT 1024")

        }
    }


//TODO TB 17.05.2019: Prüfen, ob in der source1 wirklich "5 things" statt "5" stehen soll, bzw. "3 stuff" statt "3" in source3 (s.u.)
    @ParameterizedTest
    @CsvSource(
          "5 * 6 / 3, 5, ʬarithmoperatorʬmultiplyʬ_2a_ 6, ʬarithmoperatorʬdivideʬ_2f_ 3"
        , "5 TIMES 6 / 3, 5, ʬarithmoperatorʬmultiplyʬTIMES 6, ʬarithmoperatorʬdivideʬ_2f_ 3"
        , "5 * 6 DIVIDED BY 3, 5, ʬarithmoperatorʬmultiplyʬ_2a_ 6, ʬarithmoperatorʬdivideʬDIVIDED_20_BY 3"
        , "5 TIMES 6 DIVIDED BY 3, 5, ʬarithmoperatorʬmultiplyʬTIMES 6, ʬarithmoperatorʬdivideʬDIVIDED_20_BY 3"
        , "5 things * 6 things / 3 stuff, 5 things, ʬarithmoperatorʬmultiplyʬ_2a_ 6 things, ʬarithmoperatorʬdivideʬ_2f_ 3 stuff"
        , "5 things TIMES 6 things DIVIDED BY 3 stuff, 5 things, ʬarithmoperatorʬmultiplyʬTIMES 6 things, ʬarithmoperatorʬdivideʬDIVIDED_20_BY 3 stuff"
    )
    @Throws(Exception::class)
    fun multiplication_division_asterisk_slash_spaces(paramStr: String, source1: String, source2: String, source3: String) {
        val input = """
            $paramStr AS more stuff
            """

        End2AstRunner.run(input, "{}") { r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .arithmetic()
                            .hasPreprocessedSource()
                            .operation()
                                .hasSizeOf(3)
                                .first()
                                    .hasPreprocessedSource(source1)
                            .parentOperation()
                                .second()
                                    .hasPreprocessedSource(source2)
                            .parentOperation()
                                .atIndex(2)
                                    .hasPreprocessedSource(source3)
        }
    }


//    TODO: TB 18.04.19: letzter Test mit 'new employee' am Ende funktioniert nicht
//    TODO TB 27.05.19: Bei Test 2 steht für leftVariable nicht "Some Status of someone" drin, sondern nur "Status". Der Rest "Some..of someone" geht verloren.
    @ParameterizedTest
    @CsvSource(
         "Status MUST be new, new ʬasʬAS Status, Status, be new"  // new ʬasʬAS StatusʬparagraphʬStatus ʬconstraintʬmustʬMUST be new
        ,"Some Status of someone SHOULD be new, new ʬasʬAS Status, Some Status ʬofʬof someone, be new" //  new ʬasʬAS StatusʬparagraphʬSome Status of someone ʬconstraintʬmustʬSHOULD be new
        ,"Status HAS to be new, new ʬasʬAS Status, Status, to be new"  // new ʬasʬAS StatusʬparagraphʬStatus ʬconstraintʬmustʬHAS to be new
        ,"Status HAVE to be new, new ʬasʬAS Status, Status, to be new"  // new ʬasʬAS StatusʬparagraphʬStatus ʬconstraintʬmustʬHAVE to be new
        ,"the Status of a person MUST be a new, new ʬasʬAS Status, the Status ʬofʬof a person, be a new"  //  new ʬasʬAS Statusʬparagraphʬthe Status of a person ʬconstraintʬmustʬMUST be a new
//        ,"A Status SHOULD new employee, new ʬasʬAS Status, Status, be new"
        ,"The Status HAS to be the new, new ʬasʬAS Status, The Status, to be the new"  // new ʬasʬAS StatusʬparagraphʬThe Status ʬconstraintʬmustʬHAS to be the new
//        ,"The Status HAVE to be a new employee, new ʬasʬAS Status, Status, be a new"
        ,"Status MUST EQUALS new, new ʬasʬAS Status, Status, new"  // new ʬasʬAS StatusʬparagraphʬStatus ʬconstraintʬmustʬMUST ʬoperatorʬequalsʬEQUALS new
    )
    fun source_constraint_must_variable_string(paramStr: String, source1: String, source2: String, source3: String) {
        var input = """
            new AS Status

            $paramStr
            """

//        PreProcessorUtils.preProcStr2Sysout(input, Locale("en"));

        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .hasPreprocessedSource()
                            .leftVariable()
                                .hasPreprocessedSource(source2) // ist nicht "Some Status of someone"
                        .parentCondition()
                            .rightString()
                                .hasPreprocessedSource(source3) // ist nicht "ʬconstraintʬmustʬSHOULD be new"
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource(source1)
        }
    }


    @ParameterizedTest
    @CsvSource(
         "Employees MUST GREATER 0, Employees, 0" // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬMUST ʬoperatorʬgreater_thanʬGREATER 0
        ,"Employees SHOULD be GREATER THAN 0, Employees, 0" // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬSHOULD be ʬoperatorʬgreater_thanʬGREATER THAN 0
        ,"Employees HAS to be LESS 0, Employees, 0"  // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬHAS to be ʬoperatorʬless_thanʬLESS 0
        ,"Employees HAVE to be LESS OR EQUAL the number 0, Employees, the number 0"  // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬHAVE to be ʬoperatorʬless_or_equalsʬLESS_20_OR_20_EQUAL the number 0
        ,"the Employees MUST GREATER OR EQUALS 0 stuff, the Employees, 0 stuff"  // 2 ʬasʬAS Employeesʬparagraphʬthe Employees ʬconstraintʬmustʬMUST ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUALS 0 stuff
        ,"All Employees SHOULD be SMALLER THAN 0, All Employees, 0"  // 2 ʬasʬAS EmployeesʬparagraphʬAll Employees ʬconstraintʬmustʬSHOULD be ʬoperatorʬless_thanʬSMALLER THAN 0
        ,"The Employees HAS to be BIGGER THAN the 0, The Employees, the 0"  // 2 ʬasʬAS EmployeesʬparagraphʬThe Employees ʬconstraintʬmustʬHAS to be ʬoperatorʬgreater_thanʬBIGGER THAN the 0
        ,"The Employees HAVE to be LARGER 0 in number, The Employees, 0 in number"  // 2 ʬasʬAS EmployeesʬparagraphʬThe Employees ʬconstraintʬmustʬHAVE to be ʬoperatorʬgreater_thanʬLARGER 0 in number
        ,"Employees MUST be LESS OR EQUAL 0, Employees, 0 "  // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬMUST be ʬoperatorʬless_or_equalsʬLESS_20_OR_20_EQUAL 0
        ,"Employees SHOULD be MORE THAN 0, Employees, 0"  // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬSHOULD be ʬoperatorʬgreater_thanʬMORE THAN 0
        ,"Employees HAS to be AT MOST 0, Employees, 0"  // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬHAS to be AT ʬoperatorʬless_or_equalsʬMOST 0
        ,"Employees HAVE to be AT LEAST the number 0, Employees, the number 0"  // 2 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustʬHAVE to be ʬoperatorʬgreater_or_equalsʬAT_20_LEAST the number 0
        ,"the Employees MUST be FEWER THAN 0 people, the Employees, 0 people"  // 2 ʬasʬAS Employeesʬparagraphʬthe Employees ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬFEWER THAN 0 people
        ,"All Employees SHOULD FEWER THAN 0, All Employees, 0"  // 2 ʬasʬAS EmployeesʬparagraphʬAll Employees ʬconstraintʬmustʬSHOULD ʬoperatorʬless_thanʬFEWER THAN 0
        ,"The Employees HAS to be BIGGER THAN 0, The Employees, 0"  // 2 ʬasʬAS EmployeesʬparagraphʬThe Employees ʬconstraintʬmustʬHAS to be ʬoperatorʬgreater_thanʬBIGGER THAN 0
        ,"The Employees HAVE to SMALLER THAN a 0 in number, The Employees, a 0 in number"  // 2 ʬasʬAS EmployeesʬparagraphʬThe Employees ʬconstraintʬmustʬHAVE to ʬoperatorʬless_thanʬSMALLER THAN a 0 in number
    )
    fun source_constraint_must_variable_number_relational(paramStr: String, source1: String, source2: String) {
        var input = """
            2 AS Employees

            $paramStr
            """

//        PreProcessorUtils.preProcStr2Sysout(input, Locale("en"));

        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .condition()
                            .leftVariable()
                                .hasPreprocessedSource(source1)
                        .parentCondition()
                            .rightNumber()
                                .hasPreprocessedSource(source2)
            .parentModel()
                .variables()
                    .hasSizeOf(1)
                        .first()
                            .hasPreprocessedSource()
                }
    }


    @ParameterizedTest
    @CsvSource(
         "Employees MUST NOT be FEWER THAN 0, Employees, 0"  // 0 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustnotʬMUST_20_NOT be ʬoperatorʬless_thanʬFEWER THAN 0
        ,"Employees SHOULD NOT be LESS THAN 0, Employees, 0"  // 0 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustnotʬSHOULD_20_NOT be ʬoperatorʬless_thanʬLESS THAN 0
        ,"Employees HAS NOT to be SMALLER THAN 0, Employees, 0"  // 0 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustnotʬHAS_20_NOT to be ʬoperatorʬless_thanʬSMALLER THAN 0
        ,"Employees HAVE NOT to be LARGER than the number 0, Employees, the number 0"  // 0 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustnotʬHAVE_20_NOT to be ʬoperatorʬgreater_thanʬLARGER than the number 0
        ,"the Employees MUST NOT EXCEED 0 people, the Employees, 0 people"  // 0 ʬasʬAS Employeesʬparagraphʬthe Employees ʬconstraintʬmustnotʬMUST_20_NOT ʬoperatorʬgreater_thanʬEXCEED 0 people
        ,"All Employees SHOULD NOT LESS OR EQUAL a 0, All Employees, a 0"  // 0 ʬasʬAS EmployeesʬparagraphʬAll Employees ʬconstraintʬmustnotʬSHOULD_20_NOT ʬoperatorʬless_or_equalsʬLESS_20_OR_20_EQUAL a 0
        ,"The Employees HAS NOT to be AT MOST the 0, The Employees, the 0"  // 0 ʬasʬAS EmployeesʬparagraphʬThe Employees ʬconstraintʬmustnotʬHAS_20_NOT to be AT ʬoperatorʬless_or_equalsʬMOST the 0
        ,"The Employees HAVE NOT to be a GREATER OR EQUALS 0 in number, The Employees, 0 in number"  //  0 ʬasʬAS EmployeesʬparagraphʬThe Employees ʬconstraintʬmustnotʬHAVE_20_NOT to be a ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUALS 0 in number
        ,"Employees MUSTN'T be AT LEAST 0 for our company, Employees, 0 for our company"  // 0 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustnotʬMUSTN_27_T be ʬoperatorʬgreater_or_equalsʬAT_20_LEAST 0 for our company
        ,"Employees SHOULDN'T be GREATER THAN 0, Employees, 0"  // 0 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustnotʬSHOULDN_27_T be ʬoperatorʬgreater_thanʬGREATER THAN 0
        ,"The Employees of our company HASN'T to be MORE THAN 0, The Employees of our company, 0"  // 0 ʬasʬAS EmployeesʬparagraphʬThe Employees of our company ʬconstraintʬmustnotʬHASN_27_T to be ʬoperatorʬgreater_thanʬMORE THAN 0
        ,"Employees HAVEN'T to be SMALLER THAN the number 0, Employees, the number 0"  // 0 ʬasʬAS EmployeesʬparagraphʬEmployees ʬconstraintʬmustnotʬHAVEN_27_T to be ʬoperatorʬless_thanʬSMALLER THAN the number 0
        ,"the Employees people MUSTN'T be MORE THAN 0 people, the Employees people, 0 people"  // 0 ʬasʬAS Employeesʬparagraphʬthe Employees people ʬconstraintʬmustnotʬMUSTN_27_T be ʬoperatorʬgreater_thanʬMORE THAN 0 people
    )
    fun constraint_mustnot_variable_number_relational(paramStr: String, source1: String, source2: String) {
        var input = """
            0 AS Employees

            $paramStr
            """

//        PreProcessorUtils.preProcStr2Sysout(input, Locale("en"));

        End2AstRunner.run(input, "{}") {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .condition()
                            .leftVariable()
                                .hasOriginalSource(source1)
                        .parentCondition()
                            .rightNumber()
                                .hasPreprocessedSource(source2)
            .parentModel()
                .variables()
                    .first()
                        .hasPreprocessedSource()
        }
    }


    @ParameterizedTest
    @CsvSource(
             "NumVar MUST be AT LEAST 10 AND NumVar MUST be AT MOST 50 OR NumVar MUST be LESS THAN 0, NumVar, 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬNumVar ʬconstraintʬmustʬMUST be ʬoperatorʬgreater_or_equalsʬAT_20_LEAST 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be AT ʬoperatorʬless_or_equalsʬMOST 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"Some NumVar of someone SHOULD be GREATER OR EQUAL 10 AND NumVar MUST LESS OR EQUALS 50 OR NumVar MUST be LESS OR EQUAL 0, Some NumVar of someone, 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬSome NumVar of someone ʬconstraintʬmustʬSHOULD be ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUAL 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST ʬoperatorʬless_or_equalsʬLESS_20_OR_20_EQUALS 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_or_equalsʬLESS_20_OR_20_EQUAL 0
            ,"NumVar HAS to be GREATER THAN 10 AND NumVar MUST be BIGGER THAN 50 OR NumVar MUST be LESS THAN 0, NumVar, 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬNumVar ʬconstraintʬmustʬHAS to be ʬoperatorʬgreater_thanʬGREATER THAN 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬgreater_thanʬBIGGER THAN 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"NumVar HAVE to be MORE THAN 10 AND NumVar MUST EXCEED 50 OR NumVar MUST be AT MOST 0, NumVar, 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬNumVar ʬconstraintʬmustʬHAVE to be ʬoperatorʬgreater_thanʬMORE THAN 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST ʬoperatorʬgreater_thanʬEXCEED 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be AT ʬoperatorʬless_or_equalsʬMOST 0
            ,"the NumVar of a person MUST be SMALLER THAN 10 AND NumVar MUST be LESS THAN 50 OR NumVar MUST be LESS THAN 0, the NumVar of a person, 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬthe NumVar of a person ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬSMALLER THAN 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"A NumVar SHOULD be SMALLER than 10 AND NumVar MUST be SMALLER THAN 50 OR NumVar MUST be LESS THAN 0, A NumVar, 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬA NumVar ʬconstraintʬmustʬSHOULD be ʬoperatorʬless_thanʬSMALLER than 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬSMALLER THAN 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"The NumVar HAS to be LOWER THAN 10 AND NumVar MUST be FEWER THAN 50 OR NumVar MUST be LESS THAN 0, The NumVar, 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustʬHAS to be ʬoperatorʬless_thanʬLOWER THAN 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬFEWER THAN 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"The NumVar HAVE to be LESS OR EQUALS the 10 AND NumVar MUST be LOWER THAN 50 OR NumVar MUST be LESS THAN 0, The NumVar, the 10, 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustʬHAVE to be ʬoperatorʬless_or_equalsʬLESS_20_OR_20_EQUALS the 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLOWER THAN 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"Variable NumVar MUST EQUALS the number 10 AND the variable NumVar MUST NOT EQUALS the critical 50 OR NumVar MUST be LESS THAN 0 in number, Variable NumVar, the number 10, the critical 50, 0 in number"  // 15 ʬasʬAS NumVarʬparagraphʬVariable NumVar ʬconstraintʬmustʬMUST ʬoperatorʬequalsʬEQUALS the number 10 ʬandʬAND the variable NumVar ʬconstraintʬmustʬMUST ʬoperatorʬnot_equalsʬNOT_20_EQUALS the critical 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0 in number
            ,"The NumVar MUST NOT EQUALS the forbidden number 10 AND the NumVar MUST be GREATER THAN the number 50 OR NumVar MUST be LESS THAN 0, The NumVar, the forbidden number 10, the number 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustʬMUST ʬoperatorʬnot_equalsʬNOT_20_EQUALS the forbidden number 10 ʬandʬAND the NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬgreater_thanʬGREATER THAN the number 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"The NumVar SHOULD BE LESS THAN a number 10 AND NumVar SHOULD NOT EQUALS the number 50 OR NumVar MUST be LESS THAN 0, The NumVar, a number 10, the number 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustʬSHOULD BE ʬoperatorʬless_thanʬLESS THAN a number 10 ʬandʬAND NumVar ʬconstraintʬmustʬSHOULD ʬoperatorʬnot_equalsʬNOT_20_EQUALS the number 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"The NumVar HAVE NOT to be EQUALS the forbidden number 10 AND NumVar MUST be AT LEAST the number 50 OR NumVar MUST be LESS THAN 0, The NumVar, the forbidden number 10, the number 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustnotʬHAVE_20_NOT to be ʬoperatorʬequalsʬEQUALS the forbidden number 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬgreater_or_equalsʬAT_20_LEAST the number 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"The NumVar HAVE TO BE SMALLER THAN a number 10 AND NumVar MUSTN'T EQUALS the number 50 OR NumVar MUST be LESS THAN 0, The NumVar, a number 10, the number 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustʬHAVE TO BE ʬoperatorʬless_thanʬSMALLER THAN a number 10 ʬandʬAND NumVar ʬconstraintʬmustnotʬMUSTN_27_T ʬoperatorʬequalsʬEQUALS the number 50 ʬorʬOR NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬless_thanʬLESS THAN 0
            ,"The NumVar SHOULDN'T be EQUALS the forbidden number 10 AND NumVar MUST be GREATER OR EQUALS the number 50 OR NumVar SHOULDN'T be GREATER THAN 0, The NumVar, the forbidden number 10, the number 50, 0"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustnotʬSHOULDN_27_T be ʬoperatorʬequalsʬEQUALS the forbidden number 10 ʬandʬAND NumVar ʬconstraintʬmustʬMUST be ʬoperatorʬgreater_or_equalsʬGREATER_20_OR_20_EQUALS the number 50 ʬorʬOR NumVar ʬconstraintʬmustnotʬSHOULDN_27_T be ʬoperatorʬgreater_thanʬGREATER THAN 0
            ,"Variable NumVar HAS TO EQUALS the number 10 AND the variable NumVar HAVE be LOWER than the critical 50 OR NumVar HASN'T be AT LEAST 0 in number, Variable NumVar, the number 10, the critical 50, 0 in number"  // 15 ʬasʬAS NumVarʬparagraphʬVariable NumVar ʬconstraintʬmustʬHAS TO ʬoperatorʬequalsʬEQUALS the number 10 ʬandʬAND the variable NumVar ʬconstraintʬmustʬHAVE be ʬoperatorʬless_thanʬLOWER than the critical 50 ʬorʬOR NumVar ʬconstraintʬmustnotʬHASN_27_T be ʬoperatorʬgreater_or_equalsʬAT_20_LEAST 0 in number
            ,"The NumVar MUSTN'T be EQUALS the number 10 AND NumVar HAS TO be AT MOST 50 OR NumVar MUSTN'T be HIGHER THAN 0 in number, The NumVar, the number 10, 50, 0 in number"  // 15 ʬasʬAS NumVarʬparagraphʬThe NumVar ʬconstraintʬmustnotʬMUSTN_27_T be ʬoperatorʬequalsʬEQUALS the number 10 ʬandʬAND NumVar ʬconstraintʬmustʬHAS TO be AT ʬoperatorʬless_or_equalsʬMOST 50 ʬorʬOR NumVar ʬconstraintʬmustnotʬMUSTN_27_T be ʬoperatorʬgreater_thanʬHIGHER THAN 0 in number
    )
    fun source_constraints_and_or_number_variable_static_number(paramStr: String, source1: String, source2: String, source3: String, source4: String) {
        var input = """
            15 AS NumVar

            $paramStr
            """

//        PreProcessorUtils.preProcStr2Sysout(input, Locale("en"));

        End2AstRunner.run(input, "{}") {
            r ->
                r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
            .parentModel()
                .rules()
                    .first()
                        .hasPreprocessedSource()
                        .conditionGroup()
                            .hasPreprocessedSource()
                            .first()
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasOriginalSource(source1)
                            .parentCondition()
                                .rightNumber()
                                    .hasOriginalSource(source2)
                        .parentConditionGroup()
                            .second()
                                .hasPreprocessedSource()
                                .leftVariable()
                                    .hasOriginalSource()
                            .parentCondition()
                                .rightNumber()
                                    .hasOriginalSource(source3)
                        .parentConditionGroup()
                            .atIndex(2)
                                .leftVariable()
                                    .hasPreprocessedSource()
                            .parentCondition()
                                .rightNumber()
                                    .hasOriginalSource(source4)
        }


    }


    @Test
    fun source_array_sum_of() {
        var input = """SUM OF customer.price AS value"""

        var schema = """
            {
                customer: {
                    price: number,
                    name: string
                }
            }
        """
//        PreProcessorUtils.preProcStr2Sysout(input, Locale("en"));

        End2AstRunner.run(input, schema) {
            r -> r.variables()
                    .hasSizeOf(1)
                    .first()
                .hasPreprocessedSource("ʬfunctionʬsum_ofʬSUM_20_OF customer.price ʬasʬAS value")
                .hasOriginalSource(input)
        }
    }

}
