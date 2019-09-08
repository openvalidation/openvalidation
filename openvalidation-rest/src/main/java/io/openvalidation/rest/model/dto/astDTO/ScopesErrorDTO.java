package io.openvalidation.rest.model.dto.astDTO;

import io.openvalidation.common.exceptions.OpenValidationException;

import java.util.List;

public class ScopesErrorDTO {
    private List<GenericNode> scopes;
    private List<OpenValidationException> errors;

    public ScopesErrorDTO(List<GenericNode> scopes, List<OpenValidationException> errors) {
        this.scopes = scopes;
        this.errors = errors;
    }

    public List<OpenValidationException> getErrors() {
        return errors;
    }

    public void setErrors(List<OpenValidationException> errors) {
        this.errors = errors;
    }

    public List<GenericNode> getScopes() {
        return scopes;
    }

    public void setScopes(List<GenericNode> scopes) {
        this.scopes = scopes;
    }

}
