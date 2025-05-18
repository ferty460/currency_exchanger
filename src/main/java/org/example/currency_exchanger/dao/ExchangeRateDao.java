package org.example.currency_exchanger.dao;

import org.example.currency_exchanger.entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDao extends CrudDao<ExchangeRate> {

    Optional<ExchangeRate> findByBaseCodeAndTargetCode(String baseCode, String targetCode);

}
