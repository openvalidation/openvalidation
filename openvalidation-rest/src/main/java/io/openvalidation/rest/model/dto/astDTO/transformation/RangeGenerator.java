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

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.rest.model.dto.astDTO.Position;
import io.openvalidation.rest.model.dto.astDTO.Range;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;

public class RangeGenerator {
  private List<String> outerLines;
  private Range outerRange;

  public RangeGenerator(String text) {
    this.outerLines = text != null && !text.isEmpty() ? Arrays.asList(text.split("\n")) : null;
    this.outerRange = null;
  }

  public RangeGenerator(DocumentSection section) {
    if (section != null) {
      this.outerLines = section.getLines();
      this.outerRange = section.getRange();
    } else {
      this.outerLines = null;
      this.outerRange = null;
    }
  }

  public RangeGenerator(List<String> lines, Range range) {
    this.outerLines = lines;
    this.outerRange = range;
  }

  public DocumentSection generate(ASTItem innerElement) {
    String sourceText = this.getOriginalSource(innerElement);
    return this.generate(sourceText);
  }

  public DocumentSection generate(String sourceText) {
    if (sourceText == null) return null;

    List<String> innerLines = Arrays.asList(sourceText.split("\n"));
    innerLines = innerLines.stream().filter(l -> !l.isEmpty()).collect(Collectors.toList());

    if (innerLines.size() == 0 || this.outerLines == null || this.outerLines.size() == 0)
      return null;

    int outerStartLine = 0;
    int outerStartColumn = 0;
    if (this.outerRange != null && this.outerRange.getStart() != null) {
      outerStartLine = this.outerRange.getStart().getLine();
      outerStartColumn = this.outerRange.getStart().getColumn();
    }

    String startLine = innerLines.get(0);
    String endLine = innerLines.get(innerLines.size() - 1);
    Position startPosition = null;
    Position endPosition = null;

    int lineNumber = 0;

    for (String line : this.outerLines) {
      if (startPosition == null) {
        startPosition =
            getStartPosition(startLine, line, outerStartLine, outerStartColumn, lineNumber);
      }
      if (endPosition == null) {
        endPosition =
            getEndPosition(endLine, line, startPosition, startLine, outerStartLine, lineNumber);
      }

      if (endPosition != null && startPosition != null) {
        break;
      }

      lineNumber++;
    }

    Range range =
        startPosition == null || endPosition == null ? null : new Range(startPosition, endPosition);
    if (range == null) return new DocumentSection(range, new ArrayList<>());

    return new DocumentSection(range, innerLines);
  }

  private String getOriginalSource(ASTItem element) {
    if (element == null) return "";

    if (element.getOriginalSource() != null) return element.getOriginalSource();
    if (element instanceof ASTOperandStatic) return ((ASTOperandStatic) element).getValue();

    return null;
  }

  private Position getStartPosition(
      String startLine,
      String currentLine,
      int outerStartLine,
      int outerStartColumn,
      int lineNumber) {
    int startColumnNumber = -1;
    Matcher startPattern =
        Pattern.compile("(?i)\\b" + startLine.trim() + "\\b").matcher(currentLine);
    if (startPattern.find()) {
      startColumnNumber = startPattern.start();
    } else {
      startColumnNumber = currentLine.toLowerCase().indexOf(startLine.toLowerCase());
    }

    if (startColumnNumber != -1) {
      int startLineNumber = outerStartLine + lineNumber;

      if (startLineNumber == outerStartLine)
        startColumnNumber += outerStartColumn;
      return new Position(startLineNumber, startColumnNumber);
    }

    return null;
  }

  private Position getEndPosition(
      String endLine,
      String currentLine,
      Position startPosition,
      String startLine,
      int outerStartLine,
      int lineNumber) {
    int startIndex = -1;
    Matcher endPattern = Pattern.compile("(?i)\\b" + endLine.trim() + "\\b").matcher(currentLine);
    if (endPattern.find()) {
      startIndex = endPattern.start();
    } else {
      startIndex = currentLine.toLowerCase().indexOf(endLine.toLowerCase());
    }

    if (startIndex != -1) {
      int column =
              startLine.equals(endLine) && startPosition != null
                      ? startPosition.getColumn() + endLine.length()
                      : endPattern.start() + endLine.length();
      return new Position(outerStartLine + lineNumber, column);
    }

    return null;
  }
}
