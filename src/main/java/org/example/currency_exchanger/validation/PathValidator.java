package org.example.currency_exchanger.validation;

import org.example.currency_exchanger.exception.ValidationException;

public class PathValidator implements Validator<String> {

    @Override
    public void validate(String pathInfo) throws ValidationException {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ValidationException("Missing required parameter");
        }
    }

}
