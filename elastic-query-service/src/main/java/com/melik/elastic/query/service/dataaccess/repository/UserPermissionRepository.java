package com.melik.elastic.query.service.dataaccess.repository;

import com.melik.elastic.query.service.dataaccess.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

public interface UserPermissionRepository extends JpaRepository<UserPermission, UUID> {

    @Query(nativeQuery = true, value = "Select p.user_permission_id as id,u.username,d.document_id,p.permissionType " +
            "from users u,user_permissions p,documents d " +
            "where u.id = p.user_id " +
            "and d.id = p.document_id " +
            "and u.username =:username")
    Optional<List<UserPermission>> findUserPermissionsByUsername(@Param("username") String username);
}

