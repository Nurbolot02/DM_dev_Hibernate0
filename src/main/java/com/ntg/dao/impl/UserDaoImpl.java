package com.ntg.dao.impl;

import com.ntg.dao.abstracts.UserDao;
import com.ntg.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;
@AllArgsConstructor
public class UserDaoImpl implements UserDao {
    private final SessionFactory sessionFactory;
    @Override
    public Optional<User> findUserById(Long id) {
        Session session = sessionFactory.openSession();
        EntityManager entityManager = sessionFactory.createEntityManager();

        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
