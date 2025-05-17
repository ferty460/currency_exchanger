package org.example.currency_exchanger.util.template;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<T> {

    T extract(ResultSet rs) throws SQLException;

}
