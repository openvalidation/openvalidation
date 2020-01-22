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

import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataProperty;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.data.DataVariableReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DataSchemaUniquePropertiesTest {
  private DataSchema resolve(String schema, List<DataVariableReference> variableReferences)
      throws Exception {
    DataSchema dataSchema = SchemaConverterFactory.convert(schema);
    dataSchema.completeWithVariables(variableReferences);

    return dataSchema;
  }

  private HashMap<String, DataProperty> extractUniqueProperties(String schema) throws Exception {
    return resolve(schema, Collections.emptyList()).getUniqueProperties();
  }

  private String buildFlatRawSchemaWithUniquesOnly(int size) {
    StringBuilder sb = new StringBuilder();
    if (size > 0) {
      sb.append("prop0 : something");
      for (int i = 1; i < size; i++) {
        sb.append(", prop").append(i).append(" : something");
      }
    }

    return "{" + sb.toString() + "}";
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "{name: Peter, age: 25, city: London, zipcode: 12345, haircolour: brown}",
        "{person: {name: Peter, age: 25, city: London, zipcode: 12345}}",
        "{person: {info: {name: Peter, age: 25, city: London}}}",
        "{person: {general: {name: Peter}, misc: {city: London}}}",
        "{someone: {name: Peter, age: 25}, me: {name: Hans, age: 52}, competitors: true, friends: true, coworkers: true}",
        "{someone: {info: {name: Peter, age: 25}}, me: {info: {name: Hans, age: 52}}, competitors: true, friends: true, coworkers: true}",
        "{someone: {info: {name: Peter, age: 20}, cool: true, wife: {cool : true}}, hans: {info: {name: Hans, age: 20}, married: false}, likable: true}"
      })
  void should_have_5_unique_properties(String rawSchema) throws Exception {
    // act
    HashMap<String, DataProperty> uniqueProperties = extractUniqueProperties(rawSchema);

    // assert
    assertThat(uniqueProperties.size(), is(5));
  }

  @ParameterizedTest
  @ValueSource(ints = {10, 100, 500, 1000})
  void should_have_given_amount_of_unique_properties(int numberOfUniques) throws Exception {
    String rawSchema = buildFlatRawSchemaWithUniquesOnly(numberOfUniques);

    HashMap<String, DataProperty> uniqueProperties = extractUniqueProperties(rawSchema);

    assertThat(uniqueProperties.size(), is(numberOfUniques));
  }
}
