package org.example.currency_exchanger;

import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.Dao;
import org.example.currency_exchanger.entity.Currency;

public class ApplicationRunner {

    public static void main(String[] args) {
        Dao<Currency> currencyDao = CurrencyDao.getInstance();

        currencyDao.delete(10L);
    }

}
