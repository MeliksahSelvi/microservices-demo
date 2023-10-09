package com.melik.elastic.query.service.business.impl;

import com.melik.elastic.query.service.business.QueryUserService;
import com.melik.elastic.query.service.dataaccess.entity.UserPermission;
import com.melik.elastic.query.service.dataaccess.repository.UserPermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

@Service
public class TwitterQueryUserService implements QueryUserService {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterQueryUserService.class);

    private final UserPermissionRepository userPermissionRepository;

    public TwitterQueryUserService(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    @Override
    public Optional<List<UserPermission>> findAllPermissionsByUsername(String username) {
        LOG.info("Finding permissions by username {}", username);
        return userPermissionRepository.findUserPermissionsByUsername(username);
    }
}
