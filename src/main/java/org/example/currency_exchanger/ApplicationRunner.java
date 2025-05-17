package org.example.currency_exchanger;

import org.example.currency_exchanger.dao.CrudDao;
import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.entity.Currency;
import org.example.currency_exchanger.entity.ExchangeRate;

import java.util.Optional;

public class ApplicationRunner {

    public static void main(String[] args) {
        CrudDao<Currency> currencyDao = CurrencyDao.getInstance();
        CrudDao<ExchangeRate> exchangeRateDao = ExchangeRateDao.getInstance();

        exchangeRateDao.delete(8L);

//        ExchangeRate exchangeRate1 = exchangeRateDao.findById(8L).orElseThrow();
//        exchangeRate1.setRate(0.3);
//
//        exchangeRateDao.update(exchangeRate1);

        Currency baseCurrency = currencyDao.findById(1L).orElseThrow();
        Currency targetCurrency = currencyDao.findById(2L).orElseThrow();

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(0.1);

        System.out.println(exchangeRateDao.save(exchangeRate));

//        Currency currency = new Currency();
//        currency.setCode("USD1");
//        currency.setFullName("dollar");
//        currency.setSign("-");
//
//        currencyDao.save(currency);

//        currencyDao.delete(13L);
    }

}
