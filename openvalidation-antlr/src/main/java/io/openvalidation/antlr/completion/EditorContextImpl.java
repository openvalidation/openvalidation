package io.openvalidation.antlr.completion;

import io.openvalidation.antlr.generated.mainLexer;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;

public class EditorContextImpl implements EditorContext {
  private String _code;
  private CharStream _stream;

  public EditorContextImpl(String code) {
    this._code = code;
    this._stream = CharStreams.fromString(code.trim() + " ");
  }

  public static int CARET_TOKEN_TYPE = -10;

  @Override
  public List<Token> preceedingTokens() {
    mainLexer lexer = new mainLexer(_stream);
    List<Token> returnList = new ArrayList<>();

    Token next = null;

    do {
      next = lexer.nextToken();
      if (next.getChannel() == 0) {
        if (next.getType() < 0) {
          next = new CommonToken(CARET_TOKEN_TYPE);
        }
        returnList.add(next);
      }
    } while (next.getType() >= 0);

    return returnList;
  }
}
