package com.neu.nuboard.repository;

import com.neu.nuboard.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    /** method name conventions, implement by JPA */
    // find permission by name
    Optional<Permission> findByName(String name);

    // check if permission exists by name
    boolean existsByName(String name);

    // find permissions by name pattern
    List<Permission> findByNameContainingIgnoreCase(String namePattern);

    // find all permissions ordered by name
    List<Permission> findAllByOrderByNameAsc();

    /** custom query methods */
    // find all permissions that belong to a specific role (using role ID)
    @Query("SELECT DISTINCT p FROM Permission p JOIN p.rolePermissions rp WHERE rp.role.id = :roleId")
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);

    // find all permissions that belong to a specific role (using role name)
    @Query("SELECT DISTINCT p FROM Permission p JOIN p.rolePermissions rp WHERE rp.role.name = :roleName")
    List<Permission> findPermissionsByRoleName(@Param("roleName") String roleName);

    // find all permissions that are NOT assigned to a specific role (role ID)
    @Query("SELECT p FROM Permission p WHERE p.id NOT IN (SELECT rp.permission.id FROM RolePermission rp WHERE rp.role.id = :roleId)")
    List<Permission> findPermissionsNotInRole(@Param("roleId") Long roleId);
}
