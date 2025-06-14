package org.example.currency_exchanger.validation;

import org.example.currency_exchanger.dto.ExchangeRequest;
import org.example.currency_exchanger.exception.ValidationException;

public class ExchangeValidator implements Validator<ExchangeRequest> {

    @Override
    public void validate(ExchangeRequest req) throws ValidationException {
        String from = req.base();
        String to = req.target();
        String amount = req.amount();

        if (from == null || from.isBlank()) {
            throw new ValidationException("Missing required parameter: from");
        }
        if (to == null || to.isBlank()) {
            throw new ValidationException("Missing required parameter: to");
        }
        if (amount == null || amount.isBlank()) {
            throw new ValidationException("Missing required parameter: amount");
        }

        try {
            if (Integer.parseInt(amount) < 0) {
                throw new ValidationException("Amount must be greater than 0");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Field 'amount' must be a number");
        }
    }

}
