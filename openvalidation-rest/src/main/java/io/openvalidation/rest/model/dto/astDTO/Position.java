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

public class Position {
  private int line;
  private int column;

  public Position(int line, int column) {
    this.line = line;
    this.column = column;
  }

  public Position(Position position) {
    this.line = position.getLine();
    this.column = position.getColumn();
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Position) {
      return ((Position) obj).column == this.column && ((Position) obj).line == this.line;
    }
    return false;
  }
}
