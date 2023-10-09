package com.melik.elastic.query.service.security;

import com.melik.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

public class QueryServicePermissionEvaluator implements PermissionEvaluator {

    private static final String SUPER_USER_ROLE = "APP_SUPER_USER_ROLE";

    private final HttpServletRequest httpServletRequest;

    public QueryServicePermissionEvaluator(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomain, Object permission) {

        if (isSuperUser()) {
            return true;
        }

        if (targetDomain instanceof ElasticQueryServiceRequestModel) {
            return preAuthorize(authentication, ((ElasticQueryServiceRequestModel) targetDomain).getId(), permission);
        } else if (targetDomain instanceof ResponseEntity || targetDomain == null) {
            if (targetDomain == null) {
                return true;
            }
            ElasticQueryServiceAnalyticsResponseModel responseBody =
                    ((ResponseEntity<ElasticQueryServiceAnalyticsResponseModel>) targetDomain).getBody();
            Objects.requireNonNull(responseBody);
            return postAuthorize(authentication, responseBody.getElasticQueryServiceResponseModels(), permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (targetId == null) {
            return false;
        }
        return preAuthorize(authentication, (String) targetId, permission);
    }

    private boolean preAuthorize(Authentication authentication, String id, Object permission) {
        TwitterQueryUser twitterQueryUser = (TwitterQueryUser) authentication.getPrincipal();
        PermissionType userPermission = twitterQueryUser.getPermissions().get(id);
        return hasPermission((String) permission, userPermission);
    }

    private boolean hasPermission(String permission, PermissionType userPermission) {
        return userPermission != null && permission.equals(userPermission.getType());
    }

    private boolean postAuthorize(Authentication authentication, List<ElasticQueryServiceResponseModel> responseBody, Object permission) {
        TwitterQueryUser twitterQueryUser = (TwitterQueryUser) authentication.getPrincipal();
        for (ElasticQueryServiceResponseModel responseModel : responseBody) {
            PermissionType userPermission = twitterQueryUser.getPermissions().get(responseModel.getId());
            if (!hasPermission((String) permission, userPermission)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSuperUser() {
        return httpServletRequest.isUserInRole(SUPER_USER_ROLE);
    }
}
