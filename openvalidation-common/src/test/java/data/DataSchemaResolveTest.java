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

package data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.converter.Json2DataSchemaConverter;
import io.openvalidation.common.data.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DataSchemaResolveTest {

  private void addUniquePropertyToSchema(DataProperty prop, DataSchema schema) {
    schema.addProperty(prop);
    schema.getUniqueProperties().put(prop.getName(), prop);
  }

  @Test
  void should_resolve_property() {
    DataSchema schema = new DataSchema();
    schema.addProperty("Street", "User.Address", DataPropertyType.String);

    DataPropertyBase result = schema.resolve("hallo user.address.street hallo");

    assertThat(result, notNullValue());
    assertThat(result, instanceOf(DataProperty.class));
    assertThat(result.getType(), is(DataPropertyType.String));
  }

  @Test
  void should_resolve_variable() {
    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("Location", DataPropertyType.String));

    DataPropertyBase result = schema.resolve("hallo Location hallo");

    assertThat(result, notNullValue());
    assertThat(result, instanceOf(DataVariableReference.class));
    assertThat(result.getType(), is(DataPropertyType.String));
  }

  @Test
  void should_resolve_arrayproperty() throws Exception {

    String json =
        "{"
            + "name:\"string\", "
            + "portfolio: {"
            + "shares:["
            + "{"
            + "percentage:20"
            + "}"
            + "]"
            + "}"
            + "}";

    DataSchema schema = Json2DataSchemaConverter.convertSchema(json);
    schema.sort();

    DataPropertyBase result = schema.resolve("portfolio.shares.percentage");

    assertThat(result, notNullValue());
    assertThat(result, instanceOf(DataArrayProperty.class));

    DataArrayProperty array = (DataArrayProperty) result;

    assertThat(array.getType(), is(DataPropertyType.Decimal));
    assertThat(array.getName(), is("percentage"));
    assertThat(array.getPropertyPath(), isEmptyString());
    assertThat(array.getArrayPath(), is("portfolio.shares"));
  }

  @Test
  void test_resolve_of_simple_path() {
    // assemble
    String inputContent = "person.age";
    DataSchema schema = new DataSchema();
    schema.addProperty("age", "person", DataPropertyType.Decimal);

    // act
    DataPropertyBase actualPropertyBase = schema.resolve(inputContent);

    // assert
    assertThat(actualPropertyBase, notNullValue());
    assertThat(actualPropertyBase, instanceOf(DataProperty.class));

    DataProperty dataProperty = (DataProperty) actualPropertyBase;
    assertThat(dataProperty.getType(), notNullValue());

    assertThat(dataProperty.getName(), notNullValue());
    assertThat(dataProperty.getName(), equalTo("age"));
    assertThat(dataProperty.getType(), equalTo(DataPropertyType.Decimal));

    assertThat(dataProperty.getFullName(), equalTo("person.age"));
    assertThat(dataProperty.getFullNameAsParts(), notNullValue());
    assertThat(dataProperty.getFullNameAsParts(), arrayWithSize(2));
    assertThat(dataProperty.getFullNameAsParts()[0], equalTo("person"));
    assertThat(dataProperty.getFullNameAsParts()[1], equalTo("age"));
  }

  @Test
  void test_resolve_of_simple_path_longer_path() {
    // assemble
    String inputContent = "contract.person.age";
    DataSchema schema = new DataSchema();
    schema.addProperty("age", "contract.person", DataPropertyType.Decimal);

    // act
    DataPropertyBase actualPropertyBase = schema.resolve(inputContent);

    // assert
    assertThat(actualPropertyBase, notNullValue());
    assertThat(actualPropertyBase, instanceOf(DataProperty.class));

    DataProperty dataProperty = (DataProperty) actualPropertyBase;
    assertThat(dataProperty.getType(), notNullValue());

    assertThat(dataProperty.getName(), notNullValue());
    assertThat(dataProperty.getName(), equalTo("age"));
    assertThat(dataProperty.getType(), equalTo(DataPropertyType.Decimal));

    assertThat(dataProperty.getFullName(), equalTo("contract.person.age"));
    assertThat(dataProperty.getFullNameAsParts(), notNullValue());
    assertThat(dataProperty.getFullNameAsParts(), arrayWithSize(3));
    assertThat(dataProperty.getFullNameAsParts()[0], equalTo("contract"));
    assertThat(dataProperty.getFullNameAsParts()[1], equalTo("person"));
    assertThat(dataProperty.getFullNameAsParts()[2], equalTo("age"));
  }

  @Test
  void test_resolve_single_word() {
    DataSchema schema = new DataSchema();
    schema.addProperty("ort", "", DataPropertyType.String);

    DataPropertyBase actualPropertyBase = schema.resolve("Dortmund");

    assertThat(actualPropertyBase, nullValue());
  }

  @Test
  void test_resolve_variable_name_with_spaces() {
    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("city of dreams", DataPropertyType.String));

    DataPropertyBase actualPropertyBase = schema.resolve("aaa city of dreams bbb");

    assertThat(actualPropertyBase, notNullValue());
    assertThat(actualPropertyBase.getName(), is("city of dreams"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "city of my dreams",
        "the city of my dreams",
        "city of my dreams that night",
        "the city of my dreams that night"
      })
  void test_resolve_variable_with_higher_priority_than_unique(String content) {
    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("city of my dreams", DataPropertyType.String));
    addUniquePropertyToSchema(new DataProperty("address", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("city", "address", DataPropertyType.Decimal), schema);

    DataPropertyBase actualPropertyBase = schema.resolve(content);

    assertThat(actualPropertyBase, notNullValue());
    assertThat(actualPropertyBase, instanceOf(DataVariableReference.class));
    assertThat(actualPropertyBase.getType(), is(DataPropertyType.String));
    assertThat(actualPropertyBase.getName(), is("city of my dreams"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"city", "the city", "city of my dreams", "the city of my dreams"})
  void test_resolve_unique_property(String content) {
    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("address", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(new DataProperty("city", "address", DataPropertyType.String), schema);

    DataPropertyBase actualPropertyBase = schema.resolve(content);

    assertThat(actualPropertyBase, notNullValue());
    assertThat(actualPropertyBase, instanceOf(DataProperty.class));
    assertThat(actualPropertyBase.getType(), is(DataPropertyType.String));
    assertThat(actualPropertyBase.getFullName(), is("address.city"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "nick name",
        "the nick name",
        "nick name in question",
        "the nick name in question",
      })
  void test_resolve_property_with_underscore_in_its_name_using_a_space(String content) {
    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("nick_name", "", DataPropertyType.String), schema);

    DataPropertyBase actualPropertyBase = schema.resolve(content);

    assertThat(actualPropertyBase, notNullValue());
    assertThat(actualPropertyBase, instanceOf(DataProperty.class));
    assertThat(actualPropertyBase.getType(), is(DataPropertyType.String));
    assertThat(actualPropertyBase.getFullName(), is("nick_name"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "nick name.stuff",
        "the nick name.stuff",
        "nick name.stuff in question",
        "the nick name.stuff in question",
      })
  void test_should_not_resolve_property_with_underscore_in_its_path_using_a_space(String content) {
    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("first_name", "", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("stuff", "first_name", DataPropertyType.Decimal), schema);

    DataPropertyBase actualPropertyBase = schema.resolve(content);

    assertThat(actualPropertyBase, nullValue());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "nick name",
        "the nick name",
        "nick name in question",
        "the nick name in question",
      })
  void test_resolve_unique_property_with_underscore_in_its_name_using_a_space(String content) {
    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(
        new DataProperty("nick_name", "person", DataPropertyType.String), schema);
    addUniquePropertyToSchema(new DataProperty("person", "", DataPropertyType.String), schema);

    DataPropertyBase actualPropertyBase = schema.resolve(content);

    assertThat(actualPropertyBase, notNullValue());
    assertThat(actualPropertyBase, instanceOf(DataProperty.class));
    assertThat(actualPropertyBase.getType(), is(DataPropertyType.String));
    assertThat(actualPropertyBase.getFullName(), is("person.nick_name"));
  }

  @Test
  void test_resolve_all_explicit_paths() {
    String input = "apple banana strawberry";

    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("apple", "", DataPropertyType.String), schema);
    addUniquePropertyToSchema(new DataProperty("banana", "", DataPropertyType.String), schema);
    addUniquePropertyToSchema(new DataProperty("strawberry", "", DataPropertyType.String), schema);

    List<DataPropertyBase> result = schema.resolveAll(input);

    assertThat(result, hasSize(3));
    assertThat(
        result,
        containsInAnyOrder(
            allOf(
                hasProperty("name", is("apple")), hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("banana")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("strawberry")),
                hasProperty("type", is(DataPropertyType.String)))));
  }

  @Test
  void test_resolve_all_explicit_paths_long() {
    String input = "fruits.apple fruits.banana vegetables.carrot";

    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("fruits", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(new DataProperty("vegetables", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(new DataProperty("apple", "fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("banana", "fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("carrot", "vegetables", DataPropertyType.String), schema);

    List<DataPropertyBase> result = schema.resolveAll(input);

    assertThat(result, hasSize(3));
    assertThat(
        result,
        containsInAnyOrder(
            allOf(
                hasProperty("name", is("apple")), hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("banana")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("carrot")),
                hasProperty("type", is(DataPropertyType.String)))));
  }

  @Test
  void test_resolve_all_unique() {
    String input = "apple banana carrot";

    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("fruits", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(new DataProperty("vegetables", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(new DataProperty("apple", "fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("banana", "fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("carrot", "vegetables", DataPropertyType.String), schema);

    List<DataPropertyBase> result = schema.resolveAll(input);

    assertThat(result, hasSize(3));
    assertThat(
        result,
        containsInAnyOrder(
            allOf(
                hasProperty("name", is("apple")), hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("banana")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("carrot")),
                hasProperty("type", is(DataPropertyType.String)))));
  }

  @Test
  void test_resolve_all_shortcuts() {
    String input = "fruits.apple fruits.banana vegetables.carrot";

    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("objects", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("fruits", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("vegetables", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("apple", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("banana", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("carrot", "objects.vegetables", DataPropertyType.String), schema);

    List<DataPropertyBase> result = schema.resolveAll(input);

    assertThat(result, hasSize(3));
    assertThat(
        result,
        containsInAnyOrder(
            allOf(
                hasProperty("name", is("apple")), hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("banana")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("carrot")),
                hasProperty("type", is(DataPropertyType.String)))));
  }

  @Test
  void test_resolve_mixed_with_shortcut_explicitLong_unique() {
    String input = "fruits.apple objects.fruits.banana carrot";

    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("objects", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("fruits", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("vegetables", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("apple", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("banana", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("carrot", "objects.vegetables", DataPropertyType.String), schema);

    List<DataPropertyBase> result = schema.resolveAll(input);

    assertThat(result, hasSize(3));
    assertThat(
        result,
        containsInAnyOrder(
            allOf(
                hasProperty("name", is("apple")), hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("banana")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("carrot")),
                hasProperty("type", is(DataPropertyType.String)))));
  }

  @Test
  void test_resolve_mixed_with_shortcut_explicitLong_unique_and_sugar() {
    String input =
        "The red fruits.apple lies next to the objects.fruits.banana and the carrot that looks pretty appetising";

    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("objects", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("fruits", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("vegetables", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("apple", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("banana", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("carrot", "objects.vegetables", DataPropertyType.String), schema);

    List<DataPropertyBase> result = schema.resolveAll(input);

    assertThat(result, hasSize(3));
    assertThat(
        result,
        containsInAnyOrder(
            allOf(
                hasProperty("name", is("apple")), hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("banana")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("carrot")),
                hasProperty("type", is(DataPropertyType.String)))));
  }

  @Test
  void test_resolve_mixed_with_shortcut_explicitLong_unique_and_variableReference() {
    String input = "fruits.apple objects.fruits.banana carrot varX";

    DataSchema schema = new DataSchema();
    addUniquePropertyToSchema(new DataProperty("objects", "", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("fruits", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("vegetables", "objects", DataPropertyType.Object), schema);
    addUniquePropertyToSchema(
        new DataProperty("apple", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("banana", "objects.fruits", DataPropertyType.String), schema);
    addUniquePropertyToSchema(
        new DataProperty("carrot", "objects.vegetables", DataPropertyType.String), schema);
    schema.addVariable(new DataVariableReference("varX", DataPropertyType.Decimal));

    List<DataPropertyBase> result = schema.resolveAll(input);

    assertThat(result, hasSize(4));
    assertThat(
        result,
        containsInAnyOrder(
            allOf(
                hasProperty("name", is("apple")), hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("banana")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("carrot")),
                hasProperty("type", is(DataPropertyType.String))),
            allOf(
                hasProperty("name", is("varX")),
                hasProperty("type", is(DataPropertyType.Decimal)))));
  }
}
