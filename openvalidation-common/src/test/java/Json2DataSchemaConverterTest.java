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

import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataArrayProperty;
import io.openvalidation.common.data.DataProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Json2DataSchemaConverterTest {

  //todo jgeske 04.11.19 test json-object -> json-schema -> data schema pipeline here

  @Test
  public void should_convert_simple_schema() throws Exception {
    String json = "{a:1,b:true}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(2));

    DataProperty property = schema.getProperties().stream().findFirst().get();

    assertThat(property, is(notNullValue()));
    assertThat(property.getPath(), is(""));
    assertThat(property.getName(), is("a"));
    assertThat(property.getFullName(), is("a"));
    assertThat(property.getType(), is(DataPropertyType.Decimal));

    DataProperty property2 = schema.getProperties().stream().collect(Collectors.toList()).get(1);

    assertThat(property2, is(notNullValue()));
    assertThat(property2.getPath(), is(""));
    assertThat(property2.getName(), is("b"));
    assertThat(property2.getFullName(), is("b"));
    assertThat(property2.getType(), is(DataPropertyType.Boolean));
  }

  @Test
  public void should_convert_nested_schema() throws Exception {
    String json = "{a:{b:{c:\"value\"}}}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(3));

    DataProperty aProp = schema.findPropertyByFullName("a");
    DataProperty bProp = schema.findPropertyByFullName("a.b");
    DataProperty cProp = schema.findPropertyByFullName("a.b.c");

    assertThat(aProp, is(notNullValue()));
    assertThat(bProp, is(notNullValue()));
    assertThat(cProp, is(notNullValue()));

    assertThat(aProp.getName(), is("a"));
    assertThat(bProp.getName(), is("b"));
    assertThat(cProp.getName(), is("c"));

    assertThat(aProp.getPath(), is(""));
    assertThat(bProp.getPath(), is("a"));
    assertThat(cProp.getPath(), is("a.b"));

    assertThat(aProp.getType(), is(DataPropertyType.Object));
    assertThat(bProp.getType(), is(DataPropertyType.Object));
    assertThat(cProp.getType(), is(DataPropertyType.String));
  }

  @Test
  public void should_convert_available_types_schema() throws Exception {
    String json = "{a:\"String\",b:true,c:1,d:1.2,e:{f:false},g:[]}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(7));

    DataProperty aProp = schema.findPropertyByFullName("a");
    DataProperty bProp = schema.findPropertyByFullName("b");
    DataProperty cProp = schema.findPropertyByFullName("c");
    DataProperty dProp = schema.findPropertyByFullName("d");
    DataProperty eProp = schema.findPropertyByFullName("e");
    DataProperty fProp = schema.findPropertyByFullName("e.f");
    DataProperty gProp = schema.findPropertyByFullName("g");

    assertThat(aProp, is(notNullValue()));
    assertThat(bProp, is(notNullValue()));
    assertThat(cProp, is(notNullValue()));
    assertThat(dProp, is(notNullValue()));
    assertThat(eProp, is(notNullValue()));
    assertThat(fProp, is(notNullValue()));
    assertThat(gProp, is(notNullValue()));

    assertThat(aProp.getType(), is(DataPropertyType.String));
    assertThat(bProp.getType(), is(DataPropertyType.Boolean));
    assertThat(cProp.getType(), is(DataPropertyType.Decimal));
    assertThat(dProp.getType(), is(DataPropertyType.Decimal));
    assertThat(eProp.getType(), is(DataPropertyType.Object));
    assertThat(fProp.getType(), is(DataPropertyType.Boolean));
    assertThat(gProp.getType(), is(DataPropertyType.Array));
  }

  @Test
  public void should_return_true_for_existing_property() throws Exception {
    String json = "{a:\"String\",b:true,c:1,d:1.2,e:{f:false},g:[]}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(7));

    assertThat(schema.exists("e.f"), is(true));
    assertThat(schema.exists("e.g"), is(false));
  }

  @Test
  public void should_create_special_array_properties() throws Exception {
    String json = "{" + "my:{" + "arr:[" + "{" + "prop:{" + "age:1" + "}" + "}" + "]" + "}" + "}";

    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, notNullValue());
    assertThat(schema.getAllNames(), hasSize(4));
    assertThat(schema.getArrayProperties(), hasSize(2));

    DataArrayProperty prop = schema.getArrayProperties().get(1);
    assertThat(prop.getType(), is(DataPropertyType.Decimal));
    assertThat(prop.getName(), is("age"));
    assertThat(prop.getPropertyPath(), is("prop"));
    assertThat(prop.getArrayPath(), is("my.arr"));
  }

  @Test
  public void should_create_embedded_arrays() throws Exception {
    String json =
        "{"
            + "my:{"
            + "arr:["
            + "{"
            + "prop:{"
            + "ages:["
            + "{value:1}"
            + "]"
            + "}"
            + "}"
            + "]"
            + "}"
            + "}";

    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, notNullValue());
    assertThat(schema.getAllNames(), hasSize(5));
    assertThat(schema.getArrayProperties(), hasSize(3));

    DataArrayProperty prop1 = schema.getArrayProperties().get(0);
    assertThat(prop1.getType(), is(DataPropertyType.Object));
    assertThat(prop1.getName(), is("prop"));
    assertThat(prop1.getPropertyPath(), isEmptyString());
    assertThat(prop1.getArrayPath(), is("my.arr"));

    DataArrayProperty prop2 = schema.getArrayProperties().get(1);
    assertThat(prop2.getType(), is(DataPropertyType.Array));
    assertThat(prop2.getName(), is("ages"));
    assertThat(prop2.getPropertyPath(), is("prop"));
    assertThat(prop2.getArrayPath(), is("my.arr"));

    DataArrayProperty prop3 = schema.getArrayProperties().get(2);
    assertThat(prop3.getType(), is(DataPropertyType.Decimal));
    assertThat(prop3.getName(), is("value"));
    assertThat(prop3.getPropertyPath(), is("prop.ages"));
    assertThat(prop3.getArrayPath(), is("my.arr"));
  }

  @ParameterizedTest
  @CsvSource(value = {
          "banana;String",
          "banana, apple;String",
          "banana, 1, 1.5, true, [1,2,3];String",
          "1;Decimal",
          "1, 2, 3;Decimal",
          "1, banana, 1.5, true, [1,2,3];Decimal",
          "1.5;Decimal",
          "1.5, 2.5, 3.5;Decimal",
          "1.5, banana, 1, true, [1,2,3];Decimal",
          //"date;Date",
          "[1,2,3];Array",
          "[1,2,3], [4,5,6];Array",
          "[1,2,3], banana, 1.5, true;Array",
          "true;Boolean",
          "true, false;Boolean",
          "true, 1.5, banana, 1, [1,2,3];Boolean"}, delimiter = ';')
  public void should_resolve_type_of_array_contents_with_string(String input, String expected) throws Exception {
    String json =
        "{dataArray : [" + input + "]}";
    DataSchema schema = SchemaConverterFactory.convert(json);

    assertThat(schema, is(notNullValue()));
    assertThat(schema.getProperties().size(), is(1));

    DataProperty property = schema.getProperties().get(0);
    assertThat(property, is(notNullValue()));
    assertThat(property.getName(), is("dataArray"));
    assertThat(property.getType(), is(DataPropertyType.Array));
    assertThat(property.getArrayContentType().toString(), is(expected)); // DataPropertyType.String
  }
}
