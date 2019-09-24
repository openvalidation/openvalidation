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

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.log.ProcessLogger;
import io.openvalidation.common.model.Languages;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.core.OpenValidation;
import java.util.*;

public class CLIApplication {

  private CLIOptions _options;
  public boolean canRun = false;
  private OpenValidation _openValidation;

  private List<String> messages = new ArrayList<>();

  public CLIApplication(OpenValidation openValidation, CLIOptions options)
      throws OpenValidationException {
    if (openValidation == null)
      throw new OpenValidationException("openValidation instance should not be null!");

    this._options = options;
    this.canRun = this.validateOptions(options);
    this._openValidation = openValidation;
  }

  public boolean validateOptions(CLIOptions options) {

    if (options.listAvailableFrameworks) {
      messages.add(
          "Available Frameworks: "
              + System.lineSeparator()
              + System.lineSeparator()
              + String.join(
                  System.lineSeparator(),
                  Languages.getLanguageStream()
                      .map(language -> language.getName())
                      .toArray(String[]::new)));

      ProcessLogger.error(ProcessLogger.CLI_VALIDATE_ARGS);
      return false;
    }

    if (options.rule == null || options.rule.isEmpty()) {
      this.messages.add("missing argument --rule(-r).  \n\n");

      ProcessLogger.error(ProcessLogger.CLI_VALIDATE_ARGS);
      return false;
    }

    if (options.schema == null || options.schema.isEmpty()) {
      this.messages.add("missing argument --schema(-s).  \n\n");

      ProcessLogger.error(ProcessLogger.CLI_VALIDATE_ARGS);
      return false;
    }

    ProcessLogger.success(ProcessLogger.CLI_VALIDATE_ARGS);
    return true;
  }

  public OpenValidation initOpenValidationInstance() throws Exception {
    if (this._options.culture != null && !this._options.culture.isEmpty())
      this._openValidation.setLocale(this._options.culture);

    if (this._options.language != null) this._openValidation.setLanguage(this._options.language);

    if (this._options.rule != null && !this._options.rule.isEmpty())
      this._openValidation.setRule(
          this._options.rule.replace("\\r\\n", "\r\n").replace("\\n", "\n"));

    if (this._options.out != null && !this._options.out.isEmpty())
      this._openValidation.setOutput(this._options.out);

    if (this._options.schema != null && !this._options.schema.isEmpty())
      this._openValidation.setSchema(this._options.schema);

    this._openValidation.setVerbose(this._options.verbose);
    this._openValidation.setSingleFile(this._options.single);

    if (this._options.params != null && !this._options.params.isEmpty()) {

      String[] params = this._options.params.split(";");
      if (params != null && params.length > 0) {

        Map<String, Object> result = new HashMap<>();

        for (String p : params) {
          String[] kv = p.trim().split("=");
          if (kv != null && kv.length == 2) {
            String key = kv[0].trim();
            String value = kv[1].trim();

            result.put(key, value);
          } else
            throw new OpenValidationException(
                "wrong argument params: " + this._options.params + " [" + p + "]");
        }

        this._openValidation.setParams(result);
      }
    }

    return this._openValidation;
  }

  public OpenValidationResult run() throws Exception {

    if (!this.canRun)
      return OpenValidationResult.createErrorResult(
          "can't run openvalidation. There are some wrong arguments. please see usage!\n\n"
              + String.join("\n", getValidationMessages()));

    try {
      this.initOpenValidationInstance();
      ProcessLogger.success(ProcessLogger.CLI_INIT_OV_INSTANCE);

      OpenValidationResult result = this._openValidation.generate();

      return result;
    } catch (Exception exp) {
      return OpenValidationResult.createErrorResult(exp);
    }
  }

  public String[] getValidationMessages() {
    return this.messages.toArray(new String[0]);
  }
}
