package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> findAll() {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }

    @Override
    public Role findById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role findByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r WHERE r.name = :name", Role.class);
        query.setParameter("name", name);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }
}