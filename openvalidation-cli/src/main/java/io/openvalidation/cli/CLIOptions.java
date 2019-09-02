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

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import io.openvalidation.common.model.Language;

public class CLIOptions extends OptionsBase {

  // switches

  @Option(name = "help", abbrev = 'h', help = "prints usage info", defaultValue = "false")
  public boolean help;

  @Option(
      name = "al",
      abbrev = 'a',
      help = "lists all available output programming languages.",
      defaultValue = "false")
  public boolean listAvailableFrameworks;

  @Option(
      name = "single-file",
      abbrev = 'f',
      help = "generates the code including rules and framemework to the single file",
      defaultValue = "false")
  public boolean single;

  @Option(
      name = "verbose",
      abbrev = 'v',
      help = "output the extended logging",
      defaultValue = "false")
  public boolean verbose;

  @Option(name = "no-banner", abbrev = 'n', help = "hide banner", defaultValue = "false")
  public boolean noBanner;

  // end of switches

  @Option(
      name = "culture",
      abbrev = 'c',
      help = "culture code of the natural language. for example: de-DE, en-US, ru",
      defaultValue = "")
  public String culture;

  @Option(
      name = "rule",
      abbrev = 'r',
      help = "rule as plain content or path to the rule file or an url to the rule file",
      defaultValue = "")
  public String rule;

  @Option(name = "output", abbrev = 'o', help = "output folder or file", defaultValue = "")
  public String out;

  @Option(
      name = "language",
      abbrev = 'l',
      help = "an output programming language",
      converter = LanguageConverter.class,
      defaultValue = "Java")
  public Language language;

  @Option(
      name = "params",
      abbrev = 'p',
      help = "custom params. use like \"param1=value1;param2=value2\"",
      defaultValue = "")
  public String params;

  @Option(
      name = "schema",
      abbrev = 's',
      help = "schema definition to enable property and type recognition",
      defaultValue = "")
  public String schema;
}
