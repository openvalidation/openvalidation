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
import org.junit.jupiter.api.Test;

public class NullCheckRulesTest {

  @Test
  public void should_create_null_check_rules() throws Exception {
    String input = "IF address.city IS Dortmund THEN ...";
    String schema = "{address:{city:'test'}}";

    End2AstRunner.run(
        input,
        schema,
        r -> {
          r.sizeOfElements(1)
              .sizeOfRules(1)
              .sizeOfNullCheckRules(1)
              .rules()
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty()
              .hasPath("address.city")
              .parentCondition()
              .rightString()
              .hasValue("Dortmund")
              .parentModel()
              .sizeOfNullCheckRules(1)
              .nullCheckRules()
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EMPTY)
              .leftProperty()
              .hasPath("address")
              .parentCondition();
        });
  }

  @Test
  public void should_create_null_check_rules_from_variable() throws Exception {

    String input = "address.city AS mycity \n\nIF mycity IS Dortmund THEN ...";
    String schema = "{address:{city:'test'}}";

    End2AstRunner.run(
        input,
        schema,
        r -> {
          r.sizeOfElements(2)
              .sizeOfRules(1)
              .sizeOfVariables(1)
              .rules()
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftVariable()
              .hasName("mycity")
              .parentCondition()
              .rightString()
              .hasValue("Dortmund")
              .parentModel()
              .sizeOfNullCheckRules(1)
              .nullCheckRules()
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EMPTY)
              .leftProperty()
              .hasPath("address");
        });
  }

  @Test
  public void should_create_recursive_null_check_rules_from_variable() throws Exception {
    String input =
        "address.city AS mycity \n\nmycity IS Dortmund AS isit Dortmund \n\nIF isit Dortmund THEN ...";
    String schema = "{address:{city:'test'}}";

    End2AstRunner.run(
        input,
        schema,
        r -> {
          r.sizeOfElements(3)
              .sizeOfRules(1)
              .sizeOfVariables(2)
              .rules()
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftVariable()
              .hasName("isit Dortmund")
              .parentCondition()
              .rightBoolean()
              .isTrue()
              .parentModel()
              .sizeOfNullCheckRules(1)
              .nullCheckRules()
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EMPTY)
              .leftProperty()
              .hasPath("address");
        });
  }
}
