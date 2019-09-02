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
import org.json.JSONArray;
import org.json.JSONObject;

public class Json2DataSchemaConverter implements ISchemaConverter {

  private static int MAX_DEEP = 10;
  private String _rawSchema;

  public Json2DataSchemaConverter(String rawSchema) {
    this._rawSchema = rawSchema;
  }

  public DataSchema convert() throws Exception {

    DataSchema schema = new DataSchema();

    try {
      JSONObject data = JsonUtils.loadJson(_rawSchema);

      if (data.keySet() == null || data.keySet().size() < 1)
        throw new OpenValidationException(
            "JSON Schema schould not be empty: \n" + _rawSchema + "\n");

      fillSchema(data, schema, "", 0);

      // schema.sort();
    } catch (OpenValidationException e) {
      throw e;
    } catch (Exception e) {
      throw new OpenValidationException(
          "invalid JSON Schema argument: \n" + _rawSchema + "\n",
          "invalid JSON Schema argument: \n" + _rawSchema + "\n",
          e);
    }

    return schema;
  }

  private void fillSchema(JSONObject object, DataSchema schema, String path, int level) {

    for (Object key : object.keySet()) {
      String name = (String) key;
      Object value = object.get(name);
      String pth = (path != null && path.length() > 0) ? path : "";
      String fullName = (path != null && path.length() > 0) ? path + "." + name : name;

      schema.addProperty(name, pth, JsonUtils.parseType(value));

      if (value instanceof JSONObject) fillSchema((JSONObject) value, schema, fullName, level + 1);

      if (value instanceof JSONArray) {
        ((JSONArray) value)
            .forEach(
                p -> {
                  if (p instanceof JSONObject || p instanceof JSONArray)
                    fillSchema((JSONObject) p, schema, fullName, level + 1);
                });
      }
    }
  }

  public static DataSchema convertSchema(String rawSchema) throws Exception {
    Json2DataSchemaConverter converter = new Json2DataSchemaConverter(rawSchema);

    return converter.convert();
  }
}
