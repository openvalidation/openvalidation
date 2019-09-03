package io.openvalidation.antlr.completion;

import io.openvalidation.antlr.generated.mainBaseListener;
import io.openvalidation.antlr.generated.mainLexer;
import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.common.log.ProcessLogger;
import java.util.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.AtomTransition;
import org.antlr.v4.runtime.atn.SetTransition;
import org.antlr.v4.runtime.atn.Transition;

public class CodeCompletionListener extends mainBaseListener implements AutoCompletionSuggester {

  private mainParser _parser;
  private mainParser.MainContext _ctx;
  private mainLexer _lexer;

  public CodeCompletionListener(mainParser parser, mainLexer lexer) {
    this._parser = parser;
    this._ctx = null;
    this._lexer = lexer;
  }

  @Override
  public void exitMain(mainParser.MainContext ctx) {
    try {

    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.PARSER_MAIN, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public HashSet<Integer> suggestions(EditorContext editorContext) {
    if (this._ctx == null) return null;

    HashSet<Integer> collector = new HashSet<>();
    //
    //        this.process(
    //                this._ruleNames,
    //                this._vocabulary,
    //                this._atn.states.get(0),
    //                new MyTokenStream(l);
    //                collector,
    //                new ParserStack());
    //
    //                this._atn.getExpectedTokens();

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
    this.process(
        ruleNames, vocabulary, state, tokens, collector, parserStack, new HashSet<>(), history);
  }

  private void process(
      List<String> ruleNames,
      Vocabulary vocabulary,
      ATNState atnState,
      MyTokenStream tokens,
      Set<Integer> collector,
      ParserStack parserStack,
      Set<Integer> alreadyPassed,
      List<String> history) {
    String ruleName = ruleNames.get(atnState.ruleIndex);
    boolean atCaret = tokens.atCaret();
    ParserStack.Result stackRes = parserStack.process(atnState);

    if (!stackRes.hasError()) {
      return;
    }

    for (Transition transition : atnState.getTransitions()) {
      String transitionRule = ruleNames.get(transition.target.ruleIndex);

      if (transition.isEpsilon()) {
        alreadyPassed.add(transition.target.stateNumber);
        process(
            ruleNames,
            vocabulary,
            transition.target,
            tokens,
            collector,
            stackRes.getStack(),
            alreadyPassed,
            history);
      } else if (transition instanceof AtomTransition) {
        Token nextTokenType = tokens.next();

        // Then we reached the caret
        if (atCaret) {
          if (this.isCompatibleWithStack(transition.target, parserStack)) {
            String displayname = vocabulary.getDisplayName(((AtomTransition) transition).label);
            if (history.stream()
                .noneMatch(name -> name.equals(transitionRule + " added " + displayname))) {
              history.add(transitionRule + " added " + displayname);
            }

            collector.add((((AtomTransition) transition).label));
          }
        } else {
          if (nextTokenType.getType() == ((AtomTransition) transition).label) {
            MyTokenStream tmpTokenStream = tokens.move();
            if (tmpTokenStream.atCaret())
              System.out.println(vocabulary.getDisplayName(((AtomTransition) transition).label));

            process(
                ruleNames,
                vocabulary,
                transition.target,
                tmpTokenStream,
                collector,
                stackRes.getStack(),
                new HashSet<>(),
                history);
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
                      String displayname = vocabulary.getDisplayName(sym);
                      if (history.stream()
                          .noneMatch(
                              name -> name.equals(transitionRule + " added " + displayname))) {
                        history.add(transitionRule + " added " + displayname);
                      }

                      collector.add(sym);
                    }
                  } else {
                    //                        System.out.println(ruleName);
                    String displayname = vocabulary.getDisplayName(nextTokenType.getType());
                    if (nextTokenType.getType() == sym) {

                      MyTokenStream tmpTokenStream = tokens.move();
                      if (tmpTokenStream.atCaret())
                        System.out.println(vocabulary.getDisplayName(sym));

                      process(
                          ruleNames,
                          vocabulary,
                          transition.target,
                          tmpTokenStream,
                          collector,
                          stackRes.getStack(),
                          new HashSet<>(),
                          history);
                    }
                  }
                });
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
          .anyMatch(transition -> isCompatibleWithStack(transition.target, parserStack));
    } else {
      return true;
    }
  }
}
