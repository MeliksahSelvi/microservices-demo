package com.melik.elastic.query.service.transformer;

import com.melik.elastic.query.service.dataaccess.entity.UserPermission;
import com.melik.elastic.query.service.security.PermissionType;
import com.melik.elastic.query.service.security.TwitterQueryUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

@Component
public class UserPermissionsToUserDetailTransformer {

    public TwitterQueryUser getUserDetails(List<UserPermission> userPermissions) {
        return TwitterQueryUser.builder()
                .username(userPermissions.get(0).getUsername())
                .permissions(userPermissions.stream()
                        .collect(Collectors.toMap(
                                UserPermission::getDocumentId,
                                permission -> PermissionType.valueOf(permission.getPermissionType()))))
                .build();
    }
}
