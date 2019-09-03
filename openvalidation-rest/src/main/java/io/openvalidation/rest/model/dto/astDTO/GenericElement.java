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

import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericElement {
  private List<String> lines;
  private Range range;
  private int startNumber;

  public void initializeElement(DocumentSection section) {
    if (section == null) {
      this.lines = new ArrayList<>();
    } else {
      this.lines = section.getLines();
      this.range = section.getRange();
    }
  }

  public List<String> getLines() {
    return lines;
  }

  public Range getRange() {
    return range;
  }

  public void setRange(Range range) {
    this.range = range;
  }

  public String getType() {
    return this.getClass().getSimpleName();
  }
}
