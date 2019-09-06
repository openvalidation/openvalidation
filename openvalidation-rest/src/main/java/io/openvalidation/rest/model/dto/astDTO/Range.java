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

public class Range {
  private Position start;
  private Position end;

  public Range() {
    // Serializable
  }

  public Range(Position start, Position end) {
    this.start = start;
    this.end = end;
  }

  public Range(int startLine, int startColumn, int endLine, int endColumn) {
    this.start = new Position(startLine, startColumn);
    this.end = new Position(endLine, endColumn);
  }

  public Range(Range outerRange) {
    this.start = new Position(outerRange.getStart().getLine(), outerRange.getStart().getColumn());
    this.end = new Position(outerRange.getEnd().getLine(), outerRange.getEnd().getColumn());
  }

  public Position getStart() {
    return start;
  }

  public Position getEnd() {
    return end;
  }

  public boolean includesPosition(Position position) {
    boolean afterStart = (this.getStart().getLine() == position.getLine() &&
            this.getStart().getColumn() <= position.getColumn()) ||
            this.getStart().getLine() < position.getLine();
    boolean beforeEnd = (this.getEnd().getLine() == position.getLine() &&
            this.getEnd().getColumn() >= position.getColumn()) ||
            this.getEnd().getLine() > position.getLine();
    return afterStart && beforeEnd;
  }
}
