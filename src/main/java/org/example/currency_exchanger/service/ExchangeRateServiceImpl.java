package org.example.currency_exchanger.service;

import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.dao.ExchangeRateDaoImpl;
import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.exception.DuplicateException;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.mapper.ExchangeRateMapper;

import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final ExchangeRateService INSTANCE = new ExchangeRateServiceImpl();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.getInstance();
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    private ExchangeRateServiceImpl() {
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ExchangeRateDto> getAll() {
        return exchangeRateDao.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ExchangeRateDto getByBaseCodeAndTargetCode(String baseCode, String targetCode) {
        ExchangeRate exchangeRate = exchangeRateDao.findByBaseCodeAndTargetCode(baseCode, targetCode)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Exchange Rate with base code %s and target code %s is not found", baseCode, targetCode)));

        return mapper.toDto(exchangeRate);
    }

    @Override
    public ExchangeRateDto save(ExchangeRateDto exchangeRateDto) {
        String baseCode = exchangeRateDto.baseCurrency().code();
        String targetCode = exchangeRateDto.targetCurrency().code();

        if (exchangeRateDao.findByBaseCodeAndTargetCode(baseCode, targetCode).isPresent()) {
            throw new DuplicateException("Exchange rate with such codes already exists.");
        }

        ExchangeRate exchangeRate = mapper.toEntity(exchangeRateDto);

        return mapper.toDto(exchangeRateDao.save(exchangeRate));
    }

    @Override
    public void update(ExchangeRateDto exchangeRateDto) {
        ExchangeRate exchangeRate = mapper.toEntity(exchangeRateDto);

        exchangeRateDao.update(exchangeRate);
    }

}
