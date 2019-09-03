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

package io.openvalidation.core;

// import com.sun.xml.internal.bind.v2.TODO;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.utils.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Aliases {

  public static List<String> availableCultures; // = getAvailableCultures();

  static {

    //        return ResourceUtils.getAvailableCultures();
    availableCultures = ResourceUtils.getAvailableCultures();
  }

  private static Map<String, String> _customAliases;

  public static String normalize(String alias) {
    return normalize(Locale.getDefault().toLanguageTag(), alias);
  }

  public static String normalize(String locale, String alias) {
    String normalizedValue = null;

    Map<String, String> available = getAvailableAliases(locale);

    if (available.containsKey(alias)) {
      normalizedValue = available.get(alias);
    }

    // fallback in customAliases
    if (normalizedValue == null && _customAliases != null && _customAliases.containsKey(alias)) {
      normalizedValue = _customAliases.get(alias);
    }

    // check default values
    return (normalizedValue != null) ? normalizedValue : "[NOTFOUND_" + locale + "_" + alias + "]";
  }

  public static Map<String, String> getAvailableAliases() {
    return getAvailableAliases(Locale.getDefault().toLanguageTag());
  }

  public static Map<String, String> getAvailableAliases(String locale) {
    Map<String, String> available = new HashMap<>();

    ResourceBundle bundle =
        ResourceBundle.getBundle(
            Aliases.class.getSimpleName().toLowerCase(), new Locale(locale), new UTF8Control());
    // does not work
    // ResourceBundle bundle =
    // UTF8ResourceBundle.getBundle(Aliases.class.getSimpleName().toLowerCase(), new
    // Locale(locale));

    for (String key : Collections.list(bundle.getKeys())) {
      String[] values = bundle.getString(key).split(",");

      for (String val : values) {
        String v = val.trim();
        if (!v.isEmpty()) available.put(val.trim(), key);
      }
    }

    if (_customAliases != null) {
      for (String alias : _customAliases.keySet()) {
        if (available.containsKey(alias)) available.replace(alias, _customAliases.get(alias));
        else available.put(alias, _customAliases.get(alias));
      }
    }

    return available;
  }

  public static boolean hasAlias(String alias, Locale locale) {
    return hasAlias(alias, locale.toLanguageTag());
  }

  public static boolean hasAlias(String alias, String locale) {
    return getAvailableAliases(locale).keySet().stream()
        .anyMatch(k -> k.toLowerCase().equals(alias.toLowerCase()));
  }

  public static String getSpecificAliasByToken(String token) {
    Map<String, String> allAliases = Aliases.getAvailableAliases();

    for (Map.Entry<String, String> entry : allAliases.entrySet()) {
      if (entry.getValue().equals(token)) {
        return entry.getKey();
      }
    }

    return null;
  }

  public static String resolve(String plainText, Locale locale) {

    String ruleContent =
        "MAGICSTUFF:\n" + plainText.trim().replaceAll("\r\n", "\n") + "\n:MAGICSTUFF";

    Map<String, String> aliases = Aliases.getAvailableAliases(locale.toLanguageTag());

    // order keys descending to solve the problem with AS and XXX AS Keywords.
    List<String> keysSorted =
        aliases.keySet().stream()
            .sorted(Comparator.comparing(String::length))
            .collect(Collectors.toList());

    Collections.reverse(keysSorted);

    // Collections.sort(keysSorted, Collections.reverseOrder());

    for (String alias : keysSorted) {

      List<String> originalKeywords = StringUtils.findMatches(ruleContent, alias);
      if (originalKeywords != null && originalKeywords.size() > 0) {
        for (String ok : originalKeywords) {

          String replacement = aliases.get(alias);
          if (!endsWithKeywordSymbol(replacement)) {
            replacement += Constants.KEYWORD_SYMBOL;
          }

          replacement += NameMasking.maskName(ok);

          ruleContent = StringUtils.replaceWord(ruleContent, ok, replacement);
          // ruleContent.replaceAll(StringUtils.encodeRegex(ok), replacement);
        }
      }

      //            String regexAlias = alias;
      //            if (alias.equals("*")) regexAlias = "\\*";
      //            else if (alias.equals("+")) regexAlias = "\\+";
      //            else if (alias.equals("^")) regexAlias = "\\^";
      //            else if (alias.equals("(")) regexAlias = "\\(";
      //            else if (alias.equals(")")) regexAlias = "\\)";
      //
      //            String replacement = aliases.get(alias);
      //            if(!endsWithKeywordSymbol(replacement))
      //            {
      //                replacement += Constants.KEYWORD_SYMBOL;
      //            }

      // StringUtils.findOriginal(plainText, regexAlias);
      // [\+]\s

      // replacement += NameMasking.maskName(regexAlias);

      // ruleContent = StringUtils.

      // StringUtils.matchWord(ruleContent, regexAlias);

      //            ruleContent = ruleContent.replaceAll("(?i)" + Pattern.quote("\r\n" + regexAlias
      // + "\r\n"), "\r\n" + replacement + "\r\n")
      //                    .replaceAll("(?i)" + Pattern.quote(" " + regexAlias + "\r\n"), " " +
      // replacement + "\r\n")
      //                    .replaceAll("(?i)" + Pattern.quote("\n" + regexAlias + "\r\n"), "\n" +
      // replacement + "\r\n")
      //
      //
      //                    .replaceAll("(?i)" + Pattern.quote("\r\n" + regexAlias + "\n"), "\r\n" +
      // replacement + "\n")
      //                    .replaceAll("(?i)" + Pattern.quote("\n" + regexAlias + "\n"), "\n" +
      // replacement + "\n")
      //                    .replaceAll("(?i)" + Pattern.quote(" " + regexAlias + "\n"), " " +
      // replacement + "\n")
      //
      //
      //                    .replaceAll("(?i)" + Pattern.quote("\r\n" + regexAlias + " "), "\r\n" +
      // replacement + " ")
      //                    .replaceAll("(?i)" + Pattern.quote("\n" + regexAlias + " "), "\n" +
      // replacement + " ")
      //                    .replaceAll("(?i)" + Pattern.quote(" " + regexAlias + " "), " " +
      // replacement + " ");

    }

    ruleContent =
        ruleContent
            .replaceAll("MAGICSTUFF:\n", "")
            .replaceAll("\n:MAGICSTUFF", "")
            .replaceAll("MAGICSTUFF:", "")
            .replaceAll(":MAGICSTUFF", "");

    return ruleContent.trim();
  }

  private static boolean endsWithKeywordSymbol(String input) {
    String endingChar = String.valueOf(input.charAt(input.length() - 1));
    return endingChar.equalsIgnoreCase(Constants.KEYWORD_SYMBOL);
  }

  public static String resolve(String plainText, String includedKeys, Locale locale) {
    String ruleContent = plainText;

    Map<String, String> aliases = Aliases.getAvailableAliases(locale.toLanguageTag());

    for (String alias : aliases.keySet()) {
      if (aliases.get(alias).equalsIgnoreCase(includedKeys)) {
        ruleContent = ruleContent.replaceAll("(?i)" + Pattern.quote(alias), aliases.get(alias));
      }
    }

    return ruleContent;
  }

  public static void appendCustomAliasesFromFile(String filePath) throws FileNotFoundException {
    if (filePath == null) return;

    FileSystemUtils.fileShouldExists(
        filePath, "A custom Alias File: " + filePath + " could not be found.");

    try {
      String plain = FileSystemUtils.readFile(filePath);
      Map<String, String> aliases = new HashMap<>();

      if (plain != null) {
        for (String line : plain.split(System.lineSeparator())) {

          String[] keyValue = line.split("=");

          if (keyValue != null && keyValue.length == 2) {
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            if (value.contains(",")) {
              for (String v : value.split(",")) {
                aliases.put(v.trim(), key);
              }
            } else {
              aliases.put(value, key);
            }
          }
        }
      }

      if (aliases.size() > 0) {
        appendCustomAliases(aliases);
      }

    } catch (Exception e) {

    }
  }

  public static void appendCustomAliases(Map<String, String> aliases) {
    if (aliases == null) return;

    if (_customAliases == null) _customAliases = new HashMap<>();

    for (String alias : aliases.keySet()) {
      if (_customAliases.containsKey(alias)) _customAliases.replace(alias, aliases.get(alias));
      else _customAliases.put(alias, aliases.get(alias));
    }
  }

  public static void appendCustomAliases(String key, String value) {
    Map<String, String> map = new HashMap<>();

    map.put(key, value);

    appendCustomAliases(map);
  }

  public static void validateAliases() throws Exception {
    List<String> out = new ArrayList<>();

    String[] locales = null; // ResourceUtils.getAvailableLocales("aliases");

    if (locales != null) {
      for (String locale : locales) {
        Map<String, String> available = null; // getAvailableAliases(locale);

        for (String key : available.keySet()) {
          for (String key2 : available.keySet()) {
            if (key != key2) {

              //      disable validation
              //
              //                            if (key2.contains(key)) {
              //                                String e = "ALIAS: '" + key + "' is a part of
              // another ALIAS: '" + key2 + "'";
              //
              //                                if (!out.stream().anyMatch(x -> x.equals(e))) {
              //                                    out.with(e);
              //                                }
              //                            }
            }
          }
        }
      }
    }

    if (out.size() > 0) throw new OpenValidationException(String.join(", ", out));
  }
}
