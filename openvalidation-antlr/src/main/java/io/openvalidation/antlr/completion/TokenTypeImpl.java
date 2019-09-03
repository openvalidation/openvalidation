package io.openvalidation.antlr.completion;

public class TokenTypeImpl implements TokenType {
  private int _type;

  public TokenTypeImpl(int _type) {
    this._type = _type;
  }

  @Override
  public int getType() {
    return _type;
  }

  @Override
  public boolean equals(TokenType token) {
    return this.getType() == token.getType();
  }

  @Override
  public int hashCode() {
    return this._type;
  }
}
