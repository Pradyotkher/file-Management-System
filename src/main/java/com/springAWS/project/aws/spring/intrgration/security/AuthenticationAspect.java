package com.springAWS.project.aws.spring.intrgration.security;


import com.springAWS.project.aws.spring.intrgration.constants.UserType;
import com.springAWS.project.aws.spring.intrgration.model.RequiresRoles;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import java.util.*;

@Aspect
@Component
@Slf4j
public class AuthenticationAspect {

    private UsernamePasswordAuthenticationToken getUserToken(ProceedingJoinPoint joinPoint){
        SecurityContextImpl context = (SecurityContextImpl) SecurityContextHolder.getContext();
        boolean isAuthenticated = context.getAuthentication().isAuthenticated();
        if(!isAuthenticated){
            log.error("Not Authenticated");
            throw new IllegalStateException("User is not authenticated");
        }
        return (UsernamePasswordAuthenticationToken) context.getAuthentication();
    }

    @Around("@annotation(requiresRoles)")
    public Object authenticateRoleForFileServices(ProceedingJoinPoint joinPoint , RequiresRoles requiresRoles) throws Throwable {
        UsernamePasswordAuthenticationToken userToken = getUserToken(joinPoint);
        UserType[] userTypes = requiresRoles.value();
        List<String> allowedRolesForFileUpload = Arrays.stream(userTypes).map(Enum::name).toList();
        boolean isRoleAuthorized = userToken.getAuthorities().stream().anyMatch(role -> allowedRolesForFileUpload.contains(role.getAuthority()));
        if(!isRoleAuthorized){
            throw new IllegalAccessException("Current User Role is not authorized to access");
        }

        log.info(allowedRolesForFileUpload.toString());
        return joinPoint.proceed();
    }

}
