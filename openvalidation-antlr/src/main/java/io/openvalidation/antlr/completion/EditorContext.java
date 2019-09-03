package io.openvalidation.antlr.completion;

import java.util.List;
import org.antlr.v4.runtime.Token;

public interface EditorContext {
  List<Token> preceedingTokens();
}
