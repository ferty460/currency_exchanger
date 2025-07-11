package org.example.currency_exchanger.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.context.ApplicationContext;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.service.CurrencyService;
import org.example.currency_exchanger.util.WebUtil;
import org.example.currency_exchanger.util.validation.PathValidator;
import org.example.currency_exchanger.util.validation.Validator;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/currencies", "/currency/*"})
public class CurrencyServlet extends HttpServlet {

    private static final String CODE_PARAM = "code";
    private static final String NAME_PARAM = "name";
    private static final String SIGN_PARAM = "sign";

    private final CurrencyService currencyService = ApplicationContext.getContext().get(CurrencyService.class);
    private final Validator<String> pathValidator = new PathValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/currencies".equals(servletPath)) {
            handleGetAll(resp);
        } else if (servletPath.startsWith("/currency")) {
            handleGetByCode(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CurrencyDto currency = new CurrencyDto(
                0L,
                req.getParameter(CODE_PARAM),
                req.getParameter(NAME_PARAM),
                req.getParameter(SIGN_PARAM)
        );
        CurrencyDto savedCurrency = currencyService.save(currency);

        WebUtil.sendResponse(resp, savedCurrency, HttpServletResponse.SC_CREATED);
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException {
        List<CurrencyDto> currencies = currencyService.getAll();
        WebUtil.sendResponse(resp, currencies, HttpServletResponse.SC_OK);
    }

    private void handleGetByCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        pathValidator.validate(pathInfo);

        String code = pathInfo.substring(1).toUpperCase();
        CurrencyDto currency = currencyService.getByCode(code);

        WebUtil.sendResponse(resp, currency, HttpServletResponse.SC_OK);
    }

}
