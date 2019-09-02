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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenValidationException extends Exception {
  private String _userMessage;

  @JsonProperty
  public String getUserMessage() {
    return this._userMessage;
  }

  @Override
  @JsonIgnore
  public StackTraceElement[] getStackTrace() {
    return super.getStackTrace();
  }

  @Override
  @JsonProperty
  public String getMessage() {
    return super.getMessage();
  }

  @Override
  @JsonProperty
  public String getLocalizedMessage() {
    return super.getLocalizedMessage();
  }

  public void setUserMessage(String userMessage) {

    this._userMessage = userMessage;
  }

  public OpenValidationException(String message) {
    super(message);
    setUserMessage(message);
  }

  public OpenValidationException(String message, Exception e) {
    super(e);
    setUserMessage(message);
  }

  public OpenValidationException(String technicalMessage, String userMessage) {
    super(technicalMessage);

    setUserMessage(userMessage);
  }

  public OpenValidationException(String technicalMessage, String userMessage, Exception e) {
    super(technicalMessage, e);

    setUserMessage(userMessage);
  }

  @Override
  public String toString() {
    return toString(true);
  }

  public String toString(boolean verbose) {
    StringBuilder sb = new StringBuilder();

    String uMessage = this.getUserMessage();
    String message = this.getMessage();

    sb.append(uMessage + "\n");
    if (!uMessage.equals(message)) sb.append(message);
    if (this.getCause() != null
        && this.getCause().getMessage() != null
        && !this.getCause().getMessage().equals(message))
      sb.append(this.getCause().getMessage() + "\n");
    if (this.getCause() != null
        && this.getCause().getCause() != null
        && this.getCause().getCause().getMessage() != null
        && !this.getCause().getCause().getMessage().equals(message))
      sb.append(this.getCause().getCause().getMessage() + "\n");

    sb.append("\n");

    if (verbose) {
      sb.append("\n");
      List<StackTraceElement> se = Arrays.asList(this.getStackTrace());

      Throwable innerException = this.getCause();
      List<StackTraceElement> subelms =
          innerException != null
              ? Arrays.asList(innerException.getStackTrace())
              : new ArrayList<>();
      List<StackTraceElement> ste =
          Stream.concat(subelms.stream(), se.stream()).collect(Collectors.toList());

      if (innerException != null
          && innerException.getCause() != null
          && innerException.getCause().getStackTrace() != null) {
        ste.addAll(0, Arrays.asList(innerException.getCause().getStackTrace()));
      }

      List<String> stack = new ArrayList<>();

      for (StackTraceElement st : ste) {
        if (st.getClassName().contains("io.openvalidation")
            && st.toString().trim().length() > 0
            && !stack.contains(st.toString())) {
          stack.add(st.toString());
          sb.append("    " + st.toString() + "\n");
        }
      }
    }

    return sb.toString();
  }
}
