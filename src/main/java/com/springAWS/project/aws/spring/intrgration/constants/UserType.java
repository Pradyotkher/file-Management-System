package com.springAWS.project.aws.spring.intrgration.constants;

import lombok.Getter;

@Getter
public enum UserType {

    ADMIN("ADMIN"),
    EDITOR("EDITOR"),
    VIEWER("VIEWER");
    private final String userType;

    UserType(String userType){
        this.userType = userType;
    }
}
