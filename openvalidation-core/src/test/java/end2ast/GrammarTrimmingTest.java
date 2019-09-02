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

package end2ast;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class GrammarTrimmingTest {

  @ParameterizedTest
  @CsvSource({
    // en
    "the applicants city MUST be Dortmund,en",
    "the applicants city HAVE to be Dortmund,en",
    "IF the city is NOT Dortmund THEN aa,en",
    "IF the city NOT Dortmund is THEN aa,en",

    // de
    "die city des Bewerbers MUSS Dortmund sein,de",
    "die city des Bewerbers MUSS Dortmund heißen,de",
    "die city des Bewerbers MUSS sein Dortmund,de",
    "die city des Bewerbers MUSS heißen Dortmund,de",
    "WENN die city des Bewerbers NICHT Dortmund ist DANN hallo,de",
    "WENN die city des Bewerbers NICHT heißt Dortmund ist DANN hallo,de"
  })
  public void trimm_grammar_on_string_equality(String rule, String culture) throws Exception {

    End2AstRunner.run(
        rule,
        "{city:''}",
        culture,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasPath("city")
              .hasType(DataPropertyType.String)
              .parentCondition()
              .rightString()
              .hasValue("Dortmund");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "\n\n  the applicants city MUST\n be Dortmund",
        "\n \n  the applicants city MUST\n be Dortmund \n \n",
        "\n \n \n   IF the city NOT Dortmund is \nTHEN aa",
        "\n \n \n   IF the city NOT Dortmund is \nTHEN aa \n \n \n"
      })
  public void trimm_grammar_on(String rule) throws Exception {

    End2AstRunner.run(
        rule,
        "{city:''}",
        "en",
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasPath("city")
              .hasType(DataPropertyType.String)
              .parentCondition()
              .rightString()
              .hasValue("Dortmund");
        });
  }
}
