package org.example.currency_exchanger.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.dto.ExchangeRequest;
import org.example.currency_exchanger.service.ExchangeService;
import org.example.currency_exchanger.service.ExchangeServiceImpl;
import org.example.currency_exchanger.util.WebUtil;
import org.example.currency_exchanger.util.validation.ExchangeValidator;
import org.example.currency_exchanger.util.validation.Validator;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private static final String BASE_CURRENCY_PARAM = "from";
    private static final String TARGET_CURRENCY_PARAM = "to";
    private static final String AMOUNT_PARAM = "amount";

    private final ExchangeService exchangeService = ExchangeServiceImpl.getInstance();
    private final Validator<ExchangeRequest> exchangeRequestValidator = new ExchangeValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String base = req.getParameter(BASE_CURRENCY_PARAM);
        String target = req.getParameter(TARGET_CURRENCY_PARAM);
        String amount = req.getParameter(AMOUNT_PARAM);

        ExchangeRequest exchangeRequest = new ExchangeRequest(base, target, amount);
        exchangeRequestValidator.validate(exchangeRequest);

        ExchangeDto exchangeDto = exchangeService.exchange(base, target, amount);

        WebUtil.sendResponse(resp, exchangeDto, HttpServletResponse.SC_OK);
    }

}
