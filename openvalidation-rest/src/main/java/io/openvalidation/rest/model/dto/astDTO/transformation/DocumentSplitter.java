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

import java.util.ArrayList;

public class DocumentSplitter {
  private String wholeDocument;

  public DocumentSplitter(String wholeDocument) {
    this.wholeDocument = wholeDocument;
  }

  public ArrayList<DocumentSection> splitDocument() {
    ArrayList<DocumentSection> sections = new ArrayList<>();
    if (this.wholeDocument == null) return sections;

    String[] splittedDocument = this.wholeDocument.split("\n");

    ArrayList<String> currentLines = new ArrayList<>();
    int startLineIndex = 0;
    int index = 0;

    for (String split : splittedDocument) {
      if (!split.trim().isEmpty()) {
        if (currentLines.size() == 0) {
          startLineIndex = index;
        }

        currentLines.add(split);
      } else {
        if (currentLines.size() > 0) {
          DocumentSection section = new DocumentSection(startLineIndex, currentLines);
          sections.add(section);
          currentLines = new ArrayList<>();
        }
      }
      index++;
    }

    if (!currentLines.isEmpty()) {
      DocumentSection section = new DocumentSection(startLineIndex, currentLines);
      sections.add(section);
    }

    return sections;
  }
}
