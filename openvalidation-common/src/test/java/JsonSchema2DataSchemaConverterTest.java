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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataArrayProperty;
import io.openvalidation.common.data.DataProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class JsonSchema2DataSchemaConverterTest {

  @Test
  public void should_convert_simple_schema() throws Exception {
    String json = "{type:object,properties:{a:{type:string},b:{type:boolean}}}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(2));

    DataProperty property = schema.getProperties().stream().findFirst().get();

    assertThat(property, is(notNullValue()));
    assertThat(property.getPath(), is(""));
    assertThat(property.getName(), is("a"));
    assertThat(property.getFullName(), is("a"));
    assertThat(property.getType(), is(DataPropertyType.String));

    DataProperty property2 = schema.getProperties().stream().collect(Collectors.toList()).get(1);

    assertThat(property2, is(notNullValue()));
    assertThat(property2.getPath(), is(""));
    assertThat(property2.getName(), is("b"));
    assertThat(property2.getFullName(), is("b"));
    assertThat(property2.getType(), is(DataPropertyType.Boolean));
  }

  @Test
  public void should_convert_nested_objects_schema() throws Exception {
    String json =
        "{type:object,properties:{a:{type:string},b:{type:object,properties:{c:{type:number}}}}}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(3));

    DataProperty property = schema.getProperties().get(0);

    assertThat(property, is(notNullValue()));
    assertThat(property.getPath(), is(""));
    assertThat(property.getName(), is("a"));
    assertThat(property.getFullName(), is("a"));
    assertThat(property.getType(), is(DataPropertyType.String));

    DataProperty property2 = schema.getProperties().get(1);

    assertThat(property2, is(notNullValue()));
    assertThat(property2.getPath(), is(""));
    assertThat(property2.getName(), is("b"));
    assertThat(property2.getFullName(), is("b"));
    assertThat(property2.getType(), is(DataPropertyType.Object));

    DataProperty property3 = schema.getProperties().get(2);

    assertThat(property3, is(notNullValue()));
    assertThat(property3.getPath(), is("b"));
    assertThat(property3.getName(), is("c"));
    assertThat(property3.getFullName(), is("b.c"));
    assertThat(property3.getType(), is(DataPropertyType.Decimal));
  }

  @Test
  public void should_convert_enum_schema() throws Exception {
    String json = "{type:object,properties:{a:{type:string, enum:[\"abc\",\"efg\",\"hij\"]}}}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(1));

    DataProperty property = schema.getProperties().get(0);
    assertThat(property, is(notNullValue()));
    assertThat(property.getName(), is("a"));
    assertThat(property.getType(), is(DataPropertyType.Enum));

    String[] enumValues = property.getEnumValues();

    assertThat(enumValues, is(notNullValue()));
    assertThat(enumValues.length, is(3));

    assertThat(new ArrayList<>(Arrays.asList(enumValues)), contains("abc", "efg", "hij"));
  }

  @Test
  public void should_convert_array_schema() throws Exception {
    String json =
        "{type:object,properties:{a:{type:array, items:{type:object, properties:{value:{type:integer}}}}}}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, notNullValue());
    assertThat(schema.getAllNames(), notNullValue());
    assertThat(schema.getAllNames(), hasSize(2));
    assertThat(schema.getProperties(), hasSize(1));
    assertThat(schema.getArrayProperties(), hasSize(1));

    DataProperty aProperty = schema.getProperties().get(0);
    assertThat(aProperty, notNullValue());
    assertThat(aProperty.getName(), is("a"));
    assertThat(aProperty.getFullName(), is("a"));
    assertThat(aProperty.getPath(), is(""));
    assertThat(aProperty.getType(), is(DataPropertyType.Array));

    DataArrayProperty valueProperty = schema.getArrayProperties().get(0);
    assertThat(valueProperty, notNullValue());
    assertThat(valueProperty.getName(), is("value"));
    assertThat(valueProperty.getFullName(), is("a.value"));
    assertThat(valueProperty.getArrayPath(), is("a"));
    assertThat(valueProperty.getPropertyPath(), isEmptyString());
    assertThat(valueProperty.getType(), is(DataPropertyType.Decimal));
  }

  @Test
  @Disabled
  public void should_convert_enum_array_schema() throws Exception {
    String json =
        "{type:object,properties:{a:{type:array, items:{type:string, enum:[\"abc\",\"efg\",\"hij\"]}}}}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(1));

    DataProperty property = schema.getProperties().get(0);
    assertThat(property, is(notNullValue()));
    assertThat(property.getName(), is("a"));
    assertThat(property.getType(), is(DataPropertyType.Array));
    assertThat(property.getTypeOfArray(), is(DataPropertyType.Enum));

    DataProperty propOfArray = property.getPropertyOfArray();

    assertThat(propOfArray, is(notNullValue()));
    assertThat(propOfArray.getType(), is(DataPropertyType.Enum));

    String[] enumValues = propOfArray.getEnumValues();

    assertThat(enumValues, is(notNullValue()));
    assertThat(enumValues.length, is(3));

    assertThat(new ArrayList<>(Arrays.asList(enumValues)), contains("abc", "efg", "hij"));
  }

  @ParameterizedTest
  @CsvSource(value = {
          "string,String",
          "number,Decimal",
          "integer,Decimal",
          "date,Date",
          "array,Array",
          "boolean,Boolean"})
  public void should_resolve_type_of_array_contents_with_string(String input, String expected) throws Exception {
    String json =
        "{\n" +
            "  \"definitions\": {},\n" +
            "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
            "  \"$id\": \"http://example.com/root.json\",\n" +
            "  \"type\": \"object\",\n" +
            "  \"title\": \"The Root Schema\",\n" +
            "  \"required\": [\n" +
            "    \"numbers\"\n" +
            "  ],\n" +
            "  \"properties\": {\n" +
            "    \"numbers\": {\n" +
            "      \"$id\": \"#/properties/numbers\",\n" +
            "      \"type\": \"array\",\n" +
            "      \"title\": \"The Numbers Schema\",\n" +
            "      \"items\": {\n" +
            "        \"$id\": \"#/properties/numbers/items\",\n" +
            "        \"type\": \""+input+"\",\n" +
            "        \"title\": \"The Items Schema\",\n" +
            "        \"default\": 0,\n" +
            "        \"examples\": [\n" +
            "          abc,\n" +
            "          def\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(1));

    DataProperty property = schema.getProperties().get(0);
    assertThat(property, is(notNullValue()));
    assertThat(property.getName(), is("numbers"));
    assertThat(property.getType(), is(DataPropertyType.Array));
    assertThat(property.getArrayContentType().toString(), is(expected)); // DataPropertyType.String
  }
}
