package com.springAWS.project.aws.spring.intrgration.constants;

import lombok.Getter;

@Getter
public enum UserType {

    ADMIN(1),
    EDITOR(2),
    VIEWER(3);
    private final int userType;

    UserType(int userType){
        this.userType = userType;
    }
}
