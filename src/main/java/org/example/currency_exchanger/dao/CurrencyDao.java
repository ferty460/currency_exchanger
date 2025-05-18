package org.example.currency_exchanger.dao;

import org.example.currency_exchanger.entity.Currency;

import java.util.Optional;

public interface CurrencyDao extends CrudDao<Currency> {

    Optional<Currency> findByCode(String code);

}
