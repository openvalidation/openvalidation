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
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericNode {
  private List<String> lines;
  private Range range;
  private int startNumber;
  private List<KeywordNode> keywords;

  public void initializeElement(DocumentSection section) {
    if (section == null) {
      this.lines = new ArrayList<>();
    } else {
      this.lines = section.getLines();
      this.range = section.getRange();
    }
    this.keywords = new ArrayList<>();
  }

  public void initializeElement(DocumentSection section, List<String> keywordTokens) {
    this.initializeElement(section);
    this.tokenizeKeywords(keywordTokens);
  }

  public List<String> getLines() {
    return lines;
  }

  public void setLines(List<String> lines) {
    this.lines = lines;
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

  public List<KeywordNode> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<KeywordNode> keywords) {
    this.keywords = keywords;
  }

  private void tokenizeKeywords(List<String> tokens) {
    for (String token : tokens) {
      DocumentSection section = new RangeGenerator(this.lines, this.range).generate(token);
      if (section.getRange() == null) continue;

      KeywordNode node = new KeywordNode(section);
      this.keywords.add(node);
    }
  }
}
