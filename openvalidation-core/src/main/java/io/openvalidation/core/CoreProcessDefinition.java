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

package io.openvalidation.core;

import io.openvalidation.common.log.LogicalProcess;

public class CoreProcessDefinition {

  public static LogicalProcess CreateCoreProcess() {

    LogicalProcess coreProcess = new LogicalProcess("Core");
    coreProcess
        .create("Init Core")
        .Parent
        .create("Run Engine")
        .append("Parse AST")
        .append("Generate Code")
        .append("Generate Code File");

    return coreProcess;
  }

  public static LogicalProcess CreatePreProcess() {

    LogicalProcess process = new LogicalProcess("PreProcess");
    process
        .create("Init Core")
        .Parent
        .create("Run Engine")
        .append("Parse AST")
        .append("Generate Code")
        .append("Generate Code File");

    return process;
  }

  public static LogicalProcess CreateParsingProcess() {

    LogicalProcess process = new LogicalProcess("Parsing");
    process
        .create("Init Core")
        .Parent
        .create("Run Engine")
        .append("Parse AST")
        .append("Generate Code")
        .append("Generate Code File");

    return process;
  }

  public static LogicalProcess CreateGenerationProcess() {

    LogicalProcess process = new LogicalProcess("Generation");
    process
        .create("Init Core")
        .Parent
        .create("Run Engine")
        .append("Parse AST")
        .append("Generate Code")
        .append("Generate Code File");

    return process;
  }
}
