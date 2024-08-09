package dev.orisha.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CarSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("make", table, columnPrefix + "_make"));
        columns.add(Column.aliased("model", table, columnPrefix + "_model"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));

        return columns;
    }
}
