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

package io.openvalidation.cli;

import static java.lang.System.out;

import com.google.devtools.common.options.OptionsParser;
import io.openvalidation.common.log.ProcessLogger;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.Console;
import io.openvalidation.common.utils.JsonUtils;
import io.openvalidation.common.utils.OSUtils;
import io.openvalidation.core.OpenValidation;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

// @SpringBootApplication
public class Application // implements CommandLineRunner
 {
  // private static SpringApplication _app;
  public static long start;

  public static void main(String[] args) throws Exception {
    start = System.currentTimeMillis();

    System.setProperty("file.encoding", "UTF-8");
    System.setProperty("sun.jnu.encoding", "UTF-8");

    Field charset = Charset.class.getDeclaredField("defaultCharset");
    charset.setAccessible(true);
    charset.set(null, null);

    if (args == null
        || (!Arrays.asList(args).contains("-n") && !Arrays.asList(args).contains("--no-banner"))) {
      Console.print(Banner.Text);
    }
    Console.print("\n");

    run(args);
  }

  // @Override
  public static void run(String... args) throws Exception {

    ProcessLogger.initialize();
    OptionsParser parser = OptionsParser.newOptionsParser(CLIOptions.class);
    parser.parseAndExitUponError(args);
    CLIOptions options = parser.getOptions(CLIOptions.class);

    ProcessLogger.success(ProcessLogger.CLI_PARSE_ARGUMENTS);
    OpenValidationResult result = null;
    boolean isDebugPrinted = false;

    try {
      OpenValidation op = OpenValidation.createDefault();
      CLIApplication app = new CLIApplication(op, options);

      if (app.canRun) {
        Console.print("\nstart compiling rule set...\n");
        result = app.run();

        //
        // options.noBanner

        if (result.hasErrors()) {
          // Console.titleStart("AN ERROR OCCURRED WHILE COMPILING RULE SET");

          Console.error("\n\n" + result.getErrorPrint(options.verbose));

          Console.print(result.getCreatedFilesPrint());

          printDebug(options, result);
          isDebugPrinted = true;

          ProcessLogger.error(ProcessLogger.CLI);
          Console.print(result.getSummaryPrint());
          Console.error(
              "\nCOMPILATION WAS TERMINATED WITH ERRORS in "
                  + (System.currentTimeMillis() - start)
                  + " ms.\n\n");
        } else {
          Console.titleStart("GENERATED CODE");
          // Output of the console
          Console.print(result.getImplementationCodeContent());

          Console.titleEnd("GENERATED CODE");

          Console.print(result.getCreatedFilesPrint());

          printDebug(options, result);
          isDebugPrinted = true;

          Console.print(result.getSummaryPrint());
          Console.success(
              "\nCOMPILATION WAS SUCCESSFULLY FINISHED in "
                  + (System.currentTimeMillis() - start)
                  + " ms.\n\n");
          ProcessLogger.success(ProcessLogger.CLI);
        }
      } else {
        ProcessLogger.error(ProcessLogger.CLI);
        printUsage(parser, app.getValidationMessages());
      }
    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.CLI, e);
      printUsage(parser, new String[] {e.toString()});
    }

    if (!isDebugPrinted) printDebug(options, result);
  }

  private static void printDebug(CLIOptions options, OpenValidationResult result) {
    if (options != null && options.verbose) {

      Console.titleStart("ADDITIONAL DEBUG INFORMATIONS");
      Console.print(ProcessLogger.print());
      if (result != null) {
        Console.print(result.toString(options.verbose));
      }

      Console.titleStart("ENCODING");
      out.println("Default Locale:   " + Locale.getDefault());
      out.println("Default Charset:  " + Charset.defaultCharset());
      out.println("file.encoding;    " + System.getProperty("file.encoding"));
      out.println("sun.jnu.encoding: " + System.getProperty("sun.jnu.encoding"));
      out.println("Default Encoding: " + OSUtils.getEncoding());
      out.println("\n");

      if (options.schema != null && !options.schema.isEmpty()) {
        Console.titleStart("Schema Argument");
        try {
          out.println(JsonUtils.loadJson(options.schema).toString(2));
        } catch (Exception e) {
          out.println(options.schema);
        }

        out.println("\n");
      }
    }
  }

  private static void printUsage(OptionsParser parser, String[] errors) {
    if (errors != null && errors.length > 0) {
      out.println("ERRORS occurred: \n\n");

      for (String error : errors) {
        out.println(error);
      }

      out.println("+++++++++++++++++++++++++++++\n\n");
    }

    out.println("Usage: java -jar openvalidation.jar OPTIONS\n");
    out.println(
        parser.describeOptions(
            Collections.<String, String>emptyMap(), OptionsParser.HelpVerbosity.LONG));
  }
}
