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

package io.openvalidation.common.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataProperty extends DataPropertyBase {

  private String _path;
  private String[] _enumValues;
  private DataProperty _propOfArray;

  public DataProperty(String name, String path, DataPropertyType type) {
    this(name, path, type, null);
  }

  public DataProperty(
      String name, String path, DataPropertyType type, DataPropertyType arrayContentType) {
    super(name, type, arrayContentType);
    this._path = path;
    this.setFullName(
        (this._path != null && this._path.length() > 0)
            ? this._path + "." + this.getName()
            : this.getName());
  }

  public void setEnumValues(String[] values) {
    this._enumValues = values;
  }

  public void setPropertyOfArray(DataProperty prop) {
    this._propOfArray = prop;
  }

  public String getPath() {
    return this._path;
  }

  public String[] getEnumValues() {
    return this._enumValues;
  }

  public DataProperty getPropertyOfArray() {
    return this._propOfArray;
  }

  public DataPropertyType getTypeOfArray() {
    return (this._propOfArray != null) ? this._propOfArray.getType() : null;
  }

  public String[] getFullNameAsParts() {

    List<String> parts = new ArrayList<>();
    if (this.getPath() != null && this.getPath().length() > 0)
      Arrays.stream(this.getPath().split("\\."))
          .forEach(
              s -> {
                parts.add(s.trim());
              });

    parts.add(getName());

    return parts.toArray(new String[0]);
  }
}
