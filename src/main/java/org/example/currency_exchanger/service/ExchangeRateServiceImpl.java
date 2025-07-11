package org.example.currency_exchanger.service;

import org.example.currency_exchanger.context.ApplicationContext;
import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.entity.ExchangeRate;
import org.example.currency_exchanger.exception.DuplicateException;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.mapper.ExchangeRateMapper;

import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateDao exchangeRateDao = ApplicationContext.getContext().get(ExchangeRateDao.class);
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    @Override
    public List<ExchangeRateDto> getAll() {
        return exchangeRateDao.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ExchangeRateDto getByBaseCodeAndTargetCode(String baseCode, String targetCode) {
        ExchangeRate exchangeRate = exchangeRateDao.findByBaseCodeAndTargetCode(baseCode, targetCode).orElseThrow(
                () -> new NotFoundException(("Exchange Rate with base code %s and target code %s is not found")
                        .formatted(baseCode, targetCode)
                )
        );

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
