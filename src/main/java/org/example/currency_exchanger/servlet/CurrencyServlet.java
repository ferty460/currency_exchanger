package org.example.currency_exchanger.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.exception.DuplicateException;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.exception.ValidationException;
import org.example.currency_exchanger.service.CurrencyService;
import org.example.currency_exchanger.service.CurrencyServiceImpl;
import org.example.currency_exchanger.util.WebUtil;
import org.example.currency_exchanger.validation.PathValidator;
import org.example.currency_exchanger.validation.Validator;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/currencies", "/currency/*"})
public class CurrencyServlet extends HttpServlet {

    private static final String CODE_PARAM = "code";
    private static final String NAME_PARAM = "name";
    private static final String SIGN_PARAM = "sign";

    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
    private final Validator<String> pathValidator = new PathValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();

        try {
            if ("/currencies".equals(servletPath)) {
                List<CurrencyDto> currencies = currencyService.getAll();

                WebUtil.sendResponse(resp, currencies, HttpServletResponse.SC_OK);
            } else if (servletPath.startsWith("/currency")) {
                pathValidator.validate(pathInfo);

                String code = pathInfo.substring(1).toUpperCase();
                CurrencyDto currency = currencyService.getByCode(code);

                WebUtil.sendResponse(resp, currency, HttpServletResponse.SC_OK);
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
            CurrencyDto currency = new CurrencyDto(
                    0L,
                    req.getParameter(CODE_PARAM),
                    req.getParameter(NAME_PARAM),
                    req.getParameter(SIGN_PARAM)
            );
            CurrencyDto savedCurrency = currencyService.save(currency);

            WebUtil.sendResponse(resp, savedCurrency, HttpServletResponse.SC_CREATED);
        } catch (ValidationException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (DuplicateException e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            WebUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
