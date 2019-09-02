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

import io.openvalidation.common.utils.ConsoleColors;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class LogicalTask {

  public LogicalTask Parent;
  public String Name;
  public LogicalTaskState State;
  public String Message;
  public Exception Exception;
  public int Level = 0;

  public long startDate;
  public long endDate;
  public long msLeft;

  private List<LogicalTask> _subtasks = new ArrayList<>();

  public LogicalTask(String name, LogicalTask parent) {
    this.State = LogicalTaskState.NotExecuted;
    this.Name = name;
    this.Parent = parent;
    if (this.Parent != null) this.Level = this.Parent.Level + 1;

    startDate = System.currentTimeMillis();
  }

  public LogicalTask create(String name) {
    LogicalTask task = new LogicalTask(name, this);
    this._subtasks.add(task);
    return task;
  }

  public LogicalTask append(String name) {
    LogicalTask task = new LogicalTask(name, this);
    this._subtasks.add(task);
    return this;
  }

  public void end() {
    this.endDate = System.currentTimeMillis();
    this.msLeft = (endDate - startDate);
  }

  public LogicalTask find(String name) {
    if (this.Name.toLowerCase().equals(name.toLowerCase())) return this;

    LogicalTask outTask = null;

    for (LogicalTask task : this._subtasks) {
      outTask = task.find(name);
      if (outTask != null) return outTask;
    }

    return null;
  }

  public boolean hasState(LogicalTaskState stateToFind) {
    if (this.State == stateToFind) return true;

    for (LogicalTask task : this._subtasks) {
      if (task.hasState(stateToFind)) return true;
    }

    return false;
  }

  String prefix = "|";
  int spacesPerLevel = 8;

  public String print() {
    return print(true);
  }

  public String getError() {
    if (this.Message != null && this.Message.trim().length() > 0) return " : " + this.Message;

    if (this.Exception != null && this.Exception.getMessage() != null)
      return " : " + this.Exception.getMessage();

    return "";
  }

  public String print(boolean colored) {

    StringBuilder sb = new StringBuilder();
    boolean hasNext = this.hasNextItem();
    String leftSpace = getLeftPrefix(true);

    String leftSpace2 = leftSpace.substring(0, leftSpace.length() - 1);
    String bottomSpace = (hasNext) ? leftSpace : leftSpace2 + " ";
    String spaceArrow = (hasNext) ? "|" : "┗";

    if (this.Level == 0) {
      sb.append(getStateColor(LogicalTaskState.Succeeded, colored) + "  ┏-----------┐\n") // 0xE2 ┏
          .append("  |   START   |\n")
          .append("               \n" + getResetColor(colored));
    }

    sb.append(getStateColor(colored) + leftSpace + "\n")
        .append(leftSpace + "\n")
        .append(leftSpace + "     ┏---\n")
        .append(
            leftSpace2
                + spaceArrow
                + "---> |  "
                + this.Name
                + " "
                + Long.toString(this.msLeft)
                + "ms "
                + getError()
                + "\n")
        .append(bottomSpace + "     ┗--------------\n" + getResetColor(colored));

    for (LogicalTask task : this._subtasks) {
      sb.append(task.print());
    }

    // return arrow
    if (this._subtasks.size() == 0 && !hasNext && this.Level > 0) {
      int levelWithParent = this.Parent.getLevelOfParentWithNext();
      String arrowBackLine =
          StringUtils.padLeft("-", (this.Level - levelWithParent) * spacesPerLevel)
              .replace(" ", "-");
      String arrowBackSpaces =
          StringUtils.padLeft(
              " ", ((this.Level - levelWithParent) * spacesPerLevel) + (spacesPerLevel + 1));

      String p = leftSpace.trim();
      p = p.endsWith(prefix) ? p.substring(0, p.length() - 1) : p;

      String arl =
          "<" + arrowBackLine + StringUtils.padLeft("-", spacesPerLevel).replace(" ", "-") + "┛";
      String arrowBack = "       " + p.trim() + arl;

      sb.append("       " + getStateColor(colored) + p.trim() + arrowBackSpaces + prefix + "\n");
      sb.append(arrowBack + getResetColor(colored) + "\n");
    }

    if (this.Level == 0) {
      LogicalTaskState state = LogicalTaskState.Succeeded;
      if (this instanceof LogicalProcess) {
        state = ((LogicalProcess) this).getCompleteState();
      }

      sb.append(getStateColor(state, colored) + leftSpace + "\n")
          .append(StringUtils.padLeft(" ", spacesPerLevel) + "\n")
          .append("  |    STOP   |\n")
          .append("  ┗-----------┛\n" + getResetColor(colored));
    }

    return sb.toString();
  }

  private String getStateColor(boolean colored) {
    return getStateColor(this.State, colored);
  }

  private String getResetColor(boolean colored) {
    if (!colored) return "";

    return ConsoleColors.RESET;
  }

  private String getStateColor(LogicalTaskState state, boolean colored) {
    if (!colored) return "";

    switch (state) {
      case NotExecuted:
        return ConsoleColors.RESET;
      case Warned:
        return ConsoleColors.YELLOW;
      case Succeeded:
        return ConsoleColors.GREEN;
      case Started:
        return ConsoleColors.YELLOW;
      case Errored:
        return ConsoleColors.RED;
    }

    return ConsoleColors.RESET;
  }

  private String getLeftPrefix(boolean isRoot) {
    if (this.Level == 0) return StringUtils.padLeft(prefix, spacesPerLevel);

    String pr = (isRoot || this.hasNextItem()) ? prefix : "";
    String current = StringUtils.padLeft(pr, spacesPerLevel);

    return this.Parent.getLeftPrefix(false) + current;
  }

  private boolean hasNextItem() {
    if (this.Parent == null) return true;

    int indx = this.Parent._subtasks.indexOf(this);

    return indx < (this.Parent._subtasks.size() - 1);
  }

  private int getLevelOfParentWithNext() {
    if (this.hasNextItem()) {
      return this.Level;
    } else if (this.Parent != null) {
      return this.Parent.getLevelOfParentWithNext();
    }

    return 0;
  }
}
