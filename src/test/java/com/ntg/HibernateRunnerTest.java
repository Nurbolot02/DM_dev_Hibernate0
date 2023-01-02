package com.ntg;

import com.ntg.entity.Company;
import com.ntg.entity.PersonalInfo;
import com.ntg.entity.Role;
import com.ntg.entity.User;
import com.ntg.util.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {
    @Test
    void deleteUserOrphanRemoval(){
        SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Company company = session.getReference(Company.class, 1);
            company.getUsers().removeIf(user -> user.getId().equals(1L));
            System.out.println();
            session.getTransaction().commit();
        }
    }
    @Test
    void addNewUserToCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company facebook = Company.builder()
                .name("Facebook2")
                .users(new HashSet<>())
                .build();

        User sveta = User.builder()
                .userName("Sveta@gmail.com")
                .age(30)
                .role(Role.USER)
                .build();

        facebook.addUser(sveta);

        session.persist(facebook);


        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);

        session.getTransaction().commit();
    }

    @Test
    void checkReflectionApi() throws SQLException {
        User user;
        user = User.builder()
                .userName("ngulamidinov@gmail.com")
                .age(20)
                .personalInfo(
                        PersonalInfo.builder()
                                .firstName("Nurbolot")
                                .lastName("Gulamidinov")
                                .birthDate(LocalDate.of(2002, 11, 5))
                                .build()
                )
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