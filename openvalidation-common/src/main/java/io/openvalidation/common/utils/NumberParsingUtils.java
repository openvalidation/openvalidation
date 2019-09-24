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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class NumberParsingUtils {

  public static boolean isParsableFloat(String input) {
    try {
      Float.parseFloat(input);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static Double extractDouble(String value) {

    Pattern regex = Pattern.compile("-?(\\b[0-9]+(,[0-9]{3})*(\\.[0-9]+)?\\b|\\.\\b[0-9]+)\\b");
    Matcher matcher = regex.matcher(value);

    if (matcher.find()) {

      String str = matcher.group(0);
      if (!io.openvalidation.common.utils.StringUtils.isNullOrEmpty(str)) return new Double(str);
    }

    return null;

    //        if (value != null && !value.isEmpty()) {
    //            for (String val : value.split(" ")) {
    //                if (val != null && !val.trim().isEmpty() && StringUtils.isNumeric(val.trim()))
    // {
    //                    return Double.parseDouble(val.trim());
    //                }
    //            }
    //        }
    //
    //        return null;
  }

  // new stuff

  public static boolean isDouble(String input) {
    try {
      Double.parseDouble(input);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static boolean isNumber(String value) {
    if (value != null) {
      String v = value.trim();
      if (v.length() > 0 && StringUtils.isNumeric(v) || isDouble(v)) {
        if (v.length() > 1 && v.startsWith("0")) return false;

        return true;
      }
    }

    return false;
  }

  public static boolean containsNumber(String value) {
    if (value != null && !value.isEmpty()) {
      for (String val : value.split(" ")) {
        if (val != null) {
          String v = val.trim();
          if (!v.isEmpty() && (StringUtils.isNumeric(v) || isDouble(v))) {
            if (v.length() > 1 && v.startsWith("0")) return false;

            return true;
          }
        }
      }
    }

    return false;
  }

  public static Double extractNumber(String value) {
    if (value != null && !value.isEmpty()) {
      for (String val : value.split(" ")) {
        if (val != null) {
          String v = val.trim();
          if (!v.isEmpty() && (StringUtils.isNumeric(v) || isDouble(v))) {
            return Double.parseDouble(v);
          }
        }
      }
    }

    return null;
  }
}
