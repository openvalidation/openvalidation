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

import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ASTOperandPropertyTest {

  @Test
  public void should_gets_no_next_parent_property() {
    ASTOperandProperty property = new ASTOperandProperty("city");
    ASTOperandProperty parent = property.getParentProperty();

    assertThat(parent, nullValue());
  }

  @Test
  public void should_gets_next_parent_property() {
    ASTOperandProperty property = new ASTOperandProperty("address", "city");
    ASTOperandProperty parent = property.getParentProperty();

    assertThat(parent, notNullValue());
    assertThat(parent.getPathAsString(), is("address"));
  }

  @Test
  public void should_gets_next_long_parent_property() {
    ASTOperandProperty property = new ASTOperandProperty("model", "address", "city");
    ASTOperandProperty parent = property.getParentProperty();

    assertThat(parent, notNullValue());
    assertThat(parent.getPath(), notNullValue());
    assertThat(parent.getPath(), hasSize(2));
    assertThat(parent.getPathAsString(), is("model.address"));
  }

  @Test
  public void should_gets_no_parent_property() throws Exception {
    ASTOperandProperty property = new ASTOperandProperty("city");
    List<ASTOperandProperty> parent = property.getAllParentProperties();

    assertThat(parent, notNullValue());
    assertThat(parent, hasSize(0));
  }

  @Test
  public void should_gets_one_parent_property() throws Exception {
    ASTOperandProperty property = new ASTOperandProperty("address", "city");
    List<ASTOperandProperty> parent = property.getAllParentProperties();

    assertThat(parent, notNullValue());
    assertThat(parent, hasSize(1));
    assertThat(parent.get(0).getPathAsString(), is("address"));
  }

  @Test
  public void should_gets_recursive_parent_property() throws Exception {
    ASTOperandProperty property = new ASTOperandProperty("model", "address", "city");
    List<ASTOperandProperty> parent = property.getAllParentProperties();

    assertThat(parent, notNullValue());
    assertThat(parent, hasSize(2));
    assertThat(parent.get(0).getPathAsString(), is("model"));
    assertThat(parent.get(1).getPathAsString(), is("model.address"));
  }
}
