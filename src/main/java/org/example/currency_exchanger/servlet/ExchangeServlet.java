package org.example.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.dto.ExchangeRequest;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.exception.ValidationException;
import org.example.currency_exchanger.service.ExchangeService;
import org.example.currency_exchanger.service.ExchangeServiceImpl;
import org.example.currency_exchanger.validation.ExchangeValidator;
import org.example.currency_exchanger.validation.Validator;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeServiceImpl.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    private final Validator<ExchangeRequest> exchangeRequestValidator = new ExchangeValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!req.getServletPath().startsWith("/exchange")) {
            return;
        }

        try {
            String base = req.getParameter("from");
            String target = req.getParameter("to");
            String amount = req.getParameter("amount");

            ExchangeRequest exchangeRequest = new ExchangeRequest(base, target, amount);
            exchangeRequestValidator.validate(exchangeRequest);

            ExchangeDto exchangeDto = exchangeService.exchange(base, target, amount);

            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), exchangeDto);
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
