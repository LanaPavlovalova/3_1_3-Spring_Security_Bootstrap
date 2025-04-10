package ru.kata.spring.boot_security.demo.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        try {
            logger.info("Starting database initialization...");

            initializeRoles();
            initializeUsers();

            logger.info("Database initialization completed successfully");
        } catch (Exception e) {
            logger.error("Error during database initialization", e);
            throw e;
        }
    }

    private void initializeRoles() {
        if (roleService.findAll().isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            roleService.save(adminRole);

            Role userRole = new Role("ROLE_USER");
            roleService.save(userRole);

            logger.info("Created default roles: ROLE_ADMIN, ROLE_USER");
        }
    }

    private void initializeUsers() {
        if (userService.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setEmail("admin@example.com");
            admin.setAge(30);
            admin.setRoles(Set.of(
                    roleService.findByName("ROLE_ADMIN"),
                    roleService.findByName("ROLE_USER")
            ));
            userService.save(admin);
            logger.info("Created admin user");
        }

        if (userService.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setEmail("user@example.com");
            user.setAge(25);
            user.setRoles(Set.of(
                    roleService.findByName("ROLE_USER")
            ));
            userService.save(user);
            logger.info("Created regular user");
        }
    }
}