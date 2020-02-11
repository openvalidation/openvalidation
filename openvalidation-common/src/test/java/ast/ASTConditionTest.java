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

package ast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ASTConditionTest {

  @Test
  public void should_gets_parent_properties() {
    ASTConditionBuilder builder = new ASTConditionBuilder();
    builder
        .create()
        .withLeftOperandAsProperty("address", "name")
        .withRightOperandAsProperty("order", "person", "age");

    ASTCondition condition = builder.getModel();
    List<ASTOperandProperty> parentProperties = condition.getAllParentProperties();

    assertThat(parentProperties, notNullValue());
    assertThat(parentProperties, hasSize(3));

    assertThat(parentProperties.get(0).getPathAsString(), is("address"));
    assertThat(parentProperties.get(1).getPathAsString(), is("order"));
    assertThat(parentProperties.get(2).getPathAsString(), is("order.person"));
  }

  @Test
  public void should_gets_distinted_parent_properties() {
    ASTConditionBuilder builder = new ASTConditionBuilder();
    builder
        .create()
        .withLeftOperandAsProperty("order", "person")
        .withRightOperandAsProperty("order", "person", "age");

    ASTCondition condition = builder.getModel();
    List<ASTOperandProperty> parentProperties = condition.getAllParentProperties();

    assertThat(parentProperties, notNullValue());
    assertThat(parentProperties, hasSize(2));

    assertThat(parentProperties.get(0).getPathAsString(), is("order"));
    assertThat(parentProperties.get(1).getPathAsString(), is("order.person"));
  }

  @Test
  public void should_resolve_nothing() {
    ASTOperandStaticNumber number = new ASTOperandStaticNumber(5.0);
    number.setSource("the number 5 is the source");

    ASTCondition condition = new ASTCondition();
    condition.setLeftOperand(number);

    assertThat(condition.getLeftOperand().isNumber(), is(true));
    assertThat(condition.getRightOperand(), nullValue());

    condition.resolveNumberValue();

    assertThat(condition.getLeftOperand().isNumber(), is(true));
    assertThat(condition.getRightOperand(), nullValue());
  }
}
