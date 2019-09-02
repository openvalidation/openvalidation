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

internal class FunctionTest {

    @Test
    @Throws(Exception::class)
    fun shopping_list_function() {
        var input =
                """
                     SUM OF shopping_list.price
                         AS expenses
                """
        var schema =
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
        
        End2AstRunner.run(input, schema) { r ->
            r.variables()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .hasName("expenses")
                        .function("SUM_OF")
                            .hasPreprocessedSource()
                            .firstArrayOfFunction()
                                .firstProperty("shopping_list")
                            .parentFunction()
                                .secondPropertyLambda("price")
        }
    }

    
    //todo jgeske 23.04.19 this section requires some adjustment, with the correct usages of the function below
    
    
    //currently not implemented
    @Test
    @Disabled
    @Throws(Exception::class)
    fun contains_all() {
        var input =
                """
                     CONTAINS ALL shopping_list.price
                         AS expenses
                """
        var schema =
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
                        .hasPreprocessedSource()
                        .hasName("expenses")
                        .function()
                            .hasPreprocessedSource()
                            .hasName("CONTAINS ALL") }
    }

    //currently not implemented
    @Test
    @Disabled
    @Throws(Exception::class)
    fun contains_one_of() {
        var input =
                """
                     CONTAINS ONE OF shopping_list.price
                                  AS expenses
                """
        var schema =
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
                        .hasPreprocessedSource()
                        .hasName("expenses")
                        .function()
                            .hasPreprocessedSource()
                            .hasName("CONTAINS ONE OF") }
    }

    //currently not implemented
    @Test
    @Disabled
    @Throws(Exception::class)
    fun contains_none_of() {
        var input =
                """
                     CONTAINS NONE OF shopping_list.price
                                  AS expenses
                """
        var schema =
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
                        .hasPreprocessedSource()
                        .hasName("expenses")
                        .function()
                            .hasPreprocessedSource()
                            .hasName("CONTAINS NONE OF") }
    }

    //currently not implemented
    @Test
    @Disabled
    @Throws(Exception::class)
    fun contains() {
        var input =
                """
                     CONTAINS shopping_list.price
                                  AS expenses
                """
        var schema =
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
                        .hasPreprocessedSource()
                        .hasName("expenses")
                        .function()
                            .hasPreprocessedSource()
                            .hasName("CONTAINS") }
    }
}