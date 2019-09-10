package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.exceptions.OpenValidationException;
import java.util.List;

public class ScopesErrorDTO {
  private GenericNode scope;
  private List<OpenValidationException> errors;

  public ScopesErrorDTO(GenericNode scope, List<OpenValidationException> errors) {
    this.scope = scope;
    this.errors = errors;
  }

  public List<OpenValidationException> getErrors() {
    return errors;
  }

  public void setErrors(List<OpenValidationException> errors) {
    this.errors = errors;
  }

  public GenericNode getScope() {
    return scope;
  }

  public void setScope(GenericNode scope) {
    this.scope = scope;
  }
}
