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

import io.openvalidation.common.log.LogicalProcess;
import org.junit.jupiter.api.Test;

public class LogicalProcessTest {

  @Test
  public void should_print_output() {
    LogicalProcess coreProcess = new LogicalProcess("Process Root");

    coreProcess
        .append("Task L1")
        .create("Task2 L1")
        .append("Task L1 -> L2")
        .append("Task2 L1 -> L2");

    String actualResult = coreProcess.print(false);

    // System.out.print(actualResult);
  }

  @Test
  public void should_print_output2() {
    LogicalProcess coreProcess = new LogicalProcess("Process Root");

    coreProcess
        .append("Task L1")
        .create("Task2 L1")
        .append("Task L1 -> L2")
        .create("Task2 L1 -> L2")
        .append("Task2 L2 -> L3")
        .Parent
        .Parent
        .create("Task3 L1")
        .create("Task L1 -> L2")
        .create("Task L2 -> L3");

    coreProcess
        .succeeded("Task2 L1 -> L2")
        .errored("Task L1 -> L2", "some errors occured", null)
        .warned("Task2 L1", "some errors occured", null)
        .warned("Task L2 -> L3", "some errors occured", null);

    // System.out.print(coreProcess.print());
  }
}
