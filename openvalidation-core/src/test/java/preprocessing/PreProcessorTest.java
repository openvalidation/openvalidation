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

package preprocessing;

import static io.openvalidation.common.utils.Constants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.model.PreProcessorContext;
import io.openvalidation.common.utils.FileSystemUtils;
import io.openvalidation.common.utils.ResourceUtils;
import io.openvalidation.core.preprocessing.DefaultPreProcessor;
import java.util.Locale;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PreProcessorTest {

  @Test
  @Disabled
  void shouldResolveSimpleInclude_WithRelativePath() throws Exception {
    String includeName = "simple_include.ov";
    String resPath = ResourceUtils.getPathWithoutResourceName(includeName);

    String rule = "IMPORTIERE " + includeName;

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", resPath));

    assertThat(result, containsString("CONTENT OF SIMPLE"));
  }

  @Test
  @Disabled
  void shouldResolveSimpleInclude_WithAbsolutePath() throws Exception {
    String include = ResourceUtils.getPathWithoutResourceName("simple_include.ov");
    String rule = "IMPORTIERE " + include;

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", null));

    assertThat(result, containsString("CONTENT OF SIMPLE"));
  }

  @Test
  @Disabled
  void shouldResolveRecursiveInclude_WithAbsolutePath() throws Exception {
    String include = ResourceUtils.getPathWithoutResourceName("recursive_include.ov");
    String rule = "IMPORTIERE " + include;

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result =
        pre.process(rule, getPreprocessorContext("de", FileSystemUtils.getDirectory(include)));

    assertThat(result, containsString("CONTENT OF SIMPLE"));
  }

  @Test
  @Disabled
  void shouldResolveRecursiveInclude_AndAliases_WithAbsolutePath() throws Exception {
    String include = ResourceUtils.getPathWithoutResourceName("recursive_include.ov");
    String rule = "IMPORTIERE " + include;

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result =
        pre.process(rule, getPreprocessorContext("de", FileSystemUtils.getDirectory(include)));

    assertThat(result, containsString("CONTENT OF SIMPLE"));
    assertThat(result, containsString("_less_than_"));
  }

  @Test
  void should_resolve_unix_newline() throws Exception {

    String rule = "WENN der Name IST GLEICH Ilja\n" + "DANN was für ein Quatsch! ";

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", null));

    assertThat(result, containsString(IF_TOKEN + "WENN"));
    assertThat(result, containsString(THEN_TOKEN + "DANN"));
  }

  @Test
  void should_resolve_windows_newline() throws Exception {

    String rule = "WENN der Name IST GLEICH Ilja\r\n" + "DANN was für ein Quatsch! ";

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", null));

    assertThat(result, containsString(IF_TOKEN + "WENN"));
    assertThat(result, containsString(THEN_TOKEN + "DANN"));
  }

  @Test
  void should_resolve_operator_with_first_alias_in_list() throws Exception {

    String rule = "WENN der Name ist GLEICH Ilja\r\n" + "DANN was für ein Quatsch! ";

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", null));

    assertThat(result, containsString(EQUALS_TOKEN + KEYWORD_SYMBOL + "GLEICH"));
  }

  @Test
  void should_resolve_operator_with_second_alias_in_list() throws Exception {

    String rule = "WENN der Name IST Ilja\r\n" + "DANN was für ein Quatsch! ";

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", null));

    assertThat(result, containsString(EQUALS_TOKEN + KEYWORD_SYMBOL + "IST"));
  }

  @Test
  void should_resolve_combinators() throws Exception {

    String rule =
        "WENN der Name IST GLEICH Ilja\r\n"
            + "UND das Alter IST KLEINER ALS 18\n"
            + "ODER das Alter IST GLEICH 18\n"
            + "DANN was für ein Quatsch! ";

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", null));

    assertThat(result, containsString(AND_TOKEN + "UND"));
    assertThat(result, containsString(OR_TOKEN + "ODER"));
  }

  @Test
  void should_resolve_operators_where_one_is_substring_of_the_other() throws Exception {

    String rule =
        "WENN der Name IST Ilja\r\n"
            + "UND das Alter IST KLEINER ALS 18\r\n"
            + "DANN was für ein Quatsch! ";

    DefaultPreProcessor pre = new DefaultPreProcessor();
    String result = pre.process(rule, getPreprocessorContext("de", null));

    assertThat(result, containsString(EQUALS_TOKEN + KEYWORD_SYMBOL + "IST"));
  }

  @Test
  void should_replace_2_newlines_with_1_paragraph_token_WINDOWS_nl() throws Exception {
    String rule = "KOMMENTAR Das is Kommentar 1\r\n\r\n" + "KOMMENTAR Das is Kommentar 2";
    String paragraphToken = "ʬparagraphʬ";

    DefaultPreProcessor pre = new DefaultPreProcessor();

    String result = pre.process(rule, getPreprocessorContext("de", null));
    int numberOfParagraphs = result.split(paragraphToken).length - 1;

    assertThat(numberOfParagraphs, equalTo(1));
  }

  @Test
  void should_replace_2_newlines_with_1_paragraph_token_UNIX_nl() throws Exception {
    String rule = "KOMMENTAR Das is Kommentar 1\n\n" + "KOMMENTAR Das is Kommentar 2";
    String paragraphToken = "ʬparagraphʬ";

    DefaultPreProcessor pre = new DefaultPreProcessor();

    String result = pre.process(rule, getPreprocessorContext("de", null));
    int numberOfParagraphs = result.split(paragraphToken).length - 1;

    assertThat(numberOfParagraphs, equalTo(1));
  }

  @Test
  void should_replace_5_newlines_with_1_paragraph_token_nl() throws Exception {
    String rule = "KOMMENTAR Das is Kommentar 1\n\n\n\n\n" + "KOMMENTAR Das is Kommentar 2";
    String paragraphToken = "ʬparagraphʬ";

    DefaultPreProcessor pre = new DefaultPreProcessor();

    String result = pre.process(rule, getPreprocessorContext("de", null));
    int numberOfParagraphs = result.split(paragraphToken).length - 1;

    assertThat(numberOfParagraphs, equalTo(1));
  }

  @Test
  void should_replace_5_newlines_with_1_paragraph_token_UNIX_nl() throws Exception {
    String rule =
        "KOMMENTAR Das is Kommentar 1\r\n\r\n\r\n\r\n\r\n" + "KOMMENTAR Das is Kommentar 2";
    String paragraphToken = "ʬparagraphʬ";

    DefaultPreProcessor pre = new DefaultPreProcessor();

    String result = pre.process(rule, getPreprocessorContext("de", null));
    int numberOfParagraphs = result.split(paragraphToken).length - 1;

    assertThat(numberOfParagraphs, equalTo(1));
  }

  @Test
  void should_replace_5_newlines_with_whitespace_with_1_paragraph_token_nl() throws Exception {
    String rule =
        "KOMMENTAR Das is Kommentar 1   \n      \n    \n  \n   \n   "
            + "KOMMENTAR Das is Kommentar 2";
    String paragraphToken = "ʬparagraphʬ";

    DefaultPreProcessor pre = new DefaultPreProcessor();

    String result = pre.process(rule, getPreprocessorContext("de", null));
    int numberOfParagraphs = result.split(paragraphToken).length - 1;

    assertThat(numberOfParagraphs, equalTo(1));
  }

  @Test
  void should_replace_5_newlines_with_whitespace_with_1_paragraph_token_UNIX_nl() throws Exception {
    String rule =
        "KOMMENTAR Das is Kommentar 1   \r\n   \r\n   \r\n    \r\n   \r\n   "
            + "KOMMENTAR Das is Kommentar 2";
    String paragraphToken = "ʬparagraphʬ";

    DefaultPreProcessor pre = new DefaultPreProcessor();

    String result = pre.process(rule, getPreprocessorContext("de", null));
    int numberOfParagraphs = result.split(paragraphToken).length - 1;

    assertThat(numberOfParagraphs, equalTo(1));
  }

  private PreProcessorContext getPreprocessorContext(String locale, String workingDir) {
    PreProcessorContext ctx = new PreProcessorContext();
    ctx.setLocale(new Locale(locale));
    ctx.setWorkingDirectory(new String[] {workingDir});
    ctx.setSchema(new DataSchema());

    return ctx;
  }
}
