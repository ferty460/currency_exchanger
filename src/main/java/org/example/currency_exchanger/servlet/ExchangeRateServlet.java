package org.example.currency_exchanger.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.dto.ExchangeRateRequest;
import org.example.currency_exchanger.exception.DuplicateException;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.exception.ValidationException;
import org.example.currency_exchanger.service.CurrencyService;
import org.example.currency_exchanger.service.CurrencyServiceImpl;
import org.example.currency_exchanger.service.ExchangeRateService;
import org.example.currency_exchanger.service.ExchangeRateServiceImpl;
import org.example.currency_exchanger.util.WebUtil;
import org.example.currency_exchanger.validation.ExchangeRateValidator;
import org.example.currency_exchanger.validation.PathValidator;
import org.example.currency_exchanger.validation.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/exchangeRates", "/exchangeRate/*"})
public class ExchangeRateServlet extends HttpServlet {

    private static final int CODES_INDEX = 1;
    private static final int CODE_LENGTH = 3;
    private static final String BASE_CODE_PARAM = "baseCurrencyCode";
    private static final String TARGET_CODE_PARAM = "targetCurrencyCode";
    private static final String RATE_PARAM = "rate";

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();
    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();

    private final Validator<String> pathValidator = new PathValidator();
    private final Validator<ExchangeRateRequest> exchangeRateValidator = new ExchangeRateValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();

        try {
            if ("/exchangeRates".equals(servletPath)) {
                List<ExchangeRateDto> exchangeRates = exchangeRateService.getAll();

                WebUtil.sendResponse(resp, exchangeRates, HttpServletResponse.SC_OK);
            } else if (servletPath.startsWith("/exchangeRate")) {
                pathValidator.validate(pathInfo);

                String codes = pathInfo.substring(CODES_INDEX);
                String baseCode = codes.substring(0, CODE_LENGTH);
                String targetCode = codes.substring(CODE_LENGTH);

                ExchangeRateDto exchangeRate = exchangeRateService.getByBaseCodeAndTargetCode(baseCode, targetCode);

                WebUtil.sendResponse(resp, exchangeRate, HttpServletResponse.SC_OK);
            }
        } catch (ValidationException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(
                    req.getParameter(BASE_CODE_PARAM),
                    req.getParameter(TARGET_CODE_PARAM),
                    req.getParameter(RATE_PARAM)
            );
            exchangeRateValidator.validate(exchangeRateRequest);

            CurrencyDto base = currencyService.getByCode(exchangeRateRequest.base());
            CurrencyDto target = currencyService.getByCode(exchangeRateRequest.target());
            BigDecimal rate = new BigDecimal(exchangeRateRequest.rate());

            ExchangeRateDto exchangeRateDto = new ExchangeRateDto(0L, base, target, rate);
            ExchangeRateDto savedExchangeRate = exchangeRateService.save(exchangeRateDto);

            WebUtil.sendResponse(resp, savedExchangeRate, HttpServletResponse.SC_CREATED);
        } catch (ValidationException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DuplicateException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            pathValidator.validate(pathInfo);

            Map<String, String> params = WebUtil.getRequestParameters(req);
            String codes = pathInfo.substring(CODES_INDEX);

            String base = codes.substring(0, CODE_LENGTH);
            String target = codes.substring(CODE_LENGTH);
            String rate = params.get(RATE_PARAM);

            ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(base, target, rate);
            exchangeRateValidator.validate(exchangeRateRequest);

            ExchangeRateDto exchangeRate = exchangeRateService.getByBaseCodeAndTargetCode(base, target);
            ExchangeRateDto updatedRate = new ExchangeRateDto(
                    exchangeRate.id(),
                    exchangeRate.baseCurrency(),
                    exchangeRate.targetCurrency(),
                    new BigDecimal(rate)
            );
            exchangeRateService.update(updatedRate);

            WebUtil.sendResponse(resp, updatedRate, HttpServletResponse.SC_OK);
        } catch (ValidationException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
