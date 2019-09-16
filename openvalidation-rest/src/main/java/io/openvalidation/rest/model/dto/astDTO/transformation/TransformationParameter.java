package io.openvalidation.rest.model.dto.astDTO.transformation;

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.rest.model.dto.OpenValidationExceptionDTO;

import java.util.ArrayList;
import java.util.List;

public class TransformationParameter {
    private String culture;
    private List<OpenValidationException> remainingErrors;
    private List<OpenValidationExceptionDTO> transformedErrors;

    public TransformationParameter(String culture, List<OpenValidationException> errors) {
        this.culture = culture;
        this.remainingErrors = errors;
        this.transformedErrors = new ArrayList<>();
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String _culture) {
        this.culture = _culture;
    }

    public List<OpenValidationException> getRemainingErrors() {
        return remainingErrors;
    }

    public List<OpenValidationExceptionDTO> getTransformedErrors() {
        return transformedErrors;
    }

    public void addTransformedError(OpenValidationExceptionDTO error) {
        transformedErrors.add(error);
    }
}
