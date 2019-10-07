package io.openvalidation.rest.model.dto.astDTO;

public class ScopeDTO {
  private GenericNode scope;

  public ScopeDTO(GenericNode scope) {
    this.scope = scope;
  }

  public GenericNode getScope() {
    return scope;
  }

  public void setScope(GenericNode scope) {
    this.scope = scope;
  }
}
