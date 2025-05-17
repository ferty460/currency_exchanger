package org.example.currency_exchanger.util.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementSetter {

    void set(PreparedStatement stmt) throws SQLException;

}
