package org.example.currency_exchanger.validation;

import org.example.currency_exchanger.dto.ExchangeRateRequest;
import org.example.currency_exchanger.exception.ValidationException;

public class ExchangeRateValidator implements Validator<ExchangeRateRequest> {

    @Override
    public void validate(ExchangeRateRequest req) throws ValidationException {
        String base = req.base();
        String target = req.target();
        String rateStr = req.rate();

        if (base == null || base.isBlank()) {
            throw new ValidationException("Missing required field: baseCurrencyCode");
        }
        if (target == null || target.isBlank()) {
            throw new ValidationException("Missing required field: targetCurrencyCode");
        }
        if (rateStr == null || rateStr.isBlank()) {
            throw new ValidationException("Missing required field: rate");
        }

        try {
            if (Double.parseDouble(rateStr) <= 0.0) {
                throw new ValidationException("Rate must be greater than 0");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Field 'rate' must be a number");
        }

        if (base.equals(target)) {
            throw new ValidationException("Codes are the same: %s and %s".formatted(base, target));
        }
    }

}
