package org.example.currency_exchanger;

import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.dao.ExchangeRateDaoImpl;
import org.example.currency_exchanger.entity.ExchangeRate;

import java.util.Optional;

public class ApplicationRunner {

    public static void main(String[] args) {
        ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.getInstance();

        Optional<ExchangeRate> exchangeRate = exchangeRateDao.findByBaseCodeAndTargetCode("USD", "EUR");

        exchangeRate.ifPresent(System.out::println);
    }

}
