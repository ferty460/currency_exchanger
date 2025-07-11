package org.example.currency_exchanger.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchanger.exception.DuplicateException;
import org.example.currency_exchanger.exception.NotFoundException;
import org.example.currency_exchanger.exception.ValidationException;
import org.example.currency_exchanger.util.WebUtil;

import java.io.IOException;

@WebFilter("/*")
public class ErrorHandlerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
        try {
            chain.doFilter(req, res);
        } catch (ValidationException e) {
            WebUtil.sendError(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            WebUtil.sendError(res, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DuplicateException e) {
            WebUtil.sendError(res, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            WebUtil.sendError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
