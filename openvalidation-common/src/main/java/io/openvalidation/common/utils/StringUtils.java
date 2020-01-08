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

import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

  public static String padRight(Object s, int n) {
    if (n > 0) return String.format("%1$-" + n + "s", s.toString());

    return s.toString();
  }

  public static String padLeft(Object s, int n) {
    if (n > 0) return String.format("%1$" + n + "s", s.toString());

    return s.toString();
  }

  public static InputStream toInputStream(String text) {
    return new ByteArrayInputStream(text.getBytes());
  }

  public static String stripWords(String value, String[] strings) {
    String newValue = value;

    if (value != null && strings != null) {
      for (String s : strings) {
        newValue = newValue.replaceAll("(?i)^" + s + " ", "").replaceAll("(?i) " + s + "$", "");
      }
    }

    return newValue;
  }

  public static String stripSpecialWords(String value) {
    if (!isNullOrEmpty(value)) {
      String val = trimSpecialChars(value);
      val = StringUtils.stripWords(val, Constants.TRIM_WORDS);
      return trimSpecialChars(val);
    }

    return value;
  }

  public static String trimSpecialChars(String value) {
    Pattern pattern =
        Pattern.compile(
            "[" + Constants.TRIM_REGEX + "]*(-?(\\b.+\\b))[" + Constants.TRIM_REGEX + "]*");
    Matcher matcher = pattern.matcher(value);
    if (matcher.find()) {
      return matcher.group(1);
    }

    return value;
  }

  public static String join(List<String> lst, String delimiter) {
    if (lst != null) return String.join(delimiter, lst);

    return null;
  }

  public static <T, R> String join(
      T[] arr, String delimiter, Function<? super T, ? extends String> mapper) {
    return StringUtils.join(LINQ.select(arr, mapper), delimiter);
  }

  public static <T, R> String join(
      List<T> lst, String delimiter, Function<? super T, ? extends String> mapper) {
    return StringUtils.join(LINQ.select(lst, mapper), delimiter);
  }

  public static String join(String[] lst, String delimiter) {
    if (lst != null) return String.join(delimiter, lst);

    return null;
  }

  public static boolean match(String value, String pattern) {
    Pattern p = Pattern.compile("\\b[A-Z]+\\b");
    Matcher m = p.matcher(value);

    return m.find();
  }

  public static boolean isNullOrEmpty(String source) {
    return (source == null || source.trim().length() < 1);
  }

  public static String reverseKeywords(String preprocessedRuleContent) {
    String regex =
        "\\b"
            + Constants.KEYWORD_SYMBOL
            + "([a-z_A-Z0-9]+)("
            + Constants.KEYWORD_SYMBOL
            + ")*([a-z_A-Z0-9]+)*"
            + Constants.KEYWORD_SYMBOL
            + "*([a-z_A-Z0-9]+)*\\b";

    String out = preprocessedRuleContent;

    if (!isNullOrEmpty(preprocessedRuleContent)) {
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(preprocessedRuleContent);

      while (matcher.find()) {

        String replacement = matcher.group();
        replacement = replacement.substring(replacement.lastIndexOf(Constants.KEYWORD_SYMBOL) + 1);

        replacement = NameMasking.unmask(replacement);

        out = out.replaceFirst(regex, replacement);
      }
    }

    out = (!StringUtils.isNullOrEmpty(out)) ? out.replace("_20_", " ") : out;
    return out;
  }

  public static String mirrorString(String input) {
    StringBuilder sb = new StringBuilder();
    for (char c : input.toCharArray()) {
      sb.insert(0, c);
    }
    return sb.toString();
  }

  public static boolean matchWord(String input, String searchWord) {
    return matchWord(input, searchWord, true);
  }

  public static boolean matchWord(String input, String searchWord, boolean ignoreCase) {

    List<String> matches = findMatches(input, searchWord, ignoreCase);

    return matches.size() > 0;
  }

  public static List<String> findMatches(String input, String searchWord) {
    return findMatches(input, searchWord, true);
  }

  public static List<String> findMatches(String input, String searchWord, boolean ignoreCase) {
    List<String> matches = new ArrayList<>();

    if (!isNullOrEmpty(input) && !isNullOrEmpty(searchWord)) {

      String sw = encodeRegex(searchWord);

      //            if (sw.equals("*")) sw = "\\*";
      //            else if (sw.equals("+")) sw = "\\+";
      //            else if (sw.equals("^")) sw = "\\^";
      //            else if (sw.equals("(")) sw = "\\(";
      //            else if (sw.equals(")")) sw = "\\)";

      String regex = "\\s" + sw + "\\s";
      Pattern pattern =
          (ignoreCase)
              ? Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
              : Pattern.compile(regex);
      Matcher matcher = pattern.matcher(" " + input.trim() + " ");

      while (matcher.find()) {
        matches.add(matcher.group().trim());
      }
    }

    return matches;
  }

  public static String replaceWord(String input, String searchWord, String replaceWord) {
    String output = input;

    if (!isNullOrEmpty(input) && !isNullOrEmpty(searchWord)) {
      String regex = "\\s" + encodeRegex(searchWord) + "\\s";

      Pattern pattern =
          Pattern.compile("\\s(" + encodeRegex(searchWord) + ")\\s", Pattern.UNICODE_CASE);
      Matcher matcher = pattern.matcher(input);

      while (matcher.find()) {
        String res = matcher.group(1);
        int start = matcher.start(1);
        int end = start + res.length();

        if (start > -1) {
          output = output.substring(0, start) + replaceWord + output.substring(end);
          return output;
        }
      }

      // return input.replaceFirst(regex, " " + replaceWord + " ");
    }

    return output;
  }

  public static String matchAndReplaceWords(String input, String searchWord, String replaceWord) {
    return matchAndReplaceWords(input, searchWord, replaceWord, true);
  }

  public static String matchAndReplaceWords(
      String input, String searchWord, String replaceWord, boolean ignoreCase) {
    List<String> matches = findMatches(input, searchWord, ignoreCase);
    AtomicReference<String> outString = new AtomicReference<>();
    outString.set(input);

    if (matches.size() > 0) {
      matches.forEach(
          m -> {
            outString.set(outString.get().replaceAll(encodeRegex(m), replaceWord));
          });
    }

    return outString.get();
  }

  public static String encodeRegex(String plain) {
    String out = plain;

    if (out.equals("*")) out = "\\*";
    else if (out.equals("+")) out = "\\+";
    else if (out.equals("^")) out = "\\^";
    else if (out.equals("(")) out = "\\(";
    else if (out.equals(")")) out = "\\)";

    if (out.trim().contains(" ")) out = out.trim().replaceAll(" ", "[\n ]+");

    return out;
  }

  public static String[] splitAndRemoveEmptyAsArray(String content, String separatorRegex) {
    return splitAndRemoveEmpty(content, separatorRegex).toArray(new String[0]);
  }

  public static List<String> splitAndRemoveEmpty(String content, String separatorRegex) {
    String[] values = content.split(separatorRegex);
    return LINQ.where(values, r -> !StringUtils.isNullOrEmpty(r));
  }

  public static int indexOfIgnoreCase(String string, String sub) {
    string = string.toLowerCase();
    sub = sub.toLowerCase();

    return string.indexOf(sub);
  }

  public static boolean isBoolean(String s) {
    if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
      return true;
    }
    return false;
  }

  public static String getUserFriendlyClassName(Object o) {
    String result = o.getClass().getSimpleName();

    if (o instanceof ASTVariable || o instanceof ASTOperandVariable) result = "variable";
    if (o instanceof ASTOperandProperty) result = "property";
    if (o instanceof ASTOperandFunction) result = "function";
    if (o instanceof ASTOperandStaticString) result = "string";
    if (o instanceof ASTOperandStaticNumber) result = "number";
    if (o instanceof ASTOperandLambdaCondition || o instanceof ASTCondition) result = "condition";
    if (o instanceof ASTConditionGroup) result = "condition group";

    return result;
  }
}
