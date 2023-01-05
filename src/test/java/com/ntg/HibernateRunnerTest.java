package com.ntg;

import com.ntg.entity.Chat;
import com.ntg.entity.Company;
import com.ntg.entity.LocaleInfo;
import com.ntg.entity.PersonalInfo;
import com.ntg.entity.Profile;
import com.ntg.entity.Role;
import com.ntg.entity.User;
import com.ntg.entity.UserChat;
import com.ntg.util.HibernateUtil;
import com.ntg.util.HibernateUtilTest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    void createH2Database(){
        @Cleanup SessionFactory sessionFactory = HibernateUtilTest.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Company company = Company.builder()
                .name("Google")
                .build();


        session.persist(company);
        transaction.commit();

    }

    @Test
    void localeTest(){
        @Cleanup SessionFactory sessionFactory =  HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Company company = session.get(Company.class, 2);
        company.getLocales().clear();
        company.getLocales().add(LocaleInfo.of("ru", "russian description"));
        company.getLocales().add(LocaleInfo.of("en", "english description"));
        transaction.commit();
    }

    @Test
    void CheckManyToMany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                Transaction transaction = session1.beginTransaction();

                UserChat userChat = session1.get(UserChat.class, 7L);
                session1.remove(userChat);
                transaction.commit();
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user = session.get(User.class, 2L);
                Chat chat = session.get(Chat.class, 1L);


                UserChat userChat = UserChat.builder()
                        .chat(chat)
                        .user(user)
                        .createdAt(Instant.now())
                        .addedBy("user1.getUserName()")
                        .build();

                session.persist(userChat);

                session.getTransaction().commit();

            }
            System.out.println();
        }
    }
    @Test
    void CheckOneToOne() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user1 = session.get(User.class, 7L);
                System.out.println(user1);

                Company company = Company.builder()
                        .name("Google")
                        .build();

                User user = User.builder()
                        .userName("ngulamidinov45@gmail.com")
                        .age(20)
                        .personalInfo(
                                PersonalInfo.builder()
                                        .firstName("Nurbolot %d")
                                        .lastName("Gulamidinov %d")
                                        .birthDate(LocalDate.of(2002, 11, 5))
                                        .build()
                        )
                        .role(Role.USER)
                        .company(company)
                        .build();


                Profile profile = Profile.builder()
                        .language("ru")
                        .street("Street 12")
                        .build();
                profile.setUser(user);

                company.addUser(user);


                session.persist(company);

                System.out.println();

                session.getTransaction().commit();

            }
        }
    }

    @Test
    void deleteUserOrphanRemoval() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Company company = session.getReference(Company.class, 1);
//            company.getUsers().removeIf(user -> user.getId().equals(1L));
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