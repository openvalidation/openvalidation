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

package io.openvalidation.common.ast;

import java.util.Objects;

public class ASTActionError extends ASTActionBase {
  private String errorMessage;
  private Integer errorCode;

  public ASTActionError() {}

  public ASTActionError(String errorMsg) {
    this.setErrorMessage(errorMsg);
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage =
        (errorMessage != null) ? errorMessage.replace("\r\n", " ").replace("\n", " ").trim() : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ASTActionError)) return false;
    ASTActionError that = (ASTActionError) o;
    return Objects.equals(getErrorMessage(), that.getErrorMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getErrorMessage());
  }

  @Override
  public String print(int level) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n" + this.space(level) + "error message: " + this.getErrorMessage());

    if (errorCode != null) sb.append("\n" + this.space(level) + "error code: " + errorCode);

    return sb.toString();
  }

  public Integer getErrorCode() {
    return this.errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }
}
