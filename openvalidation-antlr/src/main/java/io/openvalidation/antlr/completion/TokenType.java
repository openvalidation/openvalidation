package io.openvalidation.antlr.completion;

public interface TokenType {
  int getType();

  String toString();

  boolean equals(TokenType token);

  int hashCode();
}
