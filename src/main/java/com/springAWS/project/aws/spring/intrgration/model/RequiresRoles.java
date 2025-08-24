package com.springAWS.project.aws.spring.intrgration.model;


import com.springAWS.project.aws.spring.intrgration.constants.UserType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRoles {
    UserType[] value();
}
