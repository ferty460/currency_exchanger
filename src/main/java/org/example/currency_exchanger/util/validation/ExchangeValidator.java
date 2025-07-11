package org.example.currency_exchanger.util.validation;

import org.example.currency_exchanger.dto.ExchangeRequest;
import org.example.currency_exchanger.exception.ValidationException;

public class ExchangeValidator implements Validator<ExchangeRequest> {

    private static final double MIN_AMOUNT = 0.0;
    private static final double MAX_AMOUNT = 1_000_000.0;

    @Override
    public void validate(ExchangeRequest req) throws ValidationException {
        String from = req.base();
        String to = req.target();
        String amountStr = req.amount();

        nullValidate(from, to, amountStr);
        amountValidate(amountStr);
    }

    private static void nullValidate(String from, String to, String amountStr) {
        if (from == null || from.isBlank()) {
            throw new ValidationException("Missing required parameter: from");
        }
        if (to == null || to.isBlank()) {
            throw new ValidationException("Missing required parameter: to");
        }
        if (amountStr == null || amountStr.isBlank()) {
            throw new ValidationException("Missing required parameter: amount");
        }
    }

    private static void amountValidate(String amountStr) {
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
