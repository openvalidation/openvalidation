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
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ErrorTest {

    @Test
    fun error_condition_number_variable() {
        var input = """
            5 AS var1

            IF var1 GREATER THAN 3 THEN Variable is greater
            """

        End2AstRunner.run(input, "{}") {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError("Variable is greater") }
    }

    @Test
    fun error_condition_2variables() {
        var input = """
            5 AS var1

            3 AS var2

            IF var1 LESS THAN var2 THEN First variable is smaller
            """

        End2AstRunner.run(input, "{}") {
                r -> r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasError("First variable is smaller") }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "ERRORCODE",
        "WITH ERRORCODE",
        "WITH CODE",
        "WITH ERROR"
    ])
    @Throws(Exception::class)
    fun error_with_errorcode(paramStr : String) {
        var input =
                """
                     25 AS var1

                    IF var1 EQUALS 25
                    THEN not supported
                    $paramStr 3000
                """
        var schema = "{}"

        End2AstRunner.run(input, schema) {
            r ->
            r.variables()
                    .hasSizeOf(1)
                    .first()
                    .hasName("var1")
                    .hasNumber(25.0)

            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .condition()
                    .leftVariable()
                    .hasName("var1")
                    .parentCondition()
                    .rightNumber()
                    .hasValue(25.0)
                    .parentCondition()
                    .hasOperator(ASTComparisonOperator.EQUALS)
                    .parentRule()
                    .hasError("not supported")
                    .hasErrorCode(3000)
        }
    }
    
    @Test
    fun multiple_errors_no_errorcode_in_errormessage(){
        var input =
"""
if name is validaria
then validariaerror
with errorcode 1

if name is otto
then ottoerror
with errorcode 2
"""
        var schema = "{name:'',addresses:[{country:''}]}"

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(2)
                    .first()
                        .hasError("validariaerror")
                        .hasErrorCode(1)
            r.rules()
                    .hasSizeOf(2)
                    .second()
                        .hasError("ottoerror")
                        .hasErrorCode(2)
        }
    }

}