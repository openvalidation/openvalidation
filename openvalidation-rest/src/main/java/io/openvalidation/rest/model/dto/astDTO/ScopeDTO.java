package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.exceptions.OpenValidationException;
import java.util.List;

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
