package org.example.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.dto.ExchangeRateDto;
import org.example.currency_exchanger.service.CurrencyService;
import org.example.currency_exchanger.service.CurrencyServiceImpl;
import org.example.currency_exchanger.service.ExchangeRateService;
import org.example.currency_exchanger.service.ExchangeRateServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/exchangeRates", "/exchangeRate/*"})
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();
    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();

        if ("/exchangeRates".equals(servletPath)) {
            List<ExchangeRateDto> exchangeRates = exchangeRateService.getAll();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);

            mapper.writeValue(resp.getWriter(), exchangeRates);
        } else if (servletPath.startsWith("/exchangeRate")) {
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Currency code is missing");
                return;
            }

            String codes = pathInfo.substring(1);
            String baseCode = codes.substring(0, 3);
            String targetCode = codes.substring(3);

            ExchangeRateDto exchangeRate = exchangeRateService.getByBaseCodeAndTargetCode(baseCode, targetCode);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);

            mapper.writeValue(resp.getWriter(), exchangeRate);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/exchangeRates".equals(servletPath)) {
            String baseCurrency = req.getParameter("baseCurrency");
            String targetCurrency = req.getParameter("targetCurrency");
            Double rate = Double.valueOf(req.getParameter("rate"));

            CurrencyDto baseCurrencyDto = currencyService.getByCode(baseCurrency);
            CurrencyDto targetCurrencyDto = currencyService.getByCode(targetCurrency);

            ExchangeRateDto exchangeRateDto = new ExchangeRateDto(
                    0L, baseCurrencyDto, targetCurrencyDto, rate
            );

            ExchangeRateDto savedExchangeRate = exchangeRateService.save(exchangeRateDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);

            mapper.writeValue(resp.getWriter(), savedExchangeRate);
        }
    }

}
