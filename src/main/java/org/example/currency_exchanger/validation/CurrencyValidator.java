package org.example.currency_exchanger.validation;

import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.exception.ValidationException;

public class CurrencyValidator implements Validator<CurrencyDto> {

    private static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";

    @Override
    public void validate(CurrencyDto currency) {
        if (currency.code() == null || currency.code().isBlank()) {
            throw new ValidationException("Missing required field: code");
        }
        if (currency.name() == null || currency.name().isBlank()) {
            throw new ValidationException("Missing required field: name");
        }
        if (currency.sign() == null || currency.sign().isBlank()) {
            throw new ValidationException("Missing required field: sign");
        }
        if (!currency.name().matches(CURRENCY_CODE_REGEX)) {
            throw new ValidationException("Invalid currency code: " + currency.code());
        }
    }

}
