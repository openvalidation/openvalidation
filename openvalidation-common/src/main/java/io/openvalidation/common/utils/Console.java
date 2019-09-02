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

package io.openvalidation.common.utils;

public class Console {
  private static boolean hasANSI = false;

  static {

    //        if (System.console() != null && System.getenv().get("TERM") != null) {
    //            hasANSI = true;
    //        } else if (System.console() != null &&
    //                   System.getenv().get("PROMPT") != null &&
    //                   System.getenv().get("PROMPT").contains("$E")) {
    //            hasANSI = true;
    //        }
  }

  public static void error(String error) throws Exception {
    print(ConsoleColors.RED_BOLD + error + ConsoleColors.RESET);
  }

  public static void success(String error) throws Exception {
    print(ConsoleColors.GREEN_BOLD + error + ConsoleColors.RESET);
  }

  public static String getTitleStart(String text) {
    return "\n\n"
        + ConsoleColors.GRAY
        + text
        + " "
        + Constants.ARROW_DOWN
        + " \n\n"
        + ConsoleColors.RESET;
  }

  public static void titleStart(String text) {
    print(getTitleStart(text));
  }

  public static void titleEnd(String text) {
    print(
        "\n\n"
            + ConsoleColors.GRAY
            + text
            + " "
            + Constants.ARROW_UP
            + " \n\n"
            + ConsoleColors.RESET);
  }

  public static void print(String msg) {

    System.out.print((msg != null) ? msg.replace("\r\n", "\n").replace("\r", "\\r") : "");
  }

  public static void printl(String msg) {
    System.out.print(msg + "\n");
  }

  public static void printlGray(String msg) {
    System.out.print(grayString(msg) + "\n");
  }

  public static String errorString(String value) {
    return ConsoleColors.RED + value + ConsoleColors.RESET;
  }

  public static String successString(String value) {
    return ConsoleColors.GREEN + value + ConsoleColors.RESET;
  }

  public static String grayString(String value) {
    return ConsoleColors.GRAY + value + ConsoleColors.RESET;
  }

  public static void printError(Exception exp) {
    if (exp != null) {
      printl(exp.toString() + "\n\n");
      printl(exp.getMessage());
      printl(StringUtils.join(exp.getStackTrace(), "\n", e -> e.toString()));
    }
  }
}
