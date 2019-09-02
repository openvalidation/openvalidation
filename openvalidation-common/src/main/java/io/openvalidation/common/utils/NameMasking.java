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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameMasking {

  //    private static String hexEncodingMask = "_15042019_";
  private static String hexEncodingMask = "_";

  public static String maskName(String name) {
    final StringBuilder sb = new StringBuilder();
    final StringCharacterIterator iterator = new StringCharacterIterator(name);
    char character = iterator.current();
    String commonCharPattern = "[a-zA-Z0-9]";

    while (character != CharacterIterator.DONE) {
      if (!(String.valueOf(character).matches(commonCharPattern))) {
        sb.append(getMaskOf(character));
      } else {
        // the char is not a special one
        // add it to the stringBuilder as is
        sb.append(character);
      }
      character = iterator.next();
    }
    return sb.toString();
  }

  // encodes special characters with utf-16
  public static String getMaskOf(Character character) {
    // todo lazevedo 3.4.19 some characters are stored int UTF-16 which uses 2 bytes. change to use
    // not only byte[0]
    String hex = Integer.toHexString((int) character);
    hex = hexEncodingMask + hex + hexEncodingMask;

    return (hex);
  }

  public static String unmask(String maskedWord) {
    String input = maskedWord;
    String maskPattern = hexEncodingMask + "[a-zA-Z0-9]+" + hexEncodingMask;
    Pattern pattern = Pattern.compile(maskPattern);
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      if (input.contains(matcher.group())) {

        int hexContentStartIndex = input.indexOf(hexEncodingMask) + hexEncodingMask.length();
        int hexContentEndIndex =
            input.substring(hexContentStartIndex + 1).indexOf(hexEncodingMask)
                + hexContentStartIndex
                + 1;

        String hexCode = input.substring(hexContentStartIndex, hexContentEndIndex);
        // convert hex to decimal
        int decimal = Integer.parseInt(hexCode, 16);
        // convert the decimal to character
        String decodedHex = String.valueOf(((char) decimal));

        input = input.replace(matcher.group(), decodedHex);
      }
    }

    return input;
  }

  public static String maskVariableName(String varname) {
    if (!StringUtils.isNullOrEmpty(varname))
      return NameMasking.maskName(StringUtils.reverseKeywords(varname)) + "_var";

    return varname;
  }

  public static String unmaskVariableName(String varname) {
    if (!StringUtils.isNullOrEmpty(varname))
      return NameMasking.unmask(StringUtils.reverseKeywords(varname).replace("_var", ""));

    return varname;
  }
}
