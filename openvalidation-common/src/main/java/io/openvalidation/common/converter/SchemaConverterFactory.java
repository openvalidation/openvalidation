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

package io.openvalidation.common.converter;

import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.utils.JsonUtils;

public class SchemaConverterFactory {

  public static ISchemaConverter create(String rawSchema) throws Exception {
    if (JsonUtils.isJsonSchema(rawSchema)) return new Jsonschema2DataSchemaConverter(rawSchema);

    if (JsonUtils.isJsonData(rawSchema)) return new Json2DataSchemaConverter(rawSchema);

    throw new OpenValidationException(
        "invalid JSON Schema Format. Should be json data or json schema in json or yaml format!\n"
            + rawSchema);
  }

  public static DataSchema convert(String rawSchema) throws Exception {
    ISchemaConverter schemaConverter = SchemaConverterFactory.create(rawSchema);
    DataSchema schema = schemaConverter.convert();
    schema.determineUniqueProperties();
    return schema;
  }
}
