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
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.Console;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.core.OpenValidation;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class End2EndTest {

  @Test
  public void execute_simple_end2end_test() throws Exception {
    String comment = "ich war auch hier";
    String rule = "KOMMENTAR " + comment;
    String schema = "{Nachricht : \"Text\"}";
    String expected = "// " + comment;

    CLIOptions inputOptions =
        this.getOptions("-r", rule, "-s", schema, "-l", "javascript", "-c", "de");
    CLIApplication app = new CLIApplication(OpenValidation.createDefault(), inputOptions);

    assertThat(app.canRun, is(true));

    OpenValidationResult result = app.run();

    assertThat(result, is(notNullValue()));

    if (result.hasErrors()) Console.error(result.getErrorPrint(true));

    assertThat(result.hasErrors(), is(false));

    List<OpenValidationException> errors = result.getErrors();
    assertThat(errors, is(notNullValue()));
    assertThat(errors.size(), is(0));
  }

  @ParameterizedTest
  @CsvSource({
    " ,missing argument --schema(-s)",
    "{}, JSON Schema schould not be empty",
    "{x}, invalid JSON Schema Format"
  })
  public void expect_error_while_wrong_schema(String schema, String expectedError)
      throws Exception {
    String rule = "COMMENT hello";

    if (schema == null) schema = "";

    CLIOptions inputOptions = this.getOptions("-r", rule, "-c", "en", "-s", schema);
    CLIApplication app = new CLIApplication(OpenValidation.createDefault(), inputOptions);

    OpenValidationResult result = app.run();

    assertThat(result.hasErrors(), is(true));
    List<String> messages = LINQ.select(result.getErrors(), e -> e.getMessage());

    assertThat(messages, notNullValue());
    assertThat(messages, hasSize(1));
    assertThat(String.join(",", messages), containsString(expectedError));
  }

  @Test
  public void path_to_rules() throws Exception {
    // String comment = "ich war hier";
    //        String rule = "/rules.ovl";
    //        String schema = "{Nachricht : \"Text\"}";
    //        //String expected = "// " + comment;
    //
    //        CLIOptions inputOptions = this.getOptions("-r", rule,"-s", schema, "-l",
    // "javascript");
    //        CLIApplication app = new CLIApplication(OpenValidation.newExpectation(),
    // inputOptions);
    //
    //        assertThat(app.canRun, is(true));
    //
    //        OpenValidationResult result = app.run();
    //
    //
    //        assertThat(result, is(notNullValue()));
    //
    //        if (result.hasErrors())
    //            Console.error(result.getErrorPrint(true));
    //
    //        assertThat(result.hasErrors(), is(false));
    //
    //        List<OpenValidationException> errors = result.getErrors();
    //        assertThat(errors, is(notNullValue()));
    //        assertThat(errors.size(), is(0));
  }

  private CLIOptions getOptions(String... args) {
    OptionsParser parser = OptionsParser.newOptionsParser(CLIOptions.class);
    parser.parseAndExitUponError(args);
    return parser.getOptions(CLIOptions.class);
  }
}
