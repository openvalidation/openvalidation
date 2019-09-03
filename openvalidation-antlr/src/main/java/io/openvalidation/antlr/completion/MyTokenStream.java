package io.openvalidation.antlr.completion;

import java.util.List;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;

public class MyTokenStream {

  private final List<Token> tokens;
  private final int start;

  public MyTokenStream(List<Token> tokens) {
    this(tokens, 0);
  }

  public MyTokenStream(List<Token> tokens, int start) {
    this.tokens = tokens;
    this.start = start;
  }

  public Token next() {
    return start >= tokens.size() ? new CommonToken(-1) : tokens.get(start);
  }

  public boolean atCaret() {
    if (next().getType() < 0) {
      return true;
    } else {
      return false;
    }
  }

  public MyTokenStream move() {
    return new MyTokenStream(tokens, start + 1);
  }
}
