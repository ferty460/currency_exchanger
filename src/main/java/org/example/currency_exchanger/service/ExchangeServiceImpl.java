package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.dto.ExchangeRateDto;

public class ExchangeServiceImpl implements ExchangeService {

    private static final ExchangeService INSTANCE = new ExchangeServiceImpl();

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();

    private ExchangeServiceImpl() {
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeDto exchange(String baseCode, String targetCode, String stringedAmount) {
        ExchangeRateDto exchangeDto = exchangeRateService.getByBaseCodeAndTargetCode(baseCode, targetCode);
        double amount = Double.parseDouble(stringedAmount);

        return new ExchangeDto(
                exchangeDto.baseCurrency(),
                exchangeDto.targetCurrency(),
                exchangeDto.rate(),
                amount,
                convertAmount(exchangeDto.rate(), amount)
        );
    }

    private double convertAmount(double rate, double amount) {
        return amount * rate;
    }

}
