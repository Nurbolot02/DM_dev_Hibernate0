package com.ntg;

import com.ntg.entity.PersonalInfo;
import com.ntg.entity.Role;
import com.ntg.entity.User;
import com.ntg.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {
    public static void main(String[] args) {

        User user = User.builder()
                .userName("ngulamidinov4500fdff0@gmail.com")
                .age(20)
                .personalInfo(
                        PersonalInfo.builder()
                                .firstName("Nurbolot %d")
                                .lastName("Gulamidinov %d")
                                .birthDate(LocalDate.of(2002, 11, 5))
                                .build()
                )
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .userName("ngulamidinov4500ffddf0@gmail.com")
                .age(20)
                .personalInfo(
                        PersonalInfo.builder()
                                .firstName("Nurbolot %d")
                                .lastName("Gulamidinov %d")
                                .birthDate(LocalDate.of(2002, 11, 5))
                                .build()
                )
                .role(Role.USER)
                .build();

        log.info("User entity is in transient state, object: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        ) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();
                log.trace("Transaction is created, {}", transaction);

                session.remove(user);
                session.remove(user2);
                log.trace("User is in removed state: {}, session: {}", user, session);
                log.trace("User is in removed state: {}, session: {}", user2, session);
                session.persist(user);
                session.persist(user2);
                log.trace("User is in persistent state: {}, session: {}", user, session);

                session.getTransaction().commit();
            }
            log.warn("User is in detached state: {}, session is closed: {}", user, session);
        } catch (Exception e) {
            log.error("Exception occurred ", e);
            throw e;
        }
    }
}
