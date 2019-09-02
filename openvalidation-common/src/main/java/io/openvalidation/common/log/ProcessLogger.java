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

package io.openvalidation.common.log;

import java.util.HashMap;
import java.util.Map;

public class ProcessLogger {
  private static Map<String, LogicalProcess> _loggers = new HashMap<>();

  public static String CLI = "CLI";
  public static String CLI_PARSE_ARGUMENTS = "CLI - parse arguments";
  public static String CLI_VALIDATE_ARGS = "CLI - validate arguments";
  public static String CLI_INIT_OV_INSTANCE = "CLI - init ov instance";

  public static String PARSER = "PARSER";
  public static String PARSER_MAIN = "PARSER - main";

  public static String PREPROCESSOR = "PREPROCESSOR";
  public static String PREPROCESSOR_RESOLVE_ALIASES = "PREPROCESSOR - resolve aliases";
  public static String PREPROCESSOR_RESOLVE_INCLUDES = "PREPROCESSOR - resolve includes";

  public static String VALIDATOR = "AST VALIDATOR";

  public static String GENERATOR = "CODE GENERATOR";

  public static void initialize() {
    LogicalProcess process = new LogicalProcess(CLI);

    process
        .append(CLI_PARSE_ARGUMENTS)
        .append(CLI_VALIDATE_ARGS)
        .append(CLI_INIT_OV_INSTANCE)
        .create(PREPROCESSOR)
        .append(PREPROCESSOR_RESOLVE_INCLUDES)
        .append(PREPROCESSOR_RESOLVE_ALIASES)
        .Parent
        .create(PARSER)
        .append(PARSER_MAIN)
        .Parent
        .create(VALIDATOR)
        .Parent
        .append(GENERATOR);

    _loggers.put(getThreadID(), process);
  }

  public static void error(String name) {
    error(name, null, null);
  }

  public static void error(String name, String message) {
    error(name, message, null);
  }

  public static void error(String name, Exception e) {
    error(name, null, e);
  }

  public static void error(String name, String message, Exception exp) {
    LogicalProcess process = getCurrentProcess();
    if (process != null) process.errored(name, message, exp);
  }

  public static void warning(String name) {
    warning(name, null);
  }

  public static void warning(String name, String message) {
    warning(name, message, null);
  }

  public static void warning(String name, String message, Exception exp) {
    LogicalProcess process = getCurrentProcess();
    if (process != null) process.warned(name, message, exp);
  }

  public static void success(String name) {
    LogicalProcess process = getCurrentProcess();

    if (process != null) process.succeeded(name);
  }

  public static String print() {
    LogicalProcess process = getCurrentProcess();

    return (process != null) ? process.print() : "";
  }

  private static String getThreadID() {
    return "" + Thread.currentThread().getId();
  }

  private static LogicalProcess getCurrentProcess() {
    return _loggers.get(getThreadID());
  }
}
