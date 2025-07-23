package com.neu.nuboard.Config;

import com.neu.nuboard.model.Permission;
import com.neu.nuboard.model.Role;
import com.neu.nuboard.repository.PermissionRepository;
import com.neu.nuboard.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring-managed component, registering to Bean
 * The combination of @Component and @Autowired means you can use the repositories without manually creating them with new.
 * NO constructor needed!
 * NO manual object creation!
 * Spring automatically provides fully configured instances
 */
@Component
public class DataInitializer {
    /**
     * final: immutable
     * Logger: An interface that defines logging methods like info(), debug(), warn(), error()
     * LoggerFactory: Factory class from SLF4J that creates Logger instances
     * Purpose: Creates a logger specifically for the DataInitializer class that can write log messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private EntityManager entityManager;

    @PostConstruct // Runs after dependency injection is complete
    @Transactional // wraps the entire method in a database transaction, all-or-nothing database operations
    public void init() {
        logger.info("Starting data initialization...");

        /*
         * Create total all 13 permissions
         */
        // Event related permissions
        Permission viewEvents = createPermissionIfNotFound("EVENT_VIEW", "View events");
        Permission createEvents = createPermissionIfNotFound("EVENT_CREATE", "Create events");
        Permission updateEvents = createPermissionIfNotFound("EVENT_UPDATE", "Update events");
        Permission deleteEvents = createPermissionIfNotFound("EVENT_DELETE", "Delete events");
        Permission registerEvents = createPermissionIfNotFound("EVENT_REGISTER", "Register for events");
        Permission unregisterEvents = createPermissionIfNotFound("EVENT_UNREGISTER", "Unregister from events");
        // User related permissions
        Permission viewUsers = createPermissionIfNotFound("USER_VIEW", "View all users");
        Permission updateUsers = createPermissionIfNotFound("USER_UPDATE", "Update users");
        Permission deleteUsers = createPermissionIfNotFound("USER_DELETE", "Delete users");
        // Registration related permissions
        Permission viewRegistrations = createPermissionIfNotFound("REGISTRATION_VIEW", "View event registrations");
        Permission manageRegistrations = createPermissionIfNotFound("REGISTRATION_MANAGE", "Manage all registrations");
        // Location and College related permissions
        Permission viewLocations = createPermissionIfNotFound("LOCATION_VIEW", "View locations");
        Permission viewColleges = createPermissionIfNotFound("COLLEGE_VIEW", "View colleges");

        // Create the USER role with basic 6 permissions
        Role userRole = createRoleIfNotFound("USER", "Basic user role");
        if (isRoleEmpty(userRole)) {
            logger.info("Assigning permissions to USER role");
            userRole.addPermission(createEvents);
            userRole.addPermission(viewEvents);
            userRole.addPermission(registerEvents);
            userRole.addPermission(unregisterEvents);
            userRole.addPermission(viewLocations);
            userRole.addPermission(viewColleges);
            roleRepository.save(userRole);
        }

        // Create the ADMIN role with all 13 permissions
        Role adminRole = createRoleIfNotFound("ADMIN", "Administrator role with full access");
        if (isRoleEmpty(adminRole)) {
            logger.info("Assigning permissions to ADMIN role");
            adminRole.addPermission(viewEvents);
            adminRole.addPermission(createEvents);
            adminRole.addPermission(updateEvents);
            adminRole.addPermission(deleteEvents);
            adminRole.addPermission(registerEvents);
            adminRole.addPermission(unregisterEvents);
            adminRole.addPermission(viewUsers);
            adminRole.addPermission(updateUsers);
            adminRole.addPermission(deleteUsers);
            adminRole.addPermission(viewRegistrations);
            adminRole.addPermission(manageRegistrations);
            adminRole.addPermission(viewLocations);
            adminRole.addPermission(viewColleges);
            roleRepository.save(adminRole);
        }

        logger.info("Data initialization completed successfully");
        logInitializedData();
    }

    /**
     * why need createPermissionIfNotFound and createRoleIfNotFound methods
     * prevent duplicates on application restarts
     * make initialization idempotent (safe to run multiple times)
     * First Application Startup (Empty Database): Call createPermissionIfNotFound("EVENT_VIEW", "View events")
     * Second Application Startup (Data Exists): Returns the existing Permission, NO duplicate created
     */
    private Permission createPermissionIfNotFound(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    logger.info("Creating new permission: {}", name); // name = EVENT_VIEW
                    // Permission permission = new Permission("EVENT_VIEW", "View events");
                    Permission permission = new Permission(name, description);
                    return permissionRepository.save(permission); // save to DB
                });
    }

    private Role createRoleIfNotFound(String name, String description) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    logger.info("Creating new role: {}", name);
                    Role role = new Role(name, description);
                    return roleRepository.save(role);
                });
    }

    // Use the native SQL query to avoid lazy loading issues
    // This method checks if a role has any permissions assigned to it.
    // preventing duplicate permission assignments when the app restarts.
    private boolean isRoleEmpty(Role role) {
        String sql = "SELECT COUNT(*) FROM role_permissions WHERE role_id = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, role.getId());
        Number count = (Number) query.getSingleResult();
        return count.intValue() == 0;
    }

    /**
     * === Initialization Summary ===
     * Total permissions created: 13
     * Total roles created: 2
     * Role 'USER' has 5 permissions
     * Role 'ADMIN' has 13 permissions
     * ============================
     */
    private void logInitializedData() {
        logger.info("=== Initialization Summary ===");

        long permissionCount = permissionRepository.count();
        logger.info("Total permissions created: {}", permissionCount);

        long roleCount = roleRepository.count();
        logger.info("Total roles created: {}", roleCount);

        // FIXED: Use native query to avoid lazy loading in logging
        roleRepository.findAll().forEach(role -> {
            String sql = "SELECT COUNT(*) FROM role_permissions WHERE role_id = ?";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, role.getId());
            Number count = (Number) query.getSingleResult();
            logger.info("Role '{}' has {} permissions", role.getName(), count.intValue());
        });

        logger.info("============================");
    }
}