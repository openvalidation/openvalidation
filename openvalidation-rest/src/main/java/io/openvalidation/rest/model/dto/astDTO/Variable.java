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

package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.data.DataPropertyType;

public class Variable {
  private String name;
  private DataPropertyType dataType;

  public Variable(ASTVariable variable) {
    this.name = variable.getName();
    this.dataType = variable.getDataType();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataPropertyType getDataType() {
    return dataType;
  }

  public void setDataType(DataPropertyType dataType) {
    this.dataType = dataType;
  }
}
