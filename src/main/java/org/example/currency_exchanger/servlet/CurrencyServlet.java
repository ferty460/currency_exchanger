package org.example.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.example.currency_exchanger.validation.PathValidator;
import org.example.currency_exchanger.validation.Validator;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/currencies", "/currency/*"})
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    private final Validator<String> pathValidator = new PathValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();

        try {
            if ("/currencies".equals(servletPath)) {
                List<CurrencyDto> currencies = currencyService.getAll();

                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), currencies);
            } else if (servletPath.startsWith("/currency")) {
                pathValidator.validate(pathInfo);

                String code = pathInfo.substring(1).toUpperCase();
                CurrencyDto currency = currencyService.getByCode(code);

                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), currency);
            }
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        try {
            if ("/currencies".equals(servletPath)) {
                CurrencyDto currency = new CurrencyDto(
                        0L,
                        req.getParameter("code"),
                        req.getParameter("name"),
                        req.getParameter("sign")
                );
                CurrencyDto savedCurrency = currencyService.save(currency);

                resp.setStatus(HttpServletResponse.SC_CREATED);
                mapper.writeValue(resp.getWriter(), savedCurrency);
            }
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (DuplicateException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
