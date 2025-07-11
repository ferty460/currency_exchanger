package org.example.currency_exchanger.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.context.ApplicationContext;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.dto.ExchangeRateRequest;
import org.example.currency_exchanger.service.CurrencyService;
import org.example.currency_exchanger.service.ExchangeRateService;
import org.example.currency_exchanger.util.WebUtil;
import org.example.currency_exchanger.util.validation.ExchangeRateValidator;
import org.example.currency_exchanger.util.validation.PathValidator;
import org.example.currency_exchanger.util.validation.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {"/exchangeRates", "/exchangeRate/*"})
public class ExchangeRateServlet extends HttpServlet {

    private static final int CODES_INDEX = 1;
    private static final int CODE_LENGTH = 3;
    private static final String BASE_CODE_PARAM = "baseCurrencyCode";
    private static final String TARGET_CODE_PARAM = "targetCurrencyCode";
    private static final String RATE_PARAM = "rate";

    private final ApplicationContext context = ApplicationContext.getContext();
    private final ExchangeRateService exchangeRateService = context.get(ExchangeRateService.class);
    private final CurrencyService currencyService = context.get(CurrencyService.class);
    private final Validator<String> pathValidator = new PathValidator();
    private final Validator<ExchangeRateRequest> exchangeRateValidator = new ExchangeRateValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/exchangeRates".equals(servletPath)) {
            handleGetAll(resp);
        } else if (servletPath.startsWith("/exchangeRate")) {
            handleGetByCodes(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExchangeRateRequest request = extractExchangeRateRequest(req);
        exchangeRateValidator.validate(request);

        ExchangeRateDto exchangeRateDto = buildExchangeRateDto(request);
        ExchangeRateDto savedExchangeRate = exchangeRateService.save(exchangeRateDto);

        WebUtil.sendResponse(resp, savedExchangeRate, HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        pathValidator.validate(pathInfo);

        String base = pathInfo.substring(CODES_INDEX, CODES_INDEX + CODE_LENGTH);
        String target = pathInfo.substring(CODES_INDEX + CODE_LENGTH);
        String rate = WebUtil.getRequestParameters(req).get(RATE_PARAM);

        ExchangeRateRequest request = new ExchangeRateRequest(base, target, rate);
        exchangeRateValidator.validate(request);

        ExchangeRateDto existingRate = exchangeRateService.getByBaseCodeAndTargetCode(base, target);
        ExchangeRateDto updatedRate = updateExchangeRateDto(existingRate, rate);

        exchangeRateService.update(updatedRate);
        WebUtil.sendResponse(resp, updatedRate, HttpServletResponse.SC_OK);
    }


    private void handleGetAll(HttpServletResponse resp) throws IOException {
        List<ExchangeRateDto> exchangeRates = exchangeRateService.getAll();
        WebUtil.sendResponse(resp, exchangeRates, HttpServletResponse.SC_OK);
    }

    private void handleGetByCodes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        pathValidator.validate(pathInfo);

        String codes = pathInfo.substring(CODES_INDEX);
        String baseCode = codes.substring(0, CODE_LENGTH);
        String targetCode = codes.substring(CODE_LENGTH);

        ExchangeRateDto exchangeRate = exchangeRateService.getByBaseCodeAndTargetCode(baseCode, targetCode);
        WebUtil.sendResponse(resp, exchangeRate, HttpServletResponse.SC_OK);
    }

    private ExchangeRateRequest extractExchangeRateRequest(HttpServletRequest req) {
        return new ExchangeRateRequest(
                req.getParameter(BASE_CODE_PARAM),
                req.getParameter(TARGET_CODE_PARAM),
                req.getParameter(RATE_PARAM)
        );
    }

    private ExchangeRateDto buildExchangeRateDto(ExchangeRateRequest request) {
        CurrencyDto base = currencyService.getByCode(request.base());
        CurrencyDto target = currencyService.getByCode(request.target());
        BigDecimal rate = new BigDecimal(request.rate());

        return new ExchangeRateDto(0L, base, target, rate);
    }

    private ExchangeRateDto updateExchangeRateDto(ExchangeRateDto existingRate, String newRate) {
        return new ExchangeRateDto(
                existingRate.id(),
                existingRate.baseCurrency(),
                existingRate.targetCurrency(),
                new BigDecimal(newRate)
        );
    }

}
