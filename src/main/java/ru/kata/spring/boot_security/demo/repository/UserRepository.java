package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserRepository {
    User findByUsername(String username);
    User findById(Long id);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(Long id);
}