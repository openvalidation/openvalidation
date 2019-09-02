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

package io.openvalidation.antlr.test.util.parsers;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class IncompleteInputErrorListener extends BaseErrorListener {
  private List<String> errorList;
  private int num;

  public IncompleteInputErrorListener() {
    errorList = new ArrayList<>();
    num = 1;
  }

  public List<String> getErrorList() {
    return errorList;
  }

  @Override
  public void syntaxError(
      Recognizer<?, ?> recognizer,
      Object offendingSymbol,
      int line,
      int charPositionInLine,
      String msg,
      RecognitionException e) {
    String error =
        "Error " + (num++) + ": " + "in line: " + line + "; at position: " + charPositionInLine;

    if (e != null && e.getOffendingToken() != null) {
      error += "; offending symbol: " + e.getOffendingToken().getText();
    }

    error += "; details: " + offendingSymbol.toString();
    error += ";\n\tmessage: " + msg;
    errorList.add(error);
  }
}
