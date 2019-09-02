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

import io.openvalidation.common.data.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DataPropertyBaseTypeMatcher;

class DataSchemaBuildTest {

  private DataSchema schema;

  @BeforeEach
  void initialize() {
    schema = new DataSchema();
  }

  @Test
  void test_Add_DataPropertyBase_persistency_with_DataProperties() {
    DataPropertyBase string_property =
        new DataProperty("name1", "dummy.path", DataPropertyType.String);
    DataPropertyBase boolean_property =
        new DataProperty("name2", "dummy.path", DataPropertyType.Boolean);
    DataPropertyBase decimal_property =
        new DataProperty("name3", "dummy.path", DataPropertyType.Decimal);
    DataPropertyBase object_property =
        new DataProperty("name4", "dummy.path", DataPropertyType.Object);
    DataPropertyBase array_property =
        new DataProperty("name5", "dummy.path", DataPropertyType.Array);
    DataPropertyBase date_property = new DataProperty("name6", "dummy.path", DataPropertyType.Date);
    DataPropertyBase enum_property = new DataProperty("name7", "dummy.path", DataPropertyType.Enum);

    schema.add(string_property);
    schema.add(boolean_property);
    schema.add(decimal_property);
    schema.add(object_property);
    schema.add(array_property);
    schema.add(date_property);
    schema.add(enum_property);

    List<DataProperty> properties = schema.getProperties();

    assertThat(properties, notNullValue());
    assertThat(properties, hasItem(equalTo(string_property)));
    assertThat(properties, hasItem(equalTo(boolean_property)));
    assertThat(properties, hasItem(equalTo(decimal_property)));
    assertThat(properties, hasItem(equalTo(object_property)));
    assertThat(properties, hasItem(equalTo(array_property)));
    assertThat(properties, hasItem(equalTo(date_property)));
    assertThat(properties, hasItem(equalTo(enum_property)));
  }

  @Test
  void test_Add_DataPropertyBase_persistency_with_DataVariableReferences() {
    DataPropertyBase string_property = new DataVariableReference("name1", DataPropertyType.String);
    DataPropertyBase boolean_property =
        new DataVariableReference("name2", DataPropertyType.Boolean);
    DataPropertyBase decimal_property =
        new DataVariableReference("name3", DataPropertyType.Decimal);
    DataPropertyBase object_property = new DataVariableReference("name4", DataPropertyType.Object);
    DataPropertyBase array_property = new DataVariableReference("name5", DataPropertyType.Array);
    DataPropertyBase date_property = new DataVariableReference("name6", DataPropertyType.Date);
    DataPropertyBase enum_property = new DataVariableReference("name7", DataPropertyType.Enum);

    schema.add(string_property);
    schema.add(boolean_property);
    schema.add(decimal_property);
    schema.add(object_property);
    schema.add(array_property);
    schema.add(date_property);
    schema.add(enum_property);

    List<DataVariableReference> references = schema.getVariableReferences();

    assertThat(references, notNullValue());
    assertThat(references, hasItem(equalTo(string_property)));
    assertThat(references, hasItem(equalTo(boolean_property)));
    assertThat(references, hasItem(equalTo(decimal_property)));
    assertThat(references, hasItem(equalTo(object_property)));
    assertThat(references, hasItem(equalTo(array_property)));
    assertThat(references, hasItem(equalTo(date_property)));
    assertThat(references, hasItem(equalTo(enum_property)));
  }

  @Test
  void test_addProperty_persistency() {
    DataProperty string_property = new DataProperty("name1", "dummy.path", DataPropertyType.String);
    DataProperty boolean_property =
        new DataProperty("name2", "dummy.path", DataPropertyType.Boolean);
    DataProperty decimal_property =
        new DataProperty("name3", "dummy.path", DataPropertyType.Decimal);
    DataProperty object_property = new DataProperty("name4", "dummy.path", DataPropertyType.Object);
    DataProperty array_property = new DataProperty("name5", "dummy.path", DataPropertyType.Array);
    DataProperty date_property = new DataProperty("name6", "dummy.path", DataPropertyType.Date);
    DataProperty enum_property = new DataProperty("name7", "dummy.path", DataPropertyType.Enum);

    schema.addProperty(string_property);
    schema.addProperty(boolean_property);
    schema.addProperty(decimal_property);
    schema.addProperty(object_property);
    schema.addProperty(array_property);
    schema.addProperty(date_property);
    schema.addProperty(enum_property);

    List<DataProperty> properties = schema.getProperties();

    assertThat(properties, notNullValue());
    assertThat(properties, hasItem(equalTo(string_property)));
    assertThat(properties, hasItem(equalTo(boolean_property)));
    assertThat(properties, hasItem(equalTo(decimal_property)));
    assertThat(properties, hasItem(equalTo(object_property)));
    assertThat(properties, hasItem(equalTo(array_property)));
    assertThat(properties, hasItem(equalTo(date_property)));
    assertThat(properties, hasItem(equalTo(enum_property)));
  }

  @Test
  void test_addProperty_by_parameters_persistency() {
    schema.addProperty("name1", "dummy.path", DataPropertyType.String);
    schema.addProperty("name2", "dummy.path", DataPropertyType.Boolean);
    schema.addProperty("name3", "dummy.path", DataPropertyType.Decimal);
    schema.addProperty("name4", "dummy.path", DataPropertyType.Object);
    schema.addProperty("name5", "dummy.path", DataPropertyType.Array);
    schema.addProperty("name6", "dummy.path", DataPropertyType.Date);
    schema.addProperty("name7", "dummy.path", DataPropertyType.Enum);

    List<DataProperty> properties = schema.getProperties();

    assertThat(properties, notNullValue());

    assertThat(properties, hasItem(new DataPropertyBaseTypeMatcher(DataPropertyType.String)));
    assertThat(properties, hasItem(new DataPropertyBaseTypeMatcher(DataPropertyType.Boolean)));
    assertThat(properties, hasItem(new DataPropertyBaseTypeMatcher(DataPropertyType.Decimal)));
    assertThat(properties, hasItem(new DataPropertyBaseTypeMatcher(DataPropertyType.Object)));
    assertThat(properties, hasItem(new DataPropertyBaseTypeMatcher(DataPropertyType.Array)));
    assertThat(properties, hasItem(new DataPropertyBaseTypeMatcher(DataPropertyType.Date)));
    assertThat(properties, hasItem(new DataPropertyBaseTypeMatcher(DataPropertyType.Enum)));
  }

  @Test
  void test_addVariable_persistency() {
    DataVariableReference string_property =
        new DataVariableReference("name1", DataPropertyType.String);
    DataVariableReference boolean_property =
        new DataVariableReference("name2", DataPropertyType.Boolean);
    DataVariableReference decimal_property =
        new DataVariableReference("name3", DataPropertyType.Decimal);
    DataVariableReference object_property =
        new DataVariableReference("name4", DataPropertyType.Object);
    DataVariableReference array_property =
        new DataVariableReference("name5", DataPropertyType.Array);
    DataVariableReference date_property = new DataVariableReference("name6", DataPropertyType.Date);
    DataVariableReference enum_property = new DataVariableReference("name7", DataPropertyType.Enum);

    schema.addVariable(string_property);
    schema.addVariable(boolean_property);
    schema.addVariable(decimal_property);
    schema.addVariable(object_property);
    schema.addVariable(array_property);
    schema.addVariable(date_property);
    schema.addVariable(enum_property);

    List<DataVariableReference> references = schema.getVariableReferences();

    assertThat(references, notNullValue());
    assertThat(references, hasItem(equalTo(string_property)));
    assertThat(references, hasItem(equalTo(boolean_property)));
    assertThat(references, hasItem(equalTo(decimal_property)));
    assertThat(references, hasItem(equalTo(object_property)));
    assertThat(references, hasItem(equalTo(array_property)));
    assertThat(references, hasItem(equalTo(date_property)));
    assertThat(references, hasItem(equalTo(enum_property)));
  }

  @Test
  void test_addVariables_persistency() {
    DataVariableReference string_property =
        new DataVariableReference("name1", DataPropertyType.String);
    DataVariableReference boolean_property =
        new DataVariableReference("name2", DataPropertyType.Boolean);
    DataVariableReference decimal_property =
        new DataVariableReference("name3", DataPropertyType.Decimal);
    DataVariableReference object_property =
        new DataVariableReference("name4", DataPropertyType.Object);
    DataVariableReference array_property =
        new DataVariableReference("name5", DataPropertyType.Array);
    DataVariableReference date_property = new DataVariableReference("name6", DataPropertyType.Date);
    DataVariableReference enum_property = new DataVariableReference("name7", DataPropertyType.Enum);

    List<DataVariableReference> expectedVariableReferenceList = new ArrayList<>();
    expectedVariableReferenceList.add(string_property);
    expectedVariableReferenceList.add(boolean_property);
    expectedVariableReferenceList.add(decimal_property);
    expectedVariableReferenceList.add(object_property);
    expectedVariableReferenceList.add(array_property);
    expectedVariableReferenceList.add(date_property);
    expectedVariableReferenceList.add(enum_property);

    schema.addVariables(expectedVariableReferenceList);

    List<DataVariableReference> actualReferences = schema.getVariableReferences();

    assertThat(actualReferences, notNullValue());
    assertThat(actualReferences, equalTo(expectedVariableReferenceList));
  }

  @Test
  void test_isArrayAccessor_expected_false_with_empty_schema() {
    String input = "something";

    boolean actual = schema.isArrayAccessor(input);

    assertThat(actual, is(false));
  }

  @Test
  void test_isArrayAccessor_expected_false_with_property_of_type_Array() {
    String input = "something";
    schema.add(new DataProperty("bewohner", "haus", DataPropertyType.Array));
    boolean actual = schema.isArrayAccessor(input);

    assertThat(actual, is(false));
  }

  @Test
  void test_isArrayAccessor_expected_true_with_single_element_of_type_Array() {
    String input = "Haus.Bewohner.Bewohner1";
    schema.add(new DataProperty("bewohner", "haus", DataPropertyType.Array));
    boolean actual = schema.isArrayAccessor(input);

    assertThat(actual, is(true));
  }

  @Test
  void test_getProperties() {
    List<DataPropertyBase> properties = generateDataPropertiesOfAllTypesAsDataPropertyBaseList();
    List<DataPropertyBase> variableReferences =
        generateDataVariableReferencesOfAllTypesAsDataPropertyBaseList();

    addAllToSchema(properties);
    addAllToSchema(variableReferences);

    assertThat(schema.getProperties(), notNullValue());
    assertThat(schema.getProperties(), equalTo(properties));
  }

  @Test
  void test_getVariableReferences() {
    List<DataPropertyBase> properties = generateDataPropertiesOfAllTypesAsDataPropertyBaseList();
    List<DataPropertyBase> variableReferences =
        generateDataVariableReferencesOfAllTypesAsDataPropertyBaseList();

    addAllToSchema(properties);
    addAllToSchema(variableReferences);

    assertThat(schema.getVariableReferences(), notNullValue());
    assertThat(schema.getVariableReferences(), equalTo(variableReferences));
  }

  @Test
  void test_getAllNames_with_empty_list() {
    assertThat(schema.getAllNames(), notNullValue());
    assertThat(schema.getAllNames(), hasSize(0));
  }

  @Test
  void test_getAllNames() {
    DataPropertyBase propertyBase1 =
        new DataProperty("name1", "dummy.path", DataPropertyType.String);
    DataPropertyBase propertyBase2 =
        new DataProperty("name2", "dummy.path", DataPropertyType.Boolean);
    DataPropertyBase propertyBase3 = new DataVariableReference("name3", DataPropertyType.Array);

    schema.add(propertyBase1);
    schema.add(propertyBase2);
    schema.add(propertyBase3);

    List<String> expectedNames = new ArrayList<>();
    expectedNames.add("name1");
    expectedNames.add("name2");
    expectedNames.add("name3");

    assertThat(schema.getAllNames(), notNullValue());
    assertThat(schema.getAllNames(), equalTo(expectedNames));
  }

  @Test
  void should_add_array_properties() {
    DataSchema schema = new DataSchema();

    schema.addProperty("my", null, DataPropertyType.Object);
    schema.addProperty("arr", "my", DataPropertyType.Array);
    schema.addProperty("age", "my.arr", DataPropertyType.Decimal);

    assertThat(schema.getAllNames(), notNullValue());
    assertThat(schema.getAllNames(), hasSize(3));

    assertThat(schema.getArrayProperties(), notNullValue());
    assertThat(schema.getArrayProperties(), hasSize(1));
    DataArrayProperty arrayProperty = schema.getArrayProperties().get(0);
    assertThat(arrayProperty.getName(), is("age"));
    assertThat(arrayProperty.getPropertyPath(), isEmptyString());
    assertThat(arrayProperty.getArrayPath(), is("my.arr"));
    assertThat(arrayProperty.getFullName(), is("my.arr.age"));
    assertThat(arrayProperty.getType(), is(DataPropertyType.Decimal));
  }

  @Test
  void should_add_array_property_with_long_path() {
    DataSchema schema = new DataSchema();

    schema.addProperty("my", null, DataPropertyType.Object);
    schema.addProperty("arr", "my", DataPropertyType.Array);
    schema.addProperty("prop", "my.arr", DataPropertyType.Object);
    schema.addProperty("age", "my.arr.prop", DataPropertyType.Decimal);

    assertThat(schema.getAllNames(), notNullValue());
    assertThat(schema.getAllNames(), hasSize(4));

    assertThat(schema.getArrayProperties(), notNullValue());
    assertThat(schema.getArrayProperties(), hasSize(2));

    DataArrayProperty firstArrProp = schema.getArrayProperties().get(0);
    assertThat(firstArrProp.getName(), is("prop"));
    assertThat(
        firstArrProp.getPropertyPath(),
        isEmptyString()); // no property explicit path contains, only array path!
    assertThat(firstArrProp.getArrayPath(), is("my.arr"));
    assertThat(firstArrProp.getFullName(), is("my.arr.prop"));
    assertThat(firstArrProp.getType(), is(DataPropertyType.Object));

    DataArrayProperty secondArrProp = schema.getArrayProperties().get(1);
    assertThat(secondArrProp.getName(), is("age"));
    assertThat(secondArrProp.getPropertyPath(), is("prop"));
    assertThat(secondArrProp.getArrayPath(), is("my.arr"));
    assertThat(secondArrProp.getFullName(), is("my.arr.prop.age"));
    assertThat(secondArrProp.getType(), is(DataPropertyType.Decimal));
  }

  @Test
  private List<DataPropertyBase> generateDataPropertiesOfAllTypesAsDataPropertyBaseList() {
    List<DataPropertyBase> properties = new ArrayList<>();
    int i = 1;
    for (DataPropertyType type : DataPropertyType.values()) {
      properties.add(new DataProperty("name" + i++, "dummy.path", type));
    }

    return properties;
  }

  private List<DataPropertyBase> generateDataVariableReferencesOfAllTypesAsDataPropertyBaseList() {
    List<DataPropertyBase> properties = new ArrayList<>();
    int i = 1;
    for (DataPropertyType type : DataPropertyType.values()) {
      properties.add(new DataVariableReference("name" + i++, type));
    }

    return properties;
  }

  private void addAllToSchema(List<DataPropertyBase> propertyBaseList) {
    for (DataPropertyBase base : propertyBaseList) {
      schema.add(base);
    }
  }
}
