package com.springAWS.project.aws.spring.intrgration.constants;

import lombok.Getter;

@Getter
public enum OperationType {

    UPLOAD(1),
    DOWNLOAD(2),
    DELETE(3);

    private final Integer operationType;

    OperationType(Integer operationType){
        this.operationType = operationType;
    }
}
