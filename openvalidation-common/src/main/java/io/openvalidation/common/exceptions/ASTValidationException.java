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

package io.openvalidation.common.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.utils.ConsoleColors;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.StringUtils;

public class ASTValidationException extends OpenValidationException {
  private int _position = -1;
  private ASTItem item;

  private int errorStart = 0;
  private int errorEnd = 0;

  public ASTValidationException(String message) {
    this(message, null);
  }

  public ASTValidationException(String message, ASTItem item) {
    super(message);
    this.item = item;
    this.setUserMessage(message);
  }

  public ASTValidationException(String message, ASTItem item, int position) {
    super(message);
    this.item = item;
    this.setUserMessage(message);
    this.setGlobalElementPosition(position);
  }

  public ASTValidationException(String message, ASTItem item, int startError, int lengthError) {
    this(message, item);

    this.setErrorStart(startError);
    this.setErrorLength(lengthError);

    int srcLength = ((getSource() != null) ? getSource() : "").length();

    if (this.getErrorStart() > srcLength) this.setErrorStart(getSource().length());

    if (this.getErrorStart() < 0) this.setErrorStart(-1);
  }

  @JsonProperty
  public int getErrorStart() {
    return this.errorStart;
  }

  public void setErrorStart(int start) {
    this.errorStart = start;
  }

  @JsonProperty
  public int getErrorLength() {
    return this.errorEnd;
  }

  public void setErrorLength(int length) {
    this.errorEnd = length;
  }

  @JsonProperty
  public String getSource() {
    return (item != null) ? this.item.getPreprocessedSource() : null;
  }

  @JsonProperty
  public String getOriginalSource() {
    return (item != null) ? this.item.getOriginalSource() : null;
  }

  @JsonProperty
  public int getGlobalElementPosition() {
    return (item != null) ? item.getGlobalPosition() : _position;
  }

  @JsonIgnore
  public ASTItem getItem() {
    return item;
  }

  public void setGlobalElementPosition(int position) {
    if (item != null) item.setGlobalPosition(position);
    else _position = position;
  }

  @JsonProperty
  public String getErrorSource() {
    String source = getSource();
    if (source == null) source = "";

    if (this.getErrorStart() >= source.length()) {
      // this.errorStart = source.length();
      source = StringUtils.padRight(source, source.length() + this.getErrorLength());
    } else if (this.getErrorStart() < 0) {
      // this.errorStart = 0;
      source = StringUtils.padLeft(source, source.length() + this.getErrorLength());
    }

    return source;
  }

  public String underlineError(String error) {
    if (error != null) {
      String out = "";

      if (this.getErrorStart() > 0) {
        out += error.substring(0, this.getErrorStart());

        int end = this.getErrorStart() + this.getErrorLength();

        if (error.length() > end)
          out +=
              ConsoleColors.RED_UNDERLINED
                  + error.substring(this.getErrorStart(), end)
                  + ConsoleColors.RESET;
        else
          out +=
              ConsoleColors.RED_UNDERLINED
                  + error.substring(this.getErrorStart())
                  + ConsoleColors.RESET;

        if (end < error.length()) out += error.substring(end);
      } else {
        out +=
            ConsoleColors.RED_UNDERLINED
                + error.substring(0, this.getErrorLength())
                + ConsoleColors.RESET;
        out += error.substring(this.getErrorLength());
      }

      return out;
    }

    return error;
  }

  @JsonProperty
  public String getUnderlinedErrorSource() {
    String error = getErrorSource();
    return underlineError(error);
  }

  @Override
  public String toString(boolean verbose) {
    StringBuilder sb = new StringBuilder();

    String uMessage = this.getUserMessage();
    String errorSrc = getUnderlinedErrorSource();
    String errorArrow = StringUtils.padLeft("", errorSrc.length());

    int middle = (int) Math.floor(new Double(getErrorLength() / 2));
    errorArrow =
        errorArrow.substring(0, getErrorStart() + middle)
            + Constants.ARROW_UP
            + errorArrow.substring(getErrorStart() + middle);

    sb.append(
        ConsoleColors.GRAY
            + "RULE at Position "
            + (getGlobalElementPosition() + 1)
            + ":"
            + ConsoleColors.RESET
            + "\n\n");
    sb.append(errorSrc + "\n");
    sb.append(ConsoleColors.RED + errorArrow + ConsoleColors.RESET + "\n");
    sb.append(ConsoleColors.RED + uMessage + ConsoleColors.RESET + "\n\n");

    if (verbose) sb.append(super.toString(verbose));

    return sb.toString();
  }
}
