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

public class LogicalProcess extends LogicalTask {

  public LogicalProcess(String name) {
    super(name, null);
  }

  public LogicalProcess start(String name) {
    LogicalTask task = this.find(name);

    if (task != null) task.State = LogicalTaskState.Started;

    return this;
  }

  public LogicalProcess succeeded(String name) {
    LogicalTask task = this.find(name);

    if (task != null) {
      task.State = LogicalTaskState.Succeeded;
      task.end();
    }

    // super.end();

    return this;
  }

  public LogicalProcess errored(String name, String errorMessage, Exception exception) {
    LogicalTask task = this.find(name);

    if (task != null) {
      task.State = LogicalTaskState.Errored;
      task.Message = errorMessage;
      task.Exception = exception;
      task.end();
    }

    // super.end();
    return this;
  }

  public LogicalProcess warned(String name, String errorMessage, Exception exception) {
    LogicalTask task = this.find(name);

    if (task != null) {
      task.State = LogicalTaskState.Warned;
      task.Message = errorMessage;
      task.Exception = exception;
      task.end();
    }

    // super.end();
    return this;
  }

  public LogicalTaskState getCompleteState() {
    if (this.hasState(LogicalTaskState.Errored)) return LogicalTaskState.Errored;
    if (this.hasState(LogicalTaskState.Warned)) return LogicalTaskState.Warned;

    return LogicalTaskState.Succeeded;
  }
}
