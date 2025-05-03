package com.springAWS.project.aws.spring.intrgration.constants;

import lombok.Getter;

@Getter
public enum Messages {

    FILE_UPLOAD_SUCCESSFUL("File Successfully uploaded to AWS S3 server"),
    CANNOT_FIND_USER("Could not find User "),
    NO_USER_RIGHTS("does not have rights to upload the file to the server"),
    FILE_SAVED_TO_DOWNLOADS("File saved to Downloads folder successfully"),
    FILE_DELETE_SUCCESSFUL("File deleted from the AWS S3"),
    USER_ALREADY_EXISTS("User with this userName already exists"),
    EMPTY_FILE("Empty File , bad request"),
    DELETE_FILE_FAILED("Delete file as Transaction Failed");


    private final String message;

     Messages(String message){
        this.message = message;
    }

}
