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

package exceptionhandling

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ConditionGroupTest {
    internal var runner = ExceptionRunner()

    @ParameterizedTest
    @ValueSource(strings = [
//        """
//                  a
//              AS  b
//
//              IF  b EQUALS c
//                  b EQUALS d
//            THEN  error message
//
//        """,
        """
                  a
              AS  b

            b MUST EQUALS c
            b EQUALS d

        """    ])
    @Throws(Exception::class)
    fun missing_left_operand(rule: String) {
        runner.run(rule) { r -> r.containsValidationMessage("missing AND/OR connector in combined condition.") }
    }
}
