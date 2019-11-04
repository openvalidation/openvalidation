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

import io.openvalidation.common.data.DataProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Jsonschema2DataSchemaConverter implements ISchemaConverter {

  //  visit this: http://json-schema-faker.js.org/
  // install converter:

  //  enums check should be implemented...

  private String _rawSchema;
  private static int MAX_DEEP = 10;

  public Jsonschema2DataSchemaConverter(String rawSchema) {
    this._rawSchema = rawSchema;
  }

  public DataSchema convert() throws Exception {
    DataSchema schema = new DataSchema();

    try {
      JSONObject json = JsonUtils.loadJson(this._rawSchema);

      if (json.keySet() == null || json.keySet().size() < 1)
        throw new OpenValidationException(
            "JSON Schema schould not be empty: \n" + _rawSchema + "\n");

      fillSchema(json, schema, "", 0);
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
    JSONObject properties = object.getJSONObject("properties");

    for (String key : properties.keySet()) {

      String name = key;
      JSONObject prop = (JSONObject) properties.get(key);
      DataPropertyType type = JsonUtils.parseTypeBySchemaProperty(prop);
      DataPropertyType arrayContentType = type == DataPropertyType.Array? JsonUtils.parseTypeBySchemaProperty((JSONObject) prop.get("items")): null;

      String pth = (path != null && path.length() > 0) ? path : "";
      String fullName = (path != null && path.length() > 0) ? path + "." + name : name;

      if (type == DataPropertyType.Enum) {
        JSONArray arr = (JSONArray) prop.get("enum");

        DataProperty p = new DataProperty(name, path, type);
        p.setEnumValues(arr.toList().stream().map(a -> a.toString()).toArray(a -> new String[a]));
        schema.addProperty(p);
      }
      //            else if (type == DataPropertyType.Array) {
      //                DataProperty p = new DataProperty(name, path, type);
      //                JSONObject itemsObj = prop.getJSONObject("items");
      //
      //                DataPropertyType t = JsonUtils.parseTypeBySchemaProperty(itemsObj);
      //                DataProperty propOfArray = new DataProperty(null,null,t);
      //
      //                if (t == DataPropertyType.Enum){
      //                    JSONArray arr = (JSONArray) itemsObj.get("enum");
      //
      // propOfArray.setEnumValues(arr.toList().stream().map(a->a.toString()).toArray(a -> new
      // String[a]));
      //                }
      //
      //                p.setPropertyOfArray(propOfArray);
      //                schema.addProperty(p);
      //            }
      else {
        schema.addProperty(name, pth, type, arrayContentType);
      }

      // go deeper...
      if (type == DataPropertyType.Object) {
        fillSchema(prop, schema, fullName, level + 1);

      } else if (type == DataPropertyType.Array) {
        JSONObject arrayItems = prop.getJSONObject("items");
        DataPropertyType arrayItemsType = JsonUtils.parseTypeBySchemaProperty(arrayItems);

        if (arrayItemsType == DataPropertyType.Object)
          fillSchema(arrayItems, schema, fullName, level + 1);
        //                else {
        //                    schema.addProperty(name, pth, type);
        //                }
      }
    }
  }
}
