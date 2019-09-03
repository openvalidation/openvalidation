package io.openvalidation.antlr.completion;

import java.util.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.misc.IntervalSet;

public class AntlrAutoCompletionSuggester implements AutoCompletionSuggester {
  private List<String> _ruleNames;
  private Vocabulary _vocabulary;
  private ATN _atn;

  public AntlrAutoCompletionSuggester(String[] _ruleNames, Vocabulary _vocabulary, ATN _atn) {
    this._ruleNames = Arrays.asList(_ruleNames);
    this._vocabulary = _vocabulary;
    this._atn = _atn;
  }

  @Override
  public HashSet<Integer> suggestions(EditorContext editorContext) {
    HashSet<Integer> collector = new HashSet<>();

    this.process(
        this._ruleNames,
        this._vocabulary,
        this._atn.states.get(0),
        new MyTokenStream(editorContext.preceedingTokens()),
        collector,
        new ParserStack());

    IntervalSet set = this._atn.getExpectedTokens(98, null);

    return collector;
  }

  private void process(
      List<String> ruleNames,
      Vocabulary vocabulary,
      ATNState state,
      MyTokenStream tokens,
      HashSet<Integer> collector,
      ParserStack parserStack) {
    List<String> history = new ArrayList<>();
    history.add("start");

    Set<String> passed = new HashSet<>();

    this.process(state, tokens, collector, parserStack, passed, history);
  }

  private void process(
      ATNState state,
      MyTokenStream tokens,
      HashSet<Integer> collector,
      ParserStack parserStack,
      Set<String> alreadyPassed,
      List<String> history) {
    // LOG.debug("history size {}", history.size());

    final boolean atCaret = tokens.atCaret();
    ParserStack.Result stackRes = parserStack.process(state);
    if (!stackRes.hasError()) {
      return;
    }

    for (Transition transition : state.getTransitions()) {
      // final String desc = describe(ruleNames, vocabulary, state, transition);
      String desc = "";
      if (transition.isEpsilon()) {
        if (!alreadyPassed.contains(transition.target.stateNumber)) {
          alreadyPassed.add(String.valueOf(transition.target.stateNumber));
          process(
              transition.target,
              tokens,
              collector,
              stackRes.getStack(),
              alreadyPassed,
              plus(history, desc));
          /*process(transition.target, tokens, collector, stackRes.getStack(),
          plus(alreadyPassed, transition.target.stateNumber), plus(history, desc));*/

        }
      } else if (transition instanceof AtomTransition) {
        Token nextTokenType = tokens.next();
        if (atCaret) {
          if (isCompatibleWithStack(transition.target, parserStack)) {
            collector.add(((AtomTransition) transition).label);
          }
        } else {
          if (nextTokenType.getType() == ((AtomTransition) transition).label) {
            process(
                transition.target,
                tokens.move(),
                collector,
                stackRes.getStack(),
                new HashSet<>(),
                plus(history, desc));
          }
        }
      } else if (transition instanceof SetTransition) {
        Token nextTokenType = tokens.next();
        transition
            .label()
            .toList()
            .forEach(
                sym -> {
                  if (atCaret) {
                    if (isCompatibleWithStack(transition.target, parserStack)) {
                      collector.add(sym);
                    }
                  } else {
                    if (nextTokenType.getType() == sym) {
                      process(
                          transition.target,
                          tokens.move(),
                          collector,
                          stackRes.getStack(),
                          new HashSet<>(),
                          plus(history, desc));
                    }
                  }
                });
      } else {
        throw new UnsupportedOperationException(transition.getClass().getCanonicalName());
      }
    }
  }

  private boolean isCompatibleWithStack(ATNState state, ParserStack parserStack) {
    ParserStack.Result response = parserStack.process(state);
    if (!response.hasError()) {
      return false;
    }

    if (state.epsilonOnlyTransitions) {
      return Arrays.stream(state.getTransitions())
          .anyMatch(transition -> isCompatibleWithStack(transition.target, response.getStack()));
    } else {
      return true;
    }
  }

  private static <E> Set<E> plus(Set<E> set, E value) {
    HashSet<E> result = new HashSet<>(set);
    result.add(value);
    return result;
  }

  private static <E> List<E> plus(List<E> list, E value) {
    ArrayList<E> result = new ArrayList<>(list);
    result.add(value);
    return result;
  }
}
