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

package end2ast.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Test;

public class FirstFunctionsTests {

  /*
  todo evtl first 10 items -->OF
  todo sorting arrays

  die ersten 10 items/streets
  take 10 items from addresses
  take 10 street from addresses
  take 10 items from addresses with city gleich dortmund
  take 10 street from addresses with city gleich dortmund




   */

  @Test
  public void first_function_simple() throws Exception {
    String rule = "a first item from addresses as a first address";
    String schema = "{addresses:[]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a first address")
                .operandFunction()
                .hasName("FIRST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array));
  }

  @Test
  public void first_function_with_simple_condition() throws Exception {
    String rule = "a first item from addresses with zip_code equals 12345 as a first address";
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                  .hasName("a first address")
                  .operandFunction()
                    .hasName("FIRST")
                    .hasType(DataPropertyType.Object)
                    .sizeOfParameters(1)
                    .parameters()
                      .first()
                        .property("addresses")
                          .hasType(DataPropertyType.Array)
                    .parentList()
                      .second()
                       // .co
    );
  }
}
