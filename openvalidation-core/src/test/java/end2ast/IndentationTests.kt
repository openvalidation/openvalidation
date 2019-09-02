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

import io.openvalidation.common.ast.condition.ASTConditionConnector
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class IndentationTests {

    @Test
    @Throws(Exception::class)
    fun indentation_2_layers() {
        var input =
                """
                    IF a LESS b
                        OR b GREATER a
                    THEN whatever
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
                r.rules()
                    .hasSizeOf(1)
                    .first()
                        .hasPreprocessedSource()
                        .conditionGroup()
                            .hasSize(2)
                           .first()
                                .hasIndentationLevel(0)
                        .parentConditionGroup()
                            .second()
                                .hasIndentationLevel(24)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_2_layers_depth_8() {
        var input =
                """
                    IF a LESS b
                            OR b GREATER a
                    THEN whatever
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
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
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(28)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_3_layers_mid_indented() {
        var input =
                """
                    IF a LESS b
                            OR b GREATER a
                    AND c EQUALS d
                    THEN whatever
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .conditionGroup()
                    .hasSize(2)
                    .firstConditionGroup()
                    .hasSize(2)
                    .first()
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(28)
                    .parentConditionGroup()
                    .parentConditionGroup()
                    .first()
                    .hasIndentationLevel(20)
        }
    }
    
    @Test
    @Throws(Exception::class)
    fun indentation_4_layers_flat_grouping() {
        var input =
                """
                    IF a LESS b
                            OR b GREATER a
                    AND c EQUALS d
                    OR a EQUALS 0
                    THEN whatever
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .conditionGroup()
                    .hasSize(3)
                    .firstConditionGroup()
                    .hasSize(2)
                    .first()
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(28)
                    .parentConditionGroup()
                    .parentConditionGroup()
                    .first()
                    .hasIndentationLevel(20)
                    .hasConnector(ASTConditionConnector.AND)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(20)
                    .hasConnector(ASTConditionConnector.OR)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_4_layers_double_grouping() {
        var input =
                """
                    IF a LESS b
                            OR b GREATER a
                    AND c EQUALS d
                        OR a EQUALS 0
                    THEN whatever
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .conditionGroup()
                    .hasSize(2)
                    .firstConditionGroup()
                    .hasSize(2)
                    .first()
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(28)
                    .parentConditionGroup()
                    .parentConditionGroup()
                    .secondConditionGroup()
                    .first()
                    .hasIndentationLevel(20)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(24)
                    .hasConnector(ASTConditionConnector.OR)
        }
    }
    
    @Test
    @Throws(Exception::class)
    fun indentation_7_layers_double_grouping_backtracking() {
        var input =
                """
                    IF a LESS b
                            OR b GREATER a
                    AND c EQUALS d
                        OR a EQUALS 0
                            AND c GREATER d
                            AND d LESS a
                        OR a GREATER c
                    THEN whatever
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .conditionGroup()
                    .hasSize(2)
                    .firstConditionGroup()
                    .hasSize(2)
                    .first()
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(28)
                    .parentConditionGroup()
                    .parentConditionGroup()
                    .secondConditionGroup()
                    .first()
                    .hasIndentationLevel(20)
                    .parentConditionGroup()
                    .firstConditionGroup()
                    .hasSize(3)
                    .first()
                    .hasIndentationLevel(24)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(28)
                    .parentConditionGroup()
                    .atIndex(2)
                    .hasIndentationLevel(28)
                    .parentConditionGroup()
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(24)
                    .hasConnector(ASTConditionConnector.OR)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_4_layers_simple_recurse() {
        var input =
                """
                    IF a LESS b
                        OR b GREATER a
                            OR c GREATER a
                                OR d GREATER a
                    THEN nothing
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
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
                                .hasIndentationLevel(0)
                        .parentConditionGroup()
                            .firstConditionGroup()
                                .hasSize(2)
                                .first()
                                    .hasIndentationLevel(24)
                            .parentConditionGroup()
                                .firstConditionGroup()
                                    .hasSize(2)
                                    .first()
                                        .hasIndentationLevel(28)
                                    .parentConditionGroup()
                                    .second()
                                        .hasIndentationLevel(32)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_3_layers_mixed_indentation() {
        var input =
                """
IF a LESS b
                        OR b GREATER a
                    OR c LESS d
                  THEN nothing
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .conditionGroup()
                    .hasSize(2)
                    .firstConditionGroup()
                    .hasSize(2)
                    .first()
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(24)
                    .parentConditionGroup()
                    .parentConditionGroup()
                    .first()
                    .hasIndentationLevel(20)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_4_layers_mixed_indentation() {
        var input =
                """
IF a LESS b
                        OR b GREATER a
                    OR c LESS d
                OR d EQUALS a
                THEN nothing
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                    .hasSizeOf(1)
                    .first()
                        .conditionGroup()
                        .hasSize(2)
                        .firstConditionGroup()
                            .hasSize(2)
                            .firstConditionGroup()
                                .hasSize(2)
                                .first()
                                    .hasIndentationLevel(0)
                            .parentConditionGroup()
                                .second()
                                    .hasIndentationLevel(24)
                            .parentConditionGroup()
                        .parentConditionGroup()
                            .first()
                                .hasIndentationLevel(20)
                        .parentConditionGroup()
                    .parentConditionGroup()
                        .first()
                            .hasIndentationLevel(16)

        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_5_layers_mixed_indentation() {
        var input =
                """
IF a LESS b
                    AND a EQUALS d
                        OR b GREATER a
                    AND c LESS d
                OR d EQUALS a
                THEN nothing
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.rules()
                .hasSizeOf(1)
                .first()
                    .conditionGroup()
                    .hasSize(2)
                    .firstConditionGroup()
                        .hasSize(3)
                        .hasNoConnector()
                        .first()
                            .hasNoConnector()
                            .hasIndentationLevel(0)
                        .parentConditionGroup()
                        .firstConditionGroup()
                            .hasSize(2)
                            .hasConnector(ASTConditionConnector.AND)
                            .first()
                                .hasIndentationLevel(20)
                                .hasNoConnector()
                            .parentConditionGroup()
                            .second()
                                .hasIndentationLevel(24)
                                .hasConnector(ASTConditionConnector.OR)
                            .parentConditionGroup()
                        .parentConditionGroup()
                        .second()
                            .hasConnector(ASTConditionConnector.AND)
                            .hasIndentationLevel(20)
                        .parentConditionGroup()
                    .parentConditionGroup()
                    .first()
                        .hasIndentationLevel(16)
                        .hasConnector(ASTConditionConnector.OR)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_2_layers_pre_keyword_indention() {
        var input =
                """
                    IF a LESS b
                        OR b GREATER a
                    THEN nothing
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
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
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(24)
        }
    }

    @Test
    @Throws(Exception::class)
    fun indentation_2_layers_post_keyword_indention() {
        var input =
                """
                    IF a LESS b
                    OR      b GREATER a
                    THEN nothing
                """
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
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
                    .hasIndentationLevel(0)
                    .parentConditionGroup()
                    .second()
                    .hasIndentationLevel(20)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "\n\n",
    "\n                                \n",
    "\n    \n",
    "\n\n\n\n",
    "   \n          \n              ",
    "           \n\n",
    "    \n       \n         ",
    "  \n  \n  ",
        "\n\n\n\n\n\n\n\n\n\n"
    //"\t\n\t\n\t" tabs break formating :)
    ])
    @Throws(Exception::class)
    fun variable_paragraph_test(paramStr: String) {
        var input =
                """a AS var1"""
                        .plus("""$paramStr""")
                        .plus(
"""IF a LESS b
OR      b GREATER a
THEN nothing
                """)
        var schema = """
            {
                a: 1,
                b: 2,
                c: 3,
                d: 4
            }
            """

        End2AstRunner.run(input, schema) {
            r ->
            r.variables()
                    .hasSizeOf(1)
            
            r.rules()
                    .hasSizeOf(1)
                    .first()
                    .conditionGroup()
                    .hasSize(2)
        }
    }
}