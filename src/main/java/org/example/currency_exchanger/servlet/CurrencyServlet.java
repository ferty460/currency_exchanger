package org.example.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.dto.CurrencyDto;
import org.example.currency_exchanger.service.CurrencyService;
import org.example.currency_exchanger.service.CurrencyServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/currencies", "/currency/*"})
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();

        if ("/currencies".equals(servletPath)) {
            List<CurrencyDto> currencies = currencyService.getAll();

            resp.setStatus(HttpServletResponse.SC_OK);

            mapper.writeValue(resp.getWriter(), currencies);
        } else if (servletPath.startsWith("/currency")) {
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Currency code is missing");
                return;
            }

            String code = pathInfo.substring(1);
            CurrencyDto currency = currencyService.getByCode(code);

            resp.setStatus(HttpServletResponse.SC_OK);

            mapper.writeValue(resp.getWriter(), currency);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/currencies".equals(servletPath)) {
            CurrencyDto currency = new CurrencyDto(
                    0L,
                    req.getParameter("code"),
                    req.getParameter("name"),
                    req.getParameter("sign")
            );

            if (currency.fullName() == null || currency.code() == null || currency.sign() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing currency fields");
                return;
            }

            CurrencyDto savedCurrency = currencyService.save(currency);

            resp.setStatus(HttpServletResponse.SC_OK);

            mapper.writeValue(resp.getWriter(), savedCurrency);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
