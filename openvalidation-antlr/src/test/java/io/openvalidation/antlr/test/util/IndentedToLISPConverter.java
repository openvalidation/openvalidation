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

package io.openvalidation.antlr.test.util;

public class IndentedToLISPConverter {
  public static String formatToLISPLikeTreeString(String input) {
    return insertSpacesForIndents(formatTreeStringToString(input));
  }

  private static String formatTreeStringToString(String treeString) {
    String[] treeParts = treeString.split("\n");
    int[] indentations = new int[treeParts.length];

    for (int i = 0; i < treeParts.length; i++) {
      indentations[i] = getIndentationLevel(treeParts[i]);
    }
    for (int i = 0; i < treeParts.length; i++) {
      // currently only trims left side (look )
      treeParts[i] = trimLeft(treeParts[i]);
    }

    for (int i = 0; i < treeParts.length; i++) { // über alle lines
      treeParts[i] = '(' + treeParts[i];
      if (i > 0) {
        treeParts[i] = ' ' + treeParts[i];
      }
      boolean closed = false;
      for (int j = i + 1;
          j < treeParts.length;
          j++) { // hat eine folgende line das gleiche level von indentation?
        if (indentations[i] >= indentations[j] && !closed) {
          treeParts[j - 1] = treeParts[j - 1] + ')';
          closed = true;
        }
      }
      if (!closed) {
        treeParts[treeParts.length - 1] += ')';
      }
    }

    StringBuilder sb = new StringBuilder();
    for (String element : treeParts) {
      sb.append(element);
    }

    return sb.toString();
  }

  private static int getIndentationLevel(String indentation) {
    int counter = 0;
    int charsSinceLastTab = 0;
    boolean isContent = false;
    for (int i = 0; i < indentation.length(); i++) {
      switch (indentation.charAt(i)) {
        case '\t':
          counter += (4 - (charsSinceLastTab % 4));
          charsSinceLastTab = 0;
          break;
        case ' ':
          counter++;
          charsSinceLastTab++;
          break;
        default:
          isContent = true;
          break;
      }
      if (isContent) {
        break;
      }
    }
    return counter;
  }

  private static String trimLeft(String input) {
    // trim all initial whitespace (left side)
    return input.replaceFirst("^\\s+", "");
  }

  private static String insertSpacesForIndents(String input) {
    String[] stringParts = input.split("indent ");

    // i=1 because treeParts[0] never begins with '/i'
    for (int i = 1; i < stringParts.length; i++) {
      // replace all initial "/i" by 4 spaces each
      stringParts[i] = insertSpaces(stringParts[i]);
    }

    // dummy
    return String.join("indent ", stringParts);
  }

  private static String insertSpaces(String input) {
    // indent indicator is "/i"
    int nrIndicators = countInitialIndicators(input);
    String spaces = getSpacesString(nrIndicators);

    return input.replaceFirst("(/i)+", spaces);
  }

  // counts the nr of occurrences of '/i' at the beginning of the String
  private static int countInitialIndicators(String input) {
    int c = 0;

    boolean slashFound = false;

    for (int i = 0; i < input.length(); i++) {
      switch (input.charAt(i)) {
        case '/':
          if (!slashFound) {
            slashFound = true;
          } else {
            // end for-loop
            i = input.length();
          }
          break;
        case 'i':
          if (slashFound) {
            c++;
            slashFound = false;
          } else {
            // end for-loop
            i = input.length();
          }
          break;
        default:
          i = input.length();
      }
    }
    return c;
  }

  private static String getSpacesString(int nrIndicators) {
    StringBuilder spaces = new StringBuilder();

    for (int i = 0; i < nrIndicators; i++) {
      // one indent always equals 4 spaces
      spaces.append("    ");
    }

    return spaces.toString();
  }
}
