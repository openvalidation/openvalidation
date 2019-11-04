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

import io.openvalidation.common.utils.LINQ;

public class DataArrayProperty extends DataPropertyBase {
  private String _arrayPath;
  private String _propertyPath;

  public DataArrayProperty(String name, String propertyPath, String arrayPath, DataPropertyType type){
    this(name, propertyPath, arrayPath, type, null);
  }

  public DataArrayProperty(
      String name, String propertyPath, String arrayPath, DataPropertyType type, DataPropertyType arrayContentType) {
    super(name, type, arrayContentType);
    this.setFullName(
        (propertyPath != null && propertyPath.length() > 0)
            ? (arrayPath + "." + propertyPath + "." + name)
            : (arrayPath + "." + name));
    this._arrayPath = arrayPath;
    this._propertyPath = propertyPath;
  }

  public String getPropertyPath() {
    return _propertyPath;
  }

  public String getArrayPath() {
    return _arrayPath;
  }

  public String getFullPathExceptArrayPath() {
    return (_propertyPath != null && _propertyPath.length() > 0)
        ? _propertyPath + "." + this.getName()
        : this.getName();
  }

  public String[] getFullPathExceptArrayPathAsArray() {
    return getFullPathExceptArrayPath().split("\\.");
  }

  public String[] getPropertyPathAsList() {
    if (this.getPropertyPath() != null) return this.getPropertyPath().split("\\.");

    return null;
  }

  public String[] getArrayPathAsList() {
    if (this.getArrayPath() == null) return null;

    if (this.getArrayPath().contains(".")) return this.getArrayPath().split("\\.");
    else return LINQ.array(this.getArrayPath());
  }

  public String[] getFullPathAsArray() {
    StringBuilder fullPath = new StringBuilder();
    if (_arrayPath != null && !_arrayPath.isEmpty()) fullPath.append(_arrayPath).append(".");
    if (_propertyPath != null && !_propertyPath.isEmpty())
      fullPath.append(_propertyPath).append(".");
    fullPath.append(this.getName());

    return fullPath.toString().split("\\.");
  }
}
