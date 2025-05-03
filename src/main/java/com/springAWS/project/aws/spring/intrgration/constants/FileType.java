package com.springAWS.project.aws.spring.intrgration.constants;

import lombok.Getter;

@Getter
public enum FileType {

    PDF(1),
    PNG(2),
    JPG(3);

    private final int type;
    FileType(int type) {
        this.type = type;
    }
}
