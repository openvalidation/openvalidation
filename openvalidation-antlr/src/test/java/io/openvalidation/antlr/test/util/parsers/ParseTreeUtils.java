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

import io.openvalidation.antlr.generated.mainParser;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParseTreeUtils {
  public static void mask_NL_in_comments(ParseTree tree) {
    ((mainParser.MainContext) tree)
        .comment()
        .forEach(
            c -> {
              String s =
                  c.unknown().getText().trim().replaceAll("\r", "").replaceAll("\n", "§§§§NL§§§§");

              removeAllChildrenButOne(c.unknown());
              if (!c.unknown().STRING().isEmpty()) {
                ((CommonToken) c.unknown().STRING(0).getSymbol()).setText(s);
              } else if (!c.unknown().LPAREN().isEmpty()) {
                ((CommonToken) c.unknown().LPAREN(0).getSymbol()).setText(s);
              } else if (!c.unknown().RPAREN().isEmpty()) {
                ((CommonToken) c.unknown().RPAREN(0).getSymbol()).setText(s);
              } else {
                throw new IllegalStateException("");
              }
            });
  }

  private static void removeAllChildrenButOne(mainParser.UnknownContext ctx) {
    while (ctx.children.size() > 1) {
      ctx.removeLastChild();
    }
  }

  public static String unmask_NL_in_comments(String maskedTree) {
    return maskedTree
        .replaceAll("\\\\r", "")
        .replaceAll("\\\\n", " ")
        .replaceAll("§§§§NL§§§§", "\\\\n");
  }

  public static void trimContentInAction(ParseTree tree) {
    ((mainParser.MainContext) tree)
        .rule_definition()
        .forEach(
            rule -> {
              if (rule.action() != null) {
                String trimmedActionContent = rule.action().error().getText().trim();

                if (rule.action().error().children.size() > 1) {
                  rule.action().error().removeLastChild();
                }

                if (rule.action().error().unknown() != null) {
                  // ((CommonToken)
                  // rule.action().error().STRING().getSymbol()).setText(trimmedActionContent);
                } else if (rule.action().error().WITH_ERROR() != null) {
                  ((CommonToken) rule.action().error().WITH_ERROR().getSymbol())
                      .setText(trimmedActionContent);
                }
              }
            });
  }

  //    public static void wrapOperators(ParseTree tree)
  //    {
  //        ((mainParser.MainContext) tree).children.forEach(childTree ->
  //            {
  //                if (!(childTree instanceof ))
  //            }
  //        );
  //    }
}
