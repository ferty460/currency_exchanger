package org.example.currency_exchanger.context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.currency_exchanger.dao.CurrencyDao;
import org.example.currency_exchanger.dao.CurrencyDaoImpl;
import org.example.currency_exchanger.dao.ExchangeRateDao;
import org.example.currency_exchanger.dao.ExchangeRateDaoImpl;
import org.example.currency_exchanger.service.*;
import org.example.currency_exchanger.util.DatabaseConnectionPool;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private final ApplicationContext context = ApplicationContext.getContext();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context.register(CurrencyDao.class, new CurrencyDaoImpl());
        context.register(ExchangeRateDao.class, new ExchangeRateDaoImpl());
        context.register(CurrencyService.class, new CurrencyServiceImpl());
        context.register(ExchangeRateService.class, new ExchangeRateServiceImpl());
        context.register(ExchangeService.class, new ExchangeServiceImpl());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConnectionPool.shutdown();
    }

}
