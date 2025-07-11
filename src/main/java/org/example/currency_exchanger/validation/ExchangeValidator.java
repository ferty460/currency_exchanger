package org.example.currency_exchanger.validation;

import org.example.currency_exchanger.dto.ExchangeRequest;
import org.example.currency_exchanger.exception.ValidationException;

public class ExchangeValidator implements Validator<ExchangeRequest> {

    private static final double MIN_AMOUNT = 0.0;
    private static final double MAX_AMOUNT = 1000000.0;

    @Override
    public void validate(ExchangeRequest req) throws ValidationException {
        String from = req.base();
        String to = req.target();
        String amountStr = req.amount();

        if (from == null || from.isBlank()) {
            throw new ValidationException("Missing required parameter: from");
        }
        if (to == null || to.isBlank()) {
            throw new ValidationException("Missing required parameter: to");
        }
        if (amountStr == null || amountStr.isBlank()) {
            throw new ValidationException("Missing required parameter: amount");
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= MIN_AMOUNT) {
                throw new ValidationException("Amount must be greater than 0");
            }
            if (amount >= MAX_AMOUNT) {
                throw new ValidationException("Amount must be less than " + MAX_AMOUNT);
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Field 'amount' must be a number: " + amountStr);
        }
    }

}
