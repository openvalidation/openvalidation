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

package io.openvalidation.rest.model.dto.astDTO.transformation;

import io.openvalidation.rest.model.dto.astDTO.Range;
import java.util.List;

public class DocumentSection {
  private Range range;
  private List<String> lines;

  public DocumentSection(int startNumber, List<String> lines) {
    this.lines = lines;

    if (this.lines.size() > 0) {
      String firstLine = this.lines.get(0);
      int startColumn = firstLine.indexOf(firstLine);

      String lastLine = this.lines.get(this.lines.size() - 1);
      int lastLineIndex = startNumber + lines.size() - 1;
      int lastColumnIndex = lastLine.length();

      this.range = new Range(startNumber, startColumn, lastLineIndex, lastColumnIndex);
    }
  }

  public DocumentSection(Range range, List<String> lines) {
    this.lines = lines;
    this.range = range;
  }

  public Range getRange() {
    return range;
  }

  public void setRange(Range range) {
    this.range = range;
  }

  public List<String> getLines() {
    return lines;
  }

  public void setLines(List<String> lines) {
    this.lines = lines;
  }
}
