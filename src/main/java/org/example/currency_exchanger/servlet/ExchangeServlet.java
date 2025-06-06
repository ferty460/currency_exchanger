package org.example.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.dto.ExchangeDto;
import org.example.currency_exchanger.service.ExchangeService;
import org.example.currency_exchanger.service.ExchangeServiceImpl;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeServiceImpl.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String base = req.getParameter("from");
        String target = req.getParameter("to");
        String amount = req.getParameter("amount");

        ExchangeDto exchangeDto = exchangeService.exchange(base, target, amount);

        mapper.writeValue(resp.getWriter(), exchangeDto);
    }

}
