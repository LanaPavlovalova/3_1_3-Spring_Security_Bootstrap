package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.entity.Role;
import java.util.List;

public interface RoleRepository {
    List<Role> findAll();
    Role findById(Long id);
    Role findByName(String name);
    void save(Role role);
}