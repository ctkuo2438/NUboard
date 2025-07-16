package com.neu.nuboard.repository;

import com.neu.nuboard.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Method Name Conventions (Spring generates implementation)
     * spring automatically creates the implementation based on method names
     * spring analyzes method names and generates SQL
     */
    // find a role by name
    // "findBy" = SELECT query, "Name" = WHERE name column -> SELECT * FROM roles WHERE name = ?
    Optional<Role> findByName(String name);

    // check if a role exists by name
    // "existsBy" = EXISTS/COUNT query, "Name" = WHERE name column -> SELECT COUNT(*) > 0 FROM roles WHERE name = ?
    boolean existsByName(String name);

    // find roles by name pattern
    // SELECT * FROM roles WHERE UPPER(name) LIKE UPPER('%?%')
    List<Role> findByNameContainingIgnoreCase(String namePattern);

    // find all roles ordered by name
    List<Role> findAllByOrderByNameAsc();

    /**
     * custom Query methods
     */
    // find all roles that have a specific permission
    // SQL CODE:
    // SELECT DISTINCT r FROM Role r
    // JOIN r.rolePermissions rp
    // WHERE rp.permission.name = :permissionName
    @Query("SELECT DISTINCT r FROM Role r JOIN r.rolePermissions rp WHERE rp.permission.name = :permissionName")
    List<Role> findRolesByPermissionName(@Param("permissionName") String permissionName);

    // counts how many USERS have a specific ROLE (ADMIN, USER)
    @Query("SELECT COUNT(DISTINCT ur.user) FROM UserRole ur WHERE ur.role.name = :roleName")
    Long countUsersByRoleName(@Param("roleName") String roleName);
}
