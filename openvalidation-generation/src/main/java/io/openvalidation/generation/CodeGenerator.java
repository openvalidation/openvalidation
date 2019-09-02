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

package io.openvalidation.generation;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.utils.ResourceUtils;
import io.openvalidation.common.validation.Validator;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CodeGenerator {

  private static Map<String, String> inheritance;

  static {
    inheritance = new HashMap<>();
    inheritance.put("node", "javascript");
  }

  public static String generate(
      String language, String partial, ASTItem model, boolean isInlinePartial) throws Exception {
    Validator.shouldNotBeEmpty(partial, "Name of partial Template");
    Validator.shouldNotBeEmpty(language, "output Programming Language");
    Validator.shouldNotBeEmpty(model, "AST model");

    if (!partial.equals("{{tmpl}}")) {
      if (partial.equals("main") && model.getType().equals("astmodel")
          || partial.equals("framework") && model.getType().equals("astmodel")
          || partial.equals("validatorfactory") && model.getType().equals("astmodel")) {

      } else {
        Validator.shouldEquals(
            model.getType(),
            partial,
            "model type",
            "model type: '" + model.getType() + "' - partial template name'" + partial + "'");
      }

      Validator.shouldBeFals(
          isInlinePartial, "cause partial content is not {{tmpl}}, IS INLINE PARTIAL");
    } else {
      Validator.shouldBeTrue(
          isInlinePartial, "cause partial content is {{tmpl}}, IS INLINE PARTIAL");
    }

    TemplateLoader loader = new ClassPathTemplateLoader();
    // loader.setPrefix("/" + language);

    Handlebars handlebars = new Handlebars(loader);
    handlebars.registerHelpers(ConditionalHelpers.class);
    handlebars.infiniteLoops(true);

    handlebars.registerHelper(
        "equals",
        new Helper<Object>() {
          public CharSequence apply(Object a, Options options) throws IOException {
            Object b = options.param(0, null);

            if (a == null || b == null) return options.inverse();

            return (a.toString().toLowerCase().equals(b.toString().toLowerCase()))
                ? options.fn()
                : options.inverse();
          }
        });

    handlebars.registerHelper(
        "not_equals",
        new Helper<Object>() {
          public CharSequence apply(Object a, Options options) throws IOException {
            Object b = options.param(0, null);

            if (a == null || b == null)
              return (a == null && b == null) ? options.inverse() : options.fn();

            return (a.toString().toLowerCase().equals(b.toString().toLowerCase()))
                ? options.inverse()
                : options.fn();
          }
        });

    handlebars.registerHelper(
        "tmpl",
        new Helper<Object>() {
          public CharSequence apply(Object a, Options options) throws IOException {
            Object b = options.param(0, null);

            String tmplFullName = null;
            String tmplName = null;

            String errors = "";
            TemplateSource t = null;
            Template tmpl = null;

            try {
              if (a != null && a instanceof String) tmplName = a.toString();
              else if (options.context.model() instanceof ASTArithmeticalOperator)
                tmplName = ((ASTArithmeticalOperator) options.context.model()).getType();
              else if (options.context.model() instanceof ASTComparisonOperator)
                tmplName = ASTComparisonOperator.class.getSimpleName().toLowerCase();
              else if (options.context.model() instanceof ASTConditionConnector)
                tmplName = ASTConditionConnector.class.getSimpleName().toLowerCase();
              else tmplName = ((ASTItem) options.context.model()).getType();

              // inheritance
              if (ResourceUtils.exists("/" + language + "/" + tmplName + ".hbs"))
                tmplFullName = "/" + language + "/" + tmplName;
              else if (inheritance.containsKey(language)
                  && ResourceUtils.exists(
                      "/" + inheritance.get(language) + "/" + tmplName + ".hbs"))
                tmplFullName = "/" + inheritance.get(language) + "/" + tmplName;
              else tmplFullName = "/common/" + tmplName;

              //                    tmplFullName = ResourceUtils.exists("/" + language + "/" +
              // tmplName + ".hbs")?
              //                            tmplName : "../common/" + tmplName;

              t = options.handlebars.getLoader().sourceAt(tmplFullName);
              tmpl = options.handlebars.compile(t);

              return new Handlebars.SafeString(tmpl.apply(options.context));

            } catch (Exception e) {
              String stcktr =
                  String.join(
                      "\n",
                      Arrays.stream(e.getStackTrace())
                          .map(s -> s.toString())
                          .collect(Collectors.toList()));

              errors += "\n\n### ERRROR: \n\n" + e.toString() + stcktr + "\n\n:ERRROR ###\n\n";

              if (t != null) errors = "\n\n Template NAME: " + t.filename() + errors + "\n\n";

              if (tmpl != null) errors = "\n\n Template Content: " + tmpl.text() + errors + "\n\n";

              int x = 0;
            }

            return "TEMPLATE ["
                + tmplName
                + " - "
                + tmplFullName
                + " language: "
                + language
                + "] NOT FOUND!]\n\n"
                + errors;
          }
        });

    Template template =
        (isInlinePartial)
            ? handlebars.compileInline(partial)
            : handlebars.compile(
                "/" + language + "/" + partial); // compileInline("start [{{name}}] end");

    return template.apply(model);
  }
}
