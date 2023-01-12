package com.ntg.dao.abstracts;

import com.ntg.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findUserById(Long id);
    void deleteUserById(Long id);
}
