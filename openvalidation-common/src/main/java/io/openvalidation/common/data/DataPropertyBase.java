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

public abstract class DataPropertyBase {
  private String _name;

  private String _fullName;
  private String _fullNameLowerCase;
  private DataPropertyType _type;
  private DataPropertyType _arrayContentType;

  public DataPropertyBase(String name, DataPropertyType type) {
    this(name, type, null);
  }

  public DataPropertyBase(String name, DataPropertyType type, DataPropertyType arrayContentType) {
    this._name = name;
    this.setFullName(this._name);
    this._type = type;
    this._arrayContentType = arrayContentType;
  }

  public String getName() {
    return this._name;
  }

  public String getFullName() {
    return this._fullName;
  }

  public void setFullName(String fullName) {
    this._fullName = fullName;
    this._fullNameLowerCase = (this._fullName != null) ? this._fullName.toLowerCase() : null;
  }

  public String getFullNameLowerCase() {
    return this._fullNameLowerCase;
  }

  public DataPropertyType getType() {
    return this._type;
  }

  public void setType(DataPropertyType _type) {
    this._type = _type;
  }

  @Override
  public String toString() {
    return this.getFullName() + " : " + this.getType().name();
  }

  public DataPropertyType get_arrayContentType() {
    return _arrayContentType;
  }

  public void set_arrayContentType(DataPropertyType _arrayContentType) {
    this._arrayContentType = _arrayContentType;
  }
}
