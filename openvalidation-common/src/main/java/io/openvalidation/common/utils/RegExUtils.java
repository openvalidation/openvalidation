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

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtils {
  public static String PARAGRAPH_REGEX = "([ ]*\n[ ]*){2,}";
  private static Pattern arithmeticalTimesCollision =
      Pattern.compile(
          "(ʬarithmoperatorʬmultiplyʬ)times(\\s| )+ʬarithmoperatorʬ", Pattern.CASE_INSENSITIVE);

  public static boolean hasArithmeticalTimesCollision(String value) {
    return hasMatch(arithmeticalTimesCollision, value);
  }

  public static String fixArithmeticalTimesCollision(String value) {
    return replace(arithmeticalTimesCollision, value, "");
  }

  public static boolean hasMatch(Pattern pattern, String value) {
    if (!StringUtils.isNullOrEmpty(value)) return arithmeticalTimesCollision.matcher(value).find();

    return false;
  }

  public static String replace(Pattern pattern, String input, String replaceWord) {
    String output = input;

    if (!StringUtils.isNullOrEmpty(input)) {
      Matcher matcher = pattern.matcher(input);

      String replacement =
          StringUtils.isNullOrEmpty(replaceWord) ? "" : StringUtils.encodeRegex(replaceWord);

      while (matcher.find()) {
        String res = matcher.group(1);
        int start = matcher.start(1);
        int end = start + res.length();

        if (start > -1) {
          output = output.substring(0, start) + replacement + output.substring(end);
          return output;
        }
      }
    }

    return output;
  }

  public static void each(String regex, String input, Consumer<? super Matcher> clbk) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      clbk.accept(matcher);
    }
  }

  public static String firstGroup(String regex, String input) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);

    if (matcher.find()) return matcher.group(1);

    return null;
  }
}
