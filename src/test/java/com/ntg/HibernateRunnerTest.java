package com.ntg;

import com.ntg.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HibernateRunnerTest {
    @Test
    void checkReflectionApi() throws SQLException {
        User user;
        user = User.builder()
                .userName("ngulamidinov@gmail.com")
                .age(20)
                .firstName("Nurbolot")
                .lastName("Gulamidinov")
                .birthDate(LocalDate.of(2002, 11, 5))
                .build();

        String sql = """
                insert
                into
                %s
                (%s)
                values
                (%s)
                                
                """;

        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name())
                .orElse(user.getClass().getName());
        Field[] declaredFields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName())
                )
                .collect(Collectors.joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        String formatted = sql.formatted(tableName, columnNames, columnValues);

        System.out.println(formatted);

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(formatted);

    }

}