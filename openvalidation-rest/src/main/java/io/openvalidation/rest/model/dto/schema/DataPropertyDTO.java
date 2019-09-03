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

package io.openvalidation.rest.model.dto.schema;

import io.openvalidation.common.data.DataPropertyType;

public class DataPropertyDTO {

  private String name;
  private DataPropertyType type;

  public DataPropertyDTO(String name, DataPropertyType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String fullname) {
    this.name = fullname;
  }

  public DataPropertyType getType() {
    return type;
  }

  public void setType(DataPropertyType type) {
    this.type = type;
  }
}
