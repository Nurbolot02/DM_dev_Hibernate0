package com.ntg;

import com.ntg.entity.Company;
import com.ntg.entity.PersonalInfo;
import com.ntg.entity.Profile;
import com.ntg.entity.Role;
import com.ntg.entity.User;
import com.ntg.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.HashSet;

@Slf4j
public class HibernateRunner {
    public static void main(String[] args) {


//        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//            try (Session session = sessionFactory.openSession()) {
//
//                User user = User.builder()
//                        .userName("ngulamidinov@gmail.com")
//                        .age(20)
//                        .personalInfo(
//                                PersonalInfo.builder()
//                                        .firstName("Nurbolot")
//                                        .lastName("Gulamidinov")
//                                        .birthDate(LocalDate.of(2002, 11, 5))
//                                        .build()
//                        )
//                        .build();
//
//                Profile profile = Profile.builder()
//                        .language("ru")
//                        .street("Street 12")
//                        .build();
//                profile.setUser(user);
//
//                session.persist(user);
//
//                System.out.println();
//
//            }
//        }


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


        User user2 = User.builder()
                .userName("ngulamidinov@gmail.com")
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

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()
        ) {
//            try (Session session1 = sessionFactory.openSession()) {
//                session1.beginTransaction();
//                session1.remove(company);
//                session1.getTransaction().commit();
//            }

            User user3;

            Session session = sessionFactory.openSession();
            try (session) {

                 user3 = session.get(User.class, 2L);


                session.persist(user3);



                Transaction transaction = session.beginTransaction();

//                company.addUser(user);
//                company.addUser(user2);
//                User user1 = session.get(User.class, 3L);
//                System.out.println(user1);
//                session.persist(company);
//                session.getTransaction().commit();
            }


            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();
                session1.merge(user3);

                session1.getTransaction().commit();

            }
        }
        System.out.println();
    }
}
