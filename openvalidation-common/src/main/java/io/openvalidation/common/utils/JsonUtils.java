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

package io.openvalidation.common.utils;

import io.openvalidation.common.data.DataPropertyType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtils {

  private static final Pattern jsonArrayPattern = Pattern.compile("\\[(.+,?)*]$");

  public static void validate(String json, String schema) {
    JSONObject jsonData = loadJson(json);
    JSONObject jsonSchema = loadJson(schema);

    validate(jsonData, jsonSchema);
  }

  public static void validate(JSONObject jsonData, JSONObject jsonSchema) {
    Schema sma = SchemaLoader.load(jsonData);
    sma.validate(jsonData);
  }

  public static boolean isValid(String json, String schema) {
    return isValid(loadJson(json), loadJson(schema));
  }

  public static boolean isValid(JSONObject jsonData, JSONObject jsonSchema) {
    try {
      validate(jsonData, jsonSchema);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static JSONObject loadJson(String json) {
    if (!isValidJsonFormat(json)) return null;

    return new JSONObject(new JSONTokener(StringUtils.toInputStream(json)));
  }

  public static Schema loadJsonSchema(String rawJson) {
    JSONObject json = loadJson(rawJson);

    return SchemaLoader.load(json);
  }

  public static DataPropertyType parseType(Object object) {

    if (object instanceof Boolean) return DataPropertyType.Boolean;
    if (object instanceof Float) return DataPropertyType.Decimal;
    if (object instanceof Double) return DataPropertyType.Decimal;
    if (object instanceof Integer) return DataPropertyType.Decimal;
    if (object instanceof String) return DataPropertyType.String;
    if (object instanceof JSONArray) return DataPropertyType.Array;

    return DataPropertyType.Object;
  }

  public static DataPropertyType parseArrayContentType(JSONArray jsonArray) {
    DataPropertyType type = DataPropertyType.Unknown;
    if (jsonArray.length() > 0) {
      String firstElementString = String.valueOf(jsonArray.get(0));
      type = parseTypeFromString(firstElementString);
    }

    return type;
  }

  public static DataPropertyType parseTypeFromString(String firstElementString) {
    DataPropertyType type;

    if (NumberParsingUtils.isNumber(firstElementString)) {
      type = DataPropertyType.Decimal;
    } else if (StringUtils.isBoolean(firstElementString)) {
      type = DataPropertyType.Boolean;
    } else {
      Matcher jsonArrayMatcher = jsonArrayPattern.matcher(firstElementString);
      if (jsonArrayMatcher.matches()) {
        type = DataPropertyType.Array;
      } else {
        type = DataPropertyType.String;
      }
    }
    return type;
  }

  public static DataPropertyType parseTypeBySchemaProperty(JSONObject schemaProperty) {
    Object type =
        (schemaProperty != null && schemaProperty.has("type"))
            ? schemaProperty.get("type")
            : "object";

    switch ((String) type) {
      case "boolean":
        return DataPropertyType.Boolean;
      case "integer":
        return DataPropertyType.Decimal;
      case "number":
        return DataPropertyType.Decimal;
      case "string":
        return (schemaProperty.has("enum")) ? DataPropertyType.Enum : DataPropertyType.String;
      case "date":
        return DataPropertyType.Date;
      case "array":
        return DataPropertyType.Array;
    }

    return DataPropertyType.Object;
  }

  public static String getSchemaAsString(JSONObject jsonObject) throws Exception {
    StringBuilder sb = new StringBuilder();

    for (Object key : jsonObject.keySet()) {

      String keyStr = (String) key;
      Object value = jsonObject.get(keyStr);

      // Print key and value
      sb.append("key: " + keyStr + " value: " + value + "\n");
      // Console.print("key: "+ keyStr + " value: " + value);

      // for nested objects iteration if required
      if (value instanceof JSONObject) sb.append(getSchemaAsString((JSONObject) value));
    }

    return sb.toString();
  }

  public static boolean isValidJsonFormat(String rawJson) {
    try {
      new JSONObject(rawJson);
    } catch (JSONException ex) {
      try {
        new JSONArray(rawJson);
      } catch (JSONException ex1) {
        return false;
      }
    }
    return true;
  }

  public static boolean isJsonData(String rawJson) {
    JSONObject json = loadJson(rawJson);

    return (json != null);
  }

  public static boolean isJsonSchema(String rawJson) {
    JSONObject json = loadJson(rawJson);

    if (json != null) {
      return json.has("properties");
    }

    return false;
  }

  public static void combineProperties(JSONObject target, JSONObject source) {
    if (source == null
        || !source.has("properties")
        || source.getJSONObject("properties").keySet().size() < 1) return;
    if (target == null) target = new JSONObject();

    if (!target.has("properties")) target.put("properties", new JSONObject());

    JSONObject targetProperties = target.getJSONObject("properties");
    JSONObject sourceProperties = source.getJSONObject("properties");

    for (String key : sourceProperties.keySet()) {
      targetProperties.put(key, sourceProperties.getJSONObject(key));
    }
  }
}
