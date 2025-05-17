package org.example.currency_exchanger.util;

import org.example.currency_exchanger.exception.DatabaseAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class DatabaseConnectionPool {

    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 10;

    private static BlockingQueue<Connection> pool;

    static {
        String url = PropertiesUtil.getProperty(URL_KEY);
        String poolSize = PropertiesUtil.getProperty(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);

        pool = new ArrayBlockingQueue<>(size);

        initConnectionPool(size, url);
    }

    private DatabaseConnectionPool() {
    }

    public static Connection getConnection() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    public static void returnConnection(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
        }
    }

    private static void initConnectionPool(int poolSize, String url) {
        for (int i = 0; i < poolSize; i++) {
            Connection realConn;
            try {
                realConn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                throw new DatabaseAccessException(e.getMessage());
            }
            pool.offer(realConn);
        }
    }

}

