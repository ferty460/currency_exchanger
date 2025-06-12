package org.example.currency_exchanger.validation;

import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.exception.ValidationException;

public class CurrencyValidator implements Validator<CurrencyDto> {

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
    }

}
