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

package io.openvalidation.antlr.test.sourcetests.operand;

import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

class PropertyTest {

  // operand property
  @Test
  void test_property_simple_single_part() throws Exception {
    String input = GrammarBuilder.create().with("RandomNumber").AS("variable").getText();

    String expected = GrammarBuilder.create().with("RandomNumber").getText();

    String schema = "{Randomnumber: 25}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables().first().operandProperty().hasPreprocessedSource(expected);
        });
  }

  @Test
  void test_property_simple_multiple_parts() throws Exception {
    String input = GrammarBuilder.create().with("Person.Information.Age").AS("variable").getText();

    String expected = GrammarBuilder.create().with("Person.Information.Age").getText();

    String schema = "{Person: {Information: {Age: 25}}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables().first().operandProperty().hasPreprocessedSource(expected);
        });
  }

  // property constraint part
  // todo lazevedo 22.05.19 reminder (used for 'with-construct')

  // property static part
  @Test
  void test_property_part_multiple_parts() throws Exception {
    String input = GrammarBuilder.create().with("Person.Information.Age").AS("variable").getText();

    String schema = "{Person: {Information: {Age: 25}}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.variables()
              .first()
              .operandProperty()
              .firstStaticPart()
              .hasPreprocessedSource("Person")
              .parentProperty()
              .secondStaticPart()
              .hasPreprocessedSource("Information")
              .parentProperty()
              .thirdStaticPart()
              .hasPreprocessedSource("Age");
        });
  }
}
