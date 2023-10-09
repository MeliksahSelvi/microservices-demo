package com.melik.elastic.query.service.business;

import com.melik.elastic.query.service.dataaccess.entity.UserPermission;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

public interface QueryUserService {

    Optional<List<UserPermission>> findAllPermissionsByUsername(String username);
}
