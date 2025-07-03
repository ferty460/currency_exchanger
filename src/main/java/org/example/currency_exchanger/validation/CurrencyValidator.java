package org.example.currency_exchanger.validation;

import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.exception.ValidationException;

public class CurrencyValidator implements Validator<CurrencyDto> {

    private static final String CURRENCY_CODE_REGEX = "^[A-Za-z]{3}$";
    private static final int MAX_NAME_LENGTH = 32;
    private static final int MAX_SIGN_LENGTH = 8;

    @Override
    public void validate(CurrencyDto currency) throws ValidationException {
        String code = currency.code();
        String name = currency.name();
        String sign = currency.sign();

        if (code == null || code.isBlank()) {
            throw new ValidationException("Missing required field: code");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Missing required field: name");
        }
        if (sign == null || sign.isBlank()) {
            throw new ValidationException("Missing required field: sign");
        }
        if (!code.trim().matches(CURRENCY_CODE_REGEX)) {
            throw new ValidationException(
                    ("Invalid currency code: %s. Currency code must be 3 uppercase letters (ISO 4217 format).")
                    .formatted(code));
        }
        if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Name is too long. Max length is " + MAX_NAME_LENGTH);
        }
        if (sign.trim().length() > MAX_SIGN_LENGTH) {
            throw new ValidationException("Sign is too long. Max length is " + MAX_SIGN_LENGTH);
        }
    }

}
