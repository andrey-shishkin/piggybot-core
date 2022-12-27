package ru.piggybox.common.util;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class DBUtils {

    public static Array createSqlArrayOfStrings(JdbcTemplate jdbcTemplate, List<String> tags) {

        if (tags != null && !tags.isEmpty()) {
            try {
                return Objects.requireNonNull(jdbcTemplate.getDataSource())
                        .getConnection()
                        .createArrayOf("VARCHAR", tags.toArray());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
