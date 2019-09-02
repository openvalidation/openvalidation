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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.google.devtools.common.options.OptionsParser;
import io.openvalidation.cli.CLIApplication;
import io.openvalidation.cli.CLIOptions;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.interfaces.IOpenValidationGenerator;
import io.openvalidation.common.interfaces.IOpenValidationParser;
import io.openvalidation.common.interfaces.IOpenValidationPreprocessor;
import io.openvalidation.common.model.ContentOptionKind;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.utils.FileSystemUtils;
import io.openvalidation.core.Aliases;
import io.openvalidation.core.OpenValidation;
import io.openvalidation.core.OpenValidationOptions;
import java.io.File;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CLIOptionsInititalizationTest {
  private static String _workingDirectory =
      ((new File("")).getAbsolutePath() + File.separator).toLowerCase();

  @Mock IOpenValidationPreprocessor preprocessor;

  @Mock IOpenValidationParser parser;

  @Mock IOpenValidationGenerator generator;

  @InjectMocks OpenValidation ovInstance;

  // @Rule
  // public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_init_language_arguments_java() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("-l", "java");
    assertThat(resultOptions.getLanguage(), is(Language.Java));
  }

  @Test
  public void should_init_language_arguments_csharp() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("-l", "csharp");
    assertThat(resultOptions.getLanguage(), is(Language.CSharp));
  }

  @Test
  public void should_init_language_arguments_javascript() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("-l", "javascript");
    assertThat(resultOptions.getLanguage(), is(Language.JavaScript));
  }

  @Test
  public void should_init_culture_arguments() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("-c", "de");
    assertThat(resultOptions.getLocale(), is(new Locale("de")));
  }

  @Test
  public void should_not_init_culture_arguments() throws Exception {
    //        URL url = Thread.currentThread().getContextClassLoader().getResource(".");
    //        URL url3 = Aliases.class.getResource(".");
    //
    //        List<String> plist =
    // Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator)).filter(s ->
    // s.matches(".+core.+classes")).collect(Collectors.toList());
    //        URL[] result = new URL[plist.size()];
    //        for(int i = 0; i < plist.size(); i++) {
    //            result[i] = Paths.get(plist.get(i)).toAbsolutePath().toUri().toURL();
    //        }
    //
    //        String resStr = result[0].getFile().replaceFirst("/", "");
    //        File res = new File(resStr);
    //        File[] filez = res.listFiles();
    //

    Throwable expectedException = null;
    String wrongLocale = "d";
    OpenValidationException exceptions =
        Assertions.assertThrows(
            OpenValidationException.class,
            () -> {
              OpenValidationOptions resultOptions = this.createOptions("-c", wrongLocale);
              ;
              assertThat(
                  expectedException.getMessage(),
                  is(
                      "Language: '"
                          + wrongLocale
                          + "' could not be found. The following languages are currently available "
                          + Aliases.availableCultures));
              assertThat(resultOptions.getLocale(), is(new Locale("de")));
            });
  }
  //    public void should_not_init_culture_arguments() throws Exception {
  //        OpenValidationOptions resultOptions = this.createOptions("-c", "d");
  //        //assertThat(resultOptions.getLocale(), is(new Locale("de")));
  //    }

  @Test
  public void should_init_default_noneverbose() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("-c", "de");
    assertThat(resultOptions.isVerbose(), is(false));
  }

  @Test
  public void should_init_verbose() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("-c", "de", "-v");
    assertThat(resultOptions.isVerbose(), is(true));
  }

  @Test
  public void should_init_default_not_single_file_arguments() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions();
    assertThat(resultOptions.isSingleFile(), is(false));
  }

  @Test
  public void should_init_single_file_arguments() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("--single-file");
    assertThat(resultOptions.isSingleFile(), is(true));
  }

  @Test
  public void should_init_rule_as_content_arguments() throws Exception {
    String rule = "SOME RULE CONTENT";
    OpenValidationOptions resultOptions = this.createOptions("-r", rule);

    assertThat(resultOptions.getRuleContent(), is(rule));
    assertThat(resultOptions.getRuleOptionKind(), is(ContentOptionKind.Content));
  }

  @Test
  public void should_init_rule_as_url_arguments() throws Exception {
    String rule = "http://www.google.de/rule.ovl";
    OpenValidationOptions resultOptions = this.createOptions("-r", rule);

    assertThat(resultOptions.getRuleContent(), is(rule));
    assertThat(resultOptions.getRuleOptionKind(), is(ContentOptionKind.URL));
  }

  @Test
  public void should_init_rule_as_file_arguments() throws Exception {
    String rule = "/rule.ovl";
    OpenValidationOptions resultOptions = this.createOptions("-r", rule);

    assertThat(resultOptions.getRuleContent(), is(rule));
    assertThat(resultOptions.getRuleOptionKind(), is(ContentOptionKind.FilePath));
  }

  @Test
  public void should_init_default_out_folder_same_as_working_directory_arguments()
      throws Exception {
    OpenValidationOptions resultOptions = this.createOptions();

    assertThat(resultOptions.getOutputDirectory(), is(_workingDirectory));
    assertThat(resultOptions.getOutputCodeFileName(), is("OpenValidation"));
    assertThat(
        resultOptions.resolveCodeFileName(Language.Java).toLowerCase(),
        is((_workingDirectory + "OpenValidation.java").toLowerCase()));
  }

  @Test
  public void should_init__file_out_name_as_input_name_arguments() throws Exception {
    String rule = "/rule.ovl";
    OpenValidationOptions resultOptions = this.createOptions("-r", rule);

    assertThat(resultOptions.getOutputDirectory(), is(_workingDirectory));
    assertThat(resultOptions.getOutputCodeFileName(), is("rule"));
    assertThat(
        resultOptions.resolveCodeFileName(Language.Java).toLowerCase(),
        is((_workingDirectory + "rule.java").toLowerCase()));
  }

  @Test
  public void should_init_file_out_depends_on_out_arguments() throws Exception {
    String rule = "/rule.ovl";
    String outDir = FileSystemUtils.combinePath("c:", "temp");
    String outFullPath = FileSystemUtils.combinePath(outDir, "test.abc");
    OpenValidationOptions resultOptions = this.createOptions("-r", rule, "-o", outFullPath);

    assertThat(resultOptions.getOutputDirectory(), is(outDir));
    assertThat(resultOptions.getOutputCodeFileName(), is("test"));
    assertThat(
        resultOptions.resolveCodeFileName(Language.Java),
        is(FileSystemUtils.combinePath(outDir, "test.java")));
  }

  @Test
  public void should_init_directory_out_depends_on_out_arguments() throws Exception {
    String rule = "/rule.ovl";
    String outDir = FileSystemUtils.combinePath("c:", "temp");
    OpenValidationOptions resultOptions = this.createOptions("-r", rule, "-o", outDir);

    assertThat(resultOptions.getOutputDirectory(), is(outDir));
    assertThat(resultOptions.getOutputCodeFileName(), is("rule"));
    assertThat(
        resultOptions.resolveCodeFileName(Language.Java),
        is(FileSystemUtils.combinePath(outDir, "rule.java")));
  }

  @Test
  public void should_init_directory_out_with_default_rule_name_arguments() throws Exception {
    String outDir = FileSystemUtils.combinePath("c:", "temp");
    OpenValidationOptions resultOptions = this.createOptions("-o", outDir);

    assertThat(resultOptions.getOutputDirectory(), is(outDir));
    assertThat(resultOptions.getOutputCodeFileName(), is("OpenValidation"));
    assertThat(
        resultOptions.resolveCodeFileName(Language.Java),
        is(FileSystemUtils.combinePath(outDir, "OpenValidation.java")));
  }

  @Test
  public void should_init_custom_params() throws Exception {
    OpenValidationOptions resultOptions =
        this.createOptions("-p", "Hallo = Binhier ; Hallo2 = Test");

    assertThat(resultOptions.getParams(), is(notNullValue()));
    assertThat(resultOptions.getParams().size(), is(2));

    assertThat(resultOptions.getParams().get("Hallo"), is("Binhier"));
    assertThat(resultOptions.getParams().get("Hallo2"), is("Test"));
  }

  @Test
  public void should_throw_an_exception_while_init_custom_params() throws Exception {

    OpenValidationException exceptions =
        Assertions.assertThrows(
            OpenValidationException.class,
            () -> {
              OpenValidationOptions resultOptions = this.createOptions("-p", "Hallo=;=Test");
            });

    assertThat(exceptions.getMessage(), containsString("wrong argument params"));

    //        thrown.expect(OpenValidationException.class);
    //        thrown.expectMessage(containsString("wrong argument params"));
  }

  @Test
  public void should_init_schema_arguments() throws Exception {
    OpenValidationOptions resultOptions = this.createOptions("-s", "{x:1}");

    assertThat(resultOptions.getSchema(), is(notNullValue()));
    assertThat(resultOptions.getSchema().getProperties(), is(notNullValue()));
    assertThat(resultOptions.getSchema().getProperties().size(), is(1));
    assertThat(resultOptions.getSchema().findPropertyByFullName("x"), is(notNullValue()));
    assertThat(
        resultOptions.getSchema().findPropertyByFullName("x").getType(),
        is(DataPropertyType.Decimal));
  }

  @Test
  public void should_throw_an_exception_while_init_schema() throws Exception {

    OpenValidationException exceptions =
        Assertions.assertThrows(
            OpenValidationException.class,
            () -> {
              OpenValidationOptions resultOptions = this.createOptions("-s", "abc");
            });

    assertThat(exceptions.getMessage(), containsString("invalid JSON Schema Format"));

    //        thrown.expect(OpenValidationException.class);
    //        thrown.expectMessage(containsString("invalid JSON Schema Format"));
    //
    //        OpenValidationOptions resultOptions = this.createOptions("-s", "abc");
  }

  private OpenValidationOptions createOptions(String... args) throws Exception {
    CLIOptions inputOptions = this.getOptions(args);

    CLIApplication app = new CLIApplication(ovInstance, inputOptions);

    OpenValidation ovResultInstance = app.initOpenValidationInstance();
    return ovResultInstance.getOptions();
  }

  private CLIOptions getOptions(String... args) {
    OptionsParser parser = OptionsParser.newOptionsParser(CLIOptions.class);
    parser.parseAndExitUponError(args);
    return parser.getOptions(CLIOptions.class);
  }
}
