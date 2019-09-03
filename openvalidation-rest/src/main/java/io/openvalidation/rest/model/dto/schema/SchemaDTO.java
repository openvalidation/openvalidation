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

import io.openvalidation.common.data.DataProperty;
import io.openvalidation.common.data.DataSchema;
import java.util.ArrayList;
import java.util.List;

public class SchemaDTO {
  private List<DataPropertyDTO> dataProperties;
  private List<ComplexDataPropertyDTO> complexData;

  public SchemaDTO(DataSchema schema) {
    this.dataProperties = new ArrayList<>();
    this.complexData = new ArrayList<>();

    for (DataProperty property : schema.getProperties()) {
      this.dataProperties.add(new DataPropertyDTO(property.getFullName(), property.getType()));

      // For nested properties
      if (!property.getFullName().equals(property.getName())) {
        this.dataProperties.add(new DataPropertyDTO(property.getName(), property.getType()));
      }

      if (!property.getPath().isEmpty()) {
        this.complexData.add(new ComplexDataPropertyDTO(property));
      }
    }
  }

  public List<DataPropertyDTO> getDataProperties() {
    return dataProperties;
  }

  public void setDataProperties(List<DataPropertyDTO> dataProperties) {
    this.dataProperties = dataProperties;
  }

  public List<ComplexDataPropertyDTO> getComplexData() {
    return complexData;
  }

  public void setComplexData(List<ComplexDataPropertyDTO> complexData) {
    this.complexData = complexData;
  }
}
