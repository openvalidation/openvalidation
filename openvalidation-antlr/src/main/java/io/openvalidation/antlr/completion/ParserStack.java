package io.openvalidation.antlr.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.antlr.v4.runtime.atn.*;

public class ParserStack {

  public static final class Result {

    private final boolean error;
    private final ParserStack stack;

    private Result(boolean error, ParserStack stack) {
      this.error = error;
      this.stack = stack;
    }

    public boolean hasError() {
      return error;
    }

    public ParserStack getStack() {
      return stack;
    }
  }

  private final List<ATNState> states;

  public ParserStack() {
    this(Collections.emptyList());
  }

  public ParserStack(List<ATNState> states) {
    this.states = new ArrayList<>(states);
  }

  public Result process(ATNState state) {
    Result result;
    if (state instanceof RuleStartState
        || state instanceof StarBlockStartState
        || state instanceof BasicBlockStartState
        || state instanceof PlusBlockStartState
        || state instanceof StarLoopEntryState) {
      result = new Result(true, new ParserStack(concat(states, state)));
    } else if (state instanceof BlockEndState) {
      if (((BlockEndState) state).startState.equals(lastOf(states))) {
        result = new Result(true, new ParserStack(states.subList(0, states.size() - 1)));
      } else {
        result = new Result(false, this);
      }
    } else if (state instanceof LoopEndState) {
      ATNState lastElement = lastOf(states);
      if (lastElement instanceof StarLoopEntryState
          && ((StarLoopEntryState) lastElement)
              .loopBackState.equals(((LoopEndState) state).loopBackState)) {
        result = new Result(true, new ParserStack(states.subList(0, states.size() - 1)));
      } else {
        result = new Result(false, this);
      }
    } else if (state instanceof RuleStopState) {
      ATNState lastElement = lastOf(states);
      if (lastElement instanceof RuleStartState
          && ((RuleStartState) lastElement).stopState.equals(state)) {
        result = new Result(true, new ParserStack(states.subList(0, states.size() - 1)));
      } else {
        result = new Result(false, this);
      }
    } else if (state instanceof BasicState
        || state instanceof BlockEndState
        || state instanceof StarLoopbackState
        || state instanceof PlusLoopbackState) {
      result = new Result(true, this);
    } else {
      throw new UnsupportedOperationException(state.getClass().getCanonicalName());
    }
    return result;
  }

  private static <T> T lastOf(List<T> list) {
    if (list.isEmpty()) {
      throw new NoSuchElementException();
    }
    return list.get(list.size() - 1);
  }

  private static <T> List<T> concat(List<T> list, T element) {
    ArrayList<T> newList = new ArrayList<>(list);
    newList.add(element);
    return newList;
  }
}
