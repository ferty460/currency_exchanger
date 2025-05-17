package org.example.currency_exchanger;

import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.CrudDao;
import org.example.currency_exchanger.entity.Currency;

public class ApplicationRunner {

    public static void main(String[] args) {
        CrudDao<Currency> currencyDao = CurrencyDao.getInstance();

        Currency currency = new Currency();
        currency.setCode("USD1");
        currency.setFullName("dollar");
        currency.setSign("-");

        currencyDao.save(currency);

//        currencyDao.delete(13L);
    }

}
