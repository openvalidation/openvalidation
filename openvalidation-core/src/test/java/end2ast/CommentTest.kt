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

import org.junit.jupiter.api.Test

internal class CommentTest {

    @Test
    @Throws(Exception::class)
    fun simple_comment() {
        var input =
                """
                    COMMENT hallo
                """
        
        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(1)
                    .first()
                        .hasLineSizeOf(1)
                        .hasComment("hallo") }
    }

    @Test
    @Throws(Exception::class)
    fun multiline_comment_windows_nl() {
        var input =
                """
                    COMMENT hallo
                    test   
                """
        
        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(1)
                    .first()
                        .hasLineSizeOf(2)
                        .hasComment("hallo")
                        .hasComment("test") }

    }

    @Test
    @Throws(Exception::class)
    fun multiline_comment_linux_nl() {
        var input =
                """
                   COMMENT hallo
                   test
                """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(1)
                    .first()
                        .hasLineSizeOf(2)
                        .hasComment("hallo")
                        .hasComment("test") }
    }


    @Test
    @Throws(Exception::class)
    fun multiline_comment_3lines() {
        var input =
                """
                   COMMENT hallo
                   test
                   ende
                """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(1)
                    .first()
                        .hasLineSizeOf(3)
                        .hasComment("hallo")
                        .hasComment("test")
                        .hasComment("ende") }
    }

    @Test
    @Throws(Exception::class)
    fun two_simple_comments() {
        var input = """
            COMMENT hallo

            COMMENT welt
            """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(2)
                    .first()
                        .hasLineSizeOf(1)
                        .hasComment("hallo")
                .parentList()
                    .second()
                        .hasLineSizeOf(1)
                        .hasComment("welt") }
    }

    @Test
    @Throws(Exception::class)
    fun simple_and_multiline_comment() {
        var input = """
            COMMENT hallo

            COMMENT welt
            Wiedersehen
            """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(2)
                    .first()
                        .hasLineSizeOf(1)
                        .hasComment("hallo")
                .parentList()
                    .second()
                        .hasLineSizeOf(2)
                        .hasComment("welt")
                        .hasComment("Wiedersehen") }
    }

    @Test
    @Throws(Exception::class)
    fun two_multiline_comments() {
        var input = """
            COMMENT hallo
               welt

            COMMENT gute
              nacht
            """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(2)
                    .first()
                        .hasLineSizeOf(2)
                        .hasComment("hallo")
                        .hasComment("welt")
                .parentList()
                    .second()
                        .hasLineSizeOf(2)
                        .hasComment("gute")
                        .hasComment("nacht") }
    }

    @Test
    @Throws(Exception::class)
    fun three_multiline_comments() {
        var input = """
            COMMENT hallo
            welt

            COMMENT gute
            nacht

            COMMENT guten
            morgen
            """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(3)
                    .first()
                        .hasLineSizeOf(2)
                        .hasComment("hallo")
                        .hasComment("welt")
                .parentList()
                    .second()
                        .hasLineSizeOf(2)
                        .hasComment("gute")
                        .hasComment("nacht")
                .parentList()
                    .third()
                        .hasLineSizeOf(2)
                        .hasComment("guten")
                        .hasComment("morgen") }
    }

    @Test
    @Throws(Exception::class)
    fun one_single_line_two_multiline_comments() {
        var input = """
            COMMENT hallo welt

            COMMENT gute
            nacht

            COMMENT guten morgen
            """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                    .hasSizeOf(3)
                    .first()
                        .hasLineSizeOf(1)
                        .hasComment("hallo welt")
                .parentList()
                    .second()
                        .hasLineSizeOf(2)
                        .hasComment("gute")
                        .hasComment("nacht")
                .parentList()
                    .third()
                        .hasLineSizeOf(1)
                        .hasComment("guten morgen") }
    }

    @Test
    @Throws(Exception::class)
    fun one_multiline_two_single_line_comments() {
        var input = """
            COMMENT hallo
             welt

            COMMENT gute nacht

            COMMENT guten morgen
            """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                        .hasSizeOf(3)
                        .first()
                            .hasPreprocessedSource()
                            .hasLineSizeOf(2)
                            .hasComment("hallo")
                            .hasComment("welt")
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasLineSizeOf(1)
                            .hasComment("gute nacht")
                    .parentList()
                        .third()
                            .hasPreprocessedSource()
                            .hasLineSizeOf(1)
                            .hasComment("guten morgen") }
    }

    @Test
    @Throws(Exception::class)
    fun two_multiline_one_single_line_comments() {
        var input = """
            COMMENT hallo
             welt

            COMMENT gute
             nacht

            COMMENT guten morgen
            """

        End2AstRunner.run(input, "{}") {
                r -> r.comments()
                        .hasSizeOf(3)
                        .first()
                            .hasPreprocessedSource()
                            .hasLineSizeOf(2)
                            .hasComment("hallo")
                            .hasComment("welt")
                    .parentList()
                        .second()
                            .hasPreprocessedSource()
                            .hasLineSizeOf(2)
                            .hasComment("gute")
                            .hasComment("nacht")
                    .parentList()
                        .third()
                            .hasPreprocessedSource()
                            .hasLineSizeOf(1)
                            .hasComment("guten morgen")
        }
    }

}
