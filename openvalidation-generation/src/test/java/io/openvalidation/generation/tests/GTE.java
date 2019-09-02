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

package io.openvalidation.generation.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.utils.ResourceUtils;
import io.openvalidation.generation.CodeGenerator;

public class GTE {

  public static final String PARAM_TEST_NAME = "GENERATE {0}  with: {1} ==> expect: {2}";

  public static void executeAssertContains(String expected, String language, IThrowableCallback fn)
      throws Exception {
    execute(expected, language, fn, true);
  }

  public static void execute(String expected, String language, IThrowableCallback fn)
      throws Exception {
    execute(expected, language, fn, false);
  }

  public static void execute(
      String expected, String language, IThrowableCallback fn, boolean assertContains)
      throws Exception {

    ASTItem model = fn.call(null);

    String partial = model.getType();
    Boolean isInline = false;

    if (!ResourceUtils.exists("/" + language + "/" + partial + ".hbs")) {
      isInline = true;
      partial = "{{tmpl}}";
    }

    if (model.getType().equals("astmodel") && partial.equals("{{tmpl}}")) {
      isInline = false;
      partial = "main";
    }

    String result = CodeGenerator.generate(language, partial, model, isInline);

    assertThat(result, notNullValue());

    String res = result.trim().replaceAll("\r\n", "\n");

    if (assertContains) assertThat(res, containsString(expected));
    else assertThat(res, is(expected));
  }
}
