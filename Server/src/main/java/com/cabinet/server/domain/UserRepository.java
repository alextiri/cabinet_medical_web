package com.cabinet.server.domain;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findByUsername(String username);
    User save(User user);
    User findById(Integer id);

    void deleteUser(Integer id);
}