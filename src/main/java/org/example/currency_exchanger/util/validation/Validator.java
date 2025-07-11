package org.example.currency_exchanger.util.validation;

import org.example.currency_exchanger.exception.ValidationException;

public interface Validator<T> {

    void validate(T object) throws ValidationException;

}
